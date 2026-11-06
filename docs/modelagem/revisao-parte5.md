# Parte 5 — Resposta à revisão técnica com ressalvas

**Data:** 06/11/2026.
**Status:** ajustes propostos para nova conferência e confirmação; não aprovados.

[Parte 5](05-persistencia-snapshots.md) · [Contrato revisado](05-contratos-persistencia.md) · [Propostas DEC-039–042](../decisoes/propostas-parte5.md)

## Origem e alcance

O parecer recebido na conversa revisou a branch `feat/etapa-3-modelagem-dominio-dados`, commit `95b6208106b9912f89ee1927d34d6c46e85cd57f`, incluindo 13 arquivos rastreados modificados e 46 arquivos novos sem commit. O revisor relatou consulta aos dois PDFs, conferência de links e preservação das regras na reorganização. Esta nota resume o parecer e os ajustes preparados em resposta; não substitui a argumentação integral recebida.

A revisão sustenta o modelo híbrido de seis tabelas e os principais fluxos. Aponta uma lacuna alta no vínculo semântico remoto, três esclarecimentos médios e uma dependência numérica conhecida. O parecer favorável anterior não é uma aprovação incondicional dos contratos. Nenhuma DEC-001–DEC-038, requisito aprovado ou tabela foi alterado para responder aos achados. DEC-039–DEC-042 continuam pendentes.

## Tratamento proposto dos achados

| Achado | Ajuste recomendado | Referência |
|---|---|---|
| 1 — Alta: semântica alterada sob o mesmo token pode ser indetectável | Definição imutável por categoria/revisão como obrigação do publicador. Validar as evidências disponíveis e explicitar que token igual não comprova cumprimento dessa obrigação. Não prometer detecção independente sem dados adicionais. | [Contrato Firestore](contratos/04-firestore.md#obrigação-do-publicador-e-limite-da-detecção); proposta DEC-040 |
| 2 — Média: original incompatível não cabe na união tipada | Bloquear seleção até correção na fonte. Não substituir dado incompatível por ausência nem criar variante bruta histórica. Distinguir original canônico fora de domínio, que pode ser preservado para explicar efetivo corrigido. | [Valores](contratos/03-valores-serializacao.md#original-incompatível-e-original-fora-de-domínio); proposta DEC-041 |
| 3 — Média: edição não define substituição de definições pessoais | Edição mantém a definição associada. Mudança semântica incompatível exige novo cadastro com definições compatíveis disponíveis, sem conversão automática; ausência dessas definições bloqueia o uso. | [Compatibilidade](contratos/05-compatibilidade-offline.md#caminho-de-correção-de-produtos-pessoais); proposta DEC-042 |
| 4 — Média: identidade de recálculo entre versões indefinida | Consulta/exportação usam o resultado salvo. Recálculo explícito usa a implementação disponível. Repetibilidade se limita à mesma implementação e ambiente numérico; identidade binária entre versões não é garantida. | [Recálculo](contratos/07-recalculo-precisao.md#consulta-e-evolução-do-motor); proposta DEC-041 |
| 5 — Média condicionada: conversão pode apagar diferenças | Bloquear processamento se valores monetários efetivos distintos do mesmo critério convergirem à mesma entrada Double. Manter pendente a definição mais ampla de resolução e erro admissível por domínio. | [Precisão](contratos/07-recalculo-precisao.md#resolução-na-fronteira-do-motor); proposta DEC-041 |

### Escolhas e riscos remanescentes

No achado 1, a alternativa seria acrescentar ao produto informação suficiente para uma verificação independente. A recomendação atual conserva o payload e limita a garantia do cliente; portanto, o erro oculto do publicador continua possível se a obrigação de revisão for violada e não houver evidência para detectá-lo. Esse limite deve ser aceito explicitamente ao aprovar DEC-040/042. Uma leitura do servidor não remove o risco.

No achado 2, conservar payload bruto seria outra possibilidade. O bloqueio proposto evita esse novo formato, mas exige correção do produto oficial na fonte antes de selecioná-lo. Não acrescenta ferramenta de administração ao aplicativo.

No achado 3, novo cadastro não garante acesso a uma revisão antiga ausente. Os fluxos existentes precisam dispor de definições compatíveis; caso contrário, essa associação fica indisponível. Não há promessa de seletor de histórico nem alteração da categoria do registro.

Nos achados 4 e 5, o contrato preserva a informação histórica, mas não promete que toda entrada de um contrato conhecido será processável por qualquer versão futura do motor. O bloqueio pontual de colisões também não define sozinho toda a precisão aceitável nem comprova estabilidade do TOPSIS.

## Cenários adicionais para implementação

| Cenário | Resultado esperado na proposta | Cobertura relacionada |
|---|---|---|
| Categoria e produto com mesmo token e duas definições conflitantes disponíveis | Rejeitar conjunto novo; manter snapshots históricos intactos | VT01, VT03, VT11 |
| Mesmo token com mudança oculta, sem evidência anterior no cliente | Registrar limite de detecção; validar disciplina de publicação fora do cliente, sem alegar identificação garantida | VT01, VT12 |
| Revisões diferentes com definição antiga indisponível | Classificar indeterminado e impedir uso dependente dela | VT01, VT03, VT11 |
| Preço legado em Double seguido de efetivo válido digitado | Não selecionar esse produto até correção na fonte; não fabricar original ausente | VT03, VT04, VT08 |
| Original canônico fora de domínio e efetivo válido explicitamente ajustado | Preservar original sinalizado; cálculo usa apenas efetivo validado e sua proveniência | VT03, VT04, VT08 |
| Produto pessoal em horas e destino incompatível em minutos | Não trocar definição na edição; exigir novo cadastro com definição compatível disponível | VT02–VT04, VT11 |
| Atualização do motor seguida de consulta/exportação | Não recalcular ou regravar implicitamente o resultado salvo íntegro | VT08, VT09, VT17 |
| Recálculo explícito com resultado diferente ou falha | Resultado diferente fica pendente; falha preserva a última versão salva | VT06–VT09 |
| `9007199254740992` e `9007199254740993` centavos como efetivos do mesmo critério | Bloquear processamento por colisão na conversão, sem concluir ausência de diferenciação | VT03, VT07 |
| Valores próximos dos limites definidos e operações numericamente inviáveis | Validar domínio na representação apropriada e bloquear cálculo tecnicamente inválido | VT03, VT06, VT07 |

Esses cenários complementam os conjuntos VT existentes. Não alteram o plano aprovado nem representam testes executados do aplicativo.

## Pendências e fechamento

Ainda é necessária nova conferência dos ajustes propostos e aprovação formal de DEC-039–DEC-042 antes de fechar documentalmente a Etapa 3. Os achados 1–4 têm uma recomendação explícita nesta versão, mas não são declarados resolvidos por aceitação do usuário ou por nova revisão independente.

Permanecem para detalhamento técnico, nos momentos já indicados: CHECK AHP antes do schema definitivo; serialização e migrations durante a implementação; domínios, rubricas, protocolos, resolução e erro admitido antes de habilitar os dados reais correspondentes. Nenhum teste Room, Firestore, Kotlin ou Android foi executado em resposta a este parecer.
