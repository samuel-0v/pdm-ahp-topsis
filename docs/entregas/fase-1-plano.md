# Fase 1 — Plano de conclusão de Requisitos e Modelagem

**Versão:** 0.1 — 06/11/2026. **Situação:** proposta para aprovação e distribuição pela equipe.
**Prazo operacional provisório:** 10/11/2026, conforme informação de adiamento fornecida pelo usuário; confirmação no Teams ou pelo professor pendente. Horário oficial ainda desconhecido.
**Base auditada:** `main`, commit `0b072ca4af4cc4098487fe75d756017ab8b575ef`, com diretório de trabalho limpo antes desta atividade.

[Índice da documentação](../README.md) · [Tarefas propostas](fase-1-tarefas.md) · [Mensagem para o grupo](fase-1-mensagem-equipe.md)

Este plano organiza a primeira entrega. Não altera requisitos, decisões ou Partes 1–5 da modelagem. Nenhuma issue, branch, PR, commit, protótipo ou submissão foi criado por esta atividade. As tarefas e os papéis abaixo são sugestões; nomes e atribuições individuais dependem de confirmação.

## 1. Referências e separação das fases

### Exigências e orientações do professor

A numeração abaixo é a página física do [enunciado ProjetoDisciplina_PDM20262v2.pdf](../../references/ProjetoDisciplina_PDM20262v2.pdf), de 12 páginas, conferido integralmente por extração textual nesta auditoria.

| Referência | Conteúdo que orienta a entrega |
|---|---|
| Páginas 4–6 — escopo, categorias e requisitos | Telas/fluxo do comparador, pelo menos duas coleções e características dos métodos. A escolha aprovada da equipe é Smartphones Premium e Veículos Elétricos BEV, não todas as categorias do exemplo. |
| Página 7 — cronograma e fases | Fase 1 em 03/11/2026: requisitos funcionais/não funcionais, arquitetura MVVM/dados/UI, layouts e navegação; link do vídeo e documento com requisitos, diagramas de arquitetura e protótipos no Teams. |
| Página 7 — Fase 2 | Até 11/12/2026: telas e navegação implementadas, integração remota, testes funcionais/usabilidade, versão final e demonstração prática do aplicativo. |
| Página 7 — organização da equipe | Papéis de líder, desenvolvimento Android, engenharia de dados e Design/UX. Há outra composição com engenharia de PO na mesma página; confirmar composição real, sem deduzir nomes ou quantidade de participantes. |
| Página 9 — roteiro sugerido | Introdução pelo líder; requisitos e obtenção remota dos dados por engenharia de dados; arquitetura/navegação por desenvolvimento Android; design/protótipos por UX; encerramento pelo líder. É uma sequência sugerida. |
| Página 11 — prototipagem | Figma, identidade visual e navegação interativa. O link de exemplo fornecido pelo professor não comprova protótipo produzido pela equipe. |
| Página 12 — arquitetura | UI Compose/Material 3, ViewModel com StateFlow, motores AHP/TOPSIS e dados/repositório, com separação de responsabilidades. |

O PDF apresenta inconsistências editoriais: página 4 menciona equipe de oito; página 7 fala em até seis e traz duas composições; página 10 contém um cronograma ilustrativo intitulado “Álbum de Figurinhas”, com apresentação em “08/07”. Para este plano, prevalecem como referência de fases as páginas 7 e 9. Confirmar dúvidas com o professor; não transformar a tabela ilustrativa em novo prazo oficial.

### Delimitação desta atividade

| Fase 1 — entregar planejamento e evidências visuais | Fase 2 — executar o aplicativo e demonstrar resultados |
|---|---|
| Requisitos, modelo aprovado, arquitetura e responsabilidades dos componentes | Projeto Android executável, telas Compose e navegação real |
| Diagramas de arquitetura, fluxo de dados e navegação | Integração real de ViewModels, motores, Room e Firestore |
| Layouts e protótipos interativos no Figma | Persistência, regras de segurança, cache e exportação funcionando |
| Explicação de como os dados serão obtidos e preservados | Publicação dos dados reais com fontes/limites e validações suficientes |
| Documento/pacote acessível, vídeo e envio ao Teams | Testes automatizados, integração, desempenho, usabilidade, apresentação prática final |

APK, aplicativo completo, testes de integração executados e catálogo remoto populado **não são critérios obrigatórios deste plano de Fase 1**. Não foi encontrada orientação nova que antecipe esses itens. Existe um arquivo Kotlin inicial de cálculos em `app/`, mas ele não comprova aplicativo integrado nem muda o escopo da primeira entrega.

São recomendações internas: datas intermediárias, prioridades, organização das issues, arquivos de apoio, exportações legíveis e duração estimada do vídeo. StateFlow e os papéis se apoiam no enunciado; detalhes do checklist de estados se apoiam nos requisitos aprovados. Não há novo requisito de produto.

## 2. Checklist oficial da primeira entrega

Checklist derivado da página 7, com o roteiro da página 9 e prototipagem da página 11. Caixas abertas são itens ainda não comprovados como entrega completa; não significam que todo o conteúdo precise ser refeito.

- [x] Requisitos funcionais e não funcionais definidos: RF01–RF27 e RNF01–RNF13 localizados e conferidos.
- [ ] Documento de requisitos organizado e legível no material que será inserido no Teams; conferir formato aceito na atividade.
- [ ] Arquitetura do app modelada: MVVM, UI, estado, domínio/motores e dados/repositórios.
- [ ] Diagramas de arquitetura legíveis no pacote da entrega.
- [ ] Layouts e navegação especificados; diagrama de navegação completo para a apresentação.
- [ ] Protótipos autorais de tela no Figma, com fluxo demonstrável e evidências acessíveis.
- [ ] Link do vídeo descrevendo a etapa de desenvolvimento, com acesso testado para o professor.
- [ ] Documento(s) e materiais inseridos na atividade correta do Teams, com comprovante do envio.

As Etapas internas 1–3 concluídas não encerram toda a Fase 1 do professor. Faltam principalmente Arquitetura, Interfaces/Protótipos e Consolidação da entrega.

## 3. Diagnóstico documental e evidências

Retrato do repositório antes da criação deste plano. “Concluído e verificado” significa existência e conferência documental, não teste do aplicativo, qualidade visual comprovada ou aceitação pelo professor. “Não iniciado” limita-se ao material local encontrado; trabalhos externos podem existir e devem ser informados.

| Entregável/controle | Classificação | Evidência e limitação | Ação |
|---|---|---|---|
| Visão e escopo | Concluído e verificado | [Visão](../01-visao-e-escopo.md) delimita duas categorias, capacidades e exclusões; [registro](../decisoes/registro-decisoes.md) contém DEC-001–DEC-042. | Reutilizar na introdução; não reabrir decisões. |
| Requisitos e casos de uso | Concluído e verificado | [Índice](../requisitos/README.md), sete grupos funcionais, seis não funcionais e cinco grupos de UCs: contados 27 RFs, 13 RNFs e 16 UCs, sem duplicidade. | Selecionar o conteúdo para a apresentação. |
| Rastreabilidade e plano de verificação | Concluído e verificado | [Matriz](../requisitos/rastreabilidade.md) e [plano](../requisitos/plano-verificacao.md) existentes; o plano identifica os testes como futuros. | Manter a distinção entre planejar testes e executá-los. |
| Modelagem do domínio e persistência | Concluído e verificado | [Partes 1–5](../modelagem/dominio.md), [contratos](../modelagem/05-contratos-persistencia.md) e [fechamento](../modelagem/fechamento-etapa-3.md); DEC-036–DEC-042 formalizadas. | Selecionar diagramas úteis e citar dependências abertas. |
| Diagramas do domínio | Existente, mas precisa de revisão | Sete blocos Mermaid encontrados: entidades (1), atributos/invalidação (1), cardinalidades (1), estados (2), estrutura Room (1) e navegação (1). Fontes existem; seis são de modelagem e um de navegação. Não são sete diagramas de arquitetura. | Conferir renderização/legibilidade no material de apresentação; não alterar regras. |
| Arquitetura MVVM detalhada | Não iniciado | [Arquitetura](../arquitetura/arquitetura.md) declara elaboração pendente e lista questões técnicas; diretrizes no RNF09 não equivalem a uma arquitetura entregue. | F1-03. |
| Diagramas de arquitetura/fluxo entre camadas | Não iniciado | Inventário de arquivos e blocos Mermaid não encontrou diagrama em camadas do projeto. Diagramas do domínio não cobrem esse entregável. | F1-04. |
| Navegação | Parcial | [Fluxo Mermaid e regras](../interface/navegacao.md) cobrem o núcleo da avaliação; exportação aparece em texto. Falta mapa de telas completo, incluindo catálogos pessoais e estados de falha. | F1-05/F1-06. |
| Layouts de tela e especificação visual | Não iniciado | Não foram localizados arquivos de layouts, capturas autorais ou documento de especificação de telas; apenas o fluxo funcional. | F1-05/F1-07. |
| Protótipos no Figma | Não foi possível verificar | Não há link autoral ou evidência local da equipe. O link no PDF é referência do professor. Nenhum arquivo Figma da equipe foi aberto ou inspecionado. | Solicitar link existente; validar ou produzir em F1-07/F1-08. |
| Planejamento da integração de dados | Existente, mas precisa de revisão | Contratos de [Firestore](../modelagem/contratos/04-firestore.md), [Room](../modelagem/contratos/01-estrutura-fisica.md) e [offline/snapshots](../modelagem/contratos/05-compatibilidade-offline.md) aprovados. Falta síntese própria para a exposição. | F1-09; sem exigir base populada. |
| Pacote final de documento(s) para Teams | Parcial | Conteúdo Markdown está distribuído e indexado; não foi encontrado pacote final exportado, manifesto de entrega ou conferência de acesso do professor. | F1-02/F1-11. |
| Roteiro autoral do vídeo | Não iniciado | Nenhum roteiro próprio encontrado antes deste plano. O roteiro de referência está no PDF, página 9. | A seção 7 fornece uma base; finalizar em F1-12. |
| Vídeo gravado, link e permissões | Não foi possível verificar | Nenhum vídeo ou link autoral localizado. A existência em armazenamento externo e o acesso do professor não foram verificados. | F1-13. |
| Papéis, responsáveis reais e GitHub Projects | Não foi possível verificar | DEC-031/RNF12–RNF13 definem o processo. Histórico Git existe, mas não identifica por si só a divisão atual da equipe; nenhum quadro externo foi consultado. | F1-01/F1-10. |
| Prazo e envio no Teams | Não foi possível verificar | PDF mantém 03/11; usuário informa possível 10/11. Nenhum aviso do Teams, horário da atividade ou recibo de submissão foi consultado. | F1-01/F1-14. |

Nenhum protótipo, vídeo ou envio foi considerado concluído por inferência. A criação deste plano fornece propostas e roteiro-base, não muda automaticamente a classificação das entregas acima.

## 4. Divisão sugerida e issues para aprovação

Todas as linhas abaixo são **propostas**, inicialmente “A fazer” após aprovação. O catálogo completo de issues em [Tarefas da Fase 1](fase-1-tarefas.md) contém objetivo, referências, artefato, critérios de conclusão, dependências e prazos para cada tarefa. Nenhuma foi criada no GitHub.

| ID | Título proposto da issue | Papel principal sugerido | Prioridade | Meta interna provisória |
|---|---|---|---|---|
| F1-01 | Confirmar prazo, formato de envio e composição da equipe | Líder do projeto | P0 | 07/11, início do trabalho |
| F1-02 | Consolidar os requisitos aprovados para a primeira entrega | Engenharia de dados; revisão do líder | P0 | Rascunho 07/11; versão final 09/11 |
| F1-03 | Documentar arquitetura MVVM e responsabilidades das camadas | Desenvolvimento Android/Kotlin | P0 | 07/11 |
| F1-04 | Produzir diagramas de arquitetura e fluxo de dados | Desenvolvimento Android/Kotlin; apoio de dados | P0 | 08/11, manhã |
| F1-05 | Mapear telas e estados aos requisitos aprovados | Design/UX; revisão de Android | P0 | 07/11 |
| F1-06 | Completar e conferir o diagrama de navegação | Design/UX + Android | P0 | 08/11, manhã |
| F1-07 | Produzir layouts e protótipo Figma dos fluxos principais | Design/UX | P0 | 08/11 |
| F1-08 | Completar estados alternativos e conferir acesso ao protótipo | Design/UX; apoio de Android | P0 | 09/11, manhã |
| F1-09 | Preparar explicação da integração e preservação dos dados | Engenharia de dados | P0 | 07/11 |
| F1-10 | Registrar responsabilidades e andamento da equipe | Líder; atualização por cada responsável | P1 | 07/11; atualização diária |
| F1-11 | Revisar e consolidar o pacote documental da Fase 1 | Líder; revisão cruzada dos papéis | P0 | 09/11, antes da gravação final |
| F1-12 | Finalizar roteiro e divisão das falas do vídeo | Líder + todos os papéis | P0 | Estrutura 07/11; final 09/11 |
| F1-13 | Gravar, publicar e verificar o vídeo da Fase 1 | Líder coordena; equipe apresenta | P0 | 09/11, após conferência do material |
| F1-14 | Conferir e submeter os materiais na atividade do Teams | Líder ou integrante confirmado como responsável pelo envio | P0 | 10/11, antes do horário oficial confirmado |

P0 indica necessário para fechar a entrega ou remover bloqueio; P1 indica apoio de organização, que deve ocorrer em paralelo e não atrasar os artefatos exigidos. Não há compromisso nominal ou quantidade de pessoas assumida. O mesmo integrante pode acumular papéis conforme disponibilidade confirmada.

### Organização futura no GitHub, conforme DEC-031

Após aprovação, criar as issues com os títulos e critérios do catálogo; adicionar ao Projects e atribuir **uma pessoa confirmada** como responsável principal por issue, listando colaboradores. Usar os estados **A fazer**, **Em andamento** e **Concluído**; tarefa bloqueada permanece no estado correspondente com motivo e dependência registrados.

Sugestões de branches/PRs, ainda não criados: `docs/fase1-requisitos-entrega` (F1-02), `docs/etapa4-arquitetura` (F1-03/F1-04), `docs/etapa5-interface-navegacao` (F1-05–F1-08), `docs/fase1-integracao-planejada` (F1-09) e `docs/fase1-consolidacao` (F1-10–F1-14). Arquivos de apoio do Figma e links precisam entrar no PR de interface; vídeo e recibo entram como evidência da consolidação. Pequenas correções podem seguir a exceção da DEC-031. Revisão por outro integrante quando disponível; não esperar uma branch de implementação Android para concluir a Fase 1.

## 5. Dependências, paralelismo e cronograma

Todo o calendário é **sugestão interna condicionada ao prazo de 10/11/2026**. A confirmação de data/horário em F1-01 é urgente porque 03/11 já passou. Enquanto a confirmação é buscada, avançar os artefatos independentes; silêncio não confirma adiamento. Se houver outro prazo, o líder replaneja e comunica, preservando as exigências oficiais.

| Data | Trabalhos e pontos de controle | Dependências |
|---|---|---|
| 06/11 | Analisar este plano, informar trabalhos externos existentes e indicar integrantes disponíveis por papel. | Aprovação/distribuição ainda necessárias. |
| 07/11 | Confirmar prazo/formato; em paralelo, rascunho dos requisitos, arquitetura, mapa de telas, síntese de dados e estrutura das falas. | Base aprovada das Etapas 1–3 já disponível; decisões novas de comportamento não devem ser presumidas. |
| 08/11 | Fechar diagramas de arquitetura/navegação; concluir primeira versão navegável no Figma. | F1-03 alimenta F1-04; F1-05 alimenta F1-06/F1-07. |
| 09/11, manhã | Completar estados alternativos; conferir requisitos, telas, diagramas, links e permissões. | F1-02/F1-04/F1-06/F1-08/F1-09 alimentam F1-11. |
| 09/11, restante do dia | Fechar roteiro, ensaiar, gravar, revisar vídeo e disponibilizar link. | F1-11 e F1-12 antes da versão final de F1-13. |
| 10/11 | Reserva para problemas de acesso/arquivo e submissão, **se a data estiver confirmada**. Guardar comprovante. | F1-11/F1-13 completos; F1-01 precisa ter definido prazo, horário e atividade. |

Caminho crítico: mapa de telas → navegação/protótipo → conferência integrada → gravação/link → envio. Arquitetura → diagramas também precisa convergir antes da gravação. Engenharia de dados pode preparar requisitos e explicação da integração enquanto Android e UX avançam seus artefatos.

Paralelismo depende de pessoas disponíveis: se houver apenas um responsável de UX, F1-07 e F1-08 são sequenciais ou exigem apoio confirmado de outro integrante. Não contar duas vezes a capacidade de uma pessoa em tarefas simultâneas. O roteiro pode ser esboçado cedo; a gravação final usa os artefatos conferidos.

## 6. Critérios de aceite da Fase 1

### Exigências do professor

Requisitos apresentados, arquitetura e diagramas, layouts/navegação, protótipos e link de vídeo devem estar acessíveis no material inserido no Teams. O conteúdo precisa demonstrar planejamento e estrutura do projeto, conforme páginas 7, 9, 11 e 12. Não substituir evidência visual de protótipo por descrição textual.

### Conferências internas recomendadas

- Os documentos apontam para a mesma versão aprovada e mantêm as duas categorias iniciais; não acrescentam autenticação, novas categorias ou outras funções fora do escopo.
- Arquitetura identifica UI/Compose → ações ao ViewModel; estados retornam via StateFlow; domínio/motores ficam isolados; repositórios encapsulam Room e Firestore. Explicar o fluxo de salvar, recuperar e calcular sem prometer implementação.
- A tabela de telas cobre UC01–UC16 por tela, diálogo ou estado. Não é necessário criar dezesseis telas nem fixar o número oito do exemplo do professor.
- O protótipo demonstra consulta/seleção, configuração, AHP, resumo, resultado, rascunho e saída com alterações. Há variantes acessíveis para AHP inconsistente, empate, ausência de diferenciação, carregamento, erro e offline; cadastro pessoal, exclusão e exportação aparecem na cobertura, sem backend real.
- Fonte, unidade, valor simulado e resultado ilustrativo são identificados. Não apresentar números mockados como cálculo executado, produto oficial validado ou teste aprovado.
- Acessibilidade é considerada no design: contraste, texto além de cor, rótulos, alvos e indicação de comportamento com fontes ampliadas. Protótipo Figma não comprova teste TalkBack ou responsividade do app executável.
- Diagramas renderizados e telas são legíveis durante a gravação; links abrem com as permissões que serão usadas pelo professor.
- Há registro de responsáveis confirmados, artefatos entregues, pendências e progresso. Testes da aplicação permanecem explicitamente planejados para a Fase 2.

## 7. Plano do vídeo

Sequência sugerida pelo professor na página 9. Os tempos abaixo são **metas internas de montagem**, aproximadamente 8–10 minutos no total, não duração oficial. Confirmar eventual limite em F1-01. Nomes de apresentadores ainda não atribuídos.

| Bloco | Papel sugerido | O que mostrar/falar | Tempo interno aproximado |
|---|---|---|---|
| Introdução | Líder | Problema, propósito do comparador e duas categorias; explicar que esta apresentação cobre planejamento e modelagem. | 45–60 s |
| Requisitos e dados | Engenharia de dados | RF/RNF por grupos e exemplos; origem oficial no Firestore, dados pessoais no Room e preservação histórica. | 90–120 s |
| Modelagem e arquitetura | Desenvolvimento Android/Kotlin | Modelo aprovado selecionado, diagrama MVVM/fluxo de dados, responsabilidades, StateFlow e navegação. | 120–150 s |
| Design e protótipos | Design/UX | Percorrer o protótipo; justificar escolhas visuais; mostrar fluxo principal e variantes críticas sem fingir app funcional. | 180–210 s |
| Encerramento | Líder | O que foi concluído, pendências e próximo passo; implementação/testes e demonstração funcional da Fase 2 em 11/12. | 45–60 s |

F1-12 deve transformar essa base em roteiro com falas, telas de apoio e responsáveis confirmados. Antes de gravar: testar captura, áudio, zoom dos diagramas e funcionamento do protótipo. F1-13 deve conferir o vídeo do início ao fim, sincronizar as versões dos artefatos, publicar o link e testar acesso em sessão equivalente à do avaliador. Publicar não significa submeter no Teams.

## 8. Checklist de submissão no Teams

- [ ] Localizar a atividade correta e registrar prazo, horário e formato permitidos a partir do aviso oficial; guardar referência do adiamento quando confirmado.
- [ ] Conferir nomes/matrículas exigidos pela atividade com a equipe, sem inventar dados ou expor informação pessoal desnecessária.
- [ ] Anexar o documento de requisitos e os diagramas de arquitetura conforme a atividade permitir. Como organização interna, preferir um documento de entrada com índice e materiais legíveis; PDF é sugestão, não formato imposto pelo PDF do professor.
- [ ] Disponibilizar protótipos de tela e link Figma com permissões corretas; incluir capturas/exportações como apoio, sem confundi-las com protótipo interativo.
- [ ] Inserir o link do vídeo e testar reprodução/áudio no destino final.
- [ ] Conferir se links complementares do repositório e materiais apontam para a versão apresentada; não depender de acesso a arquivos locais de um integrante.
- [ ] Revisar presença dos itens oficiais da seção 2 e dos responsáveis definidos em F1-10.
- [ ] Executar a ação de envio/conclusão exigida pelo Teams, além de carregar arquivos.
- [ ] Registrar comprovante, data/hora, atividade e responsável. Marcar como “submetido” somente depois dessa evidência.
- [ ] Comunicar no grupo o envio e o local do comprovante. Identificar a versão efetivamente entregue por tag conforme RNF12, quando autorizada a ação de versionamento; não criar tag de entrega antecipadamente nesta atividade.

Não foi feita submissão nem consultado o estado da atividade do Teams neste trabalho.

## 9. Riscos, pendências e próximos passos imediatos

| Risco/pendência | Tratamento | Responsável sugerido |
|---|---|---|
| Prazo de 10/11 e horário não confirmados | F1-01 busca aviso oficial imediatamente; trabalhar com a data provisória sem declarar prorrogação comprovada. | Líder |
| Quantidade de integrantes e atribuições desconhecidas; PDF inconsistente | Confirmar pessoas disponíveis e composição; preencher papéis sem reproduzir automaticamente oito ou seis alunos. | Líder/equipe |
| Protótipo/vídeo já feitos fora do repositório podem existir | Pedir links, validar conteúdo e permissões antes de refazer; tarefas continuam abertas até evidência. | UX/líder |
| Arquitetura e prototipagem ainda não comprovadas; prazo curto | Executar em paralelo por papéis; priorizar fluxos aprovados e legibilidade. Não trocar protótipos exigidos por texto para caber no prazo. | Android/UX |
| Exemplos do professor divergentes do escopo escolhido | Confirmar adaptações com o professor quando necessário: duas categorias, BEV, autonomia em horas e método da DEC-018. Não adotar as quatro categorias, nome “CompareAI”, gráficos ou matriz intermediária por mera cópia do exemplo. Abertura/splash e demais itens das páginas 4/11 devem ser conciliados em F1-01/F1-05, sem modificar requisitos automaticamente. | Líder + Android/UX |
| Cronograma ilustrativo da página 10 contém datas e tema divergentes | Usar páginas 7/9 e orientação posterior confirmada para a entrega. | Líder |
| Exigir implementação real antes do vídeo desvia da Fase 1 | Usar protótipos e diagramas, com exemplos rotulados; deixar Gradle/APK, integração e testes para a Fase 2. | Todos |
| Acesso Figma/vídeo falha para o avaliador | Testar permissões antes do envio e guardar evidência; reservar 10/11 para correções de acesso. | UX/líder |
| Dependências técnicas da Etapa 3 confundidas com bloqueios da apresentação | Mostrar CHECK, precisão e serialização como trabalho futuro nos momentos aprovados; não declarar essas validações executadas. | Android/dados |

Distribuir imediatamente: Android assume arquitetura/diagramas; UX assume mapa de telas/navegação/protótipo; dados assume síntese dos requisitos e da integração planejada; líder confirma prazo/equipe e coordena roteiro, pacote e envio. Primeiro aprovar este plano e associar pessoas reais aos papéis. A [mensagem preparada](fase-1-mensagem-equipe.md) pode ser compartilhada pelo usuário após essa revisão.
