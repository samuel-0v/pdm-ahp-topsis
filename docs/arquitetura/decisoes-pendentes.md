# Decisões arquiteturais adotadas e pendências técnicas

**Versão 0.4 · 08/11/2026 · Escolhas adotadas por delegação explícita do usuário.** ARQ-P01–ARQ-P06 foram resolvidas nas DEC-043–DEC-048. O nome histórico deste arquivo foi preservado para manter os links. A [Etapa 4 foi encerrada documentalmente em 10/11/2026](fechamento-etapa-4.md), sem implementação. A DEC-049 complementa a apresentação após a revisão v0.3.

## Objetivo e contexto

Registrar as alternativas escolhidas com base no [enunciado v2](../../references/ProjetoDisciplina_PDM20262v2.pdf), nas [anotações acadêmicas identificadas por nome/hash](referencias-e-validacao.md#hierarquia-de-fontes-e-uso-das-anotações) e nos requisitos/contratos vigentes. O usuário delegou a decisão após os refinamentos v0.2; as escolhas abaixo são vigentes para orientar implementação, sem nova rodada de aprovação técnica de cada alternativa. A aceitação pelo professor não é presumida; o fechamento documental posterior está [registrado à parte](fechamento-etapa-4.md). DEC-001–DEC-042 permanecem inalteradas.

## Quadro de decisões adotadas

| Proposta resolvida → decisão | Escolha vigente | Fundamentação e limite |
|---|---|---|
| ARQ-P01 → [DEC-043](../decisoes/adotadas/dec-043.md) | MVVM, três camadas lógicas por pacotes em módulo Android único `app`. | Enunciado, pp. 2/7/12: quatro responsabilidades separadas. Aula de 20/10, §§2.2–2.4 **[AULA]**: camadas/pacotes. Vários módulos aparecem em **[MATERIAL]**, sem obrigação no enunciado. |
| ARQ-P02 → [DEC-044](../decisoes/adotadas/dec-044.md) | Injeção manual por construtor, AppContainer e ViewModelFactory. | Aula de 20/10, §§2.8–2.9 **[AULA]**: abordagem desenvolvida; 27/10, §2.18 **[AULA]**: escolha proporcional ao porte. Enunciado não exige Hilt/Koin. |
| ARQ-P03 → [DEC-045](../decisoes/adotadas/dec-045.md) | VM da sessão no NavBackStackEntry do grafo de avaliação; cadastro filho com VM próprio; StateFlow, UDF, edição e Room separados. | Enunciado, pp. 11/12; aulas de 06/10 e 09/10 **[AULA]**: estado/compartilhamento. Proprietário, término e restauração limitada são detalhamentos do projeto conforme DEC-025, não promessa de recuperação integral. |
| ARQ-P04 → [DEC-046](../decisoes/adotadas/dec-046.md) | Casos de uso quando há regras/orquestração; consultas simples pelos contratos; motores puros. | Enunciado, pp. 7/12, e aula de 20/10, §§2.2–2.7/2.12 **[AULA]**. Método AHP e contratos P5 permanecem os já aprovados. |
| ARQ-P05 → [DEC-047](../decisoes/adotadas/dec-047.md) | Navigation 2 / Navigation Compose, NavHost e grafo aninhado de avaliação. | Enunciado, p. 4: NavHost; aula de 09/10 **[AULA]**: controlador, pilha, argumentos e VM compartilhado. Navigation 3 não é necessário para o fluxo atual. Versões/rotas concretas ficam para configuração posterior. |
| ARQ-P06 → [DEC-048](../decisoes/adotadas/dec-048.md) | PDF nativo local com PdfDocument em Data, porta/conteúdo no Domain e compartilhamento Android via FileProvider/URI temporária em Presentation. | Funcionalidade autorizada pela DEC-032, não exigência extraída do enunciado/aula. API escolhida por julgamento técnico para evitar dependência externa inicial, mantendo separação ensinada em 20/10. |
| Correção do parecer v0.3 → [DEC-049](../decisoes/adotadas/dec-049.md) | Abertura/splash animada no host de Presentation, antes do conteúdo inicial e fora da sessão. | Exigência expressa do escopo do enunciado, p. 4; dois segundos pertencem ao fluxo ilustrativo da p. 11, sem atraso fixo obrigatório. Correção documental aplicada. Estado posterior (09/11): T00 representado no HTML/manifesto; essa era a pendência Figma em 09/11; a página 03 foi aplicada/auditada em 10/11, com [limites na interface](../interface/finalizacao-v05.md). Integração Android futura. |

As comparações abaixo preservam as alternativas avaliadas e a justificativa das escolhas. Não significam que Hilt, Koin, Navigation 3 ou múltiplos módulos sejam proibidos em outros contextos; sua adoção aqui exigiria revisão própria da decisão vigente.

## Comparação de injeção de dependências

| Critério | Manual + AppContainer/Factory | Hilt | Koin |
|---|---|---|---|
| Configuração | Construtores, montagem e fábrica escritos pela equipe; sem framework DI adicional. | Configuração de plugins/dependências, entrada Android e definições do grafo. | Dependência/contêiner e definições DSL; anotações/geração acrescentam ferramentas conforme modalidade. |
| Aprendizado | Ligações visíveis, adequado para entender DI/DIP; demanda disciplina de escopos. | Anotações, componentes/escopos e geração de código. | DSL concisa; compreender resolução, escopos e opções de verificação. |
| ViewModel | Fábrica e proprietário AndroidX explícitos, incluindo SavedStateHandle quando usado. | Integração Android para construção de ViewModels e redução de fábricas manuais. | Integração Android/Compose e definição de ViewModel; proprietário ainda precisa estar correto. |
| Testabilidade | Substituir dependências diretamente por fakes, sem subir contêiner nos testes puros. | Construtores seguem testáveis; substituição de bindings nos testes de integração. | Construtores seguem testáveis; módulos/verificação nos testes de integração. |
| Falhas do grafo | Tipos do Kotlin ajudam, mas equipe testa montagem e duração das instâncias. | Grafo gerado e validação de várias falhas na compilação. | DSL clássica resolve em execução; há mecanismos antecipados conforme ferramenta/modalidade, não ausência universal de validação. |
| Manutenção/custo | Baixo custo inicial, maior código de montagem conforme o grafo crescer. | Custo inicial/build maior; reduz manutenção manual em grafos maiores. | Montagem compacta; mais dependência/ferramentas e necessidade de validar configuração. |
| Adequação aqui | Adotada pelo porte e prazo acadêmico, sem comprometer contratos/DI por construtor. | Alternativa válida se equipe já domina ou se houver orientação do professor; escopo Android é compatível. | Alternativa válida se familiaridade compensar custo; multiplataforma não é requisito do projeto. |

AppContainer e ViewModelFactory são peças complementares da injeção manual, não frameworks concorrentes. DI não exige interface para motor/função pura sem variações reais. Nenhum contêiner garante por si só fluxo unidirecional, atomicidade ou salvamento manual.

O suporte do Hilt ao grafo e Android está no [guia oficial](https://developer.android.com/training/dependency-injection/hilt-android). A [documentação de DI manual](https://developer.android.com/training/dependency-injection/manual) descreve montagem/containers. A [documentação Koin](https://insert-koin.io/docs/reference/koin-core/definitions/) diferencia definições e modalidades de uso; a comparação com a DSL clássica não deve ser generalizada a todas as ferramentas atuais. A recomendação manual é julgamento desta proposta, não imposição do professor ou do guia Android.

## Navigation 2 versus Navigation 3

| Aspecto | Navigation 2 / Navigation Compose | Navigation 3 |
|---|---|---|
| Modelo | NavController, grafo e NavHost administram destinos/pilha. | Aplicação possui lista de chaves e apresenta entradas com NavDisplay/entryProvider. |
| Contratos de destinos | Pode usar rotas tipadas; não requer strings espalhadas. | Chaves tipadas e estado da pilha explícito. |
| Sessão compartilhada | A DEC-045 usa a entrada do grafo de avaliação como proprietário, com cadastro pessoal filho e retorno sem remover o pai. | Exigiria outro desenho de propriedade/escopo além de uma entrada de etapa que possa ser removida. |
| Custo para este fluxo | Aproxima-se do NavHost citado no enunciado e dos exemplos detalhados nas aulas; fluxo atual é essencialmente um destino por vez. | Posse da pilha/cenas é útil para interfaces adaptativas; não é uma necessidade atual que compense automaticamente o trabalho extra. |
| Restauração | Restaurar pilha/argumentos não restaura edição não salva por si só. | Restaurar chaves/pilha também não recupera edição perdida nem elimina limites de processo. |

As aulas de 09/10 introduziram ambas; a decisão por Navigation 2 se apoia no fluxo/prazo e familiaridade demonstrada, não em suposição sobre estágio de lançamento. A documentação oficial confirma [controle direto da pilha no Navigation 3](https://developer.android.com/guide/navigation/navigation-3) e [rotas tipadas no Navigation Compose](https://developer.android.com/guide/navigation/design/type-safety). Não inferir minSdk/API 35/36 a partir da observação oral das anotações. Versões e requisitos concretos serão verificados na configuração futura.

Qualquer escolha deve cumprir: IDs mínimos como argumentos; UI com callbacks de navegação; sessão preservada entre etapas/cadastro; saída protegida; cancelamento de cálculo com rejeição tardia; nenhum autosave. A orientação de eventos no host está no [guia de navegação Android](https://developer.android.com/guide/navigation/use-graph/navigate). Não implementar dois sistemas de navegação para adiar a decisão.

## Dependências herdadas, sem nova aprovação automática

| Dependência | Encaminhamento e limite |
|---|---|
| CHECK canônico AHP da DEC-038 | Validar declaração Room/collation e tentativa de escrita invertida; ainda não resolvido pela arquitetura. |
| Schema/migrations/serialização | Cumprir P5, round-trip e formatos conhecidos, sem fallback destrutivo; biblioteca/versões concretas em implementação posterior. Não ampliar o schema por conveniência. |
| Publicação do catálogo | Definições imutáveis por revisão e verificação do publicador; não é funcionalidade administrativa do app. Regras de escrita negada serão testadas. |
| Dados reais | Domínios, precisão, erro admissível, fontes/rubricas/protocolos antes de habilitar critérios reais; não inventar limites para concluir a arquitetura. |
| Motor inicial | Adequar sem diferenciação/empates/defesas numéricas e validar com referências independentes; não reimplementar nesta etapa documental. |
| Etapa 5 | v0.5 aprovada pelo usuário, conforme [fechamento da interface](../interface/finalizacao-v05.md#aprovação-e-fechamento-documental). Estados, escopos, retornos e diálogos estão documentados; integrar e verificar o [contrato completo](gerenciamento-estado.md) no Android posteriormente. |

## Origem da adoção e pendências reais

O [parecer independente da v0.1 e suas respostas](referencias-e-validacao.md#resposta-à-revisão-independente-da-v01) precedem esta escolha. Depois de receber a v0.2, o usuário instruiu o agente a tomar as decisões conforme enunciado e aulas. Em atendimento a essa delegação, as seis alternativas foram escolhidas e registradas nas DEC-043–DEC-048; não se atribui ao usuário uma aprovação literal anterior de cada texto, nem ao professor um aceite não informado.

As escolhas técnicas não dependem mais de confirmação individual. O alinhamento documental com a Etapa 5 v0.5 e a conferência final estão registrados no [fechamento da Etapa 4](fechamento-etapa-4.md). Permanecem integração Android e ajustes que seus testes revelem. A aprovação anterior da interface não encerrou a arquitetura por extensão; o fechamento agora decorre da conferência específica autorizada pelo usuário e não comprova entrega ao professor.

Schema/migrations/CHECK, domínios/fontes/rubricas/precisão, versões de dependências, adequações do motor e testes são tarefas posteriores já listadas acima. Não reabrem DEC-041/042 nem impedem a adoção desta arquitetura documental. Nenhum código, configuração ou commit é produzido nesta atividade.
