# Protótipo local e fontes editáveis

[**Abrir a apresentação Figma revisada**](https://www.figma.com/proto/S1Ba7mzFVfE1d0ds21FR36/PDM-AHP-TOPSIS-%E2%80%94-Etapa-5-%C2%B7-Proposta-para-revis%C3%A3o?node-id=21-2567&starting-point-node-id=21%3A2567&page-id=21%3A2491&scaling=min-zoom&content-scaling=fixed). Link conferido em janela privativa do Firefox, sem autenticação nesta execução; usar os roteiros preparados da página 03.

[Etapa 5](../README.md) · [Parecer](../revisao-uiux.md) · [Validação](../validacao.md)

## Abrir e demonstrar

Abrir [index.html](index.html) no navegador, sem servidor, dependências ou rede. A identificação “dados fictícios” acompanha o aparelho. O painel externo, recolhido inicialmente, controla cenários, 360/600 CSS px, texto 100/200%, próxima falha, Voltar e restauração simulada. Esses controles pertencem ao laboratório de avaliação do protótipo, não às funcionalidades do app.

Roteiro: abertura → Smartphones Premium → Iniciar avaliação → selecionar dois produtos → conferir/editar valores → responder os dez pares com sentido/intensidade → validar pesos → revisar → processar → conferir resultado → salvar. Para reproduzir o coeficiente 0,7517 do cenário principal, ajustar o preço do Aurora de 5.200,00 para 5.000,00 e confirmar igualdade nos dez pares. Outras entradas são calculadas, sem reutilizar esse coeficiente.

BEV percorre o mesmo assistente com seus cinco critérios. O cadastro durante seleção é editável, valida campos e retorna sem selecionar automaticamente. Oficiais/pessoais de mesmo nome continuam distintos. A lista pode manter várias avaliações na memória da página; cada gravação substitui somente a versão do mesmo ID. Não existe persistência após recarga: não há Room, Firebase, localStorage/sessionStorage ou serviço de PDF. PDF e compartilhamento são representações, sem arquivo/envio reais.

Rascunho preserva valores pendentes e julgamentos ainda não respondidos. Consulta não recalcula. Editar mantém uma cópia independente; voltar por etapas não salva. A saída possui as três ações da DEC-025. O seletor de falhas permite verificar preservação do estado após erro de gravação/cadastro/PDF/compartilhamento/exclusão.

## Fontes atuais e históricas

| Arquivo | Finalidade e alcance |
|---|---|
| [modelo.html](modelo.html), [prototipo.css](prototipo.css), [prototipo.js](prototipo.js) | Fontes preferenciais do HTML da revisão v0.5. Identidade visual, cálculo e interações validados na v0.4 foram preservados; o aviso sobre o Figma foi atualizado. |
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
| [Evidências v0.5](evidencias/figma-v05/) | Auditoria/apresentação Figma, capturas e reconferência HTML da revisão aprovada. |
| [Evidências HTML v0.4](evidencias/revisao-v04/) | Capturas e relatórios da base HTML preservada na v0.5. Evidências anteriores mantêm seu contexto histórico. |

## Figma: aplicação atual e histórico

[Arquivo autoral real](https://www.figma.com/design/S1Ba7mzFVfE1d0ds21FR36). Em 10/11 foi criada a página **03 · Protótipo revisado v0.5**, preservando os 84 frames e a página de componentes anteriores. A edição funcionou pela conta aberta no Firefox e pelo plugin Scripter; o MCP continuou limitado. O plugin local histórico não foi instalado/executado. **A fonte atual está em [revisao-v05](figma/revisao-v05/README.md)**; os scripts históricos abaixo não devem ser reaplicados.

A página nova tem 598 quadros (duplicações de contexto para os roteiros, não novas famílias de telas), 1.681 conexões básicas e sete entradas, incluindo abertura e acesso sem animação. As entradas são restritas a cenários preparados, com oito conjuntos numéricos correspondentes e rótulo de demonstração. Salvar, retomar, excluir e exportar são ramificações simuladas; não há memória global entre percursos. A [auditoria final nativa](figma/revisao-v05/auditoria-final.json) é distinta dos testes da apresentação e dos testes HTML. Ver [registro atual](../finalizacao-v05.md).

| Artefato Figma | Status |
|---|---|
| [estado-criacao.json](figma/estado-criacao.json), [spec-criada-v01.json](figma/spec-criada-v01.json) | Ledger e metadados históricos dos 84 frames. Preservados, sem inventar novos IDs. |
| [01-fundacoes.js](figma/01-fundacoes.js), [02-telas.js](figma/02-telas.js) | Criação inicial já executada. **Não repetir sobre o arquivo existente.** |
| [04-ajustes-layout.js](figma/04-ajustes-layout.js) | Preparado anteriormente: títulos ampliados, botões, progresso exato e exclusão contextual. Não aplicado/verificado. |
| [03-navegacao.js](figma/03-navegacao.js) | Preparado anteriormente: conexões e condicionais da versão nativa v0.1; não aplicado/verificado. |
| [05-revisao-uiux.js](figma/05-revisao-uiux.js) | Histórico v0.3, somente preparado: preflight de IDs/aliases, nova paleta e T00 vetorial com saída/pulo. Sem execução real ou validação de API/layout. Não porta a UI completa. |
| [Retomada v0.4](figma/retomada-v04.md) e [tentativas reais](figma/verificacao-2026-11-09.json) | Impedimento registrado em 09/11, superado na v0.5; checklist histórico, sem nova execução necessária. |
| [Plano v0.3](figma/plano-revisao-v03.json) | Grupos/IDs históricos, ajustes concretos, ordem segura e limitações. |
| [Plugin local histórico](figma/plugin-local/README.md) | Não instalado/executado. ZIP sem ID real não é instalável; não inclui script 05 nem a expansão v0.3. |
| [Registro anterior de 08/11](figma/continuacao-2026-11-08.json) | História da continuação v0.2; não é relatório desta revisão. |

Os registros v0.1–v0.4 acima permanecem históricos: o bloqueio de acesso de 09/11 foi superado por outro meio autorizado. A retomada atual deve partir do ledger v0.5, não de um novo disparo dos scripts 01/02 históricos. O compilador Starter descarta ações condicionais e não depende de recursos de prototipagem pagos.

## Reproduzir

```bash
python3 docs/interface/prototipos/atualizar_html.py
python3 docs/interface/prototipos/verificar_documentacao.py
python3 docs/interface/prototipos/validar_fixture.py
python3 docs/interface/prototipos/figma/revisao-v05/verificar_roteiros.py
node docs/interface/prototipos/verificar_matematica.cjs
python3 docs/interface/prototipos/verificar_contraste.py
python3 docs/interface/prototipos/verificar_navegador.py
python3 docs/interface/prototipos/capturar_evidencias.py
git diff --check
```

Os dois scripts de navegador pressupõem Chrome em `/opt/google/chrome/chrome`, perfil temporário e dados fictícios. Podem exigir execução fora do sandbox; não usam sessão pessoal, serviço web ou relatórios externos. O verificador grava resultados em `/tmp`; as evidências publicadas são cópias identificadas da rodada. A matriz HTML não substitui Android/TalkBack, persistência real ou testes humanos.

Na rodada v0.4, capturas e contraste são gravados em `evidencias/revisao-v04/`; o navegador grava relatórios em `/tmp/pdm-ui-v04-interacoes*.json`. O capturador aceita `--saida DIRETORIO`. Arquivos v0.3 são preservados; não confundir suas contagens com a rodada atual. RNF01 e testes Android/TalkBack continuam no plano futuro e não são requisitos de execução para fechar esta documentação.
