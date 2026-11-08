# Especificação das telas

[Mapa, RFs e UCs](mapa-telas.md) · [Navegação](navegacao.md) · [Identidade visual](design-system.md)

**Proposta local 0.2 para revisão, 08/11/2026.** O [manifesto](prototipos/telas.json) contém descritores, ações e destinos; o projetor local adapta conteúdo por categoria/seleção. O Figma conserva a versão nativa 0.1. Esta especificação inclui comportamentos ainda não clicáveis; consulte o [alcance do protótipo](prototipos/README.md).

## Catálogo e produtos — T01 a T04

T01 reúne início e categorias, sem splash temporizado obrigatório. A entrada de consulta abre produtos; a entrada “Nova avaliação” escolhe categoria e cria edição em memória. Apenas as duas categorias aprovadas são apresentadas.

T02 separa oficiais/pessoais da categoria por abas ou ações equivalentes. Mostra identificação, origem e incompletude; oferece detalhes, cadastro pessoal e iniciar avaliação. O protótipo usa ações textuais para alternar. Não introduz busca, filtros, categorias personalizadas ou limite de 50 produtos.

T03 é somente leitura para oficial. Mostra fabricante/modelo/versão conhecidos, cinco critérios, valores, unidades e referências disponíveis. Produtos pessoais oferecem editar/excluir com D03. Fonte, data de referência, protocolo/rubrica, perfil e estimativa desconhecidos não se transformam em informação confirmada. ADAS da equipe não é certificação de segurança.

T04 reúne cadastro e edição pessoal: identificação, fabricante/modelo/versão e cinco valores. Categoria é escolhida antes do cadastro e imutável na edição. Campos obrigatórios inválidos impedem gravar produto; falha mantém formulário e não confirma sucesso. Cadastro iniciado pela seleção retorna à mesma avaliação e disponibiliza o novo registro sem alterar os já selecionados. O HTML demonstra essa interação e sua falha com valores fixos; o Figma ainda precisa recebê-la.

| Categoria | Cinco critérios, unidades e natureza |
|---|---|
| Smartphones Premium | Geekbench 6 Multi-Core, pontos (benefício); preço, R$ (custo); câmeras, nota 1–10 (benefício); autonomia, h (benefício); armazenamento, GB (benefício). |
| Veículos Elétricos (BEV) | Autonomia Inmetro/PBEV, km (benefício); aquisição, R$ (custo); recarga DC 20–80%, min (custo); seguro anual, R$/ano (custo); ADAS, nota 1–10 (benefício). |

Autonomia e rubricas exigem protocolos comparáveis; veículo identifica versão/ano e seguro identifica perfil conhecido. Fontes, protocolos concretos e demais domínios/precisões permanecem pendentes; não foram inventados para preencher os mockups.

Estados de consulta: carregando, dados confirmados, cache disponível, vazio confirmado, indisponível sem cache, permissão negada e documento incompatível. Cache parcial/vazio não comprova catálogo remoto vazio. Datas de referência, atualização do publicador e confirmação de consulta têm rótulos distintos; origem cache não fornece data por inferência.

## Avaliações e seleção — T05 e T06

T05 mostra somente versões efetivamente salvas: **Rascunho**, **Concluída com ranking**, **Concluída — sem diferenciação**. Rascunho retoma configuração; concluída abre consulta, sem recálculo automático. Vazio oferece nova avaliação; falha de leitura não é vazio. D03 confirma exclusão permanente e só remove após sucesso. Dados locais podem ser perdidos por desinstalação/perda do aparelho (RNF08).

T06 mostra categoria fixa, alternativas oficiais/pessoais, seleção e contagem. Impedir duplicação por **origem + ID**, sem unir nomes/modelos iguais. Mistura de origens é permitida na mesma categoria. Menos de dois produtos permite rascunho e bloqueia processamento. A proposta bloqueia também o avanço linear até selecionar duas; isso é organização visual, não novo requisito matemático.

O HTML v0.2 oferece até três registros preparados por categoria: dois oficiais e um pessoal, com identidade diferente mesmo quando tem o mesmo nome de um oficial. Todas as combinações de dois/três registros têm fixture coerente; selecionar cópias com os mesmos valores produz conclusão válida sem diferenciação, e o conjunto de três preserva empate quando aplicável. Esse inventário ilustrativo não cria máximo funcional de três alternativas.

Ausência real ou original canônico fora do domínio pode ser corrigido no efetivo antes do cálculo. Original monetário em formato incompatível bloqueia seleção até correção na fonte; preencher efetivo não contorna a regra. Incompatibilidade semântica ou informação essencial indeterminada bloqueia reutilização dependente dela. Produto pessoal semanticamente incompatível exige novo cadastro com definições adequadas, sem copiar/converter números automaticamente (DEC-041/042).

## Valores da avaliação — T07

Apresentar cinco critérios por produto com unidade, natureza, referência original e efetivo ajustado; marcar ajustes. Usar blocos verticais em 360 dp. O cenário principal mostra preço original de R$ 5.200,00 e efetivo de R$ 5.000,00; o catálogo permanece intacto.

Ausente não vale zero. Entrada inválida é erro textual próximo ao campo; rascunho pode preservar texto pendente sem número fictício, mas produto pessoal não pode ser salvo inválido. Interpretar pt-BR sem separadores ambíguos. Fração de centavo não zero é rejeitada, sem arredondamento/truncamento silencioso; zeros decimais excedentes exatos são permitidos conforme DEC-041. Não impor duas casas a todos os critérios nem teto de preço novo. Notas de câmeras/ADAS ficam em 1–10; demais validações vêm dos metadados preservados.

Preservar fonte/data do original; marcar efetivo ajustado e não herdar referência como se descrevesse o novo número. Mudar valores/alternativas desatualiza TOPSIS; AHP continua atual se julgamentos/critérios não mudaram. Versão salva só é afetada após gravação explícita.

## Comparações e consistência — T08 e T09

Um par por vez, pergunta de importância, sentido da preferência e intensidade discreta 1–9: 1 igual, 3 moderada, 5 forte, 7 muito forte, 9 extrema; 2/4/6/8 intermediárias. Favorecer o segundo critério preenche o recíproco automaticamente. Importância do preço não significa que preço maior seja melhor; custo/benefício não muda.

Sem resposta permanece pendente, sem igualdade implícita. Contagem refere-se a respostas, não ao índice da página. Revisar conserva dados; alterar um par revoga aprovação anterior. Ordem proposta dos dez pares: Geekbench–Preço, Geekbench–Câmeras, Geekbench–Autonomia, Geekbench–Armazenamento, Preço–Câmeras, Preço–Autonomia, Preço–Armazenamento, Câmeras–Autonomia, Câmeras–Armazenamento, Autonomia–Armazenamento.

Para BEV, o HTML projeta a mesma sequência de índices sobre Autonomia Inmetro/PBEV, Preço de aquisição, Recarga DC 20–80%, Seguro anual e ADAS. Assim há dez pares da categoria correta; seguro permanece anual, recarga é custo e não se reutilizam critérios de smartphone.

T09 apresenta cinco pesos, λmax estimado, IC, RI=1,12 e RC. RC ≤ 0,10 permite revisão/TOPSIS se demais dados forem válidos. RC > 0,10 bloqueia e oferece revisar ou salvar rascunho. Comparar valor interno diretamente; mostrar casas adicionais quando o arredondamento ocultar a diferença. Falha numérica não é diagnóstico de inconsistência nem produz pesos parciais válidos.

O roteiro confirma igualdade nos dez pares: pesos 20%, λmax=5 e IC=RC=0. Outros sentidos/intensidades estão especificados, sem cálculo livre no protótipo. E01 é cenário separado: ciclo de preferências com intensidade 9 e RC ≈ 0,9524. Não ligar uma resposta isolada a esse diagnóstico como se tivesse produzido todo o cenário.

## Revisão e processamento — T10 e T11

Exibir categoria, identificação, cinco valores efetivos por alternativa, unidades/naturezas, pesos e consistência atuais; ações de revisão mantêm sessão. Validar pelo menos duas identidades distintas, cinco valores, finitude/tipos/domínios, compatibilidade e condições matemáticas. Nova avaliação pode processar e salvar diretamente como concluída sem rascunho salvo prévio.

Norma zero, falta de valor, AHP não aprovado/desatualizado ou colisão monetária na conversão bloqueiam cálculo com identificação do critério. Não converter essas falhas em empate/sem diferenciação. Usar snapshots, sem substituir valores pelo catálogo atual.

T11 é estado de atividade, não função independente. Evitar processamento duplicado; manter controles pertinentes responsivos e não publicar retorno tardio numa sessão abandonada. Cancelamento/identificação da operação dependem da arquitetura. A transição local de 300 ms é simulação, sem medição de desempenho real.

## Resultados e detalhes — T12

Ranking apresenta produto, posição, C*, d+, d−; detalhes disponibilizam pesos, λmax, IC, RI, RC e valores utilizados. Destacar apenas vencedor único. Empates usam coeficientes internos e tolerância 10⁻⁹, com posições 1º/2º/2º/4º; igualdade após arredondamento não define empate. Não arredondar cálculos intermediários.

Sem diferenciação apresenta d+=d−=0, C* não definido e nenhuma posição/vencedor fictício. É conclusão válida salvável/exportável, distinta de falha matemática. Resultado novo mostra “Ainda não salvo”; sucesso de salvar só após confirmação. Consulta salva não recalcula nem depende de catálogo atual. Falha de novo recálculo preserva versão salva; atualização isolada do motor não autoriza substituição silenciosa.

Detalhes compartilhados entre consulta e edição precisam respeitar contexto: excluir somente registro salvo; exportar somente versão elegível. O Figma atual ainda tem ação de exclusão no detalhe compartilhado sem essa condição: mover/condicionar na revisão. Não é autorização para excluir uma avaliação nunca salva.

## Diálogos, saída e PDF — D01 a D04 / T13

D01 contém Salvar, Descartar alterações e Continuar editando. Falha mantém edição e versão salva. D03 nomeia objeto e consequência permanente, com confirmar/cancelar; excluir produto não modifica snapshots das avaliações. D04 representa ajuste local e referência original.

D02 distingue salvar edição processada, exportar integralmente a última versão elegível e cancelar. Se precisar recalcular, Salvar e exportar fica indisponível com explicação; sem versão anterior elegível, a segunda opção também fica indisponível. Falha de PDF após gravação não desfaz a gravação. Compartilhamento cancelado, indisponível ou com falha não confirma envio. Conteúdo do PDF é o do RF27, sem matrizes intermediárias.

Regras de retorno e Voltar estão em [Navegação](navegacao.md); rotas/ViewModels/repositórios em [pendências](README.md#o-que-depende-da-arquitetura). Nenhuma tela Kotlin ou configuração de persistência foi criada.
