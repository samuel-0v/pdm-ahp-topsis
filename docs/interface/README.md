# Etapa 5 — Interface, navegação e prototipagem

**Revisão UI/UX v0.3 · 08–09/11/2026 · proposta visual para revisão.** Branch `docs/etapa5-interface-navegacao`, integrada à Etapa 4 antes desta revisão. Requisitos, modelagem, decisões e código Android foram preservados.

O [demonstrador local](prototipos/index.html) está utilizável para apresentação: abertura animada, duas categorias, seleção mista, cadastro pessoal, valores editáveis, dez pares AHP completos, cálculo correspondente às entradas, revisão, ranking, salvamento e exportação simulados. Abrir diretamente no navegador, sem servidor. Os registros existem **somente na memória da página**; recarregar reinicia os dados.

**A Etapa 5 completa ainda não pode ser declarada concluída.** O [Figma autoral](https://www.figma.com/design/S1Ba7mzFVfE1d0ds21FR36) não pôde sequer ser inspecionado nesta revisão: limite MCP do plano Starter. O inventário histórico tem 84 frames editáveis, com correções e conexões então pendentes; seu estado atual não foi confirmado. Nenhuma mutação Figma desta revisão foi realizada. O plugin histórico permanece preparado, sem instalação/execução declarada.

| Material | Conteúdo |
|---|---|
| [Parecer independente](revisao-uiux.md) | Problemas concretos, fontes, correções, evidências antes/depois e decisão de apresentação. |
| [Mapa e rastreabilidade](mapa-telas.md) | Famílias, RF01–RF27, UC01–UC16 e estados. |
| [Navegação](navegacao.md) | Fluxo, Voltar, saída, retomada e versões do PDF; sem rotas Kotlin. |
| [Especificação](especificacao-telas.md) | Elementos, ações e regras de cada família. |
| [Design system](design-system.md) | Paleta verde profundo, tipografia, componentes, movimento e acessibilidade aplicados localmente. |
| [Validação](validacao.md) | Verificações executadas, capturas, roteiro e limites. |
| [Fontes editáveis](prototipos/README.md) | HTML autocontido, CSS/JS/template, matemática demonstrativa e preparação Figma. |

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

## Trabalho restante

1. Inspecionar o Figma atual, aplicar e validar as correções/conexões, portar a revisão visual e os percursos BEV/mistos; testar o modo de apresentação e acesso do avaliador. [Plano concreto](prototipos/figma/plano-revisao-v03.json).
2. Revisar escolhas visuais com a equipe; executar RNF01 com três participantes e a matriz Android/TalkBack do RNF02 na implementação. Não há teste humano nem aceite presumido.
3. Aprovar fontes, protocolos, rubricas, domínios/precisões e imagens reais do catálogo. O demonstrador não valida dados de produtos reais.
4. Preparar vídeo e material final de entrega; não foi realizado envio ao Teams nem declarada aprovação do professor.

Para a apresentação imediata, usar o HTML como **demonstrador revisado** e explicar o estado pendente do Figma. O [parecer](revisao-uiux.md) fundamenta essa distinção.
