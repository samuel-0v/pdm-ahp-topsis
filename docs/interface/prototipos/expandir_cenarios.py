"""Prepara cenários locais adicionais; não escreve no arquivo Figma nem no app."""
import json
from copy import deepcopy
from pathlib import Path
from validar_fixture import ahp, topsis

base = Path(__file__).parent
data = json.loads((base / 'dados-demonstracao.json').read_text())
equal = [[1] * 5 for _ in range(5)]
phone = [[8000, 5000, 8, 16, 256], [7000, 6000, 9, 20, 512]]
bev = [[400, 180000, 30, 5000, 8], [420, 190000, 25, 4800, 9]]
existing = {s['id'] for s in data['cenarios']}
for category, values, benefits in [('phone', phone, data['beneficios']), ('bev', bev, [True, False, False, False, True])]:
    products = values + [values[0]]
    for mask in [3, 5, 6, 7]:
        name = f'{category}-{mask}'
        if name in existing:
            continue
        selected = [row for i, row in enumerate(products) if mask & (1 << i)]
        weights = ahp(equal)
        data['cenarios'].append({'id': name, 'matrizAhp': equal, 'beneficios': benefits,
                                'ahp': weights, 'valores': selected,
                                'topsis': topsis(selected, weights['pesos'], benefits)})
(base / 'dados-demonstracao.json').write_text(json.dumps(data, ensure_ascii=False, indent=2) + '\n')

manifest = json.loads((base / 'telas.json').read_text())
by_id = {s['id']: s for s in manifest['screens']}
new = deepcopy(by_id['T06-AB'])
new.update(id='T06-MISTA', title='Selecionar produtos', group='Continuação local', figmaPendente=True)
new['projection'] = 'selection'
new['body'] = [{'type': 'state', 'text': 'Smartphones Premium · oficiais e pessoais da mesma categoria', 'tone': 'Info'}] + [
    {'type': 'card', 'title': label, 'subtitle': origin, 'to': '@select-' + str(i)}
    for i, (label, origin) in enumerate([('Aurora Pro', 'Oficial'), ('Vértice Max', 'Oficial'), ('Aurora Pro', 'Pessoal')])]
if new['id'] not in by_id:
    manifest['screens'].append(new)
else:
    by_id[new['id']].update(new)
new = deepcopy(by_id['T04-SELECAO'])
new.update(id='T04-SELECAO-BEV', title='Novo produto pessoal BEV', group='Continuação local', figmaPendente=True)
new['body'] = [{'type': 'state', 'text': 'Categoria fixa: Veículos Elétricos (BEV)', 'tone': 'Info'},
               {'type': 'field', 'label': 'Nome', 'value': 'BEV Aurora · 2026'}] + [
                   {'type': 'field', 'label': label, 'value': str(value)}
                   for label, value in zip(['Autonomia PBEV · km', 'Preço · R$', 'Recarga DC 20–80% · min', 'Seguro anual · R$/ano', 'ADAS · nota 1–10'], bev[0])]
new['footer'] = [{'type': 'button', 'label': 'Salvar produto', 'to': '@productSave'}, {'type': 'button', 'label': 'Cancelar', 'to': 'D01-PRODUTO', 'secondary': True}]
if new['id'] not in by_id:
    manifest['screens'].append(new)
else:
    by_id[new['id']].update(new)
manifest['versao'] = '0.2 · 08/11/2026 · continuação local'
manifest['aviso'] = 'Cenários fictícios: catálogo BEV, seleção mista e cadastro na seleção são projetados localmente. Figma conserva 84 frames v0.1, sem novas gravações. AHP confirma igualdade; números TOPSIS vêm de fixtures verificadas, sem motor no protótipo.'
(base / 'telas.json').write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + '\n')
print('13 fixtures e 86 descritores locais preparados; Figma permanece com 84 frames.')
