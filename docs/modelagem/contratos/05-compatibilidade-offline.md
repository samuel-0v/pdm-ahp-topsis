# 5. P5-E — Definições locais, compatibilidade e proveniência

[Índice dos contratos](../05-contratos-persistencia.md) · [Parte 5](../05-persistencia-snapshots.md)

**Status:** proposta ajustada após revisão com ressalvas, pendente de nova conferência e confirmação formal. A base de seis tabelas permanece aprovada na DEC-038; os detalhes de P5-B–P5-E não estão aprovados.

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

Compatível permite copiar valores para a avaliação e validá-los nos critérios de destino; não promove atualização do cadastro pessoal. Indeterminada mantém consulta dos dados conhecidos e espera informação/correção suficiente. Confirmar uma mensagem não pode dispensar uma incompatibilidade matemática ou semântica. Para produtos oficiais, a interpretação também respeita a [obrigação do publicador e o limite de detecção](04-firestore.md#obrigação-do-publicador-e-limite-da-detecção).

## Caminho de correção de produtos pessoais

**Escolha proposta:** a edição de um produto pessoal mantém suas definições associadas. Se a diferença é apenas um número incorreto sob o mesmo significado, corrigir os valores pelos fluxos existentes e revalidá-los. Regras de destino mais restritas exigem revalidação na seleção; corrigir um valor não atualiza automaticamente as definições do cadastro.

Quando unidade, moeda/período, natureza, rubrica/protocolo ou identidade dos critérios tornam a reutilização semanticamente incompatível, não associar o cadastro antigo para depois ajustar sua interpretação. Cadastrar outro produto pelos UC03/RF06, usando definições suficientes e compatíveis com o destino, e informar explicitamente os valores sob essas definições. Não copiar ou converter automaticamente os números do cadastro antigo. O novo registro recebe outro UUID, ainda que descreva o mesmo produto físico; isso respeita a identidade por origem + ID da DEC-034.

O cadastro antigo continua consultável/editável sob suas definições conhecidas, e as avaliações existentes permanecem intactas. Se as definições necessárias ao novo cadastro não estiverem disponíveis nos fluxos existentes, informar a indisponibilidade e bloquear a reutilização nessa avaliação até dispor de dados compatíveis. Não criar seletor de revisões antigas, migração de cadastro ou substituição de definições dentro do UC04 por inferência desta proposta. A categoria permanece imutável.

**Proveniência:** para cada valor original e efetivo, copiar a fonte, data de referência, identificação/versão de protocolo ou rubrica, condições/perfil conhecidos e indicação de estimativa triestado. Bloco pode ser serializado na linha de valor, sem tabela nova. Dados de rubrica comuns podem estar na definição copiada, mas a associação à versão usada pelo valor deve ser inequívoca e autocontida, não apenas URL externa mutável. Ao ajustar, preservar referência original e marcar efetivo como ajustado; não herdar fonte/data do número antigo como se descrevessem o novo. Desconhecido não é falso, zero, data atual ou dado certificado. Não se propõe nova tela obrigatória para preencher metadados desconhecidos.

## Offline e datas

Cache SDK contém dados oficiais já acessíveis, podendo estar parcial, desatualizado ou ser removido. Não há réplica integral do catálogo em Room, nem promessa de dados nunca consultados. `fromCache` identifica a origem do evento, não completude nem atualização. Consulta vazia em cache não prova catálogo remoto vazio. Distinguir erro de permissão, consulta vazia confirmada e indisponibilidade.

`atualizadoEm` é informação do publicador; `dataReferencia` é da fonte de cada valor; confirmação de leitura no servidor é outro evento. Se registrado `confirmadoNoServidorEm`, vinculá-lo ao documento/consulta confirmado e rotular como instante de consulta no relógio local; leitura do cache não o atualiza. Datas desconhecidas ficam desconhecidas; confirmação antiga não certifica atualidade do conteúdo em cache. Metadados de origem devem atualizar a interface mesmo quando só eles mudarem. [Offline Firestore](https://firebase.google.com/docs/firestore/manage-data/enable-offline).

Definições pessoais locais permitem consultar/editar com sua interpretação conhecida. Na reutilização, offline só se verifica compatibilidade com destino disponível, sem afirmar ser a revisão mais recente. Nova avaliação/cadastro exige definições suficientes. Avaliação histórica íntegra pode ser consultada, recalculada e exportada sem catálogo atual; agregar NOVA alternativa exige verificar os dados novos contra seus critérios preservados. Dados incompletos não são completados silenciosamente pelo remoto.
