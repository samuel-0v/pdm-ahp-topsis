/* Injetar SPEC completo; conecta SOMENTE a página revisada e audita o resultado.
 * Repetir exige inspeção: substitui reações da página revisada conforme estes roteiros. */
const page=await figma.getNodeByIdAsync('21:2491');if(!page||page.name!==SPEC.pageName)throw Error('Página divergente.');await figma.setCurrentPageAsync(page);
const frames={},created=[],changed=[],connections=[],issues=[],pending=[];
for(const s of SPEC.screens){const f=page.children.find(n=>n.type==='FRAME'&&n.name===s.id+' · '+s.title);if(!f)throw Error('Quadro ausente '+s.id);frames[s.id]=f;}
const known=new Set(Object.keys(frames));
for(const s of SPEC.screens){const f=frames[s.id];if(s.modal&&f.width===328){const c=f.children.find(n=>n.name==='Conteúdo · rolagem vertical integral');f.resize(f.width,Math.min(740,c.height));changed.push(f.id);}
 const bind=async(node,to,label)=>{let actions;if(to==='@CLOSE')actions=[{type:'CLOSE'}];else{const dest=frames[to];if(!dest)throw Error('Destino ausente '+to);const def=SPEC.screens.find(x=>x.id===to);actions=[{type:'NODE',destinationId:dest.id,navigation:def.modal?'OVERLAY':'NAVIGATE',transition:null,...(!def.modal?{resetScrollPosition:true}:{})}];}pending.push({node,reactions:[{trigger:{type:'ON_CLICK'},actions}]});changed.push(node.id);connections.push({from:s.id,nodeId:node.id,to,label});};
 const act=async(label,to)=>{const nodes=f.findAll(n=>n.name==='AÇÃO · '+label);if(nodes.length!==1)throw Error('Ação ambígua/ausente '+s.id+' '+label+' '+nodes.length);await bind(nodes[0],to,label);};
 if(s.back)await act('← Voltar',s.back);
 for(const item of [...s.body,...s.footer]){
  if(item.type==='button'&&item.to&&!item.disabled)await act(item.label,item.to);
  if(item.type==='card'&&item.to){const nodes=f.findAll(n=>n.name==='CARD · '+item.title);if(nodes.length!==1)throw Error('Card ambíguo '+s.id+' '+item.title);await bind(nodes[0],item.to,item.title);}
  if(item.type==='intensities')await act(String(item.selected),item.to);
 }
 if(s.auto){pending.push({node:f,reactions:[{trigger:{type:'AFTER_TIMEOUT',timeout:s.auto.timeout},actions:[{type:'NODE',destinationId:frames[s.auto.to].id,navigation:'NAVIGATE',transition:{type:'DISSOLVE',easing:{type:'EASE_OUT'},duration:.2},resetScrollPosition:true}]}]});changed.push(f.id);connections.push({from:s.id,nodeId:f.id,to:s.auto.to,trigger:'AFTER_TIMEOUT',timeout:s.auto.timeout});}
}
// Scripter usa manifesto clássico (sem documentAccess dynamic-page): setter síncrono permitido.
for(const item of pending){try{item.node.reactions=item.reactions;}catch(error){issues.push({type:'reaction-rejected',nodeId:item.node.id,error:String(error)});}}
// Os primeiros quadros foram criados antes do ajuste da ordem guiada de seleção.
await Promise.all(['Regular','Medium','Bold'].map(style=>figma.loadFontAsync({family:'Roboto',style})));
for(const s of SPEC.screens)for(const item of s.body)if(item.type==='card'&&item.hint){const c=frames[s.id].findOne(n=>n.name==='CARD · '+item.title);const caption=c.findOne(n=>n.type==='TEXT'&&n.characters==='Abrir →');if(caption){caption.characters=item.hint;caption.textAutoResize='HEIGHT';changed.push(caption.id);}c.reactions=[];changed.push(c.id);}
for(const p of ['P','B']){const f=frames[p+'_SEL0'];const c=f.findAll(n=>n.name.startsWith('CARD · '))[1];const caption=c.findOne(n=>n.type==='TEXT'&&n.characters==='Abrir →');if(caption){caption.characters='Selecione primeiro o produto acima';caption.textAutoResize='HEIGHT';changed.push(caption.id);}c.reactions=[];changed.push(c.id);}
page.flowStartingPoints=SPEC.flowStarts.map(id=>({nodeId:frames[id].id,name:id==='OPEN0'?'Começar · abertura e avaliação':id==='HOME'?'Começar sem animação':id==='LAB'?'Exemplos e limites':id==='DRAFT_ENTRY'?'Salvar e retomar rascunho':id}));changed.push(page.id);
const frameReport=[];
for(const s of SPEC.screens){const f=frames[s.id],r=f.absoluteBoundingBox,content=f.children.find(n=>n.name==='Conteúdo · rolagem vertical integral');if(!content)throw Error('Conteúdo ausente '+s.id);const texts=f.findAllWithCriteria({types:['TEXT']});for(const t of texts){const b=t.absoluteBoundingBox;if(t.characters&&t.height<Number(t.fontSize)*.8)issues.push({id:s.id,nodeId:t.id,type:'text-height',height:t.height,fontSize:t.fontSize});if(b&&(b.x<r.x-.5||b.x+b.width>r.x+r.width+.5))issues.push({id:s.id,nodeId:t.id,type:'horizontal-overflow',box:b,frame:r});}
 const actions=f.findAll(n=>n.name.startsWith('AÇÃO · '));for(const a of actions){const label=a.findOne(n=>n.type==='TEXT'&&n.name==='Label');if(label&&label.characters!==a.name.slice(7))issues.push({id:s.id,nodeId:a.id,type:'button-label',expected:a.name.slice(7),actual:label.characters});}for(const a of actions)if(a.width<48||a.height<48)issues.push({id:s.id,nodeId:a.id,type:'target-under-48',width:a.width,height:a.height});
 if(content.height>f.height&&f.overflowDirection!=='VERTICAL')issues.push({id:s.id,type:'unscrollable-overflow'});
 frameReport.push({id:s.id,nodeId:f.id,width:f.width,height:f.height,contentHeight:content.height,fontScale:s.fontScale||1,textCount:texts.length,controlCount:actions.length,scroll:f.overflowDirection});
}
const original=await figma.getNodeByIdAsync('0:1');const expected=EXPECTED;
const originalDifferences=[];for(const e of expected){const n=await figma.getNodeByIdAsync(e.id);if(!n||n.name!==e.name||n.width!==e.width||n.height!==e.height)originalDifferences.push(e.id);}
if(original.children.length!==84)originalDifferences.push('children-count');
figma.currentPage.selection=[frames.OPEN0];figma.viewport.scrollAndZoomIntoView([frames.OPEN0]);
print(JSON.stringify({kind:'audit',version:'0.5',pageId:page.id,frameCount:frameReport.length,connectionCount:connections.length,frames:frameReport,connections,flowStartingPoints:page.flowStartingPoints,issues,originalDifferences,createdNodeIds:created,mutatedNodeIds:changed},null,2));
