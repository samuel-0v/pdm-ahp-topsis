// Executar com use_figma apenas em arquivo vazio; não é código do aplicativo.
// Usa o helper createVariableCollection da skill figma-generate-library.
async function createVariableCollection(name, modeNames) {
  const collection = figma.variables.createVariableCollection(name);
  const modeIds = {};
  collection.renameMode(collection.modes[0].modeId, modeNames[0]);
  modeIds[modeNames[0]] = collection.modes[0].modeId;
  return { collection, modeIds };
}
if (figma.root.children.some(p => p.name === '02 · Componentes')) {
  throw new Error('Fundações já existem. Inspecionar antes de repetir.');
}
await Promise.all(['Regular', 'Medium', 'Bold'].map(style => figma.loadFontAsync({family:'Roboto',style})));
const page = figma.createPage(); page.name = '02 · Componentes';
await figma.setCurrentPageAsync(page);
const created = [page.id];
const {collection,modeIds} = await createVariableCollection('PDM · Proposta clara', ['Claro']);
const mode = modeIds.Claro;
const color = h => ({r:parseInt(h.slice(0,2),16)/255,g:parseInt(h.slice(2,4),16)/255,b:parseInt(h.slice(4,6),16)/255});
const vars = {};
const palette = {primary:'65558F',onPrimary:'FFFFFF',primaryContainer:'EADDFF',onPrimaryContainer:'21005D',surface:'FDF7FF',surfaceContainer:'F3EDF7',onSurface:'1D1B20',secondary:'49454F',outline:'79747E',success:'245D3C',successContainer:'D6F5DE',error:'B3261E',errorContainer:'F9DEDC',warning:'704B00',warningContainer:'FFF0C2'};
for(const [name,hex] of Object.entries(palette)) {
  const primitive=figma.variables.createVariable('base/'+name,collection,'COLOR');
  primitive.scopes=[]; primitive.setValueForMode(mode,color(hex));
  const v=figma.variables.createVariable('color/'+name,collection,'COLOR');
  v.scopes=['FRAME_FILL','SHAPE_FILL','TEXT_FILL','STROKE_COLOR'];
  v.setValueForMode(mode,{type:'VARIABLE_ALIAS',id:primitive.id});
  v.setVariableCodeSyntax('WEB','var(--pdm-'+name+')');
  v.setVariableCodeSyntax('ANDROID','PdmTheme.'+name);
  vars[name]=v;
}
for(const n of [4,8,12,16,24,32,48]) {
  const v=figma.variables.createVariable('space/'+n,collection,'FLOAT'); v.scopes=['GAP'];
  v.setValueForMode(mode,n); v.setVariableCodeSyntax('WEB','var(--pdm-space-'+n+')');
  v.setVariableCodeSyntax('ANDROID',n+'.dp'); vars['space'+n]=v;
}
for(const n of [12,16,28]) {
  const v=figma.variables.createVariable('radius/'+n,collection,'FLOAT'); v.scopes=['CORNER_RADIUS'];
  v.setValueForMode(mode,n); v.setVariableCodeSyntax('WEB','var(--pdm-radius-'+n+')');
  v.setVariableCodeSyntax('ANDROID',n+'.dp'); vars['radius'+n]=v;
}
const styleKeys={body:'4aeaa89a2f980c69d11b84a0e2a4f1268119c277',small:'4ee182c0ddb06633c3f4beacffdc9c5306924b51',medium:'1e92164361231ef1ed79f8fd256e78e0f6fbf250',title:'6d152883d5f7e827add2e91513fd9a90bf762baa',headline:'6bbad1b27fe05ecdb1858533b6a345777d20d506',label:'5afceec95267887a6a779afde1b75400180752f8'};
const styles={}; for(const [name,key] of Object.entries(styleKeys)) styles[name]=await figma.importStyleByKeyAsync(key);
const paint=name=>figma.variables.setBoundVariableForPaint({type:'SOLID',color:color(palette[name])},'color',vars[name]);
function text(parent,value,role='body',ink='onSurface',width=296) {
  const t=figma.createText(); parent.appendChild(t); created.push(t.id);
  t.characters=value; t.textStyleId=styles[role].id; t.fills=[paint(ink)];
  t.textAutoResize='HEIGHT'; t.resize(width,t.height); return t;
}
function stack(parent,name,width=328) {
  const f=figma.createAutoLayout('VERTICAL'); parent.appendChild(f); created.push(f.id);
  f.name=name; f.fills=[]; f.resize(width,100); f.primaryAxisSizingMode='AUTO';
  f.setBoundVariable('itemSpacing',vars.space12); return f;
}
const cover=stack(page,'Fundações · propostas para revisão',720); cover.x=160;cover.y=80;
text(cover,'PDM AHP-TOPSIS','headline','primary',720);
text(cover,'Android · Material 3 · Proposta inicial · 06/11/2026','body','secondary',720);
text(cover,'Roboto • 32/40 · 22/28 · 16/24 · 14/20 · 12/16\nEspaços 4, 8, 12, 16, 24 e 32. Alvos de toque ≥ 48 dp.','body','onSurface',720);
for(const [name,hex] of Object.entries(palette)) {
  const row=stack(cover,name+' #'+hex,720);row.fills=[paint(name)];
  row.setBoundVariable('paddingTop',vars.space12); row.setBoundVariable('paddingBottom',vars.space12);
  row.setBoundVariable('paddingLeft',vars.space16);
  const ink=['primary','onSurface','secondary','success','error','warning','onPrimaryContainer','outline'].includes(name)?'onPrimary':'onSurface';
  text(row,name+' · #'+hex,'medium',ink,680);
}
const components={}; const variants=[];
for(const [index,state] of ['Info','Sucesso','Erro','Aviso'].entries()) {
  const c=figma.createComponent(); page.appendChild(c);created.push(c.id);
  c.name='Tom='+state; c.layoutMode='VERTICAL'; c.resize(328,80); c.primaryAxisSizingMode='AUTO';
  c.setBoundVariable('paddingTop',vars.space12); c.setBoundVariable('paddingBottom',vars.space12);
  c.setBoundVariable('paddingLeft',vars.space16); c.setBoundVariable('paddingRight',vars.space16);
  c.setBoundVariable('itemSpacing',vars.space4); c.setBoundVariable('cornerRadius',vars.radius12);
  const bg=['surfaceContainer','successContainer','errorContainer','warningContainer'][index];
  const ink=['secondary','success','error','warning'][index]; c.fills=[paint(bg)];
  const msg=text(c,['Informação','✓ Operação concluída','! Não foi possível concluir','! Confira os dados'][index],'medium',ink);
  const prop=c.addComponentProperty('Mensagem','TEXT',msg.characters); msg.componentPropertyReferences={characters:prop};
  c.description='Mensagem de estado com texto além da cor. Sem confirmação implícita de persistência.';
  variants.push(c); components[state]=c.id;
}
const set=figma.combineAsVariants(variants,page);set.name='PDM/Estado';created.push(set.id);
set.x=1040;set.y=80;set.resize(720,300);
variants.forEach((c,i)=>{c.x=(i%2)*352;c.y=Math.floor(i/2)*140;});
const showcase=stack(page,'Componentes Material 3 em uso',328);showcase.x=1040;showcase.y=460;
const imports={button:'ab924dce8b851fd820bd0d56da24a9c489311ae6',outline:'dd2040219668454df5aa170434636aa823624c1a',field:'a54980c1c99c62912ab39e9e30fe5c267c98edb4',dialog:'8171ce19b4b8b7a873e91c719b7fb0fdc8a23794',card:'78d3f1e228edbedd6296fda77df704a36224f12b',progress:'2f02f075b410db543e8e9770cb399c14c583110e'};
for(const [name,key] of Object.entries(imports)) {
  const s=await figma.importComponentSetByKeyAsync(key);
  const c=s.children.find(c=>name==='button'||name==='outline'?c.name==='Type=Round, Size=Medium, State=Enabled':name==='field'?c.name==='Style=Outlined, State=Enabled, Text configurations=Input text, Leading icon=False, Trailing icon=False':name==='card'?c.name==='Style=Outlined, Layout=Media & text':name==='progress'?c.name==='Type=Flat, Thickness=4 dp, Progress=50':c.name==='Icon=False') || s.defaultVariant;
  const i=c.createInstance();showcase.appendChild(i); created.push(i.id); i.name='M3/'+name;
  const props={}; const defs=s.componentPropertyDefinitions;
  for(const [k,v] of Object.entries(defs)) {
    if(v.type==='BOOLEAN'&&k.startsWith('Show icon'))props[k]=false;
    if(v.type==='TEXT') props[k]=k.startsWith('Label text')?'Ação principal':k.startsWith('Input text')?'5.000,00':k.startsWith('Supporting text')?'Mensagem de apoio':k.startsWith('Header text')?'Produto de demonstração':k.startsWith('Subhead text')?'Origem e referência':k.startsWith('Title')?'Confirmação':v.defaultValue;
  }
  i.setProperties(props);
}
return {createdNodeIds:created,collectionId:collection.id,pageId:page.id,variables:Object.fromEntries(Object.entries(vars).map(([k,v])=>[k,v.id])),styles:Object.fromEntries(Object.entries(styles).map(([k,v])=>[k,v.id])),components,variantCount:variants.length,imports};
