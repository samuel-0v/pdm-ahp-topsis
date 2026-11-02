# Método TOPSIS e resultados

**Situação:** requisitos funcionais validados.

[Índice de requisitos](../README.md) · [Decisões](../../decisoes/registro-decisoes.md)

### RF18 — Processar avaliação pelo TOPSIS
**Descrição:** O sistema deve executar o TOPSIS usando os produtos selecionados, os valores específicos da avaliação e os pesos aprovados pelo AHP.

**Ator principal:** Usuário.

**Pré-condições:**

Pelo menos dois produtos distintos válidos, da mesma categoria; todos os valores necessários completos; matriz AHP consistente com RC≤0,10; pesos correspondentes à configuração atual.
**Fluxo principal:**

Usuário revisa resumo de alternativas, valores e pesos.
Solicita processamento.
Sistema constrói matriz de decisão X.
Normaliza vetorialmente cada coluna e pondera pelos pesos AHP.
Determina ideais positivo e negativo conforme benefício/custo.
Calcula distâncias euclidianas d+ e d−.
Calcula coeficiente de proximidade C* para cada produto quando matematicamente definido.
Disponibiliza resultados para ranking e consulta, ainda não necessariamente salvos.
**Regras e expressões matemáticas:**

r[i,j] = x[i,j]/sqrt(Σ_k x[k,j]²); v[i,j] = w[j]·r[i,j].
Benefício: A+[j]=max_i v[i,j] e A−[j]=min_i v[i,j]; custo: inverter.
d+[i]=sqrt(Σ_j(v[i,j]−A+[j])²); d−[i]=sqrt(Σ_j(v[i,j]−A−[j])²).
C*[i]=d−[i]/(d+[i]+d−[i]) se o denominador for diferente de zero.
**Fluxos alternativos e exceções:**

Produtos insuficientes ou valores inválidos: bloquear e indicar correções.
RC>0,10 ou pesos desatualizados: voltar à revisão do AHP.
Coluna sem normalização definida: impedir resultado inválido.
Todas as alternativas indistinguíveis: informar ausência de diferenciação em vez de calcular 0/0.
Falha de cálculo: informar erro sem resultado fictício.
**Pós-condição:** Resultados calculados em memória, aptos a salvar mediante ação explícita.

**Critérios de aceitação:**

Usa os pesos da matriz AHP válida e valores da avaliação, não do catálogo atualizado.
Normalização, ponderação, ideais e distâncias seguem documento de referência.
Em casos definidos, 0≤C*≤1 à precisão numérica.
Resultados intermediários não são arredondados para calcular próximas etapas.
Pode ser testado com matrizes de resultados conhecidos.
**Rastreabilidade:** DEC-003, DEC-005, DEC-018, DEC-024, DEC-025.

### RF19 — Classificar os produtos pelos coeficientes TOPSIS
**Descrição:** O sistema deve formar ranking em ordem decrescente de C* sem atribuir desempates arbitrários.

**Ator principal:** Usuário.

**Pré-condições:**

Processamento TOPSIS com coeficientes válidos; ou estado matemático de ausência de diferenciação detectado.
**Fluxo principal:**

Sistema recebe C* dos produtos.
Ordena alternativas com coeficientes válidos em ordem decrescente.
Atribui posições respeitando empates matemáticos.
Destaca a alternativa com maior C* apenas quando houver vencedor único.
Disponibiliza ranking e estado correspondente à interface.
**Fluxos alternativos e exceções:**

Coeficientes empatados: mesma posição.
Todos os produtos indistinguíveis e C* indefinido: apresentar estado sem diferenciação; não ordenar por valores fictícios.
Resultado ausente/desatualizado: não apresentar ranking como concluído.
**Pós-condição:** Ranking coerente com o TOPSIS ou estado de ausência de diferenciação.

**Critérios de aceitação:**

Ordenação segue C* decrescente.
Empates compartilham a mesma posição, sem usar nome/preço como desempate.
Empate não é decidido somente pela igualdade após arredondamento visual.
Não existe vencedor arbitrário no caso degenerado.
**Rastreabilidade:** DEC-018, DEC-024.

### RF20 — Consultar e interpretar resultados da avaliação
**Descrição:** O sistema deve apresentar os resultados AHP e TOPSIS com transparência suficiente para o usuário compreender a classificação.

**Ator principal:** Usuário.

**Pré-condições:**

Avaliação processada com resultado válido, ou indicação de ausência de diferenciação.
**Fluxo principal:**

Usuário acessa os resultados.
Sistema apresenta ranking, com destaque ao vencedor único quando aplicável.
Exibe posição, C*, d+ e d− de cada produto.
Disponibiliza pesos e indicadores AHP (λmax, IC e RC).
Permite reconhecer produtos e valores utilizados na avaliação.
Oferece retorno para edição, respeitando invalidação e salvamento manual quando houver mudanças.
**Fluxos alternativos e exceções:**

Resultado ainda não processado: informar situação pendente.
Resultado invalidado: indicar necessidade de novo cálculo.
Sem diferenciação: informar ausência de vencedor.
Produto removido do catálogo: apresentar dados preservados na avaliação.
**Pós-condição:** Resultados apresentados ou estado informativo correspondente.

**Critérios de aceitação:**

Usuário pode consultar posições, pesos, indicadores e distâncias.
Valores exibidos refletem cálculo realmente realizado.
Empates e ausência de diferenciação são comunicados corretamente.
Resultados persistidos continuam consultáveis após reiniciar o app.
**Rastreabilidade:** DEC-006, DEC-018, DEC-024, DEC-025.
