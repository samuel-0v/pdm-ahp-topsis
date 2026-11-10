"""Executa o roteiro v0.4 num navegador real, apenas com dados locais do demonstrador."""
from pathlib import Path
from tempfile import TemporaryDirectory
import html as html_module
import json
import re
import subprocess
import sys
from datetime import datetime
from zoneinfo import ZoneInfo

reduced = '--movimento-reduzido' in sys.argv
suffix = '-movimento-reduzido' if reduced else ''

base = Path(__file__).resolve().parent
source = (base / 'index.html').read_text()
test = (base / 'cenarios-verificacao.js').read_text()
with TemporaryDirectory(prefix='pdm-ui-v04-') as temp:
    folder = Path(temp)
    target = folder / 'teste.html'
    target.write_text(source.replace('</html>', '<script>' + test + '</script></html>'))
    cmd = ['/opt/google/chrome/chrome', '--headless', '--no-sandbox', '--disable-gpu',
           '--disable-dev-shm-usage', '--disable-background-networking', '--no-first-run',
           '--user-data-dir=' + str(folder / 'perfil'), '--virtual-time-budget=20000',
           '--window-size=1440,1200', '--dump-dom', target.as_uri()]
    if reduced:
        cmd.insert(1, '--force-prefers-reduced-motion')
    run = subprocess.run(cmd, capture_output=True, text=True, timeout=60)
    Path('/tmp/pdm-ui-v04-dom.html').write_text(run.stdout)
    Path('/tmp/pdm-ui-v04-chrome.log').write_text(run.stderr)
    match = re.search(r'<pre id="validation-report">(.*?)</pre>', run.stdout, re.S)
    if not match:
        raise SystemExit('Navegador não produziu relatório. Ver /tmp/pdm-ui-v04-chrome.log e DOM.')
    report = json.loads(html_module.unescape(match[1]))
    report['browser'] = 'Chrome headless; viewport 1440×1200 CSS px; testes de largura interna 360/600'
    report['dateContext'] = datetime.now(ZoneInfo('America/Sao_Paulo')).isoformat(timespec='seconds')
    Path('/tmp/pdm-ui-v04-interacoes' + suffix + '.json').write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n')
    if report['status'] != 'PASS':
        print(json.dumps(report, ensure_ascii=False, indent=2))
        raise SystemExit(1)
    if reduced:
        assert report.get('motionPreference') is True, 'Preferência reduzida não confirmada pelo navegador.'
    print(json.dumps({'status': report['status'], 'checks': report['checks'],
                      'layouts': len(report['layouts']), 'reducedMotion': report.get('motionPreference'), 'scope': report['scope']}, ensure_ascii=False))
