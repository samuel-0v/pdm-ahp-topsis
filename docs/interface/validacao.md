# Verificação da interface e prontidão documental

[Índice](README.md) · [Correções v0.4](finalizacao-v04.md) · [Parecer histórico v0.3](revisao-uiux.md) · [Reprodução](prototipos/README.md)

**Rodada v0.4 · 09/11/2026 · branch `docs/etapa5-interface-navegacao`.** Base limpa `bd055c2`, igual ao remoto, com Etapa 4 integrada. Evidências v0.1/v0.2/v0.3 são históricas e foram preservadas. Nesta rodada houve correção editorial autorizada em DEC-049 e três documentos de arquitetura, sem mudar a decisão normativa ou implementar Android.

## Resultados atuais

| Comando / conferência | Resultado e limite |
|---|---|
| `python3 docs/interface/prototipos/verificar_documentacao.py` | PASS: 87 descritores de referência, 84 IDs Figma históricos, destinos, links e âncoras; RF01–RF27/UC01–UC16; geração/sintaxe/SVG. Inclui links dos quatro documentos editoriais fora da área de interface. Não verifica inventário Figma atual. |
| `python3 docs/interface/prototipos/validar_fixture.py` | PASS: 13 cenários numéricos de referência, com naturezas próprias de smartphones/BEV. |
| `node docs/interface/prototipos/verificar_matematica.cjs` | PASS: 234 verificações do calculador web; pesos/λ/IC/RC, distâncias, coeficientes e postos comparados aos cenários; dinheiro, finitude e pendências. [Relatório](prototipos/evidencias/revisao-v04/matematica-v04.json). |
| `python3 docs/interface/prototipos/verificar_contraste.py` | PASS: 18 pares sRGB; texto ≥4,5:1, borda/foco ≥3:1. [Razões](prototipos/evidencias/revisao-v04/contraste-v04.json). Sem auditoria universal de pixels/estados. |
| `python3 docs/interface/prototipos/verificar_navegador.py` | PASS: **295 verificações**, incluindo regressões de detalhe pessoal, foco/rolagem de erro, empates e exclusão. [Relatório](prototipos/evidencias/revisao-v04/interacoes-v04.json). |
| Mesmo comando com `--movimento-reduzido` | PASS: 295 verificações; preferência reduzida efetivamente reconhecida por `matchMedia`. [Relatório](prototipos/evidencias/revisao-v04/interacoes-v04-movimento-reduzido.json). |
| Geometria HTML | 16 telas/estados × 360/600 CSS px × 100/200% = **64 combinações**; sem corte horizontal dos elementos inspecionados, alvos ≥48 × 48, rótulos/ajudas associados. Diálogos nas quatro combinações. |
| `python3 docs/interface/prototipos/capturar_evidencias.py` | Doze capturas reais atuais, em Chrome isolado; fontes/hashes/viewports em [metadados](prototipos/evidencias/revisao-v04/capturas.json). Abertura congelada somente para registro estático; transição exercitada no navegador. |
| Figma | `use_figma` e `get_metadata` bloqueados por quota Starter; URL também inacessível na ferramenta web. Nenhum inventário atual, screenshot, mutação, apresentação ou acesso de avaliador foi verificado. [Tentativas](prototipos/figma/verificacao-2026-11-09.json). |
| Escopo e diff | Código Android, requisitos, modelagem e contratos preservados. DEC-049/arquitetura: apenas textos de histórico/estado. `git diff --check` sem erros. [Registro consolidado](prototipos/evidencias/revisao-v04/verificacoes-v04.json). |

Os resultados acima devem ser conferidos junto aos relatórios publicados; não são testes do aplicativo Android. A rodada anterior teve 283 verificações, 64 combinações, 234 verificações matemáticas e 13 cenários: [registro v0.3](prototipos/evidencias/revisao-v03/verificacoes-v03.json). As doze verificações adicionais cobrem os achados da revisão atual.

## Percursos e inspeção visual

Foram exercitados Smartphones e BEV, mínimo de duas alternativas, cadastro pessoal durante seleção sem autoseleção, mistura de origens/IDs, valores livres, dez pares e sentidos/intensidades Saaty, AHP consistente/inconsistente/desatualizado, revisão e TOPSIS correspondente às entradas. Inclui preferência não uniforme comparada a oráculo Python, empate competitivo e conclusão válida sem diferenciação, sem vencedor inventado.

Também verificados: salvar/retomar rascunho, edição independente da versão salva, três escolhas da DEC-025, falhas sem perda da anterior, consulta sem recálculo, exportação da última salva preservando edição, falhas de PDF/compartilhamento, cancelamento e falha de exclusão, retorno dos detalhes, resposta tardia cancelada, cache/vazio/permissão distintos e bloqueios DEC-041/042. As novas regressões conferem atualização imediata de detalhe pessoal e preservação de snapshots ao editar/excluir cadastro.

Capturas inspecionadas: [início](prototipos/evidencias/revisao-v04/depois-inicio.png), [AHP](prototipos/evidencias/revisao-v04/depois-ahp.png), [ranking](prototipos/evidencias/revisao-v04/depois-resultado.png), [abertura](prototipos/evidencias/revisao-v04/abertura-360.png), [BEV](prototipos/evidencias/revisao-v04/bev-revisao-360.png), [mista 600/200%](prototipos/evidencias/revisao-v04/mista-resultado-600-texto200.png), [cadastro 360/200%](prototipos/evidencias/revisao-v04/cadastro-360-texto200.png), [saída 360/200%](prototipos/evidencias/revisao-v04/saida-360-texto200.png), [empate](prototipos/evidencias/revisao-v04/empate-360.png), [pessoal atualizado](prototipos/evidencias/revisao-v04/pessoal-atualizado-360.png), [foco do erro](prototipos/evidencias/revisao-v04/foco-erro-valores-360.png) e [exclusão com Cancelar](prototipos/evidencias/revisao-v04/exclusao-cancelar-360.png). Comparação histórica de antes/depois permanece no [parecer](revisao-uiux.md#antes-e-depois-visual).

## Acessibilidade e limites de execução

Geometria em DOM/Chrome, viewport de teste 1440 × 1200 CSS px e larguras internas de referência; isso não equivale a dp/sp de aparelho. Foram conferidos nomes/rótulos/ajudas, ordem DOM, mensagens além da cor, foco/retorno de diálogo, foco e rolagem do primeiro erro, alvos e reflow com texto ampliado. Perfil temporário, sem sessão pessoal, rede de catálogo, instalação de dependências ou armazenamento entre recargas. No HTML, PDF e gravação são simulados; não há arquivo/envio real.

**Não executados:** participantes, TalkBack, semântica Compose, teclado/IME e foco em aparelho, densidade/fontScale reais, rotação/morte do processo Android, Room/Firestore, PDF/compartilhamento real e testes de desempenho Android. A inspeção técnica de capturas não é teste humano de usabilidade nem aceite da equipe. O Figma tem limites próprios, ainda sem verificação atual.

## Critérios de fechamento documental e pendência real

Para encerrar a documentação/prototipagem da Etapa 5, mapa, especificação, identidade visual, fluxos/estados e evidências precisam ser coerentes com os contratos, e o Figma exigido deve estar efetivamente inspecionado, corrigido e demonstrável no modo de apresentação, com link/acesso conferido. O HTML está demonstrável, mas não comprova essas condições remotas.

**Impedimento atual: Figma sem acesso funcional para validar inventário, correções, conexões, apresentação e acesso do avaliador.** A [intervenção humana](prototipos/figma/retomada-v04.md) lista exatamente o trabalho necessário. Enquanto isso, o PR deve ser draft, e a Etapa 5 não é declarada concluída.

RNF01 com participantes e testes Android/TalkBack da implementação **não são impedimentos adicionais para fechar esta documentação**. Mantém-se o plano: três participantes externos ao desenvolvimento devem consultar produto, concluir avaliação, identificar resultado/empate e salvar/retomar; registrar ajuda/dificuldades e os critérios do RNF01. No Android, aplicar RNF02 em 360/600 dp, texto 100/200%, TalkBack, foco, rolagem e ciclo de vida. Nenhuma execução ou aprovação desses testes é presumida.

Dados/fontes/rubricas/assets reais pertencem à preparação dos dados e implementação, sem inventá-los por estética. Aprovação da equipe/professor, vídeo final e envio ao Teams são evidências distintas e não declaradas aqui. Para apresentar agora, abrir o HTML offline e explicar a simulação e a pendência Figma; seguir o roteiro de categorias → seleção/cadastro → valores → dez pares → pesos → revisão → resultado → salvar/editar/sair/PDF.
