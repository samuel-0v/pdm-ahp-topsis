# 5.3. Persistência de AHP e TOPSIS — P5-B

[Índice da Parte 5](../05-persistencia-snapshots.md) · [Contrato consolidado](../05-contratos-persistencia.md)

**Situação da Parte 5:** somente P5-A aprovada (DEC-038); P5-B–P5-E pendentes. CHECK canônico requer validação técnica.

O modelo recomendado coloca o cabeçalho AHP na avaliação, pesos nas linhas de critérios e detalhes TOPSIS nas alternativas. **Isso não torna peso parte imutável de CritérioPreservado nem transforma resultado em propriedade do produto:** o mapeador reconstrói objetos de valor Resultado AHP/TOPSIS, associados exclusivamente à avaliação. Todos esses campos são gravados/limpos na mesma transação.

Alternativa: tabelas próprias `resultado_ahp`, `peso_ahp`, `resultado_topsis` e `detalhe_topsis`. Tornam os objetos mais visíveis fisicamente e permitem restrições específicas, mas acrescentam tabelas e junções. Recomenda-se a incorporação para a primeira versão, sem necessidade de histórico ou consulta independente de resultados; essa escolha aguarda aprovação.

| Situação salva | AHP e pesos | TOPSIS/detalhes |
|---|---|---|
| Rascunho sem AHP vigente | Cabeçalho e todos os pesos ausentes | Tipo AUSENTE; distâncias, coeficientes e posições ausentes |
| Rascunho com diagnóstico AHP vigente | Cabeçalho completo, pesos de todos os critérios; RC pode exceder 0,10 (DEC-037) | AUSENTE; se RC > 0,10, bloqueia TOPSIS. Se RC ≤ 0,10 e demais entradas válidas, permite processar em memória; concluir exige depois salvar o resultado vigente |
| Concluída com ranking | AHP completo e RC ≤ 0,10 | RANKING; d+/d−, C* e posição por alternativa; empate compartilha posição e permite coeficientes distintos dentro da tolerância aprovada |
| Concluída — sem diferenciação | AHP completo e RC ≤ 0,10 | SEM_DIFERENCIACAO; detalhe para cada alternativa, distâncias calculáveis; C*/posição ausentes e nenhum vencedor fictício |

## Conjuntos coerentes de resultados

- **AHP ausente:** todos os campos do cabeçalho e todos os pesos ausentes; nenhum peso isolado implica AHP calculado.
- **AHP vigente:** cabeçalho completo e finito (`lambdaMax`, IC, RI e RC) e exatamente um peso positivo e finito por critério preservado, sem pesos extras, com normalização e indicadores coerentes com os julgamentos completos atuais e o RI aplicável. RC > 0,10 continua sendo diagnóstico inconsistente válido para rascunho, não aprovação de consistência (DEC-037).
- **TOPSIS AUSENTE:** todas as colunas calculadas de detalhe (distâncias, coeficientes e posições) estão ausentes em todas as alternativas; não carregar sobras de resultado antigo.
- **TOPSIS RANKING:** avaliação concluída, AHP vigente consistente, dados completos e exatamente um detalhe completo por alternativa, sem omissões ou duplicações. Distâncias finitas e não negativas, C* definido/finito na faixa matemática esperada e posições competitivas coerentes com o agrupamento aprovado; nenhuma unicidade de posição que proíba empate.
- **TOPSIS SEM_DIFERENCIACAO:** avaliação concluída, AHP vigente consistente e detalhe para todas as alternativas, com as distâncias/indicadores calculáveis que caracterizam esse caso; C* e posição ausentes para todas, sem vencedor fictício. Não aceitar detalhes misturando ranking e ausência de diferenciação.

**Validação de conjunto:** ao gravar e ao carregar, conferir correspondência entre estado salvo, tipo de resultado, cabeçalho/pesos e todos os detalhes. Transação impede gravação parcial, mas não transforma um conjunto incompleto em válido. Restrições locais de nulabilidade/domínio ajudam, enquanto relações que cruzam linhas exigem validador do agregado. Resultado parcial ou incoerente é informado e bloqueia apresentação/exportação como conclusão válida; não completar pelo catálogo nem salvar um reparo automaticamente. Rascunho pode ser incompleto nas entradas e continuar válido como rascunho, mas não conter um resultado parcial declarado vigente.

Cálculo falho e resultado desatualizado ficam na edição; ao salvar entradas alteradas, limpar os campos de resultado afetados. Se apenas alternativas/valores mudaram, AHP vigente pode permanecer; se julgamentos mudaram, AHP e TOPSIS são removidos/invalidados. Não arredondar valores internos para persistir ou calcular; RC é comparado diretamente com 0,10, empates seguem DEC-027/028 e testes seguem DEC-029.

## Representação numérica e monetária — P5-D pendente

A proposta distingue a representação fiel da entrada da aproximação usada pelos motores. Double/REAL representa números binários aproximados e não garante exatidão decimal de centavos. Inteiros preservam unidades de escala fixa exatamente dentro do seu intervalo; decimais exigem representação e conversão explícitas. [Precisão numérica no SQLite](https://sqlite.org/floatingpoint.html).

| Alternativa para dinheiro | Precisão e limites | Integração Room/Firestore | Conversão para AHP/TOPSIS e complexidade |
|---|---|---|---|
| `Double/REAL` em reais | Simples, mas muitos valores decimais não têm representação binária exata; igualdade/arredondamento monetário exigem cuidado | REAL local e número de ponto flutuante remoto; pouca adaptação | Usa diretamente o tipo numérico do motor; menor complexidade, menor garantia de preservação exata dos centavos |
| Inteiro de 64 bits em centavos | Exato para escala fixa de duas casas; verificar intervalo/overflow ao converter; não representa fração de centavo | INTEGER local e inteiro de 64 bits remoto, com moeda/escala/unidade explícitas nos metadados | Converter para a unidade do critério somente na fronteira do motor; complexidade moderada e previsível |
| Decimal como texto canônico | Preserva escala e valor decimal; validação obrigatória; texto não fornece ordenação numérica SQL por si só | TEXT/conversor local e string remota; Firestore não tem tipo decimal arbitrário nativo | Interpretar como decimal e converter explicitamente para cálculo; maior cuidado com parsing, consultas e limites |
| Decimal como valor não escalado + escala | Exato dentro do intervalo do inteiro; mais flexível que centavos; para precisão arbitrária pode precisar texto | Dois campos ou mapa em ambas as fontes; maior esforço de coerência e consulta | Reconstruir decimal antes de converter; flexibilidade com complexidade superior ao escopo monetário inicial |

Firestore oferece inteiro de 64 bits e ponto flutuante de 64 bits, além de strings/mapas; não há tipo BigDecimal nativo. Um objeto decimal do aplicativo precisa de codificação explícita, não envio implícito como número. BigDecimal representa valor não escalado e escala e pode ser convertido por contrato de serialização próprio. [Tipos Firestore](https://firebase.google.com/docs/firestore/manage-data/data-types), [BigDecimal](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/math/BigDecimal.html).

**Recomendação sujeita a aprovação:** armazenar preços e seguro anual em reais como inteiro em centavos, se for aprovada escala de duas casas para esses critérios; moeda BRL, escala e período/unidade continuam explícitos. Manter resultados dos motores em Double/REAL, sem arredondamento intermediário. Outros critérios seguem seu domínio, sem obrigá-los a usar centavos ou inteiro. Os marcadores `number` do diagrama não fecham o tipo físico de `valorEfetivo`/`valorOrigem`; a tabela genérica precisará de representação tipada coerente com os metadados escolhidos, sem pressupor uma única coluna REAL para todos os casos.

A entrada decimal deve ser interpretada de forma exata antes da conversão em centavos, evitando multiplicar um Double aproximado e truncar. Valores com mais casas do que a escala aprovada devem ser rejeitados para correção ou seguir uma política de arredondamento explicitamente aprovada; não arredondar, converter ou migrar silenciosamente. Dados legados remotos em Double também precisam de contrato de conversão validado; não presumir reconstrução exata da entrada decimal original.

Na fronteira dos motores, converter centavos/decimal para o valor na unidade preservada do critério, verificar faixa e finitude e usar a aproximação numérica conforme a metodologia. Preservar no snapshot a entrada canônica, sua escala e proveniência; a conversão para cálculo não substitui o dado original. Isso não exige aritmética decimal dentro dos motores nem torna seus resultados exatos. Não alterar RC, tolerâncias de empate/teste ou regras de arredondamento visual das DEC-018/027/029. NaN e infinitos são rejeitados mesmo quando aceitos como tipo numérico pelo Firestore.

A escolha do tipo, a escala monetária, o formato genérico dos valores e eventual política de arredondamento continuam pendentes em P5-D; não são limites ou tipos aprovados por esta recomendação.

A recomendação consolidada revisada rejeita frações de centavo não nulas e exige correção na fonte para original com formato incompatível antes de selecionar o produto. A proposta também delimita [consulta histórica, recálculo e perda de resolução](../contratos/07-recalculo-precisao.md). As alternativas anteriores permanecem como estudo; não autorizam conversão de legado ou reprodução binária entre versões não previstas no contrato.
