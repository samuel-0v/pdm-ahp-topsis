# 5.5. Salvamento, leitura e exclusão atômicos

[Índice da Parte 5](../05-persistencia-snapshots.md) · [Contrato consolidado](../05-contratos-persistencia.md)

**Situação da Parte 5:** somente P5-A aprovada (DEC-038); P5-B–P5-E pendentes. CHECK canônico requer validação técnica.

**Recomendação técnica:** validar e preparar a cópia a gravar fora da transação, incluindo correspondência entre entradas e resultados. Capturar uma edição coerente; o sucesso só limpa a marca de alterações para o conteúdo efetivamente gravado. Uma alteração posterior em memória não pode ser marcada indevidamente como salva.

Dentro de uma única transação Room: criar ou atualizar a raiz, substituir os componentes mutáveis necessários, preservar as definições copiadas e gravar/limpar todos os campos de resultado correspondentes. Evitar substituir a raiz por uma estratégia implícita de excluir/reinserir que possa acionar cascatas indevidas; preferir atualização controlada. Nenhuma chamada Firestore, cálculo demorado ou geração PDF integra essa transação.

Após sucesso, a edição corresponde ao conteúdo salvo; em falha, reverter a operação inteira e manter versão anterior e edição pendente. No primeiro salvamento que falha, nenhuma avaliação parcial deve surgir na lista. Descartar edição não executa atualização no banco. Salvar ao sair aplica a mesma operação; falha mantém a edição na tela (Parte 4).

**Exclusão:** após confirmação, remover raiz e todos os componentes na mesma operação transacional. Não excluir produtos pessoais nem dados oficiais. Se falhar, manter avaliação íntegra. Cancelamento não executa exclusão.

**Leitura:** quando o agregado exige várias consultas, carregá-las sob leitura transacional coerente, evitando combinar componentes de antes e depois de uma gravação. A listagem pode consultar somente o cabeçalho. Transações de leitura/escrita compostas são suportadas pelo Room; isso não substitui as validações do domínio. [Referência de Transaction](https://developer.android.com/reference/androidx/room/Transaction).
