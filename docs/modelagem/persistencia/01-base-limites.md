# 5.1. Base aprovada e limite da proposta

[Índice da Parte 5](../05-persistencia-snapshots.md) · [Contrato consolidado](../05-contratos-persistencia.md)

**Situação atual:** P5-A–P5-E aprovadas nas DEC-038–DEC-042 em seus níveis documentados. CHECK canônico ainda requer validação técnica.

**Natureza deste capítulo:** estudo histórico das versões 0.1–0.5; referências a propostas ou escolhas pendentes no texto descrevem aquelas rodadas. As regras vigentes estão no [contrato consolidado aprovado](../05-contratos-persistencia.md).

As Partes 1–4 definem conceitos e regras. Uma entidade do domínio não exige uma tabela própria: Avaliação continua raiz de agregado, CritérioPreservado é objeto de valor, alternativas/julgamentos são componentes e resultados não possuem identidade independente (DEC-036). Esta parte propõe sua representação física; não altera requisitos ou decisões.

| Informação | Responsabilidade já aprovada | Representação física proposta |
|---|---|---|
| Categorias, critérios e produtos oficiais | Firestore, somente leitura pelo aplicativo | Documentos remotos e cache do SDK |
| Produtos pessoais | Persistência local, categoria fixa | Room, registro próprio com valores e definições necessárias à validação offline |
| Avaliações, critérios preservados, alternativas, julgamentos e resultados | Room; salvamento manual do agregado | Tabelas locais e gravação transacional |
| Edição pendente e mensagens de erro | Memória, distinta da última versão salva | Modelo de edição; sem tabela de autosave, falha ou histórico |
| PDF | Relatório gerado da versão salva processada | Arquivo derivado, não tabela nem backup restaurável |

A preservação de definições dentro de produtos pessoais proposta adiante é escolha técnica pendente (P5-E). Não altera o Firestore como fonte oficial. A cópia em avaliações já decorre das DEC-033/036. Nenhuma estrutura concede escrita do cliente no catálogo, sincronização pessoal ou novas categorias na versão inicial.
