# 5.8. Escolhas pendentes de aprovação

[Índice da Parte 5](../05-persistencia-snapshots.md) · [Contrato consolidado](../05-contratos-persistencia.md)

**Situação atual:** P5-A–P5-E aprovadas nas DEC-038–DEC-042 em seus níveis documentados. CHECK canônico ainda requer validação técnica.

**Natureza deste capítulo:** estudo histórico das versões 0.1–0.5; referências a propostas ou escolhas pendentes no texto descrevem aquelas rodadas. As regras vigentes estão no [contrato consolidado aprovado](../05-contratos-persistencia.md).

Os identificadores abaixo são tópicos de revisão, não novas decisões DEC. Apenas a base P5-A está aprovada; as recomendações restantes não significam schema final aprovado.

| Tópico | Alternativas | Recomendação e consequência |
|---|---|---|
| **P5-A — Estrutura Room (aprovada, DEC-038)** | Agregado serializado; normalizado; híbrido | Híbrido de seis tabelas: integridade interna mais explícita sem tabela por conceito; mais mapeamento que JSON integral |
| **P5-B — Localização física dos resultados** | Incorporar blocos/colunas; tabelas próprias | Incorporar em avaliação/critério/alternativa, com separação semântica do snapshot; economiza tabelas, exige limpeza/validação conjunta |
| **P5-C — Layout Firestore e revisão** | Critérios embutidos/subcoleção; produtos raiz/subcoleção; revisão explícita | Critérios embutidos e produtos raiz com IDs globais; revisão de definição para detectar incompatibilidade, sem prometer histórico |
| **P5-D — Identificadores e números** | IDs locais opacos textuais ou inteiros; dinheiro em Double, centavos inteiros ou representação decimal | IDs locais opacos estáveis; centavos para preços/seguro em BRL com escala de duas casas, se aprovada; Double para motores/resultados. Tipo genérico, escala e contrato de conversão continuam pendentes |
| **P5-E — Definições pessoais e offline** | Copiar pequenas definições por produto; tabela compartilhada por categoria/revisão | Cópia local junto ao produto; comparação semântica com a definição de destino, classificação compatível/incompatível/indeterminada e ação explícita. Cache pode ser parcial; snapshots históricos independem da revisão remota |

Não foi encontrada incompatibilidade que exija mudar DEC-025, DEC-036 ou DEC-037. As escolhas desta tabela são propostas de implementação para revisão técnica independente e discussão com o usuário.

## Consolidação em rodada única, sem aprovação automática

| Tópico | Escolha preferencial consolidada | Proposta pendente |
|---|---|---|
| P5-B | Campos de resultado incorporados às seis tabelas, com conjuntos coerentes e reconstrução de objetos de valor | DEC-039 |
| P5-C | Categorias raiz com critérios embutidos; produtos raiz; revisão explícita e proveniência por valor | DEC-040 |
| P5-D | UUID textual local; dinheiro BRL em Long centavos; demais valores/resultados Double; união tipada | DEC-041 |
| P5-E | Definições junto ao produto pessoal, snapshots independentes e comparação semântica com o destino; cache SDK | DEC-042 |

O [contrato consolidado](../05-contratos-persistencia.md) resolve os detalhes desta proposta, incluindo rejeição de frações de centavo, formato legado e compatibilidade. As opções são preferências do usuário para elaboração e revisão, não aprovações formais. P5-A permanece aprovada na DEC-038; nenhuma proposta é integrada automaticamente ao conjunto aprovado.
