# Desempenho e Correção Matemática

**Versão:** 1.0 — requisitos validados.

[Índice de requisitos](../README.md)

### RNF03 — Desempenho dos cálculos

**Situação:** validado, conforme DEC-026 e complementação aprovada.

A meta quantitativa e seu protocolo estão na [DEC-026](../../decisoes/registro-decisoes.md#dec-026--desempenho-dos-calculos).

Os cálculos executarão fora da thread principal. Durante o processamento, a navegação e os controles habilitados devem continuar respondendo conforme o fluxo previsto. Enquanto houver processamento, apresentar indicação de atividade, sem atraso artificial para exibir o indicador. Verificar no dispositivo físico de referência, incluindo o cenário de 50 produtos.

**Métodos de verificação:** medições dos motores conforme DEC-026 e verificação de interação durante o processamento.

### RNF04 — Precisão e correção numérica

**Situação:** validado.

**Descrição:** os motores seguem a metodologia da DEC-018 e mantêm os valores internos sem arredondamentos intermediários para apresentação.

**Critérios de verificação:**

- Verificar pesos por média geométrica normalizada, indicadores AHP, distâncias e coeficientes TOPSIS com referências independentes de precisão suficiente.
- Aplicar a tolerância combinada da [DEC-029](../../decisoes/registro-decisoes.md#dec-029--tolerancia-dos-testes-matematicos) aos testes.
- Comparar RC interno diretamente com 0,10, sem arredondamento prévio nem tolerância adicional. RC > 0,10 bloqueia TOPSIS. Exibir casas decimais suficientes para explicar o bloqueio quando o arredondamento usual ocultar a diferença. Complementação aprovada do RNF04.
- Formar grupos de empate conforme [DEC-027](../../decisoes/registro-decisoes.md#dec-027--tolerancia-e-formacao-dos-grupos-de-empate-topsis), comparando cada coeficiente com o maior do grupo, com margem absoluta de 10⁻⁹.
- Numerar posições conforme [DEC-028](../../decisoes/registro-decisoes.md#dec-028--numeracao-das-posicoes-no-ranking): 1º, 2º, 2º, 4º.
- Casos matematicamente indefinidos não devem gerar coeficientes fictícios ou vencedores arbitrários, conforme DEC-024.

**Método de verificação:** testes unitários com referências independentes, casos limítrofes de RC, cadeias de proximidade e grupos de empate. A aprovação deste requisito não significa que esses testes já foram executados.
