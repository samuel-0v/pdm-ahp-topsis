# Revisão de consolidação da Etapa 2

**Versão:** 1.0 — consolidação documental das decisões aprovadas.
**Data:** 02/11/2026.
**Escopo:** casos de uso, rastreabilidade, plano de verificação e resoluções P01–P03. Não inclui implementação, modelagem final nem execução de testes.

[Requisitos](README.md) · [Casos de uso](casos-de-uso/README.md) · [Matriz](rastreabilidade.md) · [Plano de verificação](plano-verificacao.md)

## Resultado da revisão

RF01–RF26 e RNF01–RNF13 estão validados; RF27 e UC16 registram a exportação aprovada na DEC-032. Os UC01–UC15 foram aceitos pelo usuário. As questões P01–P03 foram resolvidas pelas DEC-033–DEC-035, sem redefinir as decisões anteriores. A matriz e o plano foram alinhados a essas resoluções.

O detalhamento de schema, domínios numéricos restantes e representação dos estados segue para modelagem e preparação dos dados, conforme aprovado. Isso não reabre P01–P03. A consolidação não comprova conformidade do aplicativo em execução nem conclusão de todos os entregáveis acadêmicos.

## Fontes examinadas

- Visão e Escopo, registro DEC-001–DEC-035, sete grupos de RFs e seis grupos de RNFs.
- Casos de uso, índices, questões de arquitetura e fluxo inicial de navegação.
- Os dois PDFs do professor estão presentes em `references/`. Esta consolidação utiliza a referência e a divergência metodológica já registradas na DEC-018; não representa nova auditoria integral dos PDFs nem confirmação externa das adaptações.

## P01 — Domínio dos valores de entrada

**Situação:** resolvido pela DEC-033.

**Questão original:** definir a origem e a aplicação reutilizável das regras dos critérios, incluindo tipo, unidade, limites e ausência.

**Decisão:** as definições oficiais no Firestore contêm metadados genéricos de validação. O aplicativo aplica essas regras sem lógica específica por categoria e usa as definições disponíveis em cache offline. Produtos pessoais permanecem locais; avaliações preservam metadados e valores usados.

**Consequências:** RF04, RF06/RF07 e RF13 aplicam leitura e validação por metadados; RF25 exige preservação das definições utilizadas. Valores ausentes e inválidos continuam impedindo processamento, sem impedir rascunhos incompletos. Notas de câmeras/ADAS continuam entre 1 e 10.

**Encaminhamento aprovado:** schema, identificadores/versionamento e limites numéricos ainda não definidos serão detalhados na modelagem e preparação dos dados. Nenhum limite novo foi criado nesta revisão. Não é condição para reabrir o P01.

## P02 — Produtos distintos entre catálogos

**Situação:** resolvido pela DEC-034.

**Questão original:** distinguir repetição de registro de equivalência física entre produtos.

**Decisão:** identidade pelo par **origem + ID do registro**. Não permitir duas seleções da mesma identidade. Não deduplicar automaticamente por nome, fabricante, modelo ou versão.

**Consequências:**

- O mínimo de duas alternativas significa duas identidades distintas, da mesma categoria e com dados válidos.
- IDs iguais em origens diferentes não representam a mesma identidade.
- Registros diferentes podem coexistir mesmo descrevendo o mesmo produto físico, inclusive dentro de uma mesma origem.
- A avaliação preserva origem, ID e cópia dos dados; editar nome ou valores não muda a identidade do cadastro.
- Não se promete reconhecimento de equivalência física. Uma mudança futura dessa regra precisará de nova decisão.

RF11/RF13 e UC07 explicitam a regra. VT03/VT04 cobrem repetição de identidade, registros de origens diferentes e coincidência de nomes/modelos. Modelagem detalhará a representação dos identificadores sem alterar a regra aprovada.

## P03 — Persistência do caso sem diferenciação

**Situação:** resolvido pela DEC-035, em continuidade às DEC-024 e DEC-032.

**Questão original:** classificar o processamento válido sem diferenciação, distinguindo-o de rascunho e erro matemático.

**Decisão:** permitir salvamento como avaliação concluída e exibir **“Concluída — sem diferenciação”**. Preservar os indicadores calculáveis, sem inventar C*, posição ou vencedor.

**Consequências:**

- Salvamento continua manual; o estado só aparece como salvo depois da gravação bem-sucedida.
- Avaliação pode ser reaberta, editada, recalculada, excluída e exportada conforme os fluxos aprovados.
- Edição e invalidação obedecem à mesma distinção entre estado em memória e versão persistida da DEC-025.
- A modelagem deve permitir conclusão sem coeficiente/ranking definido.
- Coluna sem normalização definida permanece erro de validação; não é convertida em conclusão sem diferenciação.

RF20/RF22, UC11–UC13 e RNF05 refletem essa distinção. VT07 verifica o resultado matemático; VT08 verifica salvar/reabrir e VT17 verifica exportar o estado sem diferenciação.

## Ajustes finais realizados

- Atualizados os índices e as bases dos casos de uso para DEC-001–DEC-035.
- Explicado o mínimo de alternativas por identidade de registro nos requisitos e fluxos de seleção/validação.
- Completada a rastreabilidade de metadados, identidade e conclusão sem diferenciação.
- Acrescentadas verificações explícitas de identidade, preservação de metadados e salvamento/reabertura sem diferenciação.
- Esclarecidas invalidação em memória, invalidação persistida e substituição do resultado no registro central.
- Removidas pendências obsoletas sobre especificação dos RNFs e definição da política de perda de alterações não salvas.
- Corrigidas referências matemáticas e rótulos antigos de links. O diagrama de navegação continua um fluxo inicial, a detalhar com o protótipo.

## Encaminhamento para as próximas etapas

| Etapa | Trabalho restante |
|---|---|
| Preparação dos dados | Modelos reais, fontes/datas, domínios numéricos restantes, rubricas de câmeras/ADAS, autonomia de smartphones, perfil de seguro e condições de recarga |
| Modelagem | Entidades, identidade por origem/ID, cópias dos dados e metadados, relações e representação dos estados aprovados |
| Arquitetura | Contratos Room/Firestore, dependências, versões Android, navegação, backup, concorrência e erros |
| Testes | Referências matemáticas, ferramentas, massas de dados, dispositivo de desempenho e procedimento de aquecimento |
| Gestão | Configuração efetiva do GitHub Projects, revisão/integração conforme DEC-031 e tags das entregas reais |
| Disciplina | Conferir entregáveis exigidos e confirmar adaptações com o professor quando necessário; não há confirmação externa registrada aqui |

## Histórico

- UC01–UC15 aceitos pelo usuário após o rascunho inicial.
- DEC-032: exportação PDF; RF27, UC16 e VT17 acrescentados; DEC-011 e Visão e Escopo revisados explicitamente.
- DEC-033: P01 resolvido por metadados genéricos, com detalhamento numérico/schema encaminhado às próximas etapas.
- DEC-034: P02 resolvido por identidade do registro, sem equivalência física automática.
- DEC-035: P03 resolvido por conclusão válida sem diferenciação e rótulo explícito.
- Ajustes finais e commit documental autorizados pelo usuário. Nenhum teste do aplicativo é declarado executado.
