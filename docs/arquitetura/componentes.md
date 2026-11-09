# Componentes e contratos arquiteturais

**Versão 0.4 · 08/11/2026 · Decisões técnicas adotadas por delegação do usuário.** Nomes orientam a implementação; não são classes Kotlin existentes.

## Objetivo e contexto

Distribuir RF01–RF27/UC01–UC16 entre Presentation, Domain e Data com o mínimo de abstrações necessário. A [arquitetura](arquitetura.md) define dependências; o [domínio](../modelagem/dominio.md) e os [contratos P5](../modelagem/05-contratos-persistencia.md) continuam a fonte dos modelos e regras. Não se cria um modelo de negócio concorrente nem uma interface para cada classe.

## Proposta e critério para casos de uso

Criar caso de uso quando houver validação de negócio, construção de snapshot, combinação de operações, elegibilidade ou transformação relevante de resultado. Consultas simples e exclusões protegidas pelo repositório podem ser chamadas pelo ViewModel através do contrato Domain, sem classes que apenas encaminham um método. Alterações triviais da edição usam operações puras no agregado/validador; não exigem uma classe `UseCase` por campo ou botão.

### Mapa de responsabilidades

`D` = Domain; `P` = Presentation; `Data` = infraestrutura. Erros são tipados; texto localizado pertence à apresentação.

| Operação | Camada e componente sugerido | Dependências | Entrada → saída conceitual | Requisitos/decisões |
|---|---|---|---|---|
| Apresentar abertura animada | P: host Android e apresentação da abertura | Identidade visual e mecanismo de splash/transição compatível | Lançamento → abertura animada → conteúdo inicial/destino recuperável válido, sem escrita de dados | Enunciado v2, p. 4; DEC-049; conduz a UC01/RF01, sem novo RF/UC |
| Listar categorias; consultar oficiais/lista/detalhe | D: `CatalogoRepository`; Data: `FirestoreCatalogoRepository` | Fonte Firestore, mapper, validação de representação/definição | Consulta/ID → fluxo de dados conhecidos, proveniência e problemas de disponibilidade/contrato | RF01–05; UC01/02; DEC-009/010/020/033/040 |
| Consultar pessoais/lista/detalhe | D: `ProdutoPessoalRepository`; Data: implementação Room | DAO, codec e mapper | Categoria/UUID → lista reativa ou produto com definições locais | RF07/09; UC04; DEC-004/042 |
| Cadastrar/editar pessoal | D: `SalvarProdutoPessoal` | Validador e repositório pessoal | Dados/definições; UUID existente se edição → produto salvo ou erros por campo | RF06/07/09; UC03/04; DEC-021/033/041/042 |
| Excluir pessoal | P confirma; D: contrato pessoal; Data: escrita local | Repositório pessoal/DAO | UUID confirmado → sucesso, não encontrado ou falha; snapshots intactos | RF08; UC05; DEC-012 |
| Criar avaliação | D: operação pura de criação do agregado | Definição válida; identidade/instante fornecidos | Categoria/definições conhecidas → avaliação em memória, UUID estável, sem gravação | RF10; UC06; DEC-022/025/036/041 |
| Selecionar/remover alternativas | D: `SelecionarAlternativa` e remoção pura | Validador de compatibilidade/identidade; produto consultado | Edição + candidato + definições de origem/destino → cópia de dados/proveniência na avaliação ou bloqueio | RF09/11/13/25; UC07; DEC-034/036/042 |
| Alterar valores | P interpreta entrada localizada; D valida/altera agregado | Conversão decimal exata; validador por metadados | Texto/critério/alternativa → efetivo tipado ou entrada pendente; original preservado, TOPSIS desatualizado | RF12/13/24/25; UC08/14; DEC-003/033/041 |
| Registrar/revisar julgamentos | D: operações puras do agregado | Normalizador canônico dos pares | IDs + sentido + intensidade 1–9 → par canônico; invalidação AHP/TOPSIS; progresso derivado | RF14; UC09; DEC-023/037/038 |
| Calcular AHP e verificar consistência | D: `CalcularAhp` | Validador e responsabilidade `AhpEngine` | Critérios/julgamentos completos → pesos por ID, λmax/IC/RI/RC e diagnóstico consistente/inconsistente, ou falha | RF15–17; UC10; DEC-018/037 |
| Executar TOPSIS e formar resultado | D: `ProcessarAvaliacao` | Validador, conversão, `TopsisEngine`, política de empates | Cópia imutável com AHP vigente consistente → ranking/sem diferenciação ou falha tipada | RF13/17–20/24; UC11/14; DEC-018/024/027/028/035/041 |
| Salvar avaliação | D: `SalvarAvaliacao`; Data: repositório transacional | Validador do agregado, `AvaliacaoRepository`, relógio fornecido | Cópia da edição → versão salva como rascunho/conclusão ou falha sem substituição parcial | RF21/24/25; UC12/14; DEC-025/038/039/041 |
| Listar avaliações | D: `AvaliacaoRepository`; Data: Room | DAO e mapper de resumo | Ordenação/data → Flow de resumos apenas da versão persistida | RF22; UC13; DEC-035 |
| Recuperar avaliação | D: `AvaliacaoRepository`; Data: leitura consistente | DAOs, mapper e validação de contrato/invariantes | UUID → agregado salvo íntegro, inexistente, formato não suportado ou inconsistência | RF20/23/25; UC13/14; DEC-039/042 |
| Excluir avaliação | P confirma; D: contrato; Data: transação | Repositório/DAO | UUID confirmado → exclusão de raiz/filhos ou falha atômica | RF26; UC15; DEC-019/038 |
| Preparar/exportar PDF | D: `ExportarAvaliacaoPdf`; Data: `GeradorRelatorioPdfAndroid` | Repositório de avaliações, elegibilidade, porta PDF, relógio | Versão salva escolhida → documento local gerado ou erro; sem envio implícito | RF27; UC16; DEC-032 |
| Compartilhar PDF | P: host Android | Referência convertida em URI autorizada pelo adaptador Android | Documento pronto + ação explícita → abertura do seletor ou falha/cancelamento, sem comprovar envio | RF27; UC16; RNF08; DEC-032 |

O validador semântico pode ser função/objeto puro compartilhado, sem hierarquia de validadores. `SelecionarAlternativa` recebe candidato já consultado, sem exigir nova busca remota. `SalvarProdutoPessoal` reúne regras que uma escrita DAO não protege. Exclusão não ganha caso de uso artificial: atomicidade e garantias pertencem ao repositório; confirmação visual pertence à apresentação.

## Contratos de repositório no Domain

| Contrato | Operações conceituais | Garantias e limites |
|---|---|---|
| `CatalogoRepository` | Observar categorias, produtos por categoria e detalhe como `Flow` de eventos; solicitar nova tentativa/atualização | Somente leitura. Modelos/proveniência independentes de Firebase. Problemas de documento podem coexistir com itens válidos; itens incompatíveis não ficam elegíveis à seleção. Não mesclar revisões sem verificar compatibilidade. |
| `ProdutoPessoalRepository` | Observar lista (`Flow`); obter, salvar e excluir (`suspend`) | Preserva UUID/definições ao editar, exige produto completo e não modifica avaliações. Não consulta Firestore para editar dado histórico local. |
| `AvaliacaoRepository` | Observar resumos (`Flow`); carregar agregado, salvar cópia completa e excluir (`suspend`) | Leituras/escritas coerentes; invalidações/exclusão atômicas; snapshots independentes de catálogos; nenhuma escrita implícita ao carregar. Sucesso só após commit local. |

Os contratos não retornam Entity Room, Snapshot Firebase, Task, JSON bruto ou exceção SDK como API de negócio. Uma leitura pontual inicia a sessão; observar Room e substituir a edição a cada emissão perderia alterações. A lista pode observar Room continuamente. Proveniência integra o evento de catálogo: mudança apenas de metadados também deve chegar à UI.

Não haverá `BaseRepository`, CRUD universal, repositório por tabela nem repositório de resultado independente da avaliação. Resultados compõem o agregado e sua transação (DEC-036/039). Categorias e produtos oficiais ficam em um contrato coeso.

## Modelos de fronteira e erros

Manter os modelos aprovados: avaliação raiz, critérios preservados, alternativas/valores/julgamentos e objetos de resultado. DTO Firestore e Entity Room existem em Data porque representam formatos externos reais. UiState é projeção para a tela; não é obrigatória uma cópia de cada modelo por camada.

| Situação | Tratamento |
|---|---|
| Dado inválido/incompleto | Problemas por critério/alternativa/par, com identidade e causa; edição permanece. Rascunho incompleto é aceito dentro das invariantes. |
| AHP inconsistente | Diagnóstico matemático válido com RC > 0,10; bloqueia TOPSIS, não é falha de execução. |
| Indisponibilidade/cache insuficiente | Manter dados válidos conhecidos, se houver; não converter em sucesso vazio confirmado. |
| Acesso negado | Erro próprio, sem rotular como catálogo vazio ou rede offline. |
| Representação desconhecida/agregado incoerente | Bloquear uso como íntegro e operações dependentes; não completar remotamente, reparar ou salvar automaticamente. |
| Incompatibilidade semântica/indeterminação | Bloquear reutilização dependente conforme DEC-042; confirmação visual não dispensa a regra. |
| Falha numérica/perda de resolução | Identificar problema técnico; nenhum ranking/empate/sem diferenciação fictício. |
| Falha de armazenamento/PDF/compartilhamento | Informar a etapa; preservar sucessos anteriores já confirmados. |
| Cancelamento de corrotina | Propagar; não mostrar como falha de negócio nem publicar sucesso tardio. |

Códigos concretos de erro dependem da revisão. Usar resultados tipados para falhas esperadas e capturar falhas inesperadas no limite apropriado, sem engolir cancelamento. Textos localizados ficam em Presentation e logs não devem expor dados pessoais.

## Contrato da exportação

`ExportarAvaliacaoPdf` lê cópia consistente da versão persistida selecionada, valida formato/invariantes e elegibilidade e prepara conteúdo. Não chama motores nem catálogo. `GeradorRelatorioPdf` recebe conteúdo e instante de geração e retorna referência de documento independente de Android (por exemplo, identificador opaco e nome/MIME), nunca `Context` ou `Uri` no Domain.

Data implementa geração/arquivos com PdfDocument nativo, conforme DEC-048, sem biblioteca PDF externa inicial. Presentation transforma a referência em URI pelo adaptador Android e abre o seletor com acesso temporário. Paginação e limpeza de temporários exigem detalhamento técnico; a escolha da API está adotada na DEC-048 (origem ARQ-P06). As três opções da DEC-032 estão no [fluxo de dados](fluxo-dados.md).

## Justificativas, pendências e aprovação

A distribuição atende RNF09/10 com contratos substituíveis/regras puras e preserva agregado/seis tabelas. As DEC-045/046/048 adotam escopos, fronteiras do motor e infraestrutura PDF por delegação do usuário. A quantidade final de classes seguirá responsabilidades reais, não a quantidade de RFs. A escolha está definida documentalmente; nenhum componente foi implementado nesta atividade. Permanecem detalhamentos/testes de implementação e alinhamento com Etapa 5.
