"""Gera um HTML autocontido e offline a partir das fontes revisáveis do demonstrador."""
import json
from pathlib import Path

base = Path(__file__).resolve().parent
def payload(name):
    return json.dumps(json.loads((base / name).read_text()), ensure_ascii=False).replace('<', '\\u003c')

html = (base / 'modelo.html').read_text()
for key, value in {
    '__CSS__': (base / 'prototipo.css').read_text(),
    '__MANIFEST__': payload('telas.json'),
    '__FIXTURES__': payload('dados-demonstracao.json'),
    '__MATH__': (base / 'numeros.js').read_text(),
    '__APP__': (base / 'prototipo.js').read_text(),
}.items():
    assert html.count(key) == 1, key
    html = html.replace(key, value)
(base / 'index.html').write_text(html)
print('HTML autocontido atualizado; sem gravação no Figma ou aplicativo Android.')
