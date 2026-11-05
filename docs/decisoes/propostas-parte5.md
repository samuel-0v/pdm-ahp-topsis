# Propostas de decisão — Parte 5

**Versão:** 0.1 — 05/11/2026. **Todas pendentes de revisão técnica independente e confirmação formal do usuário.**

[Registro APROVADO](registro-decisoes.md) · [Parte 5](../modelagem/05-persistencia-snapshots.md) · [Contrato consolidado](../modelagem/05-contratos-persistencia.md)

O registro atual termina na DEC-038. DEC-039–DEC-042 são identificadores candidatos, a reconferir na formalização; não estão incluídos na contagem de decisões aprovadas. As preferências fornecidas nesta rodada autorizam elaborar a proposta, sem aprovação retroativa. DEC-001–038 permanecem inalteradas.

## Proposta DEC-039 — P5-B — Resultados incorporados

**Status: PENDENTE DE APROVAÇÃO.**

**Contexto:** AHP/TOPSIS são objetos de valor, mas precisam ser preservados com integridade dentro das seis tabelas da DEC-038.

**Alternativas consideradas:** Tabelas próprias de resultados/pesos/detalhes ou incorporação de campos.

**Escolha recomendada:** Incorporar λmax, IC, RI, RC e tipo TOPSIS na avaliação, pesos nos critérios e distâncias/C*/posição nas alternativas.

**Justificativa:** Reduz tabelas e mantém unidade transacional sem identidade independente dos resultados.

**Consequências e limitações:** Mapeamento reconstrói objetos de valor; peso não integra snapshot imutável. Cabeçalho/pesos e tipo/detalhes são completos ou ausentes. Diagnóstico inconsistente só em rascunho, falha não é conclusão.

**Critérios de validação:** Round-trip dos seis casos de resultado; rejeição de blocos parciais; invalidação consistente após mudança e rollback sem dados antigos vigentes.

## Proposta DEC-040 — P5-C — Contrato do catálogo Firestore

**Status: PENDENTE DE APROVAÇÃO.**

**Contexto:** Definições oficiais precisam ser lidas com significado, identidade e revisão verificáveis.

**Alternativas consideradas:** Critérios embutidos ou em subcoleção; produtos raiz ou sob categoria.

**Escolha recomendada:** Coleções raiz categorias/produtos, critérios embutidos e exclusivos por categoria, IDs estáveis, formato/revisão explícitos, valores tipados e proveniência por critério/valor.

**Justificativa:** Adequado ao conjunto pequeno de critérios e evita ambiguidade de identidade de produto.

**Consequências e limitações:** Validar estrutura, revisão, unidade, metadados e valores antes do domínio. Documentos incompletos não permitem cálculo. Não adiciona administração, autenticação ou escrita do cliente; não promete histórico remoto.

**Critérios de validação:** Ler exemplo válido/incompleto, rejeitar tipo desconhecido e critério externo; detectar revisão divergente/semântica alterada e cache parcial sem fabricar dados.

## Proposta DEC-041 — P5-D — Identidade e representação numérica

**Status: PENDENTE DE APROVAÇÃO.**

**Contexto:** Preservar identidade e entrada monetária exata, com conversões explícitas e reprodução de cálculo.

**Alternativas consideradas:** IDs inteiros ou UUID; dinheiro Double, inteiro em centavos ou decimal textual/não escalado.

**Escolha recomendada:** UUID textual local para produto pessoal/avaliação/alternativa; identidade histórica origem+ID; BRL Long centavos escala2; NUMERO Double finito; resultados Double; representação tipada original/efetiva independente.

**Justificativa:** UUID é estável sem depender do banco; centavos evitam perda da entrada decimal, mantendo motores Double proporcionais ao projeto.

**Consequências e limitações:** Formato versionado e discriminado em Room/Firestore; análise decimal exata antes de Long; rejeitar fração de centavo não nula, overflow, legado ambíguo e NaN/Inf. Conversão para reais na fronteira TOPSIS é aproximada e explícita. Não define limites de negócio novos nem altera tolerâncias.

**Critérios de validação:** Testar UUID, Long e Double em round-trip, zero/ausência, fração de centavo, overflow e payload legado; verificar unidade/escala e recálculo idêntico a partir dos dados preservados.

## Proposta DEC-042 — P5-E — Definições pessoais, snapshots e compatibilidade offline

**Status: PENDENTE DE APROVAÇÃO.**

**Contexto:** Reutilizar produto pessoal sem reinterpretar seus valores e manter avaliações independentes do catálogo.

**Alternativas consideradas:** Definições junto ao produto ou armazenamento compartilhado; cache SDK ou réplica integral Room.

**Escolha recomendada:** Copiar definições junto ao produto pessoal; snapshots próprios na avaliação; usar cache SDK oficial; classificar compatibilidade com o destino como compatível, incompatível ou indeterminada.

**Justificativa:** Evita dependência do cache para dados pessoais existentes e evita uma segunda sincronização do catálogo inteiro.

**Consequências e limitações:** Há duplicação pequena de definições. Mudanças semânticas exigem correção explícita; ausência essencial bloqueia reutilização dependente dela. Históricas não mudam e podem ser consultadas/recalculadas/exportadas offline. Datas de fonte/publicação/consulta são distintas.

**Critérios de validação:** Testar mudanças editoriais, limites e unidade/rubrica, edição de cadastro, remoção de origem/cache, revisão desconhecida e exportação histórica sem rede.

## Limites comuns

Todas dependem do contrato consolidado e preservam DEC-025, DEC-033–038 e os requisitos aprovados. A defesa física CHECK dos pares AHP continua questão técnica aberta da DEC-038, não resolvida por estas propostas. Nenhuma tabela adicional, migration, código Kotlin, configuração Firebase, navegação ou protótipo é autorizado por este documento. A implementação futura deverá comprovar os critérios; não há testes de banco executados nesta rodada.
