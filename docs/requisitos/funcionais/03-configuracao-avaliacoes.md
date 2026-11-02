# Criação e configuração de avaliações

**Situação:** requisitos funcionais validados.

[Índice de requisitos](../README.md) · [Decisões](../../decisoes/registro-decisoes.md)

### RF10 — Criar avaliação
**Descrição:** O sistema deve permitir iniciar uma avaliação vinculada a uma categoria e a seus cinco critérios predefinidos.

**Ator principal:** Usuário.

**Pré-condições:**

Categoria existente acessível e conjunto de critérios definido.
**Fluxo principal:**

Usuário solicita nova avaliação.
Sistema apresenta categorias disponíveis.
Usuário seleciona uma categoria.
Sistema cria a configuração inicial com os critérios associados.
Usuário é encaminhado à seleção de alternativas.
**Fluxos alternativos e exceções:**

Categoria indisponível: bloquear criação e explicar.
Critérios ausentes ou incompatíveis: não criar uma configuração inválida.
Saída antes do salvamento: aplicar aviso de alterações não salvas.
**Pós-condição:** Avaliação iniciada em memória; persistência requer salvamento explícito pelo RF21.

**Critérios de aceitação:**

Somente categorias existentes podem ser escolhidas.
Os cinco critérios corretos são associados.
Categoria não pode ser modificada após a criação.
Nova avaliação não modifica as já armazenadas.
Pode iniciar offline quando definições necessárias estiverem acessíveis.
**Rastreabilidade:** DEC-001, DEC-005, DEC-008, DEC-022, DEC-025.

### RF11 — Selecionar alternativas
**Descrição:** O sistema deve permitir selecionar quais produtos oficiais e pessoais, pertencentes à categoria da avaliação, integrarão a comparação.

**Ator principal:** Usuário.

**Pré-condições:**

Avaliação iniciada e categoria fixa.
**Fluxo principal:**

Sistema apresenta produtos oficiais e pessoais da categoria.
Usuário seleciona ou desmarca alternativas.
Sistema evita inclusão duplicada do mesmo produto.
Sistema associa os produtos e seus dados de referência à avaliação.
**Fluxos alternativos e exceções:**

Usuário pode cadastrar novo produto pessoal e selecioná-lo.
Menos de dois produtos: permitir rascunho, mas impedir processamento.
Produto de outra categoria: rejeitar.
Produto com dados incompletos: exigir validação/correção antes do cálculo.
**Pós-condição:** Conjunto de alternativas da avaliação definido ou parcialmente preenchido.

**Critérios de aceitação:**

É possível selecionar produtos de ambos os catálogos na mesma categoria.
Produto não aparece duas vezes na mesma seleção.
Mínimo de dois produtos distintos válidos para processar.
Rascunhos podem permanecer incompletos.
Alterações de seleção em avaliação concluída invalidam ranking após salvamento.
**Rastreabilidade:** DEC-003, DEC-005, DEC-007, DEC-012, DEC-022.

### RF12 — Ajustar valores dos critérios por avaliação
**Descrição:** O sistema deve permitir substituir os valores de referência dos critérios para uma avaliação específica, preservando os valores de origem.

**Ator principal:** Usuário.

**Pré-condições:**

Avaliação iniciada; produtos selecionados.
**Fluxo principal:**

Sistema apresenta valores de referência por produto e critério.
Usuário ajusta valores desejados.
Sistema registra ajustes na configuração em edição e aplica validações.
Após ação explícita de salvar, persiste os valores próprios da avaliação sem alterar os catálogos.
**Fluxos alternativos e exceções:**

Valor inválido ou ausente: indicar problema; rascunho pode ser salvo incompleto.
Saída com alterações pendentes: oferecer salvar, descartar ou continuar.
Falha no salvamento: manter versão persistida anterior.
**Pós-condição:** Avaliação possui seus próprios valores quando salva; catálogos originais preservados.

**Critérios de aceitação:**

Ajustes afetam apenas a avaliação corrente.
Outro uso do mesmo produto pode manter valores diferentes.
Ajustes persistem após salvar e retomar.
Alterar dados relevantes de avaliação concluída torna o resultado anterior inválido ao salvar até novo cálculo.
**Rastreabilidade:** DEC-003, DEC-006, DEC-017, DEC-025.

### RF13 — Validar dados da avaliação para processamento
**Descrição:** O sistema deve verificar que a avaliação contém alternativas e valores de critérios suficientes e matematicamente válidos antes do TOPSIS.

**Ator principal:** Usuário (aciona avanço); sistema (executa validação).

**Pré-condições:**

Avaliação com categoria, critérios e alternativas associados, ainda que incompletos.
**Fluxo principal:**

Sistema verifica categoria e coerência dos produtos.
Confere o mínimo de duas alternativas distintas.
Confere presença dos cinco critérios exigidos por produto.
Valida valores numéricos finitos, unidades, tipos e limites definidos; notas qualitativas entre 1 e 10.
Verifica condições matemáticas de normalização e comunica campos/problemas quando inválidos.
Quando todos os dados necessários são válidos, autoriza a continuidade ao processamento.
**Fluxos alternativos e exceções:**

Poucos produtos: bloquear processamento.
Campo vazio, texto não numérico, nota fora da escala ou produto de categoria diferente: indicar erro específico.
Denominador de normalização nulo ou outras condições inviáveis: não executar TOPSIS inválido.
**Pós-condição:** Configuração validada para processamento ou erros apresentados ao usuário.

**Critérios de aceitação:**

Nunca processa com menos de dois produtos distintos.
Notas de câmera e ADAS devem permanecer de 1 a 10.
Valores não numéricos/infinitos não são aceitos.
Rascunhos incompletos continuam salváveis.
Consistência dos julgamentos AHP é verificada separadamente pelo RF17.
**Rastreabilidade:** DEC-005, DEC-013, DEC-014, DEC-017, DEC-018.
