# Confiabilidade, Persistência e Disponibilidade

**Versão:** 1.0 — requisitos validados.

[Índice de requisitos](../README.md)

### RNF05 — Integridade dos dados persistidos

**Situação:** validado.

**Descrição:** preservar a consistência das avaliações salvas localmente, evitando gravações parciais e resultados incompatíveis com a configuração persistida.

**Critérios de verificação:**

- Salvar configuração, produtos, valores, julgamentos e resultados válidos de uma avaliação como uma operação única, utilizando transações na persistência local.
- Se o salvamento falhar, preservar a última versão salva e informar a falha, sem apresentar sucesso.
- Ao salvar alterações nos dados usados no cálculo de uma avaliação concluída, invalidar o resultado anterior na mesma operação.
- Excluir a avaliação, seus dados e resultados associados de forma conjunta, preservando os catálogos e exigindo a confirmação já definida na DEC-019.
- Alterações não salvas não modificam a última versão persistida e permanecem sujeitas à perda em encerramento inesperado, conforme DEC-025.
- Resultados matemáticos inválidos não são persistidos como avaliações concluídas.

**Método de verificação:** testes de integração da persistência com falhas provocadas durante operações envolvendo vários registros; verificar ausência de gravações parciais, preservação da versão anterior e coerência entre configuração e resultado. Testes ainda não executados nesta etapa documental.

**Decisões relacionadas:** DEC-004, DEC-006, DEC-019 e DEC-025.

### RNF06 — Funcionamento offline

**Situação:** validado.

**Descrição:** funcionalidades locais permanecem disponíveis sem internet quando os dados necessários estiverem presentes no dispositivo.

**Critérios de verificação:**

- Consultar e gerenciar produtos pessoais e avaliações salvas offline, incluindo edição, recálculo e exclusão de avaliações com os dados preservados localmente.
- Consultar ou recalcular uma avaliação salva com dados locais completos sem exigir nova consulta ao Firestore.
- Criar avaliações e cadastrar produtos pessoais offline quando as definições da categoria e dos critérios estiverem disponíveis localmente.
- Adicionar produtos oficiais a avaliações somente quando estiverem acessíveis no cache durante o uso offline.
- Explicar a indisponibilidade quando faltar dado necessário, sem fabricar valores ou garantir acesso a todo o catálogo.
- Identificar dados provenientes do cache e apresentar a última atualização conhecida quando disponível, conforme DEC-020.
- Os motores AHP e TOPSIS não dependem da disponibilidade do Firebase.

**Método de verificação:** executar cenários com conectividade desativada e dados locais completos, cache parcial e ausência de cache. Verificar tanto as operações disponíveis quanto as mensagens de indisponibilidade. Testes ainda não executados nesta etapa documental.

**Decisões relacionadas:** DEC-004, DEC-009, DEC-010, DEC-019, DEC-020 e DEC-025.
