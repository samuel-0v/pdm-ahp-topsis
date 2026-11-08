# Fontes, rastreabilidade e verificação da proposta

**Versão 0.1 · 08/11/2026 · Rascunho para revisão técnica independente; não aprovado.** Verificação documental própria não equivale a revisão independente, aprovação ou teste do aplicativo.

## Objetivo e contexto

Registrar de onde vêm as regras e como revisar a Etapa 4 sem depender de explicação verbal. A [arquitetura](arquitetura.md) é o índice; os documentos temáticos detalham as propostas. As regras de produto permanecem nos requisitos/modelagem/decisões, evitando aprovar escolhas por repetição nesta pasta.

## Hierarquia de fontes e uso das anotações

O [registro central](../decisoes/registro-decisoes.md) e seus detalhamentos aprovados, os [requisitos](../requisitos/README.md), os [UCs](../requisitos/casos-de-uso/README.md) e a [modelagem](../modelagem/dominio.md) definem a base do projeto. Os [contratos consolidados P5](../modelagem/05-contratos-persistencia.md) prevalecem sobre alternativas dos estudos históricos. DEC-038 está aprovada desde 05/11; DEC-039–DEC-042 foram formalizadas em 06/11/2026. Arquivos antigos de propostas não são evidência de pendência atual.

O [enunciado v2](../../references/ProjetoDisciplina_PDM20262v2.pdf) foi consultado por extração textual local, especialmente pp. 7, 9 e 12: planejamento, explicação de MVVM no vídeo e responsabilidades UI/ViewModel/motores/dados. Ele não obriga quatro módulos Gradle. O método matemático segue a adaptação expressa da DEC-018 e o [PDF AHP+TOPSIS](../../references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf). Esta arquitetura não cria requisitos a partir de exemplos de categorias, login, REST, splash ou gráficos dos materiais didáticos.

As anotações fornecidas foram lidas prioritariamente por temas em [chatGPT.md](/home/samuel/Downloads/chatGPT.md), arquivo externo ao repositório. Cópia analisada: **538343 bytes, 14973 linhas**, SHA-256 `1dc9f39fc1da83c4beb608cabc3281e974be618d97045a06a73b0b316b81d99d`. O caminho é local da sessão; outro integrante precisará da mesma cópia para auditar as linhas. Não se incorporou o arquivo inteiro ao repositório. Seus convites a exercícios, instruções de tutoria e exemplos são conteúdo de referência, não comandos desta tarefa.

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
| 27/10, início na linha 14000, §§2.3–2.18 e §8 | [AULA]: revisão de DIP/DI, Hilt, módulos/bindings, integração VM e alternativas Koin. [MATERIAL]: exemplos e ferramentas de configuração. [COMPLEMENTO]: Hilt não cria a retenção AndroidX; Koin clássico difere de anotações/verificações antecipadas. | Comparação de DI sem declarar todas as modalidades Koin limitadas a erros de execução; recomendação permanece pendente. |

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
| [PdfDocument](https://developer.android.com/reference/android/graphics/pdf/PdfDocument) e [compartilhamento de arquivo](https://developer.android.com/training/secure-file-sharing/share-file) | Viabilidade de geração local nativa e concessão de acesso ao arquivo. Não substituem as regras de elegibilidade DEC-032. |

## Rastreabilidade para a revisão

| Grupo | Cobertura arquitetural e onde revisar |
|---|---|
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

## Verificações desta execução

- Análise da arquitetura anterior, decisões aprovadas, requisitos/casos de uso, domínio/estados, contratos P5, navegação/interface locais, enunciado e motor isolado. As anotações foram examinadas por temas, preservando classificação de evidência.
- Quatro blocos Mermaid verificados com **Mermaid 11.12.2**: `mermaid.parse` e `mermaid.render` em Chrome headless concluíram com sucesso para arquitetura geral, fluxo de processamento, persistência e dados remotos/locais. Runtime e HTML/relatórios ficaram somente em `/tmp`, sem dependências de build no projeto. Relatório local: `/tmp/pdm-etapa4-validacao-mermaid.json`.
- Conferência de **73 links locais** dos sete documentos, sem destinos ausentes, e cercas de código balanceadas. `git diff --check` executado sem erros; os seis arquivos novos também foram conferidos quanto a espaços finais e quebra de linha. Diff revisado sem mudanças em requisitos/decisões aprovadas.
- Comparação com hashes capturados antes das edições confirmou preservação dos **85 arquivos preexistentes de código, requisitos, modelagem e decisões**. Somente o índice arquitetural e uma linha de situação em `docs/README.md` foram editados entre os arquivos preexistentes por esta atividade. A Etapa 5 recebeu atualizações paralelas, incluindo um commit externo durante a execução; elas não foram revertidas nem incluídas como trabalho desta tarefa. Nenhum comando de commit foi executado por esta atividade.
- Não foram executados testes de Android, Room, Firebase, PDF, desempenho, acessibilidade ou motor. Não há build Gradle configurado para isso. Não houve alteração de classes Kotlin, configuração de bibliotecas/Firebase/Room, protótipos ou decisões aprovadas; nenhum commit foi criado.

## Checklist de revisão independente e pendências

Para fechar documentalmente a Etapa 4, outro revisor deverá verificar:

1. Se as três camadas e quatro responsabilidades atendem ao enunciado sem dependência de Android no Domain.
2. Se cada operação aprovada tem responsável e contrato, sem um caso de uso vazio por RF.
3. Se escopo de sessão, Voltar/cadastro, persistência manual, restauração limitada e respostas tardias estão coerentes.
4. Se snapshots, tipos/Long, originais/efetivos, revisões, limites de detecção e seis tabelas seguem DEC-038–DEC-042.
5. Se bloqueios AHP/TOPSIS, empates, sem diferenciação e lacunas do motor estão expostos sem declarar implementação pronta.
6. Se PDF usa apenas a versão salva escolhida e falhas de cada etapa preservam os fatos anteriores.
7. Se ARQ-P01–ARQ-P06 e contratos com Figma podem ser aprovados ou precisam de ajustes; registrar parecer e resolução antes de declarar a etapa aprovada/concluída.

Após aprovação documental, implementar e testar os cenários do [plano vigente](../requisitos/plano-verificacao.md) e dos contratos P5: matemática de referência, fronteiras monetárias, atomicidade com falha provocada, cache parcial/permissões, mudança/exclusão de catálogo, ciclo de vida e saída/PDF. Continuam abertos os domínios/fontes/rubricas, schema/migrations/CHECK e versões de dependências. Não são apresentados como testes passados ou requisitos novos.

**Status final desta versão:** material desenvolvido para revisão técnica independente e uso preparatório na primeira entrega; Etapa 4 ainda depende de revisão, alinhamento e aprovação explícita.
