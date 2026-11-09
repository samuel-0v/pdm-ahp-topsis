# Documentação do projeto

| Documento | Situação |
|---|---|
| [Visão e escopo](01-visao-e-escopo.md) | Concluído |
| [Decisões](decisoes/README.md) | DEC-001–DEC-042 aprovadas; DEC-043–DEC-048 adotadas por delegação; DEC-049 corrige a abertura animada do enunciado |
| [Requisitos](requisitos/README.md) | Etapa 2 concluída documentalmente: RF01–RF27 e RNF01–RNF13 |
| [Fechamento da Etapa 2](requisitos/revisao-etapa-2.md) | Concluído: UC01–UC16, rastreabilidade e plano de verificação; P01–P03 resolvidos |
| [Modelagem — índice das Partes 1–5](modelagem/dominio.md) | Partes 1–5 aprovadas; Etapa 3 concluída documentalmente |
| [Persistência e snapshots — Parte 5](modelagem/05-persistencia-snapshots.md) | P5-A–P5-E aprovadas (DEC-038–DEC-042); dependências técnicas preservadas |
| [Contratos de persistência](modelagem/05-contratos-persistencia.md) | Contrato aprovado nas DEC-039–DEC-042 |
| [Resposta à revisão da Parte 5](modelagem/revisao-parte5.md) | Cinco achados; tratamento contratual aprovado em 06/11/2026 |
| [Fechamento da Etapa 3](modelagem/fechamento-etapa-3.md) | Concluído em 06/11/2026; dependências e próximo passo registrados |
| [Plano da Fase 1 — primeira entrega](entregas/fase-1-plano.md) | Proposta para aprovação/distribuição; prazo de 10/11/2026 ainda provisório |
| [Arquitetura](arquitetura/arquitetura.md) | Etapa 4 v0.4: dois achados da revisão v0.3 corrigidos; abertura animada reconhecida na DEC-049 e retorno à seleção explícito; protótipos/encerramento formal ainda pendentes |
| [Interface e prototipagem — Etapa 5](interface/README.md) | Proposta para revisão: telas, identidade, Figma editável e protótipo local navegável; conexões Figma pendentes por limite MCP |
| [Navegação](interface/navegacao.md) | Fluxos de catálogo, avaliação, saída e PDF; Voltar do Android |

Os requisitos detalhados ficam nos respectivos grupos. O registro de decisões e os documentos de modelagem funcionam como índices para arquivos menores, separados por tema. [PDFs do professor](../references/) e [diagramas](../diagrams/).

## Organização e leitura

| Área | Organização |
|---|---|
| Decisões | Registro central; aprovações anteriores em `decisoes/aprovadas/`, escolhas por delegação em `decisoes/adotadas/`, caminhos históricos em `decisoes/propostas/` |
| Modelagem conceitual | Um arquivo por parte, de 1 a 4 |
| Persistência | Estudo e alternativas em `modelagem/persistencia/`; contrato consolidado aprovado em `modelagem/contratos/` |
| Requisitos | Arquivos por grupo funcional, não funcional e de casos de uso; matriz e plano de verificação próprios |

Os índices indicam a situação dos conteúdos. P5-A–P5-E estão aprovadas nas DEC-038–DEC-042, com limites e dependências técnicas preservados. O contrato consolidado contém as regras vigentes; os capítulos de estudo preservam as alternativas históricas avaliadas.

A reorganização anterior não alterou as regras. A formalização de 06/11/2026 registra a aprovação expressa de DEC-039–DEC-042 e não declara execução de testes. As versões históricas dos conteúdos e as datas de aprovação foram preservadas.
