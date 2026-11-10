# Etapa 5 — Interface, navegação e prototipagem

**Etapa 5 · revisão v0.5 · 10/11/2026 · aprovada pelo usuário; concluída documentalmente.** Esta revisão reúne o HTML calculável e o Figma atualizado. No HTML, a identidade visual, os cálculos e as interações validados na v0.4 foram preservados; o aviso sobre o estado do Figma foi atualizado e o demonstrador foi regenerado e revalidado. A v0.5 concentra as novas alterações visuais e de navegação na página 03 do Figma. Branch `docs/etapa5-interface-navegacao`, integrada à Etapa 4 antes desta revisão. Requisitos, modelagem, contratos aprovados e código Android foram preservados. DEC-049 e referências de arquitetura receberam somente correção editorial de estado atual.

A aprovação e seu alcance estão no [registro de fechamento da v0.5](finalizacao-v05.md#aprovação-e-fechamento-documental). Vídeo, submissão e aceite do professor continuam separados da aprovação documental do usuário.

O [demonstrador local](prototipos/index.html) está utilizável para apresentação: abertura animada, duas categorias, seleção mista, cadastro pessoal, valores editáveis, dez pares AHP completos, cálculo correspondente às entradas, revisão, ranking, salvamento e exportação simulados. Abrir diretamente no navegador, sem servidor. Os registros existem **somente na memória da página**; recarregar reinicia os dados.

O [Figma autoral](https://www.figma.com/design/S1Ba7mzFVfE1d0ds21FR36) foi atualizado pelo Firefox autorizado, via Scripter, na página **03 · Protótipo revisado v0.5**: 598 quadros de contextos/ramificações, 1.681 conexões e sete entradas de apresentação. As duas páginas anteriores e os 84 frames históricos foram preservados. A [auditoria nativa](prototipos/figma/revisao-v05/auditoria-final.json) confere conexões efetivas, textos numéricos, identidade/origem, geometria e inventário. Nas tentativas da revisão v0.5, o MCP atingiu o limite de uso; a edição funcionou pela API nativa no navegador.

[**Abrir a apresentação Figma revisada**](https://www.figma.com/proto/S1Ba7mzFVfE1d0ds21FR36/PDM-AHP-TOPSIS-%E2%80%94-Etapa-5-%C2%B7-Proposta-para-revis%C3%A3o?node-id=21-2567&starting-point-node-id=21%3A2567&page-id=21%3A2491&scaling=min-zoom&content-scaling=fixed). Link conferido em janela privativa do Firefox, sem autenticação nesta execução; usar os roteiros preparados da página 03.

O Figma usa **roteiros preparados e entradas restritas**, sem cálculo livre ou memória global. São quatro percursos: Smartphones/BEV × oficiais/mista; números correspondentes às escolhas fixadas, calculados pela adaptação DEC-018. Listas, salvamentos e PDF são ramificações ilustrativas. O HTML continua sendo a demonstração com entradas livres e resultado calculado. Ver [aplicação e limites](prototipos/figma/revisao-v05/README.md) e [evidências v0.5](finalizacao-v05.md). Nenhum dos artefatos constitui implementação Android ou aprovação da entrega.

| Material | Conteúdo |
|---|---|
| [Atualização Figma e prontidão v0.5](finalizacao-v05.md) | Aplicação real, apresentação, evidências, limites e avaliação de prontidão. |
| [Correções locais v0.4](finalizacao-v04.md) | Histórico da revisão do HTML e do impedimento MCP de 09/11. |
| [Parecer independente histórico v0.3](revisao-uiux.md) | Problemas, fontes e antes/depois daquela rodada; parecer e aprovação atuais na v0.5. |
| [Mapa e rastreabilidade](mapa-telas.md) | Famílias, RF01–RF27, UC01–UC16 e estados. |
| [Navegação](navegacao.md) | Fluxo, Voltar, saída, retomada e versões do PDF; sem rotas Kotlin. |
| [Especificação](especificacao-telas.md) | Elementos, ações e regras de cada família. |
| [Design system](design-system.md) | Paleta verde profundo, tipografia, componentes, movimento e acessibilidade aplicados localmente. |
| [Validação](validacao.md) | Verificações executadas, capturas, roteiro e limites. |
| [Fontes editáveis](prototipos/README.md) | HTML autocontido, CSS/JS/template, matemática demonstrativa e registro da aplicação Figma. |

## Base e precedência

Foram examinados os dois PDFs em [references](../../references/), visão, requisitos, modelagem, decisões aprovadas/adotadas e os sete documentos de arquitetura. Os contratos definitivos prevalecem sobre estudos históricos. A leitura classificada de `chatGPT.md` está registrada no [parecer](revisao-uiux.md#fontes-e-classificações). Não se adotam instruções contidas nessa fonte como ordens para esta tarefa.

A abertura animada é exigência do enunciado v2, **p. 4**, conforme DEC-049; dois segundos na p. 11 são exemplo. A matemática segue **DEC-018**, com média geométrica normalizada e RI 1,12 para cinco critérios. Somente as duas categorias aprovadas são oferecidas; dados e ilustrações são fictícios e identificados. Não se acrescentaram categorias personalizadas, gráficos funcionais novos ou limites de negócio para acomodar o layout.

## O que depende da arquitetura

As escolhas de três camadas, DI manual, sessão compartilhada, motores independentes, Navigation 2 Compose e PDF local já foram adotadas nas DEC-043–DEC-048. Não são decisões à espera desta revisão. A abertura fora da sessão segue DEC-049.

| Integração futura, já orientada pela Etapa 4 | Contrato preservado no demonstrador |
|---|---|
| Rotas concretas, NavBackStackEntry e ViewModels | Voltar conserva edição; sair resolve DEC-025; cadastro retorna à mesma seleção sem selecionar. |
| Coroutines, tokens e processamento | Resultado tardio não pode substituir outra entrada/sessão; mudar valores invalida TOPSIS; mudar julgamentos invalida ambos. |
| Repositórios, Room, serialização e transações | Versão salva separada da edição; gravação manual e falha sem perda da anterior. A memória web é apenas simulação. |
| Firestore, metadados e domínios reais | Cache não comprova leitura atual/vazio; desconhecido permanece desconhecido; DEC-041/042 impedem atalhos. |
| PdfDocument, arquivos temporários e compartilhamento | Somente snapshot salvo elegível; falha não desfaz salvamento nem afirma envio. |
| Host/splash do sistema e ciclo Android | Abertura única por lançamento; restauração da última salva sem autosave. Validar integração real depois. |

## Trabalho posterior à Etapa 5

1. Consolidar o material da primeira entrega e o vídeo, usando a revisão v0.5 aprovada e seus limites documentados. A página 03 foi aplicada e seus percursos principais e acesso foram conferidos; a [retomada v0.4](prototipos/figma/retomada-v04.md) permanece como histórico.
2. Manter o plano futuro de RNF01 com três participantes e RNF02 no Android/TalkBack. Não foram executados nem são impedimentos artificiais ao fechamento documental da Etapa 5.
3. Na preparação de dados/implementação, aprovar fontes, protocolos, rubricas, domínios/precisões e imagens reais. O demonstrador fictício não depende de inventar esses dados para estar documentado.
4. Preparar vídeo e material final de entrega; não foi realizado envio ao Teams nem declarada aprovação do professor.

Para demonstrar entradas livres e cálculo correspondente, abrir o HTML. No Figma, usar a **página 03** e os roteiros explicitamente preparados; as páginas 01/02 são histórico preservado. O [registro de aprovação](finalizacao-v05.md#aprovação-e-fechamento-documental) fecha a Etapa 5 documental pelo usuário; a entrega acadêmica depende dos materiais e do envio pelos responsáveis.
