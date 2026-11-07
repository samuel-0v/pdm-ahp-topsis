# 6. Operações e validação cruzada

[Índice dos contratos](../05-contratos-persistencia.md) · [Parte 5](../05-persistencia-snapshots.md)

**Status:** aprovado pelo usuário em 06/11/2026 nas DEC-038–DEC-042. Contrato documental para orientar implementação; CHECK AHP, domínios/precisão e serialização permanecem dependências técnicas.

Salvar prepara uma cópia coerente da edição e valida o agregado; grava raiz, filhos e resultados/limpezas numa única transação. Criar concluída diretamente é permitido. Falha reverte integralmente e preserva edição, inclusive ao sair. Resultados novos/diferentes sem mudança nas entradas continuam alterações pendentes. Leitura de múltiplas tabelas deve observar uma única versão consistente. Exclusão confirmada remove agregado e dependentes numa transação; não toca produto pessoal ou catálogo. Não usar substituição implícita da raiz que dispare cascatas inesperadas. Nenhuma chamada remota ou PDF integra a transação.

PDF carrega somente versão salva processada e consistente. Havendo edição pendente, aplica Salvar e exportar, Exportar última versão salva ou Cancelar conforme DEC-032; sucesso de geração não significa envio. Rascunho não é exportável e resultado desatualizado não é resultado concluído. Validação ou salvamento falho não autoriza exportação da edição.

| Relação com decisão aprovada | Conferência do contrato |
|---|---|
| DEC-025/038 | Edição em memória separada; atomicidade; sem autosave/histórico; seis tabelas preservadas. |
| DEC-034/036 | Origem + ID histórica, FKs apenas internas, alternativas independentes do cadastro atual. |
| DEC-033/037 | Critérios copiados imutáveis; AHP inconsistente completo em rascunho; cinco critérios iniciais; n futuro depende de suporte matemático. |
| DEC-018/027/028/029/035 | Pesos/indicadores separados da definição, tolerâncias preservadas, empates e ausência de diferenciação sem dados fictícios. |
| DEC-020/030/032 | Origem/datas honestas, sem sincronização pessoal ou mudança de backup, PDF explícito da versão salva. |

Não foi identificada contradição que exija alterar decisões anteriores. Escala monetária2, representação tipada e rejeição de frações de centavo são escolhas aprovadas na DEC-041; não fixam limites de negócio adicionais para os critérios. Campos de revisão e formato não criam histórico de avaliações.

## Dependências técnicas preservadas após a aprovação

- Viabilidade/declaração e teste da defesa CHECK canônica no Room: dependência já reconhecida na DEC-038.
- Domínios concretos, precisão, fontes/rubricas e protocolos do catálogo: não inventados aqui. A faixa física do tipo não substitui validação de domínio. Definir resolução e erro admitido antes de habilitar os dados reais; o [bloqueio de colisões na conversão](07-recalculo-precisao.md#resolução-na-fronteira-do-motor) não encerra essa dependência.
- Biblioteca e configuração concreta de serialização e schema/migrations: serão trabalho posterior; devem cumprir o contrato de round-trip, sem fallback destrutivo. Não são implementadas nesta rodada.

## Cenários de aceitação documental e testes futuros

Verificar round-trip de UUID, Long monetário (inclusive zero e fronteiras de representação), Double finito, originais/efetivos diferentes e proveniência desconhecida; rejeitar excesso fracionário, overflow, formato legado, tag contraditória e preenchimento parcial. Testar par A/B versus B/A e tentativa de referência cruzada entre avaliações. Restaurar rascunho incompleto (inclusive entrada pendente), AHP consistente/inconsistente e duas conclusões. Simular falha entre escritas e exclusões. Alterar/excluir catálogo, remover cache e recalcular histórico. Testar revisões editoriais/semânticas, cache parcial, data desconhecida e PDF da versão salva escolhida. Esses cenários complementam VT03, VT06–VT13 e VT17; nenhum teste do aplicativo ou banco foi executado.

Os cinco achados da revisão posterior, os ajustes recomendados e os cenários adicionais estão em [Resposta à revisão da Parte 5](../revisao-parte5.md). Os ajustes foram aprovados pelo usuário em 06/11/2026 e integram o fechamento documental da Etapa 3; os cenários continuam como verificações futuras.

**Status final: contrato aprovado nas DEC-039–DEC-042; Etapa 3 concluída documentalmente em 06/11/2026. Implementação e testes permanecem para as etapas técnicas.**
