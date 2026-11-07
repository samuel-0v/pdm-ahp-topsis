# 3. P5-D — Valores tipados, dinheiro e serialização

[Índice dos contratos](../05-contratos-persistencia.md) · [Parte 5](../05-persistencia-snapshots.md)

**Status:** aprovado pelo usuário em 06/11/2026 nas DEC-038–DEC-042. Contrato documental para orientar implementação; CHECK AHP, domínios/precisão e serialização permanecem dependências técnicas.

## União tipada aprovada

| Variante lógica | Campos Room de cada grupo (`origem`/`efetivo`) | Representação Firestore/JSON |
|---|---|---|
| Ausente | `tipo`, `centavos` e `numero` todos NULL | Chave do critério ausente ou valor null; ambos normalizam para ausência. Não usar objeto vazio ou número fictício. |
| Monetário BRL | `tipo=MONETARIO_BRL`, `centavos` Long não nulo, `numero` NULL | `{tipo: MONETARIO_BRL, centavos: inteiro64, moeda: BRL, escala: 2}` |
| Número não monetário | `tipo=NUMERO`, `numero` Double finito, `centavos` NULL | `{tipo: NUMERO, numero: número finito}` |

Moeda, escala, unidade e período são copiados na definição preservada (por exemplo, preço R$ e seguro R$/ano). Em Firestore/JSON o envelope monetário repete moeda/escala para validação; devem concordar com a definição. No Room elas são obtidas do critério preservado daquela linha, sem repetir sua definição em cada valor. Original e efetivo têm discriminadores/conteúdo próprios e proveniência própria; nunca sobrescrever original ao ajustar efetivo. Sem ajuste, efetivo é cópia do original; original pode ser ausente e efetivo informado depois.

No JSON local, Long usa sequência decimal inteira sem passar por Double, Double usa representação que recupera o mesmo valor binário finito, null permanece null. `formatoPayload` identifica o contrato de serialização, não histórico de avaliações. Formato desconhecido, campos incompatíveis com o discriminador ou mais de uma variante preenchida são erros, não ausência válida. Nenhum decoder pode transformar Long em Double antes de preservar o valor canônico. Firestore recebe inteiro64 real no campo `centavos`, não string ou Double com aparência inteira. O leitor não deve coerci-los silenciosamente.

Valores de rascunho podem faltar. Para preservar uma entrada ainda inválida/incompleta sem inventar número, adota-se `entradaPendenteEfetiva` opcional (texto) no mesmo registro, com valor canônico efetivo ausente; não participa de cálculo. Texto pendente é limpo quando a entrada é corrigida/convertida explicitamente. Produto pessoal não é salvo com campo obrigatório inválido; conclusão não admite entrada pendente. Não há autosave desse texto.

## Conversão e validação

1. Interpretar entrada monetária decimal no contexto pt-BR da interface, sem separadores ambíguos; no contrato serializado usar formato canônico independente de locale. Realizar análise decimal exata antes de obter centavos, usando BigDecimal ou equivalente apenas nessa fronteira.
2. Escala lógica BRL é 2: aceitar valores exatamente representáveis em centavos, incluindo zeros decimais excedentes sem perda (`1,230` equivale a `1,23`). Fração de centavo não zero (`1,231`) é rejeitada para correção explícita. Não truncar nem arredondar silenciosamente. Essa política foi aprovada na DEC-041.
3. Validar a faixa técnica signed64 (-2^63 a 2^63−1) antes de converter; detectar overflow em multiplicação/parse. Negatividade, zero e limites de negócio continuam governados pelos metadados; a faixa de armazenamento não cria novos limites físicos de domínio.
4. Na entrada TOPSIS, obter o valor na unidade do critério por conversão determinística equivalente a `centavos.toDouble() / 100.0`. Não usar divisão inteira. Exemplo: 12345 centavos → 123,45 reais aproximados em Double; manter 12345 canônico no snapshot. Para R$/ano a grandeza permanece anual, sem dividir por 12.
5. Motores recebem Double; verificar finitude/faixa, completude e condições matemáticas. Double pode perder exatidão de inteiros grandes: se um domínio exigir precisão exata não representável, sinalizar incompatibilidade técnica em vez de fingir exatidão. Os domínios reais e a precisão admitida ainda precisam ser documentados; não inventar teto de preços. Conversão aproximada é explícita, não migração do dado armazenado. Os bloqueios de perda de distinção e o alcance do recálculo estão na [seção 7 — recálculo e precisão](07-recalculo-precisao.md).
6. `NUMERO` segue finitude e metadados (inteiro quando exigido, limites inclusivos/exclusivos e escala conhecida). Não aplicar a regra de duas casas a autonomia, notas ou outros critérios sem base nos metadados. Pesos, λmax/IC/RI/RC, distâncias e C* usam Double/REAL; posições/ordens são inteiras, não coeficientes.
7. Ao carregar, validar novamente discriminador, conteúdo, unidade/escala e definição preservada. O mesmo contrato e conversão são usados no primeiro cálculo e no recálculo; não reler unidade atual do catálogo. Não arredondar resultados intermediários; RC e tolerâncias DEC-027/029 permanecem intactos.

Inteiros exatos e ponto flutuante binário têm propriedades diferentes; a conversão para o motor não torna a aritmética TOPSIS decimal exata. [Números no SQLite](https://sqlite.org/floatingpoint.html), [tipos Firestore](https://firebase.google.com/docs/firestore/manage-data/data-types).

## Original incompatível e original fora de domínio

**Formato incompatível:** dinheiro em Double, string, envelope desconhecido, escala diferente ou outra representação que não possa ser decodificada pelo contrato conhecido não integra a união tipada. O produto pode continuar consultável com sinalização quando possível, mas sua seleção para uma avaliação é bloqueada até correção explícita na fonte e nova leitura válida. Informar um efetivo válido não contorna esse bloqueio: não converter o original presumidamente, marcá-lo como ausente ou armazená-lo como NUMERO para acomodar dinheiro legado. Este contrato não preserva payload bruto incompatível na avaliação nem adiciona variante histórica para ele.

O fluxo de ajuste continua disponível para **ausência real** de valor, ou para original com representação canônica conhecida e significado verificável. Original finito e corretamente tipado, mas fora de um limite de domínio, pode ser preservado como evidência do dado recebido, com seu problema indicado; ele não autoriza cálculo enquanto o efetivo não for corrigido e validado. O original é validado quanto a estrutura, identidade e interpretação; o efetivo também deve satisfazer o domínio e os pré-requisitos do processamento. Não atribuir ao efetivo corrigido a fonte ou data do valor anterior como se o descrevessem. Produto pessoal continua sujeito à validação completa do cadastro.

Não há migration ou rotina administrativa proposta. Snapshots históricos no contrato conhecido permanecem interpretados por sua própria versão, não pela revisão remota. Histórico com representação desconhecida não é reparado automaticamente e não pode ser apresentado como íntegro.
