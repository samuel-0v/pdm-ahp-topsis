"""Verificações de links, manifesto e rastreabilidade da proposta de interface."""
import json
import re
from pathlib import Path
from urllib.parse import unquote
from xml.etree import ElementTree

base = Path(__file__).resolve().parent
area = base.parent
manifest = json.loads((base / 'telas.json').read_text())
ledger = json.loads((base / 'figma/estado-criacao.json').read_text())
ids = {s['id'] for s in manifest['screens']}
assert len(ids) == len(manifest['screens'])
assert len(ledger['frames']) == 84
assert {f['id'] for f in ledger['frames']} <= ids
assert all(s.get('figmaPendente') for s in manifest['screens'] if s['id'] not in {f['id'] for f in ledger['frames']})
assert len({f['nodeId'] for f in ledger['frames']}) == 84
targets = 0
for screen in manifest['screens']:
    for target in [screen.get('back'), screen.get('auto'), screen.get('exitTo')] + [
        item.get('to') for item in screen['body'] + screen['footer']
    ]:
        if target and not target.startswith('@'):
            assert target in ids, (screen['id'], target)
            targets += 1
links = 0
for file in area.rglob('*.md'):
    for target in re.findall(r'\]\(([^)]+)\)', file.read_text()):
        if target.startswith(('http:', 'https:', '#')):
            continue
        path = unquote(target.split('#', 1)[0])
        assert (file.parent / path).exists(), (str(file), path)
        links += 1
coverage = (area / 'mapa-telas.md').read_text()
for prefix, maximum in [('RF', 27), ('UC', 16)]:
    present = {int(n) for n in re.findall(prefix + r'(\d+)', coverage)}
    # Intervalos na matriz incluem seus extremos e todos os IDs intermediários.
    for a, b in re.findall(prefix + r'(\d+)[–-]' + prefix + r'(\d+)', coverage):
        present.update(range(int(a), int(b) + 1))
    assert set(range(1, maximum + 1)) <= present, (prefix, present)
html = (base / 'index.html').read_text()
embedded = re.search(r'<script id="manifesto" type="application/json">(.*?)</script>', html, re.S)[1]
assert json.loads(embedded) == manifest
fixtures = re.search(r'<script id="fixtures" type="application/json">(.*?)</script>', html, re.S)[1]
assert json.loads(fixtures) == json.loads((base / 'dados-demonstracao.json').read_text())
projection = re.search(r'// CONTEXTOS_INICIO\n(.*?)// CONTEXTOS_FIM', html, re.S)[1]
assert projection == (base / 'contextos.js').read_text()
ElementTree.parse(base / 'mapa-navegacao.svg')
for file in area.rglob('*'):
    if file.is_file() and file.suffix in {'.md', '.json', '.js', '.py', '.html', '.svg'}:
        contents = file.read_text()
        assert contents.endswith('\n'), file
        assert all(line == line.rstrip() for line in contents.splitlines()), file
print(f'{len(ids)} descritores locais; 84 frames/IDs Figma; {targets} destinos; {links} links locais; RF01–RF27/UC01–UC16; HTML e SVG conferidos.')
