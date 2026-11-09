# Mapa de telas e rastreabilidade

[Índice da Etapa 5](README.md) · [Navegação](navegacao.md) · [Especificação](especificacao-telas.md)

**Revisão local v0.4, 09/11/2026.** Quatorze famílias, incluindo T00; T11 é um estado e D01–D04 são diálogos/referências, não novos destinos principais. O manifesto tem 87 descritores de storyboard, dos quais 84 constam do ledger Figma histórico. O HTML atual renderiza famílias e sessão próprias em `prototipo.js`, em vez de projetar coeficientes de frames estáticos. Não existe correspondência 1:1 entre cada estado editável e um frame Figma. O estado atual do arquivo remoto não pôde ser lido nesta revisão.

| Família / IDs no manifesto | Nome e objetivo | Requisitos | Casos de uso |
|---|---|---|---|
| T00 — Abertura animada | Identificar o aplicativo com animação e seguir ao conteúdo inicial; fora da sessão de avaliação | Enunciado v2, p. 4; [DEC-049](../decisoes/adotadas/dec-049.md); sem novo RF | Entrada do app, conduzindo a UC01 no percurso normal; sem novo UC |
| T01; T01-NOVA | Início/Categorias; escolher uma das duas categorias para consultar ou criar avaliação | RF01, RF04, RF05, RF10 | UC01, UC06 |
| T02 e variantes | Produtos por categoria; distinguir oficiais e pessoais, consultar e iniciar comparação | RF02, RF04, RF05, RF09, RF11 | UC01, UC03–UC05, UC07 |
| T03 e variantes | Detalhes do produto; cinco valores/unidades, origem, referência e ações pessoais | RF03, RF07, RF08, RF25 | UC02, UC04, UC05 |
| T04 e variantes | Cadastro/edição pessoal; identificação e cinco valores sob categoria fixa | RF06, RF07, RF09 | UC03, UC04, UC07 |
| T05 e variantes | Avaliações salvas; rascunho, concluída com ranking e concluída sem diferenciação | RF21, RF22, RF23, RF25, RF26 | UC12, UC13, UC15 |
| T06 (HTML); T06-0/A/B/AB, T06-MISTA, T06-BEV (storyboard) | Seleção de alternativas; mínimo de duas identidades da mesma categoria, incluindo origem pessoal | RF09, RF10, RF11, RF13, RF25 | UC06, UC07, UC14 |
| T07; T07-EDITAR | Valores da avaliação; referência original, efetivo ajustado e erros por campo | RF12, RF13, RF24, RF25 | UC08, UC14 |
| T08, T08-LISTA (HTML); T08-01–10-P/R (storyboard) | Comparação AHP; um par por vez, sentido, intensidade e progresso | RF14, RF21 | UC09, UC12, UC14 |
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

| Estado | Representação local v0.3 | Limite Figma |
|---|---|---|
| Abertura e transição | T00; animação, pulo, redução de movimento; não reaparece por retorno/restauração | T00 preparado no script 05; não criado/verificado remotamente. |
| AHP pendente/progresso | T08; dez pares, três sentidos, intensidades 2–9 e igualdade 1 explícitos; T08-LISTA permite revisão | Vinte frames históricos de igualdade; sentidos/intensidades completos ainda precisam ser portados. |
| AHP consistente/inconsistente/desatualizado | T09; pesos/λmax/IC/RI/RC calculados; TOPSIS bloqueado quando necessário | E01 e T09 históricos; correções não verificadas. |
| Ranking/empate | T12; calculado das entradas; cenário de quatro alternativas com posições 1º, 2º, 2º, 4º | T12/E03 históricos; novo layout não aplicado. |
| Sem diferenciação | C* indefinido, sem posições/vencedor; conclusão salvável/exportável | E04 histórico; novo layout não aplicado. |
| Dados insuficientes/inválidos | Campos editáveis, ausente ≠ zero, erro por campo e rascunho com texto pendente | E02/E08 históricos; formulário atual precisa ser portado. |
| Falha matemática | Norma zero, finitude, perda de resolução e erros de precisão sem resultado fictício | E05/E13 históricos; novos controles não aplicados. |
| Cache/consulta | Cache com data desconhecida, carregamento, cache insuficiente, vazio confirmado, permissão negada e erro | E06/E07 históricos; variantes novas pendentes. |
| Gravação/rascunho | Falha preparada, mensagem persistente, edição preservada; múltiplas versões salvas em memória da página | Script 03 preparado; nenhuma navegação atual confirmada. |
| Edição concluída e saída | Abrir sem alterar mantém resultado atual; mudanças invalidam dependências; D01 com três ações | D01 histórico; corrigir contexto do detalhe e verificar retorno. |
| Exclusão/PDF | Confirmação; cancelamento e falhas; D02 escolhe versão; compartilhamento somente simulado | D02/D03/T13 históricos; runtime não verificado. |
| DEC-041/042 | Candidato bloqueado; efetivo não contorna original; novo cadastro vazio para nova semântica | E11/E13 não cobrem automaticamente os novos cenários; portar. |
| BEV/mista/cadastro durante seleção | Cinco critérios próprios, identidade origem+ID, valores livres; cadastro retorna sem autoselecionar | Expansão ainda pendente no arquivo remoto. |
| Texto/largura/foco | 360/600 CSS px × 100/200%; 16 telas/estados e diálogos medidos; capturas inspecionadas | Amostras QA históricas não comprovam correção atual. |

O cadastro não cria máximo de três alternativas: os dois oficiais por categoria são dados ilustrativos, e novos pessoais podem ser criados na sessão. A matriz acima registra cobertura de demonstração, não certificação de implementação. Falha de leitura do armazenamento Android, integração real do compartilhamento e metadados de catálogo reais continuam fora da simulação.

Na v0.4, T03 reflete imediatamente cadastro pessoal editado, preservando snapshots; T07 focaliza e traz à vista o primeiro erro; T12 rotula empates também em postos intermediários; D03 inicia foco em Cancelar e mantém falha de exclusão visível. Não foram acrescentadas famílias ou regras de negócio. Ver [correções e evidências](finalizacao-v04.md).
