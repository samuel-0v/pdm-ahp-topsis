# 5.4. Snapshots e independência dos catálogos

[Índice da Parte 5](../05-persistencia-snapshots.md) · [Contrato consolidado](../05-contratos-persistencia.md)

**Situação atual:** P5-A–P5-E aprovadas nas DEC-038–DEC-042 em seus níveis documentados. CHECK canônico ainda requer validação técnica.

**Natureza deste capítulo:** estudo histórico das versões 0.1–0.5; referências a propostas ou escolhas pendentes no texto descrevem aquelas rodadas. As regras vigentes estão no [contrato consolidado aprovado](../05-contratos-persistencia.md).

1. **Criar em memória:** copiar categoria histórica e definições dos critérios disponíveis, em ordem estável. Depois, ao selecionar produtos, copiar identificação, origem + ID e valores de referência conhecidos.
2. **Ajustar valores:** manter valor efetivo próprio e valor original quando conhecido; ausência continua distinta de zero. Não copiar todas as definições para cada alternativa.
3. **Salvar:** gravar todo o agregado e apenas resultados vigentes. Avaliação nova pode ir diretamente para concluída sem rascunho prévio. Critérios preservados já definidos permanecem imutáveis nas próximas edições.
4. **Reabrir/recalcular/exportar:** usar dados locais preservados; não buscar dados atuais para preencher silenciosamente ausências ou atualizar valores/unidades.
5. **Editar/excluir catálogo pessoal ou atualizar/remover documento oficial:** nenhuma cascata/atualização alcança avaliações salvas. Produtos removidos não são oferecidos em novas seleções, mas a participação histórica continua.

Os dados de referência/proveniência conhecidos no momento da seleção são históricos. Não apresentar o snapshot como dado atualmente confirmado pelo servidor. Uma avaliação não precisa armazenar matrizes normalizadas/ponderadas para continuar consultável; indicadores e entradas aprovados são suficientes ao escopo, e fórmulas intermediárias podem ser recomputadas conforme necessário.

## Proveniência por valor utilizado

Recomendação física: cada linha `valor_alternativa` preserva, além dos números, dois blocos opcionais de proveniência — **do valor de origem** e **do valor efetivo**. Podem ser campos incorporados/serializados, sem tabela independente. Referências gerais do produto ou da definição do critério não substituem a associação inequívoca com o valor realmente usado.

| Informação do bloco | Preservação proposta |
|---|---|
| Fonte | Documento, URL, publicação ou descrição conhecida; referência histórica à origem + ID do registro quando aplicável |
| Data de referência | Data/perfil do valor na fonte, quando disponível; não usar a data do salvamento, geração do PDF ou consulta como substituta |
| Protocolo/rubrica | Identificação e versão conhecidas e descrição/parâmetros necessários à interpretação: teste de autonomia, rubrica de câmera/ADAS, perfil de seguro ou condições de recarga, conforme o critério |
| Indicação de estimativa/simulação | Verdadeiro, falso ou desconhecido, conforme informação disponível; ausência de indicação não significa “medido” nem “não estimado” |
| Origem do valor efetivo | Cópia do catálogo oficial, cópia do produto pessoal ou ajuste específico na avaliação; não pressupõe identificação pessoal do usuário |

Ao copiar um valor sem modificá-lo, sua proveniência acompanha o valor efetivo. Ao ajustá-lo, preservar o valor/proveniência de origem como referência e marcar o efetivo como ajustado; não atribuir automaticamente ao novo valor a fonte, a data ou o protocolo do número substituído. Informações aplicáveis já conhecidas podem ser mantidas apenas quando continuarem descrevendo o novo valor; detalhes desconhecidos permanecem desconhecidos. Se houver nova fonte informada no fluxo de dados, copiá-la com o valor; esta proposta não cria uma nova tela obrigatória de edição de fontes.

Preservar a proveniência conhecida disponível em produto oficial/pessoal antes da seleção, sem buscar obrigatoriamente a fonte externa no futuro. Cópia de avaliação salva é histórica e independente do catálogo. Não inventar fonte, data, versão de protocolo, condição de medição ou indicação de estimativa, nem inferir que todo ajuste manual seja necessariamente uma estimativa. A definição preservada pode guardar a rubrica comum, mas deve ficar claro qual versão fundamenta cada valor.
