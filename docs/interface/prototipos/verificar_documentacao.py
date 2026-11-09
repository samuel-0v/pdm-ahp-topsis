"""Confere documentação da UI, links/âncoras, fontes e inventário histórico distinto do atual."""
import json
import re
import subprocess
from pathlib import Path
from urllib.parse import unquote
from xml.etree import ElementTree

base = Path(__file__).resolve().parent
area = base.parent
manifest = json.loads((base / 'telas.json').read_text())
ledger = json.loads((base / 'figma/estado-criacao.json').read_text())
ids = {s['id'] for s in manifest['screens']}
assert len(ids) == len(manifest['screens']) == 87
assert len(ledger['frames']) == 84
native_ids = {f['id'] for f in ledger['frames']}
assert native_ids <= ids
assert all(s.get('figmaPendente') for s in manifest['screens'] if s['id'] not in native_ids)
assert len({f['nodeId'] for f in ledger['frames']}) == 84
assert 'T00' in ids and 'T00' not in native_ids

targets = 0
for screen in manifest['screens']:
    for target in [screen.get('back'), screen.get('auto'), screen.get('exitTo')] + [item.get('to') for item in screen['body'] + screen['footer']]:
        if target and not target.startswith('@'):
            assert target in ids, (screen['id'], target)
            targets += 1

def anchors(file):
    text = file.read_text()
    text = re.sub(r'```.*?```', '', text, flags=re.S)
    seen, result = {}, set(re.findall(r'(?:id|name)=[\"\']([^\"\']+)', text))
    for heading in re.findall(r'^#{1,6}\s+(.+?)\s*#*$', text, re.M):
        heading = re.sub(r'\[([^]]+)\]\([^)]*\)', r'\1', heading).replace('`', '').lower()
        slug = re.sub(r'[^\w -]', '', heading).replace(' ', '-')
        count = seen.get(slug, 0)
        result.add(slug + (f'-{count}' if count else ''))
        seen[slug] = count + 1
    return result

links = fragments = 0
editorials = [area.parent / 'decisoes/adotadas/dec-049.md'] + [area.parent / 'arquitetura' / name for name in ['arquitetura.md', 'decisoes-pendentes.md', 'referencias-e-validacao.md']]
for file in [*area.rglob('*.md'), *editorials]:
    for target in re.findall(r'\]\(([^)]+)\)', file.read_text()):
        if target.startswith(('http:', 'https:', 'mailto:')):
            continue
        path, _, fragment = unquote(target).partition('#')
        destination = file.parent / path if path else file
        assert destination.exists(), (str(file), path)
        if fragment:
            assert destination.is_file(), (file, target)
            assert fragment in anchors(destination), (str(file), target, sorted(anchors(destination)))
            fragments += 1
        links += 1

coverage = (area / 'mapa-telas.md').read_text()
for prefix, maximum in [('RF', 27), ('UC', 16)]:
    present = {int(n) for n in re.findall(prefix + r'(\d+)', coverage)}
    for a, b in re.findall(prefix + r'(\d+)[–-]' + prefix + r'(\d+)', coverage):
        present.update(range(int(a), int(b) + 1))
    assert set(range(1, maximum + 1)) <= present, (prefix, present)

html = (base / 'index.html').read_text()
for identifier, name in [('manifesto', 'telas.json'), ('fixtures', 'dados-demonstracao.json')]:
    embedded = re.search(r'<script id="' + identifier + r'" type="application/json">(.*?)</script>', html, re.S)[1]
    assert json.loads(embedded) == json.loads((base / name).read_text())
# Conferir geração sem executar o gerador nem modificar o arquivo observado.
expected = (base / 'modelo.html').read_text()
for marker, name in [('CSS', 'prototipo.css'), ('MATH', 'numeros.js'), ('APP', 'prototipo.js'), ('MANIFEST', 'telas.json'), ('FIXTURES', 'dados-demonstracao.json')]:
    payload = (base / name).read_text()
    if name.endswith('.json'):
        payload = json.dumps(json.loads(payload), ensure_ascii=False).replace('<', '\\u003c')
    expected = expected.replace('__' + marker + '__', payload)
assert expected == html, 'HTML precisa ser regenerado por atualizar_html.py.'
assert 'localStorage' not in (base / 'prototipo.js').read_text()
assert 'sessionStorage' not in (base / 'prototipo.js').read_text()
ElementTree.parse(base / 'mapa-navegacao.svg')
for file in area.rglob('*'):
    if file.is_file() and file.suffix in {'.md', '.json', '.js', '.cjs', '.css', '.py', '.html', '.svg'}:
        contents = file.read_text()
        assert contents.endswith('\n'), file
        assert all(line == line.rstrip() for line in contents.splitlines()), file
for name in ['prototipo.js', 'numeros.js', 'cenarios-verificacao.js', 'verificar_matematica.cjs']:
    subprocess.run(['node', '--check', str(base / name)], check=True, capture_output=True)
# Scripts Figma têm await/return de topo, como corpo da chamada do plugin, não módulos Node.
for file in (base / 'figma').glob('*.js'):
    script = 'const fs=require("fs");const AsyncFunction=Object.getPrototypeOf(async function(){}).constructor;new AsyncFunction(fs.readFileSync(process.argv[1],"utf8"));'
    subprocess.run(['node', '-e', script, str(file)], check=True, capture_output=True)
print(f'PASS: {len(ids)} descritores locais; 84 IDs Figma históricos; {targets} destinos; {links} links locais e {fragments} âncoras; RF01–RF27/UC01–UC16; geração/sintaxe/SVG. Sem inspeção Figma atual.')
