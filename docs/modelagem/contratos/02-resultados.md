# 2. P5-B — Resultados incorporados e reconstrução do domínio

[Índice dos contratos](../05-contratos-persistencia.md) · [Parte 5](../05-persistencia-snapshots.md)

**Status:** proposta ajustada após revisão com ressalvas, pendente de nova conferência e confirmação formal. A base de seis tabelas permanece aprovada na DEC-038; os detalhes de P5-B–P5-E não estão aprovados.

| Situação | Contrato persistido |
|---|---|
| AHP ausente ou julgamentos incompletos | Todos os quatro campos do cabeçalho e todos os pesos são NULL. Julgamentos parciais são preservados sem resultado parcial. |
| AHP completo consistente | Cabeçalho finito, RI aplicável, exatamente um peso positivo/finito por critério; pesos normalizados e indicadores correspondentes aos julgamentos completos; RC ≤ 0,10 interno. Pode existir em rascunho ainda sem TOPSIS. |
| AHP completo inconsistente | Mesmo conjunto completo, com RC > 0,10, somente em rascunho e TOPSIS ausente. Diagnóstico não autoriza processamento TOPSIS. |
| TOPSIS não calculado/desatualizado ao salvar | `tipoTopsis=AUSENTE`, `estadoSalvo=RASCUNHO`; todos os campos de detalhe calculado NULL em todas as alternativas. |
| TOPSIS com ranking | `tipoTopsis=RANKING`, estado concluído correspondente, AHP completo consistente; exatamente um detalhe completo por alternativa, com distâncias finitas não negativas, C* definido e posição competitiva válida. Empates conforme DEC-027/028, sem desempate por ID. |
| TOPSIS sem diferenciação | `tipoTopsis=SEM_DIFERENCIACAO`, estado concluído correspondente, AHP completo consistente; distâncias calculáveis por alternativa; C* e posição NULL para todas, sem vencedor. Não confundir com normalização impossível. |

O mapeamento lê avaliação, critérios, alternativas, valores e julgamentos sob leitura consistente. Reconstrói CritérioPreservado apenas com definição copiada; recolhe seus pesos em um Resultado AHP separado. Agrupa colunas de detalhe das alternativas em Resultado TOPSIS por identidade contextual. Peso não integra a igualdade/imutabilidade da definição: alterar peso significa substituir resultado, nunca alterar snapshot semântico do critério.

Validar conjuntos e cardinalidades ao salvar E carregar; transação impede escrita parcial, mas não garante sozinha coerência semântica. Bloco incompleto, peso órfão, detalhe faltante/extra ou mistura de estados bloqueia apresentação/exportação como resultado válido. Informar inconsistência, sem completar pelo catálogo, inventar coeficiente ou reparar/gravar automaticamente.

Essa leitura valida o contrato e as invariantes dos dados salvos; não exige recálculo silencioso pelo motor atual para validar identidade binária de um resultado histórico. Consulta, exportação e recálculo explícito têm as garantias distintas da [seção 7 — recálculo e precisão](07-recalculo-precisao.md).

Alteração de julgamento invalida AHP/TOPSIS na edição; ao salvar, limpar cabeçalho, pesos e detalhes afetados. Alteração de alternativas/valores invalida TOPSIS, preservando AHP se seus dados não mudaram. Em memória, resultado anterior pode ser identificado como desatualizado, nunca publicado como vigente. A versão salva permanece intacta até salvamento bem-sucedido. Resultado recém-calculado também pode deixar edição pendente. Não criar estado persistido de falha.
