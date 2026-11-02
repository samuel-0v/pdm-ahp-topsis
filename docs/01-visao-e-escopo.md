# Documento de Visão e Escopo do Projeto

**Projeto:** Sistema de Avaliação Comparativa Multicritério de Produtos (AHP-TOPSIS)  
**Disciplina:** FACOM32503 — Programação para Dispositivos Móveis  
**Instituição:** Universidade Federal de Uberlândia — UFU  
**Semestre:** 2026/2  
**Versão:** 1.0  
**Situação:** Escopo preliminar consolidado a partir das decisões da equipe

---

## 1. Contextualização

A escolha de produtos envolve frequentemente a análise simultânea de características que podem entrar em conflito, como preço, desempenho, autonomia e qualidade.

Uma comparação baseada exclusivamente no menor preço ou na maior quantidade de recursos pode não refletir as necessidades do usuário.

O projeto propõe o desenvolvimento de um aplicativo Android que utilize métodos de Análise de Decisão Multicritério (MCDA) para auxiliar indivíduos e organizações na seleção de produtos.

Serão utilizados dois métodos complementares:

- **AHP (Analytic Hierarchy Process):** determinação dos pesos dos critérios por comparações pareadas, com validação da consistência dos julgamentos.
- **TOPSIS (Technique for Order of Preference by Similarity to Ideal Solution):** classificação das alternativas a partir de sua proximidade relativa às soluções ideais positiva e negativa.

## 2. Problema

Consumidores e organizações enfrentam dificuldades para comparar produtos com diferentes características e estabelecer a importância relativa de cada atributo.

As comparações tradicionais podem apresentar subjetividade excessiva, critérios inconsistentes e pouca transparência na justificativa das decisões.

O aplicativo pretende oferecer um processo estruturado, reproduzível e compreensível para apoiar essas escolhas.

## 3. Visão do Produto

Desenvolver um aplicativo Android de apoio à tomada de decisão multicritério que permita a indivíduos e organizações selecionar e comparar produtos, atribuir importância aos critérios pelo método AHP e obter rankings pelo método TOPSIS.

A solução disponibilizará categorias e critérios predefinidos, permitirá o cadastro e o gerenciamento de produtos pessoais e oferecerá avaliações personalizadas com armazenamento local.

O sistema deverá apresentar resultados transparentes, preservar os dados utilizados nas avaliações e utilizar uma arquitetura extensível para permitir futuras funcionalidades de personalização.

## 4. Público-alvo

O aplicativo destina-se a:

- Consumidores individuais que desejam comparar produtos antes de uma aquisição.
- Profissionais e organizações que necessitam avaliar alternativas para decisões de compra ou investimento.

A primeira versão não possuirá recursos específicos de gestão corporativa ou colaboração entre usuários.

## 5. Escopo da Primeira Versão

### 5.1. Catálogo de produtos

O aplicativo disponibilizará inicialmente duas categorias:

1. Smartphones Premium.
2. Veículos Elétricos, exclusivamente do tipo BEV.

As categorias e os critérios serão definidos previamente pela equipe.

Os dados oficiais serão armazenados remotamente no Firebase Cloud Firestore.

O aplicativo deverá consultar automaticamente os dados remotos e utilizar cache offline quando disponível.

### 5.2. Catálogo pessoal

O usuário poderá cadastrar, consultar, editar e excluir produtos pessoais pertencentes às categorias existentes.

Os produtos pessoais serão armazenados localmente e poderão ser reutilizados em diferentes avaliações.

As operações de edição e exclusão do catálogo pessoal não deverão modificar os valores previamente registrados em avaliações existentes.

A exclusão de produtos deverá solicitar confirmação.

### 5.3. Seleção e personalização

O usuário poderá selecionar quais produtos deseja incluir em uma avaliação, combinando produtos oficiais e produtos pessoais.

Cada avaliação deverá conter no mínimo duas alternativas.

Os valores dos critérios poderão ser ajustados para uma avaliação específica, preservando as informações originais dos catálogos.

### 5.4. Gerenciamento de avaliações

O aplicativo permitirá:

- Criar avaliações.
- Salvar avaliações incompletas como rascunhos.
- Retomar avaliações anteriores.
- Consultar avaliações concluídas.
- Editar avaliações.
- Recalcular resultados após alterações.

Não haverá histórico de versões dos resultados. Um novo processamento substituirá o resultado anterior daquela avaliação.

Os dados de cada avaliação serão armazenados localmente, de maneira independente dos valores atuais dos catálogos.

### 5.5. Avaliação multicritério — AHP

O aplicativo permitirá a comparação pareada dos critérios utilizando a escala de Saaty.

O processamento deverá determinar os pesos dos critérios e calcular os indicadores de consistência, incluindo autovalor máximo, Índice de Consistência e Razão de Consistência.

Uma avaliação somente poderá avançar para o processamento TOPSIS quando apresentar Razão de Consistência menor ou igual a 0,10.

Quando a matriz for inconsistente, o aplicativo deverá informar o problema e permitir a revisão dos julgamentos.

### 5.6. Classificação — TOPSIS

O aplicativo deverá utilizar os pesos obtidos pelo AHP e os valores dos produtos selecionados para:

- Normalizar a matriz de decisão.
- Aplicar os pesos dos critérios.
- Identificar as soluções ideais positiva e negativa.
- Calcular as distâncias euclidianas.
- Determinar o coeficiente de proximidade relativa de cada produto.
- Ordenar as alternativas em classificação decrescente.

### 5.7. Apresentação dos resultados

O aplicativo deverá apresentar o ranking dos produtos, destacando a alternativa mais bem classificada.

Serão exibidos os coeficientes de proximidade, as distâncias às soluções ideais e os pesos dos critérios, permitindo compreender os fundamentos da classificação.

A organização visual e a navegação serão definidas pela equipe, sem obrigatoriedade de reproduzir integralmente o protótipo de referência fornecido pelo professor.

## 6. Categorias e Critérios

### 6.1. Smartphones Premium

| Critério | Natureza | Unidade |
|---|---|---|
| Desempenho | Benefício | Geekbench 6 Multi-Core, pontos |
| Preço de venda | Custo | R$ |
| Qualidade das câmeras | Benefício | Nota de 1 a 10 |
| Autonomia da bateria | Benefício | Horas |
| Armazenamento interno | Benefício | GB |

A autonomia deverá ser determinada a partir de testes padronizados, realizados sob condições comparáveis.

A qualidade das câmeras será representada por uma nota baseada em rubrica documentada, considerando aspectos relevantes da capacidade fotográfica e de gravação.

### 6.2. Veículos Elétricos

| Critério | Natureza | Unidade |
|---|---|---|
| Autonomia elétrica | Benefício | km — Inmetro/PBEV |
| Preço de aquisição | Custo | R$ |
| Tempo de recarga rápida | Custo | Minutos, 20% a 80% |
| Custo do seguro anual | Custo | R$/ano |
| Pacote de segurança ADAS | Benefício | Nota de 1 a 10 |

A categoria será limitada a veículos totalmente elétricos (BEV).

A autonomia utilizará os valores de referência do padrão brasileiro Inmetro/PBEV.

O custo do seguro deverá considerar um perfil padronizado, com condições de referência documentadas.

A pontuação dos recursos ADAS será baseada em uma rubrica definida pela equipe. Essa pontuação não representará uma certificação oficial de segurança.

## 7. Política de Qualidade dos Dados

Os valores disponibilizados no catálogo deverão seguir princípios de:

**Rastreabilidade:** registrar fontes e datas de referência.

**Comparabilidade:** utilizar unidades e metodologias equivalentes para produtos de uma mesma categoria.

**Validação:** impedir valores inválidos e assegurar que notas qualitativas estejam no intervalo de 1 a 10.

**Transparência:** identificar quando valores forem estimados ou simulados.

**Preservação:** manter em cada avaliação os valores efetivamente utilizados no cálculo.

Os dados poderão ser atualizados pela equipe no catálogo remoto, sem modificar automaticamente avaliações previamente registradas.

## 8. Diretrizes Técnicas e Arquiteturais

A aplicação será desenvolvida em Kotlin para Android, utilizando Jetpack Compose e arquitetura MVVM.

A organização preliminar será dividida em quatro camadas:

| Camada | Responsabilidade |
|---|---|
| Apresentação | Interfaces, navegação e interação com o usuário |
| Estado e apresentação lógica | ViewModels, estado reativo e coordenação das operações |
| Domínio | Modelos e motores de cálculo AHP e TOPSIS |
| Dados | Repositórios, Firebase Firestore e Room |

O Firebase Cloud Firestore será responsável pelo catálogo remoto oficial.

O Room será utilizado para persistência local dos produtos pessoais e das avaliações.

A modelagem do domínio deverá representar categorias, critérios, produtos e valores de forma genérica, evitando vincular os motores matemáticos exclusivamente às duas categorias iniciais ou a uma quantidade fixa de produtos.

## 9. Funcionalidades Fora do Escopo

Não serão implementados na primeira versão:

- Autenticação e contas de usuários.
- Sincronização dos dados pessoais entre dispositivos.
- Criação e gerenciamento de categorias e critérios personalizados.
- Histórico de versões das avaliações.
- Avaliações colaborativas e gerenciamento de equipes.
- Integração comercial para consulta de preços, estoques e compras em tempo real.
- Exportação de relatórios para PDF, Excel ou outros arquivos.

Essas funcionalidades poderão ser consideradas em evoluções futuras.

## 10. Premissas e Restrições

- O aplicativo será desenvolvido para Android.
- A primeira versão trabalhará com duas categorias predefinidas.
- O catálogo oficial será disponibilizado remotamente.
- Os dados pessoais serão armazenados no dispositivo.
- O aplicativo poderá apresentar dados previamente consultados quando estiver offline, respeitando as limitações do cache.
- A primeira consulta ao catálogo poderá exigir conexão com a internet.
- Os cálculos AHP e TOPSIS deverão funcionar independentemente de serviços externos.
- A equipe deverá utilizar controle de versão e documentar as decisões técnicas.
- O protótipo apresentado pelo professor será utilizado como referência, não como implementação visual obrigatória.

## 11. Registro das Decisões

| ID | Decisão aprovada |
|---|---|
| DEC-001 | Modelo híbrido e arquitetura extensível |
| DEC-002 | Público-alvo individual e organizacional |
| DEC-003 | Valores ajustáveis por avaliação |
| DEC-004 | Persistência local no dispositivo |
| DEC-005 | Seleção personalizada, mínimo de dois produtos |
| DEC-006 | Rascunhos, retomada, edição e recálculo |
| DEC-007 | Cadastro automático no catálogo pessoal |
| DEC-008 | Smartphones Premium e Veículos Elétricos |
| DEC-009 | Catálogo remoto no Firebase Cloud Firestore |
| DEC-010 | Atualização automática com cache offline |
| DEC-011 | Exclusões da primeira versão |
| DEC-012 | Cadastro, consulta, edição e exclusão dos produtos pessoais |
| DEC-013 | Autonomia dos smartphones medida em horas |
| DEC-014 | Rubricas padronizadas para câmeras e ADAS |
| DEC-015 | Veículos exclusivamente 100% elétricos (BEV) |
| DEC-016 | Autonomia veicular segundo Inmetro/PBEV |
| DEC-017 | Padronização das métricas e política de qualidade dos dados |

## 12. Pontos a Detalhar nas Próximas Etapas

As decisões de escopo estão consolidadas. Permanecem para especificação posterior:

- Requisitos funcionais e não funcionais verificáveis.
- Casos de uso, cenários e fluxos alternativos.
- Modelo de entidades, relacionamentos e persistência.
- Contratos dos repositórios e estrutura do Firestore.
- Rubricas detalhadas de câmeras e ADAS.
- Seleção dos produtos reais e levantamento de suas fontes.
- Política de salvamento e tratamento de alterações dos rascunhos.
- Tratamento de valores ausentes, empates e entradas numéricas inválidas.
- Definição precisa do algoritmo de obtenção dos pesos AHP, considerando as duas abordagens presentes nos materiais do professor.
- Diagrama de arquitetura e navegação.
- Protótipo de interfaces da equipe.
- Estratégia de testes e critérios de aceite.

## 13. Conclusão

O escopo aprovado estabelece uma aplicação Android de apoio à decisão multicritério, com categorias predefinidas, cadastro pessoal de produtos, avaliações persistentes e cálculo integrado AHP-TOPSIS.

A proposta busca equilibrar funcionalidade, qualidade técnica e viabilidade acadêmica.

A arquitetura será concebida para permitir futuras ampliações, especialmente a criação de categorias e critérios personalizados, sem que essas funcionalidades precisem ser implementadas na versão inicial.

**Status do documento:** versão preliminar consolidada, sujeita à revisão pela equipe e às orientações do professor.