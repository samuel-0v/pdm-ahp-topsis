"""Empacota as fontes revisáveis; o ID real vem do manifesto gerado pelo Figma."""
import argparse
import json
from pathlib import Path

base = Path(__file__).resolve().parent
parser = argparse.ArgumentParser()
parser.add_argument('--manifesto-figma', type=Path, help='Manifesto de plugin de desenvolvimento criado no Figma, com ID real.')
parser.add_argument('--saida', type=Path, default=base / 'pacote')
args = parser.parse_args()
ledger = json.loads((base.parent / 'estado-criacao.json').read_text())
minimal = {k: ledger[k] for k in ['fileKey', 'foundations', 'frames']}
spec = json.loads((base.parent / 'spec-criada-v01.json').read_text())
# Nomes esperados são congelados antes de qualquer aplicação, não buscados por aproximação.
current = json.loads((base.parent.parent / 'telas.json').read_text())
titles = {s['id']: s['title'] for s in current['screens']}
for s in spec['screens']:
    s['title'] = titles[s['id']]
code = (base / 'code.template.js').read_text()
for token, value in [('__LEDGER__', json.dumps(minimal, ensure_ascii=False)), ('__SPEC__', json.dumps(spec, ensure_ascii=False)),
                     ('__LAYOUT__', (base.parent / '04-ajustes-layout.js').read_text()),
                     ('__NAVIGATION__', (base.parent / '03-navegacao.js').read_text())]:
    code = code.replace(token, value)
args.saida.mkdir(parents=True, exist_ok=True)
(args.saida / 'code.js').write_text(code)
(args.saida / 'ui.html').write_text((base / 'ui.html').read_text())
manifest = json.loads((base / 'manifest.template.json').read_text())
if args.manifesto_figma:
    identifier = json.loads(args.manifesto_figma.read_text()).get('id', '')
    if not identifier or identifier == manifest['id']:
        raise SystemExit('Informe manifesto com ID real gerado pelo Figma. Nenhum ID é inventado pelo empacotador.')
    manifest['id'] = identifier
    (args.saida / 'manifest.json').write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + '\n')
else:
    (args.saida / 'manifest.template.json').write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + '\n')
print('Fontes empacotadas em', args.saida)
print('Pronto para importar com ID real.' if args.manifesto_figma else 'ID real ainda necessário. Este pacote sem ID não foi instalado/executado no Figma.')
