# Fechamento da Etapa 2 — Engenharia de Requisitos

**Versão:** 1.1.
**Situação:** concluída documentalmente, conforme aceite do usuário.

[Requisitos](README.md) · [Casos de uso](casos-de-uso/README.md) · [Rastreabilidade](rastreabilidade.md) · [Plano de verificação](plano-verificacao.md)

Este registro resume o fechamento e os encaminhamentos. As regras completas permanecem nos requisitos e no [registro de decisões](../decisoes/registro-decisoes.md), evitando duplicação. O nome do arquivo foi preservado para manter os links existentes.

## Escopo consolidado

- RF01–RF27 e RNF01–RNF13 documentados e validados.
- UC01–UC15 aceitos e UC16 documentado conforme exportação aprovada na DEC-032.
- Matriz de rastreabilidade e plano VT01–VT17 conferidos e alinhados às decisões aprovadas.
- DEC-001–DEC-035 registradas; exportação PDF incorpora a revisão explícita da exclusão original da DEC-011.

## Resoluções da revisão

| Ponto | Decisão | Resultado |
|---|---|---|
| P01 — Validação dos critérios | DEC-033 | Metadados genéricos no Firestore, uso offline quando disponíveis e preservação das definições utilizadas. Limites numéricos restantes e schema seguem para modelagem/preparação dos dados. |
| P02 — Identidade das alternativas | DEC-034 | Identidade por origem + ID; não repetir o mesmo registro nem inferir equivalência física por nome/modelo. |
| P03 — Resultado sem diferenciação | DEC-035 | Permitir salvar como “Concluída — sem diferenciação”, sem inventar coeficiente, posição ou vencedor; permitir consulta e exportação. |

P01–P03 estão resolvidos. Os detalhamentos técnicos encaminhados abaixo não reabrem essas decisões.

## Verificação documental realizada

Conferidos os 27 RFs, 13 RNFs e 16 UCs, sem identificadores ausentes ou duplicados, as referências aos 17 conjuntos de verificação e os links/âncoras locais. A revisão relacionou requisitos, casos de uso, decisões e resultados esperados dos testes futuros.

As referências complementares identificadas na revisão foram incorporadas: RF07 → VT03; RF20 → VT08; RNF05 → DEC-035. Foram esclarecidos salvamento manual, identidade dos registros, preservação de metadados e distinção entre ausência de diferenciação e falha matemática.

Esta verificação não executou testes do aplicativo nem constitui nova auditoria integral dos PDFs. A aprovação documental não comprova implementação, entrega ao professor ou aceitação externa das adaptações da DEC-018.

## Encaminhamentos

| Área | Trabalho seguinte |
|---|---|
| Modelagem de domínio e dados | Entidades, identidades, cópias dos dados/metadados, relações, estados e schema. |
| Preparação do catálogo | Fontes/datas, domínios numéricos restantes, rubricas e protocolos comparáveis. |
| Arquitetura e interface | Contratos Room/Firestore, versões Android, dependências, backup, navegação e protótipos. |
| Testes | Ferramentas, massas de dados, referências matemáticas, dispositivo de desempenho, aquecimento e execução do plano. |
| Gestão e entrega | Publicar e integrar a branch conforme DEC-031, acompanhar GitHub Projects e identificar entregas reais por tags. |

O próximo passo do planejamento é a modelagem de domínio e dados. Publicação e integração no GitHub são ações de versionamento; não significam execução dos testes previstos.
