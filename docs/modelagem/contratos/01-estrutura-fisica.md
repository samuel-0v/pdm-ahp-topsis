# 1. Contrato de representação física

[Índice dos contratos](../05-contratos-persistencia.md) · [Parte 5](../05-persistencia-snapshots.md)

**Status:** aprovado pelo usuário em 06/11/2026 nas DEC-038–DEC-042. Contrato documental para orientar implementação; CHECK AHP, domínios/precisão e serialização permanecem dependências técnicas.

As seis tabelas permanecem as da DEC-038. Os campos abaixo definem o contrato de persistência aprovado; não constituem entidades Kotlin implementadas. PK/FK/UNIQUE descrevem garantias a implementar e testar posteriormente. Campos não marcados opcionais são obrigatórios na linha; rascunhos permitem ausência de componentes/valores conforme o domínio.

| Tabela | Chaves e campos do contrato |
|---|---|
| `produto_pessoal` | PK `produtoId` UUID textual; `categoriaId`, identificação obrigatória conforme RF06; rótulo histórico; `definicoesJson`, `valoresJson`, `formatoPayload` inteiro positivo; referências conhecidas opcionais. JSON inclui definições e proveniência por valor; produto pessoal não admite cadastro incompleto. |
| `avaliacao` | PK `avaliacaoId` UUID; `categoriaId`, rótulo histórico, `estadoSalvo`, `tipoTopsis`, datas de criação/salvamento; título opcional; `lambdaMax`, `ic`, `ri`, `rc` opcionais como bloco indivisível. |
| `criterio_preservado` | PK `(avaliacaoId, criterioId)`; FK para avaliação; definição completa copiada, ordem e revisão conhecida opcional; metadados/protocolo serializados; `pesoAhp` opcional separado da definição. UNIQUE `(avaliacaoId, ordem)`. |
| `alternativa_avaliacao` | PK `(avaliacaoId, alternativaId UUID)`; FK para avaliação; `origemProduto`, `idProdutoOrigem`, identificação copiada, ordem, proveniência de consulta conhecida; `dMais`, `dMenos`, `coeficiente`, `posicao` opcionais. UNIQUE `(avaliacaoId, origemProduto, idProdutoOrigem)`. |
| `valor_alternativa` | PK `(avaliacaoId, alternativaId, criterioId)`; FKs compostas para alternativa e critério da MESMA avaliação. Dois grupos tipados independentes, `origem` e `efetivo`, definidos na [seção 3 — valores e serialização](03-valores-serializacao.md); proveniência de cada grupo; texto de entrada pendente opcional apenas para rascunho. |
| `julgamento_ahp` | PK `(avaliacaoId, criterioMenorId, criterioMaiorId)`; duas FKs compostas para critérios da MESMA avaliação; intensidade inteira 1–9 e direção canônica. Pares pendentes não têm linha. |

FKs internas têm remoção coerente em cascata a partir da avaliação. Não existe FK da alternativa para produto atual, nem de critério preservado para definição remota. Pais referenciados têm PK/UNIQUE; índices filhas cobrem os prefixos de suas FKs. Manter índice por categoria no catálogo pessoal e por data de salvamento na lista; índices compostos adicionais dependem das consultas reais. Posição TOPSIS não é única, pois há empates. Nenhuma sétima tabela é proposta.

O produto pessoal guarda um payload JSON estruturado e versionado, não referências de objetos Room. Enumerações possuem códigos estáveis; UUID é serializado em representação textual canônica minúscula com hífens, validada na leitura, sem gerar novo ID ao editar ou carregar. IDs remotos são opacos, globalmente inequívocos e não precisam ser UUID. Origem é `OFICIAL` ou `PESSOAL`; colisões entre origens não são duplicidade. O `alternativaId` é contextual e estável nas edições da mesma participação, não substitui a identidade histórica.

## Unicidade AHP

Normalizar o par antes de qualquer escrita pela ordem binária determinística dos IDs textuais canônicos, independente do idioma e da ordem visual. Ao trocar os membros, trocar a direção relativa, mantendo o significado e a intensidade; intensidade 1 usa direção `IGUAL`. A PK impede dois registros normalizados do mesmo par. IDs iguais, critérios externos e ordem invertida sem normalização são rejeitados. A defesa CHECK `criterioMenorId < criterioMaiorId` com collation compatível continua dependência da DEC-038: ainda é necessário validar sua declaração no schema Room. Não assumir que a anotação de PK a cria. Testar tentativa direta invertida, além do normalizador, antes de considerar a defesa física concluída.
