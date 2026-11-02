# Requisitos

[Requisitos não funcionais](nao-funcionais.md) — RNF03 validado; demais em revisão.

## Requisitos funcionais

Os RF01–RF26 estão validados funcionalmente. A validação documental não representa implementação ou testes executados.

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

Referência matemática: [documento AHP-TOPSIS](../../references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf). As regras transversais e decisões ficam no [registro central](../decisoes/registro-decisoes.md).
