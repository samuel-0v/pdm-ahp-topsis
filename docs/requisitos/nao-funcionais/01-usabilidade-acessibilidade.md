# Usabilidade e Interface

**Versão:** 1.0 — requisitos validados.

[Índice de requisitos](../README.md)

### RNF01 — Usabilidade

**Situação:** validado.

A interface deverá apresentar ações principais identificáveis, progresso AHP visível, erros próximos dos campos pertinentes e confirmação de operações destrutivas.

**Protocolo de verificação aprovado:** três participantes que não tenham desenvolvido as telas executarão o mesmo roteiro:

1. Consultar uma categoria e os detalhes de um produto.
2. Criar uma avaliação, selecionar produtos e concluir as comparações AHP.
3. Consultar o ranking e identificar a alternativa mais bem classificada ou a existência de empate.
4. Salvar e retomar um rascunho.

O avaliador poderá explicar o objetivo de cada tarefa, sem indicar quais controles utilizar. Registrar conclusão, necessidade de ajuda e dificuldades observadas.

**Critério de aceitação:** pelo menos dois dos três participantes devem concluir cada tarefa sem ajuda para navegar. Problemas que impeçam a conclusão devem ser corrigidos e a tarefa afetada reavaliada. O teste fornece evidência prática, sem representar pesquisa estatística.

### RNF02 — Responsividade e acessibilidade

**Situação:** validado.

**Escopo inicial de verificação:** larguras de 360 dp e 600 dp, em orientação retrato. Paisagem não integra esta matriz inicial; essa delimitação não declara validação de outros formatos.

**Critérios de verificação:**

- Testar escala de fonte padrão e 200%, permitindo rolagem, sem sobreposição, corte de informações essenciais ou controles inacessíveis.
- Verificar os fluxos de avaliação e consulta de resultados com TalkBack, incluindo identificação dos controles e ordem de leitura.
- Apresentar erros, consistência AHP e destaque do ranking por texto ou símbolos identificáveis, além da cor.
- Verificar alvos de toque de pelo menos 48 × 48 dp para controles interativos.

**Método de verificação:** testes visuais, interação e TalkBack nas condições definidas acima, com registro do ambiente e dos resultados.
