from pathlib import Path
import json,subprocess,re
base=Path(__file__).resolve().parent
html=(base/'index.html').read_text()
script=r'''
<script>
(async()=>{
 const passed=[];const assert=(value,name)=>{if(!value)throw new Error(name);passed.push(name)};
 const click=label=>{const root=$('dialog').open?$('dialog'):document.querySelector('.phone');const button=[...root.querySelectorAll('button')].find(b=>b.textContent===label);if(!button)throw new Error('Controle ausente: '+label);button.click();};
 const choose=id=>{const s=$('scenario');s.value=id;s.dispatchEvent(new Event('change'));};
 try{
 assert(memory.current==='T01','Entrada no catálogo');click('Smartphones Premium5 critérios · desempenho, preço e mais');
 click('Iniciar avaliação');assert(memory.dirty&&!memory.saved,'Criação sem salvamento automático');
 const next=()=>[...$('footer').querySelectorAll('button')].find(b=>b.textContent==='Conferir valores');
 assert(next().disabled,'Zero alternativas bloqueia avanço');
 document.querySelector('button.card').click();assert(next().disabled,'Uma alternativa bloqueia avanço');
 [...document.querySelectorAll('button.card')][1].click();assert(!next().disabled,'Duas alternativas liberam conferência');
 click('Conferir valores');assert(memory.current==='T07','Valores da avaliação');click('Comparar critérios');
 for(let i=1;i<=10;i++){
  assert(memory.current===`T08-${String(i).padStart(2,'0')}-P`,'Par pendente '+i);
  assert($('footer').querySelector('button').disabled,'Par '+i+' não respondido bloqueia próximo');
  click('Igual importância · 1');click(i===10?'Validar AHP':'Próxima comparação');
 }
 assert(memory.current==='T09','Dez respostas liberam consistência');click('Revisar avaliação');click('Processar TOPSIS');
 await new Promise(r=>setTimeout(r,400));assert(memory.current==='T12'&&!memory.saved,'TOPSIS não salva automaticamente');
 click('Sair');assert($('dialog').open&&$('dialog').querySelectorAll('button').length===3,'DEC-025 oferece três opções');
 click('Continuar editando');assert(memory.current==='T12'&&memory.dirty,'Continuar mantém edição');
 memory.fail=true;click('Sair');click('Salvar');assert(!memory.saved&&memory.current==='T12'&&memory.dirty,'Falha ao salvar preserva edição e não sai');
 click('Sair');click('Salvar');assert(memory.saved==='ranking'&&!memory.dirty&&memory.current==='T05-CONCLUIDA','Salvar resultado e retornar às avaliações');
 document.querySelector('button.card').click();click('Editar avaliação');assert(memory.saved==='ranking'&&!memory.dirty,'Abrir edição mantém versão salva e não cria alterações');
 choose('T07-EDITAR');assert(memory.dirty,'Cenário de alteração invalida somente a edição');
 click('Exportar PDF');assert($('dialog').querySelector('button').disabled,'Edição desatualizada bloqueia Salvar e exportar');
 click('Exportar última versão salva');click('Cancelar compartilhamento');assert(memory.current==='T07-EDITAR'&&memory.dirty,'Exportar última salva mantém edição pendente');
 click('Sair');click('Descartar alterações');assert(memory.current==='T12-SALVA'&&memory.saved==='ranking'&&!memory.dirty,'Descartar restaura resultado salvo');
 $('reset').click();choose('T05-RASCUNHO');document.querySelector('button.card').click();assert(memory.current==='T08-04-P'&&!memory.dirty,'Retoma par 4 ainda pendente');
 click('Igual importância · 1');click('Sair');click('Descartar alterações');document.querySelector('button.card').click();assert(memory.current==='T08-04-P'&&!memory.dirty,'Descartar preserva rascunho anterior');
 click('Igual importância · 1');click('Salvar rascunho');document.querySelector('button.card').click();assert(memory.current==='T08-04-R'&&!memory.dirty,'Salvar rascunho preserva progresso');
 choose('E01');assert($('footer').querySelector('button').disabled,'RC inconsistente bloqueia TOPSIS');
 choose('E03');assert($('content').textContent.includes('1º, 2º, 2º, 4º'),'Empate com numeração competitiva');
 choose('E04');assert(!$('content').textContent.includes('1º'),'Sem diferenciação não inventa posição');click('Salvar avaliação');assert(memory.saved==='sem'&&memory.current==='T05-SEMDIF','Sem diferenciação pode ser salva');
 document.querySelector('button.card').click();click('Exportar PDF');assert(memory.current==='T13-SEMDIF','Sem diferenciação exportável no cenário simulado');
 // BEV completo: nenhum critério de smartphone pode aparecer neste percurso.
 $('reset').click();choose('T06-BEV');document.querySelector('button.card').click();[...document.querySelectorAll('button.card')][1].click();
 assert(memory.context.category==='bev'&&memory.context.mask===3,'Seleção BEV mantém categoria e duas alternativas');
 click('Conferir valores');assert($('content').textContent.includes('Seguro anual')&&!$('content').textContent.includes('Geekbench'),'Cinco critérios BEV e unidades próprios');
 click('Comparar critérios');
 for(let i=1;i<=10;i++){assert(!$('content').textContent.includes('Geekbench'),'Par BEV '+i+' não usa critério de smartphone');click('Igual importância · 1');click(i===10?'Validar AHP':'Próxima comparação');}
 click('Revisar avaliação');assert($('content').textContent.includes('R$/ano')&&$('content').textContent.includes('Recarga DC'),'Revisão BEV preserva seguro anual e recarga 20–80%');
 click('Processar TOPSIS');await new Promise(r=>setTimeout(r,400));assert(fixture().id==='bev-3'&&$('content').textContent.includes('BEV Boreal'),'Ranking BEV usa fixture da categoria');
 click('Salvar avaliação');click('Voltar às avaliações');document.querySelector('button.card').click();assert(memory.savedContext.category==='bev','Consulta salva preserva categoria BEV');
 // Cadastro durante seleção, falha de cadastro e seleção mista com nomes iguais.
 $('reset').click();show('T02');click('Iniciar avaliação');[...document.querySelectorAll('button.card')][1].click();
 click('Adicionar produto pessoal');assert(memory.productReturn.context.mask===2,'Cadastro conserva seleção anterior');
 memory.fail=true;click('Salvar produto');assert(!memory.personalAdded.phone&&memory.context.mask===2&&memory.current==='T04-SELECAO','Falha no cadastro preserva formulário e seleção');
 click('Salvar produto');assert(memory.personalAdded.phone&&memory.context.mask===2,'Salvar cadastro retorna à seleção sem selecionar automaticamente');
 [...document.querySelectorAll('button.card')][2].click();assert(memory.context.mask===6,'Oficial e pessoal podem coexistir');
 click('Conferir valores');click('Comparar critérios');for(let i=1;i<=10;i++){click('Igual importância · 1');click(i===10?'Validar AHP':'Próxima comparação');}
 click('Revisar avaliação');click('Processar TOPSIS');await new Promise(r=>setTimeout(r,400));assert(fixture().id==='phone-6'&&$('content').textContent.includes('Pessoal'),'Resultado misto usa valores das alternativas selecionadas');
 // Três alternativas e identidade distinta de dois registros de mesmo nome.
 show('T06-MISTA');[...document.querySelectorAll('button.card')][0].click();assert(memory.context.mask===7,'Registros oficial e pessoal com mesmo nome não são deduplicados');
 show('T12');assert(fixture().id==='phone-7'&&fixture().topsis.alternativas.map(r=>r.posicao).join(',')==='1,2,2','Ranking de três alternativas preserva empate entre os dois registros Aurora');
 // Discard da nova avaliação mantém o contexto da última salva, inclusive categoria.
 choose('T06-BEV');memory.saved='ranking';memory.savedContext={category:'phone',mask:6};memory.savedAnswers=10;click('Sair');click('Descartar alterações');assert(memory.savedContext.category==='phone'&&memory.context.category==='phone','Descartar nova categoria preserva última avaliação salva');
 const layouts=[];
 for(const width of [360,600])for(const scale of [1,2])for(const id of ['T01','T07','T08-01-P','T10','T12-SALVA','T06-MISTA','T04-SELECAO-BEV']){
  document.documentElement.style.setProperty('--width',width+'px');document.documentElement.style.setProperty('--scale',scale);show(id);
  const phone=document.querySelector('.phone').getBoundingClientRect();
  const nodes=[...document.querySelector('.phone').querySelectorAll('button,p,h2,input,progress')];
  const outside=nodes.filter(n=>{const r=n.getBoundingClientRect();return r.width&& (r.left<phone.left-.5||r.right>phone.right+.5)});
  assert(!outside.length,`Sem corte horizontal ${id}/${width}/${scale}`);
  const small=nodes.filter(n=>n.tagName==='BUTTON'&&!n.disabled&&n.getBoundingClientRect().height<48);
  assert(!small.length,`Alvos ≥48dp ${id}/${width}/${scale}`);
  layouts.push({id,width,scale});
 }
 document.documentElement.style.setProperty('--width','360px');document.documentElement.style.setProperty('--scale',1);show('T10');
 const report={status:'PASS',checks:passed.length,passed,layouts};
 const out=document.createElement('pre');out.id='validation-report';out.textContent=JSON.stringify(report);document.body.append(out);
 }catch(e){const out=document.createElement('pre');out.id='validation-report';out.textContent=JSON.stringify({status:'FAIL',error:String(e),passed});document.body.append(out);}
})();
</script>
'''
target=Path('/tmp/pdm-prototipo-teste.html');target.write_text(html.replace('</html>',script+'</html>'))
cmd=['/opt/google/chrome/chrome','--headless','--no-sandbox','--disable-gpu','--disable-dev-shm-usage','--disable-background-networking','--no-first-run','--user-data-dir=/tmp/pdm-chrome-verificacao','--virtual-time-budget=5000','--window-size=1440,1200','--dump-dom',target.as_uri()]
r=subprocess.run(cmd,capture_output=True,text=True,timeout=45)
Path('/tmp/pdm-teste-dom.html').write_text(r.stdout)
match=re.search(r'<pre id="validation-report">(.*?)</pre>',r.stdout,re.S)
if not match: print(r.stderr[-3000:]);raise SystemExit('Navegador não produziu relatório')
import html as html_module
report=json.loads(html_module.unescape(match[1]));print(json.dumps(report,ensure_ascii=False,indent=2))
Path('/tmp/pdm-validacao-interacoes.json').write_text(json.dumps(report,ensure_ascii=False,indent=2)+'\n')
if report['status']!='PASS':raise SystemExit(1)
