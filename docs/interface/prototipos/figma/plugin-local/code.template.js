// Fonte de plugin de desenvolvimento local, não generativo. Sem comunicação de rede.
const LEDGER=__LEDGER__;
const SPEC=__SPEC__;
let running=false;
async function layout(){
__LAYOUT__
}
async function navigation(start,end){
 const RANGE=[start,end];
__NAVIGATION__
}
async function inspect(){
 if(figma.editorType!=='figma')throw new Error('Abra o arquivo Figma Design da Etapa 5.');
 if(figma.fileKey&&figma.fileKey!==LEDGER.fileKey)throw new Error('Arquivo diferente do autorizado. Nenhuma alteração aplicada.');
 const page=await figma.getNodeByIdAsync('0:1');
 if(!page||page.type!=='PAGE'||page.name!=='01 · Protótipo')throw new Error('Página do projeto não encontrada.');
 await figma.setCurrentPageAsync(page);
 const names=new Map(SPEC.screens.map(s=>[s.id,s]));
 for(const f of LEDGER.frames){
  const node=await figma.getNodeByIdAsync(f.nodeId);
  if(!node||node.type!=='FRAME'||node.name!==f.id+' · '+names.get(f.id).title)throw new Error('Frame divergente: '+f.id+'. Inspecione o arquivo antes de continuar.');
  for(const item of f.interactions){
   const control=await figma.getNodeByIdAsync(item.id);
   if(!control)throw new Error('Controle ausente: '+item.id+'. Nenhuma alteração aplicada.');
  }
 }
 return {fileKey:LEDGER.fileKey,frames:LEDGER.frames.length,page:page.id,stage:'inspecionado'};
}
function send(message){figma.ui.postMessage(message);}
async function audit(){
 const frames=[];let reactions=0;
 function destinations(actions){return actions.flatMap(a=>a.type==='NODE'?[a.destinationId]:a.type==='CONDITIONAL'?a.conditionalBlocks.flatMap(b=>destinations(b.actions)):[]);}
 for(const f of LEDGER.frames){
  const node=await figma.getNodeByIdAsync(f.nodeId);let count=0;
  for(const control of f.interactions){
   if(!control.to||control.disabled)continue;
   const n=await figma.getNodeByIdAsync(control.id);
   const r=n.reactions.filter(r=>r.trigger&&r.trigger.type==='ON_CLICK');
   if(!r.length)throw new Error('Conexão não aplicada: '+f.id+'/'+control.id);
   for(const reaction of r)for(const id of destinations(reaction.actions||[]))if(id&&!await figma.getNodeByIdAsync(id))throw new Error('Destino ausente: '+id);
   count++;
  }
  reactions+=count;frames.push({id:f.id,nodeId:f.nodeId,controls:count,width:node.width,height:node.height,texts:node.findAllWithCriteria({types:['TEXT']}).length,instances:node.findAllWithCriteria({types:['INSTANCE']}).length});
 }
 return {frames,reactions,manualPresentationReviewRequired:true};
}
figma.showUI(__html__,{width:600,height:720});
figma.ui.onmessage=async message=>{
 if(running)return;
 running=true;const report={date:'2026-11-08',fileKey:LEDGER.fileKey,createdNodeIds:[],mutatedNodeIds:[],removedNodeIds:[],navigation:[]};
 try{
  report.inspection=await inspect();send({type:'progress',text:'84 frames conferidos. IDs e nomes correspondem ao arquivo da Etapa 5.'});
  if(message.type==='inspect'){send({type:'report',report});return;}
  if(message.type!=='apply')return;
  const fix=await layout();report.layout=fix;
  report.createdNodeIds.push(...fix.createdNodeIds);report.mutatedNodeIds.push(...fix.mutatedNodeIds);report.removedNodeIds.push(...fix.removedNodeIds);
  for(let i=0;i<LEDGER.frames.length;i+=14){
   const result=await navigation(i,Math.min(i+14,LEDGER.frames.length));report.navigation.push(result);report.mutatedNodeIds.push(...result.mutatedNodeIds);
   send({type:'progress',text:'Conexões aplicadas: '+Math.min(i+14,LEDGER.frames.length)+' / '+LEDGER.frames.length});
  }
  report.audit=await audit();report.stage='aplicado; validação visual e apresentação pendentes';
  send({type:'report',report});
  for(const id of ['T01','T12','QA-360-200']){
   const node=await figma.getNodeByIdAsync(LEDGER.frames.find(f=>f.id===id).nodeId);
   const bytes=await node.exportAsync({format:'PNG',constraint:{type:'SCALE',value:1}});send({type:'preview',id,bytes:Array.from(bytes)});
  }
 }catch(error){report.error=String(error);report.stage='interrompido; inspecionar alterações já registradas antes de repetir';send({type:'report',report});}
 finally{running=false;send({type:'ready'});}
};
