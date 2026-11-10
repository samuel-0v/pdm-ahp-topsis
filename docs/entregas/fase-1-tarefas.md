# Fase 1 — Catálogo de tarefas e issues propostas

**Catálogo original:** v0.1 — 06/11/2026. **Atualização:** 10/11/2026. Distribuição nominal e prazos permanecem propostos. Os artefatos F1-05–F1-08 integram a Etapa 5 v0.5 aprovada pelo usuário; a branch de interface e o PR #5 já existem. Este catálogo não é inventário do estado de issues/Projects.

[Plano e diagnóstico](fase-1-plano.md) · [Cronograma](fase-1-plano.md#5-dependências-paralelismo-e-cronograma) · [Mensagem para o grupo](fase-1-mensagem-equipe.md)

O [fechamento da Etapa 5](../interface/finalizacao-v05.md#aprovação-e-fechamento-documental) reúne aprovação, fontes, apresentação e limites. As descrições abaixo preservam os critérios e metas originais; os artefatos de interface já produzidos devem ser reutilizados na consolidação da entrega.

Todos os prazos abaixo são **metas internas condicionadas a 10/11/2026**, ainda sem confirmação no Teams/professor. F1-01 deve registrar prazo e horário oficiais; silêncio não confirma adiamento. Os papéis precisam ser associados a integrantes reais antes da execução. “Dependência” indica artefato necessário, não obrigação de esperar passivamente: rascunhos independentes podem avançar enquanto a confirmação é buscada.

Os IDs F1-01–F1-14 são identificadores deste plano, não números de issues existentes. Ao aprovar, copiar título, objetivo e critérios para o GitHub Projects conforme DEC-031; usar **A fazer**, **Em andamento**, **Concluído**, com responsável confirmado e evidência antes de concluir. P0 é necessário à entrega/remoção de bloqueio; P1 é apoio de organização em paralelo. Não são tarefas de implementação do app.

## F1-01 — Confirmar prazo, formato de envio e composição da equipe

- **Objetivo:** obter orientação verificável para organizar a entrega e distribuir pessoas sem presumir prorrogação ou composição do grupo.
- **Responsável sugerido:** líder do projeto; equipe informa disponibilidade e trabalhos externos existentes.
- **Referências:** [enunciado](../../references/ProjetoDisciplina_PDM20262v2.pdf), páginas 4, 7, 9–11; [DEC-031](../decisoes/aprovadas/dec-031.md); [riscos do plano](fase-1-plano.md#9-riscos-pendências-e-próximos-passos-imediatos).
- **Entregável verificável:** registro em `docs/entregas/fase-1-acompanhamento.md` de atividade, data/horário, origem da confirmação, formato de entrega e pessoas que aceitaram cada papel. Solicitar links Figma/vídeo já existentes, se houver. O arquivo é destino proposto, ainda não criado.
- **Critério de conclusão:** referência do aviso/professor disponível; adiamento confirmado ou explicitamente negado; atividade/horário conhecidos; integrantes e responsável pelo envio confirmados. Registrar dúvidas sobre composição e adaptações, inclusive itens exemplificativos de tela; não alterar escopo por conta própria. Se não houver resposta, manter pendência aberta e comunicar o risco.
- **Dependências:** acesso de um integrante à turma/atividade no Teams ou contato com o professor; aprovação/distribuição deste plano.
- **Prioridade e prazo:** P0; 07/11, início do trabalho; informar imediatamente se já existir confirmação anterior.
- **Paralelismo:** pode ocorrer enquanto F1-02/F1-03/F1-05/F1-09 produzem seus rascunhos.
- **Branch/PR sugerido:** registro documental em `docs/fase1-consolidacao`, após aprovação; nenhuma ação remota nesta atividade.

## F1-02 — Consolidar os requisitos aprovados para a primeira entrega

- **Objetivo:** transformar a documentação existente em apresentação de requisitos legível para o professor, sem redefinir funcionalidades.
- **Responsável sugerido:** engenharia de dados; líder revisa completude e formato.
- **Referências:** [requisitos](../requisitos/README.md), [UC01–UC16](../requisitos/casos-de-uso/README.md), [rastreabilidade](../requisitos/rastreabilidade.md), [fechamento da Etapa 3](../modelagem/fechamento-etapa-3.md); enunciado, páginas 6, 7 e 9.
- **Entregável verificável:** documento de entrada da Fase 1 com objetivo, escopo, categorias, grupos de RF/RNF, casos principais e referências aos textos aprovados. Exportação em PDF ou formato aceito no Teams, com índice; formato final depende de F1-01.
- **Critério de conclusão:** 27 RFs e 13 RNFs preservados e recuperáveis no pacote; UC01–UC16 referenciados; exigências do professor separadas de decisões/adaptações da equipe. Leitor consegue identificar requisito e localizar o detalhamento sem acesso a caminho local. Não apresentar plano de testes como execução.
- **Dependências:** documentos já aprovados disponíveis; F1-01 para confirmar formato/atividade; F1-11 recebe a versão final.
- **Prioridade e prazo:** P0; rascunho em 07/11, versão final em 09/11 antes da consolidação.
- **Paralelismo:** independente da conclusão do protótipo e da arquitetura para selecionar/conferir requisitos.
- **Branch/PR sugerido:** `docs/fase1-requisitos-entrega`; revisar apresentação e links sem modificar as regras aprovadas.

## F1-03 — Documentar arquitetura MVVM e responsabilidades das camadas

- **Objetivo:** definir a estrutura técnica necessária à Fase 1 com base na modelagem aprovada.
- **Responsável sugerido:** desenvolvimento Android/Kotlin; engenharia de dados revisa os limites de persistência.
- **Referências:** [arquitetura documentada](../arquitetura/arquitetura.md), [RNF09](../requisitos/nao-funcionais/05-qualidade-codigo.md), [modelagem](../modelagem/dominio.md), [contratos aprovados](../modelagem/05-contratos-persistencia.md); enunciado, páginas 7, 11 e 12.
- **Entregável verificável:** documento de arquitetura descrevendo Compose/UI, ViewModels com StateFlow, domínio/motores, repositórios e fontes Room/Firestore, responsabilidades e direção das dependências. Incluir edição em memória, salvamento manual, estados de carregamento/erro/offline e exemplos de fluxo de ações/estado.
- **Critério de conclusão:** nenhum acesso direto a Room/Firestore nas telas ou motores; motores genéricos e independentes de Android; contratos de consultar, calcular, salvar e reabrir compreensíveis. Distinguir estados da UI, edição e avaliação persistida. Listar escolhas técnicas ainda necessárias para Fase 2 sem exigir Gradle, schema executável ou biblioteca configurada agora.
- **Dependências:** Etapas 1–3 concluídas; revisão dos papéis em F1-01. Pode iniciar pela documentação atual antes da confirmação externa do prazo.
- **Prioridade e prazo:** P0; 07/11.
- **Paralelismo:** em paralelo com inventário de telas F1-05, requisitos F1-02 e síntese de dados F1-09; alinhar estados entre Android e UX.
- **Branch/PR sugerido:** `docs/etapa4-arquitetura`; revisão de outro integrante quando disponível, sem código de aplicação.

## F1-04 — Produzir diagramas de arquitetura e fluxo de dados

- **Objetivo:** fornecer evidência visual da arquitetura, além dos diagramas de domínio já existentes.
- **Responsável sugerido:** desenvolvimento Android/Kotlin; apoio de engenharia de dados.
- **Referências:** F1-03; [índice de diagramas](../../diagrams/README.md); [contrato de operações](../modelagem/contratos/06-operacoes-validacao.md); enunciado, páginas 7, 9 e 12.
- **Entregável verificável:** fonte editável do diagrama em camadas e do fluxo de dados; renderizações legíveis no pacote/vídeo. Mostrar UI, ViewModel/StateFlow, domínio/AHP-TOPSIS, repositórios, Room, Firestore e cache; distinguir dependência de código e fluxo de execução.
- **Critério de conclusão:** diagramas renderizam, têm legenda/direção clara e correspondem ao texto de F1-03. Fluxo de consultar catálogo e fluxo de calcular/salvar/reabrir aparecem de modo explicável, sem afirmar integração executada. Não representar réplica integral do Firestore no Room ou sincronização pessoal. Fontes e exportações apontam para a mesma versão.
- **Dependências:** rascunho estável de F1-03; revisão de dados com F1-09.
- **Prioridade e prazo:** P0; 08/11, manhã.
- **Paralelismo:** refinamento dos diagramas pode ocorrer junto à revisão do texto de arquitetura; protótipos não precisam esperar as exportações finais.
- **Branch/PR sugerido:** mesma `docs/etapa4-arquitetura` de F1-03, com revisão do conjunto.

## F1-05 — Mapear telas e estados aos requisitos aprovados

- **Objetivo:** definir o conjunto de telas, diálogos e estados necessários antes de detalhar layouts.
- **Responsável sugerido:** Design/UX; desenvolvimento Android revisa viabilidade e estados.
- **Referências:** [UC01–UC16](../requisitos/casos-de-uso/README.md), [estados da avaliação](../modelagem/04-estados-transicoes.md), [RNF01/RNF02](../requisitos/nao-funcionais/01-usabilidade-acessibilidade.md), [escopo](../01-visao-e-escopo.md); enunciado, páginas 4 e 11.
- **Entregável verificável:** inventário em [mapa-telas.md](../interface/mapa-telas.md) com tela/diálogo, objetivo, UC/RF, ações, retorno e estados relevantes. Cobrir categorias, produtos/detalhes, catálogo pessoal, configuração, AHP, resumo, resultados, avaliações salvas e exportação.
- **Critério de conclusão:** UC01–UC16 associados a telas/diálogos/estados, sem confundir um UC com uma tela obrigatória. Estados de rascunho, AHP inconsistente, empate, sem diferenciação, alterações pendentes, confirmação de exclusão, carregamento, erro e offline localizados. Confrontar abertura/splash e os exemplos das páginas 4/11 com o escopo; registrar dúvidas para F1-01, sem adicionar quatro categorias, nome do exemplo ou gráficos automaticamente.
- **Dependências:** requisitos/modelagem já aprovados; F1-01 resolve eventuais dúvidas externas; sincronizar com rascunho de F1-03.
- **Prioridade e prazo:** P0; 07/11.
- **Paralelismo:** junto com arquitetura e síntese dos requisitos; protótipo pode começar pelas telas de consenso.
- **Branch/PR sugerido:** `docs/etapa5-interface-navegacao`.

## F1-06 — Completar e conferir o diagrama de navegação

- **Objetivo:** transformar o fluxo inicial em mapa demonstrável das telas e retornos do aplicativo planejado.
- **Responsável sugerido:** Design/UX com desenvolvimento Android.
- **Referências:** [navegação existente](../interface/navegacao.md), F1-05, [UC12–UC15](../requisitos/casos-de-uso/04-persistencia.md), [UC16](../requisitos/casos-de-uso/05-exportacao.md); enunciado, páginas 7 e 9.
- **Entregável verificável:** diagrama com fontes e renderização legível, complementando o fluxo atual sem alterar as regras. Catálogo pessoal, avaliações salvas, edição, rascunho e exportação fazem parte do mapa.
- **Critério de conclusão:** há caminho de avaliação completa, retorno após AHP inconsistente, rascunho/retomada e saída com Salvar/Descartar/Continuar. Falha ao salvar preserva edição; empate e ausência de diferenciação têm apresentação correta; exportação contempla versão salva e alterações pendentes. Não impor salvamento prévio em rascunho para concluir. Conferir mapa com o protótipo.
- **Dependências:** F1-05; alinhamento de estados com F1-03.
- **Prioridade e prazo:** P0; 08/11, manhã.
- **Paralelismo:** pode avançar enquanto F1-07 cria layouts com o mesmo inventário de telas.
- **Branch/PR sugerido:** `docs/etapa5-interface-navegacao`; outra pessoa revisa os fluxos quando disponível.

## F1-07 — Produzir layouts e protótipo Figma dos fluxos principais

- **Objetivo:** apresentar telas próprias da equipe e permitir percorrer o fluxo principal sem aplicativo implementado.
- **Responsável sugerido:** Design/UX; Android fornece revisão das ações e estados.
- **Referências:** F1-05/F1-06, [requisitos](../requisitos/README.md), [diretrizes de acessibilidade](../requisitos/nao-funcionais/01-usabilidade-acessibilidade.md); enunciado, páginas 9 e 11.
- **Entregável verificável:** arquivo Figma autoral, link de apresentação, frames identificados e primeira navegação interativa; registro do link, data/revisão e capturas no [índice dos protótipos](../interface/prototipos/README.md) e no [fechamento v0.5](../interface/finalizacao-v05.md). Se já existir protótipo externo, verificar e reaproveitar em vez de refazer por presunção.
- **Critério de conclusão:** fluxo consultável de categoria/produto → seleção/valores → julgamentos AHP → resumo → resultados; caminhos de rascunho/retomada e acesso às funções pessoais previstos no inventário. Duas categorias aprovadas representadas, com formulários genéricos e unidades corretas. Explicar paleta, ícones, tipografia e hierarquia. Dados e resultados de demonstração rotulados como ilustrativos; não prometer cálculo real no Figma.
- **Dependências:** F1-05 e navegação preliminar de F1-06; um responsável confirmado com acesso ao Figma.
- **Prioridade e prazo:** P0; 08/11.
- **Paralelismo:** pode avançar com F1-06; se houver um único responsável de UX, planejar a capacidade e deixar F1-08 para a revisão seguinte.
- **Branch/PR existente:** `docs/etapa5-interface-navegacao`, [PR #5](https://github.com/samuel-0v/pdm-ahp-topsis/pull/5). Figma v0.5 aplicado e aprovado pelo usuário; os scripts históricos de criação não devem ser repetidos.

## F1-08 — Completar estados alternativos e conferir acesso ao protótipo

- **Objetivo:** demonstrar comportamentos que influenciam a experiência e as regras centrais, além do caminho de sucesso.
- **Responsável sugerido:** Design/UX; apoio de Android para percorrer e conferir o fluxo.
- **Referências:** [estados](../modelagem/04-estados-transicoes.md), [resultados](../requisitos/funcionais/05-topsis-resultados.md), [exportação](../requisitos/funcionais/07-exportacao.md), [RNF01/RNF02](../requisitos/nao-funcionais/01-usabilidade-acessibilidade.md), F1-05/F1-07.
- **Entregável verificável:** variantes de frames/diálogos identificáveis no mesmo protótipo e tabela de estados em [mapa-telas.md](../interface/mapa-telas.md), com IDs/evidências no [fechamento v0.5](../interface/finalizacao-v05.md).
- **Critério de conclusão:** mostrar RC > 0,10 bloqueando ranking; vencedor único, empate e conclusão sem diferenciação; rascunho e aviso de alterações; exclusão confirmada; opções de exportação; carregamento, erro e offline com cache insuficiente ou dados locais disponíveis. Nada de vencedor arbitrário ou autosave. Legibilidade, rótulos e indicação além de cor conferidos no design. Link acessível em sessão equivalente à do professor e fluxos percorridos por outro integrante, quando disponível. Não declarar testes TalkBack ou do app a partir do Figma.
- **Dependências:** primeira versão F1-07 e mapa F1-06; participação de um revisor quando disponível.
- **Prioridade e prazo:** P0; 09/11, manhã.
- **Paralelismo:** Android/dados podem conferir requisitos e preparar roteiro enquanto UX conclui as variantes; a mesma pessoa não deve ser contada em tarefas simultâneas.
- **Branch/PR sugerido:** mesmo PR de interface; evidências necessárias à aprovação de F1-11.

## F1-09 — Preparar explicação da integração e preservação dos dados

- **Objetivo:** explicar o planejamento técnico exigido na apresentação, sem transformar a tarefa em implementação do catálogo.
- **Responsável sugerido:** engenharia de dados; Android revisa os limites entre repositórios e UI.
- **Referências:** [Firestore](../modelagem/contratos/04-firestore.md), [Room](../modelagem/contratos/01-estrutura-fisica.md), [compatibilidade/offline](../modelagem/contratos/05-compatibilidade-offline.md), [categorias e critérios](../decisoes/regras-transversais.md), [DEC-018](../decisoes/aprovadas/dec-018.md); enunciado, páginas 7 e 9.
- **Entregável verificável:** síntese em `docs/entregas/fase-1-dados-apresentacao.md` (destino proposto), com origem oficial, categorias/critério/unidades, dados pessoais locais, cache e snapshots. Preparar um exemplo ilustrativo curto e material para o bloco de requisitos/dados do vídeo.
- **Critério de conclusão:** explicar Firestore somente leitura pelo app; Room para produtos pessoais/avaliações; cache limitado ao disponível; snapshots independentes de alterações do catálogo; distinção entre dinheiro em centavos e Double do motor. Identificar a procedência pretendida dos dados e as rubricas/limites ainda pendentes. Não incluir credenciais, exigir projeto Firebase configurado, publicar produtos reais ou alegar funcionamento offline testado.
- **Dependências:** contratos aprovados já disponíveis; alinhar a exposição com F1-03/F1-04.
- **Prioridade e prazo:** P0; 07/11.
- **Paralelismo:** independente da primeira versão visual do protótipo; pode apoiar F1-02 e o roteiro ao mesmo tempo, conforme pessoas disponíveis.
- **Branch/PR sugerido:** `docs/fase1-integracao-planejada`.

## F1-10 — Registrar responsabilidades e andamento da equipe

- **Objetivo:** tornar a divisão do trabalho verificável e acompanhar bloqueios até o envio.
- **Responsável sugerido:** líder coordena; cada responsável confirmado atualiza sua atividade.
- **Referências:** [DEC-031](../decisoes/aprovadas/dec-031.md), [RNF12/RNF13](../requisitos/nao-funcionais/06-processo-desenvolvimento.md), F1-01 e este catálogo.
- **Entregável verificável:** acompanhamento com pessoas confirmadas, tarefa, estado, previsão e link da evidência; issues/Projects somente depois de aprovadas as propostas. Registrar o link do quadro quando houver acesso e criação confirmada.
- **Critério de conclusão:** responsável nominal confirmado para cada tarefa assumida; estados A fazer/Em andamento/Concluído coerentes com as evidências; bloqueio com motivo e próximo passo; progresso comunicado diariamente até a entrega. Não usar autores de commits como substituto de confirmação de papéis. Não declarar quadro criado sem verificá-lo.
- **Dependências:** plano aprovado e associação de pessoas aos papéis em F1-01; autorização futura para criar issues/branches/PRs.
- **Prioridade e prazo:** P1; registro inicial em 07/11, atualização diária até F1-14.
- **Paralelismo:** atividade curta de apoio a todos os fluxos; não bloquear quem já dispõe das informações para produzir artefatos.
- **Branch/PR sugerido:** `docs/fase1-consolidacao`; configuração futura do Projects segue DEC-031.

## F1-11 — Revisar e consolidar o pacote documental da Fase 1

- **Objetivo:** reunir materiais coerentes e acessíveis para o vídeo e o Teams.
- **Responsável sugerido:** líder; revisão cruzada de Android, dados e UX.
- **Referências:** [checklist oficial](fase-1-plano.md#2-checklist-oficial-da-primeira-entrega), [critérios de aceite](fase-1-plano.md#6-critérios-de-aceite-da-fase-1), F1-02/F1-04/F1-06/F1-08/F1-09; enunciado, páginas 7 e 9.
- **Entregável verificável:** documento de entrada, requisitos, diagramas, protótipos/evidências e manifesto de links/versões, no formato aceito pela atividade. Destinos dos arquivos de apresentação serão definidos após F1-01; não há pacote final criado por este plano.
- **Critério de conclusão:** itens oficiais presentes; RF/RNF e UCs recuperáveis; diagramas renderizados; protótipos acessíveis; duas categorias e regras AHP/TOPSIS consistentes; links abrem fora dos caminhos locais. Identificar o que está concluído, simulado, planejado ou pendente. Congelar versão de apresentação para F1-13; qualquer ajuste posterior precisa ser refletido no material e na gravação afetados.
- **Dependências:** versões finais de F1-02/F1-04/F1-06/F1-08/F1-09; formato/atividade definidos em F1-01.
- **Prioridade e prazo:** P0; 09/11, antes da gravação final.
- **Paralelismo:** revisão parcial pode iniciar enquanto as últimas variantes de F1-08 são finalizadas; aceite do pacote exige todas as dependências.
- **Branch/PR sugerido:** `docs/fase1-consolidacao`; integração dos PRs documentais de cada frente antes da versão final.

## F1-12 — Finalizar roteiro e divisão das falas do vídeo

- **Objetivo:** transformar a sequência sugerida pelo professor em falas e demonstrações que caibam na apresentação.
- **Responsável sugerido:** líder coordena; engenharia de dados, Android e UX preparam seus blocos.
- **Referências:** [plano do vídeo](fase-1-plano.md#7-plano-do-vídeo); enunciado, página 9; materiais de F1-02/F1-04/F1-06/F1-08/F1-09.
- **Entregável verificável:** roteiro autoral com introdução, requisitos/dados, modelagem/arquitetura, design/protótipos e encerramento; indicação de apresentador confirmado, arquivo/tela de apoio e transição. Pode ficar em `docs/entregas/fase-1-video-roteiro.md` (destino proposto).
- **Critério de conclusão:** todos os blocos preparados, falas distribuídas sem inventar pessoas, demonstração do protótipo ensaiada e limite de duração verificado em F1-01. Encerramento informa implementação/testes da Fase 2 em 11/12/2026. Não prometer aplicativo funcional ou testes que não existem. Meta de 8–10 minutos é recomendação interna se não houver limite oficial diferente.
- **Dependências:** F1-01 para orientações oficiais; versões preliminares dos materiais permitem rascunho, mas a revisão final usa F1-11.
- **Prioridade e prazo:** P0; estrutura em 07/11 e roteiro final em 09/11.
- **Paralelismo:** iniciar junto com as demais frentes; ensaio final após consolidação dos artefatos.
- **Branch/PR sugerido:** `docs/fase1-consolidacao`.

## F1-13 — Gravar, publicar e verificar o vídeo da Fase 1

- **Objetivo:** gerar o link de vídeo exigido, com imagem e áudio utilizáveis pelo professor.
- **Responsável sugerido:** líder coordena gravação/publicação; cada papel apresenta o bloco confirmado em F1-12.
- **Referências:** F1-11/F1-12; [vídeo e submissão](fase-1-plano.md#7-plano-do-vídeo); enunciado, páginas 7 e 9.
- **Entregável verificável:** vídeo publicado e registro de URL, versão dos materiais, data e conferência de acesso/áudio. Usar destino permitido e acessível ao professor; não é necessário versionar arquivo de vídeo grande no Git.
- **Critério de conclusão:** assistir do início ao fim; texto/diagramas legíveis e áudio compreensível; todos os blocos e fluxos escolhidos aparecem; link abre com as permissões do avaliador; resultados ilustrativos identificados. Publicação e submissão são eventos separados. Guardar evidência da verificação sem expor credenciais.
- **Dependências:** F1-11 e F1-12 concluídos; ferramenta de gravação e local de publicação disponíveis à equipe.
- **Prioridade e prazo:** P0; 09/11 após revisão do material; reservar 10/11 para falhas de acesso, se a data estiver confirmada.
- **Paralelismo:** testes de captura/áudio e preparação do ambiente podem ocorrer antes; gravação final depende das versões aprovadas do pacote.
- **Branch/PR sugerido:** registrar link e evidências em `docs/fase1-consolidacao`; publicação externa é ação futura do responsável confirmado.

## F1-14 — Conferir e submeter os materiais na atividade do Teams

- **Objetivo:** concluir a primeira entrega com prova de que arquivos e links foram efetivamente enviados.
- **Responsável sugerido:** líder ou integrante explicitamente confirmado para realizar o envio; revisão final de outro integrante quando disponível.
- **Referências:** [checklist Teams](fase-1-plano.md#8-checklist-de-submissão-no-teams), F1-01/F1-11/F1-13; enunciado, página 7; [RNF12](../requisitos/nao-funcionais/06-processo-desenvolvimento.md).
- **Entregável verificável:** submissão no Teams e registro de atividade, data/hora, responsável, arquivos, links e comprovante em local acessível à equipe. Identificar posteriormente a versão realmente entregue por tag conforme RNF12, com autorização de versionamento.
- **Critério de conclusão:** prazo/horário e atividade confirmados; documento de requisitos, diagramas, protótipos e link do vídeo presentes; permissões testadas; ação final de envio concluída no Teams; recibo/captura ou confirmação equivalente guardado; grupo informado. Sem comprovante, manter “envio não verificado”. Não declarar que upload de arquivo ou criação de tag comprovam submissão.
- **Dependências:** F1-01, F1-11 e F1-13; acesso do responsável ao Teams e autorização da equipe para envio.
- **Prioridade e prazo:** P0; 10/11/2026 antes do horário oficial, somente se essa data for confirmada. Se o prazo for outro, replanejar e registrar a orientação.
- **Paralelismo:** conferência preliminar da atividade e dos anexos pode ocorrer antes do vídeo; conclusão depende do pacote e do link finais.
- **Branch/PR sugerido:** registro de evidências em `docs/fase1-consolidacao`, conforme regras de compartilhamento; nenhum envio, commit ou tag executado nesta atividade.
