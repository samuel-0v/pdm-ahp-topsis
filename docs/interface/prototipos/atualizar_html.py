"""Atualiza os dados embutidos no HTML editável; dispensa servidor e dependências."""
import json
import re
from pathlib import Path

base = Path(__file__).parent
target = base / 'index.html'
manifest = json.loads((base / 'telas.json').read_text())
payload = json.dumps(manifest, ensure_ascii=False).replace('<', '\\u003c')
html = target.read_text()
updated, count = re.subn(r'(<script id="manifesto" type="application/json">).*?(</script>)',
                         lambda m: m[1] + payload + m[2], html, flags=re.S)
assert count == 1
fixtures = json.dumps(json.loads((base / 'dados-demonstracao.json').read_text()), ensure_ascii=False).replace('<', '\\u003c')
block = '<script id="fixtures" type="application/json">' + fixtures + '</script>'
if '<script id="fixtures"' in updated:
    updated = re.sub(r'<script id="fixtures" type="application/json">.*?</script>', lambda _: block, updated, flags=re.S)
else:
    updated = updated.replace('<script>\n\'use strict\';', block + '\n<script>\n\'use strict\';', 1)
contexts = '// CONTEXTOS_INICIO\n' + (base / 'contextos.js').read_text() + '// CONTEXTOS_FIM'
if '// CONTEXTOS_INICIO' in updated:
    updated = re.sub(r'// CONTEXTOS_INICIO.*?// CONTEXTOS_FIM', lambda _: contexts, updated, flags=re.S)
else:
    updated = updated.replace('let focusBeforeModal=null;', 'let focusBeforeModal=null;\n' + contexts, 1)
target.write_text(updated)
print('HTML atualizado a partir de telas.json.')
