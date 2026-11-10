/* Injetar SPEC. Scrim editável no próprio frame de overlay: as propriedades
 * overlayBackground são somente leitura na API. Não altera as conexões. */
const page=await figma.getNodeByIdAsync('21:2491');if(!page||page.name!==SPEC.pageName)throw Error('Página divergente.');await figma.setCurrentPageAsync(page);
await Promise.all(['Regular','Medium','Bold'].map(style=>figma.loadFontAsync({family:'Roboto',style})));
const changed=[];let count=0;
for(const s of SPEC.screens.filter(s=>s.modal)){const f=page.children.find(n=>n.name===s.id+' · '+s.title);if(!f)throw Error('Diálogo ausente '+s.id);const c=f.children.find(n=>n.name==='Conteúdo · rolagem vertical integral');if(c.height>740)throw Error('Diálogo precisa de rolagem própria '+s.id);f.resize(360,800);f.cornerRadius=0;f.fills=[{type:'SOLID',color:{r:0,g:0,b:0},opacity:.32}];f.overflowDirection='NONE';c.x=16;c.y=(800-c.height)/2;c.fills=[{type:'SOLID',color:{r:1,g:1,b:1}}];c.cornerRadius=24;c.effects=[{type:'DROP_SHADOW',color:{r:0,g:0,b:0,a:.2},offset:{x:0,y:8},radius:24,spread:0,visible:true,blendMode:'NORMAL'}];changed.push(f.id,c.id);count++;}
print(JSON.stringify({kind:'audit',fix:'editable-dialog-scrim',pageId:page.id,dialogs:count,createdNodeIds:[],mutatedNodeIds:changed},null,2));
