# Modelagem do domínio

**Versão de conteúdo:** 1.3 — Partes 1–5 aprovadas; Etapa 3 concluída documentalmente em 06/11/2026.
**Organização documental:** 05/11/2026 — conteúdo separado por parte.
**Situação:** Partes 1–4 aprovadas conceitualmente nos níveis registrados. A Parte 4 foi aprovada após revisão técnica independente em 04/11/2026. A Parte 5 está aprovada nas DEC-038–DEC-042, com aprovação conjunta de P5-B–P5-E em 06/11/2026. A defesa CHECK canônica ainda requer validação técnica.

[Requisitos](../requisitos/README.md) · [Decisões](../decisoes/registro-decisoes.md)

## Objetivo e limites

Esta modelagem traduz os requisitos aprovados em conceitos do domínio, relações e regras de preservação. A organização conceitual da Parte 1 foi aprovada na DEC-036. A Parte 2 está aprovada em nível conceitual; a Parte 3 detalha cardinalidades conforme DEC-037. A Parte 5 reúne a base Room aprovada na DEC-038 e os contratos de persistência aprovados nas DEC-039–DEC-042.

As definições oficiais de categorias, critérios e produtos vêm do Firestore (DEC-009). Produtos pessoais, avaliações e resultados permanecem locais (DEC-004). A modelagem precisa preservar a identidade de origem dos produtos (DEC-034), a definição dos critérios e os valores usados em cada avaliação (DEC-017, DEC-033), bem como representar conclusão sem diferenciação (DEC-035).

## Partes da modelagem

| Documento | Situação |
|---|---|
| [Parte 1 — Entidades e responsabilidades](01-entidades-responsabilidades.md) | Aprovada conceitualmente |
| [Parte 2 — Atributos e regras](02-atributos-regras.md) | Aprovada conceitualmente |
| [Parte 3 — Relacionamentos e cardinalidades](03-relacionamentos-cardinalidades.md) | Aprovada conceitualmente |
| [Parte 4 — Estados e transições](04-estados-transicoes.md) | Aprovada conceitualmente |
| [Parte 5 — Persistência e snapshots](05-persistencia-snapshots.md) | Aprovada — DEC-038–DEC-042 |

**Rastreabilidade:** RF01–RF27, RNF01–RNF13; DEC-004, DEC-009, DEC-017, DEC-022, DEC-024, DEC-025, DEC-033–DEC-042.

As Partes 1–4 mantêm seu escopo conceitual; a Parte 5 aprova os contratos de persistência. O [fechamento da Etapa 3](fechamento-etapa-3.md) registra as dependências de implementação e a próxima etapa de Arquitetura.
