# Mapa de telas e rastreabilidade

[Índice da Etapa 5](README.md) · [Navegação](navegacao.md) · [Especificação](especificacao-telas.md)

**Proposta local 0.2, 08/11/2026.** Treze famílias organizam as funcionalidades. T11 é estado de processamento; D01–D04 são diálogos, não destinos principais. O Figma mantém os 84 frames v0.1, incluindo variantes e vinte estados AHP. Localmente são 86 descritores e projeções reutilizadas conforme categoria/seleção; não são 86 telas independentes. Os dois descritores adicionais ainda não existem no Figma.

| Família / IDs no manifesto | Nome e objetivo | Requisitos | Casos de uso |
|---|---|---|---|
| T01; T01-NOVA | Início/Categorias; escolher uma das duas categorias para consultar ou criar avaliação | RF01, RF04, RF05, RF10 | UC01, UC06 |
| T02 e variantes | Produtos por categoria; distinguir oficiais e pessoais, consultar e iniciar comparação | RF02, RF04, RF05, RF09, RF11 | UC01, UC03–UC05, UC07 |
| T03 e variantes | Detalhes do produto; cinco valores/unidades, origem, referência e ações pessoais | RF03, RF07, RF08, RF25 | UC02, UC04, UC05 |
| T04 e variantes | Cadastro/edição pessoal; identificação e cinco valores sob categoria fixa | RF06, RF07, RF09 | UC03, UC04, UC07 |
| T05 e variantes | Avaliações salvas; rascunho, concluída com ranking e concluída sem diferenciação | RF21, RF22, RF23, RF25, RF26 | UC12, UC13, UC15 |
| T06-0/A/B/AB; T06-MISTA; T06-BEV | Seleção de alternativas; mínimo de duas identidades da mesma categoria, incluindo origem pessoal | RF09, RF10, RF11, RF13, RF25 | UC06, UC07, UC14 |
| T07; T07-EDITAR | Valores da avaliação; referência original, efetivo ajustado e erros por campo | RF12, RF13, RF24, RF25 | UC08, UC14 |
| T08-01–10-P/R | Comparação AHP; um par por vez, sentido, intensidade e progresso | RF14, RF21 | UC09, UC12, UC14 |
| T09; E01 | Pesos e consistência; λmax, IC, RI e RC, bloqueio quando RC > 0,10 | RF15, RF16, RF17 | UC10, UC14 |
| T10; E02; E05; E13 | Revisão; dados utilizados, pesos e validações antes do processamento | RF13, RF18, RF24 | UC08, UC11, UC14 |
| T12; T12-SALVA; E03; E04 | Resultados; ranking/empates ou conclusão sem diferenciação e salvamento manual | RF18, RF19, RF20, RF24, RF25 | UC11, UC12, UC14 |
| T12-DETALHES | Detalhes da avaliação; valores preservados e explicação dos indicadores | RF20, RF25 | UC11, UC13, UC14 |
| T13 e variantes | Compartilhamento PDF; representação do sistema Android, após geração local elegível | RF27 | UC16 |
| T11 | Estado de processamento dentro do percurso revisão/resultados | RF18; RNF03 | UC11 |
| D01 e variantes | Saída pendente: Salvar / Descartar alterações / Continuar editando | RF12, RF21, RF24, RF25 | UC08, UC12, UC14 |
| D02 e variantes | Exportação pendente: Salvar e exportar / Exportar última versão salva / Cancelar | RF27 | UC16, UC12, UC14 |
| D03 e variantes | Confirmar exclusão permanente de avaliação ou produto pessoal | RF08, RF26 | UC05, UC15 |
| D04 | Ajuste de valor no contexto da avaliação, sem modificar catálogo | RF12, RF13 | UC08 |

Todos os RF01–RF27 e UC01–UC16 estão relacionados acima; isso significa cobertura de especificação, não implementação nem cobertura integral das interações. RNF01/02 orientam todas as famílias; RNF04/05/06 aplicam-se a validação, cálculo, salvamento e uso offline. RNF07/08 orientam consulta oficial, dados locais e compartilhamento. RNF09–RNF13 permanecem restrições de implementação/processo, sem novas telas.

## Estados representados e alcance

| Estado | Artefatos / IDs | Alcance nesta execução |
|---|---|---|
| AHP pendente e progresso | T08-01–10-P/R | Vinte frames e percurso local completo; nenhuma comparação recebe 1 antes de confirmação explícita. |
| AHP consistente | T09 | Igualdade nos dez pares: pesos 0,20; λmax 5; IC e RC 0. |
| AHP inconsistente | E01 | Fixture separada: RC ≈ 0,9524; TOPSIS desabilitado; rascunho permitido. |
| Ranking | T12 | Dois produtos fictícios, C* ≈ 0,7517 e 0,2483. |
| Empate | E03 | Fixture de quatro alternativas: 1º, 2º, 2º, 4º. |
| Sem diferenciação | E04, E04-SALVA, T05-SEMDIF | d+ = d− = 0; C* não definido; sem posição; conclusão salvável/exportável. |
| Falha matemática | E05, E13 | Norma zero e colisão na conversão monetária; sem resultado fictício. |
| Produto incompleto / valor ausente | E08, E02 | Campo ausente identificado; processamento bloqueado; rascunho possível. |
| Valor inválido / edição desatualizada | T07-EDITAR, E10 | Fração de centavo rejeitada; ranking da edição não é apresentado como atual. |
| Cache com data desconhecida | T02, T03 | Texto explícito; nenhuma data de leitura atual presumida. |
| Carregamento / erro de consulta | E06, E07 | Cenários visuais; tentativa de consulta simulada. |
| Falha ao salvar | E09 + controle externo da demonstração | No HTML, falha injetada mantém a tela/edição; não confirma sucesso. E09 é também um cenário isolado. |
| Alterações pendentes / rascunho salvo | D01, T05-RASCUNHO | HTML simula salvar, descartar e retomar exatamente o estado fixo salvo. Não grava no dispositivo. |
| Exclusão e falha | D03, E14, T05-EXCLUIDA | Confirmação e retornos; nenhuma exclusão real. |
| PDF pendente / falha | D02, E12, T13 | Seleção da versão elegível e preservação da edição; nenhum PDF real. |
| Definição incompatível | E11 | Não basta confirmar; novo cadastro pessoal sob definições adequadas. |
| Cadastro na seleção e falha | T04-SELECAO; T04-SELECAO-BEV | Localmente preserva seleção anterior; falha mantém formulário; salvar cadastro não seleciona automaticamente. |
| Avaliação BEV e seleção mista | Projeções T06/T07/T08/T09/T10/T12 | Localmente percorrem os cinco critérios da categoria e usam fixtures próprias; até três registros, sem criar limite funcional no app. |
| 360/600 dp e texto ampliado | QA-360-200, QA-600, QA-600-200; controles HTML | Amostras Figma precisam correção; verificação de geometria local registrada em [Validação](validacao.md). |

Faltam variantes visuais dedicadas para RC imediatamente acima do limite (ex.: 0,10000001), erro de permissão, cache parcial/vazio não confirmado, falha de leitura local, produto com original monetário incompatível, cadastro inválido e compartilhamento indisponível. Os comportamentos estão especificados e não podem ser convertidos em sucesso silencioso durante implementação.
