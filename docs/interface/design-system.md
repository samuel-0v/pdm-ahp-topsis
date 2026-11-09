# Identidade visual e componentes

[Índice](README.md) · [Especificação](especificacao-telas.md) · [Evidências](validacao.md)

**Proposta v0.4 aplicada ao HTML; Figma ainda não atualizado/verificado.** Referência Material Design 3 para Android/Compose, com superfícies claras, verde profundo e controles de altura expansível. A cor destaca ações e seleção; texto, símbolo e posição comunicam o significado. A marca com três barras é um estudo de comparação, não uma marca definitiva.

## Cores aplicadas localmente

| Papel CSS | Cor | Uso |
|---|---|---|
| primary / primary-soft | `#006A62` / `#D8F2ED` | Botões, seleção, navegação, progresso; branco no botão preenchido. |
| ink / muted | `#142C2F` / `#4B6062` | Títulos, conteúdo e textos auxiliares. |
| surface / canvas | `#FFFFFF` / `#F4F7F6` | Cards e fundo; a diferença de superfície organiza conteúdo sem excesso de contornos. |
| line | `#6D8181` | Borda funcional dos campos e controles; divisórias decorativas são mais claras. |
| success / success-soft | `#205E42` / `#E1F3E8` | Confirmação, consistência e gravação. |
| error / error-soft | `#A52932` / `#FFECEE` | Erro textual junto ao campo e mensagens persistentes de falha. |
| warning / warning-soft | `#775214` / `#FFF2D8` | Resposta pendente e dados incompletos. |
| foco / pessoal | `#5B478F` / `#F0EAFB` | Contorno de foco de 3 px; rótulo pessoal diferencia origem sem depender só da cor. |
| abertura | `#142C2F`, branco, `#9BDAC3`, `#B9D8CD` | Contraste da identificação e marca inicial. |

Razões calculadas em [contraste-v03.json](prototipos/evidencias/revisao-v04/contraste-v04.json). A conferência cobre os pares listados, sem afirmar auditoria de todos os pixels/estados do Figma. Controles desabilitados usam opacidade e texto explicativo de bloqueio nas telas pertinentes.

## Tipografia e composição

A fonte local usa `Inter, Roboto, system-ui, -apple-system, Segoe UI, sans-serif`, sem consulta remota; Roboto continua a referência Android. As capturas refletem a fonte disponível no navegador, sem alegar igualdade tipográfica com Figma. Corpo de referência 16 px, linha 1,6; títulos 24 px/1,3; destaque inicial 32 px/1,12; título da barra 20 px/1,3; ações 14 px/1,45; auxiliares 12–14 px e marcadores 11 px. Números usam alinhamento tabular; zero negativo causado pelo arredondamento visual é mostrado como zero, sem alterar entradas, resultados internos ou limites de consistência. O controle 200% dobra tamanhos relativos; números decorativos/ícones não substituem texto.

Ritmo de 4 px, com espaços 8/12/16/20/24/28/32. Conteúdo: 20 px em 360 e 28 px na referência ampla em desktop. Cards: raio 20, categoria/diálogo 24, campo 12, botão 16. Botão principal mínimo 52 px; campos 56; texto/ícones, abas e ações secundárias com alvo mínimo 48 × 48. Alturas crescem com o texto.

Os valores são referências visuais em **CSS px**, não medidas de uma implementação Android em dp/sp. Em 600 a composição aproveita largura; em 200% cards de métricas passam para uma coluna, navegação e abas empilham e rótulos quebram. O conteúdo usa rolagem vertical da página. Não se validou paisagem.

## Componentes aplicados

| Componente | Decisão de uso e estados |
|---|---|
| Categoria | Ilustração vetorial identificada, nome, descrição e chamada; alvo ocupa o card inteiro. |
| Produto selecionável | Nome/modelo, origem textual, contagem de critérios, símbolo +/✓, `aria-pressed`; bloqueio com explicação. |
| Campo | Rótulo persistente com critério/unidade, valor editável, ajuda original/ajuste, `aria-describedby` e erro `aria-invalid`. Não depender do teclado decimal para validar. |
| Preferência AHP | Grupo nativo de três sentidos; igualdade registra 1; demais exigem intensidade 2–9. Ajuda expansível com significado da escala. |
| Progresso | Etapas resumidas + contador exato de respondidos/10 e `progress` proporcional; par exibido não equivale a resposta. |
| Peso e consistência | Pesos com barra e número, RC em destaque e λmax/IC/RI; texto explícito de bloqueio. Precisão extra perto de 0,10 evita ocultar a condição. |
| Resultado | Posição, origem, C*, d+/d−; destaque só quando existe primeiro colocado único. Empates não recebem desempate estético e possuem rótulo textual em cada card do grupo, inclusive abaixo do primeiro posto. Sem diferenciação não tem posto/C* inventado. |
| Mensagem | Info/sucesso/aviso/erro com texto e símbolo; falhas de gravação/PDF ficam na tela, além do anúncio breve. |
| Diálogo | Ações empilhadas; `dialog` modal com foco inicial/devolução e rolagem. DEC-025 tem três escolhas; excluir exige confirmar/cancelar. |
| Navegação | Catálogo/Avaliações com nomes; Voltar e Sair contextual; nenhuma troca de seção ignora mudanças pendentes. |

## Movimento, foco e leitura

T00 desenha as barras e revela a identificação em 520 ms; a saída usa dissolução de 160 ms. São escolhas da proposta, não duração exigida no enunciado. A conclusão da animação libera T01; fallback de 850 ms evita bloqueio por evento ausente, sem aguardar rede. “Ir ao catálogo” permite pular. `prefers-reduced-motion` reduz animação e segue ao conteúdo; navegação/retomada/restauração não reapresentam abertura.

Ordem DOM: título → situação → progresso → conteúdo/erros → ações → navegação. Mudança de tela leva foco ao conteúdo; seleção preserva rolagem; botões e campos possuem foco visível; o primeiro campo inválido do cadastro ou dos valores recebe foco após validação, com rolagem para ficar visível. Exclusões iniciam foco em Cancelar; falhas permanecem na tela. Mensagens de erro usam texto e `role=alert`; toast usa `role=status`. A splash oculta os demais conteúdos enquanto visível.

O Figma histórico usa tokens violeta e instâncias do Material 3 Design Kit; estes **não receberam automaticamente** a nova paleta. O [script 05](prototipos/figma/05-revisao-uiux.js) prepara atualização dos aliases e T00, sem prometer restilização integral das instâncias externas nem navegação testada. TalkBack, semântica Compose, teclado/IME e testes humanos permanecem pendentes.
