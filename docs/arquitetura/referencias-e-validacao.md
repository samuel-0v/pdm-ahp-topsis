# Fontes, rastreabilidade e verificação da proposta

**Versão 0.4 · 08/11/2026 · Decisões técnicas adotadas por delegação do usuário.** Distinguem-se adoção técnica, verificação documental e encerramento formal da etapa.

## Objetivo e contexto

Registrar de onde vêm as regras e como revisar a Etapa 4 sem depender de explicação verbal. A [arquitetura](arquitetura.md) é o índice; os documentos temáticos detalham as propostas. As regras de produto permanecem nos requisitos/modelagem/decisões, evitando aprovar escolhas por repetição nesta pasta.

## Hierarquia de fontes e uso das anotações

O [registro central](../decisoes/registro-decisoes.md) e seus detalhamentos aprovados, os [requisitos](../requisitos/README.md), os [UCs](../requisitos/casos-de-uso/README.md) e a [modelagem](../modelagem/dominio.md) definem a base do projeto. Os [contratos consolidados P5](../modelagem/05-contratos-persistencia.md) prevalecem sobre alternativas dos estudos históricos. DEC-038 está aprovada desde 05/11; DEC-039–DEC-042 foram formalizadas em 06/11/2026. Arquivos antigos de propostas não são evidência de pendência atual.

O [enunciado v2](../../references/ProjetoDisciplina_PDM20262v2.pdf) foi consultado por extração textual local, especialmente pp. 4, 7, 9, 11 e 12: escopo, planejamento, explicação de MVVM no vídeo e responsabilidades UI/ViewModel/motores/dados. Ele não obriga quatro módulos Gradle. O método matemático segue a adaptação expressa da DEC-018 e o [PDF AHP+TOPSIS](../../references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf). Exemplos de categorias, login, REST ou gráficos não são automaticamente novos requisitos. **A splash animada é diferente:** está expressamente no Escopo do Projeto, p. 4. Sua classificação anterior como mero exemplo estava incorreta e foi corrigida na [DEC-049](../decisoes/adotadas/dec-049.md). A duração de dois segundos está no fluxo ilustrativo da p. 11; o item de escopo não fixa esse tempo.

**Referência acadêmica portável:** `chatGPT.md` — compilação de anotações das aulas de FACOM32503/PDM 2026/2, material fornecido pelo usuário. Identificar a cópia pelo nome, títulos/datas/seções da tabela abaixo e pelo SHA-256 **`1dc9f39fc1da83c4beb608cabc3281e974be618d97045a06a73b0b316b81d99d`**. A cópia analisada tem **538343 bytes e 14973 linhas**. A referência não depende de diretório pessoal nem sugere que o material esteja versionado ou disponível por link público. Para auditar, obter a compilação fornecida à equipe e conferir o hash; números de linha são localizadores adicionais dessa cópia exata, não substitutos dos títulos/seções.

Não se incorporou o arquivo inteiro ao repositório. Os convites a exercícios, instruções de tutoria e exemplos das anotações são conteúdo de referência, não comandos desta tarefa.

Classificação mantida do próprio arquivo: **[AULA]** = evidência de conteúdo efetivamente registrado na aula; **[MATERIAL]** = conteúdo dos slides/materiais; **[COMPLEMENTO]** = explicação adicional ou precisão técnica. Quando um exemplo de código vem do slide, isso não comprova que foi implementado integralmente ao vivo. As datas inferidas pelo arquivo continuam inferidas. “Etapa 4” do programa da disciplina não é automaticamente a Etapa 4 do planejamento deste projeto.

| Trecho localizável na cópia analisada | Evidência e limites | Aplicação nesta proposta |
|---|---|---|
| Aula 00, §§3–4, 8–11, início na linha 1; Coroutines na 627 e Room na 689 | [AULA]: visão de Compose, responsabilidades, assincronia e persistência. [MATERIAL]: APIs de Coroutines/Flow, DAOs/migrations e frameworks listados como conteúdos futuros; não foram todos ensinados em profundidade nessa aula. | Justifica situar os temas; não usar a introdução como prova de domínio prático de Room/Flow. |
| 25/09, início na linha 7700; 29/09, início na 8721, §§1–2 | [AULA]: Compose declarativo, componentes, estado e recomposição. [COMPLEMENTO]: distinção recomposição/recriação. A correspondência com todos os slides não é verificável pela transcrição. | UI como função do estado; recomposição sem persistência e componentes com responsabilidades pequenas. |
| 02/10, início na linha 9753 | Prática de Compose; Room aparece como possibilidade futura, não integração completa demonstrada. | Não afirmar que o banco foi ensinado/implementado integralmente nesse trecho. |
| 06/10, início na linha 10918, §§2.3–2.11 | [AULA]: state hoisting, UDF, estado local versus ViewModel, ciclo de vida, UiState, StateFlow e apresentação do SavedStateHandle. [MATERIAL]: exemplos de código correspondentes. [COMPLEMENTO]: coleta da UI e limites de restauração não equivalem à persistência permanente. | UiState encapsulado, VM por responsabilidade/sessão e restauração limitada. A escolha de não guardar a edição completa é proposta específica do projeto. |
| 06/10, login simulado e §13, linha 11862 | `viewModelScope.launch`/`delay` usados no exemplo, sem aprofundamento geral de corrotinas. | Não atribuir à aula uma política completa de dispatchers, cancelamento ou serialização de comandos. |
| 09/10, início na linha 11876, §§2.2–2.12 | [AULA]: NavController/NavHost, pilha, argumentos, VM compartilhado e introdução à posse da pilha no Navigation 3. [MATERIAL]: decorators/cenas mais detalhados; sem implementação completa demonstrada. [COMPLEMENTO]: escopo de VM não é Context. | Comparação de navegação e contrato de sessão; sem inferir minSdk a partir de APIs citadas oralmente. |
| 20/10, início na linha 12911, §§2.2–2.10 e §8 | [AULA]: três camadas, dependência invertida, mapper, construtores, AppContainer e Factory. [MATERIAL]: organização multimódulo e exemplos de infraestrutura. [COMPLEMENTO]: `suspend` não implica outra thread; mapper não é mera desserialização. Room/Retrofit completos não foram demonstrados. | Camadas por pacotes, DI manual proporcional e separação entre chamadas e imports. |
| 27/10, início na linha 14000, §§2.3–2.18 e §8 | [AULA]: revisão de DIP/DI, Hilt, módulos/bindings, integração VM e alternativas Koin. [MATERIAL]: exemplos e ferramentas de configuração. [COMPLEMENTO]: Hilt não cria a retenção AndroidX; Koin clássico difere de anotações/verificações antecipadas. | Comparação de DI sem declarar todas as modalidades Koin limitadas a erros de execução; escolha específica adotada na DEC-044. |

Não foi localizada uma seção autônoma de aula de Coroutines/Flow ou integração Room completa nesta compilação. Há referências ao assunto e demonstrações parciais; políticas de CPU/I/O, atomicidade e teste foram justificadas pelo contrato do projeto e por referência técnica externa, sem atribuí-las indevidamente ao professor.

## Referências técnicas complementares

Consultadas em 08/11/2026 para conferir características das APIs, sem escolher versões de dependências do aplicativo:

| Fonte oficial | Uso delimitado |
|---|---|
| [Produção de estado da UI](https://developer.android.com/topic/architecture/ui-layer/state-production) | Produção/exposição de UiState e relação com ciclo de vida. |
| [Saved State no ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-savedstate) | Restauração condicionada de estado pequeno após recriação do processo, distinta de persistência do agregado. |
| [Coroutines no Android](https://developer.android.com/kotlin/coroutines/coroutines-best-practices) | Dispatchers fornecidos, trabalho seguro para Main e testabilidade. |
| [Consultas Room assíncronas](https://developer.android.com/training/data-storage/room/async-queries) | Operações pontuais suspensas e observação por Flow. |
| [Firestore offline](https://firebase.google.com/docs/firestore/manage-data/enable-offline) | Cache, listeners e mudança somente de metadados. O limite de completude do produto segue P5. |
| [DI manual](https://developer.android.com/training/dependency-injection/manual), [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) e [definições Koin](https://insert-koin.io/docs/reference/koin-core/definitions/) | Comparação de composição/grafo; a escolha para o projeto é julgamento arquitetural, não exigência dessas fontes. |
| [Navegação por eventos](https://developer.android.com/guide/navigation/use-graph/navigate), [rotas tipadas](https://developer.android.com/guide/navigation/design/type-safety) e [Navigation 3](https://developer.android.com/guide/navigation/navigation-3) | Diferenciar controle de pilha, argumentos e integração com a UI. Sem afirmar que Navigation 2 só admite strings ou que Navigation 3 exige determinada API citada na aula. |
| [Escopos ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel/viewmodel-apis), [NavBackStackEntry e compartilhamento](https://developer.android.com/guide/navigation/use-graph/programmatic), [grafos aninhados](https://developer.android.com/guide/navigation/design/nested-graphs) e [pilha/estado de navegação](https://developer.android.com/guide/navigation/backstack) | Conferidas na v0.2: proprietário pai e destino filho, retorno por resultado pequeno e término sem guardar sessão descartada para restauração. A política de edição/manual continua definida pelo projeto. |
| [PdfDocument](https://developer.android.com/reference/android/graphics/pdf/PdfDocument) e [compartilhamento de arquivo](https://developer.android.com/training/secure-file-sharing/share-file) | Viabilidade de geração local nativa e concessão de acesso ao arquivo. Não substituem as regras de elegibilidade DEC-032. |

## Rastreabilidade para a revisão

| Grupo | Cobertura arquitetural e onde revisar |
|---|---|
| Enunciado v2, p. 4; DEC-049 | Abertura animada no host de Presentation, registrada em [componentes](componentes.md) e na [especificação da interface](../interface/especificacao-telas.md#aberturasplash--exigência-do-enunciado); sem inventar novo RF/UC. |
| RF01–RF09; UC01–UC05 | Componentes/repositórios em [componentes](componentes.md); eventos/cache/compatibilidade em [fluxo de dados](fluxo-dados.md); VMs de catálogo/pessoal em [estado](gerenciamento-estado.md). |
| RF10–RF14; UC06–UC09 | Criação/snapshots, seleção por identidade, efetivos tipados, pares canônicos e edição em memória nos mesmos documentos. |
| RF15–RF20; UC10–UC11 | Contratos, bloqueio de consistência, integração e lacunas em [motores](motores-matematicos.md). |
| RF21–RF26; UC12–UC15 | Referência salva/edição em [estado](gerenciamento-estado.md); diagrama transacional, leitura consistente e exclusão em [fluxo de dados](fluxo-dados.md). |
| RF27; UC16 | Porta/host em [componentes](componentes.md); escolhas de versão, geração e falhas em [fluxo de dados](fluxo-dados.md). |
| RNF01/02 | Estados/erros por campo, confirmações e contratos de integração Figma em [estado](gerenciamento-estado.md). Usabilidade/TalkBack exigem validação própria posterior. |
| RNF03/04 | CPU, ausência de arredondamento, empates e protocolo DEC-026 em [motores](motores-matematicos.md). |
| RNF05/06/07/08 | Transações, offline disponível, leitura remota sem escrita pessoal e backup/transferência em [fluxo de dados](fluxo-dados.md). |
| RNF09/10/11 | Fronteiras/testes/pacotes na [arquitetura](arquitetura.md) e contratos nos capítulos; documentação navegável nesta pasta. |
| RNF12/13 | Trabalho entregue como diff revisável, sem commit, PR ou atualização de quadro nesta execução; gestão e integração futuras seguem DEC-031. |

## Resposta à revisão independente da v0.1

Parecer recebido: **“Revisão técnica — Etapa 4: Arquitetura do PDM AHP-TOPSIS”**, favorável com ajustes pontuais antes da aprovação, sobre os sete documentos v0.1 de 08/11/2026. O próprio parecer informa que analisou os anexos e não auditou o diff da branch. A resposta abaixo confronta os dois pontos contratuais com os arquivos **definitivos aprovados**, sem substituir contratos por estudos/propostas históricos.

| Apontamento | Resposta e tratamento na v0.2 | Sustentação definitiva ou documento refinado |
|---|---|---|
| 1 — Novo cadastro/UUID poderia ser exigência arquitetural adicional | **Esclarecido, sem alteração de regra.** Correção numérica sob o mesmo significado pode manter cadastro/UUID e suas definições. Incompatibilidade semântica exige novo cadastro com definições compatíveis e outro UUID. A tabela em fluxo de dados explicita os dois caminhos e a indeterminação. | [DEC-042 aprovada](../decisoes/aprovadas/dec-042.md) e [P5-E — caminho de correção de produtos pessoais](../modelagem/contratos/05-compatibilidade-offline.md#caminho-de-correção-de-produtos-pessoais). |
| 2 — Efetivo explícito poderia contornar original desconhecido | **Esclarecido, sem alteração de regra.** Separados original válido, canônico fora de domínio, realmente ausente e representação desconhecida/incompatível. O último bloqueia seleção até correção na fonte/nova leitura válida; efetivo válido não contorna o bloqueio. Ajustes explícitos continuam possíveis nas condições aprovadas. | [DEC-041 aprovada](../decisoes/aprovadas/dec-041.md) e [P5-D — original incompatível e fora de domínio](../modelagem/contratos/03-valores-serializacao.md#original-incompatível-e-original-fora-de-domínio). |
| 3 — Escopo concreto durante cadastro pessoal | **Refinado na v0.2.** Proprietário é o NavBackStackEntry da instância do grafo de avaliação; cadastro é filho com VM próprio; retorno remove só o formulário. Especificados Voltar, encerramento do grafo, manutenção da edição e recuperação limitada após morte do processo. | [Gerenciamento de estado — proprietário AndroidX](gerenciamento-estado.md#proprietário-androidx-da-sessão--arq-p03p05) e [Diagrama 5](gerenciamento-estado.md#diagrama-5--propriedade-e-ciclo-de-vida-da-sessão). |
| 4 — Caminho absoluto das anotações | **Substituído por identificação portável.** Nome do material, disciplinas/aulas, seções, hash e distinção AULA/MATERIAL/COMPLEMENTO foram preservados; nenhum diretório pessoal é necessário para identificar a fonte. | [Hierarquia de fontes e uso das anotações](#hierarquia-de-fontes-e-uso-das-anotações). |

Na rodada v0.2, o parecer recomendou aprovação de escolhas, sem aprovar ARQ-P01–ARQ-P06 em nome da equipe/usuário. A v0.2 consolidou módulo único/três camadas, DI manual, sessão refinada, casos de uso/motores independentes, Navigation Compose e PDF local como **propostas prontas para decisão**, conforme [quadro de aprovação](decisoes-pendentes.md). Nenhuma DEC nova foi registrada naquela rodada nem decisão anterior modificada. A delegação posterior do usuário e a adoção das escolhas estão registradas na v0.3.

## Histórico de verificação da v0.1

Os registros abaixo descrevem a execução original da documentação. A v0.1 foi posteriormente publicada por solicitação do usuário no commit `916ee6c`; não confundir esse histórico com as verificações da revisão v0.2.

- Análise da arquitetura anterior, decisões aprovadas, requisitos/casos de uso, domínio/estados, contratos P5, navegação/interface locais, enunciado e motor isolado. As anotações foram examinadas por temas, preservando classificação de evidência.
- Quatro blocos Mermaid verificados com **Mermaid 11.12.2**: `mermaid.parse` e `mermaid.render` em Chrome headless concluíram com sucesso para arquitetura geral, fluxo de processamento, persistência e dados remotos/locais. Runtime e HTML/relatórios ficaram somente em `/tmp`, sem dependências de build no projeto. Relatório local: `/tmp/pdm-etapa4-validacao-mermaid.json`.
- Conferência de **73 links locais** dos sete documentos, sem destinos ausentes, e cercas de código balanceadas. `git diff --check` executado sem erros; os seis arquivos novos também foram conferidos quanto a espaços finais e quebra de linha. Diff revisado sem mudanças em requisitos/decisões aprovadas.
- Comparação com hashes capturados antes das edições confirmou preservação dos **85 arquivos preexistentes de código, requisitos, modelagem e decisões**. Somente o índice arquitetural e uma linha de situação em `docs/README.md` foram editados entre os arquivos preexistentes por esta atividade. A Etapa 5 recebeu atualizações paralelas, incluindo um commit externo durante a execução; elas não foram revertidas nem incluídas como trabalho desta tarefa. Nenhum comando de commit foi executado por esta atividade.
- Não foram executados testes de Android, Room, Firebase, PDF, desempenho, acessibilidade ou motor. Não há build Gradle configurado para isso. Não houve alteração de classes Kotlin, configuração de bibliotecas/Firebase/Room, protótipos ou decisões aprovadas; nenhum commit foi criado.

## Verificações da v0.2

- **Cinco diagramas Mermaid:** parser e renderização com Mermaid 11.12.2 em Chrome headless concluídos sem erros, incluindo o novo diagrama de propriedade da sessão. Runtime e relatórios ficaram apenas em `/tmp`; relatório desta versão: `/tmp/pdm-etapa4-v02-validacao-mermaid.json`. Nenhuma dependência foi adicionada ao aplicativo.
- **90 referências locais, incluindo 11 âncoras de seções:** destinos e fragmentos conferidos nos sete documentos arquiteturais, sem referência ausente. Cabeçalhos/versionamento e cercas de código também foram conferidos. A referência acadêmica não contém caminho pessoal absoluto; a compilação continua externa ao Git e identificada pelo mesmo hash.
- **Diff completo revisado e `git diff --check` sem erros.** Oito arquivos modificados: os sete capítulos arquiteturais e a linha de situação da arquitetura em `docs/README.md`. Em componentes/motores, apenas cabeçalhos de versão/status foram atualizados; suas responsabilidades e método permanecem intactos.
- **131 outros arquivos preexistentes preservados**, conforme hashes capturados antes desta rodada, incluindo Etapa 5, requisitos, modelagem, decisões aprovadas e código. O HEAD permaneceu `916ee6c`; não houve commit, push ou alteração no registro de decisões nesta atividade.
- Nenhuma classe Kotlin, configuração Android/Firebase/Room, schema ou protótipo foi alterado. Não foram executados testes do aplicativo; os resultados acima verificam a documentação, não sua implementação futura.

## Adoção das escolhas e verificação da v0.3

Após os refinamentos, o usuário delegou expressamente a tomada de decisões conforme o enunciado em `references/` e `chatGPT.md`. Foram adotadas e registradas as DEC-043–DEC-048, resolvendo ARQ-P01–ARQ-P06. O [quadro de decisões](decisoes-pendentes.md) identifica, por escolha, a exigência do enunciado, o conteúdo **[AULA]**, o que aparece só em **[MATERIAL]** e os detalhamentos julgados adequados ao projeto. A identificação/hash das anotações permaneceu a mesma.

O enunciado v2 pede MVVM/Compose/NavHost e quatro responsabilidades separadas, sem impor Hilt/Koin ou um módulo Gradle por camada. A aula de 20/10 fundamenta pacotes/DI manual; 27/10 condiciona o framework ao porte; 06/10 e 09/10 fundamentam estado/navegação. A exportação continua uma ampliação aprovada na DEC-032: PdfDocument/FileProvider é escolha técnica do projeto, não exigência atribuída ao professor. DEC-018 e contratos definitivos de DEC-038–DEC-042 foram preservados.

- **18 documentos conferidos, 190 referências locais e 20 âncoras válidas**, incluindo os seis novos registros em `decisoes/adotadas/`. O hash das anotações foi reconferido e permaneceu o mesmo.
- **Cinco diagramas Mermaid com parser e renderização sem erros**, com runtime 11.12.2/Chrome headless em arquivos temporários. Relatório local: `/tmp/pdm-etapa4-v03-validacao-mermaid.json`. Nenhuma ferramenta foi incorporada ao build.
- **Diff revisado e `git diff --check` sem erros.** As alterações v0.2 foram mantidas e consolidadas; nesta adoção houve mudanças em 12 arquivos preexistentes e seis novos detalhamentos de decisão. Outros 127 arquivos preexistentes permaneceram íntegros, incluindo os 117 protegidos de código, requisitos, modelagem, decisões anteriores e interface. Os 42 resumos anteriores do registro central também foram preservados.
- Nenhuma implementação, configuração, alteração no Figma, commit ou push nesta atividade. O HEAD permaneceu `916ee6c`. A adoção delegada das escolhas não comprova execução de testes do aplicativo ou encerramento formal da etapa.

## Resposta à revisão independente da v0.3

Parecer recebido: **“Parecer técnico — arquitetura v0.3”**. A conclusão foi favorável à consistência/proporcionalidade da arquitetura, sem contradição bloqueante ou necessidade de reabrir DEC-043–DEC-048, com dois ajustes antes do encerramento documental. Os dois achados foram confrontados com o PDF, o texto da sessão e a interface; ambos foram aceitos. Não se identificou discordância a devolver ao revisor.

| Apontamento | Avaliação e correção na v0.4 | Rastreabilidade e limite |
|---|---|---|
| Importante — abertura animada omitida ou tratada como exemplo | **Procedente.** O item é exigência no Escopo do Projeto, p. 4; corrigida a classificação e registrada a DEC-049. Host Presentation, componentes, especificação, mapa e navegação passam a prever a abertura animada, sem impor os dois segundos ilustrativos. | [DEC-049](../decisoes/adotadas/dec-049.md), [enunciado](../../references/ProjetoDisciplina_PDM20262v2.pdf) e [abertura na interface](../interface/especificacao-telas.md#aberturasplash--exigência-do-enunciado). Na rodada arquitetural, Figma/HTML/manifesto não foram alterados. Posteriormente, a Etapa 5 acrescentou T00 ao HTML/manifesto; em 10/11, a página Figma 03 recebeu os quadros de abertura e conexões, com limites registrados na interface. |
| Melhoria — `RET → VAL` no Diagrama 5 pode pular seleção | **Procedente.** Corrigido para `RET → SEL`; `SEL → VAL` tem ação explícita de conferir seleção e avançar. Cancelamento ou sucesso de cadastro revela a mesma seleção, sem incorporar produto nem salvar avaliação por consequência da navegação. | [Diagrama 5](gerenciamento-estado.md#diagrama-5--propriedade-e-ciclo-de-vida-da-sessão) e adendo na DEC-047; texto do contrato já definia o comportamento correto, que foi preservado. |

A correção da abertura não muda os contratos DEC-041/042, o salvamento manual, os motores, as seis tabelas ou as seis escolhas técnicas anteriores. Os ajustes da Etapa 5 nesta rodada são estritamente documentais, sem alterar os protótipos ou seus inventários/evidências. Alinhamento documental registrado não equivale a protótipo aplicado e validado pela equipe.

**Complemento editorial da v0.4:** uma conferência posterior identificou que [design-system.md](../interface/design-system.md) ainda excluía a splash do escopo visual. A frase foi corrigida para reconhecer a exigência da DEC-049 e distinguir essa obrigação da representação ainda pendente nos protótipos. O arquivo não havia sido incluído entre os quatro documentos de interface conferidos na rodada original; esta correção complementa aquele alinhamento, sem alterar decisões ou protótipos.

Na verificação complementar, foram conferidos 24 documentos, 253 referências locais e 27 âncoras, sem falhas; `git diff --check` passou. Nenhum diagrama foi alterado nesse complemento, portanto a renderização dos oito blocos permanece a da rodada v0.4 registrada abaixo. As menções restantes à antiga entrada direta identificadas na busca descrevem o histórico da omissão, não a regra vigente.

## Verificações da v0.4

- **23 documentos conferidos, 248 referências locais e 26 âncoras válidas**, incluindo DEC-049 e os quatro documentos de interface alinhados. Hash das anotações reconferido, sem mudança.
- **Oito diagramas com parser e renderização Mermaid 11.12.2/Chrome headless sem erros:** cinco arquiteturais e três de navegação da interface. Inclui a seta corrigida do Diagrama 5 e a abertura no fluxo de navegação. Relatório local: `/tmp/pdm-etapa4-v04-validacao-mermaid.json`; ferramenta/artefatos temporários não foram incorporados ao projeto.
- **Diff revisado e `git diff --check` sem erros.** Nesta rodada foram atualizados 16 arquivos preexistentes e criado somente o detalhamento DEC-049; em fluxo de dados/motores, apenas a versão do documento mudou. Outros 129 arquivos preexistentes permaneceram íntegros, incluindo código, requisitos, modelagem, decisões aprovadas e todos os protótipos/scripts/manifestos da Etapa 5. A DEC-047 recebeu apenas adendo de rastreabilidade, sem troca da biblioteca ou do escopo.
- Nenhuma classe Kotlin, configuração Android/Firebase/Room ou protótipo foi alterado. Não foram executados testes do aplicativo. Não houve commit ou push; o HEAD permaneceu `916ee6c`. A concordância com os dois achados e o alinhamento documental não comprovam aplicação/validação da abertura no Figma/HTML nem encerram formalmente a etapa.

## Atualização editorial de interface — 09/11/2026

Os registros de v0.4 acima descrevem a execução histórica da arquitetura. A Etapa 5 evoluiu depois: `bd055c2` já contém T00 no HTML e no manifesto, com evidências de animação/transição e ausência de repetição em retorno/restauração. A [validação da interface](../interface/validacao.md) registra a rodada atual. Esse era o estado Figma em 09/11. Em 10/11, a API nativa via Firefox/Scripter permitiu criar a página 03; a [rodada atual da interface](../interface/finalizacao-v05.md) registra aplicação e apresentação. O limite MCP não representa mais ausência total de acesso ao arquivo. Essa distinção editorial não altera DEC-018/025/032/041–049, camadas, sessão ou persistência.

## Checklist de revisão independente e pendências

O parecer independente já foi recebido e respondido; as seis escolhas foram adotadas por delegação. Na conferência final da documentação e integração, verificar:

1. Se as três camadas e quatro responsabilidades atendem ao enunciado sem dependência de Android no Domain.
2. Se cada operação aprovada tem responsável e contrato, sem um caso de uso vazio por RF.
3. Se escopo de sessão, Voltar/cadastro, persistência manual, restauração limitada e respostas tardias estão coerentes.
4. Se snapshots, tipos/Long, originais/efetivos, revisões, limites de detecção e seis tabelas seguem DEC-038–DEC-042.
5. Se bloqueios AHP/TOPSIS, empates, sem diferenciação e lacunas do motor estão expostos sem declarar implementação pronta.
6. Se PDF usa apenas a versão salva escolhida e falhas de cada etapa preservam os fatos anteriores.
7. Se os contratos com Figma e os estados/retornos refletem DEC-043–DEC-049, incluindo abertura animada e retorno à seleção; registrar a aplicação/validação visual pendente e o encerramento formal da etapa quando efetivamente autorizado.

Após aprovação documental, implementar e testar os cenários do [plano vigente](../requisitos/plano-verificacao.md) e dos contratos P5: matemática de referência, fronteiras monetárias, atomicidade com falha provocada, cache parcial/permissões, mudança/exclusão de catálogo, ciclo de vida e saída/PDF. Continuam abertos os domínios/fontes/rubricas, schema/migrations/CHECK e versões de dependências. Não são apresentados como testes passados ou requisitos novos.

**Status final desta versão:** seis escolhas arquiteturais adotadas e formalizadas por delegação do usuário; permanecem alinhamento com Etapa 5, conferência/encerramento formal e tarefas técnicas futuras. A decisão não declara protótipos, aplicativo ou entrega ao professor concluídos.
