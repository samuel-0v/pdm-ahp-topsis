# 10. Consequências para requisitos e modelagem

[Índice de decisões](README.md) · [Registro](registro-decisoes.md)

- Os requisitos de catálogo devem distinguir Firestore (catálogo oficial) e Room (dados pessoais e avaliações locais).
- O modelo deve representar categorias, critérios, produtos, valores, avaliações, julgamentos AHP e resultados TOPSIS de forma genérica.
- As definições oficiais de categorias, critérios e produtos têm origem no Firestore (DEC-009). Pela DEC-033, a definição de cada critério também fornece metadados reutilizáveis para validar valores; o aplicativo aplica validação genérica e pode usar definições em cache offline. Produtos pessoais permanecem locais e referenciam os critérios da categoria. Avaliações persistidas preservam os metadados e valores usados, evitando que uma atualização do catálogo altere seu significado.
- Pela DEC-034, a identidade da alternativa selecionada é composta pela origem e pelo identificador do registro: ID oficial do Firestore ou ID local do catálogo pessoal. A mesma identidade não pode ser selecionada mais de uma vez; registros distintos de catálogos diferentes podem coexistir mesmo que descrevam o mesmo produto físico, pois não há equivalência automática por nome/modelo/versão.
- Pela DEC-035, o processamento válido em que todas as alternativas são indistinguíveis e C* fica indefinido pode ser salvo como resultado concluído e aparece na listagem com o estado “Concluída — sem diferenciação”, distinto de rascunho e falha matemática.
- Uma avaliação deve preservar uma cópia dos dados relevantes usados no cálculo.
- Requisitos de edição, recálculo e exclusão devem explicitar estados e efeitos sobre resultados armazenados.
- A especificação funcional validada RF01–RF27 está em [Requisitos](../requisitos/README.md). As Partes 1–4 da [modelagem de domínio](../modelagem/dominio.md) estão aprovadas conceitualmente; na Parte 5, a P5-A está aprovada pela DEC-038 e P5-B–P5-E permanecem pendentes.
