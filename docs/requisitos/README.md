# Requisitos

Os RF01–RF27 e RNF01–RNF13 estão consolidados e validados documentalmente; a exportação do RF27 foi aprovada na DEC-032. P01–P03 da revisão da Etapa 2 foram esclarecidos pelas DEC-033–DEC-035; detalhamentos de schema e modelagem seguem pendentes conforme relatório. Os testes de implementação serão executados posteriormente.

## Etapa 2 — concluída documentalmente

Os UC01–UC15 foram aceitos. RF27 e UC16 detalham a exportação aprovada na DEC-032. A matriz e o plano foram alinhados às DEC-033–DEC-035. P01–P03 estão resolvidos; o relatório distingue a consolidação documental dos detalhamentos técnicos e testes futuros.

- [Casos de uso UC01–UC16](casos-de-uso/README.md), organizados em cinco grupos.
- [Matriz de rastreabilidade](rastreabilidade.md) entre RFs, RNFs, decisões e verificações.
- [Plano inicial de verificação](plano-verificacao.md), sem testes de implementação executados.
- [Registro de fechamento](revisao-etapa-2.md), com as resoluções de P01–P03, as verificações documentais realizadas e os encaminhamentos para as próximas etapas.

## Requisitos funcionais

Os RF01–RF27 estão validados documentalmente. A validação documental não representa implementação ou testes executados.

| ID | Requisito | Situação |
|---|---|---|
| RF01 | [Consultar categorias de produtos](funcionais/01-catalogo-oficial.md) | Validado |
| RF02 | [Consultar produtos de uma categoria](funcionais/01-catalogo-oficial.md) | Validado |
| RF03 | [Consultar detalhes de um produto oficial](funcionais/01-catalogo-oficial.md) | Validado |
| RF04 | [Consultar catálogo remoto no Firebase Cloud Firestore](funcionais/01-catalogo-oficial.md) | Validado |
| RF05 | [Atualizar catálogo e consultar dados offline](funcionais/01-catalogo-oficial.md) | Validado |
| RF06 | [Cadastrar produto pessoal](funcionais/02-catalogo-pessoal.md) | Validado |
| RF07 | [Consultar e editar produto pessoal](funcionais/02-catalogo-pessoal.md) | Validado |
| RF08 | [Excluir produto pessoal](funcionais/02-catalogo-pessoal.md) | Validado |
| RF09 | [Persistir e reutilizar produtos pessoais](funcionais/02-catalogo-pessoal.md) | Validado |
| RF10 | [Criar avaliação](funcionais/03-configuracao-avaliacoes.md) | Validado |
| RF11 | [Selecionar alternativas](funcionais/03-configuracao-avaliacoes.md) | Validado |
| RF12 | [Ajustar valores dos critérios por avaliação](funcionais/03-configuracao-avaliacoes.md) | Validado |
| RF13 | [Validar dados da avaliação para processamento](funcionais/03-configuracao-avaliacoes.md) | Validado |
| RF14 | [Realizar comparações pareadas dos critérios](funcionais/04-ahp.md) | Validado |
| RF15 | [Calcular pesos AHP por média geométrica normalizada](funcionais/04-ahp.md) | Validado |
| RF16 | [Calcular indicadores de consistência AHP](funcionais/04-ahp.md) | Validado |
| RF17 | [Validar consistência e bloquear TOPSIS quando necessário](funcionais/04-ahp.md) | Validado |
| RF18 | [Processar avaliação pelo TOPSIS](funcionais/05-topsis-resultados.md) | Validado |
| RF19 | [Classificar os produtos pelos coeficientes TOPSIS](funcionais/05-topsis-resultados.md) | Validado |
| RF20 | [Consultar e interpretar resultados da avaliação](funcionais/05-topsis-resultados.md) | Validado |
| RF21 | [Salvar avaliação como rascunho](funcionais/06-persistencia-avaliacoes.md) | Validado |
| RF22 | [Consultar avaliações salvas](funcionais/06-persistencia-avaliacoes.md) | Validado |
| RF23 | [Retomar avaliação salva](funcionais/06-persistencia-avaliacoes.md) | Validado |
| RF24 | [Editar e recalcular avaliação concluída](funcionais/06-persistencia-avaliacoes.md) | Validado |
| RF25 | [Preservar os dados utilizados nas avaliações](funcionais/06-persistencia-avaliacoes.md) | Validado |
| RF26 | [Excluir avaliação salva](funcionais/06-persistencia-avaliacoes.md) | Validado |
| RF27 | [Exportar avaliação em PDF](funcionais/07-exportacao.md) | DEC-032 aprovada; detalhamento registrado |

Referência matemática: [documento AHP-TOPSIS](../../references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf). As regras transversais e decisões ficam no [registro central](../decisoes/registro-decisoes.md).

## Requisitos não funcionais

| Grupo | Identificadores | Situação |
|---|---|---|
| [Usabilidade e Interface](nao-funcionais/01-usabilidade-acessibilidade.md) | RNF01–RNF02 | Validado |
| [Desempenho e Correção Matemática](nao-funcionais/02-desempenho-correcao.md) | RNF03–RNF04 | Validado |
| [Confiabilidade, Persistência e Disponibilidade](nao-funcionais/03-integridade-offline.md) | RNF05–RNF06 | Validado |
| [Segurança e Privacidade](nao-funcionais/04-seguranca-privacidade.md) | RNF07–RNF08 | Validado |
| [Qualidade do Código](nao-funcionais/05-qualidade-codigo.md) | RNF09–RNF11 | Validado |
| [Processo de Desenvolvimento](nao-funcionais/06-processo-desenvolvimento.md) | RNF12–RNF13 | Validado |
