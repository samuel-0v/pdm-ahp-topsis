# Revisão nativa v0.5 — 10/11/2026

[Arquivo autoral](https://www.figma.com/design/S1Ba7mzFVfE1d0ds21FR36) · [Inspeção inicial](inspecao-inicial.json)

A conta com edição estava aberta no Firefox, conforme informado pelo usuário. A leitura detalhada MCP continuou limitada pelo plano Starter, e a criação de plugin generativo hospedado retornou falta de permissão. O caminho funcional foi o plugin de comunidade [Scripter, de Rasmus](https://www.figma.com/community/plugin/757836922707087381), executando a API nativa no arquivo autorizado. O plugin local histórico do repositório não foi instalado nem executado. Nenhuma compra ou alteração de plano foi realizada.

A inspeção confirmou as duas páginas, os 84 IDs/nomes e as fontes Roboto; não havia pontos iniciais de apresentação. A atualização cria **03 · Protótipo revisado v0.5**, mantendo as duas páginas anteriores. Os scripts iniciais históricos 01/02 e os patches históricos 03/04/05 não foram executados sobre o trabalho existente.

## Escolhas e limites

Variáveis de design estão disponíveis no Starter; variáveis, expressões e condições **de prototipagem** exigem Education ou plano pago, conforme a [documentação oficial](https://help.figma.com/hc/en-us/articles/14506587589399-Use-variables-in-prototypes). Por isso, a página usa conexões básicas entre quadros de **roteiros preparados**. Não tenta contornar essa restrição.

São quatro roteiros: Smartphones/BEV × dois oficiais/seleção mista. Os oito conjuntos numéricos (preço original/ajustado em cada roteiro) são calculados pelo mesmo módulo demonstrativo `numeros.js`, conforme DEC-018. No Figma, somente as entradas correspondentes estão habilitadas: não há texto livre seguido de coeficiente fixo. Há dez pares completos e sentido/intensidade na escala Saaty; um percurso preparado consistente não substitui a validação de julgamentos arbitrários do HTML.

Listas, salvamentos e retomadas são ramificações ilustrativas, sem memória global ou persistência. Voltar revisita o quadro ilustrado. Não é a pilha Android nem uma segunda implementação do motor. PDF/compartilhamento são simulados. Falhas e bloqueios têm exemplos identificados; esses exemplos isolados não comprovam transações ou recuperação de falhas em execução. A edição livre e esses testes funcionais continuam no HTML v0.4.

## Fontes e segurança de execução

- `01-inspecionar.js`: leitura somente; inventário inicial confrontado com o ledger histórico.
- `gerar-modelo.cjs`: gera os descritores intermediários e números fictícios; suas ações condicionais de estudo são descartadas pelo compilador Starter.
- `gerar-roteiros.cjs`: compila os quadros guiados e valida destinos. Executar com Node; produz `modelo.json` e `roteiros.json`, ignorados pelo Git.
- `02-construir.js`: recebe `SPEC` com lote, `offset` e `initialize`; cria apenas quadros ausentes. O primeiro lote rejeita página homônima. Os demais exigem a página/fundações. **Não reexecutar lotes existentes.**
- `03-corrigir-lote-inicial.js`: registro da correção aplicada ao primeiro lote, com IDs reais. O código final de construção já incorpora altura automática e botões locais. **Não reaplicar esse patch.**
- `05-refinar-formatacao.js`: formatação portuguesa com quatro casas na exibição de C*/distâncias e uma nos percentuais; o runtime nativo ignorou as opções de `toLocaleString`. Não altera coeficientes nem pesos.
- `06-dialogos.js`: scrim e superfície do diálogo editáveis dentro do frame de overlay, pois `overlayBackground` é somente leitura na API.
- `08-exportar-evidencias.js`: exportação PNG somente de leitura de quadros preservados/novos, distinta de testar o player.
- `07-auditar.js`: leitura nativa das conexões efetivas, textos numéricos, identidades/origens e limites de geometria.
- `04-conectar.js`: recebe `SPEC` completo e `EXPECTED` da inspeção inicial; conecta somente a página nova e verifica geometria e inventário. Substitui reações dessa página: inspecionar antes de qualquer repetição.

Uma carga inicial excessiva deixou o plugin lento e foi interrompida. A construção passou a usar lotes e JSON como string, evitando análise de um grande literal pelo editor. A inspeção visual do primeiro lote revelou textos com altura fixa de 1 px e largura herdada inadequada nos botões. Foram corrigidos na página nova antes da continuação: altura automática após dimensionamento, rótulos que quebram linha e componentes locais de ação com mínimo de 52 px. O Material 3 importado e seus tokens históricos não foram alterados.

Durante a retomada do plugin, um texto de diagnóstico foi colado no canvas por engano; o nó novo `21:2308` foi removido. O inventário original foi conferido novamente. Isso não foi incorporado aos protótipos.

O registro final de aplicação, a auditoria e os percursos realmente testados devem ser consultados junto às evidências; preparar um script não comprova sua execução, e inspecionar conexões não substitui testar a apresentação.

As tentativas de reação com `overlayRelativePosition` em overlay central foram rejeitadas pela API e corrigidas removendo a posição manual. A navegação para o próprio frame foi eliminada: o card já selecionado permanece indicação estática no roteiro. O ledger final e a auditoria de leitura não registram essas tentativas como conexões válidas.

## Retomada sem falsa alteração pendente

A retomada dedicada de Valores usa estados próprios de seleção, valores e primeiro par AHP, com rótulo de versão salva e saída sem confirmação quando nada mudou. Registrar uma resposta entra novamente na edição pendente. Rascunhos salvos a partir da edição concluída também abrem revisão limpa; descartar um recálculo volta ao **último rascunho salvo**, não à conclusão que já foi substituída.

Retomadas parciais adicionais (por exemplo, qualquer prefixo arbitrário de pares) não são simuladas neste Figma: seus controles estão explicitamente desabilitados. O estado de gravação permanece consultável. Essa limitação pertence ao roteiro; o HTML oferece retomada livre e não houve alteração do requisito. O script `09-retomada-limpa.js` registra a correção dos quadros existentes da página nova, com preflight e atualização de conexões seguida de auditoria. Não reaplicar sobre alterações posteriores sem inspeção.


## Resultado aplicado e verificado

A [auditoria final](auditoria-final.json) corresponde a 598 quadros, 1.681 conexões e sete entradas; contém os IDs reais da página 03 e das ações. `estado-aplicacao-inicial.json` é o primeiro resultado de aplicação (583 quadros), anterior às correções da retomada, e permanece histórico. Não é o inventário atual.

O [parecer v0.5](../../../finalizacao-v05.md) registra os dois percursos completos realmente usados no player, regressões dirigidas, estados/layout visitados e acesso pela janela privativa. Os outros dois cruzamentos têm auditoria estrutural/numérica, sem percurso GUI completo. A coleção, os frames e as reações foram efetivamente aplicados; os scripts são registro/reprodução controlada, não uma fila pendente para execução cega.
