// Confere o cálculo JS do demonstrador contra as fixtures independentes em JSON.
const fs=require('fs'),path=require('path'),assert=require('assert');
const math=require('./numeros.js'),data=JSON.parse(fs.readFileSync(path.join(__dirname,'dados-demonstracao.json'),'utf8'));
let checks=0;const near=(a,b)=>{assert(Math.abs(a-b)<=1e-9+1e-9*Math.abs(b),`${a} != ${b}`);checks++;};
for(const scenario of data.cenarios){
 const judgments=math.pairs.map(([i,j])=>scenario.matrizAhp[i][j]),a=math.ahp(judgments);
 a.weights.forEach((x,i)=>near(x,scenario.ahp.pesos[i]));for(const [actual,expected] of [[a.lambda,scenario.ahp.lambda],[a.ic,scenario.ahp.ic],[a.rc,scenario.ahp.rc]])near(actual,expected);
 if(!scenario.topsis)continue;
 if(scenario.topsis.tipo==='falha'){assert.throws(()=>math.topsis(scenario.valores,a.weights,scenario.beneficios||data.beneficios),/Normalização/);checks++;continue;}
 const result=math.topsis(scenario.valores,a.weights,scenario.beneficios||data.beneficios);assert.equal(result.kind,scenario.topsis.tipo==='ranking'?'ranking':'sem-diferenciacao');checks++;
 for(const row of result.rows){const expected=scenario.topsis.alternativas.find(x=>x.alternativa===row.index);near(row.dp,expected.dMais);near(row.dm,expected.dMenos);if(expected.coeficiente===null){assert.equal(row.coefficient,null);assert.equal(row.rank,undefined);checks+=2;}else{near(row.coefficient,expected.coeficiente);assert.equal(row.rank,expected.posicao);checks++;}}
}
assert.equal(math.money('1,230').canonical,'123');checks++;
assert.throws(()=>math.money('1,231'),/Fração/);checks++;
assert.throws(()=>math.money('92.233.720.368.547.758,08'),/representação/);checks++;
assert.equal(math.money('-92.233.720.368.547.758,08').canonical,'-9223372036854775808');checks++;
assert.throws(()=>math.number('NaN'));checks++;assert.throws(()=>math.number('Infinity'));checks++;
assert.throws(()=>math.number('0,'+'0'.repeat(330)+'1'),/Precisão/);checks++;
assert.throws(()=>math.parse('11',{score:true}),/entre 1 e 10/);checks++;
const a=math.money('90.071.992.547.409,92'),b=math.money('90.071.992.547.409,93');assert.notEqual(a.canonical,b.canonical);assert.equal(a.number,b.number);checks+=2;
assert.throws(()=>math.topsis([[1,a.number,1,1,1],[2,b.number,2,2,2]],[.2,.2,.2,.2,.2],[true,false,true,true,true],[[1,a.canonical,1,1,1],[2,b.canonical,2,2,2]],[false,true,false,false,false]),/resolução monetária/);checks++;
assert.throws(()=>math.ahp(Array(10).fill(null)),/dez comparações/);checks++;
const uniform=math.ahp(Array(10).fill(1));assert(uniform.consistent);checks++;
const nonuniform=math.ahp([3,1,1,1,1,1,1,1,1,1]);assert(Math.abs(nonuniform.weights[0]-.2)>.01);checks++;
console.log(JSON.stringify({status:'PASS',checks,fixtures:data.cenarios.length,scope:'Somente matemática do demonstrador JS; não é teste do motor Android.'},null,2));
