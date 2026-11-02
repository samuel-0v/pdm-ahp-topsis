# Catálogo pessoal

**Situação:** requisitos funcionais validados.

[Índice de requisitos](../README.md) · [Decisões](../../decisoes/registro-decisoes.md)

### RF06 — Cadastrar produto pessoal
**Descrição:** O sistema deve permitir cadastrar produtos pessoais em categorias existentes, fornecendo identificação e os valores necessários à avaliação.

**Ator principal:** Usuário.

**Pré-condições:**

Categoria existente selecionada; critérios da categoria disponíveis.
**Fluxo principal:**

Usuário inicia cadastro e define a categoria.
Informa os dados de identificação e valores dos critérios.
Solicita o salvamento do produto.
Sistema valida o preenchimento e persiste o registro no Room.
O produto passa a integrar o catálogo pessoal da categoria.
**Fluxos alternativos e exceções:**

Campos obrigatórios ausentes ou inválidos: indicar erros e impedir cadastro.
Cancelamento: não criar o registro.
Falha de gravação: informar que o produto não foi salvo.
**Pós-condição:** Produto pessoal criado e armazenado localmente.

**Critérios de aceitação:**

É possível cadastrar nas duas categorias iniciais.
Os cinco critérios exigidos correspondem à categoria escolhida.
Valores inválidos são rejeitados.
O produto permanece após reiniciar o app e não modifica o Firestore.
A categoria é fixada no cadastro.
**Rastreabilidade:** DEC-001, DEC-004, DEC-007, DEC-012, DEC-021.

### RF07 — Consultar e editar produto pessoal
**Descrição:** O sistema deve permitir consultar e editar dados de identificação e valores dos critérios de um produto pessoal, sem alterar sua categoria.

**Ator principal:** Usuário.

**Pré-condições:**

Produto previamente cadastrado no catálogo local.
**Fluxo principal:**

Usuário abre seu catálogo pessoal e seleciona um produto.
Sistema apresenta os dados existentes.
Usuário entra na edição e modifica campos permitidos.
Sistema valida os valores alterados.
Usuário salva; sistema atualiza o registro local.
**Fluxos alternativos e exceções:**

Entrada inválida: manter edição para correção.
Cancelamento: preservar versão original.
Erro de gravação: informar falha.
Tentativa de alterar categoria ou produto oficial: impedir a operação.
**Pós-condição:** Dados permitidos atualizados localmente; categoria inalterada.

**Critérios de aceitação:**

É possível editar nome, fabricante, modelo e valores pertinentes.
Categoria é exibida sem permitir alteração.
Alterações persistem após reinício.
Edição não muda avaliações anteriores.
Produtos do catálogo oficial permanecem somente leitura.
**Rastreabilidade:** DEC-003, DEC-007, DEC-012, DEC-021.

### RF08 — Excluir produto pessoal
**Descrição:** O sistema deve permitir excluir um produto pessoal mediante confirmação explícita, preservando os dados já copiados para avaliações.

**Ator principal:** Usuário.

**Pré-condições:**

Produto pessoal existente.
**Fluxo principal:**

Usuário seleciona produto e solicita exclusão.
Sistema apresenta diálogo de confirmação.
Usuário confirma.
Sistema remove o produto local e atualiza a listagem.
**Fluxos alternativos e exceções:**

Cancelamento: produto preservado.
Falha na exclusão: informar erro sem afirmar sucesso.
Produto já associado a avaliações: preservar os snapshots contidos nelas.
**Pós-condição:** Produto retirado do catálogo pessoal; avaliações anteriores inalteradas.

**Critérios de aceitação:**

Confirmação é obrigatória.
Cancelamento não altera dados.
Exclusão remove o produto da lista.
Avaliações anteriores permanecem consultáveis.
Produtos oficiais não são afetados.
**Rastreabilidade:** DEC-007, DEC-012.

### RF09 — Persistir e reutilizar produtos pessoais
**Descrição:** O sistema deve manter o catálogo pessoal no dispositivo e permitir reutilizar seus produtos em várias avaliações da mesma categoria.

**Ator principal:** Usuário.

**Pré-condições:**

Produto pessoal cadastrado e disponível localmente.
**Fluxo principal:**

Usuário cria ou edita uma avaliação de determinada categoria.
Sistema disponibiliza os produtos pessoais dessa categoria.
Usuário seleciona produto pessoal.
Sistema copia seus valores como referência inicial da avaliação.
Avaliação mantém seus próprios dados, independentes do registro do catálogo.
**Fluxos alternativos e exceções:**

Catálogo pessoal vazio: permitir trabalhar com catálogo oficial.
Produto excluído: não oferecê-lo em novas seleções, preservando avaliações antigas.
Falha na leitura do banco: informar indisponibilidade.
**Pós-condição:** Produto reutilizável e dados da avaliação independentes dos do catálogo.

**Critérios de aceitação:**

Produto pessoal permanece após reiniciar o app.
Pode ser usado em múltiplas avaliações.
Seleção respeita a categoria.
Mudanças dentro da avaliação não modificam o catálogo pessoal.
Exclusão posterior não altera avaliações anteriores.
**Rastreabilidade:** DEC-003, DEC-004, DEC-007, DEC-012.
