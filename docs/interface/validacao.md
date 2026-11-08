# Revisão e validação da proposta

**Atividade iniciada em 06/11/2026; continuação local em 08/11/2026. Status: material preparado para revisão; Etapa 5 ainda aberta.** Branch: `docs/etapa5-interface-navegacao`, criada nesta continuação. Commit e push foram autorizados posteriormente pelo usuário.

[Índice](README.md) · [Rastreabilidade](mapa-telas.md) · [Limites do protótipo](prototipos/README.md)

## Evidências executadas

| Verificação | Resultado e limite |
|---|---|
| Leitura da base aprovada e enunciado | Base e adaptações registradas no índice; requisitos e DEC-001–DEC-042 não foram editados. |
| Acesso Figma | Autenticação, criação de arquivo separado, importações e montagem confirmadas pelo serviço. 84 frames na página do protótipo; ledger com IDs reais. |
| Revisão visual Figma | Capturas de entrada, AHP, resultado, diálogo e amostra ampliada inspecionadas. Encontrados os problemas registrados em `prototipos/README.md`; correções/conexões interrompidas pelo limite do plano Starter. **Não aprovado visualmente nem como fluxo navegável.** |
| Números ilustrativos | **Treze cenários** conferidos: cinco originais e oito combinações de alternativas em smartphones/BEV, com naturezas próprias. Não são testes dos motores Android. |
| Contraste proposto | Sete pares de texto/fundo com razão calculada ≥4,5:1; valores em [contraste.json](prototipos/evidencias/contraste.json). Não é auditoria de todos os nós/estados do Figma. |
| Interação local v0.2 | **124 verificações passaram** no Chrome 151.0.7922.169 headless, Linux, viewport de teste 1440 × 1200 CSS px. [Relatório atual](prototipos/evidencias/interacoes-v02.json) e [roteiro executável](prototipos/verificar_navegador.py). O [relatório v0.1](prototipos/evidencias/interacoes.json), com 84 verificações, foi preservado como histórico. |
| Geometria local | Parte das 124 verificações: sete telas × larguras 360/600 × texto 100%/200%, sem elementos essenciais fora da largura e com altura dos botões ≥48 px. Não comprova toda a matriz Android nem TalkBack. |
| Links/manifesto/sintaxe | Destinos, links, 86 descritores locais, correspondência dos 84 IDs Figma, HTML/projetor/fixtures embutidos e SVG conferidos pelo verificador documental. Fontes do plugin e scripts Figma com sintaxe válida; API de mutação não foi testada. |
| Inspeção preventiva do plugin | Três verificações em Node: rejeitar arquivo diferente, rejeitar controle ausente e inspecionar o inventário correto sem chamar aplicação. Manifesto sem rede conferido. Não equivale a teste real do plugin no Figma. |
| Whitespace e escopo | `git diff --check` executado; revisão dos arquivos para garantir ausência de alterações Android/requisitos/decisões. Sem commit. |

As interações exercitadas incluem seleção mínima, dez pares pendentes/respondidos, AHP/TOPSIS sem autosave, as três opções da DEC-025, falha no próximo salvamento com preservação da edição, retomada/descarte de rascunho, edição de concluída, exportação da última salva mantendo mudanças, bloqueio RC, empate e conclusão/exportação simulada sem diferenciação. Não há teste de banco, rede, compartilhamento real ou desempenho de motores nesta rodada.

Na continuação foram exercitados também: percurso BEV completo com dez pares próprios; preservação de categoria após salvar; cadastro pessoal durante seleção; falha de cadastro sem perda da seleção; seleção mista; coexistência de nomes iguais com origem diferente; empate entre esses registros; descarte de uma nova categoria preservando a versão anterior; abertura de edição concluída sem marcar alteração só por abrir.

Capturas **do HTML local**, que cresce verticalmente e permite rolagem:

- [Revisão em 360](prototipos/evidencias/revisao-360.png).
- [Resultado em 600 com texto 200%](prototipos/evidencias/resultado-600-texto200.png).
- [Revisão BEV v0.2 em 360](prototipos/evidencias/bev-revisao-360.png).
- [Resultado misto v0.2 em 600/texto 200%](prototipos/evidencias/mista-resultado-600-texto200.png).

## Retomada do Figma em 08/11

A leitura confirmou os 84 frames e a estrutura anterior. A tentativa de aplicar correções foi rejeitada pelo limite de chamadas Starter, sem mutação confirmada. As reações internas M3 existentes não são conexões de fluxo do aplicativo. `whoami` confirmou o plano. Não se alterou o status do Figma para navegável.

Foi preparado um [plugin local de aplicação](prototipos/figma/plugin-local/README.md), com fontes revisáveis, empacotador, interface de inspeção/aplicação e relatório de IDs. O [ZIP de revisão](prototipos/figma/plugin-local/pacote-revisao.zip) ainda precisa do ID real de um plugin de desenvolvimento gerado pelo Figma; não foi instalado ou executado. A verificação sintática e os testes preventivos não validam layout, condicionais, overlays ou navegação no runtime Figma.

## Roteiro para a primeira apresentação

| Passo | Demonstrar | Evidência a apontar |
|---|---|---|
| 1 | Catálogo e detalhes de Smartphones Premium; mostrar que BEV usa outros critérios | T01–T03; apenas duas categorias aprovadas. |
| 2 | Iniciar avaliação e selecionar uma alternativa, depois duas | Avanço bloqueado com menos de duas, seleção identificável. |
| 3 | Conferir valores e preço efetivo ajustado | Original e efetivo distintos; catálogo não muda. |
| 4 | Responder dez pares pela igualdade, revisando um anterior | Progresso real de respostas; ausência não vale 1. |
| 5 | Mostrar pesos, λmax, IC, RI e RC | 20% por critério, RC 0 no cenário principal; cenário inconsistente separado bloqueia TOPSIS. |
| 6 | Revisar, processar e consultar ranking | C* e distâncias coerentes, resultado ainda não salvo. |
| 7 | Sair, continuar; simular falha e depois salvar | Três ações da DEC-025; falha não confirma gravação nem sai. |
| 8 | Salvar/retomar rascunho; editar concluída e descartar | Separação entre edição em memória e versão salva. |
| 9 | Mostrar empate e sem diferenciação | Mesmo posto no empate; nenhuma posição/C* inventada na ausência de diferenciação. |
| 10 | Exportar última salva com edição pendente | Edição permanece; compartilhamento é simulado e não confirma envio. |

Abrir o HTML para a demonstração navegável atual. Mostrar o Figma como trabalho editável em andamento, explicando a pendência de conexões; não apresentá-lo como fluxo já testado. O vídeo, a autorização de acesso do professor e o envio da entrega ainda precisam ser produzidos/verificados.

## Validação de usabilidade e acessibilidade ainda necessária

Aplicar o RNF01 com três participantes que não desenvolveram as telas: consultar produto, concluir avaliação, identificar melhor alternativa/empate e salvar/retomar rascunho. Explicar apenas o objetivo; registrar conclusão sem ajuda, ajuda necessária e dificuldades. Pelo menos dois de três devem concluir cada tarefa sem ajuda para navegar. Corrigir e repetir tarefas bloqueadas. Nenhum participante foi recrutado ou teste humano declarado nesta execução.

No Android futuro, aplicar RNF02: retrato em 360/600 dp, fontes padrão/200%, fluxos de avaliação/resultados com TalkBack, ordem de leitura, semântica, texto/símbolo além da cor e alvos ≥48 × 48 dp. Verificar teclado, foco, diálogo, rolagem e ciclo de processo. A geometria do HTML não substitui esses testes.

## Pendências para aceite

| Prioridade | Trabalho |
|---|---|
| P0 | Corrigir e conectar Figma, testar no modo de apresentação e verificar permissão do avaliador. |
| P0 | Revisar mapa/fluxos/telas com a equipe; alinhar o contrato de navegação com a Etapa 4. |
| P1 | Portar seleção mista/cadastro/BEV do HTML para o Figma; completar entrada e AHP fora do cenário fixo e exceções secundárias. |
| P1 | Rodar protocolo de usabilidade; revisar contraste e expansão de texto de todos os componentes Figma. |
| P1 | Preparar capturas/vídeo e consolidar com a arquitetura para a primeira entrega. |
| Posterior | Rotas, ViewModels, repositórios, PDF real, semântica Compose e testes Android, após autorização da etapa de implementação. |

## Arquivos desta atividade

Criados nesta execução: `docs/interface/README.md`, `mapa-telas.md`, `especificacao-telas.md`, `design-system.md`, `validacao.md` e os artefatos listados no [índice de protótipos](prototipos/README.md), incluindo HTML, manifesto, fixtures, scripts Figma, ledger, verificações, capturas e diagrama SVG.

Alterados: `docs/interface/navegacao.md` e `docs/README.md`. O diretório `docs/entregas/` já estava não rastreado no início e foi preservado. Não foram alterados requisitos, decisões aprovadas, modelagem ou arquivos do aplicativo. Alterações paralelas de arquitetura ficam fora do commit da Etapa 5.

Na continuação de 08/11 foram acrescentados o projetor `contextos.js`, a preparação `expandir_cenarios.py`, os metadados/registro Figma v0.1/continuação, as fontes e o ZIP de revisão do plugin local, duas capturas e `interacoes-v02.json`. HTML, manifesto, fixtures, verificadores e documentação da área foram atualizados. O registro JSON dessa continuação preserva o estado anterior à autorização posterior de commit e push.
