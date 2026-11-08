# Etapa 5 — Interface, navegação e prototipagem

**Versão 0.2 · iniciada em 06/11/2026; continuação em 08/11/2026 · Proposta para revisão da equipe.** Branch: `docs/etapa5-interface-navegacao`. As escolhas visuais e a organização das telas ainda não são decisões aprovadas. RF01–RF27, UC01–UC16 e DEC-001–DEC-042 permanecem inalterados.

O material permite demonstrar localmente os percursos de avaliação nas duas categorias e a seleção mista. A Etapa 5 **não está encerrada**: o Figma contém 84 frames editáveis v0.1; a leitura funcionou em 08/11, mas nova tentativa de gravação foi bloqueada pelo limite MCP Starter. As correções e conexões continuam pendentes. Um [plugin local para aplicar os scripts](prototipos/figma/plugin-local/README.md) foi preparado para revisão e execução manual no editor autorizado; não foi instalado ou executado.

| Artefato | Situação e acesso |
|---|---|
| [Figma autoral — PDM AHP-TOPSIS](https://www.figma.com/design/S1Ba7mzFVfE1d0ds21FR36) | Arquivo separado criado nesta atividade; 84 frames de telas, variantes e diálogos; componentes e tokens. **Ainda sem conexões aplicadas e com ajustes visuais pendentes.** Não é apresentado como protótipo navegável concluído. |
| [Protótipo local navegável](prototipos/index.html) | v0.2: Smartphones, BEV, seleção mista, cadastro durante seleção, rascunhos, saída e exportação simulada. 86 descritores com projeções por categoria/seleção; 13 fixtures verificadas. Abrir em navegador, sem servidor. |
| [Mapa de telas e rastreabilidade](mapa-telas.md) | Famílias de telas, RFs, UCs e representação dos estados. |
| [Navegação conceitual](navegacao.md) | Diagrama principal, Voltar do Android, saída e exportação. Amplia o arquivo existente; não cria um fluxo concorrente. |
| [Mapa de navegação vetorial](prototipos/mapa-navegacao.svg) | Visão resumida em SVG editável, utilizável sem renderizador Mermaid. |
| [Especificação das telas](especificacao-telas.md) | Elementos, ações, validações, estados e critérios das duas categorias. |
| [Identidade visual inicial](design-system.md) | Paleta, tipografia, espaçamentos, componentes e acessibilidade proposta. |
| [Roteiro e verificação](validacao.md) | Evidências executadas, limites, pendências e roteiro de apresentação. |
| [Artefatos editáveis e retomada do Figma](prototipos/README.md) | Manifesto, dados, fontes do protótipo e scripts preparados. |

## Base analisada e adaptações preservadas

Foram lidos [Visão e Escopo](../01-visao-e-escopo.md), [requisitos e casos de uso](../requisitos/README.md), [domínio](../modelagem/dominio.md), [estados](../modelagem/04-estados-transicoes.md), [persistência e snapshots](../modelagem/05-persistencia-snapshots.md), [contratos consolidados](../modelagem/05-contratos-persistencia.md) e os capítulos de valores, compatibilidade, operações e recálculo. O [registro de decisões](../decisoes/registro-decisoes.md) e as regras transversais definem os comportamentos; a [arquitetura existente](../arquitetura/arquitetura.md) ainda é um documento de pendências.

O [enunciado v2](../../references/ProjetoDisciplina_PDM20262v2.pdf), pp. 7, 9 e 11, pede layouts, navegação, protótipos e apresentação das escolhas visuais na primeira entrega. A data da Fase 1 indicada ali é 03/11/2026; este material foi preparado em 06/11/2026 e não constitui comprovante de entrega. A arquitetura continua necessária para a entrega, embora não seja pré-condição para estes artefatos. O [plano de entrega existente](../entregas/fase-1-plano.md) não foi alterado nesta execução.

| Exemplo do enunciado | Proposta guiada pela base aprovada |
|---|---|
| Quatro categorias e exemplos genéricos de critérios | Somente Smartphones Premium e Veículos Elétricos (BEV), com os cinco critérios próprios aprovados. |
| Nome “CompareAI”, splash de 2 s e “cores da seleção” | Nome PDM AHP-TOPSIS; entrada direta no catálogo. Não adotar trechos editoriais alheios ao projeto como requisitos. |
| AHP antes dos valores | Conferir valores → AHP → revisão, preservando o fluxo já existente. |
| Vencedor com destaque dourado | Destaque apenas para vencedor único; empates e ausência de diferenciação respeitam DEC-024/027/028/035. |
| Gráficos e matrizes como telas obrigatórias | Não acrescentados como funcionalidades. Detalhes exibem os indicadores e valores exigidos; PDF não contém matrizes intermediárias. |
| Link Figma de exemplo | Não foi usado como arquivo da equipe nem sobrescrito. O usuário confirmou que não havia arquivo autoral anterior. |

Não havia protótipos locais anteriores para comparar com os requisitos; existia apenas `navegacao.md`. O novo desenho mantém suas regras e incorpora os detalhes aprovados nas DEC-039–DEC-042. Não se identificou conflito que exija rever um requisito aprovado.

## O que depende da arquitetura

| Pendência da Etapa 4 | Comportamento de interface que já deve ser preservado |
|---|---|
| Rotas concretas e pilha do Navigation Compose | Voltar dentro do assistente mantém a edição; sair com mudanças aplica as três escolhas da DEC-025. |
| ViewModels, escopo da sessão e eventos de interface | Versão salva e edição pendente são distinguíveis; calcular não salva. |
| Contratos de repositório e transações | Confirmar gravação/exclusão somente após sucesso; falha mantém a edição e a versão anterior. |
| Atualização e proveniência do catálogo | Cache, referência da fonte e confirmação de consulta têm rótulos distintos; datas desconhecidas não são inventadas. |
| Validação tipada e tradução de falhas dos motores | Campos ausentes, RC inconsistente, normalização inválida e perda de resolução bloqueiam processamento com explicação específica. |
| Geração de PDF e integração de compartilhamento | Somente versão salva elegível; exportar versão anterior mantém a edição; não afirmar envio ao abrir compartilhamento. |
| Restauração após recriação do processo e operações em curso | Não introduzir autosave. Detalhar a preservação em memória conforme ciclo Android e DEC-025, sem prometer recuperação após encerramento inesperado. |

Fontes/rubricas reais, domínios e precisão dos critérios dependem também da preparação dos dados. Não foram definidos limites de preço ou protocolos novos para viabilizar os mockups.

## Itens restantes para concluir a Etapa 5

- Aplicar e verificar conexões no Figma após a liberação de chamadas; corrigir barras superiores, botões estreitos, progresso e variantes ampliadas antes de apresentar esse arquivo como navegável.
- Completar interações de intensidade/sentido Saaty e entrada livre. BEV, seleção mista e cadastro durante seleção já são demonstráveis localmente com dados fixos; sua expansão para Figma continua pendente.
- Completar exceções secundárias, incluindo falhas de leitura/compartilhamento, cadastro inválido e estados de consulta vazia/sem permissão. Falha de cadastro durante seleção já é simulável no HTML.
- Revisar a proposta com a equipe, alinhar navegação com a Etapa 4 e substituir os dados fictícios quando fontes e rubricas forem aprovadas.
- Executar o protocolo com três participantes do RNF01 e, posteriormente no Android, a matriz completa do RNF02 com TalkBack. Testar permissões do link Figma com a conta do avaliador.
- Produzir capturas finais do Figma, vídeo de demonstração e evidência de entrega conforme orientação do professor. Nenhum envio ao Teams foi realizado.
