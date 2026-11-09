# Correções finais e prontidão — v0.4

[Índice](README.md) · [Validação e comandos](validacao.md) · [Protótipo local](prototipos/index.html)

**09/11/2026. Status: demonstrador local revisado; Etapa 5 aberta por falta de verificação do Figma. PR deve ser draft.** A branch foi encontrada limpa em `bd055c2`, igual ao remoto, e `origin/main` em `907e3dd`, com a Etapa 4 integrada. Não havia PR existente para a branch no início da rodada. O histórico foi preservado, sem rebase ou force push.

## Achados corrigidos

| Local / cenário | Problema concreto em v0.3 | Aplicação v0.4 e evidência |
|---|---|---|
| T03 → editar pessoal → salvar | O catálogo era atualizado, mas o detalhe ainda exibia uma cópia antiga | Título e valores do detalhe passam a refletir o cadastro confirmado; snapshots salvos continuam intactos. [Captura](prototipos/evidencias/revisao-v04/pessoal-atualizado-360.png). |
| T07 → avançar ou processar com valor ausente em produto abaixo da área inicial | Validação focava conteúdo genérico e retornava ao topo, dificultando achar a correção | Foco e rolagem levam ao primeiro campo inválido, com erro associado e nome acessível por produto/critério/unidade. [Captura completa com foco](prototipos/evidencias/revisao-v04/foco-erro-valores-360.png). |
| T12, posições 1º/2º/2º/4º | Empate intermediário dependia da interpretação dos postos repetidos | Cada card do grupo recebe “Empate · 2 alternativas”, preservando ranking calculado, tolerância e ausência de vencedor fictício. [Captura](prototipos/evidencias/revisao-v04/empate-360.png). |
| D03, confirmar exclusão | O foco inicial podia cair na ação destrutiva; falha só aparecia em toast | Foco inicial em Cancelar, retorno à origem e falha persistente; sucesso limpa o erro anterior. [Captura](prototipos/evidencias/revisao-v04/exclusao-cancelar-360.png). |
| Mensagens e números | Confirmação genérica de cadastro não distinguia edição; arredondamento podia mostrar zero negativo | Feedback contextual de cadastro/edição; normalização somente visual de zero arredondado. Entradas, RC interno e cálculos não mudaram. |
| Teste de abertura em movimento reduzido | Exigia observar T00 antes do primeiro frame, mesmo quando a abertura já tinha encerrado corretamente | Teste aceita a transição imediata com preferência reduzida e abertura única; falhas reais são reportadas antes de conferir preferência. Não se acrescentou espera à UI. |
| DEC-049 e arquitetura | Texto histórico dizia que HTML/manifesto ainda precisavam representar abertura | Registro original mantido e estado posterior explicitado. Seção normativa da decisão preservada; T00 local não é declarado como integração Android ou atualização Figma. |
| Critérios de fechamento | Texto misturava validações futuras com conclusão documental | RNF01 e Android/TalkBack mantêm plano/limites; não são bloqueios artificiais. A pendência real de prototipagem é o Figma verificável. |

A paleta, a hierarquia de títulos/cards e os espaçamentos da v0.3 foram mantidos: não se identificou necessidade de substituí-los para resolver os achados. Os novos rótulos usam pares já conferidos de contraste. Em 360 e texto ampliado há rolagem vertical; não se comprime conteúdo para caber numa captura. A avaliação visual é inspeção técnica do autor desta revisão, não teste humano de usabilidade ou aceite da equipe.

## Fontes e contratos

Os dois PDFs, os documentos obrigatórios e os artefatos da v0.3 foram reconferidos. `chatGPT.md` permanece fonte de estudo com as classificações **[AULA]**, **[MATERIAL]** e **[COMPLEMENTO]** e o mesmo SHA-256 registrado no [parecer](revisao-uiux.md#fontes-e-classificações). Tema, componentes/estado/navegação ensinados orientam UI, sem converter exemplos em regras novas. Splash animada é exigência da p. 4; dois segundos da p. 11 são ilustração. Paleta, duração e organização visual são propostas de design.

DEC-018/025/032/041–049 permanecem normativas. Nenhum coeficiente foi fixado para simular edição livre: `numeros.js` foi preservado e revalidado. Apenas duas categorias fictícias; salvamento manual, edição separada, PDF da versão salva elegível e bloqueios de representação/compatibilidade continuam demonstráveis. Não se implementaram Kotlin, Navigation Compose, Room, Firebase ou PDF real.

## Figma: evidência e intervenção

O [arquivo autoral](https://www.figma.com/design/S1Ba7mzFVfE1d0ds21FR36) continua sem acesso funcional nesta execução. Uma sessão foi criada; `use_figma` e `get_metadata` retornaram limite Starter, e a ferramenta web não abriu a URL. `whoami` confirmou o plano, sem comprovar permissão de edição do arquivo. [Registro das tentativas](prototipos/figma/verificacao-2026-11-09.json).

Não foi possível conferir IDs atuais, alterações posteriores da equipe, componentes, conexões, movimento, apresentação ou conta do avaliador. Nenhum script 01–05 foi executado e nenhum plugin local foi instalado/executado. Os 84 frames são apenas referência histórica. [Checklist concreto de intervenção humana](prototipos/figma/retomada-v04.md).

## Decisão de prontidão

O HTML revisado pode ser demonstrado, com dados fictícios e armazenamento/PDF simulados claramente identificados. A [validação](validacao.md) reúne comandos e limites; as capturas v0.3 foram preservadas e a rodada atual tem evidências próprias.

A Etapa 5 **não está concluída** enquanto faltar inspeção/correção e teste de apresentação/acesso do Figma. Abrir PR draft permite revisar o trabalho independente já pronto sem afirmar essa validação. Execução de RNF01 com participantes, RNF02 no Android/TalkBack e integração do aplicativo não são pré-condições adicionais de fechamento documental; seus planos e critérios continuam vigentes. Aceite da equipe/professor, vídeo final e envio ao Teams não são presumidos.
