# Registro de Decisões do Projeto — AHP-TOPSIS

**Disciplina:** FACOM32503 — Programação para Dispositivos Móveis
**Instituição:** Universidade Federal de Uberlândia (UFU)
**Semestre:** 2026/2
**Projeto:** Sistema de Avaliação Comparativa Multicritério de Produtos (AHP-TOPSIS)
**Versão:** 1.14
**Data de consolidação:** 05/11/2026
**Situação:** DEC-001 a DEC-038 aprovadas nas discussões da equipe.

Este registro consolida decisões da equipe para orientar os requisitos, a modelagem e o desenvolvimento. Não substitui o enunciado do professor. Quando uma decisão adapta um exemplo ou procedimento do enunciado, a adaptação é identificada.

## 1. Documentos de referência

- [Visão e Escopo](../01-visao-e-escopo.md): visão, público, capacidades e limites do produto; o registro de decisões fica centralizado neste documento.
- [Especificação de requisitos funcionais — versão 1.0](../requisitos/README.md): RF01–RF26 validados funcionalmente.
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
| DEC-038 | Estrutura Room (P5-A) | Modelo híbrido de seis tabelas; CHECK canônico pendente de validação; P5-B–P5-E não aprovadas. |

## 3. Regras transversais

### 3.1. Preservação e catálogo

- Produtos oficiais são somente para consulta no aplicativo.
- Produtos pessoais são locais e podem ser gerenciados pelo usuário.
- A categoria do produto pessoal é definida no cadastro e não pode ser alterada; mudanças de categoria exigem novo cadastro.
- Valores ajustados em uma avaliação não modificam catálogos.
- Cada avaliação preserva os dados utilizados para que continue consultável após alterações ou remoções do produto de origem.
- Atualizar o catálogo remoto não altera avaliações existentes.

### 3.2. Avaliações

- A avaliação seleciona produtos de uma categoria e exige pelo menos duas alternativas para processamento.
- Avaliações podem ficar em estado de rascunho ou concluído.
- Alterar entradas de cálculo invalida o resultado para a edição em memória; a versão persistida só é alterada ao salvar, quando entradas e invalidação do resultado anterior são gravadas atomicamente (DEC-025, RNF05).
- Recalcular gera o novo resultado em memória; salvar explicitamente substitui a versão persistida. Não há histórico de resultados.
- Excluir avaliação exige confirmação e remove localmente os dados e resultados dela, sem apagar produtos dos catálogos. Não há lixeira nem recuperação na interface da primeira versão.

### 3.3. Dados das categorias

**Smartphones Premium:** Geekbench 6 Multi-Core (benefício), preço em R$ (custo), nota de câmeras de 1 a 10 (benefício), autonomia em horas (benefício) e armazenamento em GB (benefício). A versão exata, fonte e data devem ser identificáveis; autonomia e rubrica de câmeras precisam de protocolos comparáveis.

**Veículos Elétricos BEV:** autonomia Inmetro/PBEV em km (benefício), preço de aquisição em R$ (custo), recarga rápida DC de 20% a 80% em minutos (custo), seguro anual em R$/ano (custo) e nota ADAS de 1 a 10 (benefício). Identificar versão/ano; documentar perfil do seguro e rubrica ADAS. A nota ADAS da equipe não é certificação oficial de segurança.

### 3.4. Origem dos dados oficiais (DEC-020)

Quando o catálogo oficial for apresentado a partir do cache do Firebase, o aplicativo indicará essa origem e mostrará a data da última atualização conhecida, quando disponível. Não se deve estimar ou inventar a data. Os detalhes estão nos requisitos RF03–RF05.

### 3.5. Categoria dos produtos pessoais (DEC-021)

A categoria é selecionada no cadastro e permanece imutável. Na edição, fica visível somente para consulta; nome, fabricante, modelo e valores dos critérios podem ser alterados. Para usar outra categoria, o usuário cadastra outro produto. Avaliações existentes preservam os valores utilizados, independentemente de alterações posteriores no catálogo pessoal.
## 4. DEC-018 — Metodologia matemática AHP-TOPSIS

A equipe escolheu o documento complementar [ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf](../../references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf) como referência matemática principal. O [enunciado `v2`](../../references/ProjetoDisciplina_PDM20262v2.pdf) descreve outro método de obtenção de pesos; a escolha da equipe fica explícita para orientar a implementação e poderá ser confirmada com o professor.

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

## 6. DEC-022 — Categoria da avaliação imutável

A categoria é escolhida ao criar uma avaliação e não pode ser alterada depois. Para comparar produtos de outra categoria, o usuário deve criar uma nova avaliação. Produtos selecionados, valores e julgamentos AHP podem ser editados, desde que pertençam à categoria da avaliação.

## 7. DEC-023 — Comparações AHP guiadas

A interface apresenta um par de critérios por vez, com indicador de progresso e opção de voltar para revisar julgamentos. Para cinco critérios, há dez comparações independentes. O usuário informa sentido da preferência e intensidade pela escala de Saaty (1 a 9, incluindo 2, 4, 6 e 8); a matriz mantém diagonal igual a 1 e preenche automaticamente o recíproco. Julgamentos parciais podem ser salvos em rascunho, mas uma matriz incompleta não pode ser aprovada. Alterar comparações invalida a consistência previamente calculada. O algoritmo permanece o definido na DEC-018.

## 8. DEC-024 — Empates e ausência de diferenciação no TOPSIS

Alternativas com coeficientes de proximidade matematicamente empatados compartilham a posição. O aplicativo não usa preço, nome, ordem de cadastro ou outro critério secundário para desempatar. Se as alternativas forem indistinguíveis e `d+ = d- = 0`, `C*` fica indefinido e o aplicativo informa “Sem diferenciação entre as alternativas”, sem fabricar coeficientes ou um vencedor. A tolerância e formação dos grupos seguem a DEC-027; a numeração das posições segue a DEC-028.

## 9. DEC-025 — Salvamento manual e alterações não salvas

Rascunhos, alterações em avaliações existentes e resultados são persistidos somente após ação explícita do usuário. Ao tentar sair com alterações pendentes, o aplicativo oferece “Salvar”, “Descartar alterações” e “Continuar editando”. O sucesso só é informado após confirmar a persistência no Room. Descartar mantém a última versão salva. Ao salvar mudanças que afetam uma avaliação concluída, o resultado anterior é invalidado; o novo resultado precisa ser recalculado e salvo explicitamente. Processar não salva automaticamente. Alterações não salvas podem se perder se o aplicativo encerrar inesperadamente. O salvamento do produto pessoal após o cadastro bem-sucedido (DEC-007) não significa salvar automaticamente o formulário.

## DEC-026 — Desempenho dos cálculos

**Status:** aprovada.

Para avaliações matematicamente válidas com 2 a 50 produtos e cinco critérios, o processamento conjunto AHP e TOPSIS deverá terminar em até 500 ms em pelo menos 95% das medições de cada cenário. Inclui validações matemáticas, pesos, consistência, TOPSIS e ordenação final; exclui rede, persistência e renderização.

A verificação usará cenários de 2, 10 e 50 produtos, com 100 medições por cenário após aquecimento, em build de produção e dispositivo físico documentado. A execução deverá manter a interface responsiva. O limite de 50 produtos vale exclusivamente para testes de desempenho, sem restrição funcional.

## DEC-027 — Tolerância e formação dos grupos de empate TOPSIS

**Status:** aprovada, incluindo complementação.

Usar os coeficientes internos antes do arredondamento para exibição. Após ordenação decrescente, o maior coeficiente de cada grupo é sua referência. Um produto integra o grupo se a diferença absoluta para essa referência for menor ou igual a 10⁻⁹; caso contrário, inicia novo grupo. Não comparar somente vizinhos, evitando encadear diferenças que ultrapassem a tolerância em relação à referência.

## DEC-028 — Numeração das posições no ranking

**Status:** aprovada.

Usar posições competitivas, pulando posições após empates: 1º, 2º, 2º, 4º. Os grupos são definidos pela DEC-027.

## DEC-029 — Tolerância dos testes matemáticos

**Status:** aprovada.

Aceitar um resultado de teste quando `|calculado − referência| ≤ 10⁻⁹ + 10⁻⁹ × |referência|`. Aplicar a pesos, indicadores AHP, distâncias e coeficientes, utilizando referências independentes e de precisão suficiente. Valores arredondados dos PDFs, isoladamente, não bastam para verificar essa tolerância. Esta margem não altera o limite de consistência AHP nem a formação dos grupos de empate.

## DEC-030 — Backup e transferência dos dados pessoais

**Status:** aprovada — opção A.

Excluir produtos pessoais e avaliações dos mecanismos de backup e transferência de dados do Android. Configurar e verificar a restrição nas versões suportadas. Comunicar ao usuário que a desinstalação do aplicativo ou a perda do aparelho pode causar perda definitiva dos dados. Os critérios de privacidade e verificação estão no [RNF08](../requisitos/nao-funcionais/04-seguranca-privacidade.md#rnf08--privacidade-dos-dados-pessoais).

## DEC-031 — Gestão das atividades e colaboração

**Status:** aprovada.

Utilizar GitHub Projects com tarefas contendo título, descrição e critério de conclusão; atribuir responsável às tarefas assumidas; acompanhar pelos estados **A fazer**, **Em andamento** e **Concluído**. Cada integrante atualiza suas tarefas ao iniciar ou concluir o trabalho. Pendências e tarefas das entregas ficam registradas. Sprints, estimativas em pontos e automações não são obrigatórios.

Código e alterações relevantes de requisitos ou arquitetura usam branch própria e PR. Pequenas correções de texto podem receber commit direto. PRs recebem revisão de outro integrante antes da integração, quando disponível.

A aprovação define o processo; não declara configuração do GitHub Projects ou execução de operações remotas. Critérios verificáveis em [RNF12 e RNF13](../requisitos/nao-funcionais/06-processo-desenvolvimento.md#rnf12--controle-de-versão).

## DEC-032 — Exportação de avaliações em PDF

**Status:** aprovada, incluindo geração, conteúdo e tratamento de alterações não salvas.
**Origem:** ampliação de escopo aprovada pelo usuário; não é apresentada como exigência adicional do professor.

Permitir exportar avaliações salvas e processadas em relatório PDF, incluindo avaliações sem diferenciação entre alternativas. O relatório usa a última versão salva com processamento correspondente aos seus dados. Rascunhos, importação e matrizes intermediárias de cálculo ficam fora desta funcionalidade na primeira versão.

A geração ocorre localmente, sem depender de internet. Após gerar o arquivo, o aplicativo abre o compartilhamento padrão do Android; o usuário escolhe um aplicativo ou destino disponível. O envio depende de sua ação explícita. A disponibilidade de rede exigida por um destino externo não é requisito para gerar o PDF.

**Conteúdo aprovado:**

1. Identificação da avaliação e categoria.
2. Data de geração do PDF, distinta das datas de referência dos dados.
3. Produtos, valores utilizados e unidades.
4. Critérios, indicação de custo/benefício e pesos AHP.
5. Indicadores AHP: λmax, IC e RC.
6. Ranking, coeficientes TOPSIS e distâncias d+ e d−, quando matematicamente definidos.
7. Indicação clara de empates ou ausência de diferenciação, sem inventar coeficiente, posição ou vencedor.
8. Referência ao método AHP-TOPSIS utilizado.

**Alterações não salvas:** oferecer **Salvar e exportar**, **Exportar última versão salva** e **Cancelar**. Salvar e exportar exige processamento válido e atualizado e sucesso na persistência antes da geração. Se a edição exigir recálculo, informar a necessidade antes de permitir sua exportação. Exportar a última versão salva só está disponível quando ela já estiver processada; não mistura a edição em memória com os resultados persistidos. Cancelar retorna à avaliação.

Falhas de geração são informadas sem alterar a avaliação salva. Cancelar o compartilhamento não exclui nem desfaz a avaliação; geração do arquivo ou abertura do compartilhamento não comprova envio ao destino.

**Alteração da DEC-011:** a exclusão original de exportação de relatórios é substituída por esta permissão específica. As demais exclusões permanecem. Não adiciona sincronização, backup restaurável nem importação de avaliações; DEC-030 permanece aplicável aos dados internos.

**Rastreabilidade:** [RF27](../requisitos/funcionais/07-exportacao.md), [UC16](../requisitos/casos-de-uso/05-exportacao.md). O compartilhamento explícito do PDF complementa a privacidade do RNF08 sem autorizar envio dos dados pessoais ao Firestore.

## 10. Consequências para requisitos e modelagem

- Os requisitos de catálogo devem distinguir Firestore (catálogo oficial) e Room (dados pessoais e avaliações locais).
- O modelo deve representar categorias, critérios, produtos, valores, avaliações, julgamentos AHP e resultados TOPSIS de forma genérica.
- As definições oficiais de categorias, critérios e produtos têm origem no Firestore (DEC-009). Pela DEC-033, a definição de cada critério também fornece metadados reutilizáveis para validar valores; o aplicativo aplica validação genérica e pode usar definições em cache offline. Produtos pessoais permanecem locais e referenciam os critérios da categoria. Avaliações persistidas preservam os metadados e valores usados, evitando que uma atualização do catálogo altere seu significado.
- Pela DEC-034, a identidade da alternativa selecionada é composta pela origem e pelo identificador do registro: ID oficial do Firestore ou ID local do catálogo pessoal. A mesma identidade não pode ser selecionada mais de uma vez; registros distintos de catálogos diferentes podem coexistir mesmo que descrevam o mesmo produto físico, pois não há equivalência automática por nome/modelo/versão.
- Pela DEC-035, o processamento válido em que todas as alternativas são indistinguíveis e C* fica indefinido pode ser salvo como resultado concluído e aparece na listagem com o estado “Concluída — sem diferenciação”, distinto de rascunho e falha matemática.
- Uma avaliação deve preservar uma cópia dos dados relevantes usados no cálculo.
- Requisitos de edição, recálculo e exclusão devem explicitar estados e efeitos sobre resultados armazenados.
- A especificação funcional validada RF01–RF26 está em [Requisitos](../requisitos/README.md); a modelagem de domínio ainda será especificada em [Modelagem de domínio](../modelagem/dominio.md).

## DEC-033 — Metadados reutilizáveis de validação dos critérios

**Status:** aprovada pelo usuário em 02/11/2026 durante a revisão da Etapa 2 (P01).
**Origem:** resolução de P01; complementa a DEC-009 sem alterar sua definição do Firestore como fonte oficial.

As categorias, critérios e produtos oficiais continuam definidos no Firebase Cloud Firestore. A definição remota de cada critério deve conter metadados genéricos reutilizáveis para validar valores, incluindo tipo, unidade, domínio ou limites aplicáveis, tratamento de ausência e natureza de benefício/custo. O aplicativo deve validar valores por mecanismo genérico orientado por esses metadados, sem regras específicas embutidas por categoria. As definições disponíveis em cache devem permitir a mesma validação offline.

Esta decisão não fixa novos limites numéricos. Permanecem válidos os limites já aprovados, como notas de câmeras e ADAS de 1 a 10; outros domínios e limites devem ser documentados com base no significado físico, na unidade e/ou na fonte pertinente. Produto pessoal continua armazenado localmente e associado à definição do critério da categoria. Avaliações salvas preservam os metadados e valores usados para manter sua interpretação estável diante de atualizações do catálogo.

**Consequências:** RF04 e RF13 detalham a leitura e aplicação dos metadados; RF06/RF07 aplicam as definições às entradas do catálogo pessoal; a modelagem futura deverá definir identificadores/versionamento das definições e cópia dos metadados nas avaliações. Isso não altera o armazenamento local de dados pessoais (DEC-004/DEC-030), nem autoriza escrita do cliente Android no catálogo oficial (DEC-009).

## DEC-034 — Identidade de produtos na seleção de alternativas

**Status:** aprovada pelo usuário em 02/11/2026 durante a revisão da Etapa 2 (P02).
**Origem:** resolução de P02; esclarece a prevenção de duplicidade da seleção sem alterar as decisões de catálogo.

Cada alternativa é identificada pelo par origem e identificador do registro. Para produtos oficiais, usa-se o identificador do documento no Firestore; para produtos pessoais, o identificador local. A avaliação não pode conter duas seleções da mesma identidade. Não se faz deduplicação automática entre origens por nome, fabricante, modelo ou versão: dois registros distintos podem ser selecionados mesmo que descrevam o mesmo produto físico. A equipe pode definir equivalência entre catálogos em decisão futura, se necessário.

**Consequências:** RF11 e UC07 aplicam a prevenção pela identidade do registro; a modelagem deve preservar origem e identificador nas alternativas e avaliações. A regra não declara que registros distintos representam necessariamente produtos físicos distintos.

## DEC-035 — Estado persistido sem diferenciação

**Status:** aprovada pelo usuário em 02/11/2026 durante a revisão da Etapa 2 (P03).
**Origem:** resolução do ponto restante de P03; complementa DEC-024 e DEC-032 sem alterar seu comportamento matemático ou de exportação.

Quando TOPSIS termina com processamento matematicamente válido, mas todas as alternativas são indistinguíveis e C* não está definido, a avaliação pode ser salva como concluída. Na listagem de avaliações, seu estado é apresentado como **“Concluída — sem diferenciação”**, distinto de rascunho e de falha matemática. O resultado preserva os indicadores calculáveis e não inventa C*, posição ou vencedor, conforme DEC-024 e DEC-032.

**Consequências:** RF20/RF22 e UC11/UC13 identificam explicitamente este estado; a modelagem representa conclusão válida sem coeficiente/ranking definido. Exportação continua conforme DEC-032.

## DEC-036 — Organização conceitual do domínio

**Status:** aprovada pelo usuário em 02/11/2026 ao iniciar a Etapa 3.
**Origem:** aprovação da proposta de Modelagem do Domínio v0.2, com os três ajustes abaixo.

Adotar os oito conceitos principais: Categoria, Critério, Produto, Avaliação, Alternativa da avaliação, Julgamento AHP, Resultado AHP e Resultado TOPSIS. A Avaliação é a raiz do agregado e responde pela consistência de seus componentes. Alternativas e julgamentos são componentes internos; resultados AHP/TOPSIS são objetos de valor sem identidade independente. Essa organização conceitual não exige uma tabela por conceito.

Na avaliação, as definições dos critérios utilizados são preservadas como **CritérioPreservado**, objeto de valor interno que não constitui uma nona entidade independente. As alternativas guardam identidade histórica de origem e ID, identificação preservada do produto e seus valores associados aos critérios preservados. A identidade original é referência histórica e não exige que o produto ainda exista no catálogo atual (DEC-012, DEC-034). A definição atual do critério no catálogo é conceitualmente distinta de sua cópia preservada.

A edição em memória permanece separada da avaliação persistida até salvamento explícito, sem criar uma entidade persistida adicional (DEC-025). A decisão não define schema, tabelas, tipos concretos, migrações ou implementação.

**Consequências:** modelagem e arquitetura devem manter snapshots suficientes para consulta, cálculo e exportação offline, inclusive após remoções/atualizações do catálogo; atributos e persistência serão detalhados nas partes seguintes da Etapa 3.

## DEC-037 — Cardinalidades conceituais da modelagem

**Status:** aprovada pelo usuário em 03/11/2026 durante a revisão da Parte 3 da Etapa 3.
**Origem:** resolução conceitual das questões abertas de cardinalidade, sem definir schema ou estrutura física.

As duas categorias iniciais mantêm exatamente cinco critérios. A modelagem conceitual deve comportar `n` variável para futuras categorias, mas isso não adiciona categorias personalizadas à versão inicial nem promete suporte matemático irrestrito. Antes de habilitar outro `n`, devem ser verificados a validade dos métodos AHP/TOPSIS, o RI aplicável e casos especiais; sem RI/regra matemática adequada, o processamento é bloqueado. Não se fixam limites arbitrários sem justificativa.

Cada definição atual de Critério pertence logicamente a uma única Categoria. Categorias diferentes podem ter critérios semelhantes ou com o mesmo nome, mas suas definições e identidades não são compartilhadas. CritérioPreservado pertence exclusivamente à Avaliação que o copiou.

Rascunhos podem preservar pesos e indicadores AHP calculados mesmo quando `RC > 0,10`, identificados como diagnóstico inconsistente dos julgamentos atuais. Isso não aprova consistência e não autoriza TOPSIS. TOPSIS exige julgamentos completos, pesos/indicadores vigentes e `RC ≤ 0,10`. Alterar julgamentos invalida os indicadores AHP anteriores na edição. O salvamento continua manual: a versão persistida permanece intacta até confirmação explícita, conforme DEC-025.

**Consequências:** Partes 1–3 do documento de domínio refletem `n` variável conceitual, pertencimento exclusivo de critérios e persistência de diagnóstico AHP inconsistente em rascunhos. A decisão não define classes, tipos, tabelas, coleções, chaves, serialização ou migrações.

## DEC-038 — Estrutura Room híbrida de seis tabelas (P5-A)

**Status:** aprovada pelo usuário em 05/11/2026, com os limites abaixo.

**Contexto:** mapear o domínio aprovado para persistência local com integridade, snapshots independentes e salvamento manual, sem impor uma tabela para cada conceito.

**Alternativas avaliadas:** agregado inteiramente serializado (simplicidade, mas menor integridade relacional); modelo totalmente normalizado (mais restrições físicas, porém mais tabelas/junções); modelo híbrido (relações explícitas para componentes e conteúdo incorporado quando apropriado).

**Decisão:** adotar como base as seis tabelas `produto_pessoal`, `avaliacao`, `criterio_preservado`, `alternativa_avaliacao`, `valor_alternativa` e `julgamento_ahp`. Avaliação permanece raiz do agregado. PKs, FKs internas e unicidades preservam pertencimento e identidade histórica origem + ID, sem dependência obrigatória dos registros atuais dos catálogos. Definições copiadas dos critérios permanecem imutáveis. Criação/atualização por salvamento explícito e exclusão confirmada são transacionais. Rascunhos incompletos e conclusões são suportados, sem histórico de versões ou persistência automática da edição em memória.

**Unicidade AHP:** no máximo um julgamento independente por par não ordenado; pares ainda não respondidos podem faltar no rascunho. Normalizar IDs deterministicamente antes de gravar e ajustar a direção ao inverter a ordem, preservando a preferência. Rejeitar comparação consigo mesmo e critérios de outra avaliação. Preservar a PK composta por avaliação e par canônico. Prever defesa física adicional contra pares invertidos.

**Dependência técnica aberta:** verificar a viabilidade e a forma de declarar uma restrição CHECK de ordem canônica no schema gerenciado pelo Room, com ordenação compatível com o normalizador. Esta aprovação não resolve sua implementação. A defesa física deverá ser definida e validada antes da implementação definitiva; PK isolada não impede pares invertidos sem normalização.

**Limites:** não aprova P5-B (localização dos resultados), P5-C (Firestore), P5-D (tipos/IDs/dinheiro) ou P5-E (definições pessoais/compatibilidade offline). Campos e detalhes dessas propostas não são aprovados por aparecerem no modelo de seis tabelas. Tabelas adicionais exigem justificativa técnica e aprovação posterior.

**Consequências:** manter separação entre domínio, estruturas físicas e edição em memória; testar integridade do agregado, rollback e pares invertidos na implementação futura. Preserva DEC-025, DEC-034, DEC-036 e DEC-037. Não exige código, migrations ou configuração de banco nesta atividade.

**Rastreabilidade:** [Parte 5 — P5-A](../modelagem/05-persistencia-snapshots.md); RF11, RF14, RF21–RF26; RNF05, RNF09. Não altera os requisitos aprovados.

## 11. Pendências de especificação

Estas questões não alteram as decisões aprovadas; precisam ser detalhadas para requisitos, modelagem e testes:

- Selecionar modelos reais e registrar fontes e datas dos dados.
- Definir rubricas de câmeras e ADAS e os protocolos comparáveis de autonomia de smartphones.
- Definir perfil de referência do seguro e condições de medição da recarga rápida.
- Detalhar os domínios e limites numéricos ainda não definidos para cada critério e os metadados/schema de critério no Firestore, conforme DEC-033; tolerâncias matemáticas aprovadas nas DEC-027 e DEC-029.
- Detalhar contratos e estrutura Firestore/Room na modelagem/arquitetura. Casos de uso e RNF01–RNF13 já estão documentados; persistência transacional e perda de alterações não salvas seguem RNF05 e DEC-025. P01–P03 foram resolvidos pelas DEC-033–DEC-035.
- Confirmar com o professor, se necessário, as adaptações e a diferença metodológica do AHP.

## 12. Histórico

- **v1.14:** DEC-038 registra aprovação limitada da P5-A; defesa física AHP ainda requer validação e P5-B–P5-E permanecem pendentes.

- **v1.8:** DEC-032 aprovada: exportação local em PDF; revisão explícita da exclusão de relatórios da DEC-011. UC01–UC15 aceitos pelo usuário.
- **v1.9:** DEC-033 aprovada para P01: metadados genéricos de validação junto às definições remotas dos critérios; limites numéricos adicionais e schema permanecem para detalhamento.
- **v1.10:** DEC-034 aprovada para P02: identidade por origem e ID do registro; equivalência entre catálogos não é inferida automaticamente.
- **v1.11:** DEC-035 aprovada para P03: resultado válido sem diferenciação é concluído e listado com rótulo explícito.
- **v1.12:** DEC-036 aprovada para a organização conceitual da modelagem, incluindo distinção de CritérioPreservado, referências históricas independentes do catálogo e valores por critério nas alternativas.
- **v1.13:** DEC-037 aprovada para cardinalidades conceituais da Parte 3: critérios iniciais/futuros, exclusividade por categoria e diagnóstico AHP inconsistente em rascunhos.

Este documento consolida as decisões DEC-001 a DEC-038 aprovadas nas discussões da equipe até 05/11/2026. Novas decisões devem receber novo identificador e registrar status, contexto, decisão e consequências. Alterações em decisões existentes devem ser registradas, sem apagar silenciosamente o histórico.

## Validação dos requisitos não funcionais

RNF01–RNF13 validados. RNF05 consolida a integridade transacional e a preservação da última versão salva; RNF06 delimita as operações offline pela disponibilidade dos dados locais. Critérios completos em [requisitos não funcionais](../requisitos/README.md). RNF07 consolida leitura pública e bloqueio de escrita do catálogo oficial; RNF08 incorpora a DEC-030 sobre backup e transferência.

RNF09–RNF11 validados com critérios de separação de responsabilidades, cobertura mínima de testes e checklist documental. O detalhamento permanece no documento de RNFs, sem novos identificadores DEC.

RNF12–RNF13 validados com tags de entrega, histórico identificável, GitHub Projects e fluxo de colaboração aprovado na DEC-031.

RNF01 e RNF02 validados: protocolo de usabilidade com três participantes e matriz inicial de acessibilidade/responsividade aprovada. Todos os RNF01–RNF13 estão validados; os critérios completos ficam nos respectivos grupos de requisitos. Nenhum teste é declarado executado por essa validação documental.
