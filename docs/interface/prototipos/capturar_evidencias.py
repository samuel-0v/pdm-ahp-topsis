"""Capturas reais do demonstrador, em perfil isolado. Não altera dados do aplicativo."""
from pathlib import Path
from tempfile import TemporaryDirectory
import subprocess
import argparse
import json
import re
import hashlib
from datetime import datetime
from zoneinfo import ZoneInfo

base = Path(__file__).resolve().parent
parser = argparse.ArgumentParser()
parser.add_argument('--saida', type=Path, default=base / 'evidencias/revisao-v04')
parser.add_argument('--somente', nargs='+', help='Refaz apenas capturas nomeadas, preservando as demais evidências atuais.')
args = parser.parse_args()
destination = args.saida
destination.mkdir(parents=True, exist_ok=True)
source = (base / 'index.html').read_text()
captures = {
    'depois-inicio': (360, 1, 1900, 'finishSplash();'),
    'depois-ahp': (360, 1, 1900, "seed();S.edit.judgments=Array(10).fill(null);S.edit.ahp=null;S.pair=0;go('T08');$('pref-left').click();$('intensity-3').click();"),
    'depois-resultado': (360, 1, 1900, "seed();S.edit.alts[0].values[1]='5.000,00';computeSeed();go('T12');"),
    'abertura-360': (360, 1, 1000, "clearTimeout(finishSplash.timer);S.splashSeen=false;S.route='T00';$('splash').hidden=false;render();$('splash-path').replaceWith($('splash-path').cloneNode(true));"),
    'bev-revisao-360': (360, 1, 2300, "seed('bev');go('T10');"),
    'mista-resultado-600-texto200': (600, 2, 4400, "loadScenario('mixed');computeSeed();go('T12');"),
    'cadastro-360-texto200': (360, 2, 5200, "seed();newPersonal();fillExample();"),
    'foco-erro-valores-360': (360, 1, 1200, "loadScenario('missing');toAhp();window.scrollTo(0,0);"),
    'exclusao-cancelar-360': (360, 1, 1200, "loadScenario('saved');go('T12-DETALHES');openDialog('delete');"),
    'empate-360': (360, 1, 2400, "loadScenario('tie');"),
    'pessoal-atualizado-360': (360, 1, 1800, "resetDemo();newPersonal();fillExample();saveProduct();S.detail=copy(S.personal[0]);go('T03');newPersonal(S.detail);S.form.draft.name='Aurora Pro · pessoal revisado';S.form.draft.values[1]='5.100,00';saveProduct();"),
    'saida-360-texto200': (360, 2, 1500, "seed();go('T07');requestExit();"),
}
previous = json.loads((destination / 'capturas.json').read_text()) if args.somente else {}
real_heights = {name: item['alturaViewport'] for name,item in previous.get('depois',{}).items()}
with TemporaryDirectory(prefix='pdm-ui-capturas-') as tmp:
    folder = Path(tmp)
    for name, (width, scale, height, action) in captures.items():
        if args.somente and name not in args.somente:
            continue
        if name != 'abertura-360':
            action = 'finishSplash();' + action
        setup = f"document.documentElement.style.setProperty('--width','{width}px');document.documentElement.style.setProperty('--scale','{scale}');$('phone').classList.toggle('wide',{str(width == 600).lower()});$('phone').classList.toggle('font-large',{str(scale == 2).lower()});"
        # Apenas oculta o laboratório externo, mantendo o aparelho e os controles reais.
        style = '<style>aside.review-panel{display:none}.workspace{display:block;padding:24px}.stage{display:block}.phone{margin:auto}.splash-line{animation:none!important;stroke-dashoffset:0!important}.splash-copy{animation:none!important}</style>'
        target = folder / (name + '.html')
        target.write_text(source.replace('</head>', style + '</head>').replace('</html>', '<script>setTimeout(()=>{' + setup + action + "$('toast').hidden=true;document.documentElement.dataset.captureHeight=Math.ceil($('phone').getBoundingClientRect().height+48);},50);</script></html>"))
        cmd = ['/opt/google/chrome/chrome', '--headless', '--no-sandbox', '--disable-gpu', '--disable-dev-shm-usage', '--disable-background-networking', '--no-first-run', '--virtual-time-budget=1000', '--run-all-compositor-stages-before-draw', '--hide-scrollbars', '--user-data-dir=' + str(folder / name), '--window-size=' + str(max(480, width + 80)) + ',' + str(height), '--screenshot=' + str(destination / (name + '.png')), target.as_uri()]
        if not name.startswith(('saida-', 'exclusao-')):
            probe = [c for c in cmd if not c.startswith('--screenshot=')]
            probe.insert(1, '--dump-dom')
            measured = subprocess.run(probe, capture_output=True, text=True, timeout=60)
            match = re.search(r'data-capture-height="(\d+)"', measured.stdout)
            assert match, measured.stderr[-1000:]
            actual = int(match[1])
            cmd = [('--window-size=' + str(max(480, width + 80)) + ',' + str(actual)) if c.startswith('--window-size=') else c for c in cmd]
            real_heights[name] = actual
        else:
            real_heights[name] = height
        run = subprocess.run(cmd, capture_output=True, text=True, timeout=60)
        assert run.returncode == 0, run.stderr[-1000:]
        print(name, width, scale, flush=True)
(destination / 'capturas.json').write_text(json.dumps({'capturadoEm': datetime.now(ZoneInfo('America/Sao_Paulo')).isoformat(timespec='seconds'), 'fontesSha256': {name: hashlib.sha256((base / name).read_bytes()).hexdigest() for name in ['index.html','prototipo.css','prototipo.js','numeros.js']}, 'origem': 'HTML local v0.4; Chrome headless; sem imagens simuladas por IA', 'compararCom': 'evidencias/revisao-v03/ preserva o antes desta rodada; fontes anteriores no commit bd055c2', 'depois': {k: {'larguraCss': v[0], 'texto': v[1], 'alturaViewport': real_heights[k]} for k, v in captures.items()}, 'limite': 'Erro de valores: captura de altura completa reposicionada no topo, mantendo foco no campo; a rolagem ao erro foi verificada separadamente no DOM. Capturas de viewport; páginas longas usam rolagem. Abertura congelada na fase final da marca para registro estático; transição verificada no roteiro DOM.'}, ensure_ascii=False, indent=2) + '\n')
