/* Somente leitura. Exporta o quadro histórico preservado e dois quadros novos.
 * PNG é evidência visual; não substitui camadas editáveis nem teste do player. */
const exportTargets=[['antes-inicio-preservado.png','6:8'],['depois-inicio-exportado.png','21:2526'],['abertura-exportada.png','21:2595']];
await Promise.all(['Regular','Medium','Bold'].map(style=>figma.loadFontAsync({family:'Roboto',style})));
const exported=[];
for(const [filename,id] of exportTargets){const n=await figma.getNodeByIdAsync(id);if(!n||n.type!=='FRAME')throw Error('Frame ausente '+id);if(n.parent.type==='PAGE')await n.parent.loadAsync();const bytes=await n.exportAsync({format:'PNG',constraint:{type:'SCALE',value:1}});exported.push({filename,nodeId:id,name:n.name,width:n.width,height:n.height,bytes:bytes.length,base64:figma.base64Encode(bytes)});}
print(JSON.stringify({kind:'preview',method:'exportAsync PNG, escala 1; leitura nativa, não captura da apresentação',exported},null,2));
