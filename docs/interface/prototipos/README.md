# Protótipo local e fontes editáveis

[Etapa 5](../README.md) · [Parecer](../revisao-uiux.md) · [Validação](../validacao.md)

## Abrir e demonstrar

Abrir [index.html](index.html) no navegador, sem servidor, dependências ou rede. A identificação “dados fictícios” acompanha o aparelho. O painel externo, recolhido inicialmente, controla cenários, 360/600 CSS px, texto 100/200%, próxima falha, Voltar e restauração simulada. Esses controles pertencem ao laboratório de avaliação do protótipo, não às funcionalidades do app.

Roteiro: abertura → Smartphones Premium → Iniciar avaliação → selecionar dois produtos → conferir/editar valores → responder os dez pares com sentido/intensidade → validar pesos → revisar → processar → conferir resultado → salvar. Para reproduzir o coeficiente 0,7517 do cenário principal, ajustar o preço do Aurora de 5.200,00 para 5.000,00 e confirmar igualdade nos dez pares. Outras entradas são calculadas, sem reutilizar esse coeficiente.

BEV percorre o mesmo assistente com seus cinco critérios. O cadastro durante seleção é editável, valida campos e retorna sem selecionar automaticamente. Oficiais/pessoais de mesmo nome continuam distintos. A lista pode manter várias avaliações na memória da página; cada gravação substitui somente a versão do mesmo ID. Não existe persistência após recarga: não há Room, Firebase, localStorage/sessionStorage ou serviço de PDF. PDF e compartilhamento são representações, sem arquivo/envio reais.

Rascunho preserva valores pendentes e julgamentos ainda não respondidos. Consulta não recalcula. Editar mantém uma cópia independente; voltar por etapas não salva. A saída possui as três ações da DEC-025. O seletor de falhas permite verificar preservação do estado após erro de gravação/cadastro/PDF/compartilhamento/exclusão.

## Fontes atuais e históricas

| Arquivo | Finalidade e alcance |
|---|---|
| [modelo.html](modelo.html), [prototipo.css](prototipo.css), [prototipo.js](prototipo.js) | Template, identidade visual e sessão/controles da UI v0.4; fontes preferenciais para editar. |
| [numeros.js](numeros.js) | Cálculo demonstrativo DEC-018 e validações DEC-041. Não integra o app Android. |
| [atualizar_html.py](atualizar_html.py) | Gera HTML autocontido a partir dessas fontes e JSONs; não editar apenas o HTML gerado. |
| [telas.json](telas.json) | 87 descritores de storyboard: inclui T00 e 84 IDs do ledger histórico. Não é motor da sessão atual nem inventário remoto atualizado. |
| [dados-demonstracao.json](dados-demonstracao.json) | Treze cenários numéricos de referência, com naturezas por categoria; não é catálogo real. |
| [validar_fixture.py](validar_fixture.py), [verificar_matematica.cjs](verificar_matematica.cjs) | Conferência independente Python e comparação do calculador web com os 13 cenários. |
| [cenarios-verificacao.js](cenarios-verificacao.js), [verificar_navegador.py](verificar_navegador.py) | Interações DOM reais, foco e geometria em Chrome isolado. |
| [verificar_documentacao.py](verificar_documentacao.py), [verificar_contraste.py](verificar_contraste.py) | Links/âncoras, manifesto, geração, RF/UC e pares sRGB. |
| [capturar_evidencias.py](capturar_evidencias.py) | Capturas reais do HTML; oculta somente laboratório externo. Abertura congelada para registro estático. |
| [mapa-navegacao.svg](mapa-navegacao.svg) | Diagrama vetorial editável; detalhamento em `navegacao.md`. |
| [contextos.js](contextos.js), [expandir_cenarios.py](expandir_cenarios.py) | Histórico v0.2, preservado para rastreabilidade; não incorporado na UI atual. Expansor interrompe antes de sobrescrever o manifesto v0.3. |
| [evidências atuais](evidencias/revisao-v04/) | Relatórios e capturas antes/depois. Evidências v0.1/v0.2/v0.3 permanecem históricas. |

## Figma: situação exata e retomada

[Arquivo autoral real](https://www.figma.com/design/S1Ba7mzFVfE1d0ds21FR36). O usuário autorizou arquivo separado na etapa anterior. O ledger registra 84 frames editáveis, página de componentes, tokens e instâncias Material 3 da criação anterior. Nesta revisão, uma nova sessão foi criada, mas a tentativa de leitura foi bloqueada pelo limite MCP Starter; `whoami` confirmou o plano. Não foi possível verificar se houve trabalho manual posterior. **Não se declara o arquivo atualmente navegável, atualizado ou aprovado.**

| Artefato Figma | Status |
|---|---|
| [estado-criacao.json](figma/estado-criacao.json), [spec-criada-v01.json](figma/spec-criada-v01.json) | Ledger e metadados históricos dos 84 frames. Preservados, sem inventar novos IDs. |
| [01-fundacoes.js](figma/01-fundacoes.js), [02-telas.js](figma/02-telas.js) | Criação inicial já executada. **Não repetir sobre o arquivo existente.** |
| [04-ajustes-layout.js](figma/04-ajustes-layout.js) | Preparado anteriormente: títulos ampliados, botões, progresso exato e exclusão contextual. Não aplicado/verificado. |
| [03-navegacao.js](figma/03-navegacao.js) | Preparado anteriormente: conexões e condicionais da versão nativa v0.1; não aplicado/verificado. |
| [05-revisao-uiux.js](figma/05-revisao-uiux.js) | Novo, somente preparado: preflight de IDs/aliases, nova paleta e T00 vetorial com saída/pulo. Sem execução real ou validação de API/layout. Não porta a UI completa. |
| [Retomada v0.4](figma/retomada-v04.md) e [tentativas reais](figma/verificacao-2026-11-09.json) | Impedimento remoto reconfirmado; checklist para inspeção humana e modo de apresentação. |
| [Plano v0.3](figma/plano-revisao-v03.json) | Grupos/IDs históricos, ajustes concretos, ordem segura e limitações. |
| [Plugin local histórico](figma/plugin-local/README.md) | Não instalado/executado. ZIP sem ID real não é instalável; não inclui script 05 nem a expansão v0.3. |
| [Registro anterior de 08/11](figma/continuacao-2026-11-08.json) | História da continuação v0.2; não é relatório desta revisão. |

Na retomada, ler as skills Figma adequadas, inspecionar inventário atual, conferir ledger e preservar alterações da equipe. Fazer cópia/versionamento antes de mutar. Revisar 04/03 contra o estado atual e a especificação v0.1; não injetar descritores v0.3 como se fossem frames já criados. Revisar 05, injetando `LEDGER`, e parar se T00 ou aliases divergirem. O script retorna IDs para registro, não aprovação visual.

Os próximos ajustes incluem layout e cores de instâncias M3 externas, campos/escala completos, BEV/mista/cadastro, estados DEC-041/042, movimento e fluxos. No Figma, usar cenários preparados com entradas restritas e identificadas, ou um mecanismo efetivamente calculado; não permitir entrada livre chegar a coeficiente fixo. Testar retornos, três escolhas, falhas e versões do PDF no modo de apresentação. Capturar evidências e verificar conta do avaliador antes de declarar concluído.

## Reproduzir

```bash
python3 docs/interface/prototipos/atualizar_html.py
python3 docs/interface/prototipos/verificar_documentacao.py
python3 docs/interface/prototipos/validar_fixture.py
node docs/interface/prototipos/verificar_matematica.cjs
python3 docs/interface/prototipos/verificar_contraste.py
python3 docs/interface/prototipos/verificar_navegador.py
python3 docs/interface/prototipos/capturar_evidencias.py
git diff --check
```

Os dois scripts de navegador pressupõem Chrome em `/opt/google/chrome/chrome`, perfil temporário e dados fictícios. Podem exigir execução fora do sandbox; não usam sessão pessoal, serviço web ou relatórios externos. O verificador grava resultados em `/tmp`; as evidências publicadas são cópias identificadas da rodada. A matriz HTML não substitui Android/TalkBack, persistência real ou testes humanos.

Na rodada v0.4, capturas e contraste são gravados em `evidencias/revisao-v04/`; o navegador grava relatórios em `/tmp/pdm-ui-v04-interacoes*.json`. O capturador aceita `--saida DIRETORIO`. Arquivos v0.3 são preservados; não confundir suas contagens com a rodada atual. RNF01 e testes Android/TalkBack continuam no plano futuro e não são requisitos de execução para fechar esta documentação.
