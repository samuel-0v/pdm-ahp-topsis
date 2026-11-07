# Parte 5 — Mapeamento do Domínio para Room, Firestore e Snapshots

**Versão:** 1.0 — P5-A–P5-E aprovadas nas DEC-038–DEC-042; Parte 5 concluída documentalmente.
**Data:** 06/11/2026.
**Situação:** estrutura de seis tabelas e contratos de resultados, Firestore, representação numérica e compatibilidade aprovados pelo usuário. P5-A mantém a aprovação de 05/11/2026; P5-B–P5-E foram aprovadas em 06/11/2026. A implementação da defesa CHECK canônica requer validação antes da implementação definitiva.

[Domínio e Partes 1–4](dominio.md) · [Decisões aprovadas](../decisoes/registro-decisoes.md) · [Requisitos](../requisitos/README.md)

## Contrato consolidado aprovado

As escolhas aprovadas estão nos [Contratos P5-B–P5-E](05-contratos-persistencia.md), incluindo dicionário físico, formato remoto, conversões e compatibilidade. As [DEC-039–DEC-042](../decisoes/registro-decisoes.md) foram formalizadas em 06/11/2026. Os capítulos de estudo abaixo preservam alternativas e recomendações históricas; o contrato consolidado define as escolhas vigentes.

A [resposta à revisão técnica](revisao-parte5.md) registra cinco achados e os ajustes aprovados. A [Etapa 3 está concluída documentalmente](fechamento-etapa-3.md); implementação e testes permanecem futuros.

## Organização por tema

| Documento | Situação |
|---|---|
| [5.1. Base aprovada e limite da proposta](persistencia/01-base-limites.md) | Estudo histórico; contrato vigente aprovado |
| [5.2. Estrutura Room — P5-A aprovada (DEC-038)](persistencia/02-estrutura-room.md) | Estudo histórico; contrato vigente aprovado |
| [5.3. Persistência de AHP e TOPSIS — P5-B](persistencia/03-resultados-numeros.md) | Estudo histórico; contrato vigente aprovado |
| [5.4. Snapshots e independência dos catálogos](persistencia/04-snapshots-proveniencia.md) | Estudo histórico; contrato vigente aprovado |
| [5.5. Salvamento, leitura e exclusão atômicos](persistencia/05-operacoes-atomicas.md) | Estudo histórico; contrato vigente aprovado |
| [5.6. Catálogo oficial no Firestore — P5-C](persistencia/06-firestore-compatibilidade.md) | Estudo histórico; contrato vigente aprovado |
| [5.7. Offline, proveniência e datas — P5-E](persistencia/07-offline-datas.md) | Estudo histórico; contrato vigente aprovado |
| [5.8. Histórico das escolhas submetidas à aprovação](persistencia/08-escolhas-pendentes.md) | Estudo histórico; contrato vigente aprovado |
| [5.9. Verificações previstas da proposta](persistencia/09-verificacoes.md) | Estudo histórico; contrato vigente aprovado |
| [Histórico da proposta](persistencia/10-historico.md) | Estudo histórico; contrato vigente aprovado |

A comparação de alternativas foi preservada nos capítulos. Para as regras vigentes aprovadas, consulte o [contrato consolidado](05-contratos-persistencia.md).
