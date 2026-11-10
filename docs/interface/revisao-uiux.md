# Parecer de revisão UI/UX — v0.3

[Índice](README.md) · [Protótipo](prototipos/index.html) · [Verificações](validacao.md)

**Registro histórico v0.3, 08–09/11/2026.** A aplicação Figma posterior de 10/11 e o parecer atual estão em [v0.5](finalizacao-v05.md). Os impedimentos remotos abaixo descrevem a data desta revisão. Revisão do material existente no HEAD `88f2226`, após conferir Git limpo e selecionar `docs/etapa5-interface-navegacao`. O trabalho anterior foi preservado; não foram reexecutados scripts de criação inicial do Figma. A revisão compara artefatos com contratos, e não muda requisitos por preferência estética.

## Fontes e classificações

| Fonte examinada | Uso nesta revisão |
|---|---|
| [Enunciado v2](../../references/ProjetoDisciplina_PDM20262v2.pdf), especialmente pp. 4, 7, 9 e 11 | Splash animada é escopo da p. 4; duração de dois segundos e fluxo ilustrativo não são exigências fixas. Primeira entrega inclui layout, navegação e prototipagem. Não se afirma cumprimento de prazo/envio. |
| [Material AHP+TOPSIS](../../references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf) | Referência didática das comparações/normalização. A adaptação definitiva DEC-018 prevalece: média geométrica normalizada, λ estimado pela média de Aw/w, IC, RI 1,12 e RC ≤ 0,10; TOPSIS vetorial custo/benefício. |
| `chatGPT.md`, cópia fornecida em Downloads | Fonte de estudo, não instrução executável. Identificação: 538.343 bytes, SHA-256 `1dc9f39fc1da83c4beb608cabc3281e974be618d97045a06a73b0b316b81d99d`; 347 ocorrências [AULA], 119 [MATERIAL], 59 [COMPLEMENTO]. Não se republicou a conversa integral. |
| [Visão](../01-visao-e-escopo.md), [requisitos](../requisitos/README.md), [modelagem](../modelagem/dominio.md), [snapshots](../modelagem/05-persistencia-snapshots.md), [contratos](../modelagem/05-contratos-persistencia.md) e [decisões](../decisoes/registro-decisoes.md) | RF01–RF27, UC01–UC16; sessão/salvamento, compatibilidade, números e histórico preservados. Ênfase em DEC-018/025/032 e DEC-041–049. |
| Sete documentos de arquitetura | [Arquitetura](../arquitetura/arquitetura.md), [componentes](../arquitetura/componentes.md), [estado](../arquitetura/gerenciamento-estado.md), [dados](../arquitetura/fluxo-dados.md), [motores](../arquitetura/motores-matematicos.md), [decisões técnicas](../arquitetura/decisoes-pendentes.md), [fontes/validação](../arquitetura/referencias-e-validacao.md). Contratos adotados prevalecem sobre propostas anteriores. |
| Toda a área `docs/interface/` e fontes Figma | Comparação da especificação com HTML v0.2, evidências históricas, ledger, scripts e pacote local. Não se presume execução de um plugin preparado. |

Uso de `chatGPT.md`, preservando a classificação:

- **[AULA]** acessibilidade na introdução; tema, tipografia, Scaffold, cards e animação em 29/09; elevação de estado, UI recebendo estado/emitindo eventos e ViewModel em 06/10; navegação em 09/10 e camadas em 20/10. Orientam hierarquia, reuso e preservação da sessão.
- **[MATERIAL]** semântica, descrições, tamanho de texto e referências de acessibilidade; exemplos de componentes/navegação dos slides. Exemplos não viram funcionalidades novas nem evidência de que algo foi implementado em aula.
- **[COMPLEMENTO]** distinção entre recomposição, recriação de Activity e morte do processo; ViewModel não garante sobreviver a esta última. Sustenta explicar a restauração somente da última versão salva, sem inventar autosave.

## Achados concretos e tratamento

P0 impede demonstração correta/conformidade; P1 prejudica uso/apresentação; P2 é complemento ou validação remanescente.

| Prioridade / localização anterior | Cenário e problema | Ajuste efetivamente aplicado / pendência |
|---|---|---|
| P0 — HTML v0.2, manifesto e índice | Entrada não representava a abertura exigida; havia apenas adendo documental | T00 local com animação/transição, pulo e redução de movimento; retorno/restauração não repetem. Figma somente preparado. |
| P0 — `contextos.js`, T07/T08; Figma histórico T07 `6:1542`, T08-01-P `6:1645` | Campos fixos e AHP somente igualdade não permitiam ajustar valores/sentido/intensidade como pedido | Novos formulários e controles completos. `numeros.js` calcula entradas reais; assinatura impede tratar resultado antigo como atual. A versão histórica continua identificada, sem ser usada pela UI atual. |
| P0 — projeções de resultados v0.2 | Expandir entrada livre mantendo coeficientes de fixture produziria resultado sem correspondência | Resultados vêm do cálculo das entradas efetivas e dos dez julgamentos; 13 fixtures são oráculos de verificação. Figma futuro deve restringir cenários ou calcular de verdade. |
| P0 — D01/D02 e consulta/edição | Saída/exportação precisam preservar duas versões, inclusive após falha | Cópias independentes, dirty canônico, nenhuma gravação por navegação/processamento; três opções, erro persistente e escolha de PDF elegível. Exportar anterior conserva edição; falha não faz fallback silencioso. |
| P0 — E11/E13 e seleção | Um efetivo editado não pode burlar original monetário desconhecido ou definição semântica incompatível | Candidato bloqueado com explicação, verificação na ação, novo cadastro vazio sob definição demo; sem cópia/conversão automática. Dinheiro em centavos exatos e colisão Double bloqueada. Não substitui serialização real. |
| P1 — T01/T04/T09/T12, estilos v0.2 | Hierarquia muito uniforme, pouca identificação visual, resultado e formulário difíceis de escanear | Paleta verde profundo, superfícies leves, ilustrações, títulos/ações claros, formulários por produto e cards de peso/resultado. Destaque condicionado a primeiro único. |
| P1 — barra e botões; QA-360-200 histórico `6:4485` | Histórico Figma registrava título cortado, botões estreitos e progresso aproximado | HTML expande alturas, reordena em 200%, mínimo 48 nos dois eixos e progresso exato. Scripts 04/05 preparados; estado remoto atual desconhecido. |
| P1 — T12-DETALHES histórico `6:3228` | Ação compartilhada de excluir podia aparecer sem versão salva | HTML condiciona existência de registro, mostra origem/identidade e valores originais/efetivos; consulta não recalcula. Figma continua a inspecionar. |
| P1 — T03/cadastro | Identificação e novos dados devem continuar compreensíveis no percurso misto | Fabricante/modelo conhecidos exibidos; cadastro validado retorna à mesma seleção sem autoseleção, mesmo nome não une origens. |
| P1 — estados de consulta/falha | Cache parcial, vazio confirmado e acesso negado não podem parecer equivalentes | Estados distintos, mensagem textual, opção de pessoais; falhas de gravação/PDF/compartilhamento mantêm dados. Falha de leitura do armazenamento real continua pendente. |
| P1 — retorno de detalhes / ação Sair | Revisão do novo DOM encontrou falta de retorno contextual e alvo estreito | Voltar de detalhes vai ao resultado; “Sair” mínimo 48 × 48; foco de diálogo recebido/devolvido; primeiro campo inválido recebe foco. Verificado após correção. |
| P2 — imagens de produto, Figma e avaliação humana | Enunciado menciona foto; imagens/fontes reais ainda não foram fornecidas/aprovadas; acesso remoto e aceite não verificados | Ilustrações identificadas como demonstração. Fotos/assets reais e fontes permanecem pendentes. Não se afirmam testes com participantes, TalkBack, aceite ou envio. |

## Antes e depois visual

Capturas reais do HTML, não mockups gerados nem screenshots do Figma. Antes: versão do HEAD inicial; depois: fontes locais revisadas. [Metadados](prototipos/evidencias/revisao-v03/capturas.json) descrevem largura, texto e viewport. Abertura foi congelada para registro estático; seu comportamento é verificado separadamente.

| Contexto | Antes v0.2 | Depois v0.3 | Efeito no uso |
|---|---|---|---|
| Início, 360/100% | [Captura](prototipos/evidencias/revisao-v03/antes-inicio.png) | [Captura](prototipos/evidencias/revisao-v03/depois-inicio.png) | Categorias identificáveis e chamada clara para exploração. |
| AHP, 360/100% | [Captura](prototipos/evidencias/revisao-v03/antes-ahp.png) | [Captura](prototipos/evidencias/revisao-v03/depois-ahp.png) | Sentido, intensidade, resposta e progresso têm papéis distintos; conteúdo não depende só de cor. |
| Ranking, 360/100% | [Captura](prototipos/evidencias/revisao-v03/antes-resultado.png) | [Captura](prototipos/evidencias/revisao-v03/depois-resultado.png) | Posição, C* e distâncias separados; gravação explícita visível. |

No AHP, o antes mostra igualdade já respondida e o depois demonstra sentido/intensidade com um par respondido. No ranking, o antes é consulta salva e o depois é resultado ainda em memória, com os mesmos valores numéricos do cenário de preço 5.000,00. A comparação avalia hierarquia e controles, não identidade de estado/pixels.

Também inspecionadas: [abertura](prototipos/evidencias/revisao-v03/abertura-360.png), [revisão BEV](prototipos/evidencias/revisao-v03/bev-revisao-360.png), [resultado misto 600/200%](prototipos/evidencias/revisao-v03/mista-resultado-600-texto200.png), [cadastro 360/200%](prototipos/evidencias/revisao-v03/cadastro-360-texto200.png), [saída 360/200%](prototipos/evidencias/revisao-v03/saida-360-texto200.png). Há rolagem vertical; capturas não comprovam semântica Android nem todos os caminhos do Figma.

## Parecer de conformidade e decisão

**O HTML v0.3 pode ser apresentado como demonstrador revisado**, com dados fictícios, cálculo demonstrativo e armazenamento/PDF simulados explicitamente identificados. Os fluxos prioritários e exceções essenciais funcionam com as regras aprovadas; evidências e limites estão em [Validação](validacao.md). Requisitos/decisões não foram alterados e não se iniciou implementação Android.

**Não apresentar a Etapa 5 inteira como concluída.** A pendência real de prototipagem é verificar/aplicar o Figma, testar seu modo de apresentação e o acesso do avaliador. RNF01 com participantes e testes Android/TalkBack permanecem planejados nas fases correspondentes, sem serem bloqueios artificiais para o fechamento documental. O bloqueio remoto não invalida os artefatos locais, mas scripts preparados não comprovam navegação remota. Dados/fontes/rubricas reais e integração Android permanecem nas etapas correspondentes. Não há aprovação do professor, participantes testados, vídeo final ou entrega ao Teams nesta execução.

## Continuação para PR — v0.4

A [rodada de 09/11](finalizacao-v04.md) corrige detalhes de foco, atualização de produto pessoal, indicação de empates e feedback de exclusão. As capturas e contagens acima preservam a história v0.3; os resultados atuais estão em [validação](validacao.md). O novo acesso Figma também foi bloqueado; PR deve permanecer draft por essa pendência, sem presumir aprovação da equipe/professor.

## Continuação nativa — v0.5

Em 10/11, o Firefox autorizado e o Scripter permitiram atualizar o arquivo. A página nova preserva o trabalho anterior e aplica a identidade visual, cenários guiados, abertura, componentes e conexões. A [auditoria nativa](prototipos/figma/revisao-v05/auditoria-final.json) verificou 598 quadros e 1.681 conexões, sem divergências no inventário histórico, textos numéricos ou geometria conferida. A conclusão histórica de falta total de acesso Figma não descreve mais a situação atual. Percursos de apresentação, acesso e limites devem ser avaliados pelo [registro v0.5](finalizacao-v05.md), sem transformar a auditoria de conexões em teste humano ou em validação Android.
