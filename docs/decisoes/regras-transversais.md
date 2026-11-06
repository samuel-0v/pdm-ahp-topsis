# 3. Regras transversais

[Índice de decisões](README.md) · [Registro](registro-decisoes.md)

## 3.1. Preservação e catálogo

- Produtos oficiais são somente para consulta no aplicativo.
- Produtos pessoais são locais e podem ser gerenciados pelo usuário.
- A categoria do produto pessoal é definida no cadastro e não pode ser alterada; mudanças de categoria exigem novo cadastro.
- Valores ajustados em uma avaliação não modificam catálogos.
- Cada avaliação preserva os dados utilizados para que continue consultável após alterações ou remoções do produto de origem.
- Atualizar o catálogo remoto não altera avaliações existentes.

## 3.2. Avaliações

- A avaliação seleciona produtos de uma categoria e exige pelo menos duas alternativas para processamento.
- Avaliações podem ficar em estado de rascunho ou concluído.
- Alterar entradas de cálculo invalida o resultado para a edição em memória; a versão persistida só é alterada ao salvar, quando entradas e invalidação do resultado anterior são gravadas atomicamente (DEC-025, RNF05).
- Recalcular gera o novo resultado em memória; salvar explicitamente substitui a versão persistida. Não há histórico de resultados.
- Excluir avaliação exige confirmação e remove localmente os dados e resultados dela, sem apagar produtos dos catálogos. Não há lixeira nem recuperação na interface da primeira versão.

## 3.3. Dados das categorias

**Smartphones Premium:** Geekbench 6 Multi-Core (benefício), preço em R$ (custo), nota de câmeras de 1 a 10 (benefício), autonomia em horas (benefício) e armazenamento em GB (benefício). A versão exata, fonte e data devem ser identificáveis; autonomia e rubrica de câmeras precisam de protocolos comparáveis.

**Veículos Elétricos BEV:** autonomia Inmetro/PBEV em km (benefício), preço de aquisição em R$ (custo), recarga rápida DC de 20% a 80% em minutos (custo), seguro anual em R$/ano (custo) e nota ADAS de 1 a 10 (benefício). Identificar versão/ano; documentar perfil do seguro e rubrica ADAS. A nota ADAS da equipe não é certificação oficial de segurança.

## 3.4. Origem dos dados oficiais (DEC-020)

Quando o catálogo oficial for apresentado a partir do cache do Firebase, o aplicativo indicará essa origem e mostrará a data da última atualização conhecida, quando disponível. Não se deve estimar ou inventar a data. Os detalhes estão nos requisitos RF03–RF05.

## 3.5. Categoria dos produtos pessoais (DEC-021)

A categoria é selecionada no cadastro e permanece imutável. Na edição, fica visível somente para consulta; nome, fabricante, modelo e valores dos critérios podem ser alterados. Para usar outra categoria, o usuário cadastra outro produto. Avaliações existentes preservam os valores utilizados, independentemente de alterações posteriores no catálogo pessoal.
