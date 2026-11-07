# Arquitetura

Pendente de elaboração. Decisões aprovadas ficam no [registro central](../decisoes/registro-decisoes.md). A [modelagem](../modelagem/dominio.md) registra as Partes 1–4 aprovadas conceitualmente e a base de seis tabelas da P5-A aprovada na DEC-038; os [contratos de persistência](../modelagem/05-contratos-persistencia.md) foram aprovados nas DEC-039–DEC-042, encerrando documentalmente a Etapa 3. Arquitetura é a próxima etapa e ainda não foi elaborada.

## Questões técnicas pendentes

Os itens abaixo são questões de especificação e não alteram as decisões já aprovadas:

1. Detalhar os contratos de acesso ao Firebase Cloud Firestore e a publicação coerente do catálogo, respeitando o layout e as identidades aprovados nas DEC-040/041.
2. Detalhar o schema e as migrações Room a partir das seis tabelas aprovadas na DEC-038; validar a defesa CHECK canônica. A implementação deve cumprir os contratos aprovados nas DEC-039–DEC-042.
3. Especificar atualização, erros e comunicação de defasagem do catálogo, complementando a indicação de cache já aprovada na DEC-020.
4. Definir bibliotecas e versões para navegação Compose, injeção de dependências e serialização.
5. Detalhar estados de ViewModel e contratos dos repositórios/casos de uso.
6. Aplicar DEC-027–DEC-029 e RNF04 para precisão, empates e posições; detalhar validação de entradas e dados ausentes.
7. Detalhar a persistência transacional e o comportamento diante de encerramento inesperado, conforme a política manual da DEC-025.
8. Escolher dados de referência e ferramentas de teste para os motores, repositórios e fluxos de interface.
