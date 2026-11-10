"""Verifica compilação e matemática dos roteiros; não testa apresentação nem Android."""
import json, math, subprocess
from pathlib import Path
root = Path(__file__).resolve().parent
subprocess.run(['node',str(root/'gerar-roteiros.cjs')],check=True,capture_output=True)
m = json.loads((root/'roteiros.json').read_text())
ids = {s['id'] for s in m['screens']}
assert len(ids)==len(m['screens'])==598
checks=0
for s in m['screens']:
    if s.get('back'):assert s['back'] in ids
    if s.get('auto'):assert s['auto']['to'] in ids
    for item in s['body']+s['footer']:
        assert 'actions' not in item, 'Ações condicionais não pertencem ao roteiro Starter.'
        if item.get('to') and not item['to'].startswith('@'):assert item['to'] in ids
        if item['type']=='field':assert not item.get('editable',False)
        checks+=1
for prefix in ['P_O','P_M','B_O','B_M']:
    for i in range(10):
        p=next(s for s in m['screens'] if s['id']==f'{prefix}_A{i}P')
        enabled=[i for i in p['footer'] if i.get('to') and not i.get('disabled') and i['label']!='Sair da edição']
        assert len(enabled)==1
        assert enabled[0]['to']==f'{prefix}_A{i}'+('R' if prefix.startswith('P') or i in [1,5] else 'S')
        r=next(s for s in m['screens'] if s['id']==f'{prefix}_A{i}R')
        assert next(x for x in r['body'] if x['type']=='progress')['prepared']==i+1
for clean in ['P_DRAFT_CLEAN_SEL','P_DRAFT_CLEAN_VAL','P_DRAFT_CLEAN_A0P']+[r+'_EDIT_CLEAN_REVIEW' for r in ['P_O','P_M','B_O','B_M']]:
    s=next(s for s in m['screens'] if s['id']==clean)
    assert s['savedLabel'].startswith('Versão salva')
    assert not any((i.get('to') or '').endswith('_EXIT') for i in s['footer'])
for r in ['P_O','P_M','B_O','B_M']:
    result=next(s for s in m['screens'] if s['id']==r+'_EDIT_CLEAN_RESULT')
    assert next(b for b in result['footer'] if b['label']=='Descartar recálculo')['to']==r+'_EDIT_DRAFT'
for f in m['fixtures']:
    matrix=[[1.0]*5 for _ in range(5)]
    pairs=[(i,j) for i in range(5) for j in range(i+1,5)]
    for (i,j),a in zip(pairs,f['judgments']):matrix[i][j]=a;matrix[j][i]=1/a
    gm=[math.prod(row)**.2 for row in matrix];w=[x/sum(gm) for x in gm]
    lam=sum(sum(a*b for a,b in zip(row,w))/weight for row,weight in zip(matrix,w))/5
    rc=(lam-5)/4/1.12
    assert math.isclose(rc,f['ahp']['rc'],abs_tol=1e-12)
    assert rc<=.1
    for a,b in zip(w,f['ahp']['weights']):assert math.isclose(a,b,abs_tol=1e-12)
    values=f['values'];costs={1} if f['id'].startswith('P') else {1,2,3}
    norm=[math.sqrt(sum(row[j]**2 for row in values)) for j in range(5)]
    v=[[row[j]/norm[j]*w[j] for j in range(5)] for row in values]
    best=[(min if j in costs else max)(row[j] for row in v) for j in range(5)]
    worst=[(max if j in costs else min)(row[j] for row in v) for j in range(5)]
    for r in f['result']['rows']:
        a=v[r['index']];dp=math.dist(a,best);dm=math.dist(a,worst);c=dm/(dp+dm)
        assert math.isclose(dp,r['dp'],abs_tol=1e-12)
        assert math.isclose(dm,r['dm'],abs_tol=1e-12)
        assert math.isclose(c,r['coefficient'],abs_tol=1e-12)
        checks+=3
    checks+=6
result={'status':'PASS','framesPrepared':len(ids),'preparedFlows':4,'numericFixtures':len(m['fixtures']),'checks':checks,'scope':'Compilador e oráculo Python dos roteiros estáticos. Não comprova execução nativa, apresentação Figma ou Android.'}
Path('/tmp/pdm-figma-roteiros-verificacao.json').write_text(json.dumps(result,ensure_ascii=False,indent=2)+'\n')
print(json.dumps(result,ensure_ascii=False))
