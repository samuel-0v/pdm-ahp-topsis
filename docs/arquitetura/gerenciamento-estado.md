# ViewModels, estado e execução assíncrona

**Versão 0.1 · 08/11/2026 · Rascunho para revisão técnica independente; não aprovado.** Escopos e nomes são propostas ARQ-P03/P05, sem ViewModels implementados.

## Objetivo e contexto

Aplicar MVVM e fluxo unidirecional aos [casos de uso](../requisitos/casos-de-uso/README.md), respeitando as dimensões da [Parte 4](../modelagem/04-estados-transicoes.md) e o salvamento manual DEC-025. As aulas de 06/10 e 09/10 fundamentam estado, ciclo de vida e compartilhamento; políticas específicas abaixo são propostas do projeto, conforme [fontes](referencias-e-validacao.md).

## Proposta de ViewModels

Não criar um ViewModel por frame Figma ou por par AHP. Uma sessão de avaliação compartilha estado entre seleção, valores, comparações, resumo e resultado. Consultar uma avaliação salva pode usar o mesmo ViewModel em modo de consulta, sem marcar alteração por abrir.

| ViewModel e escopo | Estado observado em StateFlow | Eventos principais | Operações assíncronas e dependências |
|---|---|---|---|
| `CatalogoViewModel`, fluxo de catálogo | Categorias, categoria selecionada, oficiais/pessoais, detalhe consultado, origem/cache, datas conhecidas, carregamento, atualização, erros/vazio | Selecionar categoria/produto, tentar novamente, abrir cadastro, solicitar criação de avaliação | Observa contratos de catálogo/pessoais; troca consulta por categoria cancelando a anterior; não mantém a edição da avaliação. |
| `ProdutoPessoalViewModel`, formulário | Identificação, categoria fixa após escolha/cadastro, valores/definições locais, erros por campo, alteração pendente, salvando/excluindo, confirmação | Alterar campo, salvar, solicitar/confirmar exclusão, tentar sair, descartar/continuar | Carrega local quando edição; chama `SalvarProdutoPessoal` e contrato de exclusão. Só retorna ID após sucesso; falha mantém formulário. Novo cadastro exige definições suficientes, não necessariamente rede. |
| `AvaliacoesViewModel`, lista | Resumos efetivamente salvos com os três estados persistidos, carregando/vazio/erro, exclusão em curso/confirmação | Abrir, criar, tentar novamente, solicitar/confirmar/cancelar exclusão | Observa `AvaliacaoRepository`; exclusão confirmada por contrato. Não injeta resultados em memória na lista salva. |
| `AvaliacaoViewModel`, sessão inteira | Edição, referência da última salva, alterações pendentes, etapa/par atual, estados AHP/TOPSIS, erros por campo/tentativa, operação em curso, diálogos e PDF disponível | Criar/carregar, selecionar/remover, alterar efetivo, registrar julgamento, calcular AHP, processar, salvar, pedir saída/PDF, escolher opção, descartar/continuar | Casos de uso de seleção, AHP, processamento, salvamento/exportação; repositório para carregar; consultas de candidatos pelos contratos quando necessárias. Cálculos e acesso externo em corrotinas controladas. |

Todos traduzem erros tipados em informação de UI, preservando dados válidos conhecidos. Não usam uma única variante global `Error` que apague formulário ou resultado. Carregamento inicial pode ser exclusivo; atualizar, salvar e exportar são estados de operação associados ao conteúdo já visível. AHP inconsistente é diagnóstico exibível, não tela genérica de falha.

## Quatro responsabilidades de estado

| Estado | Proprietário/fonte | Duração e regra |
|---|---|---|
| Visual local | Compose: expansão de seção, foco, rolagem, visibilidade puramente visual | `remember` quando efêmero; `rememberSaveable` para pequenos valores elegíveis que valha restaurar. Não contém a cópia autoritativa do agregado. |
| Apresentação | ViewModel: UiState imutável exposto como `StateFlow`, etapa/par, operação, erros e confirmação | Retido enquanto o proprietário AndroidX existir, inclusive recriação por configuração. Não é sinônimo de dado persistido. |
| Edição ainda não salva | Agregado/cópia de trabalho em memória, pertencente à sessão no `AvaliacaoViewModel` | Inclui valores originais/efetivos, entradas pendentes, julgamentos e resultados novos. Não escreve Room por mudança de campo, navegação ou cálculo. |
| Última versão salva | Room, lida pelo repositório; cópia de referência imutável na sessão | Só muda por salvamento explícito ou exclusão confirmada com sucesso. A lista e a exportação usam essa fonte, não a edição. Antes do primeiro salvamento ela inexiste. |

UiState projeta a edição e a referência salva; não manter duas edições mutáveis desconectadas. Ao retomar, ler uma vez, validar e copiar o agregado salvo para a sessão. Emissões da lista Room não sobrescrevem essa sessão. Atualizações do catálogo só afetam candidatos ainda não incorporados, nunca snapshots já selecionados.

`temAlteracoesNaoSalvas` compara conteúdo canônico relevante a salvar, incluindo resultados e texto pendente, com a referência da última gravação. Uma avaliação nova é pendente mesmo vazia de alternativas. Abrir/navegar/rolar não marca mudança. Calcular resultado novo/diferente pode marcar pendência mesmo sem alterar entradas; recalcular resultado idêntico não exige marcar diferença só pela tentativa. Indicadores visuais, datas de tentativa e loading não participam dessa igualdade. Data de salvamento só é atribuída ao comando explícito, não à digitação.

## Contrato de eventos e fluxo unidirecional

1. Composable recebe estado e callbacks; emite intenção identificando critério, alternativa ou par por ID.
2. ViewModel aplica alteração pura ou chama caso de uso. Regras e matrizes não ficam em Composable; ViewModel não duplica matemática.
3. O resultado modifica a cópia em memória e os indicadores de validade/pendência, de forma coerente.
4. ViewModel expõe novo UiState somente de leitura. A UI coleta com `collectAsStateWithLifecycle()` e renderiza.

O estado mutável fica encapsulado. Coleta vinculada ao ciclo de vida da UI não tem o mesmo ciclo de `viewModelScope`. Parar a coleta em segundo plano não destrói automaticamente o ViewModel nem salva a edição. A produção de estado e a coleta são responsabilidades distintas, como documenta o [guia Android](https://developer.android.com/topic/architecture/ui-layer/state-production).

Confirmação de saída, confirmação de exclusão e escolha de PDF ficam no UiState até resposta explícita. Mensagens que precisam sobreviver à rotação também ficam em estado até reconhecimento. Navegação/abertura do compartilhamento são executadas pelo host Android mediante solicitação identificada e reconhecimento de consumo, sem refazer a ação por recomposição. Não atribuir garantia universal de entrega única a `Channel` ou `SharedFlow`: queda de processo pode perder a solicitação. Um PDF gerado pode voltar a ser oferecido para compartilhar por ação explícita; não reenviar automaticamente ao restaurar tela.

## Invalidação, salvar e saída

| Evento | Efeito em memória | Efeito no Room |
|---|---|---|
| Alterar julgamento | Desatualiza AHP e TOPSIS; pode manter resultado antigo apenas identificado como desatualizado | Nenhum até salvar. |
| Alterar alternativas/valores | Desatualiza TOPSIS; preserva AHP se julgamentos/critérios permanecem | Nenhum até salvar. |
| Calcular AHP | Atualiza pesos/indicadores e diagnóstico; mudança de pesos ou perda de consistência desatualiza TOPSIS. Diagnóstico inconsistente continua salvável em rascunho | Nenhum. |
| Processar TOPSIS | Publica ranking ou sem diferenciação válido, se operação ainda corresponder à edição | Nenhum. |
| Salvar explicitamente | Valida cópia congelada; após sucesso atualiza referência salva e pendência | Transação integral, incluindo limpezas de resultados desatualizados. |
| Falhar ao salvar | Mantém edição e pendência; não autoriza saída nem exportação da edição | Versão anterior preservada em falha transacional confirmada. |
| Descartar | Restaura cópia da última salva ou abandona nova avaliação; pode sair após essa escolha | Nenhuma gravação/exclusão. |

Ao encerrar sessão pendente, apresentar **Salvar**, **Descartar alterações** e **Continuar editando**. Salvar escolhe rascunho ou conclusão conforme validade atual e só permite sair após confirmação; falha permanece na tela. Voltar entre etapas mantém a sessão. Abrir edição de concluída não invalida seus resultados. Rascunhos podem preservar AHP inconsistente atual, sem TOPSIS. O [diagrama de persistência](fluxo-dados.md) detalha sucesso/falha.

Proibir gravação por `LaunchedEffect` observando campos, por debounce, `onStop`, `onCleared`, recomposição, mudança de rota ou atualização de StateFlow. Não usar tabela de rascunho automático, DataStore ou arquivo oculto para contornar DEC-025. O texto inválido permitido em rascunho só é persistido por salvar explícito, conforme P5-D.

## Coroutines, Flow e concorrência

| Mecanismo | Uso proposto | Limite |
|---|---|---|
| `suspend` | Carregar agregado pontual, gravar/excluir e gerar documento | Pode suspender; não troca automaticamente de thread nem torna um cálculo bloqueante seguro para Main. |
| `Flow` | Emissões de listas Room e catálogo/metadata remotos | Representa sequência; não significa estado da tela nem gravação automática. Cancelar coleta remota remove listener. |
| `StateFlow` | Último UiState de cada ViewModel, publicamente somente leitura | Estado atual, não fila confiável de todos os eventos. Não usar para transmitir cada escrita de banco. |
| Função comum | Alteração pequena da edição, validação trivial e construção de evento | Não iniciar corrotina para mudar booleano, avançar par ou atualizar campo simples. |

Consultas Firestore usam integração assíncrona do SDK; adaptar listener para Flow cancelável, com remoção ao cancelar e eventos de metadata habilitados. Retentativas são controladas; troca de categoria cancela a observação anterior. O SDK cuida da comunicação, sem bloquear com espera síncrona. Mapeamento volumoso pode ir para dispatcher de CPU.

Room expõe consultas observáveis e operações suspensas; o adaptador usa as APIs assíncronas/transacionais e executores do Room. Não envolver todo método em novas corrotinas ou `IO` por hábito. Código realmente bloqueante de arquivos/PDF usa dispatcher apropriado fornecido. Serialização pesada e cálculo usam CPU (`Default` injetado), preservando motores como funções puras síncronas. Atualizações coordenadas do UiState ocorrem no contexto do ViewModel.

`viewModelScope` inicia os trabalhos ligados à sessão. `CalcularAhp`/`ProcessarAvaliacao` podem encapsular `withContext` no dispatcher de CPU recebido, sem importar Android e sem scope global. Dispatchers são fornecidos por DI para testes determinísticos, conforme [boas práticas Android](https://developer.android.com/kotlin/coroutines/coroutines-best-practices).

### Política inicial de operações concorrentes

- Cada cálculo captura entradas imutáveis e um identificador de geração da edição. Mudança relevante, novo cálculo, descarte ou encerramento cancela/invalida o trabalho anterior. A resposta só entra no UiState se ainda corresponder à geração ativa; um Job cancelado de CPU pode terminar antes de observar cancelamento, portanto cancelar sozinho não basta.
- AHP e TOPSIS não publicam parcialmente sobre a mesma edição. TOPSIS exige AHP vigente consistente; se ausente/desatualizado, direcionar à validação AHP. Não encadear processamento usando pesos antigos.
- Proposta simples: durante salvar, excluir ou preparar PDF, serializar comandos conflitantes da mesma avaliação e bloquear mutações/novas gravações até o retorno. Não congelar Main. Antes de salvar, invalidar respostas de cálculos ainda em curso. Não há múltiplos editores simultâneos do mesmo agregado nesta proposta.
- Ao salvar com sucesso, a referência é o conteúdo confirmado pelo repositório, inclusive os resultados/limpezas realmente gravados. Erro ou cancelamento não pode limpar pendência nem navegar como sucesso. A transação Room é atômica; se o processo morrer após commit e antes da confirmação visual, o próximo carregamento consulta o banco para conhecer o fato. Não prometer que todo cancelamento tardio desfez um commit já ocorrido.
- Encerrar sessão cancela cálculos/leituras e invalida respostas tardias. Salvar para sair mantém o proprietário ativo até confirmação. Rotação não cancela automaticamente o scope retido. Não há trabalho persistente em segundo plano nem necessidade atual de WorkManager.

Falha de rede conserva cache válido conhecido com aviso, sem declarar sincronização. Falha local mantém conteúdo e oferece nova tentativa. Erros matemáticos indicam causa sem produzir conclusão. Propagar `CancellationException`, evitando captura genérica que a converta em sucesso/falha de negócio.

## Mudança de configuração e morte do processo

O ViewModel retido pelo mesmo proprietário preserva a sessão durante rotação, mas perde-se ao terminar o proprietário ou morrer o processo. O mecanismo AndroidX retém ViewModels; DI apenas ajuda a criá-los.

Proposta ARQ-P03: `SavedStateHandle` guarda apenas pequenos identificadores/parâmetros de recuperação (avaliação salva, categoria, etapa/par quando reconstruíveis), sem serializar a edição ou resultados não salvos. Após morte do processo, uma avaliação já salva pode ser recarregada integralmente do Room; edição posterior pode se perder. Uma avaliação nova nunca salva não pode ser recuperada pelo seu UUID sozinho. Etapa restaurada deve ser validada contra o conteúdo realmente recuperado; não prometer retorno a um resultado não salvo. Sem registro salvo ou definições suficientes, oferecer reinício/retorno seguro, sem criar rascunho.

`rememberSaveable`/`SavedStateHandle` são restauração condicionada ao estado fornecido pelo Android e a seus limites, não backup permanente. O aviso ao sair cobre saída controlada; não há garantia de mostrar diálogo antes de encerramento inesperado. Essa limitação já é aceita em RNF05/DEC-025 e precisa aparecer na comunicação do produto.

## Contrato com navegação e Etapa 5

Destinos são identificados conceitualmente, sem strings/rotas fixadas. Passar IDs e origem, não matrizes/objetos inteiros no back stack. Para avaliação nova, compartilhar a sessão em memória e sua categoria; UUID ainda não salvo não é uma referência recuperável pelo Room.

O proprietário compartilhado deve permanecer vivo durante todo o assistente e no cadastro pessoal aberto a partir da seleção. Com Navigation 2 pode ser uma entrada do grafo da avaliação; com Navigation 3 é necessário definir proprietário/escopo que não seja destruído ao substituir a entrada da etapa. A solução concreta depende de ARQ-P05; Activity global não é escolhida por conveniência. Retorno do cadastro informa o ID do produto salvo à sessão; não salva nem seleciona automaticamente avaliação por efeito de recomposição.

| Contrato a alinhar com Figma | Estados e ações que precisam de representação |
|---|---|
| T01–T04/T06: catálogos e seleção | Carregando, vazio confirmado, cache parcial/indisponível, permissão negada, dados incompatíveis, origem, seleção mista e confirmação de gravação do pessoal. |
| T07/T08/T09: valores e AHP | Original/efetivo, entrada pendente, erros por critério, dez pares atuais, progresso/revisão, RC precisa e bloqueio por inconsistência. |
| T10/T11/T12: cálculo e resultado | Em processamento sem atraso artificial, cancelamento/Voltar, falha numérica/resolução, resultado desatualizado, empates e sem diferenciação. |
| T05/T12: dados salvos | Distinguir resultado em memória da última salva e seus três estados; ausência de versão salva; edição pendente por resultados novos. |
| D01–D04 e T13 | Saída com três escolhas; exportação com três escolhas; exclusão com confirmação; sucesso somente após commit; falhas/cancelamento e sem afirmar envio no compartilhamento. |
| Preservação e navegação | Voltar entre etapas preserva edição; saída aplica aviso; cadastro durante seleção retém sessão; morte do processo recupera somente o que a política permite. |

IDs seguem o [mapa em elaboração](../interface/mapa-telas.md); não são contratos de rotas. A arquitetura não altera protótipos. Após revisão, alinhar os fluxos de [navegação](../interface/navegacao.md), sem aguardar o encerramento visual para validar responsabilidades.

## Justificativas, pendências e aprovação

Um ViewModel de sessão evita cópias concorrentes de uma avaliação; ViewModels separados para catálogos/formulário/lista evitam estado global excessivo. Estado imutável e comandos explícitos tornam DEC-025/039 verificáveis. Revisar ARQ-P03 (escopo, restauração e serialização de comandos) e ARQ-P05 com a equipe/Figma. Não houve implementação nem aprovação desses contratos.
