"""Contraste sRGB dos pares efetivamente usados no demonstrador v0.4."""
import json
from pathlib import Path

base = Path(__file__).resolve().parent
pairs = [
    ('ação preenchida', '#FFFFFF', '#006A62', 4.5),
    ('conteúdo', '#142C2F', '#FFFFFF', 4.5),
    ('auxiliar', '#4B6062', '#FFFFFF', 4.5),
    ('auxiliar no fundo', '#4B6062', '#F4F7F6', 4.5),
    ('seleção', '#006A62', '#D8F2ED', 4.5),
    ('mensagem informativa', '#4B6062', '#E9F1EF', 4.5),
    ('sucesso', '#205E42', '#E1F3E8', 4.5),
    ('erro', '#A52932', '#FFECEE', 4.5),
    ('aviso', '#775214', '#FFF2D8', 4.5),
    ('pessoal', '#5B478F', '#F0EAFB', 4.5),
    ('abertura título', '#FFFFFF', '#142C2F', 4.5),
    ('abertura marca/texto', '#9BDAC3', '#142C2F', 4.5),
    ('abertura auxiliar', '#B9D8CD', '#142C2F', 4.5),
    ('abertura ação', '#C7EEE0', '#142C2F', 4.5),
    ('borda funcional', '#6D8181', '#FFFFFF', 3),
    ('foco no fundo', '#5B478F', '#F4F7F6', 3),
    ('foco próximo ao botão', '#5B478F', '#FFFFFF', 3),
    ('borda ação abertura', '#69998A', '#142C2F', 3),
]
def luminance(color):
    rgb = [int(color[i:i+2], 16) / 255 for i in (1, 3, 5)]
    linear = [c / 12.92 if c <= .04045 else ((c + .055) / 1.055) ** 2.4 for c in rgb]
    return sum(c * w for c, w in zip(linear, [.2126, .7152, .0722]))

(base / 'evidencias/revisao-v04').mkdir(exist_ok=True)
rows = []
css = (base / 'prototipo.css').read_text().upper()
for name, fg, bg, minimum in pairs:
    assert fg.upper() in css and bg.upper() in css, name
    a, b = sorted([luminance(fg), luminance(bg)])
    ratio = (b + .05) / (a + .05)
    assert ratio >= minimum, (name, ratio)
    rows.append(dict(uso=name, frente=fg, fundo=bg, razao=round(ratio, 3), minimo=minimum, passa=True))
report = dict(status='PASS', pares=rows, limite='Pares sRGB do HTML, não auditoria de todos os nós/estados nem do Figma/Android. Estados desabilitados e decoração não são usados como informação exclusiva.')
(base / 'evidencias/revisao-v04/contraste-v04.json').write_text(json.dumps(report, ensure_ascii=False, indent=2) + '\n')
print(f'PASS: {len(rows)} pares; texto ≥4,5:1, borda/foco ≥3:1.')
