/* Matemática exclusiva do demonstrador de interface. Não integra o aplicativo Android. */
const DemoMath=(()=>{
 const pairs=[];for(let i=0;i<5;i++)for(let j=i+1;j<5;j++)pairs.push([i,j]);
 const fail=message=>{throw new Error(message);};
 function decimal(raw){
  const text=String(raw??'').trim();if(!text)fail('Informe um valor.');
  if(!/^-?(?:\d+|\d{1,3}(?:\.\d{3})+)(?:,\d+)?$/.test(text))fail('Use números em pt-BR, por exemplo 1.250,50.');
  return text.replaceAll('.','').split(',');
 }
 function money(raw){
  const [whole,fraction='']=decimal(raw);if(/[1-9]/.test(fraction.slice(2)))fail('Fração de centavo não permitida. Corrija sem arredondamento.');
  const negative=whole.startsWith('-');const cents=BigInt(whole.replace('-',''))*100n+BigInt((fraction+'00').slice(0,2));
  const canonical=negative?-cents:cents;
  if(canonical<-(2n**63n)||canonical>2n**63n-1n)fail('Valor fora da representação monetária suportada.');
  const number=Number(canonical)/100;if(!Number.isFinite(number))fail('Valor monetário não representável no cálculo.');
  return {canonical:canonical.toString(),number};
 }
 function number(raw){const [a,b]=decimal(raw);const n=Number(a+(b===undefined?'':'.'+b));if(!Number.isFinite(n))fail('Informe um número finito.');if(n===0&&/[1-9]/.test(a+(b||'')))fail('Precisão insuficiente para representar este número.');return {canonical:n,number:n};}
 function parse(raw,criterion){const value=criterion.money?money(raw):number(raw);if(criterion.score&&(value.number<1||value.number>10))fail('A nota deve ficar entre 1 e 10.');return value;}
 function ahp(judgments){
  if(judgments.length!==10||judgments.some(x=>x===null||!Number.isFinite(x)||x<=0))fail('Responda as dez comparações antes de validar.');
  const matrix=Array.from({length:5},()=>Array(5).fill(1));
  pairs.forEach(([i,j],k)=>{matrix[i][j]=judgments[k];matrix[j][i]=1/judgments[k];});
  const logs=matrix.map(row=>row.reduce((sum,x)=>sum+Math.log(x),0)/5),max=Math.max(...logs);
  const gm=logs.map(x=>Math.exp(x-max)),sum=gm.reduce((a,b)=>a+b,0),weights=gm.map(x=>x/sum);
  const lambda=matrix.reduce((sum,row,i)=>sum+row.reduce((a,v,j)=>a+v*weights[j],0)/weights[i],0)/5;
  const ic=(lambda-5)/4,rc=ic/1.12;
  if(![lambda,ic,rc,...weights].every(Number.isFinite)||weights.some(w=>w<=0))fail('Não foi possível calcular o AHP com segurança.');
  return {weights,lambda,ic,ri:1.12,rc,consistent:rc<=0.10,matrix};
 }
 function topsis(values,weights,benefits,canonicals,monetary){
  if(values.length<2||weights.length!==5||values.some(r=>r.length!==5))fail('Selecione ao menos duas alternativas com cinco critérios.');
  if(!values.flat().every(Number.isFinite)||!weights.every(w=>Number.isFinite(w)&&w>0))fail('Há entradas numéricas inválidas.');
  for(let j=0;j<5;j++)if(monetary?.[j]){
   const seen=new Map();for(let i=0;i<values.length;i++){
    const x=values[i][j],canonical=canonicals[i][j];
    if(seen.has(x)&&seen.get(x)!==canonical)fail('Perda de resolução monetária no critério '+(j+1)+'. Valores distintos colidiram na conversão.');seen.set(x,canonical);
   }
  }
  const norms=weights.map((_,j)=>Math.hypot(...values.map(row=>row[j])));
  norms.forEach((n,j)=>{if(!Number.isFinite(n)||n===0)fail('Normalização indefinida no critério '+(j+1)+'. Revise seus valores.');});
  const weighted=values.map(row=>row.map((x,j)=>{
   const r=x/norms[j],v=r*weights[j];if(!Number.isFinite(v)||(x!==0&&(r===0||v===0)))fail('Precisão numérica insuficiente no critério '+(j+1)+'.');return v;
  }));
  const positive=weights.map((_,j)=>(benefits[j]?Math.max:Math.min)(...weighted.map(row=>row[j])));
  const negative=weights.map((_,j)=>(benefits[j]?Math.min:Math.max)(...weighted.map(row=>row[j])));
  const rows=weighted.map((row,i)=>{
   const dp=Math.hypot(...row.map((v,j)=>v-positive[j])),dm=Math.hypot(...row.map((v,j)=>v-negative[j]));
   if(!Number.isFinite(dp+dm))fail('Distâncias não representáveis.');
   return {index:i,dp,dm,coefficient:dp+dm===0?null:dm/(dp+dm)};
  });
  if(rows.every(r=>r.coefficient===null))return {kind:'sem-diferenciacao',rows};
  if(rows.some(r=>r.coefficient===null||!Number.isFinite(r.coefficient)))fail('Resultado matemático indefinido.');
  rows.sort((a,b)=>b.coefficient-a.coefficient);
  let leader=null,rank=0;rows.forEach((r,i)=>{if(leader===null||Math.abs(leader-r.coefficient)>1e-9){leader=r.coefficient;rank=i+1;}r.rank=rank;});
  return {kind:'ranking',rows};
 }
 return {pairs,money,number,parse,ahp,topsis};
})();
if(typeof module!=='undefined')module.exports=DemoMath;
