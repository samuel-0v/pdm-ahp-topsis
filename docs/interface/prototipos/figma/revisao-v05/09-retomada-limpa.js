/* Injetar PATCH. Só altera quadros próprios da página revisada, após preflight.
 * Retomadas não simuladas ficam desabilitadas; sete estados novos já devem existir.
 * As ligações são atualizadas pelo 04 e conferidas novamente pelo 07. */
if(figma.fileKey&&figma.fileKey!==PATCH.fileKey)throw Error('Arquivo divergente.');
const page=await figma.getNodeByIdAsync('21:2491');if(!page||page.name!==PATCH.pageName)throw Error('Página divergente.');await figma.setCurrentPageAsync(page);
const todo=[];for(const change of PATCH.patches){const f=page.children.find(n=>n.type==='FRAME'&&n.name===change.before.id+' · '+change.before.title);if(!f)throw Error('Frame divergente '+change.before.id);todo.push({f,change});}
await Promise.all(['Regular','Medium','Bold'].map(style=>figma.loadFontAsync({family:'Roboto',style})));
const master=await figma.getNodeByIdAsync('21:2518');if(!master||master.type!=='COMPONENT')throw Error('Componente de mensagem ausente.');
const messageKey=Object.keys(master.componentPropertyDefinitions).find(k=>k.startsWith('Message'));
const made=[],changed=[];
function updateText(t,str){t.characters=str;t.textAutoResize='HEIGHT';t.name=str.slice(0,90);changed.push(t.id);}
for(const {f,change} of todo){const {before,after}=change;const content=f.children.find(n=>n.name==='Conteúdo · rolagem vertical integral');
 for(let i=0;i<before.footer.length;i++){const a=before.footer[i],b=after.footer[i];if(JSON.stringify(a)===JSON.stringify(b))continue;const node=f.findOne(n=>n.name==='AÇÃO · '+a.label)||f.findOne(n=>n.name==='AÇÃO · '+b.label);if(!node||node.type!=='INSTANCE')throw Error('Ação ausente '+after.id+' '+a.label);const w=node.width;node.reactions=[];node.setProperties({Kind:b.kind||'primary',State:b.disabled?'Disabled':'Enabled'});node.name='AÇÃO · '+b.label;node.resize(w,52);node.primaryAxisSizingMode='AUTO';const t=node.findOne(n=>n.type==='TEXT'&&n.name==='Label');t.characters=b.label;t.resize(w-32,20);t.textAutoResize='HEIGHT';t.layoutSizingHorizontal='FILL';changed.push(node.id,t.id);}
 for(let i=0;i<Math.min(before.body.length,after.body.length);i++){const a=before.body[i],b=after.body[i];if(a.type==='card'&&a.text!==b.text){const card=f.findOne(n=>n.name==='CARD · '+a.title);const t=card.findOne(n=>n.type==='TEXT'&&(n.characters===a.text||n.characters===b.text));if(!t)throw Error('Texto de cartão ausente '+after.id);updateText(t,b.text);}if(a.type==='notice'&&a.text!==b.text){const t=f.findOne(n=>n.type==='TEXT'&&(n.characters==='i '+a.text||n.characters==='i '+b.text));if(!t)throw Error('Mensagem ausente '+after.id);updateText(t,'i '+b.text);}}
 for(const item of after.body.slice(before.body.length)){if(item.type!=='notice')throw Error('Tipo de adição inesperado');if(f.findOne(n=>n.type==='TEXT'&&n.characters==='i '+item.text))continue;const n=master.createInstance();const actions=content.children.find(n=>n.name==='Ações do quadro');content.insertChild(content.children.indexOf(actions),n);n.name='MESSAGE · info';n.setProperties({[messageKey]:'i '+item.text});n.resize(f.width-32,n.height);const t=n.findOne(n=>n.type==='TEXT');t.resize(f.width-64,22);t.textAutoResize='HEIGHT';made.push(n.id,t.id);}
}
print(JSON.stringify({kind:'audit',fix:'clean-resumption-and-explicit-bounds',pageId:page.id,patchedFrames:todo.length,createdNodeIds:made,mutatedNodeIds:changed},null,2));
