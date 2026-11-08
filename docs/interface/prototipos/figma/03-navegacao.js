// PREPARADO, NÃO EXECUTADO: limite MCP Starter impediu novas chamadas.
// Injetar LEDGER (estado-criacao.json), SPEC (telas.json) e RANGE [início,fim].
// Executar somente após aplicar os ajustes de layout e inspecionar o arquivo.
const page=await figma.getNodeByIdAsync('0:1');await figma.setCurrentPageAsync(page);
await Promise.all(['Regular','Medium','Bold'].map(style=>figma.loadFontAsync({family:'Roboto',style})));
const frames=new Map(LEDGER.frames.map(f=>[f.id,f]));
const defs=new Map(SPEC.screens.map(s=>[s.id,s]));
const changed=[],createdVariables=[];
let collection=(await figma.variables.getLocalVariableCollectionsAsync()).find(c=>c.name==='PDM · Simulação');
if(!collection)collection=figma.variables.createVariableCollection('PDM · Simulação');
const mode=collection.modes[0].modeId;
const local=await figma.variables.getLocalVariablesAsync();
const variables={};
for(const [name,type,value] of [['resume','FLOAT',0],['currentStep','FLOAT',0],['savedKind','FLOAT',0],['status','STRING','Alterações não salvas']]){
 let v=local.find(v=>v.name===name&&v.variableCollectionId===collection.id);
 if(!v){v=figma.variables.createVariable(name,collection,type);v.scopes=type==='STRING'?['TEXT_CONTENT']:[];v.setValueForMode(mode,value);createdVariables.push(v.id);}
 variables[name]=v;
}
function set(name,value){return {type:'SET_VARIABLE',variableId:variables[name].id,variableValue:{type:variables[name].resolvedType==='FLOAT'?'FLOAT':'STRING',resolvedType:variables[name].resolvedType,value}};}
function equal(name,value){return {type:'EXPRESSION',value:{expressionFunction:'EQUALS',expressionArguments:[{type:'VARIABLE_ALIAS',value:{type:'VARIABLE_ALIAS',id:variables[name].id}},{type:typeof value==='number'?'FLOAT':'STRING',value}]}};}
function nav(id){const target=frames.get(id);if(!target)throw new Error('Destino ausente: '+id);return {type:'NODE',destinationId:target.nodeId,navigation:defs.get(id).modal?'OVERLAY':'NAVIGATE',transition:null,...(defs.get(id).modal?{overlayRelativePosition:{x:16,y:120}}:{resetScrollPosition:true})};}
function savedList(){return {type:'CONDITIONAL',conditionalBlocks:[{condition:equal('savedKind',2),actions:[nav('T05-CONCLUIDA')]},{condition:equal('savedKind',3),actions:[nav('T05-SEMDIF')]},{condition:equal('savedKind',1),actions:[nav('T05-RASCUNHO')]},{actions:[nav('T05')]}]};}
function draft(index){return [set('resume',index),set('savedKind',1),set('status','Rascunho salvo neste dispositivo'),nav('T05-RASCUNHO')];}
function actions(def,frame){
 const to=def.to;const pre=def.change?[set('status','Alterações não salvas')]:[];
 if(def.create||(frame.id==='T01-NOVA'&&['T06-0','T06-BEV'].includes(to)))pre.push(set('status','Alterações não salvas'));
 if(def.saveResult)return [set('savedKind',2),set('status','Avaliação salva neste dispositivo'),nav(to)];
 if(def.draftFrom)return draft(frames.get(def.draftFrom).index);
 if(to==='@draft')return draft(frame.index);
 if(to==='@resume')return [{type:'CONDITIONAL',conditionalBlocks:LEDGER.frames.filter(f=>defs.get(f.id).edit).map(f=>({condition:equal('resume',f.index),actions:[nav(f.id)]})).concat({actions:[nav('T06-0')]})}];
 if(to==='@saveExit')return [{type:'CONDITIONAL',conditionalBlocks:LEDGER.frames.filter(f=>defs.get(f.id).edit).map(f=>({condition:equal('currentStep',f.index),actions:draft(f.index)}))}];
 if(to==='@discard')return [set('status','Rascunho salvo neste dispositivo'),savedList()];
 if(to==='@exit')return [set('currentStep',frame.index),{type:'CONDITIONAL',conditionalBlocks:[{condition:equal('status','Alterações não salvas'),actions:[nav(frame.id==='T12'?'D01-RESULTADO':'D01')]},{actions:[savedList()]}]}];
 if(to==='@close')return pre.concat({type:'CLOSE'});
 if(to==='@back')return [{type:'BACK'}];
 if(to==='T05')return [savedList()];
 if(to==='T05-SEMDIF'&&(frame.id==='E04'||frame.id==='D01-SEMDIF'))return [set('savedKind',3),set('status','Avaliação salva neste dispositivo'),nav(to)];
 if(def.label==='Descartar alterações')pre.push(set('status','Avaliação salva neste dispositivo'));
 if(def.label==='Excluir avaliação')pre.push(set('savedKind',0));
 return to?pre.concat(nav(to)):[];
}
for(const f of LEDGER.frames.slice(RANGE[0],RANGE[1])){
 const node=await figma.getNodeByIdAsync(f.nodeId);if(!node)throw new Error('Frame não encontrado: '+f.id);
 for(const def of f.interactions){
  if(!def.to||def.disabled)continue;
  const n=await figma.getNodeByIdAsync(def.id);if(!n)throw new Error('Controle não encontrado: '+def.id);
  await n.setReactionsAsync([{trigger:{type:'ON_CLICK'},actions:actions(def,f)}]);changed.push(n.id);
 }
 const status=node.findOne(n=>n.name==='Persistência da edição');
 if(status){const t=status.findOne(n=>n.type==='TEXT');if(t){await figma.loadFontAsync(t.fontName);t.setBoundVariable('characters',variables.status);changed.push(t.id);}}
 if(defs.get(f.id).auto){await node.setReactionsAsync([{trigger:{type:'AFTER_TIMEOUT',timeout:0.3},actions:[nav(defs.get(f.id).auto)]}]);changed.push(node.id);}
}
page.flowStartingPoints=['T01','E01','E03','E04','E05','E07','E09','QA-360-200','QA-600','QA-600-200'].map(id=>({nodeId:frames.get(id).nodeId,name:id+' · '+defs.get(id).title}));
changed.push(page.id);
return {mutatedNodeIds:changed,createdVariableIds:createdVariables,appliedRange:RANGE};
