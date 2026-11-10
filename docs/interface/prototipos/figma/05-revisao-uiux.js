// PREPARADO, NÃO EXECUTADO. Requer LEDGER histórico, inspeção atual e cópia de segurança.
// Aplicar depois de revisar 04/03. Não executa 01/02 e não porta o motor web para Figma.
if (figma.fileKey && figma.fileKey !== LEDGER.fileKey) throw new Error('Arquivo divergente.');
const page = await figma.getNodeByIdAsync('0:1');
if (!page || page.type !== 'PAGE') throw new Error('Página original ausente.');
for (const entry of LEDGER.frames) {
  const node = await figma.getNodeByIdAsync(entry.nodeId);
  if (!node || node.type !== 'FRAME' || !node.name.startsWith(entry.id + ' · ')) throw new Error('Inventário divergente: ' + entry.id);
}
if (page.children.some(n => n.name.startsWith('T00 · '))) throw new Error('T00 já existe; revisar manualmente antes de repetir.');
const hex = value => ({r:parseInt(value.slice(0,2),16)/255,g:parseInt(value.slice(2,4),16)/255,b:parseInt(value.slice(4,6),16)/255});
const palette = {primary:'006A62',onPrimary:'FFFFFF',primaryContainer:'D8F2ED',onPrimaryContainer:'142C2F',surface:'FFFFFF',surfaceContainer:'F4F7F6',onSurface:'142C2F',secondary:'4B6062',outline:'6D8181',success:'205E42',successContainer:'E1F3E8',error:'A52932',errorContainer:'FFECEE',warning:'775214',warningContainer:'FFF2D8'};
const changes = [],semantics = {};
// Resolver todos os aliases antes de qualquer mutação; preservar vínculo semântico existente.
for (const [name,value] of Object.entries(palette)) {
  const semantic = await figma.variables.getVariableByIdAsync(LEDGER.foundations.variables[name]);
  if (!semantic) throw new Error('Token ausente: ' + name);
  semantics[name]=semantic;
  for (const [mode,alias] of Object.entries(semantic.valuesByMode)) {
    if (alias.type !== 'VARIABLE_ALIAS') throw new Error('Alias divergente: ' + name);
    const primitive = await figma.variables.getVariableByIdAsync(alias.id);
    if (!primitive || primitive.name !== 'base/' + name) throw new Error('Primitivo divergente: ' + name);
    changes.push({primitive,mode,value});
  }
}
await Promise.all(['Regular','Bold'].map(style => figma.loadFontAsync({family:'Roboto',style})));
await figma.setCurrentPageAsync(page);
for (const c of changes) c.primitive.setValueForMode(c.mode,hex(c.value));
const created = [],changedVariables = changes.map(c => c.primitive.id);
const paint = name => figma.variables.setBoundVariableForPaint({type:'SOLID',color:hex(palette[name])},'color',semantics[name]);
const frame = figma.createFrame();page.appendChild(frame);created.push(frame.id);
frame.name = 'T00 · Abertura animada · proposta v0.3';frame.resize(360,800);
frame.x = 0;frame.y = Math.min(0,...page.children.filter(n => n.id !== frame.id).map(n => n.y))-900;
frame.layoutMode = 'VERTICAL';frame.primaryAxisAlignItems = 'CENTER';frame.counterAxisAlignItems = 'CENTER';frame.itemSpacing = 20;
frame.paddingTop = frame.paddingBottom = frame.paddingLeft = frame.paddingRight = 32;
frame.fills = [paint('onSurface')];
const mark = figma.createNodeFromSvg('<svg width="100" height="100" viewBox="0 0 100 100"><path d="M20 70V46h15v24M44 70V30h15v40M68 70V14h15v56" fill="none" stroke="#9BDAC3" stroke-width="5" stroke-linecap="round" stroke-linejoin="round"/></svg>');frame.appendChild(mark);mark.name='Marca vetorial editável';created.push(mark.id);
function label(text,size,style='Regular') {
  const t=figma.createText();frame.appendChild(t);created.push(t.id);t.fontName={family:'Roboto',style};t.fontSize=size;t.characters=text;
  t.fills=[paint('onPrimary')];t.textAutoResize='HEIGHT';t.resize(296,t.height);t.textAlignHorizontal='CENTER';return t;
}
label('COMPARE COM CLAREZA',12,'Bold');label('PDM\nAHP-TOPSIS',32,'Bold');label('Suas prioridades.\nUma escolha bem fundamentada.',16);
const skip=figma.createFrame();frame.appendChild(skip);created.push(skip.id);skip.name='AÇÃO · Ir ao catálogo';skip.layoutMode='VERTICAL';skip.primaryAxisAlignItems=skip.counterAxisAlignItems='CENTER';skip.resize(296,56);skip.fills=[];skip.cornerRadius=16;
const caption=figma.createText();skip.appendChild(caption);created.push(caption.id);caption.fontName={family:'Roboto',style:'Bold'};caption.fontSize=16;caption.characters='Ir ao catálogo';caption.fills=[paint('onPrimary')];
const destination=LEDGER.frames.find(f => f.id === 'T01').nodeId;
await skip.setReactionsAsync([{trigger:{type:'ON_CLICK'},actions:[{type:'NODE',destinationId:destination,navigation:'NAVIGATE',transition:null}]}]);
await frame.setReactionsAsync([{trigger:{type:'AFTER_TIMEOUT',timeout:0.6},actions:[{type:'NODE',destinationId:destination,navigation:'NAVIGATE',transition:{type:'DISSOLVE',easing:{type:'EASE_OUT'},duration:0.25}}]}]);
// O desenho de barras do HTML não é reproduzido por esta reação. Conferir movimento no editor.
page.flowStartingPoints=[{nodeId:frame.id,name:'Entrada · proposta v0.3'},...page.flowStartingPoints];
return {createdNodeIds:created,changedVariableIds:changedVariables,openingNodeId:frame.id,requiresRuntimeAndScreenshotReview:true,remaining:'Restilizar instâncias M3 externas; portar formulários/Saaty/BEV/mista; validar 04/03 e todos os fluxos. Este script não conclui o Figma.'};
