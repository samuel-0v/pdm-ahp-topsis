# 4. P5-C — Contrato documental Firestore

[Índice dos contratos](../05-contratos-persistencia.md) · [Parte 5](../05-persistencia-snapshots.md)

**Status:** aprovado pelo usuário em 06/11/2026 nas DEC-038–DEC-042. Contrato documental para orientar implementação; CHECK AHP, domínios/precisão e serialização permanecem dependências técnicas.

Todos os campos a seguir integram o contrato aprovado na DEC-040. O cliente só lê. `obrigatório` é condição para aceitar uma definição válida; documento de produto com valor ausente pode ser exibido como incompleto. Campo desconhecido meramente adicional pode ser ignorado; enum/versão semântica desconhecida não deve ser adivinhada.

| Documento/campo | Obrigatoriedade e validação |
|---|---|
| `categorias/{categoriaId}` | ID estável e não vazio; `nome` obrigatório; `descricao` opcional; `formatoContrato=1` e `revisaoDefinicao` string não vazia obrigatórios. Revisão é token opaco comparado por igualdade, não data nem número inferido. Na mesma categoria, cada token identifica uma definição imutável, conforme a obrigação de publicação abaixo. |
| `criterios` | Lista obrigatória ordenada por `ordem`, com IDs únicos e ordens únicas; exatamente cinco nas duas categorias iniciais. Cada item pertence exclusivamente à categoria. Identidade de critério globalmente inequívoca; não compartilhar ID entre categorias. |
| Item de critério | `id`, `nome`, `ordem` inteira não negativa, `unidade` (código estável), `natureza` BENEFICIO/CUSTO e `validacao` obrigatórios. Descrição/referências opcionais. Ordem numérica pode ter lacunas, desde que estável e sem repetição. |
| `validacao` | `tipoValor` MONETARIO_BRL/NUMERO; `obrigatorioCadastro`, `obrigatorioProcessamento` e `exigeInteiro` explícitos; limites e inclusividade quando conhecidos, codificados no mesmo tipo do valor; casas decimais quando exigidas pela fonte. Monetário exige moeda BRL e escala2. Não omitir regra essencial e presumir suporte. |
| Regras iniciais | Critérios aprovados da categoria, unidades e custo/benefício conforme registro; notas de câmeras/ADAS de 1 a 10; todos os cinco valores necessários ao processamento. Outros limites ainda não definidos não são inventados. `exigeInteiro` e precisão devem refletir a definição publicada e validada. |
| `produtos/{produtoId}` | ID global estável; `categoriaId`, `revisaoDefinicao` correspondente, `formatoContrato=1`, `nome` obrigatórios. Fabricante/modelo/versão/ano quando conhecidos; ausência não vira texto fictício. |
| `valores` | Mapa por ID de critério da categoria; envelopes tipados da [seção 3 — valores e serialização](03-valores-serializacao.md). Valor ausente é chave omitida/null. Critério desconhecido, variante errada ou campo contraditório é erro tratado. Valores faltantes não são zeros. |
| Proveniência por valor | `referenciasPorCriterio` opcional, mapa com `fonte`, `dataReferencia`, `protocoloOuRubrica`, `estimativa` quando disponíveis. Fonte tem descrição/URL/documento conhecido; protocolo tem ID/versão/parâmetros conhecidos; estimativa é true/false/null desconhecido. Datas da fonte podem ser datas civis ISO8601 identificadas como tal. |
| Atualização | `atualizadoEm` opcional como Timestamp do publicador, limitado ao documento; não inferir data de referência de cada valor a partir dele. |

Exemplo de formato, com números meramente ilustrativos, sem definir domínio ou produto real:

```json
{
  "formatoContrato": 1,
  "categoriaId": "smartphones-premium",
  "revisaoDefinicao": "r1",
  "nome": "Produto ilustrativo",
  "valores": {
    "smartphones-preco": {"tipo": "MONETARIO_BRL", "centavos": 12345, "moeda": "BRL", "escala": 2},
    "smartphones-autonomia": {"tipo": "NUMERO", "numero": 12.5}
  },
  "referenciasPorCriterio": {
    "smartphones-autonomia": {"fonte": "Referência ilustrativa", "estimativa": null}
  }
}
```

O produto ilustrativo está incompleto: campos dos outros critérios não foram preenchidos. A ausência de data/protocolo não significa atualização recente ou método certificado. Timestamp nativo não é representado neste exemplo JSON.

**Validação antes do domínio:** verificar formato/tipos/IDs, categoria conhecida, critérios exclusivos, revisão e regras compatíveis, mapa de valores, finitude/faixa e proveniência. Revisões divergentes exigem comparar definições quando disponíveis; não basta encontrar nomes iguais. Se só o token antigo do produto estiver disponível e não houver definição que permita interpretar os valores, classificar como indeterminado e bloquear reutilização dependente dela. Não pressupor serviço de histórico remoto de revisões.

## Obrigação do publicador e limite da detecção

**Decisão aprovada:** manter o payload pequeno e exigir imutabilidade da definição associada a `(categoriaId, revisaoDefinicao)`. Qualquer alteração de conteúdo da definição, inclusive editorial, recebe novo token que não pode ser reutilizado para outro conteúdo. Uma alteração editorial pode continuar semanticamente compatível após comparação; revisão diferente não significa incompatibilidade automática. Atualizar um produto para outra revisão exige verificar seus valores e sua proveniência sob a definição correspondente, sem apenas trocar o token.

O publicador responde pelo vínculo entre os números publicados e essa definição. O cliente valida formato, valores e todos os metadados efetivamente disponíveis, mesmo quando os tokens coincidem. Se possui duas definições diferentes sob o mesmo par categoria/revisão ou outra evidência de significado contraditório, rejeita o conjunto para seleção/processamento e informa conflito de definição; não escolhe arbitrariamente uma delas. Avaliações históricas íntegras não são reescritas por esse conflito.

**Limite explícito:** sem definição anterior, metadados suficientes no produto ou outra evidência independente, o cliente não consegue detectar uma troca semântica oculta com token mantido. Por exemplo, `NUMERO=12` não informa por si só se a autonomia foi produzida em horas ou minutos. A aceitação de produto e categoria de mesma revisão depende da obrigação do publicador; não comprova que essa obrigação foi cumprida. O contrato não promete detectar toda violação dessa obrigação e não adiciona assinatura, hash ou cópia integral da definição em cada produto. Exigir essa detecção independente demandaria outra proposta de payload e validação, ainda não aprovada.

Ler categoria/produto em eventos separados não garante uma revisão atômica comum: aguardar conjunto compatível, informar falha/incompletude ou usar snapshots já íntegros. Revisões divergentes sem definições suficientes permanecem indeterminadas. Produto incompleto consultável não se transforma em domínio pronto para processar. Cache e confirmação de leitura do servidor não relaxam essas validações nem removem o limite de detecção.

Coleções raiz evitam ambiguidade do ID de produto; se futuramente o layout mudar, identidade histórica não pode mudar silenciosamente. Não adicionar autenticação, UI administrativa ou escrita do app. Consulta por categoria é a consulta básica; índices extras apenas conforme consultas concretas. [Estruturas Firestore](https://firebase.google.com/docs/firestore/manage-data/structure-data).
