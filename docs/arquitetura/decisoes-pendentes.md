# Propostas e decisões arquiteturais pendentes

**Versão 0.1 · 08/11/2026 · Rascunho para revisão técnica independente; nenhuma proposta aprovada.** IDs `ARQ-P` são locais desta etapa, não novas DEC. O [registro central](../decisoes/registro-decisoes.md) não foi alterado.

## Objetivo e contexto

Tornar escolhas revisáveis sem confundir recomendação técnica com aprovação. A base DEC-001–DEC-042 continua vigente, inclusive os contratos de persistência. MVVM, Kotlin, Compose, Room, Firestore e salvamento manual já constam da base aprovada; este documento discute como organizá-los, não se devem ser substituídos.

## Quadro de propostas

| ID e tema | Proposta recomendada e justificativa | Alternativa/custo | Condição para aprovação |
|---|---|---|---|
| ARQ-P01 — Camadas e módulo | Três camadas lógicas por pacotes dentro de um único módulo `app`; menor configuração com fronteiras claras e núcleo puro. | Módulos Gradle dão fronteiras de compilação, mas aumentam configuração, dependências de build e coordenação. Sem necessidade atual de distribuição independente. | Revisar [arquitetura](arquitetura.md), imports permitidos e viabilidade pela equipe. |
| ARQ-P02 — Injeção | Construtores + `AppContainer` + `ViewModelFactory` manual. Grafo pequeno, explícito e alinhado à aula de 20/10. | Hilt reduz fábricas/valida grafo, com configuração/geração; Koin simplifica montagem com outro contêiner/dependência. | Equipe aceitar manutenção do grafo/fábrica e escopos; se exigência pedagógica adicional aparecer, reavaliar. |
| ARQ-P03 — Sessão, estado e concorrência | ViewModels para catálogo, pessoal, lista e sessão de avaliação; edição só em memória, StateFlow, comandos conflitantes serializados, identificação de cálculos; estado salvo Android limitado a IDs/parâmetros pequenos. | Um VM por etapa fragmenta edição; VM global expande duração e acoplamento. Recuperar edição inteira após morte de processo exigiria rever a política, não será introduzido como autosave. | Revisar escopo compartilhado, Voltar, cadastro durante seleção, perdas esperadas e rejeição de respostas tardias com UX/Android. |
| ARQ-P04 — Casos de uso e fronteiras | Consultas simples pelos contratos; casos de uso para seleção/validação/cálculo/salvar/exportar; motores puros e mapeamento de matrizes/IDs no Domain. | Classe por operação trivial aumenta indireção; concentrar tudo no ViewModel mistura negócio e apresentação. | Revisar [componentes](componentes.md), [motores](motores-matematicos.md) e adequações necessárias no protótipo Kotlin. |
| ARQ-P05 — Navegação | Preferência inicial por Navigation 2/Navigation Compose, com destinos tipados a detalhar; manter contratos de estado independentes da biblioteca. | Navigation 3 oferece posse explícita da pilha e cenas; exige definir seu gerenciamento e retenção da sessão. Não se trata de biblioteca proibida ou necessariamente experimental. | Confirmar familiaridade da equipe, requisitos do enunciado e compatibilidade das versões ao iniciar configuração; validar sessão e retorno. Nenhuma rota está fixada. |
| ARQ-P06 — PDF | Porta no Domain; geração nativa Android em Data, referência opaca e compartilhamento no host Presentation, com arquivo privado/URI temporária. | Biblioteca externa pode ajudar layout, mas traz dependência/licença/tamanho e não resolve elegibilidade por si só. | Revisar referência de documento, paginação, legibilidade, limpeza e integração Android sem misturar versões. |

## Comparação de injeção de dependências

| Critério | Manual + AppContainer/Factory | Hilt | Koin |
|---|---|---|---|
| Configuração | Construtores, montagem e fábrica escritos pela equipe; sem framework DI adicional. | Configuração de plugins/dependências, entrada Android e definições do grafo. | Dependência/contêiner e definições DSL; anotações/geração acrescentam ferramentas conforme modalidade. |
| Aprendizado | Ligações visíveis, adequado para entender DI/DIP; demanda disciplina de escopos. | Anotações, componentes/escopos e geração de código. | DSL concisa; compreender resolução, escopos e opções de verificação. |
| ViewModel | Fábrica e proprietário AndroidX explícitos, incluindo SavedStateHandle quando usado. | Integração Android para construção de ViewModels e redução de fábricas manuais. | Integração Android/Compose e definição de ViewModel; proprietário ainda precisa estar correto. |
| Testabilidade | Substituir dependências diretamente por fakes, sem subir contêiner nos testes puros. | Construtores seguem testáveis; substituição de bindings nos testes de integração. | Construtores seguem testáveis; módulos/verificação nos testes de integração. |
| Falhas do grafo | Tipos do Kotlin ajudam, mas equipe testa montagem e duração das instâncias. | Grafo gerado e validação de várias falhas na compilação. | DSL clássica resolve em execução; há mecanismos antecipados conforme ferramenta/modalidade, não ausência universal de validação. |
| Manutenção/custo | Baixo custo inicial, maior código de montagem conforme o grafo crescer. | Custo inicial/build maior; reduz manutenção manual em grafos maiores. | Montagem compacta; mais dependência/ferramentas e necessidade de validar configuração. |
| Adequação aqui | Recomendada pelo porte e prazo acadêmico, sem comprometer contratos/DI por construtor. | Alternativa válida se equipe já domina ou se houver orientação do professor; escopo Android é compatível. | Alternativa válida se familiaridade compensar custo; multiplataforma não é requisito do projeto. |

AppContainer e ViewModelFactory são peças complementares da injeção manual, não frameworks concorrentes. DI não exige interface para motor/função pura sem variações reais. Nenhum contêiner garante por si só fluxo unidirecional, atomicidade ou salvamento manual.

O suporte do Hilt ao grafo e Android está no [guia oficial](https://developer.android.com/training/dependency-injection/hilt-android). A [documentação de DI manual](https://developer.android.com/training/dependency-injection/manual) descreve montagem/containers. A [documentação Koin](https://insert-koin.io/docs/reference/koin-core/definitions/) diferencia definições e modalidades de uso; a comparação com a DSL clássica não deve ser generalizada a todas as ferramentas atuais. A recomendação manual é julgamento desta proposta, não imposição do professor ou do guia Android.

## Navigation 2 versus Navigation 3

| Aspecto | Navigation 2 / Navigation Compose | Navigation 3 |
|---|---|---|
| Modelo | NavController, grafo e NavHost administram destinos/pilha. | Aplicação possui lista de chaves e apresenta entradas com NavDisplay/entryProvider. |
| Contratos de destinos | Pode usar rotas tipadas; não requer strings espalhadas. | Chaves tipadas e estado da pilha explícito. |
| Sessão compartilhada | Pode usar proprietário do grafo da avaliação para reter o VM do assistente. | Exige projetar propriedade/escopo da sessão além de uma entrada de etapa que possa ser removida. |
| Custo para este fluxo | Aproxima-se do NavHost citado no enunciado e dos exemplos detalhados nas aulas; fluxo atual é essencialmente um destino por vez. | Posse da pilha/cenas é útil para interfaces adaptativas; não é uma necessidade atual que compense automaticamente o trabalho extra. |
| Restauração | Restaurar pilha/argumentos não restaura edição não salva por si só. | Restaurar chaves/pilha também não recupera edição perdida nem elimina limites de processo. |

As aulas de 09/10 introduziram ambas; a preferência por Navigation 2 se apoia no fluxo/prazo e familiaridade demonstrada, não em suposição sobre estágio de lançamento. A documentação oficial confirma [controle direto da pilha no Navigation 3](https://developer.android.com/guide/navigation/navigation-3) e [rotas tipadas no Navigation Compose](https://developer.android.com/guide/navigation/design/type-safety). Não inferir minSdk/API 35/36 a partir da observação oral das anotações. Versões e requisitos concretos serão verificados na configuração futura.

Qualquer escolha deve cumprir: IDs mínimos como argumentos; UI com callbacks de navegação; sessão preservada entre etapas/cadastro; saída protegida; cancelamento de cálculo com rejeição tardia; nenhum autosave. A orientação de eventos no host está no [guia de navegação Android](https://developer.android.com/guide/navigation/use-graph/navigate). Não implementar dois sistemas de navegação para adiar a decisão.

## Dependências herdadas, sem nova aprovação automática

| Dependência | Encaminhamento e limite |
|---|---|
| CHECK canônico AHP da DEC-038 | Validar declaração Room/collation e tentativa de escrita invertida; ainda não resolvido pela arquitetura. |
| Schema/migrations/serialização | Cumprir P5, round-trip e formatos conhecidos, sem fallback destrutivo; biblioteca/versões concretas em implementação posterior. Não ampliar o schema por conveniência. |
| Publicação do catálogo | Definições imutáveis por revisão e verificação do publicador; não é funcionalidade administrativa do app. Regras de escrita negada serão testadas. |
| Dados reais | Domínios, precisão, erro admissível, fontes/rubricas/protocolos antes de habilitar critérios reais; não inventar limites para concluir a arquitetura. |
| Motor inicial | Adequar sem diferenciação/empates/defesas numéricas e validar com referências independentes; não reimplementar nesta etapa documental. |
| Etapa 5 | Alinhar estados, escopos, retornos e diálogos, conforme [contrato de integração](gerenciamento-estado.md); não aguardar acabamento visual nem alterar o Figma nesta atividade. |

## Processo de revisão e status

Submeter esta pasta a outro integrante/revisor técnico. Para cada ARQ-P, registrar parecer, alterações necessárias e aprovação expressa, ou alternativa adotada. Somente depois formalizar decisões novas no registro central conforme processo da equipe. A entrega presente permanece rascunho; verificações locais de links/Mermaid não são revisão independente nem aprovação da Etapa 4.

Faltam para concluir documentalmente a Etapa 4: revisar a proposta, resolver achados, confirmar escolhas arquiteturais e alinhar contratos com Etapa 5. Configurar bibliotecas, implementar classes/schema e executar testes do app são trabalhos posteriores e não foram executados. Essa distinção evita confundir documentação revisável com aplicativo pronto.
