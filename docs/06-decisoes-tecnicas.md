# Decisões técnicas

## DEC-018 — Metodologia matemática AHP-TOPSIS

**Status:** Aprovada pela equipe.

**Referência principal:** [ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf](../references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf), documento complementar “Avaliação Comparativa de Produtos com AHP-TOPSIS”.

O documento complementar será a referência principal para a implementação dos algoritmos matemáticos. O enunciado principal apresenta outro procedimento para obter os pesos; esta escolha registra explicitamente a abordagem adotada pela equipe e mantém a divergência documentada.

### AHP — determinação dos pesos

Será utilizado o método da média geométrica normalizada. Para cada linha `i` da matriz de comparações pareadas `A`, com `n` critérios:

`GM_i = (∏_{j=1..n} a_ij)^(1/n)`

Os pesos serão normalizados por:

`w_i = GM_i / Σ_{k=1..n} GM_k`

O cálculo do autovalor máximo estimado, Índice de Consistência (IC) e Razão de Consistência (RC) seguirá o procedimento descrito no documento de referência. A avaliação só poderá prosseguir ao TOPSIS se `RC ≤ 0,10`; caso contrário, os julgamentos deverão ser revisados.

As duas categorias definidas no escopo possuem cinco critérios. Para `n = 5`, será usado o Índice Aleatório `IR = 1,12` da tabela adotada no documento complementar.

### TOPSIS — classificação

A implementação seguirá esta sequência:

1. Construir a matriz de decisão das alternativas por critério.
2. Realizar a normalização vetorial.
3. Aplicar os pesos obtidos pelo AHP.
4. Determinar as soluções ideais positiva e negativa conforme a natureza de cada critério.
5. Calcular as distâncias euclidianas às soluções ideais.
6. Calcular o coeficiente de proximidade relativa.
7. Ordenar os produtos pelo coeficiente em ordem decrescente.

### Consequências

- Os motores AHP e TOPSIS serão implementados em Kotlin como componentes independentes da interface e do acesso a dados.
- A validação matemática será coberta por testes unitários com resultados conhecidos.
- A documentação de requisitos e arquitetura deverá refletir esta metodologia escolhida, sem apagar que o enunciado principal descreve outro método de obtenção de pesos.

## Outras divergências do enunciado

- `ProjetoDisciplina_PDM20262v2.pdf` é mantido como referência do escopo geral, requisitos e entregas.
- Em caso de conflito sobre os cálculos, a decisão DEC-018 adota explicitamente o método descrito no documento complementar `ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf`.
