// Exercita apenas a inspeção preventiva do pacote, sem simular mutações Figma.
const fs=require('fs'),path=require('path'),vm=require('vm'),assert=require('assert');
const pkg=process.argv[2]||'/tmp/pdm-etapa5-plugin-revisao';
const source=fs.readFileSync(path.join(pkg,'code.js'),'utf8');
const ledger=JSON.parse(fs.readFileSync(path.join(__dirname,'../estado-criacao.json'),'utf8'));
const spec=JSON.parse(fs.readFileSync(path.join(__dirname,'../../telas.json'),'utf8'));
const names=new Map(spec.screens.map(s=>[s.id,s.title]));
async function run(fileKey,missing){
 const nodes=new Map([['0:1',{id:'0:1',type:'PAGE',name:'01 · Protótipo'}]]),reports=[];
 for(const frame of ledger.frames){
  nodes.set(frame.nodeId,{id:frame.nodeId,type:'FRAME',name:frame.id+' · '+names.get(frame.id)});
  for(const control of frame.interactions)if(control.id!==missing)nodes.set(control.id,{id:control.id,type:'INSTANCE'});
 }
 const figma={editorType:'figma',fileKey,showUI(){},getNodeByIdAsync:async id=>nodes.get(id)||null,setCurrentPageAsync:async()=>{},ui:{postMessage(message){if(message.type==='report')reports.push(message.report);}}};
 vm.runInNewContext(source,{figma,__html__:'',console,Uint8Array});
 await figma.ui.onmessage({type:'inspect'});
 return reports.at(-1);
}
(async()=>{
 const wrong=await run('ARQUIVO_DE_TESTE_DIFERENTE');assert(wrong.error.includes('Arquivo diferente'));assert(!wrong.layout);
 const missing=await run(ledger.fileKey,ledger.frames[0].interactions[0].id);assert(missing.error.includes('Controle ausente'));assert(!missing.layout);
 const correct=await run(ledger.fileKey);assert.equal(correct.inspection.frames,84);assert(!correct.layout);assert(!correct.error);
 assert.equal(JSON.parse(fs.readFileSync(path.join(pkg,'manifest.template.json'),'utf8')).networkAccess.allowedDomains[0],'none');
 console.log('3 verificações de inspeção preventiva e manifesto sem rede passaram. API de mutação e execução real do plugin não foram testadas.');
})().catch(error=>{console.error(error);process.exitCode=1;});
