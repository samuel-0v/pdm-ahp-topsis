# Documentação do projeto

| Documento | Situação |
|---|---|
| [Visão e escopo](01-visao-e-escopo.md) | Concluído |
| [Decisões](decisoes/README.md) | DEC-001–DEC-038 aprovadas |
| [Requisitos](requisitos/README.md) | Etapa 2 concluída documentalmente: RF01–RF27 e RNF01–RNF13 |
| [Fechamento da Etapa 2](requisitos/revisao-etapa-2.md) | Concluído: UC01–UC16, rastreabilidade e plano de verificação; P01–P03 resolvidos |
| [Modelagem — índice das Partes 1–5](modelagem/dominio.md) | Partes 1–4 aprovadas conceitualmente; Parte 5 parcialmente aprovada |
| [Persistência e snapshots — Parte 5](modelagem/05-persistencia-snapshots.md) | P5-A aprovada (DEC-038); P5-B–P5-E pendentes; CHECK requer validação |
| [Contratos de persistência](modelagem/05-contratos-persistencia.md) | Detalhamento proposto de P5-B–P5-E, pendente de aprovação |
| [Resposta à revisão da Parte 5](modelagem/revisao-parte5.md) | Cinco achados; ajustes propostos para nova conferência e aprovação |
| [Arquitetura](arquitetura/arquitetura.md) | Pendente |
| [Navegação](interface/navegacao.md) | Fluxo funcional inicial |

Os requisitos detalhados ficam nos respectivos grupos. O registro de decisões e os documentos de modelagem funcionam como índices para arquivos menores, separados por tema. [PDFs do professor](../references/) e [diagramas](../diagrams/).

## Organização e leitura

| Área | Organização |
|---|---|
| Decisões | Resumo no registro, detalhamentos em `decisoes/aprovadas/`, propostas em `decisoes/propostas/` |
| Modelagem conceitual | Um arquivo por parte, de 1 a 4 |
| Persistência | Estudo e alternativas em `modelagem/persistencia/`; contrato consolidado proposto em `modelagem/contratos/` |
| Requisitos | Arquivos por grupo funcional, não funcional e de casos de uso; matriz e plano de verificação próprios |

Os índices indicam a situação dos conteúdos. A P5-A está aprovada com os limites da DEC-038; as propostas DEC-039–DEC-042 permanecem pendentes. O contrato consolidado detalha a recomendação atual das propostas, enquanto o estudo preserva as alternativas avaliadas.

Esta organização não altera decisões nem declara execução de testes. As versões históricas dos conteúdos e as datas de aprovação foram preservadas.
