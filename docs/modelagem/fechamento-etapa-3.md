# Fechamento da Etapa 3 — Modelagem do domínio e dos dados

**Versão:** 1.0 — 06/11/2026.
**Situação:** concluída documentalmente, com Partes 1–5 aprovadas pelo usuário nos níveis descritos abaixo.

[Índice da documentação](../README.md) · [Modelagem](dominio.md) · [Decisões aprovadas](../decisoes/registro-decisoes.md) · [Rastreabilidade](../requisitos/rastreabilidade.md)

## Aprovação e entregas

O usuário autorizou a aprovação conjunta de DEC-039, DEC-040, DEC-041 e DEC-042 em 06/11/2026, após a explicação das escolhas revisadas e dos limites que permanecem. A confirmação expressa foi “autorizo e pode finalizar”. Essa aprovação completa a Parte 5 e encerra documentalmente a Etapa 3, sem criar DEC-043.

| Entrega | Base e situação |
|---|---|
| [Parte 1 — Entidades e responsabilidades](01-entidades-responsabilidades.md) | Aprovada conceitualmente; DEC-036 |
| [Parte 2 — Atributos e regras](02-atributos-regras.md) | Aprovada conceitualmente |
| [Parte 3 — Relacionamentos e cardinalidades](03-relacionamentos-cardinalidades.md) | Aprovada conceitualmente; DEC-037 |
| [Parte 4 — Estados e transições](04-estados-transicoes.md) | Aprovada conceitualmente em 04/11/2026, sem nova DEC |
| [Parte 5 — Persistência e snapshots](05-persistencia-snapshots.md) | P5-A aprovada na DEC-038; P5-B–P5-E aprovadas nas DEC-039–DEC-042 |
| [Contrato consolidado](05-contratos-persistencia.md) | Versão 1.0 aprovada, incorporando os ajustes da versão revisada 0.2 |
| [Tratamento da revisão técnica](revisao-parte5.md) | Cinco achados com tratamento contratual aprovado e dependências preservadas |

As seis tabelas permanecem `produto_pessoal`, `avaliacao`, `criterio_preservado`, `alternativa_avaliacao`, `valor_alternativa` e `julgamento_ahp`. Resultados não recebem tabelas adicionais. Os conceitos, identidades e regras das decisões anteriores foram preservados; a nota na DEC-038 apenas contextualiza a aprovação posterior de P5-B–P5-E.

## Limites aceitos

- Cada revisão remota identifica uma definição imutável sob responsabilidade do publicador. O cliente valida as evidências disponíveis, mas não garante detectar alteração semântica oculta com token mantido e sem informação independente.
- Original com formato incompatível bloqueia a seleção do produto até correção na fonte. Original canônico fora de domínio pode explicar um efetivo explicitamente corrigido e válido.
- Incompatibilidade semântica do produto pessoal exige novo cadastro com definições compatíveis disponíveis; a edição mantém suas definições associadas. Cadastros e avaliações anteriores permanecem independentes.
- Consulta/exportação usam o resultado salvo íntegro. Recálculo explícito usa a implementação disponível, sem promessa de identidade binária entre versões do motor.
- Colisão entre valores monetários efetivos distintos na conversão para a mesma entrada Double bloqueia processamento. Esse bloqueio não encerra a definição mais ampla da precisão por domínio.

## Dependências técnicas que permanecem abertas

| Dependência | Momento de resolução | Evidência necessária |
|---|---|---|
| Defesa física canônica dos pares AHP no Room | Antes do schema definitivo | Declaração compatível com o normalizador e teste de escrita direta invertida, igualdade de IDs e pertencimento à avaliação |
| Domínios, limites, resolução, erro admitido, fontes, rubricas e protocolos dos critérios | Antes de habilitar os dados reais correspondentes | Definições publicadas suficientes, validação na representação apropriada e cenários numéricos nas fronteiras do domínio |
| Biblioteca/configuração de serialização e schema/migrations | Durante o detalhamento técnico e a implementação, antes de persistir dados reais | Round-trip de UUID, Long, Double e metadados; leitura de formato conhecido e tratamento explícito de formatos desconhecidos, sem fallback destrutivo |

Essas dependências não são declaradas implementadas pela aprovação arquitetural. Os cenários de integridade, rollback, offline, cálculo e exportação continuam vinculados aos conjuntos VT existentes na rastreabilidade e nos contratos.

## Verificação documental e próximo passo

O fechamento inclui conferência de identificadores, status, links/âncoras locais e relações entre contratos, decisões e requisitos. Não equivale a testes de Room, Firestore, motores Kotlin, migrations ou Android, nem à comprovação da aceitação das adaptações pelo professor. A revisão independente com ressalvas e a aprovação do usuário são eventos distintos; não se declara nova revisão independente após a autorização.

A próxima etapa de trabalho é [Arquitetura](../arquitetura/arquitetura.md): detalhar responsabilidades, contratos de acesso aos dados, estados da apresentação, bibliotecas e estratégia de implementação/testes a partir da modelagem aprovada. A elaboração dessa etapa ainda está pendente; este fechamento não inicia implementação Android.
