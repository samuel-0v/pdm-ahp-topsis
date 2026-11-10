# Atualização Figma, validação e fechamento documental — v0.5

[Índice](README.md) · [Validação](validacao.md) · [Fontes nativas](prototipos/figma/revisao-v05/README.md)

**10/11/2026 · branch `docs/etapa5-interface-navegacao`, base `91cafd7`.** O Git e o remoto foram conferidos antes da edição e após a pausa solicitada pelo usuário, até as 05h de Brasília. Nenhum commit posterior da equipe foi encontrado; alterações existentes foram preservadas. A Etapa 4 já estava integrada. Requisitos, decisões normativas e implementação Android não foram alterados.

**Estado atual:** Etapa 5 v0.5 aprovada pelo usuário e concluída documentalmente. O [registro de aprovação](#aprovação-e-fechamento-documental) complementa o parecer técnico e os testes abaixo, realizados antes dessa manifestação.

## Parecer técnico e alcance

O arquivo autoral foi efetivamente atualizado na página **03 · Protótipo revisado v0.5**, pelo Firefox autorizado e pelo plugin Scripter. As páginas 01/02 e o inventário de 84 frames anteriores foram preservados. Há **598 quadros de contextos/ramificações, 1.681 conexões e sete entradas de apresentação**, não 598 famílias de telas. A [auditoria nativa final](prototipos/figma/revisao-v05/auditoria-final.json) lê as reações efetivas, não apenas scripts preparados; não encontrou divergências nas verificações executadas ou no inventário histórico conferido.

O Figma permite demonstrar quatro roteiros preparados (P/B × oficiais/mista), com oito conjuntos de números calculados pelo módulo DEC-018 e conferidos por oráculo Python. Não há entrada livre seguida de resultado fixo. O fluxo com dois Smartphones utiliza preço 5.000,00 e dez igualdades; BEV usa julgamentos `[2, 1, 2, 4, 1/2, 1, 2, 2, 4, 2]`, com pesos proporcionais a `[4, 2, 4, 2, 1]`. RC = 0 aplica-se a esses julgamentos, não a qualquer preferência do usuário.

As ramificações nativas não possuem memória global: listas/rascunhos pertencem ao roteiro aberto. Campos são restritos e identificados; armazenamento, exclusão, PDF e compartilhamento são simulados. Erros do laboratório são exemplos isolados, não injeções em uma transação em andamento. **O HTML mantém valores/julgamentos livres, cálculo correspondente, sessão editável distinta da versão salva e testes funcionais próprios.** Nenhum desses artefatos implementa o app Android.

## Achados concretos e correções aplicadas

| Achado / cenário | Aplicação efetiva | Evidência / limite |
|---|---|---|
| Figma histórico sem pontos iniciais, com conexões ainda pendentes | Nova página separada, sete entradas e conexões básicas; scripts iniciais históricos não repetidos | [Inspeção anterior](prototipos/figma/revisao-v05/inspecao-inicial.json) e [auditoria atual](prototipos/figma/revisao-v05/auditoria-final.json). |
| Layout histórico não refletia a identidade visual do HTML | Coleção própria com verde profundo/superfícies claras, Roboto, ritmo 4/8/16/24, cards e ilustrações vetoriais | [Design system](design-system.md#adaptação-aplicada-ao-figma-v05). As duas páginas antigas não foram recoloridas. |
| Primeiro lote: textos com altura de 1 px e botões herdando largura inadequada | Altura automática depois do dimensionamento; seis variantes locais de ação com quebra de rótulo e mínimo de 52 | Correção registrada no script 03; geometria final auditada. Não se deixou o lote defeituoso como versão final. |
| Runtime nativo ignorava opções de `toLocaleString`, mostrando coeficientes longos com ponto | Exibição com quatro casas e vírgula em C*/distâncias; uma casa nos percentuais | Script 05 e verificação dos textos por identidade/origem. Os números do cálculo não foram trocados. |
| Diálogos precisavam manter foco visual e as três escolhas da DEC-025 | Overlay de tela inteira, scrim 32%, superfície central, raio 24 e sombra; fechamento explícito | [Saída real na apresentação](prototipos/evidencias/figma-v05/saida-tres-opcoes.png). Não equivale a provar semântica TalkBack. |
| Reação com posição manual em overlay central e navegação para o próprio quadro eram rejeitadas pela API | Overlays centrais sem posição manual; quadros já selecionados têm indicação estática, sem navegação para si mesmos | As tentativas rejeitadas foram corrigidas; a auditoria lê os destinos finais aceitos. A ordem do roteiro não vira regra do aplicativo. |
| Seleção mista/cadastro poderia selecionar o recém-criado automaticamente | FORM volta ao mesmo contexto com os dois oficiais; a inclusão pessoal exige toque posterior | [Retorno BEV](prototipos/evidencias/figma-v05/bev-cadastro-retorno-sem-autoselecao.png). Formulário preparado, sem texto livre. |
| Edição concluída e PDF poderiam misturar a edição atual com o resultado salvo | Edição de preço separada, bloqueio de salvar/exportar desatualizado, exportação anterior preservando a edição | [Escolha de versão](prototipos/evidencias/figma-v05/pdf-edicao-desatualizada.png), [rascunho após salvar edição](prototipos/evidencias/figma-v05/edicao-salva-como-rascunho.png). |
| HTML ainda informava que o estado Figma era totalmente desconhecido | Texto externo atualizado para descrever a página 03 e suas restrições; HTML regenerado | Mudança editorial em `modelo.html`/`index.html`; calculador e contratos web preservados. |

## Apresentação e evidências

Os percursos são executados no player Figma pelo Firefox, com cliques/rolagem e conferência do destino real na barra de endereço. O registro final distingue percursos completos de visitas a estados por link. Uma captura de carregamento foi recusada como evidência final; falha de leitura da barra foi repetida sem presumir sucesso.

Confirmado no percurso de Smartphones: categorias, dois produtos, ajuste preparado, dez pares, pesos/RC, revisão, ranking, salvamento explícito, PDF salvo, edição independente, exportação anterior mantendo edição, três escolhas de saída, rascunho e recálculo com novos números correspondentes. [Ranking final](prototipos/evidencias/figma-v05/smartphones-resultado.png), [pesos](prototipos/evidencias/figma-v05/smartphones-pesos.png), [PDF salvo](prototipos/evidencias/figma-v05/smartphones-pdf-salvo.png), [recálculo](prototipos/evidencias/figma-v05/resultado-recalculado.png).

O percurso BEV misto também chegou aos dez pares, pesos/RC, resultado, salvamento e PDF, incluindo cadastro sem autoseleção e os dois sentidos Saaty com intensidades. A exclusão foi cancelada e depois confirmada em seu contexto preparado. A retomada de Valores sem edição saiu diretamente; registrar uma resposta voltou a sinalizar pendência. Descartar um recálculo da edição restaurou o último rascunho salvo.

No laboratório foram abertos os exemplos de AHP inconsistente, empate competitivo, ausência de diferenciação, valor ausente, normalização indefinida, bloqueios DEC-041/042 e falhas de salvamento/PDF/compartilhamento; os retornos foram exercitados. A conclusão sem diferenciação pôde ser salva e exportada como exemplo, sem posto/C* definidos. Carregamento levou ao erro de consulta. Essas são visitas e ações em estados preparados, não falhas injetadas no cálculo do Figma.

As referências 360/200%, 600/100% e 600/200% foram abertas, capturadas e roladas até a ação de retorno. O percurso padrão usa 360/100%. Textos longos quebram linha e a ação final permanece alcançável. Não se generaliza essa amostragem visual para todos os 598 quadros, nem para dp/sp/fontScale Android. Os outros dois cruzamentos (Smartphones misto e BEV oficiais) receberam auditoria estrutural e numérica; não houve percurso GUI completo desses dois roteiros.

## Antes/depois visual

| Ajuste nativo | Antes real | Depois real |
|---|---|---|
| Identidade visual, hierarquia e cards de categoria | [Página 01 preservada](prototipos/evidencias/figma-v05/antes-inicio-preservado.png) | [Nova página 03](prototipos/evidencias/figma-v05/depois-inicio-exportado.png) |
| Precisão visual do ranking | [Casas excessivas do runtime](prototipos/evidencias/figma-v05/antes-formatacao-resultado.png) | [Quatro casas, vírgula e mesma ordenação](prototipos/evidencias/figma-v05/smartphones-resultado.png) |

O par de início é exportação PNG nativa dos frames reais, com IDs/hashes em [exportações](prototipos/evidencias/figma-v05/exportacoes.json); o par de ranking é captura real do player. Não são imagens geradas por IA. A nova hierarquia dá destaque à tarefa e à categoria; ilustrações vetoriais distinguem as duas entradas sem substituir seus nomes. Superfícies claras, bordas e mensagens textuais mantêm legibilidade e estados além da cor. A comparação da identidade anterior/local continua no [parecer histórico](revisao-uiux.md#antes-e-depois-visual). Inspeção técnica de capturas não constitui teste com participantes nem aprovação da equipe/professor.

## Verificações desta rodada

- Auditoria nativa: 598 quadros, 1.681 conexões efetivas, sete entradas, textos de resultados por nome/origem, alvos conectados ≥48, alturas/limites horizontais e rolagem; zero problemas nas verificações executadas e zero diferenças no inventário histórico conferido.
- Roteiros: 3.804 verificações estruturais/numéricas, 40 pares nos quatro percursos e oito conjuntos numéricos conferidos por Python independente. [Relatório](prototipos/evidencias/figma-v05/roteiros-verificacao.json).
- HTML: 13 cenários, 234 verificações matemáticas, 18 pares de contraste e 295 verificações de interação em cada modo (normal/reduzido), com 64 combinações de layout por rodada. Resultados e limites em [validação](validacao.md).
- Apresentação: percursos e visitas acima executados; [registro dos destinos](prototipos/evidencias/figma-v05/apresentacao.json) separado da auditoria estrutural. O acesso sem a conta editora é tratado no parecer de prontidão ao final.

RNF01 com participantes, Android/TalkBack, dp/sp/fontScale reais, IME/ciclo de vida, Room/Firebase, PDF real e integração das DEC-043–049 pertencem à implementação/validação posterior. Seu plano permanece, sem inventar execução nem usá-los como bloqueios artificiais da Etapa 5 documental. Na rodada técnica ainda não havia aprovação do usuário; ela foi recebida posteriormente e está registrada abaixo. Vídeo final, aceite do professor e envio ao Teams não foram realizados ou presumidos.

A retomada testada revelou que reutilizar o quadro pendente após abrir o rascunho podia sugerir alteração inexistente. A revisão acrescentou estados limpos de consulta/retomada e separou o descarte do recálculo, que agora restaura o último rascunho salvo. Retomadas parciais fora dos cenários preparados ficaram desabilitadas e rotuladas; permanecem funcionais no HTML. Não se declara que o Figma mantém dirty checking ou memória global.


## Acesso e decisão de prontidão

[**Abrir apresentação — abertura e avaliação**](https://www.figma.com/proto/S1Ba7mzFVfE1d0ds21FR36/PDM-AHP-TOPSIS-%E2%80%94-Etapa-5-%C2%B7-Proposta-para-revis%C3%A3o?node-id=21-2567&starting-point-node-id=21%3A2567&page-id=21%3A2491&scaling=min-zoom&content-scaling=fixed). Em uma janela privativa nova do Firefox, sem autenticar nessa execução, OPEN0 transicionou a HOME e foi possível abrir a categoria Smartphones. [Início](prototipos/evidencias/figma-v05/acesso-privativo-inicio.png) e [categoria](prototipos/evidencias/figma-v05/acesso-privativo-categoria.png) mostram esse contexto. Não foram ampliadas permissões ou testada uma conta específica do professor. Uma tentativa anterior em Chrome headless expirou e não foi contada como sucesso.

**Parecer técnico anterior à aprovação: a documentação e os protótipos da Etapa 5 estão preparados para revisão e apresentação da primeira entrega, com os limites explícitos acima.** O impedimento remoto que justificava o draft foi superado por edição real, auditoria nativa, uso no player e conferência de acesso. O [PR #5](https://github.com/samuel-0v/pdm-ahp-topsis/pull/5) foi retirado de draft e recebeu a aprovação do usuário nesta conversa, conforme registro abaixo. Aprovação documental não comprova merge ou entrega acadêmica.

Para demonstrar, usar o fluxo inicial da página 03: Smartphones com dois oficiais e dez igualdades, ou BEV com cadastro pessoal, inclusão explícita e as respostas habilitadas. O laboratório abre estados especiais e amostras de texto ampliado. Para experimentar outros números/preferências e retomadas arbitrárias, usar o HTML calculável. Os coeficientes Figma pertencem somente às entradas preparadas, identificadas na interface.

Restam vídeo/material final e envio pelos responsáveis. Implementação Android e os testes futuros mantêm seus planos próprios; não são condições para reabrir o fechamento documental aprovado. [Validação](validacao.md) reúne comandos e limites; [fontes nativas](prototipos/figma/revisao-v05/README.md) permitem inspecionar o que foi aplicado sem repetir a criação histórica.

## Aprovação e fechamento documental

**10/11/2026 — aprovação explícita do usuário nesta conversa:** “da minha parte essa versao esta aprovada”. A manifestação refere-se à Etapa 5 v0.5 apresentada, com base no commit `a8c0d68`, incluindo documentação, demonstrador HTML calculável e página 03 do Figma com roteiros preparados. As correções editoriais de status e referências feitas após essa manifestação preservam a interface e os contratos aprovados.

**Status: Etapa 5 v0.5 aprovada pelo usuário e concluída documentalmente.** A aprovação abrange interface, navegação e prototipagem nos limites documentados; não altera requisitos/decisões, não aprova a Etapa 4 por extensão e não declara implementação Android ou execução dos testes futuros. A manifestação é do usuário, sem atribuí-la a outros integrantes ou ao professor. Vídeo, pacote final e envio ao Teams permanecem tarefas da entrega acadêmica.

## Conferência editorial após a aprovação

Foram alinhados os cabeçalhos da revisão v0.5, os índices, o resumo SVG de navegação, as referências de arquitetura e o acompanhamento da primeira entrega. O plano de retomada e o plugin local antigos receberam identificação explícita de histórico; seus scripts não foram reaplicados. Caminhos propostos já substituídos por documentos existentes foram corrigidos. Relatórios numéricos, capturas e ledgers anteriores permaneceram como evidências datadas, sem reescrever seus resultados como novos testes.

Validação desta conferência: `python3 docs/interface/prototipos/verificar_documentacao.py` e `git diff --check`. A conferência documental inclui os índices, os sete documentos de arquitetura, os três documentos da primeira entrega e DEC-049, além da área de interface. O SVG foi renderizado para inspeção visual. Esta rodada editorial não modifica o HTML, os cálculos ou o arquivo Figma.

Resultado da conferência editorial de 10/11: **PASS — 432 links locais e 46 âncoras**, além das verificações existentes de manifesto, rastreabilidade, geração, sintaxe e XML/SVG; `git diff --check` sem erros. O HTML foi regenerado e permaneceu idêntico ao aprovado. O SVG renderizado foi inspecionado visualmente. Não houve repetição dos testes funcionais HTML/Figma nesta rodada editorial; os resultados anteriores continuam vinculados à revisão técnica que os executou.
