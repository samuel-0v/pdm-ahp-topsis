# Casos de uso — Persistência e gerenciamento

**Versão:** 1.1 — UC01–UC15 aceitos; estados de avaliação alinhados às DEC-033–DEC-035.

Base: RF01–RF26, RNF01–RNF13 e DEC-001–DEC-035 aprovados. Os casos de uso não representam implementação.

[Índice dos casos de uso](README.md) · [Rastreabilidade](../rastreabilidade.md)

## UC12 — Salvar avaliação e tratar saída com alterações

**Objetivo:** preservar explicitamente um rascunho ou uma avaliação concluída com resultado válido, inclusive sem diferenciação.
**Ator principal:** usuário.
**Gatilho:** acionar salvar ou tentar sair com alterações não salvas.
**Pré-condição:** avaliação criada com categoria válida.
**Requisitos:** RF21, RF24, RF25; DEC-025; RNF01, RNF05, RNF06, RNF08, RNF10.

**Fluxo principal:**

1. O usuário solicita salvar rascunho ou avaliação processada.
2. O sistema reúne configuração, cópias dos dados necessários, valores e julgamentos disponíveis; inclui indicadores/resultados somente quando válidos e correspondentes à configuração, mesmo que C* esteja indefinido por ausência de diferenciação.
3. Persiste o conjunto de forma atômica.
4. Se entradas de cálculo foram alteradas sem novo resultado válido, invalida o resultado persistido anterior na mesma operação.
5. Somente após concluir a gravação, confirma sucesso e considera a edição salva.

**Alternativas e exceções:**

- A1 — Rascunho com menos de dois produtos, campos ausentes ou AHP incompleto/inconsistente: permitir salvamento dos dados disponíveis, sem declarar avaliação concluída.
- A2 — Saída com alterações: oferecer Salvar, Descartar alterações ou Continuar editando. Salvar executa este fluxo; descartar mantém a última versão persistida (ou abandona a nova avaliação não salva); continuar mantém a edição.
- A3 — Falha na gravação: não confirmar salvamento, preservar última versão salva e informar falha; a escolha de salvar na saída não pode ser representada como sucesso.
- A4 — Encerramento inesperado antes de salvar: mudanças não salvas podem se perder, conforme DEC-025.
- A5 — Nenhuma mudança pendente: a regra de aviso por alterações não salvas não exige confirmação de saída.

**Pós-condição de sucesso:** última versão salva íntegra e disponível localmente; catálogos preservados. Não há histórico de versões.
**Verificação:** salvar e reabrir rascunho e resultado, simular falha entre gravações e verificar que a versão anterior permanece íntegra. O salvamento de resultado já decorre de RF24/RF25 e DEC-025; não cria novo RF.

## UC13 — Consultar e retomar avaliações salvas

**Objetivo:** localizar uma avaliação salva e retomar um rascunho ou consultar um resultado concluído.
**Ator principal:** usuário.
**Gatilho:** abrir avaliações salvas e selecionar registro.
**Pré-condição:** aplicativo aberto; existência de registros verificada na consulta.
**Requisitos:** RF22, RF23, RF25; RNF01, RNF02, RNF05, RNF06, RNF08.

**Fluxo principal:**

1. O usuário acessa avaliações salvas.
2. O sistema consulta o armazenamento local e apresenta identificação, categoria e situação, incluindo “Concluída — sem diferenciação” quando aplicável (DEC-035).
3. O usuário seleciona um registro.
4. Para rascunho, o sistema restaura categoria, alternativas, critérios, valores e julgamentos da última gravação; para concluída, abre o resultado válido, incluindo estado sem diferenciação.
5. O usuário continua a edição de rascunho ou consulta/exporta resultado pelos casos de uso pertinentes, com categoria fixa.

**Alternativas e exceções:**

- A1 — Seleção de avaliação concluída: abrir resultado válido pelo UC11 ou editar pelo UC14; o estado sem diferenciação é resultado concluído consultável e exportável.
- A2 — Lista vazia: informar ausência de avaliações salvas.
- A3 — Registro inexistente ou falha de leitura: informar indisponibilidade.
- A4 — Dados incoerentes: impedir cálculo inválido e informar erro; não substituir silenciosamente dados pelo catálogo atual.
- A5 — Catálogo atualizado ou produto removido: manter dados da avaliação; consulta e recálculo com cópias completas funcionam offline.

**Pós-condição:** última versão efetivamente salva carregada, sem representar alterações apenas em memória como persistidas.
**Verificação:** reiniciar aplicativo e retomar rascunho parcial; modificar/excluir produto de origem e reabrir avaliação offline.

## UC14 — Editar e recalcular avaliação concluída

**Objetivo:** atualizar a comparação sem alterar a categoria ou manter histórico de resultados.
**Ator principal:** usuário.
**Gatilho:** escolher editar avaliação concluída.
**Pré-condição:** avaliação concluída acessível localmente.
**Requisitos:** RF11, RF12, RF14, RF17, RF18, RF24, RF25; RNF04, RNF05, RNF06, RNF10.

**Fluxo principal:**

1. O sistema carrega a versão salva para edição, preservando o registro persistido.
2. O usuário altera alternativas, valores ou julgamentos.
3. O sistema indica que o resultado anterior não corresponde à edição atual; mudanças nos julgamentos também invalidam pesos e consistência atuais.
4. O usuário corrige e valida os dados pelos UC08–UC10 e recalcula pelo UC11.
5. Solicita salvar pelo UC12; configuração e novo resultado válido substituem a versão anterior de forma atômica.

**Alternativas e exceções:**

- A1 — Salvar antes do recálculo: persistir configuração como rascunho e invalidar resultado anterior na mesma transação.
- A2 — Descartar edição: manter/restaurar versão salva e seu resultado válido.
- A3 — Saída com mudanças: aplicar as três opções do UC12.
- A4 — RC inconsistente, validação ou cálculo com falha: não publicar novo resultado válido; permitir correção ou rascunho.
- A5 — Falha de salvamento: manter a última versão persistida íntegra e informar falha.
- A6 — Mudança de categoria pretendida: iniciar outra avaliação pelo UC06.

**Pós-condição:** edição recalculada e, somente após sucesso ao salvar, nova versão persistida; sem histórico de resultados.
**Verificação:** alterar e descartar; alterar e salvar sem recalcular; recalcular e salvar; simular falha de gravação em cada caso relevante.

## UC15 — Excluir avaliação salva

**Objetivo:** remover definitivamente rascunho ou avaliação concluída.
**Ator principal:** usuário.
**Gatilho:** solicitar exclusão de avaliação salva.
**Pré-condição:** avaliação persistida existente.
**Requisitos:** RF26; RNF01, RNF05, RNF06, RNF08, RNF10.

**Fluxo principal:**

1. O usuário seleciona avaliação e solicita exclusão.
2. O sistema pede confirmação explícita.
3. O usuário confirma.
4. O sistema exclui avaliação e dados/resultados vinculados numa operação atômica.
5. Atualiza a lista e confirma sucesso após a conclusão.

**Alternativas e exceções:**

- A1 — Cancelamento: preservar integralmente avaliação e dados.
- A2 — Falha na exclusão: não confirmar sucesso nem deixar exclusão parcial.
- A3 — Registro já inexistente: informar indisponibilidade.

**Pós-condição de sucesso:** avaliação removida permanentemente, sem afetar produtos dos catálogos; sem lixeira ou restauração na interface.
**Verificação:** excluir rascunho e concluída, cancelar confirmação, provocar falha de exclusão e conferir integridade; reiniciar e verificar ausência após sucesso.
