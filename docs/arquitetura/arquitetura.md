# Arquitetura

Pendente de elaboração. Decisões aprovadas ficam no [registro central](../decisoes/registro-decisoes.md). A [modelagem](../modelagem/dominio.md) registra as Partes 1–4 aprovadas conceitualmente e a base de seis tabelas da P5-A aprovada na DEC-038; os [contratos restantes de persistência](../modelagem/05-contratos-persistencia.md) continuam propostos.

## Questões técnicas pendentes

Os itens abaixo são questões de especificação e não alteram as decisões já aprovadas:

1. Definir a estrutura de coleções/documentos do Firebase Cloud Firestore e os identificadores dos dados oficiais.
2. Detalhar o schema e as migrações Room a partir das seis tabelas aprovadas na DEC-038; validar a defesa CHECK canônica. Os detalhes de P5-B–P5-E permanecem sujeitos à aprovação.
3. Especificar atualização, erros e comunicação de defasagem do catálogo, complementando a indicação de cache já aprovada na DEC-020.
4. Definir bibliotecas e versões para navegação Compose, injeção de dependências e serialização.
5. Detalhar estados de ViewModel e contratos dos repositórios/casos de uso.
6. Aplicar DEC-027–DEC-029 e RNF04 para precisão, empates e posições; detalhar validação de entradas e dados ausentes.
7. Detalhar a persistência transacional e o comportamento diante de encerramento inesperado, conforme a política manual da DEC-025.
8. Escolher dados de referência e ferramentas de teste para os motores, repositórios e fluxos de interface.
