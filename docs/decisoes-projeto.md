# Registro de Decisões do Projeto — AHP-TOPSIS

**Disciplina:** FACOM32503 — Programação para Dispositivos Móveis
**Instituição:** Universidade Federal de Uberlândia (UFU)
**Semestre:** 2026/2
**Projeto:** Sistema de Avaliação Comparativa Multicritério de Produtos (AHP-TOPSIS)
**Versão:** 1.0
**Data de consolidação:** 01/11/2026
**Situação:** DEC-001 a DEC-019 aprovadas nas discussões da equipe.

Este registro consolida decisões da equipe para orientar os requisitos, a modelagem e o desenvolvimento. Não substitui o enunciado do professor. Quando uma decisão adapta um exemplo ou procedimento do enunciado, a adaptação é identificada.

## 1. Documentos de referência

- [Visão e Escopo](01-visao-e-escopo.md): visão, público, capacidades e limites do produto; o registro de decisões fica centralizado neste documento.
- [Requisitos — versão preliminar 0.1](02-requisitos.md): atores, casos de uso, requisitos funcionais e critérios de aceitação iniciais.
- [Decisões técnicas](06-decisoes-tecnicas.md): detalhamento da DEC-018 e DEC-019.
- [ProjetoDisciplina_PDM20262v2.pdf](../references/ProjetoDisciplina_PDM20262v2.pdf): referência para o enunciado, escopo e entregas.
- [ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf](../references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf): referência matemática principal escolhida pela equipe na DEC-018.

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
| DEC-011 | Fora do escopo | Sem contas/autenticação, sincronização entre dispositivos, categorias ou critérios personalizados, colaboração, histórico de versões, compras ou exportação de relatórios. |
| DEC-012 | Catálogo pessoal | Permite cadastrar, consultar, editar e excluir produtos pessoais com confirmação; mudanças não alteram avaliações salvas. |
| DEC-013 | Bateria de smartphones | Autonomia medida em horas por testes padronizados e comparáveis. |
| DEC-014 | Rubricas qualitativas | Notas documentadas de 1 a 10 para qualidade das câmeras e recursos ADAS. |
| DEC-015 | Veículos | Categoria limitada a veículos totalmente elétricos (BEV). |
| DEC-016 | Autonomia veicular | Usar referência brasileira Inmetro/PBEV, identificando modelo, versão e ano. |
| DEC-017 | Qualidade dos dados | Valores comparáveis, validados, rastreáveis e transparentes; preservar os valores usados na avaliação. |
| DEC-018 | Algoritmos AHP-TOPSIS | Usar `ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf` como referência matemática principal; pesos AHP por média geométrica normalizada. |
| DEC-019 | Exclusão de avaliações | Permitir exclusão permanente de rascunhos e avaliações concluídas após confirmação. |

## 3. Regras transversais

### 3.1. Preservação e catálogo

- Produtos oficiais são somente para consulta no aplicativo.
- Produtos pessoais são locais e podem ser gerenciados pelo usuário.
- Valores ajustados em uma avaliação não modificam catálogos.
- Cada avaliação preserva os dados utilizados para que continue consultável após alterações ou remoções do produto de origem.
- Atualizar o catálogo remoto não altera avaliações existentes.

### 3.2. Avaliações

- A avaliação seleciona produtos de uma categoria e exige pelo menos duas alternativas para processamento.
- Avaliações podem ficar em estado de rascunho ou concluído.
- Editar uma avaliação concluída invalida o resultado anterior como resultado atual até que haja novo processamento.
- Recalcular substitui o resultado anterior; não há histórico de resultados.
- Excluir avaliação exige confirmação e remove localmente os dados e resultados dela, sem apagar produtos dos catálogos. Não há lixeira nem recuperação na interface da primeira versão.

### 3.3. Dados das categorias

**Smartphones Premium:** Geekbench 6 Multi-Core (benefício), preço em R$ (custo), nota de câmeras de 1 a 10 (benefício), autonomia em horas (benefício) e armazenamento em GB (benefício). A versão exata, fonte e data devem ser identificáveis; autonomia e rubrica de câmeras precisam de protocolos comparáveis.

**Veículos Elétricos BEV:** autonomia Inmetro/PBEV em km (benefício), preço de aquisição em R$ (custo), recarga rápida DC de 20% a 80% em minutos (custo), seguro anual em R$/ano (custo) e nota ADAS de 1 a 10 (benefício). Identificar versão/ano; documentar perfil do seguro e rubrica ADAS. A nota ADAS da equipe não é certificação oficial de segurança.

## 4. DEC-018 — Metodologia matemática AHP-TOPSIS

A equipe escolheu o documento complementar [ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf](../references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf) como referência matemática principal. O [enunciado `v2`](../references/ProjetoDisciplina_PDM20262v2.pdf) descreve outro método de obtenção de pesos; a escolha da equipe fica explícita para orientar a implementação e poderá ser confirmada com o professor.

### AHP

A matriz de julgamentos usa a escala de Saaty, reciprocidade (`a_ji = 1/a_ij`) e diagonal unitária. O peso é obtido pela média geométrica normalizada:

`GM_i = (∏_{j=1..n} a_ij)^(1/n)`
`w_i = GM_i / Σ_{k=1..n} GM_k`

Calcular `Aw`, `λmax`, índice de consistência e razão de consistência segundo o documento complementar. Prosseguir ao TOPSIS somente se `RC ≤ 0,10`. Para os cinco critérios das categorias iniciais, usar `IR = 1,12` da tabela adotada no documento.

### TOPSIS

Construir a matriz de decisão; normalizar vetorialmente; ponderar com os pesos AHP; identificar ideais positivo e negativo respeitando critérios de benefício/custo; calcular distâncias euclidianas; calcular o coeficiente de proximidade relativa; ordenar em ordem decrescente.

Os motores AHP e TOPSIS serão componentes Kotlin independentes da interface e do acesso a dados, com validação por testes unitários de resultados conhecidos.

## 5. DEC-019 — Exclusão de avaliações

O usuário pode excluir avaliações em andamento (rascunhos) e concluídas. A ação exige confirmação. Cancelar não altera dados; confirmar remove a avaliação e resultados locais associados. Produtos oficiais do Firebase e produtos pessoais permanecem intactos. A exclusão é permanente na primeira versão.

## 6. Consequências para requisitos e modelagem

- Os requisitos de catálogo devem distinguir Firestore (catálogo oficial) e Room (dados pessoais e avaliações locais).
- O modelo deve representar categorias, critérios, produtos, valores, avaliações, julgamentos AHP e resultados TOPSIS de forma genérica.
- Uma avaliação deve preservar uma cópia dos dados relevantes usados no cálculo.
- Requisitos de edição, recálculo e exclusão devem explicitar estados e efeitos sobre resultados armazenados.
- Requisitos funcionais preliminares e casos de uso estão em [02-requisitos.md](02-requisitos.md); a modelagem de domínio ainda será especificada em [03-modelagem-dominio.md](03-modelagem-dominio.md).

## 7. Pendências de especificação

Estas questões não alteram as decisões aprovadas; precisam ser detalhadas para requisitos, modelagem e testes:

- Selecionar modelos reais e registrar fontes e datas dos dados.
- Definir rubricas de câmeras e ADAS e os protocolos comparáveis de autonomia de smartphones.
- Definir perfil de referência do seguro e condições de medição da recarga rápida.
- Especificar dados ausentes, empates, entradas inválidas e salvamento de rascunhos.
- Detalhar contratos e estrutura Firestore/Room, casos de uso, requisitos não funcionais e critérios de aceitação.
- Confirmar com o professor, se necessário, as adaptações e a diferença metodológica do AHP.

## 8. Histórico

Este documento consolida as decisões DEC-001 a DEC-019 aprovadas nas discussões da equipe até 01/11/2026. Novas decisões devem receber novo identificador e registrar status, contexto, decisão e consequências. Alterações em decisões existentes devem ser registradas, sem apagar silenciosamente o histórico.
