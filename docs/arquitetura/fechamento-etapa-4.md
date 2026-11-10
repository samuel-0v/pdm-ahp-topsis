# Fechamento da Etapa 4 — Arquitetura do aplicativo

**Data:** 10/11/2026.

**Situação:** Etapa 4 concluída documentalmente após conferência final solicitada pelo usuário. A arquitetura de referência é a v0.4, integrada à Etapa 5 v0.5 aprovada.

[Índice da arquitetura](arquitetura.md) · [Fontes e validação](referencias-e-validacao.md) · [Decisões adotadas](decisoes-pendentes.md) · [Interface aprovada](../interface/finalizacao-v05.md#aprovação-e-fechamento-documental)

## Alcance e autorização

O usuário solicitou em 10/11/2026: “faça conferencia e se tudo certo comfirme o fechamento commit na main”. A conferência abaixo não encontrou impedimento contratual ou lacuna documental que impeça o **fechamento documental** da Etapa 4. As escolhas DEC-043–DEC-048 haviam sido adotadas por delegação; a DEC-049 corrigiu a abertura animada. Este registro fecha a etapa após a integração da interface, sem atribuir aprovação ao professor ou a outros integrantes.

O fechamento abrange os sete documentos de `docs/arquitetura/` da v0.4 e seu alinhamento documental com a Etapa 5 v0.5. A implementação Android, a demonstração acadêmica e a entrega ao professor têm verificação própria.

## Resultado da conferência

| Item do [checklist final](referencias-e-validacao.md#checklist-de-revisão-independente-e-pendências) | Resultado documental |
|---|---|
| Camadas e dependências | Presentation, Domain e Data representam as quatro responsabilidades do enunciado. O Domain é especificado sem dependência de Android, Compose, Room ou Firebase; setas de código e chamadas em execução são distinguidas. |
| Componentes e proporcionalidade | RF01–RF27/UC01–UC16 têm responsáveis no mapa de componentes. Casos de uso concentram regras e orquestração; consultas simples usam contratos Domain. O módulo Android único e a injeção manual não são apresentados como obrigação do professor. |
| Sessão, navegação e concorrência | `AvaliacaoViewModel` pertence à entrada AndroidX do grafo de avaliação; cadastro pessoal usa entrada filha/VM próprio e retorna à seleção sem sobrescrever a edição. Voltar, saída, descarte, cancelamento e respostas tardias têm tratamento explícito. `SavedStateHandle` não promete restaurar edição não salva após morte do processo. Navegação/recomposição não salvam. |
| Dados e contratos DEC-041/042 | Mantidas seis tabelas, snapshots independentes, BRL em `Long` centavos, original/efetivo separados e correção numérica sob significado equivalente. Incompatibilidade semântica exige novo cadastro/UUID; original conhecido fora de domínio, ausência real e representação incompatível recebem tratamentos diferentes. Efetivo válido não contorna bloqueio do formato original incompatível. |
| Motores | Média geométrica normalizada segue DEC-018 e o PDF AHP+TOPSIS; TOPSIS requer AHP vigente consistente. Benefício/custo, empates, posições e sem diferenciação são contratados. Limitações de `AhpTopsis.kt` estão identificadas como tarefas de implementação. |
| Persistência e PDF | Firestore/cache, Room e snapshots históricos permanecem separados. Salvamento é explícito e atômico; consulta/exportação usam a versão salva sem recálculo silencioso. As três opções de PDF diante de edição pendente, elegibilidade e preservação do salvamento confirmado em falha de geração constam do fluxo. |
| Interface | A Etapa 5 v0.5 aprovada registra abertura T00, retorno do cadastro à seleção, estados, diálogos e limites dos roteiros Figma/HTML. IDs de tela não são rotas Kotlin nem prova da execução Android. |

Foram conferidos nesta rodada os dois PDFs de `references/`, as anotações `/home/samuel/Downloads/chatGPT.md`, DEC-018/041/042/043–049, contratos definitivos de valores e compatibilidade, os sete documentos arquiteturais e o fechamento da Etapa 5. As marcações **[AULA]**, **[MATERIAL]** e **[COMPLEMENTO]** das anotações mantêm seus alcances; decisões de biblioteca e escopo são julgamento do projeto quando o enunciado não as impõe.

As verificações automáticas de links/âncoras, manifesto, sintaxe e SVG e a conferência de `git diff --check` desta rodada são registradas no [histórico de validação](referencias-e-validacao.md#conferência-final-e-fechamento-documental--10112026). Resultados anteriores de renderização Mermaid e testes do demonstrador pertencem às respectivas rodadas documentadas, sem alegar nova execução aqui.

## Dependências após o fechamento

- Implementar e testar Android/Compose, Room, Firestore, navegação, PDF e concorrência conforme os contratos e o [plano de verificação](../requisitos/plano-verificacao.md).
- Detalhar schema/migrations/CHECK canônico, serialização, versões de dependências, domínios/fontes/rubricas/precisão e adequações numéricas do motor antes de usar dados reais, nos momentos previstos pela Etapa 3.
- Consolidar diagramas e material da Fase 1, gravar/verificar o vídeo e enviar os artefatos na atividade correta. O fechamento documental não comprova essas ações nem aceite do professor.
