# Categorias e catálogo oficial

**Situação:** requisitos funcionais validados.

[Índice de requisitos](../README.md) · [Decisões](../../decisoes/registro-decisoes.md)

### RF01 — Consultar categorias de produtos
**Descrição:** O sistema deve apresentar ao usuário as categorias disponíveis no catálogo oficial para que ele escolha uma delas.

**Ator principal:** Usuário.

**Pré-condições:**

Aplicativo aberto; definições de categorias acessíveis remotamente ou em cache.
**Fluxo principal:**

Usuário acessa a área de catálogo.
Sistema consulta as categorias disponíveis.
Sistema apresenta nome e identificação visual das categorias.
Usuário pode selecionar uma categoria para abrir seus produtos.
**Fluxos alternativos e exceções:**

Nenhuma categoria disponível: apresentar estado vazio.
Falha remota e cache ausente: informar indisponibilidade e oferecer nova tentativa.
**Pós-condição:** Categoria disponível para consulta e seleção.

**Critérios de aceitação:**

Smartphones Premium e Veículos Elétricos aparecem quando presentes no catálogo.
O usuário pode abrir uma categoria.
Falhas de consulta não encerram inesperadamente o aplicativo.
**Rastreabilidade:** DEC-001, DEC-008, DEC-009, DEC-010.

### RF02 — Consultar produtos de uma categoria
**Descrição:** O sistema deve listar os produtos oficiais associados à categoria escolhida e permitir consultar seus detalhes.

**Ator principal:** Usuário.

**Pré-condições:**

Categoria válida selecionada.
**Fluxo principal:**

Usuário seleciona uma categoria.
Sistema consulta os produtos oficiais pertencentes a ela.
Sistema apresenta os produtos encontrados.
Usuário seleciona um item para consultar seus detalhes.
**Fluxos alternativos e exceções:**

Categoria sem produtos: informar ausência de registros.
Erro de rede: usar dados em cache, se disponíveis.
Produto incompleto: sinalizar campos ausentes sem inventar valores.
**Pós-condição:** Lista da categoria consultada ou situação de erro/estado vazio apresentada.

**Critérios de aceitação:**

A listagem não mistura produtos de categorias distintas.
É possível acessar os detalhes de cada produto.
Estados vazio, erro e indisponibilidade são tratados.
**Rastreabilidade:** DEC-001, DEC-008, DEC-009, DEC-010.

### RF03 — Consultar detalhes de um produto oficial
**Descrição:** O sistema deve exibir a identificação do produto, seus critérios, valores e unidades, além das informações conhecidas de origem e referência dos dados.

**Ator principal:** Usuário.

**Pré-condições:**

Produto oficial acessível no catálogo ou cache.
**Fluxo principal:**

Usuário seleciona um produto oficial.
Sistema apresenta nome, fabricante, modelo e versão, quando disponíveis.
Sistema exibe os cinco critérios da categoria e seus valores e unidades.
Sistema indica quando os dados provêm do cache e apresenta a data de referência/atualização conhecida, quando disponível.
**Fluxos alternativos e exceções:**

Campo opcional ausente: sinalizar ausência.
Critério obrigatório não informado: identificar dado incompleto.
Data de atualização desconhecida: não inventar nem sugerir confirmação recente.
**Pós-condição:** Detalhes do produto disponíveis apenas para consulta.

**Critérios de aceitação:**

Os valores são associados aos critérios e unidades corretos.
Os produtos oficiais não podem ser alterados pelo cliente Android.
Dados vindos do cache não são representados como atualização confirmada pelo servidor.
**Rastreabilidade:** DEC-009, DEC-014, DEC-017, DEC-020.

### RF04 — Consultar catálogo remoto no Firebase Cloud Firestore
**Descrição:** O sistema deve obter dados oficiais de categorias, critérios e produtos do Firestore e disponibilizar os metadados necessários à apresentação de sua origem.

**Ator principal:** Usuário (indiretamente); Firebase Cloud Firestore (sistema externo).

**Pré-condições:**

Aplicativo configurado para acesso ao projeto Firebase e regras de leitura.
**Fluxo principal:**

Aplicativo solicita dados ao repositório de catálogo.
Repositório consulta o Firestore.
Sistema interpreta e valida os documentos recebidos.
Sistema disponibiliza os dados e a informação de proveniência conhecida às telas.
**Fluxos alternativos e exceções:**

Sem rede: usar caminho de cache compatível com a consulta.
Permissão negada: apresentar erro tratado.
Documento incompatível: não exibir informações inválidas como se fossem válidas.
Consulta vazia: apresentar estado vazio.
**Pós-condição:** Dados oficiais disponíveis para uso ou erro representado adequadamente.

**Critérios de aceitação:**

É possível ler produtos e categorias existentes no Firestore.
O cliente não tem permissão para escrever no catálogo oficial.
A aplicação distingue conteúdo vindo do cache de conteúdo confirmado pelo servidor.
**Rastreabilidade:** DEC-009, DEC-010, DEC-020.

### RF05 — Atualizar catálogo e consultar dados offline
**Descrição:** O sistema deve buscar automaticamente atualizações do catálogo oficial e permitir consultar dados anteriormente disponíveis no cache quando a fonte remota estiver indisponível.

**Ator principal:** Usuário.

**Pré-condições:**

Dados disponíveis remotamente ou cópia em cache.
**Fluxo principal:**

Usuário acessa o catálogo.
Sistema inicia leitura dos dados disponíveis.
Havendo conexão, tenta obter informações atuais do servidor.
Sistema apresenta o catálogo e indica se as informações foram obtidas do cache.
Exibe data de referência ou de sincronização efetivamente conhecida, com sua identificação.
**Fluxos alternativos e exceções:**

Offline com cache: mostrar dados disponíveis e sinalizar sua origem.
Offline sem cache: informar indisponibilidade do catálogo.
Falha na atualização: preservar dados válidos disponíveis; não afirmar sincronização bem-sucedida.
Data desconhecida: omitir ou identificar como indisponível.
**Pós-condição:** Catálogo apresentado com proveniência coerente, quando disponível.

**Critérios de aceitação:**

Dados consultados anteriormente podem ser usados offline enquanto permanecerem no cache.
Dados do cache são identificados como tais.
Datas conhecidas não são inferidas apenas da origem do cache.
Atualizações do catálogo não alteram avaliações salvas.
**Rastreabilidade:** DEC-009, DEC-010, DEC-020.
