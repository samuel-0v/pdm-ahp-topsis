# Parte 5 — Mapeamento do Domínio para Room, Firestore e Snapshots

**Versão:** 0.5 — P5-A aprovada; P5-B–P5-E ajustadas após revisão com ressalvas, pendentes de nova conferência e confirmação.
**Data:** 06/11/2026.
**Situação:** organização híbrida de seis tabelas e integridade geral da P5-A aprovadas na DEC-038. Demais campos, tipos e escolhas de P5-B–P5-E permanecem propostas. A implementação da defesa CHECK canônica requer validação antes da implementação definitiva.

[Domínio e Partes 1–4](dominio.md) · [Decisões aprovadas](../decisoes/registro-decisoes.md) · [Requisitos](../requisitos/README.md)

## Contrato consolidado para esta revisão

As escolhas preferenciais indicadas pelo usuário foram detalhadas em [Contratos P5-B–P5-E](05-contratos-persistencia.md), incluindo o dicionário físico, formato remoto, conversões e política de compatibilidade. As [propostas DEC-039–042](../decisoes/propostas-parte5.md) aguardam confirmação formal. São candidatas, não pertencem ao conjunto aprovado. Este contrato consolidado especifica a recomendação atual; as alternativas abaixo permanecem como comparação, sem representar escolhas definitivamente aprovadas.

A [resposta à revisão técnica](revisao-parte5.md) registra cinco achados e os ajustes propostos. A Etapa 3 permanece aberta; os ajustes não constituem aprovação automática nem execução de testes.

## Organização por tema

| Documento | Situação |
|---|---|
| [5.1. Base aprovada e limite da proposta](persistencia/01-base-limites.md) | Estudo e alternativas; limites de aprovação preservados |
| [5.2. Estrutura Room — P5-A aprovada (DEC-038)](persistencia/02-estrutura-room.md) | Estudo e alternativas; limites de aprovação preservados |
| [5.3. Persistência de AHP e TOPSIS — P5-B](persistencia/03-resultados-numeros.md) | Estudo e alternativas; limites de aprovação preservados |
| [5.4. Snapshots e independência dos catálogos](persistencia/04-snapshots-proveniencia.md) | Estudo e alternativas; limites de aprovação preservados |
| [5.5. Salvamento, leitura e exclusão atômicos](persistencia/05-operacoes-atomicas.md) | Estudo e alternativas; limites de aprovação preservados |
| [5.6. Catálogo oficial no Firestore — P5-C](persistencia/06-firestore-compatibilidade.md) | Estudo e alternativas; limites de aprovação preservados |
| [5.7. Offline, proveniência e datas — P5-E](persistencia/07-offline-datas.md) | Estudo e alternativas; limites de aprovação preservados |
| [5.8. Escolhas pendentes de aprovação](persistencia/08-escolhas-pendentes.md) | Estudo e alternativas; limites de aprovação preservados |
| [5.9. Verificações previstas da proposta](persistencia/09-verificacoes.md) | Estudo e alternativas; limites de aprovação preservados |
| [Histórico da proposta](persistencia/10-historico.md) | Estudo e alternativas; limites de aprovação preservados |

A comparação de alternativas foi preservada nos capítulos. Para a recomendação detalhada vigente desta proposta, consulte o [contrato consolidado](05-contratos-persistencia.md).
