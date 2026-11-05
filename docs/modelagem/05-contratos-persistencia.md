# Parte 5 — Contratos consolidados propostos (P5-B–P5-E)

**Versão:** 0.1 — 05/11/2026. **Status: proposta pendente de revisão independente e confirmação formal do usuário.**

[Parte 5](05-persistencia-snapshots.md) · [Propostas DEC-039–042](../decisoes/propostas-parte5.md) · [Domínio aprovado](dominio.md)

Este complemento detalha as opções preferenciais indicadas pelo usuário para elaborar a proposta final. Não atribui aprovação a P5-B–P5-E. DEC-038 permanece a aprovação da base de seis tabelas; suas dependências continuam abertas. Em caso de redação mais genérica na versão anterior, este contrato descreve a proposta consolidada, sem substituir decisões aprovadas.

## 1. Representação física candidata

As seis tabelas permanecem as da DEC-038. Campos abaixo são candidatos ao schema, não entidades Kotlin. PK/FK/UNIQUE descrevem garantias a implementar e testar posteriormente. Campos não marcados opcionais são obrigatórios na linha; rascunhos permitem ausência de componentes/valores conforme o domínio.

| Tabela | Chaves e campos propostos |
|---|---|
| `produto_pessoal` | PK `produtoId` UUID textual; `categoriaId`, identificação obrigatória conforme RF06; rótulo histórico; `definicoesJson`, `valoresJson`, `formatoPayload` inteiro positivo; referências conhecidas opcionais. JSON inclui definições e proveniência por valor; produto pessoal não admite cadastro incompleto. |
| `avaliacao` | PK `avaliacaoId` UUID; `categoriaId`, rótulo histórico, `estadoSalvo`, `tipoTopsis`, datas de criação/salvamento; título opcional; `lambdaMax`, `ic`, `ri`, `rc` opcionais como bloco indivisível. |
| `criterio_preservado` | PK `(avaliacaoId, criterioId)`; FK para avaliação; definição completa copiada, ordem e revisão conhecida opcional; metadados/protocolo serializados; `pesoAhp` opcional separado da definição. UNIQUE `(avaliacaoId, ordem)`. |
| `alternativa_avaliacao` | PK `(avaliacaoId, alternativaId UUID)`; FK para avaliação; `origemProduto`, `idProdutoOrigem`, identificação copiada, ordem, proveniência de consulta conhecida; `dMais`, `dMenos`, `coeficiente`, `posicao` opcionais. UNIQUE `(avaliacaoId, origemProduto, idProdutoOrigem)`. |
| `valor_alternativa` | PK `(avaliacaoId, alternativaId, criterioId)`; FKs compostas para alternativa e critério da MESMA avaliação. Dois grupos tipados independentes, `origem` e `efetivo`, definidos na seção 3; proveniência de cada grupo; texto de entrada pendente opcional apenas para rascunho. |
| `julgamento_ahp` | PK `(avaliacaoId, criterioMenorId, criterioMaiorId)`; duas FKs compostas para critérios da MESMA avaliação; intensidade inteira 1–9 e direção canônica. Pares pendentes não têm linha. |

FKs internas têm remoção coerente em cascata a partir da avaliação. Não existe FK da alternativa para produto atual, nem de critério preservado para definição remota. Pais referenciados têm PK/UNIQUE; índices filhas cobrem os prefixos de suas FKs. Manter índice por categoria no catálogo pessoal e por data de salvamento na lista; índices compostos adicionais dependem das consultas reais. Posição TOPSIS não é única, pois há empates. Nenhuma sétima tabela é proposta.

O produto pessoal guarda um payload JSON estruturado e versionado, não referências de objetos Room. Enumerações possuem códigos estáveis; UUID é serializado em representação textual canônica minúscula com hífens, validada na leitura, sem gerar novo ID ao editar ou carregar. IDs remotos são opacos, globalmente inequívocos e não precisam ser UUID. Origem é `OFICIAL` ou `PESSOAL`; colisões entre origens não são duplicidade. O `alternativaId` é contextual e estável nas edições da mesma participação, não substitui a identidade histórica.

### Unicidade AHP

Normalizar o par antes de qualquer escrita pela ordem binária determinística dos IDs textuais canônicos, independente do idioma e da ordem visual. Ao trocar os membros, trocar a direção relativa, mantendo o significado e a intensidade; intensidade 1 usa direção `IGUAL`. A PK impede dois registros normalizados do mesmo par. IDs iguais, critérios externos e ordem invertida sem normalização são rejeitados. A defesa CHECK `criterioMenorId < criterioMaiorId` com collation compatível continua dependência da DEC-038: ainda é necessário validar sua declaração no schema Room. Não assumir que a anotação de PK a cria. Testar tentativa direta invertida, além do normalizador, antes de considerar a defesa física concluída.

## 2. P5-B — Resultados incorporados e reconstrução do domínio

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

Alteração de julgamento invalida AHP/TOPSIS na edição; ao salvar, limpar cabeçalho, pesos e detalhes afetados. Alteração de alternativas/valores invalida TOPSIS, preservando AHP se seus dados não mudaram. Em memória, resultado anterior pode ser identificado como desatualizado, nunca publicado como vigente. A versão salva permanece intacta até salvamento bem-sucedido. Resultado recém-calculado também pode deixar edição pendente. Não criar estado persistido de falha.

## 3. P5-D — Valores tipados, dinheiro e serialização

### União tipada proposta

| Variante lógica | Campos Room de cada grupo (`origem`/`efetivo`) | Representação Firestore/JSON |
|---|---|---|
| Ausente | `tipo`, `centavos` e `numero` todos NULL | Chave do critério ausente ou valor null; ambos normalizam para ausência. Não usar objeto vazio ou número fictício. |
| Monetário BRL | `tipo=MONETARIO_BRL`, `centavos` Long não nulo, `numero` NULL | `{tipo: MONETARIO_BRL, centavos: inteiro64, moeda: BRL, escala: 2}` |
| Número não monetário | `tipo=NUMERO`, `numero` Double finito, `centavos` NULL | `{tipo: NUMERO, numero: número finito}` |

Moeda, escala, unidade e período são copiados na definição preservada (por exemplo, preço R$ e seguro R$/ano). Em Firestore/JSON o envelope monetário repete moeda/escala para validação; devem concordar com a definição. No Room elas são obtidas do critério preservado daquela linha, sem repetir sua definição em cada valor. Original e efetivo têm discriminadores/conteúdo próprios e proveniência própria; nunca sobrescrever original ao ajustar efetivo. Sem ajuste, efetivo é cópia do original; original pode ser ausente e efetivo informado depois.

No JSON local, Long usa sequência decimal inteira sem passar por Double, Double usa representação que recupera o mesmo valor binário finito, null permanece null. `formatoPayload` identifica o contrato de serialização, não histórico de avaliações. Formato desconhecido, campos incompatíveis com o discriminador ou mais de uma variante preenchida são erros, não ausência válida. Nenhum decoder pode transformar Long em Double antes de preservar o valor canônico. Firestore recebe inteiro64 real no campo `centavos`, não string ou Double com aparência inteira. O leitor não deve coerci-los silenciosamente.

Valores de rascunho podem faltar. Para preservar uma entrada ainda inválida/incompleta sem inventar número, propõe-se `entradaPendenteEfetiva` opcional (texto) no mesmo registro, com valor canônico efetivo ausente; não participa de cálculo. Texto pendente é limpo quando a entrada é corrigida/convertida explicitamente. Produto pessoal não é salvo com campo obrigatório inválido; conclusão não admite entrada pendente. Não há autosave desse texto.

### Conversão e validação

1. Interpretar entrada monetária decimal no contexto pt-BR da interface, sem separadores ambíguos; no contrato serializado usar formato canônico independente de locale. Propor análise decimal exata antes de obter centavos, usando BigDecimal ou equivalente apenas nessa fronteira.
2. Escala lógica BRL é 2: aceitar valores exatamente representáveis em centavos, incluindo zeros decimais excedentes sem perda (`1,230` equivale a `1,23`). Fração de centavo não zero (`1,231`) é rejeitada para correção explícita. Não truncar nem arredondar silenciosamente. Essa política é proposta, aguardando aprovação formal com P5-D.
3. Validar a faixa técnica signed64 (-2^63 a 2^63−1) antes de converter; detectar overflow em multiplicação/parse. Negatividade, zero e limites de negócio continuam governados pelos metadados; a faixa de armazenamento não cria novos limites físicos de domínio.
4. Na entrada TOPSIS, obter o valor na unidade do critério por conversão determinística equivalente a `centavos.toDouble() / 100.0`. Não usar divisão inteira. Exemplo: 12345 centavos → 123,45 reais aproximados em Double; manter 12345 canônico no snapshot. Para R$/ano a grandeza permanece anual, sem dividir por 12.
5. Motores recebem Double; verificar finitude/faixa, completude e condições matemáticas. Double pode perder exatidão de inteiros grandes: se um domínio exigir precisão exata não representável, sinalizar incompatibilidade técnica em vez de fingir exatidão. Os domínios reais e a precisão admitida ainda precisam ser documentados; não inventar teto de preços. Conversão aproximada é explícita, não migração do dado armazenado.
6. `NUMERO` segue finitude e metadados (inteiro quando exigido, limites inclusivos/exclusivos e escala conhecida). Não aplicar a regra de duas casas a autonomia, notas ou outros critérios sem base nos metadados. Pesos, λmax/IC/RI/RC, distâncias e C* usam Double/REAL; posições/ordens são inteiras, não coeficientes.
7. Ao carregar, validar novamente discriminador, conteúdo, unidade/escala e definição preservada. O mesmo contrato e conversão são usados no primeiro cálculo e no recálculo; não reler unidade atual do catálogo. Não arredondar resultados intermediários; RC e tolerâncias DEC-027/029 permanecem intactos.

Inteiros exatos e ponto flutuante binário têm propriedades diferentes; a conversão para o motor não torna a aritmética TOPSIS decimal exata. [Números no SQLite](https://sqlite.org/floatingpoint.html), [tipos Firestore](https://firebase.google.com/docs/firestore/manage-data/data-types).

**Dados legados:** formato monetário em Double, string, envelope desconhecido ou escala diferente é sinalizado como incompatível/indeterminado, não convertido automaticamente. Produto oficial continua consultável como incompleto quando possível, mas esse valor não pode alimentar cálculo. Correção explícita do dado na fonte ou ajuste no fluxo já aprovado pode fornecer um novo valor tipado; preservar original conhecido e sua proveniência. Não há migration ou rotina administrativa proposta. Snapshots históricos no contrato conhecido permanecem interpretados por sua própria versão, não pela revisão remota.

## 4. P5-C — Contrato documental Firestore candidato

Todos os campos a seguir são do contrato PROPOSTO. O cliente só lê. `obrigatório` é condição para aceitar uma definição válida; documento de produto com valor ausente pode ser exibido como incompleto. Campo desconhecido meramente adicional pode ser ignorado; enum/versão semântica desconhecida não deve ser adivinhada.

| Documento/campo | Obrigatoriedade e validação |
|---|---|
| `categorias/{categoriaId}` | ID estável e não vazio; `nome` obrigatório; `descricao` opcional; `formatoContrato=1` e `revisaoDefinicao` string não vazia obrigatórios. Revisão é token opaco comparado por igualdade, não data nem número inferido. |
| `criterios` | Lista obrigatória ordenada por `ordem`, com IDs únicos e ordens únicas; exatamente cinco nas duas categorias iniciais. Cada item pertence exclusivamente à categoria. Identidade de critério globalmente inequívoca; não compartilhar ID entre categorias. |
| Item de critério | `id`, `nome`, `ordem` inteira não negativa, `unidade` (código estável), `natureza` BENEFICIO/CUSTO e `validacao` obrigatórios. Descrição/referências opcionais. Ordem numérica pode ter lacunas, desde que estável e sem repetição. |
| `validacao` | `tipoValor` MONETARIO_BRL/NUMERO; `obrigatorioCadastro`, `obrigatorioProcessamento` e `exigeInteiro` explícitos; limites e inclusividade quando conhecidos, codificados no mesmo tipo do valor; casas decimais quando exigidas pela fonte. Monetário exige moeda BRL e escala2. Não omitir regra essencial e presumir suporte. |
| Regras iniciais | Critérios aprovados da categoria, unidades e custo/benefício conforme registro; notas de câmeras/ADAS de 1 a 10; todos os cinco valores necessários ao processamento. Outros limites ainda não definidos não são inventados. `exigeInteiro` e precisão devem refletir a definição publicada e validada. |
| `produtos/{produtoId}` | ID global estável; `categoriaId`, `revisaoDefinicao` correspondente, `formatoContrato=1`, `nome` obrigatórios. Fabricante/modelo/versão/ano quando conhecidos; ausência não vira texto fictício. |
| `valores` | Mapa por ID de critério da categoria; envelopes tipados da seção3. Valor ausente é chave omitida/null. Critério desconhecido, variante errada ou campo contraditório é erro tratado. Valores faltantes não são zeros. |
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

Mesmo que o publicador altere o conteúdo sem mudar token, conferir estrutura e metadados disponíveis. Ler categoria/produto em eventos separados não garante uma revisão atômica comum: aguardar conjunto compatível, informar falha/incompletude ou usar snapshots já íntegros. Produto incompleto consultável não se transforma em domínio pronto para processar. Cache não relaxa essas validações.

Coleções raiz evitam ambiguidade do ID de produto; se futuramente o layout mudar, identidade histórica não pode mudar silenciosamente. Não adicionar autenticação, UI administrativa ou escrita do app. Consulta por categoria é a consulta básica; índices extras apenas conforme consultas concretas. [Estruturas Firestore](https://firebase.google.com/docs/firestore/manage-data/structure-data).

## 5. P5-E — Definições locais, compatibilidade e proveniência

Produto pessoal mantém cópia das definições conhecidas, categoria e revisão conhecida junto ao próprio registro, no payload versionado. Uma avaliação mantém seus próprios CritérioPreservado e valores, sem vínculo vivo com esse payload. Editar produto pessoal não atualiza avaliação. Ao selecionar um produto, copiar o que foi validado, não guardar apenas referência ao cadastro.

Destino da comparação: definições escolhidas ao criar nova avaliação ou CritérioPreservado de avaliação existente. Comparar por IDs e significado, não posição/nome/revisão isolada. A comparação sempre é relativa a dados e definições efetivamente disponíveis.

| Mudança | Resultado proposto |
|---|---|
| Nome/descrição/ordem apenas editoriais | Compatível se IDs e significado forem os mesmos. Remapear por ID para a ordem de destino, sem reescrever definição preservada. |
| Limites/inclusividade/domínio mais restritos ou ampliados | Revalidar cada valor no destino. Compatível para os valores que continuam válidos e semanticamente iguais; incompatível para os que violam regra conhecida. Informação insuficiente resulta indeterminada. |
| Escala/formato de armazenamento | Compatível só com contrato exato e aprovado de representação que preserve grandeza; nesta proposta, formato diferente não é convertido silenciosamente. Sem equivalência demonstrada, indeterminada. |
| Unidade, moeda/período, custo/benefício ou rubrica/protocolo que mude a comparabilidade | Incompatível para reutilização direta. Mesmo valor numérico não demonstra mesmo significado. |
| Critério removido/trocado ou identidade/categoria diferente | Incompatível para associação direta. Critério novo exigido sem valor torna a configuração incompleta; pedir preenchimento explícito. Não mudar categoria pessoal. |
| Definição, regra, revisão interpretável ou proveniência essencial à comparabilidade ausente | Indeterminada; informar lacuna e impedir reutilização/processamento que dependa dela. Metadado opcional desconhecido não bloqueia sozinho quando o significado continua verificável. |

Compatível permite copiar valores para a avaliação e validá-los nos critérios de destino; não promove atualização do cadastro pessoal. Incompatível exige correção explícita pelos fluxos já existentes, preservando categoria e snapshots anteriores. Indeterminada mantém consulta dos dados conhecidos e espera informação/correção suficiente. Confirmar uma mensagem não pode dispensar uma incompatibilidade matemática ou semântica. Não criar migração/conversão silenciosa nem novos fluxos obrigatórios sem aprovação.

**Proveniência:** para cada valor original e efetivo, copiar a fonte, data de referência, identificação/versão de protocolo ou rubrica, condições/perfil conhecidos e indicação de estimativa triestado. Bloco pode ser serializado na linha de valor, sem tabela nova. Dados de rubrica comuns podem estar na definição copiada, mas a associação à versão usada pelo valor deve ser inequívoca e autocontida, não apenas URL externa mutável. Ao ajustar, preservar referência original e marcar efetivo como ajustado; não herdar fonte/data do número antigo como se descrevessem o novo. Desconhecido não é falso, zero, data atual ou dado certificado. Não se propõe nova tela obrigatória para preencher metadados desconhecidos.

### Offline e datas

Cache SDK contém dados oficiais já acessíveis, podendo estar parcial, desatualizado ou ser removido. Não há réplica integral do catálogo em Room, nem promessa de dados nunca consultados. `fromCache` identifica a origem do evento, não completude nem atualização. Consulta vazia em cache não prova catálogo remoto vazio. Distinguir erro de permissão, consulta vazia confirmada e indisponibilidade.

`atualizadoEm` é informação do publicador; `dataReferencia` é da fonte de cada valor; confirmação de leitura no servidor é outro evento. Se registrado `confirmadoNoServidorEm`, vinculá-lo ao documento/consulta confirmado e rotular como instante de consulta no relógio local; leitura do cache não o atualiza. Datas desconhecidas ficam desconhecidas; confirmação antiga não certifica atualidade do conteúdo em cache. Metadados de origem devem atualizar a interface mesmo quando só eles mudarem. [Offline Firestore](https://firebase.google.com/docs/firestore/manage-data/enable-offline).

Definições pessoais locais permitem consultar/editar com sua interpretação conhecida. Na reutilização, offline só se verifica compatibilidade com destino disponível, sem afirmar ser a revisão mais recente. Nova avaliação/cadastro exige definições suficientes. Avaliação histórica íntegra pode ser consultada, recalculada e exportada sem catálogo atual; agregar NOVA alternativa exige verificar os dados novos contra seus critérios preservados. Dados incompletos não são completados silenciosamente pelo remoto.

## 6. Operações e validação cruzada

Salvar prepara uma cópia coerente da edição e valida o agregado; grava raiz, filhos e resultados/limpezas numa única transação. Criar concluída diretamente é permitido. Falha reverte integralmente e preserva edição, inclusive ao sair. Resultados novos/diferentes sem mudança nas entradas continuam alterações pendentes. Leitura de múltiplas tabelas deve observar uma única versão consistente. Exclusão confirmada remove agregado e dependentes numa transação; não toca produto pessoal ou catálogo. Não usar substituição implícita da raiz que dispare cascatas inesperadas. Nenhuma chamada remota ou PDF integra a transação.

PDF carrega somente versão salva processada e consistente. Havendo edição pendente, aplica Salvar e exportar, Exportar última versão salva ou Cancelar conforme DEC-032; sucesso de geração não significa envio. Rascunho não é exportável e resultado desatualizado não é resultado concluído. Validação ou salvamento falho não autoriza exportação da edição.

| Relação com decisão aprovada | Conferência da proposta |
|---|---|
| DEC-025/038 | Edição em memória separada; atomicidade; sem autosave/histórico; seis tabelas preservadas. |
| DEC-034/036 | Origem + ID histórica, FKs apenas internas, alternativas independentes do cadastro atual. |
| DEC-033/037 | Critérios copiados imutáveis; AHP inconsistente completo em rascunho; cinco critérios iniciais; n futuro depende de suporte matemático. |
| DEC-018/027/028/029/035 | Pesos/indicadores separados da definição, tolerâncias preservadas, empates e ausência de diferenciação sem dados fictícios. |
| DEC-020/030/032 | Origem/datas honestas, sem sincronização pessoal ou mudança de backup, PDF explícito da versão salva. |

Não foi identificada contradição que exija alterar decisões anteriores. Escala monetária2, representação tipada e rejeição de frações de centavo são escolhas propostas para confirmação, não limites de domínio retroativamente aprovados. Campos de revisão e formato não criam histórico de avaliações.

### Dependências ainda abertas, sem impedir revisão desta proposta

- Viabilidade/declaração e teste da defesa CHECK canônica no Room: dependência já reconhecida na DEC-038.
- Domínios concretos, precisão, fontes/rubricas e protocolos do catálogo: não inventados aqui. A faixa física do tipo não substitui validação de domínio.
- Biblioteca e configuração concreta de serialização e schema/migrations: serão trabalho posterior; devem cumprir o contrato de round-trip, sem fallback destrutivo. Não são implementadas nesta rodada.
- Aprovação formal das propostas DEC-039–042 após revisão independente. Seus números são candidatos e precisam ser reconferidos ao registrar no conjunto aprovado.

### Cenários de aceitação documental e testes futuros

Verificar round-trip de UUID, Long monetário (inclusive zero e fronteiras de representação), Double finito, originais/efetivos diferentes e proveniência desconhecida; rejeitar excesso fracionário, overflow, formato legado, tag contraditória e preenchimento parcial. Testar par A/B versus B/A e tentativa de referência cruzada entre avaliações. Restaurar rascunho incompleto (inclusive entrada pendente), AHP consistente/inconsistente e duas conclusões. Simular falha entre escritas e exclusões. Alterar/excluir catálogo, remover cache e recalcular histórico. Testar revisões editoriais/semânticas, cache parcial, data desconhecida e PDF da versão salva escolhida. Esses cenários complementam VT03, VT06–VT13 e VT17; nenhum teste do aplicativo ou banco foi executado.

**Status final: consolidação proposta, aguardando revisão técnica independente e confirmação formal; nenhuma implementação ou aprovação automática.**
