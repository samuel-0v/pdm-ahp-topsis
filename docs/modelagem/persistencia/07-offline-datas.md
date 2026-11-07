# 5.7. Offline, proveniência e datas — P5-E

[Índice da Parte 5](../05-persistencia-snapshots.md) · [Contrato consolidado](../05-contratos-persistencia.md)

**Situação atual:** P5-A–P5-E aprovadas nas DEC-038–DEC-042 em seus níveis documentados. CHECK canônico ainda requer validação técnica.

**Natureza deste capítulo:** estudo histórico das versões 0.1–0.5; referências a propostas ou escolhas pendentes no texto descrevem aquelas rodadas. As regras vigentes estão no [contrato consolidado aprovado](../05-contratos-persistencia.md).

**Recomendação:** cache do SDK como cópia corrente dos produtos oficiais; não replicar todo o catálogo em Room. Room mantém dados pessoais e snapshots das avaliações, que não são cache descartável. Ler avaliação salva completa nunca depende de o documento de origem ainda estar no cache.

`fromCache` identifica a origem da consulta, não fornece data de atualização nem certifica completude. O cache pode conter somente parte dos documentos de uma categoria, produtos sem todos os valores/metadados ou categoria e produtos de revisões diferentes. Uma consulta local vazia não prova ausência de produtos no catálogo remoto. Conferir os dados disponíveis, sinalizar campos/definições ausentes e impedir processamento quando faltar informação exigida, sem fabricar valores ou descartar snapshots íntegros.

Mudanças de metadados podem exigir observação específica para atualizar a indicação na interface. Dados confirmados pelo servidor confirmam a leitura daquela consulta, não garantem completude semântica dos campos nem igualdade de todos os documentos com uma mesma revisão. Uma definição ausente/incompatível não fica válida apenas por a origem ser servidor. Cache parcial/ausente pode limitar consultas oficiais sem impedir operações locais com dados completos. [Funcionamento offline do Firestore](https://firebase.google.com/docs/firestore/manage-data/enable-offline).

| Informação | Significado proposto | Cuidado |
|---|---|---|
| `dataReferencia` | Data/perfil da fonte do valor | Pode variar entre critérios; não é data da consulta |
| `atualizadoEm` | Data da alteração informada pelo publicador para aquele documento | Pode não existir; não é a data de leitura. Não atribuir um timestamp de um documento a todos os valores/documentos do catálogo |
| `confirmadoNoServidorEm`, se adotado | Instante conhecido da última leitura efetivamente confirmada no servidor, limitado ao documento/consulta correspondente | Registro local de consulta, sujeito ao relógio do dispositivo; não é timestamp de publicação nem comprova quando o valor mudou. Uma leitura apenas do cache não atualiza essa confirmação |
| Origem da leitura | Cache ou servidor confirmado no evento da consulta | Não converter uma antiga leitura confirmada em garantia de atualidade quando o dado atual veio do cache |

Para produtos pessoais existentes, consultar/editar/validar offline exige definições locais suficientes. Duas opções: **A**, manter as definições conhecidas junto ao produto (campo serializado proposto na tabela); **B**, criar armazenamento local compartilhado de definições por categoria/revisão. A evita tabela adicional e dependência de cache descartável, mas repete pequenas definições; B reduz repetição e centraliza atualização, com relações e ciclo de vida extras. **Recomendação inicial A**, sujeita a revisão.

Essa cópia não altera o catálogo oficial nem deve reinterpretar automaticamente os valores pessoais quando o servidor muda. A política de compatibilidade da [seção 5.6](06-firestore-compatibilidade.md) precisa ser aprovada; recomenda-se preservar a interpretação existente, validar contra a definição de destino disponível e pedir correção explícita quando necessária. Não propor migração silenciosa ou mudança de categoria. Novo cadastro/avaliação offline depende das definições disponíveis conforme RNF06; não se promete catálogo completo offline.

Continuam aplicáveis: ausência de sincronização pessoal, restrições de backup/transferência Android da DEC-030 e compartilhamento explícito do PDF da DEC-032. Não há criação de configurações Firebase/Android nesta parte.
