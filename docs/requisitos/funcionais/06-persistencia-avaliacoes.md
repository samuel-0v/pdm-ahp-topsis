# Persistência e gerenciamento de avaliações

**Situação:** requisitos funcionais validados.

[Índice de requisitos](../README.md) · [Decisões](../../decisoes/registro-decisoes.md)

### RF21 — Salvar avaliação como rascunho
**Descrição:** O sistema deve permitir salvar manualmente uma avaliação incompleta e preservar seus dados disponíveis no banco local.

**Ator principal:** Usuário.

**Pré-condições:**

Avaliação criada com categoria válida.
**Fluxo principal:**

Usuário edita a avaliação.
Aciona “Salvar rascunho” ou escolhe “Salvar” no aviso de saída.
Sistema persiste, de forma consistente, os dados preenchidos até então.
Após confirmação da gravação, informa sucesso e mantém estado de rascunho.
**Fluxos alternativos e exceções:**

Menos de dois produtos ou comparações AHP incompletas: permitir rascunho.
Falha no Room: informar que não salvou, sem afirmar sucesso.
Saída com alterações pendentes: apresentar opções salvar, descartar ou continuar.
Encerramento inesperado sem salvamento: alterações não salvas podem se perder.
**Pós-condição:** Última versão salva do rascunho disponível localmente.

**Critérios de aceitação:**

Rascunhos incompletos são aceitos.
Dados persistem após reiniciar app.
Nenhuma internet é necessária.
Confirmação de sucesso somente após conclusão da gravação.
Não há salvamento automático de formulários.
**Rastreabilidade:** DEC-004, DEC-006, DEC-022, DEC-025.

### RF22 — Consultar avaliações salvas
**Descrição:** O sistema deve listar avaliações efetivamente persistidas, distinguindo rascunhos de avaliações concluídas.

**Ator principal:** Usuário.

**Pré-condições:**

Área de avaliações salvas acessada.
**Fluxo principal:**

Usuário abre a listagem.
Sistema consulta dados do Room.
Apresenta avaliações salvas, com informações de identificação, categoria e situação.
Usuário seleciona um registro para continuar ou consultar.
**Fluxos alternativos e exceções:**

Lista vazia: apresentar estado vazio.
Erro de leitura: informar indisponibilidade.
Avaliação excluída: não apresentar.
Alterações em memória ainda não salvas: não tratá-las como versão persistida.
**Pós-condição:** Avaliações persistidas visíveis ao usuário.

**Critérios de aceitação:**

Rascunhos e concluídas são identificáveis.
Seleção permite abrir avaliação individual.
Listagem funciona offline e após reinício.
Somente dados efetivamente persistidos são mostrados como salvos.
**Rastreabilidade:** DEC-004, DEC-006, DEC-019, DEC-025.

### RF23 — Retomar avaliação salva
**Descrição:** O sistema deve restaurar a última versão persistida de um rascunho, preservando categoria, alternativas, valores e comparações já preenchidos.

**Ator principal:** Usuário.

**Pré-condições:**

Avaliação salva existente e não excluída.
**Fluxo principal:**

Usuário seleciona rascunho na listagem.
Sistema carrega a última versão salva.
Restaura categoria, seleção de alternativas, valores ajustados e julgamentos AHP.
Usuário retorna ao processo de avaliação e continua do estado persistido.
**Fluxos alternativos e exceções:**

Registro inexistente: informar que não está disponível.
Dados persistidos incoerentes: evitar cálculo e informar erro.
Falha de leitura: informar problema.
**Pós-condição:** Rascunho aberto com os dados da última gravação concluída.

**Critérios de aceitação:**

A categoria não pode ser alterada.
Valores e comparações previamente salvos são restaurados.
Matriz incompleta permanece incompleta.
Retomada funciona offline e não depende do catálogo remoto atual.
**Rastreabilidade:** DEC-004, DEC-006, DEC-022, DEC-025.

### RF24 — Editar e recalcular avaliação concluída
**Descrição:** O sistema deve permitir editar uma avaliação concluída, invalidar resultados afetados por alterações salvas e gerar novos resultados mediante recálculo.

**Ator principal:** Usuário.

**Pré-condições:**

Avaliação concluída existente e acessível localmente.
**Fluxo principal:**

Usuário abre avaliação e escolhe editar.
Sistema carrega seus dados salvos em estado de edição, preservando a versão persistida.
Usuário modifica alternativas, valores ou comparações AHP.
Sistema indica que o resultado anterior não corresponde à edição atual.
Usuário pode salvar alterações; nesse momento, o resultado persistido anterior é invalidado se os dados de cálculo mudaram.
Sistema exige validações pertinentes, novo AHP quando necessário e novo TOPSIS.
Usuário processa e salva explicitamente o novo resultado, que substitui o resultado anterior.
**Fluxos alternativos e exceções:**

Usuário descarta edição: restaurar versão salva e seu resultado válido.
Saída com mudanças: oferecer salvar, descartar ou continuar.
Erro de validação/RC inconsistente: impedir novo TOPSIS.
Falha no recálculo: não publicar resultado inválido.
Erro ao salvar: manter estado anterior persistido íntegro e informar falha.
**Pós-condição:** Última configuração salva e, após novo salvamento, único resultado atual válido.

**Critérios de aceitação:**

Categoria permanece fixa.
Alterações não salvas não sobrescrevem registros persistidos.
Resultado anterior é invalidado ao salvar mudanças relevantes.
Novo resultado usa dados atuais e é salvo explicitamente.
Não há histórico de versões.
**Rastreabilidade:** DEC-003, DEC-006, DEC-018, DEC-022, DEC-024, DEC-025.

### RF25 — Preservar os dados utilizados nas avaliações
**Descrição:** O sistema deve persistir os dados necessários para reabrir e reproduzir uma avaliação mesmo após mudanças ou exclusão de produtos nos catálogos.

**Ator principal:** Usuário (indiretamente).

**Pré-condições:**

Avaliação criada, com dados disponíveis para persistência.
**Fluxo principal:**

Sistema associa à avaliação cópias dos dados dos produtos e critérios necessários ao cálculo.
Durante salvamento manual, persiste valores, seleção, categoria e julgamentos AHP, além de indicadores e resultados válidos quando existentes.
Ao reabrir a avaliação, usa os dados preservados, sem substituir automaticamente por valores atuais do catálogo.
**Fluxos alternativos e exceções:**

Produto oficial atualizado: preservar valores antigos da avaliação.
Produto pessoal editado/excluído: avaliação anterior continua consultável.
Dados locais corrompidos ou incompletos: não reconstruir resultado inválido.
Falha de persistência: não confirmar salvamento.
**Pós-condição:** Instantâneo dos dados relevantes disponível para retomada e consulta.

**Critérios de aceitação:**

Avaliações continuam consultáveis após mudanças no Firestore ou no catálogo pessoal.
Dados usados nos cálculos são os da avaliação.
O recálculo pode funcionar offline com snapshots íntegros.
Não há atualização retroativa silenciosa de valores.
**Rastreabilidade:** DEC-003, DEC-004, DEC-006, DEC-012, DEC-020.

### RF26 — Excluir avaliação salva
**Descrição:** O sistema deve permitir excluir definitivamente um rascunho ou avaliação concluída, mediante confirmação, sem excluir produtos.

**Ator principal:** Usuário.

**Pré-condições:**

Avaliação persistida existente.
**Fluxo principal:**

Usuário seleciona uma avaliação e solicita exclusão.
Sistema apresenta confirmação explícita.
Usuário confirma.
Sistema exclui avaliação e dados e resultados vinculados no Room.
Interface atualiza listagem e confirma sucesso após a conclusão.
**Fluxos alternativos e exceções:**

Cancelamento: nenhuma alteração.
Falha na exclusão: comunicar erro e não confirmar sucesso.
Avaliação já inexistente: informar indisponibilidade.
**Pós-condição:** Avaliação removida permanentemente; catálogos intactos.

**Critérios de aceitação:**

A confirmação é obrigatória.
Cancelar preserva todos os dados.
Após sucesso, avaliação não reaparece após reiniciar.
Produtos pessoais e oficiais não são afetados.
Não há lixeira ou restauração pela interface na primeira versão.
**Rastreabilidade:** DEC-012, DEC-019, DEC-025.
