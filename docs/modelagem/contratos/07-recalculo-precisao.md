# 7. Consulta histórica, recálculo e precisão

[Índice dos contratos](../05-contratos-persistencia.md) · [Valores e serialização](03-valores-serializacao.md)

**Status:** aprovado pelo usuário em 06/11/2026 com a DEC-041, incluindo os esclarecimentos da revisão. Não alteram método, RC ou tolerâncias das decisões aprovadas.

## Consulta e evolução do motor

| Operação | Garantia aprovada |
|---|---|
| Reabrir e consultar | Recuperar entradas, definições e resultados salvos de um contrato conhecido, validando estrutura e invariantes do agregado. Não executar o motor atual para exigir igualdade binária com o resultado histórico. |
| Exportar | Usar o resultado salvo elegível e consistente, conforme DEC-032. Atualizar o aplicativo não equivale a solicitar recálculo. |
| Recalcular explicitamente | Usar entradas e definições preservadas com a implementação disponível, sem consultar o catálogo para reinterpretá-las. Se houver resultado novo/diferente, manter a edição pendente até salvamento manual. Falha preserva a última versão salva. |
| Repetir o mesmo cálculo | As mesmas entradas canônicas, conversão e implementação matemática determinística devem produzir o mesmo resultado no mesmo ambiente numérico. A serialização deve preservar os valores binários utilizados. |
| Atualizar motor ou validação | Não há garantia de identidade binária entre implementações ou versões. Mudança isolada no comportamento do motor não torna corrupto o resultado salvo nem autoriza substituí-lo automaticamente. |

“Reproduzir” no RF25 mantém seu objetivo de independência dos catálogos. O contrato não adiciona reprodução binária histórica entre versões, armazenamento de motores antigos ou histórico de resultados. `formatoPayload` versiona a representação dos dados, não identifica o motor.

Validar na leitura significa conferir o contrato conhecido e suas invariantes, inclusive completude, faixas dos resultados, relações entre campos e correspondência dos componentes. Não significa recalcular silenciosamente tudo com a implementação atual. Inconsistência efetiva continua bloqueando apresentação/exportação válida; impossibilidade de interpretar uma versão também deve ser informada. Se entradas históricas não puderem ser processadas com segurança pelo motor disponível, bloquear esse recálculo, sem transformar uma falha nova em alteração automática do estado salvo. Uma implementação que mude o método aprovado ou as tolerâncias exige revisão própria, não está autorizada por esta regra de evolução.

## Resolução na fronteira do motor

Centavos preservados em Long são exatos dentro da faixa do tipo. A conversão para Double em reais pode apagar diferenças e não apenas introduzir erro decimal pequeno. Por exemplo, `9007199254740992` e `9007199254740993` centavos são distintos, mas `centavos.toDouble() / 100.0` resulta no mesmo Double para ambos. A faixa de Long e a finitude do Double não bastam para aceitar uma entrada de cálculo.

**Bloqueio aprovado:** antes do TOPSIS, se dois valores monetários efetivos distintos, do mesmo critério na avaliação, virarem exatamente a mesma entrada Double após a conversão, rejeitar esse processamento como incompatibilidade técnica de resolução. Preservar os valores canônicos e indicar o critério afetado. Não gerar ranking, empate ou conclusão sem diferenciação para contornar essa perda. O teste compara valores efetivos e suas conversões, não valores originais que foram ajustados; a tolerância de empate sobre C* permanece inalterada.

Comparações monetárias com limites de domínio devem usar centavos exatos, sem passar antes por Double. A implementação também deve verificar condições numéricas das operações de normalização, distâncias e denominadores; overflow, underflow que inviabilize o cálculo ou outra falha numérica não autorizam classificar ausência de diferenciação. Não se exige exatidão decimal de todos os passos do TOPSIS.

## Dependência de domínio e critérios de aceitação

Antes de habilitar os critérios reais, definir faixa, resolução exigida e erro de conversão admissível com base nas grandezas/fontes. Além do bloqueio de colisões acima, essas definições precisam determinar quando outras perdas de resolução são aceitáveis. O contrato não inventa teto de preço ou precisão universal nem considera essa dependência encerrada pelo teste de colisão.

Na implementação futura, demonstrar conversão e recálculo para valores representativos e fronteiras do domínio, colisões antes e depois da divisão por 100, normas numericamente inviáveis e casos próximos das fronteiras de RC e empate. Verificar consulta/exportação de resultado salvo sem invocar o motor e recálculo explícito com falha ou resultado diferente. Uma demonstração isolada de Double não substitui testes dos motores ou de persistência.
