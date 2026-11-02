# Casos de uso — Configuração da avaliação

**Versão:** 1.1 — UC01–UC15 aceitos; alinhamento às DEC-033–DEC-035.

Base: RF01–RF27, RNF01–RNF13 e DEC-001–DEC-035 aprovados. P01–P03 resolvidos; detalhamento de modelagem e dados segue para as próximas etapas. Não representa implementação.

[Índice dos casos de uso](README.md) · [Rastreabilidade](../rastreabilidade.md)

## UC06 — Criar avaliação

**Objetivo:** iniciar comparação vinculada a uma categoria predefinida.
**Ator principal:** usuário.
**Gatilho:** solicitar nova avaliação.
**Pré-condição:** categoria e cinco critérios disponíveis.
**Requisitos:** RF10; RNF01, RNF02, RNF06.

**Fluxo principal:**

1. O usuário solicita nova avaliação e seleciona a categoria.
2. O sistema associa seus cinco critérios à configuração inicial.
3. Mantém a categoria fixa e a avaliação em memória.
4. Encaminha o usuário à seleção de alternativas pelo UC07.

**Alternativas e exceções:**

- A1 — Definições ausentes ou incompatíveis: impedir criação inválida e explicar o problema.
- A2 — Offline com definições disponíveis: permitir criação.
- A3 — Saída com alterações não salvas: aplicar o UC12.
- A4 — Desejo de mudar categoria: criar outra avaliação; não alterar a existente.

**Pós-condição:** avaliação iniciada, ainda não salva; avaliações anteriores preservadas.
**Verificação:** criar nas duas categorias e conferir os cinco critérios e a imutabilidade da categoria.

## UC07 — Selecionar alternativas

**Objetivo:** reunir produtos da mesma categoria para comparar.
**Ator principal:** usuário. **Ator de apoio:** Firestore nas consultas oficiais remotas.
**Gatilho:** acessar seleção de produtos da avaliação.
**Pré-condição:** avaliação em memória com categoria definida.
**Requisitos:** RF09, RF11, RF13, RF25; RNF01, RNF02, RNF06.

**Fluxo principal:**

1. O sistema apresenta produtos oficiais acessíveis e produtos pessoais da categoria.
2. O usuário seleciona ou desmarca produtos.
3. O sistema impede repetir a mesma identidade de registro, formada por origem e ID (documento do Firestore ou cadastro pessoal local), conforme DEC-034.
4. Associa cópias dos dados necessários à avaliação.
5. O usuário prossegue para ajustar valores ou comparar critérios; o processamento exige pelo menos duas alternativas válidas com identidades distintas (origem + ID, DEC-034).

**Alternativas e exceções:**

- A1 — Novo produto pessoal: executar UC03 e disponibilizá-lo para seleção.
- A2 — Menos de dois produtos: permitir rascunho pelo UC12, bloqueando processamento.
- A3 — Produto de outra categoria: rejeitar associação.
- A4 — Valores incompletos: indicar necessidade de correção pelo UC08 antes do cálculo.
- A5 — Offline: oferecer oficiais acessíveis no cache e pessoais locais; explicar dados indisponíveis.
- A6 — Alteração em avaliação concluída: aplicar invalidação e salvamento do UC14.

**Pós-condição:** alternativas associadas à edição atual; alterações nos catálogos não são realizadas.
**Verificação:** seleção mista, prevenção de repetição do mesmo registro, permissão para selecionar registros distintos de origens diferentes mesmo com nomes/modelos iguais, e bloqueio de processamento com uma alternativa. Não se infere equivalência física automaticamente.

## UC08 — Ajustar e validar valores da avaliação

**Objetivo:** personalizar valores apenas na comparação atual e verificar sua aptidão matemática.
**Ator principal:** usuário.
**Gatilho:** abrir valores dos produtos ou solicitar processamento.
**Pré-condição:** avaliação com produtos selecionados e critérios associados.
**Requisitos:** RF12, RF13, RF25; RNF01, RNF02, RNF04, RNF05, RNF06.

**Fluxo principal:**

1. O sistema apresenta valores de referência por produto, critério e unidade.
2. O usuário ajusta os valores desejados na avaliação.
3. O sistema verifica categoria, identidades distintas (DEC-034), cinco critérios por produto, preenchimento e valores finitos, aplicando tipo, unidade, domínio/limites e ausência conforme metadados dos critérios (DEC-033).
4. Valida notas de câmeras e ADAS entre 1 e 10 e as condições de normalização.
5. Se os dados forem válidos, permite prosseguir, ainda sujeito à consistência AHP pelo UC10.

**Alternativas e exceções:**

- A1 — Ausência, valor inválido, categoria incompatível ou alternativas insuficientes: indicar problemas e impedir processamento; rascunho incompleto continua permitido.
- A2 — Denominador de normalização nulo: impedir TOPSIS inválido e informar o problema.
- A3 — Saída com mudanças ou falha ao salvar: aplicar UC12.
- A4 — Edição de avaliação concluída: aplicar UC14.

**Pós-condição:** valores próprios em memória, sem alterar catálogos; persistência somente por UC12.
**Verificação:** valores distintos em duas avaliações do mesmo produto, campos ausentes, entradas não numéricas/não finitas e notas fora de 1–10. Limites ainda não definidos não são inventados nesta especificação.
