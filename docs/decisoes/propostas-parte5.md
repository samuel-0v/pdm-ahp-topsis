# Propostas de decisão — Parte 5

**Versão:** 0.2 — 06/11/2026. **Todas pendentes de aprovação. DEC-040–DEC-042 ajustadas após revisão com ressalvas; redação revisada sujeita a nova conferência técnica.**

[Registro APROVADO](registro-decisoes.md) · [Parte 5](../modelagem/05-persistencia-snapshots.md) · [Contrato consolidado](../modelagem/05-contratos-persistencia.md)

O registro atual termina na DEC-038. DEC-039–DEC-042 são identificadores candidatos, a reconferir na formalização; não estão incluídos na contagem de decisões aprovadas. As preferências fornecidas nesta rodada autorizam elaborar a proposta, sem aprovação retroativa. DEC-001–038 permanecem inalteradas.

Os cinco achados recebidos e a justificativa dos ajustes propostos estão na [resposta à revisão da Parte 5](../modelagem/revisao-parte5.md). Não se transfere o parecer favorável da versão anterior para esta redação sem nova conferência.

## Propostas por tema

| Documento | Situação |
|---|---|
| [DEC-039 — P5-B — Resultados incorporados](propostas/dec-039.md) | Pendente de aprovação |
| [DEC-040 — P5-C — Contrato do catálogo Firestore](propostas/dec-040.md) | Pendente de aprovação |
| [DEC-041 — P5-D — Identidade e representação numérica](propostas/dec-041.md) | Pendente de aprovação |
| [DEC-042 — P5-E — Definições pessoais, snapshots e compatibilidade offline](propostas/dec-042.md) | Pendente de aprovação |

## Limites comuns

Todas dependem do contrato consolidado e preservam DEC-025, DEC-033–038 e os requisitos aprovados. A defesa física CHECK dos pares AHP continua questão técnica aberta da DEC-038, não resolvida por estas propostas. Nenhuma tabela adicional, migration, código Kotlin, configuração Firebase, navegação ou protótipo é autorizado por este documento. A implementação futura deverá comprovar os critérios; não há testes de banco executados nesta rodada.
