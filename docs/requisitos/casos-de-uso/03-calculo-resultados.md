# Casos de uso — AHP, TOPSIS e resultados

**Versão:** 0.1 — rascunho, proposta de consolidação para revisão.

Base: RF01–RF26, RNF01–RNF13 e DEC-001–DEC-031 aprovados. O detalhamento ainda aguarda revisão; não registra novas decisões nem implementação.

[Índice dos casos de uso](README.md) · [Rastreabilidade](../rastreabilidade.md)

## UC09 — Realizar e revisar comparações AHP

**Objetivo:** expressar a importância relativa dos cinco critérios.
**Ator principal:** usuário.
**Gatilho:** acessar comparações AHP da avaliação.
**Pré-condição:** avaliação criada com critérios disponíveis.
**Requisitos:** RF14; RNF01, RNF02, RNF04, RNF06.

**Fluxo principal:**

1. O sistema apresenta um par de critérios e o progresso.
2. O usuário informa direção da preferência e intensidade na escala Saaty de 1 a 9, incluindo intermediários; 1 representa igual importância.
3. O sistema registra o julgamento, o recíproco e mantém diagonal 1.
4. O usuário avança ou retorna para revisar pares.
5. Ao completar as dez comparações independentes, pode solicitar validação pelo UC10.

**Alternativas e exceções:**

- A1 — Comparações pendentes: mostrar progresso real, não presumir igualdade e impedir aprovação da matriz incompleta.
- A2 — Revisão de resposta: atualizar o recíproco e invalidar pesos/consistência anteriores; resultados afetados seguem UC14.
- A3 — Interrupção: permitir rascunho parcial ou aplicar aviso de saída pelo UC12.

**Pós-condição:** matriz recíproca parcial ou completa na edição atual, ainda sujeita à validação.
**Verificação:** dez pares, navegação de revisão, reciprocidade e retomada de comparações parciais sem preenchimento artificial.

## UC10 — Calcular pesos e validar consistência AHP

**Objetivo:** obter pesos e verificar se os julgamentos permitem TOPSIS.
**Ator principal:** usuário. O motor matemático é parte interna do aplicativo, não ator externo.
**Gatilho:** solicitar validação dos julgamentos.
**Pré-condição:** comparações informadas; integridade da matriz verificada antes do cálculo.
**Requisitos:** RF15, RF16, RF17; RNF03, RNF04, RNF06, RNF09, RNF10.

**Fluxo principal:**

1. O sistema verifica matriz completa, quadrada, positiva e recíproca.
2. Calcula pesos pela média geométrica normalizada, conforme DEC-018.
3. Calcula λmax estimado, IC e RC, usando IR/RI = 1,12 para cinco critérios.
4. Compara o RC interno diretamente com 0,10, sem arredondamento prévio ou tolerância adicional.
5. Apresenta pesos e indicadores; se RC ≤ 0,10, libera TOPSIS condicionado à validade dos demais dados.

**Alternativas e exceções:**

- A1 — RC > 0,10: bloquear TOPSIS e permitir revisão pelo UC09; mostrar precisão suficiente para explicar o bloqueio.
- A2 — Matriz incompleta/inválida ou resultado não finito: impedir aprovação e informar problema.
- A3 — Julgamento alterado depois da validação: exigir novo cálculo de pesos e consistência.
- A4 — Usuário deseja interromper: permitir salvar rascunho inconsistente/incompleto pelo UC12.

**Pós-condição:** estado de consistência correspondente aos julgamentos atuais; cálculo não salva automaticamente.
**Verificação:** matriz toda igual a 1 com pesos 0,20 e RC aproximadamente zero, matrizes inválidas e fronteira RC=0,10 versus RC>0,10; referências e tolerância dos testes conforme DEC-029.

## UC11 — Processar TOPSIS e consultar resultados

**Objetivo:** comparar alternativas e compreender ranking ou ausência de diferenciação.
**Ator principal:** usuário.
**Gatilho:** solicitar processamento após revisar os dados, ou abrir resultado válido de avaliação salva.
**Pré-condições para calcular:** pelo menos dois produtos distintos da mesma categoria, valores matematicamente válidos e AHP consistente com pesos da configuração atual. Para consulta, resultado válido disponível.
**Requisitos:** RF13, RF18, RF19, RF20, RF25; RNF01–RNF06, RNF09, RNF10.

**Fluxo principal:**

1. O usuário revisa alternativas, valores e pesos e solicita processamento.
2. O sistema verifica os pré-requisitos e usa os dados próprios da avaliação.
3. Normaliza vetorialmente a matriz de decisão, aplica pesos, determina ideais conforme benefício/custo e calcula distâncias euclidianas e C* quando definido.
4. Ordena C* decrescentemente e forma grupos de empate conforme DEC-027, comparando com o maior coeficiente de cada grupo.
5. Atribui posições competitivas conforme DEC-028; destaca vencedor somente se único.
6. Apresenta posições, coeficientes, distâncias, pesos, λmax, IC, RC e dados utilizados.
7. O usuário pode editar pelo UC14 ou salvar explicitamente pelo UC12.

**Alternativas e exceções:**

- A1 — Abrir resultado salvo válido: apresentar os dados persistidos, sem exigir recálculo nem atualização do catálogo.
- A2 — Dados inválidos, pesos desatualizados ou RC inconsistente: indicar correção pelos UC08–UC10; não publicar ranking válido.
- A3 — Todos indistinguíveis, com d+ = d− = 0: apresentar ausência de diferenciação, sem C* fictício nem vencedor. A classificação persistida desse estado é ponto de revisão indicado no relatório.
- A4 — Empate: mesma posição para o grupo, sem desempate por nome ou preço; não confundir igualdade visual arredondada com empate matemático.
- A5 — Falha numérica/normalização indefinida: informar falha sem resultado fictício.
- A6 — Resultado desatualizado: indicar necessidade de recálculo, sem apresentá-lo como resultado atual válido.

**Pós-condição:** resultado em memória ou resultado salvo consultado; novo processamento não implica gravação.
**Verificação:** cenário conhecido, critérios de custo/benefício, empate por tolerância, cadeia de proximidade, posições 1,2,2,4 e ausência de diferenciação. Desempenho segue DEC-026; o teto de 50 produtos é apenas cenário de teste.
