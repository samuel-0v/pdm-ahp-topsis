/* Injetar SPEC. Ajuste visual idempotente somente na página revisada.
 * QuickJS não respeitou as opções de toLocaleString: usar toFixed + vírgula.
 * Não altera os números do modelo nem as ligações existentes. */
const page=await figma.getNodeByIdAsync('21:2491');if(!page||page.name!==SPEC.pageName)throw Error('Página divergente.');await figma.setCurrentPageAsync(page);
await Promise.all(['Regular','Medium','Bold'].map(style=>figma.loadFontAsync({family:'Roboto',style})));
const changed=[];
const decimal=x=>x.toFixed(4).replace('.',',');
for(const s of SPEC.screens){const f=page.children.find(n=>n.type==='FRAME'&&n.name===s.id+' · '+s.title);if(!f)throw Error('Quadro ausente '+s.id);
 for(const item of s.body){
  if(item.type==='result'){const card=f.findAll(n=>n.type==='FRAME'&&n.name==='RESULTADO · '+item.name).find(c=>c.findOne(t=>t.type==='TEXT'&&t.characters===item.origin));if(!card)throw Error('Identidade de resultado ausente '+s.id+' '+item.name+' '+item.origin);const texts=card.findAllWithCriteria({types:['TEXT']});const coefficient=texts.find(n=>n.characters.startsWith('C*'));const distances=texts.find(n=>n.characters.startsWith('d+'));coefficient.characters=item.coefficient===null?'C* não definido':'C* '+decimal(item.coefficient);coefficient.name=coefficient.characters;distances.characters='d+ '+decimal(item.dp)+'  ·  d− '+decimal(item.dm);distances.name=distances.characters;changed.push(coefficient.id,distances.id);}
  if(item.type==='weight'){const wrap=f.findOne(n=>n.type==='FRAME'&&n.name===item.label);const t=wrap.findOne(n=>n.type==='TEXT');t.characters=item.label+' · '+(item.value*100).toFixed(1).replace('.',',').replace(/,0$/,'')+'%';t.name=t.characters;changed.push(t.id);}
 }
 for(const t of f.findAllWithCriteria({types:['TEXT']}))if(t.characters.startsWith('✓ ✓ ')){t.characters=t.characters.replace(/^✓ ✓ /,'✓ ');changed.push(t.id);}
}
print(JSON.stringify({kind:'audit',fix:'decimal-display-and-single-symbol',pageId:page.id,createdNodeIds:[],mutatedNodeIds:changed},null,2));
