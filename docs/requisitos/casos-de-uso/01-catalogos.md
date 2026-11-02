# Casos de uso — Catálogos

**Versão:** 0.1 — rascunho, proposta de consolidação para revisão.

Base: RF01–RF26, RNF01–RNF13 e DEC-001–DEC-031 aprovados. O detalhamento ainda aguarda revisão; não registra novas decisões nem implementação.

[Índice dos casos de uso](README.md) · [Rastreabilidade](../rastreabilidade.md)

## UC01 — Consultar catálogo oficial

**Objetivo:** encontrar os produtos de uma categoria e reconhecer a origem dos dados.
**Ator principal:** usuário. **Ator de apoio:** Firebase Cloud Firestore, nas consultas remotas.
**Gatilho:** abrir o catálogo ou selecionar uma categoria.
**Pré-condições:** aplicativo aberto; disponibilidade dos dados verificada durante a consulta.
**Requisitos:** RF01, RF02, RF04, RF05; RNF01, RNF02, RNF06, RNF07.

**Fluxo principal:**

1. O usuário abre o catálogo.
2. O sistema obtém categorias e critérios disponíveis e apresenta as categorias.
3. O usuário escolhe uma categoria.
4. O sistema consulta seus produtos e tenta obter dados atuais quando há conexão.
5. O sistema apresenta os produtos, identifica dados vindos do cache e informa a data conhecida com seu significado, quando disponível.
6. O usuário pode abrir um produto pelo UC02 ou iniciar uma avaliação pelo UC06.

**Alternativas e exceções:**

- A1 — Sem rede com cache: apresentar apenas os dados disponíveis, identificando a origem.
- A2 — Sem dados acessíveis: informar indisponibilidade e permitir nova tentativa; não representar erro de permissão como catálogo vazio.
- A3 — Consulta válida sem registros: apresentar estado vazio.
- A4 — Documento incompatível ou incompleto: sinalizar indisponibilidade/incompletude sem fabricar valores nem oferecer dados inválidos como válidos.
- A5 — Falha ao atualizar: preservar dados válidos disponíveis sem afirmar atualização bem-sucedida; data desconhecida não é estimada.

**Pós-condição de sucesso:** catálogo consultado com origem coerente. Nenhuma avaliação salva é atualizada pela consulta.
**Verificação:** servidor, cache parcial, ausência de cache, consulta vazia e permissão negada devem produzir estados distinguíveis.

## UC02 — Consultar detalhes de produto oficial

**Objetivo:** conhecer os dados e as referências de um produto antes da comparação.
**Ator principal:** usuário. **Ator de apoio:** Firestore quando for necessário obter dados remotos.
**Gatilho:** selecionar um produto oficial.
**Pré-condição:** produto acessível no catálogo ou cache.
**Requisitos:** RF03; RNF01, RNF02, RNF06, RNF07.

**Fluxo principal:**

1. O usuário seleciona o produto.
2. O sistema apresenta identificação, fabricante, modelo e versão quando disponíveis.
3. Apresenta os cinco critérios da categoria, seus valores e unidades.
4. Exibe referências conhecidas dos dados, origem em cache e data conhecida quando aplicável.
5. O usuário consulta os dados e retorna ao catálogo.

**Alternativas e exceções:**

- A1 — Campo ausente: identificar a ausência; distinguir informação opcional de critério necessário ao processamento.
- A2 — Produto inacessível: explicar a indisponibilidade sem produzir detalhes fictícios.
- A3 — Tentativa de edição de produto oficial: não oferecer escrita; as regras do Firestore também a bloqueiam.

**Pós-condição:** produto consultado sem alteração do catálogo.
**Verificação:** valores associados às unidades corretas e dados em cache nunca apresentados como confirmação recente do servidor.

## UC03 — Cadastrar produto pessoal

**Objetivo:** registrar um produto local reutilizável em avaliações.
**Ator principal:** usuário.
**Gatilho:** solicitar cadastro no catálogo pessoal ou durante a seleção de alternativas.
**Pré-condição:** categoria e critérios disponíveis localmente ou obtidos do catálogo.
**Requisitos:** RF06, RF09; RNF01, RNF02, RNF06, RNF08.

**Fluxo principal:**

1. O usuário escolhe a categoria do novo produto.
2. O sistema apresenta identificação e os cinco critérios correspondentes.
3. O usuário informa identificação e valores e solicita salvar.
4. O sistema valida os campos obrigatórios e valores conforme as regras definidas.
5. O sistema persiste o produto localmente e confirma sucesso após a gravação.
6. O produto fica disponível para avaliações da mesma categoria, inclusive na seleção que originou o cadastro.

**Alternativas e exceções:**

- A1 — Entrada ausente ou inválida: indicar campos e impedir cadastro até correção.
- A2 — Cancelamento: não criar produto.
- A3 — Falha de gravação: informar que o produto não foi salvo.
- A4 — Offline: permitir cadastro se as definições necessárias estiverem disponíveis; caso contrário, explicar indisponibilidade.

**Pós-condição de sucesso:** produto local persistido, com categoria imutável, sem escrita no Firestore.
**Verificação:** reabrir após reinício e reutilizar o produto em mais de uma avaliação; ajustes nessas avaliações não alteram o cadastro.

## UC04 — Consultar e editar produto pessoal

**Objetivo:** manter identificação e valores de um produto local.
**Ator principal:** usuário.
**Gatilho:** selecionar produto no catálogo pessoal.
**Pré-condição:** produto pessoal persistido.
**Requisitos:** RF07, RF09; RNF01, RNF02, RNF06, RNF08.

**Fluxo principal:**

1. O usuário consulta o produto e solicita editar.
2. O sistema apresenta os dados atuais e a categoria somente para consulta.
3. O usuário modifica nome, fabricante, modelo ou valores dos critérios.
4. Solicita salvar; o sistema valida e grava as alterações localmente.
5. O sistema confirma a gravação e apresenta os dados atualizados.

**Alternativas e exceções:**

- A1 — Entrada inválida: permitir correção sem confirmar atualização.
- A2 — Cancelamento: preservar registro original.
- A3 — Falha de gravação ou leitura: informar falha sem afirmar sucesso.
- A4 — Outra categoria desejada: utilizar novo cadastro pelo UC03; não alterar a categoria existente.

**Pós-condição de sucesso:** cadastro atualizado, categoria e avaliações anteriores preservadas.
**Verificação:** editar o cadastro e reabrir avaliação anterior; ela deve manter os valores usados originalmente.

## UC05 — Excluir produto pessoal

**Objetivo:** remover produto do catálogo pessoal sem afetar avaliações existentes.
**Ator principal:** usuário.
**Gatilho:** solicitar exclusão de produto pessoal.
**Pré-condição:** produto pessoal existente.
**Requisitos:** RF08, RF09, RF25; RNF01, RNF06, RNF08.

**Fluxo principal:**

1. O usuário solicita excluir o produto.
2. O sistema pede confirmação explícita.
3. O usuário confirma.
4. O sistema remove o produto local e atualiza a lista após sucesso.

**Alternativas e exceções:**

- A1 — Cancelar confirmação: preservar produto.
- A2 — Falha na exclusão: informar falha sem afirmar sucesso.
- A3 — Produto usado em avaliações: preservar as cópias já salvas nessas avaliações.

**Pós-condição de sucesso:** produto não é oferecido em novas seleções; avaliações anteriores e catálogo oficial permanecem intactos.
**Verificação:** confirmar e cancelar exclusões; após exclusão confirmada, consultar uma avaliação que utilizou o produto.
