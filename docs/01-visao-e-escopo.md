# Documento de Visão e Escopo do Projeto

**Projeto:** Sistema de Avaliação Comparativa Multicritério de Produtos (AHP-TOPSIS)
**Disciplina:** FACOM32503 — Programação para Dispositivos Móveis
**Instituição:** Universidade Federal de Uberlândia — UFU
**Semestre:** 2026/2
**Versão editorial:** 1.2
**Base aprovada:** Visão e Escopo v1.0
**Situação:** base aprovada acrescida da exportação PDF autorizada na DEC-032; decisões detalhadas no [Registro de Decisões](decisoes/registro-decisoes.md).

## 1. Contextualização e problema

A escolha de produtos envolve características que podem entrar em conflito, como preço, desempenho, autonomia e qualidade. Comparar apenas pelo menor preço ou pela maior quantidade de recursos nem sempre representa as necessidades de quem decide.

O projeto propõe um aplicativo Android de apoio à tomada de decisão multicritério. O processo combina AHP para derivar pesos de critérios a partir de comparações pareadas e avaliar a consistência dos julgamentos, e TOPSIS para ordenar alternativas pela proximidade às soluções ideais positiva e negativa.

## 2. Visão do produto

O aplicativo permitirá a indivíduos e organizações comparar produtos por critérios, realizar avaliações personalizadas e consultar rankings explicáveis. A primeira versão combinará um catálogo oficial remoto com um catálogo pessoal local. Os dados efetivamente utilizados em cada avaliação serão preservados para consulta posterior.

A especificação aprovada das escolhas de categorias, métricas, armazenamento, algoritmo e exclusões da primeira versão está centralizada no [Registro de Decisões do Projeto](decisoes/registro-decisoes.md).

## 3. Público-alvo

- Consumidores individuais que comparam produtos antes de uma aquisição.
- Profissionais e organizações que avaliam alternativas para decisões de compra ou investimento.

A primeira versão não oferece funções específicas de gestão corporativa nem colaboração entre usuários.

## 4. Escopo da primeira versão

A aplicação abrangerá duas categorias predefinidas — Smartphones Premium e Veículos Elétricos BEV — com critérios também predefinidos. O usuário poderá consultar o catálogo oficial, manter produtos pessoais, selecionar alternativas, personalizar valores por avaliação, configurar julgamentos AHP, processar TOPSIS e consultar os indicadores e o ranking.

As avaliações serão locais e poderão ser salvas como rascunhos, retomadas, consultadas, editadas e recalculadas. A avaliação exigirá no mínimo duas alternativas. A exclusão de produtos pessoais e avaliações exigirá confirmação. Detalhes e consequências dessas escolhas estão no [Registro de Decisões](decisoes/registro-decisoes.md); requisitos verificáveis estão em [Requisitos](requisitos/README.md).

Avaliações salvas e processadas, incluindo as sem diferenciação, poderão ser exportadas em PDF gerado localmente e compartilhado por ação explícita do usuário. Conteúdo e tratamento de alterações não salvas seguem a DEC-032 e o [RF27](requisitos/funcionais/07-exportacao.md).

## 5. Qualidade e rastreabilidade dos dados

Os dados do catálogo devem permitir identificar fontes e datas de referência, usar unidades e métodos comparáveis e sinalizar valores estimados ou simulados. Valores qualitativos precisam seguir rubricas documentadas. Atualizações de catálogo não devem alterar os dados preservados em avaliações anteriores.

A lista de critérios, unidades, fontes e adaptações aprovadas está no [Registro de Decisões](decisoes/registro-decisoes.md). As rubricas e fontes concretas ainda serão detalhadas.

## 6. Diretrizes gerais

O produto será desenvolvido para Android, em Kotlin, com Jetpack Compose e arquitetura MVVM. O catálogo oficial será remoto; produtos pessoais e avaliações serão armazenados localmente. Os cálculos devem ser independentes de serviços externos e do tipo específico de produto, permitindo evolução futura sem ampliar automaticamente o escopo da primeira versão.

A metodologia matemática aprovada, incluindo a referência escolhida e a diferença entre os PDFs do professor, está registrada na [DEC-018](decisoes/registro-decisoes.md#4-dec-018--metodologia-matemática-ahp-topsis). Os detalhes arquiteturais serão especificados em [Arquitetura](arquitetura/arquitetura.md).

## 7. Fora do escopo

A primeira versão não inclui autenticação ou contas, sincronização dos dados pessoais entre dispositivos, criação de categorias ou critérios pelo usuário, colaboração, histórico de versões de avaliações, integração com lojas para compra/preço/estoque em tempo real ou importação de avaliações. Exportação de rascunhos e de matrizes intermediárias também fica fora desta versão; o relatório PDF aprovado é delimitado pela DEC-032.

## 8. Limites e condições de uso

- O catálogo remoto pode exigir conexão na primeira consulta; offline depende dos dados já armazenados em cache.
- A qualidade dos resultados depende da validade e comparabilidade dos valores informados.
- As notas de câmeras e ADAS são avaliações baseadas em rubricas da equipe, não certificações oficiais.
- Preços, seguro e outros valores sujeitos a variação devem ter fonte e data/perfil de referência documentados.

## 9. Resultado esperado desta etapa

A Visão e Escopo delimita o problema, o público, as capacidades e os limites do produto. Requisitos, casos de uso e critérios de aceitação evoluem em [Requisitos](requisitos/README.md); modelo, arquitetura, navegação e protótipo serão registrados nos documentos próprios após detalhamento.

## 10. Histórico editorial

- **v1.2:** inclusão da exportação PDF conforme DEC-032, revisando a exclusão anterior de relatórios.

- **v1.0:** escopo preliminar consolidado a partir das decisões da equipe.
- **v1.1:** reorganização editorial para manter o registro DEC-001 a DEC-019 em um documento central; as decisões aprovadas permanecem inalteradas.
