# Registro de Decisões do Projeto — AHP-TOPSIS

**Disciplina:** FACOM32503 — Programação para Dispositivos Móveis
**Instituição:** Universidade Federal de Uberlândia (UFU)
**Semestre:** 2026/2
**Projeto:** Sistema de Avaliação Comparativa Multicritério de Produtos (AHP-TOPSIS)
**Versão:** 1.15
**Data de consolidação:** 06/11/2026
**Situação:** DEC-001 a DEC-042 aprovadas nas discussões da equipe.
**Organização documental:** 05/11/2026 — detalhamentos separados por decisão; versão de conteúdo preservada.

Este registro consolida decisões da equipe para orientar os requisitos, a modelagem e o desenvolvimento. Não substitui o enunciado do professor. Quando uma decisão adapta um exemplo ou procedimento do enunciado, a adaptação é identificada.

## 1. Documentos de referência

- [Visão e Escopo](../01-visao-e-escopo.md): visão, público, capacidades e limites do produto; este registro reúne o resumo e os links dos detalhamentos.
- [Especificação de requisitos](../requisitos/README.md): RF01–RF27 e RNF01–RNF13 validados documentalmente.
- [Decisões técnicas](../arquitetura/arquitetura.md): questões técnicas ainda pendentes.
- [ProjetoDisciplina_PDM20262v2.pdf](../../references/ProjetoDisciplina_PDM20262v2.pdf): referência para o enunciado, escopo e entregas.
- [ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf](../../references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf): referência matemática principal escolhida pela equipe na DEC-018.

## 2. Resumo das decisões

| ID | Tema | Decisão aprovada |
|---|---|---|
| DEC-001 | Modelo do aplicativo | Comparador híbrido com arquitetura genérica preparada para futuras configurações. |
| DEC-002 | Público-alvo | Consumidores individuais e equipes/organizações, sem recursos corporativos específicos nesta versão. |
| DEC-003 | Valores dos produtos | Ajustáveis por avaliação, sem alterar os valores originais do catálogo. |
| DEC-004 | Persistência | Produtos pessoais, avaliações e resultados ficam armazenados localmente; Room é a tecnologia prevista. |
| DEC-005 | Seleção de alternativas | Usuário seleciona/adiciona produtos; cada avaliação exige pelo menos duas alternativas. |
| DEC-006 | Ciclo de vida da avaliação | Rascunhos podem ser salvos e retomados; avaliações concluídas podem ser editadas e recalculadas; sem histórico de versões. |
| DEC-007 | Novos produtos | Produtos cadastrados pelo usuário são salvos no catálogo pessoal local e podem ser reutilizados. |
| DEC-008 | Categorias iniciais | Smartphones Premium e Veículos Elétricos, com categorias e critérios predefinidos. |
| DEC-009 | Catálogo remoto | Firebase Cloud Firestore é a fonte remota oficial de categorias, critérios e produtos; leitura pelo app. |
| DEC-010 | Atualização e offline | Atualização automática com conexão e cache offline para dados previamente consultados; primeira consulta offline não é garantida. |
| DEC-011 | Fora do escopo | Sem contas/autenticação, sincronização entre dispositivos, categorias ou critérios personalizados, colaboração, histórico de versões, compras ou importação de avaliações. Exportação PDF permitida pela DEC-032, que revisa a exclusão original de relatórios. |
| DEC-012 | Catálogo pessoal | Permite cadastrar, consultar, editar e excluir produtos pessoais com confirmação; mudanças não alteram avaliações salvas. |
| DEC-013 | Bateria de smartphones | Autonomia medida em horas por testes padronizados e comparáveis. |
| DEC-014 | Rubricas qualitativas | Notas documentadas de 1 a 10 para qualidade das câmeras e recursos ADAS. |
| DEC-015 | Veículos | Categoria limitada a veículos totalmente elétricos (BEV). |
| DEC-016 | Autonomia veicular | Usar referência brasileira Inmetro/PBEV, identificando modelo, versão e ano. |
| DEC-017 | Qualidade dos dados | Valores comparáveis, validados, rastreáveis e transparentes; preservar os valores usados na avaliação. |
| DEC-018 | Algoritmos AHP-TOPSIS | Usar `ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf` como referência matemática principal; pesos AHP por média geométrica normalizada. |
| DEC-019 | Exclusão de avaliações | Permitir exclusão permanente de rascunhos e avaliações concluídas após confirmação. |
| DEC-020 | Origem dos dados | Indicar quando o catálogo oficial veio do cache do Firebase e mostrar a última atualização conhecida, quando disponível. |
| DEC-021 | Categoria do produto pessoal | Definida no cadastro e imutável; para usar outra categoria, cadastrar novo produto. |
| DEC-022 | Categoria da avaliação | Definida ao criar a avaliação e imutável; para comparar outra categoria, criar outra avaliação. |
| DEC-023 | Comparações AHP | Guiadas, um par de critérios por vez, com progresso e possibilidade de revisar. |
| DEC-024 | Empates TOPSIS | Preservar posições empatadas e informar ausência de diferenciação sem eleger vencedor arbitrário. |
| DEC-025 | Salvamento | Manual; avisar ao sair com alterações não salvas e oferecer salvar, descartar ou continuar editando. |
| DEC-026 | Desempenho | Até 500 ms em pelo menos 95% das medições por cenário; condições no detalhamento abaixo. |
| DEC-027 | Empates TOPSIS | Tolerância absoluta de 10⁻⁹; comparar com o maior coeficiente do grupo. |
| DEC-028 | Posições no ranking | Numeração competitiva: 1º, 2º, 2º, 4º. |
| DEC-029 | Testes matemáticos | Tolerância absoluta e relativa combinada de 10⁻⁹. |
| DEC-030 | Backup e transferência | Excluir produtos pessoais e avaliações dos mecanismos de backup e transferência do Android. |
| DEC-031 | Gestão e colaboração | GitHub Projects para tarefas e fluxo de branches/PRs proporcional às alterações. |
| DEC-032 | Exportação PDF | Relatório local de avaliação salva e processada, compartilhamento explícito e tratamento de alterações não salvas. |
| DEC-033 | Metadados de validação dos critérios | Definições de critérios oficiais no Firestore incluem metadados genéricos reutilizáveis de validação; valores ainda não definidos além das faixas previamente aprovadas. |
| DEC-034 | Identidade de produtos na seleção | Duplicidade é definida pela identidade do registro de origem; não há deduplicação automática entre catálogo oficial e pessoal. |
| DEC-035 | Estado sem diferenciação | Processamento TOPSIS válido sem C* definido é persistido/listado como “Concluída — sem diferenciação”. |
| DEC-036 | Organização conceitual do domínio | Avaliação é raiz de agregado; alternativas e julgamentos são componentes internos; resultados são objetos de valor; critérios e identidade histórica são preservados sem depender do catálogo atual. |
| DEC-037 | Cardinalidades conceituais da modelagem | Categorias iniciais têm cinco critérios; futuras podem variar n dentro do suporte matemático; critérios atuais são exclusivos por categoria; rascunhos podem preservar AHP inconsistente sem liberar TOPSIS. |
| DEC-038 | Estrutura Room (P5-A) | Modelo híbrido de seis tabelas; CHECK canônico pendente de validação; detalhes de P5-B–P5-E aprovados posteriormente nas DEC-039–DEC-042. |
| DEC-039 | Resultados incorporados (P5-B) | Indicadores AHP na avaliação, pesos nos critérios e detalhes TOPSIS nas alternativas; conjuntos completos e invalidação coerente. |
| DEC-040 | Contrato Firestore (P5-C) | Critérios embutidos e produtos raiz; revisão imutável sob responsabilidade do publicador, com limite explícito de detecção pelo cliente. |
| DEC-041 | Identidade e números (P5-D) | UUID local, BRL em Long centavos, demais números/resultados Double; validação tipada, bloqueio de colisões monetárias e alcance do recálculo delimitado. |
| DEC-042 | Snapshots e offline (P5-E) | Definições locais pessoais, snapshots independentes e compatibilidade com o destino; incompatibilidade semântica exige novo cadastro. |

## Detalhamento das decisões aprovadas

DEC-001–DEC-017, DEC-020 e DEC-021 estão descritas no resumo acima e nas regras transversais. Os detalhamentos existentes das demais decisões ficam nos arquivos abaixo.

| Documento | Situação |
|---|---|
| [DEC-018 — Metodologia matemática AHP-TOPSIS](aprovadas/dec-018.md) | Aprovada |
| [DEC-019 — Exclusão de avaliações](aprovadas/dec-019.md) | Aprovada |
| [DEC-022 — Categoria da avaliação imutável](aprovadas/dec-022.md) | Aprovada |
| [DEC-023 — Comparações AHP guiadas](aprovadas/dec-023.md) | Aprovada |
| [DEC-024 — Empates e ausência de diferenciação no TOPSIS](aprovadas/dec-024.md) | Aprovada |
| [DEC-025 — Salvamento manual e alterações não salvas](aprovadas/dec-025.md) | Aprovada |
| [DEC-026 — Desempenho dos cálculos](aprovadas/dec-026.md) | Aprovada |
| [DEC-027 — Tolerância e formação dos grupos de empate TOPSIS](aprovadas/dec-027.md) | Aprovada |
| [DEC-028 — Numeração das posições no ranking](aprovadas/dec-028.md) | Aprovada |
| [DEC-029 — Tolerância dos testes matemáticos](aprovadas/dec-029.md) | Aprovada |
| [DEC-030 — Backup e transferência dos dados pessoais](aprovadas/dec-030.md) | Aprovada |
| [DEC-031 — Gestão das atividades e colaboração](aprovadas/dec-031.md) | Aprovada |
| [DEC-032 — Exportação de avaliações em PDF](aprovadas/dec-032.md) | Aprovada |
| [DEC-033 — Metadados reutilizáveis de validação dos critérios](aprovadas/dec-033.md) | Aprovada |
| [DEC-034 — Identidade de produtos na seleção de alternativas](aprovadas/dec-034.md) | Aprovada |
| [DEC-035 — Estado persistido sem diferenciação](aprovadas/dec-035.md) | Aprovada |
| [DEC-036 — Organização conceitual do domínio](aprovadas/dec-036.md) | Aprovada |
| [DEC-037 — Cardinalidades conceituais da modelagem](aprovadas/dec-037.md) | Aprovada |
| [DEC-038 — Estrutura Room híbrida de seis tabelas (P5-A)](aprovadas/dec-038.md) | Aprovada |
| [DEC-039 — P5-B — Resultados incorporados](aprovadas/dec-039.md) | Aprovada em 06/11/2026 |
| [DEC-040 — P5-C — Contrato do catálogo Firestore](aprovadas/dec-040.md) | Aprovada em 06/11/2026 |
| [DEC-041 — P5-D — Identidade e representação numérica](aprovadas/dec-041.md) | Aprovada em 06/11/2026 |
| [DEC-042 — P5-E — Definições pessoais, snapshots e compatibilidade offline](aprovadas/dec-042.md) | Aprovada em 06/11/2026 |

## Documentos de apoio

| Documento | Situação |
|---|---|
| [Regras transversais](regras-transversais.md) | Base aprovada |
| [Consequências para requisitos e modelagem](consequencias-modelagem.md) | Orientações derivadas |
| [Pendências, histórico e validação documental](acompanhamento.md) | Sem comprovação de implementação |

As quatro propostas da Parte 5 foram aprovadas conjuntamente em 06/11/2026, incluindo os ajustes da revisão. Consulte o [histórico da formalização](propostas-parte5.md) e o [fechamento da Etapa 3](../modelagem/fechamento-etapa-3.md).
