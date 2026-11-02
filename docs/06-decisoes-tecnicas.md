# Decisões técnicas

As decisões aprovadas e seu histórico ficam centralizados em [Registro de Decisões do Projeto](decisoes-projeto.md), evitando cópias divergentes entre documentos.

- A metodologia matemática aprovada está em [DEC-018](decisoes-projeto.md#4-dec-018--metodologia-matematica-ahp-topsis).
- A política de exclusão de avaliações está em [DEC-019](decisoes-projeto.md#5-dec-019--exclusao-de-avaliacoes).
- Requisitos funcionais, casos de uso e critérios de aceitação estão em [02-requisitos.md](02-requisitos.md).

## Decisões técnicas ainda pendentes

Os itens abaixo são questões de especificação e não alteram as decisões já aprovadas:

1. Definir a estrutura de coleções/documentos do Firebase Cloud Firestore e os identificadores dos dados oficiais.
2. Definir entidades, relações, migrações e estratégia de persistência no Room.
3. Especificar sincronização do catálogo e comportamento de erro/cache, respeitando os limites aprovados para uso offline.
4. Definir bibliotecas e versões para navegação Compose, injeção de dependências e serialização.
5. Detalhar estados de ViewModel e contratos dos repositórios/casos de uso.
6. Definir arredondamento, precisão numérica e tratamento de empates e dados ausentes, mantendo os algoritmos da DEC-018.
7. Definir a estratégia de salvamento automático e recuperação de rascunhos, sem contrariar DEC-006 e DEC-019.
8. Escolher dados de referência e ferramentas de teste para os motores, repositórios e fluxos de interface.

As decisões serão acrescentadas ao [registro central](decisoes-projeto.md) com novo identificador quando aprovadas. A arquitetura detalhada será mantida em [04-arquitetura.md](04-arquitetura.md).
