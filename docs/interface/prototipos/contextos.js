// HISTÓRICO v0.2. Não incorporado no HTML v0.3; a sessão atual está em prototipo.js.
// Projeção de cenários fixos do storyboard. Não calcula AHP ou TOPSIS.
const fixtureData=JSON.parse(document.getElementById('fixtures').textContent);
const demoCategories={
 phone:{name:'Smartphones Premium',criteria:['Geekbench 6 Multi-Core','Preço','Câmeras','Autonomia','Armazenamento'],units:['pontos','R$','nota 1–10','h','GB'],benefits:[true,false,true,true,true],products:[{name:'Aurora Pro',origin:'Oficial',key:'oficial:aurora-pro',values:[8000,5000,8,16,256]},{name:'Vértice Max',origin:'Oficial',key:'oficial:vertice-max',values:[7000,6000,9,20,512]},{name:'Aurora Pro',origin:'Pessoal',key:'pessoal:aurora-pro-demo',values:[8000,5000,8,16,256]}]},
 bev:{name:'Veículos Elétricos (BEV)',criteria:['Autonomia Inmetro/PBEV','Preço de aquisição','Recarga DC 20–80%','Seguro anual','ADAS'],units:['km','R$','min','R$/ano','nota 1–10'],benefits:[true,false,false,false,true],products:[{name:'BEV Aurora · 2026',origin:'Oficial',key:'oficial:bev-aurora',values:[400,180000,30,5000,8]},{name:'BEV Boreal · 2026',origin:'Oficial',key:'oficial:bev-boreal',values:[420,190000,25,4800,9]},{name:'BEV Aurora · 2026',origin:'Pessoal',key:'pessoal:bev-aurora-demo',values:[400,180000,30,5000,8]}]}
};
function context(){return memory.context||{category:'phone',mask:3};}
function selectionId(c=context()){return c.category==='bev'?'T06-BEV':c.mask&4?'T06-MISTA':['T06-0','T06-A','T06-B','T06-AB'][c.mask];}
function category(c=context()){return demoCategories[c.category];}
function products(c=context()){return category(c).products.filter((p,i)=>c.mask&(1<<i));}
function fixture(c=context()){return fixtureData.cenarios.find(s=>s.id===c.category+'-'+c.mask);}
function plain(text,role='body'){return {type:'text',text,role};}
function message(text,tone='Info'){return {type:'state',text,tone};}
function command(label,to,extra={}){return {type:'button',label,to,...extra};}
function row(title,subtitle,to){return {type:'card',title,subtitle,to};}
function formatNumber(value){return value.toLocaleString('pt-BR',{maximumFractionDigits:4});}
function describe(p,c=context()){const d=category(c);return p.values.map((v,i)=>d.criteria[i]+': '+formatNumber(v)+' '+d.units[i]+' · '+(d.benefits[i]?'benefício':'custo')).join('\n');}
function resultBody(c,saved=false){
 const f=fixture(c),list=products(c),d=category(c);if(!f)return [message('Selecione pelo menos duas alternativas.','Aviso')];
 const body=[message(saved?'Avaliação salva neste dispositivo.':'Resultado calculado. Ainda não salvo.',saved?'Sucesso':'Aviso')];
 if(f.topsis.tipo==='sem-diferenciacao'){
  body.push(plain('Sem diferenciação entre as alternativas','title'),plain('Os valores efetivos são iguais. Não há vencedor, coeficiente ou posição definidos.'));
  f.topsis.alternativas.forEach(r=>body.push(row(list[r.alternativa].name+' · '+list[r.alternativa].origin,'d+ = 0 · d− = 0 · C* não definido','T12-DETALHES')));
 }else{
  const first=f.topsis.alternativas[0];const winners=f.topsis.alternativas.filter(r=>r.posicao===1);
  body.push(plain(winners.length===1?list[first.alternativa].name:'Empate na primeira posição','headline'));
  f.topsis.alternativas.forEach(r=>body.push(row(r.posicao+'º · '+list[r.alternativa].name+' · '+list[r.alternativa].origin,`C* ${r.coeficiente.toFixed(4)} · d+ ${r.dMais.toFixed(4)} · d− ${r.dMenos.toFixed(4)}`.replaceAll('.',','),'T12-DETALHES')));
 }
 body.push(plain(d.criteria.map(n=>n+' · 20%').join('\n')),plain('λmax 5 · IC 0 · RI 1,12 · RC 0'),message('Valores fictícios e pesos ilustrativos. Nenhuma consulta ao catálogo modifica esta avaliação.'));
 return body;
}
function projectScreen(id){
 const s=JSON.parse(JSON.stringify(screens.get(id)));if(!s)return s;
 const savedView=['T12-SALVA','T05-CONCLUIDA','T05-SEMDIF','E04-SALVA'].includes(id);
 const c=savedView&&memory.savedContext?memory.savedContext:context(),d=category(c),selected=products(c);
 if(['T06-0','T06-A','T06-B','T06-AB','T06-MISTA','T06-BEV'].includes(id)){
  const visible=memory.personalAdded[c.category]?3:2;
  s.title='Selecionar produtos';s.body=[plain('1 de 4 · Alternativas','label'),message('Categoria fixa: '+d.name),plain(selected.length+' de pelo menos 2 produtos selecionados')];
  d.products.slice(0,visible).forEach((p,i)=>s.body.push(row((c.mask&(1<<i)?'✓ ':'○ ')+p.name,p.origin+' · '+p.key,'@select-'+i)));
  s.body.push(command('Adicionar produto pessoal',c.category==='bev'?'T04-SELECAO-BEV':'T04-SELECAO',{newPersonal:true}),message('Origem + ID identificam cada registro. Nomes iguais não são unidos automaticamente.'));
  s.footer=[command('Conferir valores','T07',{disabled:selected.length<2}),command('Salvar rascunho','@draft',{secondary:true})];s.dirtyCards=false;
 }
 if(['T04-SELECAO','T04-SELECAO-BEV'].includes(id)){
  const p=d.products[2];s.body=[message('Categoria fixa: '+d.name),{type:'field',label:'Nome',value:p.name},...p.values.map((v,i)=>({type:'field',label:d.criteria[i]+' · '+d.units[i],value:formatNumber(v)})),message('Dados preenchidos de demonstração. Salvar adiciona ao catálogo pessoal simulado e retorna à seleção preservada.')];
  s.footer=[command('Salvar produto','@productSave'),command('Cancelar','D01-PRODUTO',{secondary:true})];s.back='D01-PRODUTO';
 }
 if(id==='D01-PRODUTO'&&memory.productReturn){s.footer=[command('Salvar','@productSave'),command('Descartar alterações','@productDiscard',{secondary:true}),command('Continuar editando','@close',{secondary:true})];}
 if(id==='T07'){
  s.body=[plain('2 de 4 · Confira os 5 critérios','label'),message('Categoria fixa: '+d.name+'\nAjustes valem apenas nesta avaliação.')];
  selected.forEach(p=>{s.body.push(plain(p.name+' · '+p.origin,'title'));p.values.forEach((v,i)=>s.body.push({type:'field',label:d.criteria[i]+' · '+d.units[i],value:formatNumber(v),helper:d.benefits[i]?'Benefício':'Custo',...(c.category==='phone'&&p.key==='oficial:aurora-pro'&&i===1?{helper:'Original: R$ 5.200,00 · Efetivo ajustado: R$ 5.000,00',to:'D04-VALOR'}:{})}));});
  s.body.push(message(c.category==='bev'?'Valores simulados; não são medições/certificações oficiais. Perfil do seguro e rubrica ADAS reais ainda pendentes.':'Originais preservados. Fonte/data do efetivo ajustado desconhecidas.'));
  s.back=selectionId(c);
 }
 if(id.startsWith('T08-')||id==='T09'){
  const original=demoCategories.phone.criteria;
  const pattern=new RegExp(original.map(x=>x.replace(/[.*+?^${}()|[\]\\]/g,'\\$&')).join('|'),'g');
  for(const item of s.body)if(item.text)item.text=item.text.replace(pattern,x=>d.criteria[original.indexOf(x)]);
  for(const item of s.body)if(item.type==='progress')item.count=id==='T09'?10:memory.answers;
 }
 if(id==='T10'){
  s.body=[plain('4 de 4 · Antes do TOPSIS','label'),message(selected.length+' alternativas válidas · 5 critérios\n'+d.name+'\nAHP consistente · RC = 0,0000','Sucesso'),...selected.map(p=>row(p.name+' · '+p.origin,describe(p,c),'T07')),plain('Pesos: 20% para cada critério.'),command('Revisar pesos e consistência','T09',{secondary:true}),message('O processamento usa estes dados preservados, sem atualização pelo catálogo.')];
 }
 if(['T12','T12-SALVA','E04','E04-SALVA'].includes(id))s.body=resultBody(c,id==='T12-SALVA'||id==='E04-SALVA');
 if(id==='T12-DETALHES'){
  const detailsContext=memory.viewSaved&&memory.savedContext?memory.savedContext:context();
  s.body=[plain(category(detailsContext).name+' · Valores preservados','label'),...products(detailsContext).map(p=>row(p.name+' · '+p.origin,describe(p,detailsContext))),plain(category(detailsContext).criteria.map(n=>n+' · 20%').join('\n')),plain('λmax 5 · IC 0 · RI 1,12 · RC 0'),message('Dados fictícios. Consultar não recalcula nem substitui valores pelo catálogo.'),command('Excluir avaliação','D03-AVALIACAO',{secondary:true})];
 }
 if(['T05-CONCLUIDA','T05-SEMDIF'].includes(id)&&memory.savedContext)s.body=[row('Comparação · '+d.name,memory.saved==='sem'?'Concluída — sem diferenciação':'Concluída com ranking','T12-SALVA'),message('Última gravação simulada confirmada.','Sucesso')];
 if(id==='T05-RASCUNHO'&&memory.savedContext)s.body=[row('Comparação · '+category(memory.savedContext).name,'Rascunho · Retomar última gravação','@resume'),message('Dados locais na demonstração; nenhuma gravação real no dispositivo.')];
 if(id==='T12-SALVA'){
  s.footer[0]={...s.footer[0],to:'T07',editSaved:true};
  s.footer[1].to=memory.saved==='sem'?'T13-SEMDIF':'T13';s.footer[2].to='T05';s.back='T05';
 }
 return s;
}
