// Injetar FOUNDATIONS, SPEC e RANGE [início,fim] antes deste script.
// Reexecutar uma faixa já criada exige inspeção; nenhuma tela é sobrescrita.
await Promise.all(['Regular','Medium','Bold'].map(style=>figma.loadFontAsync({family:'Roboto',style})));
let page=figma.root.children.find(p=>p.name==='01 · Protótipo');
if(!page) {page=figma.root.children.find(p=>p.id==='0:1' && p.children.length===0) || figma.createPage();page.name='01 · Protótipo';}
await figma.setCurrentPageAsync(page);
const made=[], vars={}, styles={}, library={};
for(const [k,id] of Object.entries(FOUNDATIONS.variables)) vars[k]=await figma.variables.getVariableByIdAsync(id);
const styleKeys={body:'4aeaa89a2f980c69d11b84a0e2a4f1268119c277',small:'4ee182c0ddb06633c3f4beacffdc9c5306924b51',medium:'1e92164361231ef1ed79f8fd256e78e0f6fbf250',title:'6d152883d5f7e827add2e91513fd9a90bf762baa',headline:'6bbad1b27fe05ecdb1858533b6a345777d20d506',label:'5afceec95267887a6a779afde1b75400180752f8'};
await Promise.all(Object.entries(styleKeys).map(async([k,key])=>{styles[k]=await figma.importStyleByKeyAsync(key);}));
await Promise.all(Object.entries(FOUNDATIONS.imports).map(async([k,key])=>{library[k]=await figma.importComponentSetByKeyAsync(key);}));
const paint=name=>figma.variables.setBoundVariableForPaint({type:'SOLID',color:{r:0,g:0,b:0}},'color',vars[name]);
function add(parent,node) {parent.appendChild(node);made.push(node.id);return node;}
function stack(parent,name,w,gap=12) {
  const f=add(parent,figma.createAutoLayout('VERTICAL'));f.name=name;f.fills=[];
  f.resize(w,10);f.primaryAxisSizingMode='AUTO';f.setBoundVariable('itemSpacing',vars['space'+gap]);return f;
}
function text(parent,value,role,w,scale=1,ink='onSurface') {
  const t=add(parent,figma.createText());t.name=value.slice(0,80);t.characters=value;
  t.textStyleId=styles[role||'body'].id; t.fills=[paint(ink)];
  if(scale!==1) {t.fontSize=Number(t.fontSize)*scale;if(t.lineHeight.unit==='PIXELS')t.lineHeight={unit:'PIXELS',value:t.lineHeight.value*scale};}
  t.textAutoResize='HEIGHT'; t.resize(w,Math.max(1,t.height));return t;
}
function variant(key,needle){return library[key].children.find(c=>c.name===needle)||library[key].defaultVariant;}
function props(instance,set,map){
 const updates={};for(const [prefix,value] of Object.entries(map)){
  const key=Object.keys(set.componentPropertyDefinitions).find(k=>k.startsWith(prefix));if(key)updates[key]=value;
 }instance.setProperties(updates);
}
function scaleText(node,scale){if(scale===1)return; for(const t of node.findAllWithCriteria({types:['TEXT']})){
  if(typeof t.fontSize==='number')t.fontSize*=scale;
  if(typeof t.lineHeight==='object'&&t.lineHeight.unit==='PIXELS')t.lineHeight={unit:'PIXELS',value:t.lineHeight.value*scale};
 }}
function button(parent,def,w,scale=1){
 const key=def.secondary?'outline':'button';
 const c=variant(key,'Type=Round, Size=Medium, State='+(def.disabled?'Disabled':'Enabled'));
 const i=add(parent,c.createInstance());i.name='AÇÃO · '+def.label;
 props(i,library[key],{'Label text':def.label,'Show icon':false});
 i.primaryAxisSizingMode='FIXED';i.counterAxisSizingMode='FIXED';
 i.resize(w,Math.max(56,56*scale));
 for(const f of i.findAllWithCriteria({types:['FRAME']})) {
  if(f.name==='Content'||f.name==='State-layer')f.layoutSizingHorizontal='FILL';
  if(f.name==='State-layer' && w<100){f.paddingLeft=8;f.paddingRight=8;}
 }
 for(const t of i.findAllWithCriteria({types:['TEXT']})) {t.textAutoResize='HEIGHT';t.resize(Math.max(20,w-48),t.height);t.layoutSizingHorizontal='FILL';t.textAlignHorizontal='CENTER';}
 scaleText(i,scale); return i;
}
function status(parent,msg,tone,w,scale){
 const cId=FOUNDATIONS.components[tone||'Info'];
 // Instâncias de mensagens locais preservam vínculo com o componente mestre.
 return figma.getNodeByIdAsync(cId).then(c=>{
 const i=add(parent,c.createInstance());i.name='ESTADO · '+tone;
 const prop=Object.keys(c.componentPropertyDefinitions).find(k=>k.startsWith('Mensagem'));
 i.setProperties({[prop]:msg});i.resize(w,i.height);
 const t=i.findOne(n=>n.type==='TEXT');t.resize(w-32,t.height);t.textAutoResize='HEIGHT';
 scaleText(i,scale);return i;
 });
}
const rendered=[];
for(let index=RANGE[0];index<Math.min(RANGE[1],SPEC.screens.length);index++){
 const spec=SPEC.screens[index];
 if(page.children.some(n=>n.name.startsWith(spec.id+' · ')))throw new Error('Tela já existe: '+spec.id);
 const scale=spec.fontScale||1, w=spec.width||360, inner=w-32;
 const f=stack(page,spec.id+' · '+spec.title,w,0);f.resize(w,spec.modal?500:800);f.primaryAxisSizingMode='FIXED';
 f.fills=[paint('surface')];f.clipsContent=true;
 const canvasIndex=spec.canvasIndex===undefined?index:spec.canvasIndex;
 f.x=160+(canvasIndex%8)*680;f.y=120+Math.floor(canvasIndex/8)*1060;
 const interactions=[];
 if(spec.modal){
  f.resize(328,100);f.primaryAxisSizingMode='AUTO';f.setBoundVariable('cornerRadius',vars.radius28);
  for(const side of ['Top','Bottom','Left','Right'])f.setBoundVariable('padding'+side,vars.space24);
  f.setBoundVariable('itemSpacing',vars.space16);
  const dialog=add(f,variant('dialog','Icon=False').createInstance());dialog.name='M3/Diálogo';
  props(dialog,library.dialog,{'Title':spec.title,'Supporting text':spec.body[0].text});
  const actions=dialog.findOne(n=>n.name==='Actions');if(actions)actions.visible=false;
  dialog.resize(280,dialog.height);
  for(const t of dialog.findAllWithCriteria({types:['TEXT']})) {t.textAutoResize='HEIGHT';t.resize(232,t.height);}
  for(const def of spec.footer){const b=button(f,def,280);interactions.push({id:b.id,...def});}
 }else{
  const system=stack(f,'Android · barre de status',w,0);system.setBoundVariable('paddingLeft',vars.space16);system.setBoundVariable('paddingTop',vars.space8);system.setBoundVariable('paddingBottom',vars.space8);
  text(system,'9:41   ·   Protótipo de demonstração','small',inner,1,'secondary');
  const top=add(f,figma.createAutoLayout('HORIZONTAL'));top.name='Barra superior';top.fills=[];top.resize(w,64);top.primaryAxisSizingMode='AUTO';top.setBoundVariable('itemSpacing',vars.space8);top.setBoundVariable('paddingLeft',vars.space8);top.setBoundVariable('paddingRight',vars.space8);
  if(spec.back){const b=button(top,{label:'‹',secondary:true},48);b.name='Voltar';interactions.push({id:b.id,to:spec.back,back:true});}
  const titleW=w-(spec.back?64:16)-(spec.exit?64:16);
  const title=text(top,spec.title,'title',titleW,scale);title.layoutSizingHorizontal='FILL';
  if(spec.exit){const b=button(top,{label:'Sair',secondary:true},56);interactions.push({id:b.id,to:spec.exitTo||'@exit'});}
  const view=stack(f,'Conteúdo rolável',w,0);view.primaryAxisSizingMode='FIXED';view.layoutSizingVertical='FILL';view.clipsContent=true;view.overflowDirection='VERTICAL';
  const content=stack(view,'Conteúdo',w,16);for(const side of ['Top','Bottom','Left','Right'])content.setBoundVariable('padding'+side,vars.space16);
  text(content,'DEMONSTRAÇÃO · DADOS FICTÍCIOS','small',inner,scale,'primary');
  if(spec.edit){const s=await status(content,'Alterações não salvas','Aviso',inner,scale);s.name='Persistência da edição';}
  for(const def of spec.body){
   if(def.type==='text')text(content,def.text,def.role||'body',inner,scale);
   else if(def.type==='state')await status(content,def.text,def.tone,inner,scale);
   else if(def.type==='button'){const b=button(content,def,inner,scale);interactions.push({id:b.id,...def});}
   else if(def.type==='card'){
    const card=add(content,variant('card','Style=Outlined, Layout=Media & text').createInstance());card.name='CARD · '+def.title;
    props(card,library.card,{'Header text':def.title,'Subhead text':def.subtitle});
    for(const name of ['Avatar','Media']){const n=card.findOne(n=>n.name===name);if(n)n.visible=false;}
    card.resize(inner,100*scale);
    const tf=card.findOne(n=>n.name==='Text'&&n.type==='FRAME');if(tf){tf.resize(inner-32,tf.height);tf.layoutSizingHorizontal='FILL';}
    for(const t of card.findAllWithCriteria({types:['TEXT']})){t.textAutoResize='HEIGHT';t.resize(inner-32,t.height);}
    scaleText(card,scale);interactions.push({id:card.id,to:def.to,change:!!spec.dirtyCards});
   }else if(def.type==='field'){
    const wrap=stack(content,'CAMPO · '+def.label,inner,4);
    const c=variant('field','Style=Outlined, State='+(def.error?'Error':'Enabled')+', Text configurations=Input text, Leading icon=False, Trailing icon=False');
    const input=add(wrap,c.createInstance());input.name='M3/Campo · '+def.label;
    props(input,library.field,{'Label text':def.label,'Input text':def.value||'—','Show supporting text':false});
    input.resize(inner,56*scale);scaleText(input,scale);
    if(def.helper)text(wrap,def.helper,'medium',inner,scale,def.error?'error':'secondary');
    if(def.to)interactions.push({id:input.id,to:def.to,change:true});
   }else if(def.type==='progress'){
    text(content,def.count+' de '+def.total+' comparações respondidas','medium',inner,scale,'primary');
    const percent=Math.round(def.count/def.total*100);
    const options=[0,10,20,50,80,100];const nearest=options.reduce((a,b)=>Math.abs(b-percent)<Math.abs(a-percent)?b:a);
    const bar=add(content,variant('progress','Type=Flat, Thickness=4 dp, Progress='+nearest).createInstance());bar.name='M3/Progresso · '+percent+'%';bar.resize(inner,12);
    // Texto mantém contagem exata; a largura dos segmentos visuais é corrigida na revisão.
   }
  }
  if(spec.footer.length){const footer=stack(f,'Ações da etapa',w,8);for(const side of ['Top','Bottom','Left','Right'])footer.setBoundVariable('padding'+side,vars.space16);footer.fills=[paint('surface')];
   for(const def of spec.footer){const b=button(footer,def,inner,scale);interactions.push({id:b.id,...def});}
  }
  if(spec.nav){const nav=add(f,figma.createAutoLayout('HORIZONTAL'));nav.name='Navegação principal';nav.resize(w,72);nav.fills=[paint('surfaceContainer')];nav.setBoundVariable('paddingLeft',vars.space16);nav.setBoundVariable('paddingRight',vars.space16);nav.setBoundVariable('paddingTop',vars.space8);nav.setBoundVariable('paddingBottom',vars.space8);nav.setBoundVariable('itemSpacing',vars.space8);
   for(const [label,to] of [['Catálogo','T01'],['Avaliações','T05']]){const b=button(nav,{label,secondary:true},(inner-8)/2);interactions.push({id:b.id,to});}
  }
 }
 rendered.push({id:spec.id,nodeId:f.id,index:canvasIndex,width:f.width,height:f.height,interactions});
}
return {pageId:page.id,createdNodeIds:made,frames:rendered};
