// PREPARADO, NÃO EXECUTADO. Requer LEDGER e SPEC injetados.
// Revisar o canvas antes de aplicar; os IDs abaixo pertencem apenas a esta execução.
const page=await figma.getNodeByIdAsync('0:1');await figma.setCurrentPageAsync(page);
const changed=[],created=[],removed=[];
for(const entry of LEDGER.frames){
 const frame=await figma.getNodeByIdAsync(entry.nodeId);if(!frame)throw new Error('Frame ausente: '+entry.id);
 const spec=SPEC.screens.find(s=>s.id===entry.id);if(spec.modal)continue;
 const textNodes=frame.findAllWithCriteria({types:['TEXT']});
 for(const t of textNodes)for(const segment of t.getStyledTextSegments(['fontName']))await figma.loadFontAsync(segment.fontName);
 const header=frame.findOne(n=>n.name==='Barra superior');
 if(header){
  header.primaryAxisSizingMode='FIXED';header.counterAxisSizingMode='AUTO';header.resize(frame.width,56);changed.push(header.id);
  const title=header.children.find(n=>n.type==='TEXT');
  const buttons=header.children.filter(n=>n.type==='INSTANCE');
  const width=frame.width-16-buttons.reduce((sum,b)=>sum+b.width,0)-8*buttons.length;
  if(title){title.layoutSizingHorizontal='FIXED';title.textAutoResize='HEIGHT';title.resize(width,title.height);changed.push(title.id);}
 }
 for(const i of frame.findAllWithCriteria({types:['INSTANCE']}).filter(n=>n.name.startsWith('AÇÃO')||n.name==='Voltar')){
  const width=i.width,scale=spec.fontScale||1;i.primaryAxisSizingMode='FIXED';i.counterAxisSizingMode='FIXED';i.resize(width,56*scale);changed.push(i.id);
  for(const child of i.findAllWithCriteria({types:['FRAME']})){
   if(child.name==='Content'||child.name==='State-layer'){child.layoutSizingHorizontal='FILL';changed.push(child.id);}
   if(child.name==='State-layer'&&width<100){child.paddingLeft=8;child.paddingRight=8;changed.push(child.id);}
  }
  for(const t of i.findAllWithCriteria({types:['TEXT']})){t.textAutoResize='HEIGHT';t.resize(Math.max(16,width-(width<100?16:48)),t.height);t.layoutSizingHorizontal='FILL';t.textAlignHorizontal='CENTER';changed.push(t.id);}
 }
 // Evitar exclusão de avaliação nunca salva no detalhe compartilhado.
 if(entry.id==='T12-DETALHES'){
  const del=frame.findOne(n=>n.name==='AÇÃO · Excluir avaliação');
  if(del){
   del.visible=false;changed.push(del.id);
   const saved=await figma.getNodeByIdAsync(LEDGER.frames.find(f=>f.id==='T12-SALVA').nodeId);
   const content=saved.findOne(n=>n.name==='Conteúdo');
   if(!content.findOne(n=>n.name==='AÇÃO · Excluir avaliação')){
    const copy=del.clone();content.appendChild(copy);copy.visible=true;created.push(copy.id);
    const destination=LEDGER.frames.find(f=>f.id==='D03-AVALIACAO').nodeId;
    await copy.setReactionsAsync([{trigger:{type:'ON_CLICK'},actions:[{type:'NODE',destinationId:destination,navigation:'OVERLAY',transition:null,overlayRelativePosition:{x:16,y:120}}]}]);
   }
  }
 }
}
// Progresso exato: substituir as aproximações de variantes remotas por instâncias locais.
const primary=await figma.variables.getVariableByIdAsync(LEDGER.foundations.variables.primary);
const container=await figma.variables.getVariableByIdAsync(LEDGER.foundations.variables.primaryContainer);
function paint(variable){return figma.variables.setBoundVariableForPaint({type:'SOLID',color:{r:0,g:0,b:0}},'color',variable);}
for(const entry of LEDGER.frames){
 const frame=await figma.getNodeByIdAsync(entry.nodeId);
 const old=frame.findOne(n=>n.type==='INSTANCE'&&n.name.startsWith('M3/Progresso · '));if(!old)continue;
 const count=Number(old.name.match(/(\d+)%/)[1])/10;
 const name='PDM/Progresso exato/'+count;
 let master=page.children.find(n=>n.type==='COMPONENT'&&n.name===name);
 if(!master){
  master=figma.createComponent();master.name=name;master.resize(328,12);master.fills=[];master.x=6200;master.y=100+count*48;created.push(master.id);
  master.description=count+' de 10 comparações respondidas; usar em conjunto com contagem textual.';
  const track=figma.createRectangle();master.appendChild(track);track.resize(328,4);track.y=4;track.fills=[paint(container)];track.constraints={horizontal:'SCALE',vertical:'CENTER'};created.push(track.id);
  if(count>0){const fill=figma.createRectangle();master.appendChild(fill);fill.resize(328*count/10,4);fill.y=4;fill.fills=[paint(primary)];fill.constraints={horizontal:'SCALE',vertical:'CENTER'};created.push(fill.id);}
 }
 const parent=old.parent,index=parent.children.indexOf(old),replacement=master.createInstance();parent.insertChild(index,replacement);replacement.resize(old.width,12);replacement.name=name;created.push(replacement.id);removed.push(old.id);old.remove();
}
return {mutatedNodeIds:[...new Set(changed)],createdNodeIds:created,removedNodeIds:removed,requiresScreenshotReview:true};
