# 5.9. Verificações previstas da proposta

[Índice da Parte 5](../05-persistencia-snapshots.md) · [Contrato consolidado](../05-contratos-persistencia.md)

**Situação da Parte 5:** somente P5-A aprovada (DEC-038); P5-B–P5-E pendentes. CHECK canônico requer validação técnica.

| Cenário | Evidência a obter na implementação |
|---|---|
| Avaliação nova → resultado → primeiro salvamento concluído | Conjunto completo gravado atomicamente sem rascunho prévio |
| Rascunho com valores de alternativas ausentes, julgamentos completos e AHP RC > 0,10 | Retoma ausências e diagnóstico inconsistente sem resultado TOPSIS ou conclusão indevida |
| Rascunho com julgamentos incompletos | Retoma pares pendentes sem inventar AHP vigente ou respostas |
| Julgamento aponta para critério de outra avaliação | FK/validação rejeita; não ocorre gravação parcial |
| Inserir `(A,B)` e depois `(B,A)`, inclusive por tentativa direta sem normalizador | Normalização preserva preferência e converge à mesma chave; restrição física rejeita ordem invertida quando adotada; nunca há dois julgamentos independentes |
| Alterar valor de catálogo na avaliação | Valor original/proveniência permanecem históricos; efetivo é marcado como ajustado sem atribuir fonte/data/rubrica desconhecidas |
| Cabeçalho/pesos AHP parciais ou detalhes TOPSIS misturados/incompletos | Salvar/carregar rejeita conjunto como vigente; transação reverte tentativa inválida; não há conclusão ou reparo automático |
| Valor monetário com centavos, excesso de casas, overflow ou entrada remota legada | Contrato aprovado preserva valor/escala; rejeita conversão ambígua ou fora de faixa; sem truncamento/arredondamento silencioso |
| Revisão apenas editorial, limites restritos e mudança semântica | Compara por IDs e metadados, revalida valores e classifica sem depender só do número da revisão |
| Produto repetido e IDs iguais em origens diferentes | Rejeita repetição da identidade, permite identidades distintas |
| Cadastro original editado/excluído ou cache removido | Avaliação salva mantém consulta, recálculo e PDF offline |
| Mudança remota de unidade/revisão | Snapshot não muda; novo cadastro/seleção de dados do catálogo exige compatibilidade. Recálculo histórico com snapshots íntegros permanece independente da revisão remota |
| Empate e sem diferenciação | Posições repetidas permitidas; coeficiente indefinido permanece ausente, nunca zero substituto |
| Falha provocada em gravação ou exclusão | Rollback integral; última versão salva e edição preservadas conforme o fluxo |
| Resultado novo sem mudança nas entradas | Alteração pendente até salvar; aviso ao sair permanece correto |
| Cache parcial, consulta vazia e data desconhecida | Não promete catálogo completo nem inventa valores/data; publicação e confirmação de leitura são distintas; leitura de cache não atualiza confirmação de servidor |

Esses são testes planejados, vinculados aos VT03, VT06–VT13 e VT17 existentes. Nenhum teste de Room/Firestore, migration, banco ou Kotlin foi executado. Na conferência documental, foram esclarecidos o processamento a partir de rascunho consistente, os pré-requisitos do diagnóstico AHP e a independência do recálculo histórico. O diagrama candidato passou no parser/renderizador Mermaid 11.12.0. Essa validação não aprova a modelagem física.

Os cenários específicos dos cinco achados posteriores estão na [resposta à revisão](../revisao-parte5.md#cenários-adicionais-para-implementação), incluindo os limites de detecção remota, original incompatível, novo cadastro pessoal, evolução do motor e colisões numéricas.

**Status final da Parte 5: P5-A aprovada com ressalvas na DEC-038; P5-B–P5-E revisadas como propostas. Ajustes aguardam nova conferência técnica e confirmação formal; nenhum avanço automático para implementação.**
