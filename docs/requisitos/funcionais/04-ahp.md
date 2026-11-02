# Método AHP

**Situação:** requisitos funcionais validados.

[Índice de requisitos](../README.md) · [Decisões](../../decisoes/registro-decisoes.md)

### RF14 — Realizar comparações pareadas dos critérios
**Descrição:** O sistema deve permitir que o usuário compare os cinco critérios dois a dois em fluxo guiado, utilizando a Escala Fundamental de Saaty.

**Ator principal:** Usuário.

**Pré-condições:**

Avaliação criada; critérios de sua categoria disponíveis.
**Fluxo principal:**

Sistema apresenta um par de critérios por vez e um indicador de progresso.
Usuário define sentido da preferência e intensidade de 1 a 9, incluindo valores intermediários.
Sistema registra o julgamento e preenche automaticamente o correspondente recíproco.
Usuário avança ou volta para revisar comparações anteriores.
Após dez comparações independentes, sistema possui a matriz 5×5 completa.
**Fluxos alternativos e exceções:**

Julgamento pendente: manter comparação incompleta.
Resposta alterada: atualizar par e recíproco.
Tentativa de validar incompleta: indicar pares não respondidos.
Usuário pode salvar rascunho com comparações parciais.
**Pós-condição:** Comparações registradas na avaliação e matriz recíproca completa ou parcial.

**Critérios de aceitação:**

Há dez comparações independentes para cinco critérios.
Matriz possui diagonal 1 e reciprocidade a[j,i]=1/a[i,j].
Usuário visualiza progresso e consegue voltar para revisar.
Matriz incompleta não é aprovada.
**Rastreabilidade:** DEC-018, DEC-023, DEC-029.

### RF15 — Calcular pesos AHP por média geométrica normalizada
**Descrição:** O sistema deve obter o vetor de prioridades dos critérios pelo método da média geométrica normalizada da matriz de comparações pareadas.

**Ator principal:** Usuário (inicia validação); sistema (calcula).

**Pré-condições:**

Matriz AHP completa, quadrada, positiva e recíproca.
**Fluxo principal:**

Sistema calcula a média geométrica dos valores de cada linha.
Divide cada média geométrica pela soma das médias geométricas.
Associa os pesos normalizados aos critérios corretos.
Disponibiliza pesos para a validação de consistência e apresentação.
**Regras e expressões matemáticas:**

GM[i] = (∏ a[i,j])^(1/n).
w[i] = GM[i] / Σ GM[k].
**Fluxos alternativos e exceções:**

Matriz incompleta, não recíproca ou inválida: rejeitar cálculo.
Resultado numérico não finito: impedir uso dos pesos.
Comparação alterada: invalidar pesos anteriores e calcular novamente.
**Pós-condição:** Vetor de pesos calculado, aguardando ou acompanhado de validação de consistência.

**Critérios de aceitação:**

Os pesos obedecem à média geométrica normalizada, não à média aritmética das colunas do outro documento.
Pesos são positivos e somam aproximadamente 1.
Matriz 5×5 com todos os elementos iguais a 1 resulta em pesos 0,20 para cada critério.
Resultados são verificáveis por testes unitários com matrizes conhecidas.
**Rastreabilidade:** DEC-018, DEC-023, DEC-029.

### RF16 — Calcular indicadores de consistência AHP
**Descrição:** O sistema deve calcular λmax estimado, Índice de Consistência (IC), Índice Aleatório (RI) e Razão de Consistência (RC).

**Ator principal:** Usuário (solicita validação); sistema (calcula).

**Pré-condições:**

Matriz AHP e vetor de pesos válidos.
**Fluxo principal:**

Sistema calcula o produto matriz×vetor (Aw).
Calcula λmax pela média de (Aw)[i]/w[i].
Calcula IC=(λmax−n)/(n−1).
Obtém RI na tabela de Saaty para n critérios.
Calcula RC=IC/RI.
Disponibiliza indicadores para a interface.
**Regras e expressões matemáticas:**

λmax = (1/n) Σ ((A·w)[i]/w[i]).
IC = (λmax−n)/(n−1); RC = IC/RI; n=5 e RI=1,12 nesta versão.
**Fluxos alternativos e exceções:**

Matriz/pesos inválidos: não calcular.
Resultados não finitos: informar falha.
Em futuras categorias com n de RI indisponível: não prosseguir sem regra adequada.
**Pós-condição:** Indicadores matemáticos disponíveis para decidir a consistência.

**Critérios de aceitação:**

Para cinco critérios o RI usado é 1,12.
A matriz 5×5 toda igual a 1 produz λmax≈5, IC≈0 e RC≈0.
Os resultados correspondem ao documento matemático escolhido.
Indicadores exibidos são derivados dos cálculos reais.
**Rastreabilidade:** DEC-018, DEC-029.

### RF17 — Validar consistência e bloquear TOPSIS quando necessário
**Descrição:** O sistema deve liberar o TOPSIS somente após obter uma matriz AHP completa e consistente, com RC menor ou igual a 0,10.

**Ator principal:** Usuário.

**Pré-condições:**

Comparações completas; pesos e indicadores calculados.
**Fluxo principal:**

Sistema compara RC ao limite 0,10.
Se RC≤0,10, indica julgamento aprovado e libera próxima etapa, desde que os demais dados estejam válidos.
Se RC>0,10, apresenta alerta de inconsistência.
Permite voltar às comparações, corrigir respostas e recalcular os indicadores.
**Fluxos alternativos e exceções:**

Matriz incompleta: bloquear aprovação.
Peso/RC inválido: impedir TOPSIS.
Julgamentos alterados: revogar aprovação anterior e exigir nova validação.
Usuário pode salvar rascunho mesmo com matriz inconsistente.
**Pós-condição:** Estado AHP atualizado como validado ou pendente/inconsistente.

**Critérios de aceitação:**

RC igual a 0,10 é aceita.
RC maior que 0,10 bloqueia processamento TOPSIS.
Usuário consegue corrigir julgamentos sem criar nova avaliação.
Qualquer alteração de comparações exige recálculo.
**Rastreabilidade:** DEC-018, DEC-023, DEC-029.
