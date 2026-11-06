# 5.6. Catálogo oficial no Firestore — P5-C

[Índice da Parte 5](../05-persistencia-snapshots.md) · [Contrato consolidado](../05-contratos-persistencia.md)

**Situação da Parte 5:** somente P5-A aprovada (DEC-038); P5-B–P5-E pendentes. CHECK canônico requer validação técnica.

Recomendação para revisão: categorias e produtos em coleções raiz; critérios embutidos como conjunto pequeno no documento da categoria. IDs de critérios são estáveis, exclusivos à categoria e globalmente inequívocos no catálogo; nomes iguais não significam identidade compartilhada (DEC-037).

```text
categorias/{categoriaId}
  nome, descricao?, revisaoDefinicao, atualizadoEm?
  criterios: [
    { id, ordem, nome, descricao?, unidade, natureza,
      validacao: { tipo, limites?, ausencia, ... }, referencias? }
  ]

produtos/{produtoId}
  categoriaId, revisaoDefinicao
  nome, fabricante?, modelo?, versao?, ano?
  valores: { criterioId: valor ou ausencia explicita }
  referenciasPorCriterio?: { criterioId: { fonte?, dataReferencia?, protocoloOuRubrica?, estimativa? } }
  dataReferencia?, atualizadoEm?
```

Esse desenho é ilustrativo; obrigatoriedade final de campos e formato de revisão aguardam modelagem detalhada. `?` indica informação opcional/desconhecida, nunca data estimada. Não contém produtos pessoais, julgamentos ou avaliações. A ordem de negócio é explícita em `ordem`, e não inferida da iteração de mapas.

| Alternativa | Vantagens | Desvantagens e recomendação |
|---|---|---|
| Critérios embutidos na categoria | Uma leitura obtém definição coerente, ordem e validações; proporcional aos cinco critérios atuais | Documento cresce com o conjunto; **recomendado** para este escopo |
| Critérios em subcoleção | Consultas/atualizações independentes e crescimento mais flexível | Mais documentos/leituras e verificação de coerência de revisão; não necessária apenas por existir conceito Critério |
| Produtos em coleção raiz | ID oficial inequívoco e consulta por categoria | Consulta exige filtro `categoriaId`; **recomendado** |
| Produtos em subcoleção da categoria | Agrupamento por caminho | IDs locais à coleção podem colidir entre categorias; nesse caso o ID histórico precisa incluir caminho/namespace completo |

A documentação do Firestore apresenta mapas/listas aninhados para conjuntos pequenos e subcoleções para dados que crescem ou precisam de consultas próprias. A escolha acima é recomendação da proposta, não obrigação da plataforma. [Estruturas de dados Firestore](https://firebase.google.com/docs/firestore/manage-data/structure-data).

## Identidade, coerência e validação remota

- Produto oficial: identidade de domínio `(OFICIAL, produtoId)` no layout recomendado. Produto pessoal: `(PESSOAL, produtoIdLocal)`. Não incluir nome/modelo na identidade e não trocar ID só por editar dados.
- Duas identidades podem representar o mesmo produto físico; a seleção não as deduplica automaticamente (DEC-034).
- A categoria e os critérios devem corresponder ao produto e à revisão informada. Não há FK entre Firestore e Room; o leitor valida o documento antes de convertê-lo ao domínio.
- Propor `revisaoDefinicao` explícita para detectar categoria e produto com definições incompatíveis, inclusive em cache. Revisão não é histórico de avaliações. Se houver incompatibilidade ou tipo desconhecido, sinalizar e impedir processamento que reinterpretaria unidade/domínio; não inventar conversões.
- Produtos oficiais incompletos continuam consultáveis e sinalizados; a alternativa pode manter ausência no rascunho, mas não pode processar sem os valores exigidos. Produto pessoal tem validação de cadastro própria (RF06).
- Índices Firestore: planejar consulta de produtos por `categoriaId`; acrescentar índice composto somente se filtros/ordenação reais exigirem. Não configurar índices nesta proposta.
- A publicação por responsáveis pelo catálogo deverá manter categorias/produtos coerentes. A versão revisada do contrato propõe definição imutável por categoria/revisão e explicita o [limite de detecção de mudança oculta com token mantido](../contratos/04-firestore.md#obrigação-do-publicador-e-limite-da-detecção). Não se está propondo tela administrativa, rotina de publicação ou escrita pelo cliente. Leitura pública e escrita bloqueada continuam conforme RNF07.

## Comparação de definições de produtos pessoais — P5-E pendente

**Alvo da comparação:** no novo cadastro/seleção, comparar a definição conhecida junto ao produto com a definição usada pela avaliação de destino. Em uma avaliação nova, ela deriva da categoria disponível naquele momento; ao acrescentar alternativa a uma avaliação existente, usar seus CritérioPreservado, sem trocar suas definições pelas atuais. Consultar, recalcular e exportar uma avaliação histórica completa utiliza exclusivamente os snapshots já salvos.

**Detecção proposta:** conferir categoria, conjunto de IDs, significado do critério, unidade/moeda/período e escala, tipo/domínio/ausência, limites/inclusividade, natureza benefício/custo e protocolo/rubrica aplicável. Comparar por ID, nunca somente por nome, posição ou tipo físico da coluna. `revisaoDefinicao` serve como indício para comparar: revisões diferentes não provam incompatibilidade, e revisão igual não dispensa validar o conteúdo recebido. A revisão deve ser preservada quando conhecida; não fabricar revisão ausente.

| Alteração observada | Classificação proposta e ação |
|---|---|
| Nome, descrição ou ordem de apresentação, com significado/IDs/metadados semânticos preservados | Compatível após conferência; associar por ID e manter a ordem/definições da avaliação de destino. Alteração de texto que mude significado não é apenas editorial |
| Limites ampliados, ou restrição nova/mais estreita | Revalidar cada valor sob a definição de destino. Só considerar reutilizável quando o mesmo valor e seu significado satisfizerem as regras; não declarar compatibilidade universal da revisão |
| Tipo representacional diferente ou escala de armazenamento diferente, mas mesma grandeza | Somente compatível com codificação e conversão exatas explicitamente definidas/aprovadas; não adivinhar centavos versus reais, truncar ou mudar valor. Sem contrato, indeterminada |
| Mudança de unidade, moeda/período, custo/benefício, rubrica/protocolo que altere a comparabilidade, remoção/troca de ID ou de categoria | Incompatível para reutilização direta. Não renomear IDs nem converter valor automaticamente; requer revisão/correção explícita dos dados para o destino |
| Definição ausente, tipo/regra desconhecida ou metadados insuficientes | Indeterminada. Não afirmar compatibilidade; informar a lacuna e impedir uso que dependa dessa interpretação, mantendo consulta histórica/local disponível |

**Ação recomendada na proposta revisada:** incompatibilidade ou indeterminação mantém o produto e a definição anterior intactos e sinaliza o motivo antes de incluir/processar dados novos. Correção de número sob o mesmo significado pode usar edição/ajuste, respeitando a validação. Incompatibilidade semântica do cadastro pessoal exige novo cadastro com definições compatíveis disponíveis, conforme o [caminho de correção consolidado](../contratos/05-compatibilidade-offline.md#caminho-de-correção-de-produtos-pessoais). Formato original incompatível exige correção na fonte antes da seleção, conforme o [contrato de valores](../contratos/03-valores-serializacao.md#original-incompatível-e-original-fora-de-domínio). Não criar migração, conversão de unidade, atualização de categoria ou substituição de valores/definições silenciosa.

Compatibilidade é relativa aos dados e à definição de destino conhecida. Offline, comparar somente cópias locais disponíveis e informar que podem estar desatualizadas; não afirmar validação contra revisão atual desconhecida do servidor nem impor uma leitura remota ao recálculo histórico. Atualização do catálogo não invalida nem reinterpreta avaliações existentes. Esta política e seu contrato de comparação são propostas pendentes em P5-E/P5-C, não decisões tomadas.
