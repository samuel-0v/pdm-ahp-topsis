# Identidade visual e componentes

[Índice](README.md) · [Figma — fundações e componentes](https://www.figma.com/design/S1Ba7mzFVfE1d0ds21FR36)

**Proposta visual, sujeita à revisão.** Material Design 3 orienta a composição Android/Jetpack Compose. A identidade usa superfícies claras, violeta para ações e mensagens com texto além de cor. Não inclui marca ou logotipo definitivo, tema escuro ou gráficos novos. A abertura/splash animada integra o escopo de apresentação exigido pelo enunciado, conforme [DEC-049](../decisoes/adotadas/dec-049.md) e [especificação da abertura](especificacao-telas.md#aberturasplash--exigência-do-enunciado). Sua representação visual e animação ainda precisam ser aplicadas e verificadas no Figma, HTML e manifesto; não foram implementadas por esta correção editorial.

## Cores propostas

| Papel | Cor | Uso |
|---|---|---|
| Primary / OnPrimary | `#65558F` / `#FFFFFF` | Ação principal e seu texto |
| PrimaryContainer / OnPrimaryContainer | `#EADDFF` / `#21005D` | Ênfase tonal e seleção |
| Surface / OnSurface | `#FDF7FF` / `#1D1B20` | Fundo e conteúdo |
| SurfaceContainer / Secondary | `#F3EDF7` / `#49454F` | Mensagens neutras e navegação |
| Outline | `#79747E` | Limites dos campos e cards |
| Success / SuccessContainer | `#245D3C` / `#D6F5DE` | Consistência ou gravação confirmada |
| Error / ErrorContainer | `#B3261E` / `#F9DEDC` | Erros, inconsistência e bloqueios |
| Warning / WarningContainer | `#704B00` / `#FFF0C2` | Pendências e dados incompletos |

Os pares de texto acima são conferidos por cálculo de contraste; ver [evidências](validacao.md). Eles não comprovam contraste de cada nó importado do Figma. Componentes M3 importados mantêm variáveis próprias; alinhar suas cores ao tema local na revisão do arquivo.

## Tipografia e espaçamento

Roboto é a referência principal e está disponível no Figma. O HTML usa `Roboto, Arial, sans-serif` sem buscar fontes remotas; se Roboto não estiver instalado, o navegador emprega a alternativa. As capturas locais não são evidência de equivalência tipográfica pixel a pixel com o Figma.

| Papel M3 | Tamanho / altura de linha, referência padrão |
|---|---|
| Headline large | 32 / 40 sp |
| Title large | 22 / 28 sp |
| Body large | 16 / 24 sp |
| Body medium e Label large | 14 / 20 sp; label medium |
| Body small | 12 / 16 sp |

Unidade base: 4 dp. Espaços: 4, 8, 12, 16, 24 e 32 dp. Margem de conteúdo: 16 dp em 360; proposta de até 24 dp em 600, sem comprimir campos. Raios: 12 dp para mensagens, 16 para cards e 28 para botões/diálogos. A geometria nunca deve impedir expansão de texto.

## Componentes e estados

| Componente | Variações / regra |
|---|---|
| Botão principal | Habilitado, pressionado, desabilitado e carregando; mínimo 48 × 48 dp, referência 56 dp. Uma ação de avanço predominante por etapa. |
| Botão secundário | Contorno/texto; salvar rascunho, revisar, voltar e cancelar. Rótulos completos e quebra de linha quando necessário. |
| Campo delineado | Rótulo persistente, valor, unidade, ajuda e erro textual. Teclado numérico/decimal no Android conforme tipo; validação não depende do teclado. |
| Card de produto | Nome/modelo/versão conhecidos, origem oficial/pessoal, incompletude e seleção por símbolo + texto. |
| Card de resultado | Posição quando definida, C*, d+, d−; destaque ao vencedor único. Empates compartilham posição, sem desempate visual arbitrário. |
| Progresso AHP | Contagem exata de pares respondidos / 10 e barra proporcional; pendente é diferente de igual importância. |
| Mensagem PDM/Estado | Info, Sucesso, Erro e Aviso; texto exposto por propriedade do componente. |
| Diálogo | Título, consequência e ações empilhadas em telas estreitas. DEC-025 sempre com três escolhas; exclusão com confirmar/cancelar. |
| Navegação principal | Catálogo e Avaliações, com rótulos. Dentro da edição, barra com Voltar e Sair; sem troca de seção que ignore mudanças pendentes. |

No Figma foi reutilizada a biblioteca **Material 3 Design Kit**: Button, Button - outline, Text field, Basic dialog, Horizontal card e Linear-determinate progress indicator, além de estilos de texto. A busca não encontrou um composto de avaliação pronto. Não foram encontrados arquivos Code Connect do projeto. Foram criados tokens locais com aliases e escopos, e o componente de mensagens com quatro variantes. Não houve publicação de biblioteca nem mapeamento para Kotlin.

## Responsividade e acessibilidade

- Referências aprovadas: retrato em 360 e 600 dp, com fontes a 100% e 200%; rolagem vertical e alturas adaptáveis. Não declarar paisagem validada.
- Em tela estreita, valores por produto ficam em blocos verticais; a revisão repete os cinco critérios/unidades em vez de exigir leitura de uma tabela horizontal extensa.
- Conteúdo e ações devem continuar acessíveis quando o texto cresce. No HTML toda a composição cresce e usa a rolagem da página. No Figma há área de conteúdo rolável; títulos ampliados e barras precisam os ajustes registrados.
- Ordem de leitura: título → situação → progresso → dados/erro → ações. Para o Android, descrições como “Aurora Pro, oficial, selecionado” e “3 de 10 comparações respondidas”; nomear Voltar, Salvar e cada campo com critério/unidade/produto.
- Mensagens de erro e consistência não usam somente cor. Associar erro ao campo; anunciar conclusão de cálculo e falha sem roubar foco continuamente. Diálogo deve receber foco e devolvê-lo ao controle de origem.
- Comparar RC interno diretamente com 0,10. Exibir precisão adicional quando quatro casas ocultarem o bloqueio; nunca liberar TOPSIS por arredondamento visual.
- Respeitar preferência por menos animação. O indicador de atividade não deve introduzir atraso artificial no app. A transição de 300 ms do storyboard local é apenas simulação.

TalkBack, semântica Compose, contraste de todos os estados importados e verificação no dispositivo físico continuam pendentes. Os componentes são uma referência editável; as restrições aprovadas prevalecem sobre suas dimensões atuais.
