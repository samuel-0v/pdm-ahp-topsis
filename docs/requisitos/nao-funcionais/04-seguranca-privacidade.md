# Segurança e Privacidade

**Versão:** 1.0 — requisitos validados.

[Índice de requisitos](../README.md)

### RNF07 — Segurança do catálogo remoto

**Situação:** validado.

**Descrição:** permitir consulta pública sem login ao catálogo oficial e impedir alterações realizadas por clientes Android.

**Critérios de verificação:**

- Permitir leitura sem autenticação de categorias, critérios e produtos publicados para consulta pública.
- Bloquear criação, alteração e exclusão pelo cliente Android nas regras do Firestore, incluindo tentativas realizadas fora da interface.
- Manter credenciais administrativas e chaves privadas de serviço fora do aplicativo e do repositório.
- Tratar falhas de permissão como erro de acesso, sem informar sucesso nem representar incorretamente um catálogo vazio.
- Manter produtos pessoais e avaliações no armazenamento local.

**Método de verificação:** testar leitura permitida e tentativas de criação, alteração e exclusão negadas nas coleções oficiais; revisar configuração e presença de credenciais administrativas. A aprovação documental não significa execução desses testes.

**Decisão relacionada:** DEC-009; critérios complementares aprovados na validação do RNF07.

### RNF08 — Privacidade dos dados pessoais

**Situação:** validado.

**Descrição:** manter produtos pessoais, julgamentos e resultados localmente, sem envio ao Firestore, excluindo esses dados dos mecanismos de backup e transferência do Android conforme DEC-030.

**Critérios de verificação:**

- Produtos pessoais, matrizes de comparação e resultados permanecem no armazenamento local e não são enviados ao Firestore nas operações previstas.
- O uso não exige conta nem autenticação.
- Solicitar somente permissões necessárias às funcionalidades implementadas.
- Configurar e verificar a exclusão dos produtos pessoais e avaliações dos mecanismos de backup e transferência de dados nas versões Android suportadas.
- Informar ao usuário que desinstalar o aplicativo ou perder o aparelho pode causar perda definitiva dos dados.

**Método de verificação:** inspecionar operações de rede, permissões e configuração de backup/transferência; verificar a restrição nas versões Android suportadas e a comunicação ao usuário. A aprovação documental não significa execução desses testes.

**Decisões relacionadas:** DEC-004, DEC-009, DEC-011 e DEC-030.
