# Verificação da revisão UI/UX

[Índice](README.md) · [Parecer](revisao-uiux.md) · [Fontes reproduzíveis](prototipos/README.md)

**Rodada v0.3 · 08–09/11/2026 · branch `docs/etapa5-interface-navegacao`.** Git foi conferido limpo antes das edições, no HEAD integrado à Etapa 4. Requisitos/modelagem/decisões/arquitetura e código Android foram preservados. Evidências v0.1/v0.2 continuam históricas, sem serem apresentadas como resultado desta rodada.

## Resultados executados

| Verificação | Resultado e alcance |
|---|---|
| `verificar_documentacao.py` | PASS: 87 descritores de referência, 84 IDs Figma históricos, destinos válidos, links locais e âncoras, RF01–RF27/UC01–UC16, geração idêntica às fontes, sintaxe JS e SVG. Não consulta Figma nem valida a API de mutação. |
| `validar_fixture.py` | PASS: 13 cenários de referência recalculados em Python, com naturezas próprias de smartphones/BEV. |
| `verificar_matematica.cjs` | PASS: 234 verificações do calculador web, comparando pesos/λ/IC/RC, distâncias, coeficientes e postos com 13 fixtures; centavos/finitude/fração/overflow/colisão e julgamentos pendentes. [Relatório](prototipos/evidencias/revisao-v03/matematica-v03.json). |
| `verificar_navegador.py` | PASS: **283 verificações** de interações DOM reais em Chrome headless. [Relatório](prototipos/evidencias/revisao-v03/interacoes-v03.json). Não é teste de implementação Android. |
| Mesmo roteiro com `--movimento-reduzido` | PASS: 283 verificações; `matchMedia` confirmou preferência reduzida ativa. [Relatório](prototipos/evidencias/revisao-v03/interacoes-v03-movimento-reduzido.json). Abertura segue ao conteúdo sem repetição. |
| Responsividade e alvos | 16 telas/estados × 360/600 CSS px × 100/200% = **64 combinações**, sem corte horizontal dos elementos inspecionados, alvos de ambos os eixos ≥48 px e rótulos/ajudas associados. Diálogo de saída nas quatro combinações também cabe e permite rolagem. |
| `verificar_contraste.py` | PASS: **18 pares** sRGB; texto ≥4,5:1 e borda/foco ≥3:1. [Razões](prototipos/evidencias/revisao-v03/contraste-v03.json). Sem alegar auditoria de todos os estados Figma/Android. |
| Capturas e inspeção | Onze PNGs reais: três antes e oito depois, com origem/metadados. Inspecionados início, AHP, ranking, abertura, BEV, mistura, cadastro ampliado e diálogo. [Comparação e links](revisao-uiux.md#antes-e-depois-visual). |
| Figma | Sessão criada, tentativa de leitura bloqueada pelo limite MCP Starter; `whoami` confirmou o plano. Nenhum inventário atual, screenshot ou fluxo remoto foi verificado nesta revisão; nenhuma mutação efetuada. |
| Inspeção do plugin histórico | Três verificações preventivas em Node passaram sobre pacote regenerado em `/tmp`: arquivo divergente, controle ausente e inventário de referência. Nenhuma mutação/API Figma executada; ZIP/ledger anteriores preservados. |
| Escopo/Git | Hashes dos 99 arquivos protegidos conferidos; `git diff --check` sem erros. [Registro consolidado](prototipos/evidencias/revisao-v03/verificacoes-v03.json). |

A execução usa perfil temporário do Chrome, sem sessão pessoal, rede de catálogo, instalação de dependências ou armazenamento persistente. Os relatórios incluem data contextual e limites. Não foram feitos testes de desempenho Android, Room/Firestore, geração de PDF, serviço de compartilhamento ou entrega externa.

## Percursos e exceções exercitados

- **Entrada:** T00 presente no lançamento; animação/transição liberam catálogo sem consulta remota; sem criação/salvamento de avaliação; retorno/restauração não repetem abertura. Texto da abertura também conferido a 100/200%.
- **Smartphones:** mínimo de duas identidades; edição de preço preservando original/catálogo; dez respostas explícitas; AHP consistente; revisão; TOPSIS correspondente às entradas, resultado inicialmente não salvo. Percurso adicional de preferência 3 gera pesos/RC e coeficiente não uniforme conferidos com oráculo Python, sem resultado fixo fictício.
- **BEV:** seleção, valores, dez pares, pesos, revisão, resultado e gravação, preservando seguro anual, custo da recarga e critérios próprios.
- **Mista/cadastro:** erro obrigatório com foco no campo; falha de cadastro mantém formulário e sessão; sucesso retorna à mesma seleção sem associação automática; nomes iguais mantêm origem/ID; descartar avaliação não apaga produto pessoal já salvo.
- **Sessão e versões:** abrir concluída não marca mudança; editar valores invalida TOPSIS e conserva AHP; mudar julgamento invalida ambos; pendência/resultado novo conta como mudança; salvar texto inválido mantém rascunho e remove conclusão afetada; retomar conserva respostas parciais. Restauração usa somente versão salva.
- **Saída/foco:** três ações DEC-025, continuar preserva edição, diálogo recebe/devolve foco; falha de gravação mantém tela, edição e versão anterior com mensagem persistente. Voltar dos detalhes vai ao resultado; navegação interna não salva.
- **Matemática e compatibilidade:** inconsistência bloqueia; empate competitivo; conclusão válida sem diferenciação sem C*/posto/vencedor; norma zero é falha; fração de centavo e colisão Double rejeitadas; original/semântica incompatíveis bloqueados sem atalho pelo efetivo; novo cadastro não copia números antigos.
- **PDF/exclusão/consulta:** atual desatualizada bloqueia Salvar e exportar; exportar anterior mantém edição; falhas de PDF/compartilhamento não desfazem gravação nem afirmam envio; cancelar/falhar exclusão preserva registro; cache insuficiente, vazio confirmado e permissão negada distintos.

## Acessibilidade: o que foi e não foi testado

A geometria foi medida no DOM, não em dp de aparelho Android. Larguras são internas ao demonstrador em viewport desktop de 1440 × 1200 CSS px; texto é escalado pelo laboratório. Rótulos, `aria-describedby`, nomes dos campos repetidos com produto/critério/unidade, ordem DOM, mensagens com texto/símbolo, foco de diálogo e primeiro campo inválido foram conferidos por código e inspeção visual. A página e diálogos rolam verticalmente; cards/métricas refluem em 200%.

Isso não prova experiência com leitor de tela: **TalkBack, semântica Compose, teclado/IME, foco em aparelho, retorno do sistema, densidade/fontScale reais, rotação e morte do processo Android ainda não foram executados**. Não houve auditoria humana com tecnologia assistiva nem teste com participantes. O CSS respeita redução de movimento e a preferência foi efetivamente exercitada no Chrome; movimento no Figma continua pendente.

## Roteiro de apresentação e aceite restante

1. Abrir o HTML e mostrar a abertura. Explorar detalhes de produto e dados fictícios/cache com data desconhecida.
2. Criar Smartphones, selecionar dois e ajustar um valor. Confirmar igualdade ou explicar a escala completa, mostrar progresso e pesos/RC.
3. Revisar/processar, explicar ranking e salvar manualmente. Editar concluída, mostrar resultado desatualizado e as três opções de saída.
4. Demonstrar BEV e cadastro pessoal durante seleção; selecionar manualmente o novo produto.
5. Usar o laboratório para inconsistência, empate, sem diferenciação e falhas de salvamento/PDF. Mostrar exportação da anterior sem perder edição.
6. Mostrar 360/600 e texto 200%, com rolagem, indicando a limitação frente a Android/TalkBack.

Pode-se apresentar **o demonstrador HTML revisado**. Para fechar a Etapa 5, aplicar/verificar Figma e sua navegação, revisar a proposta com a equipe e verificar acesso do professor. Executar RNF01 com três participantes externos ao desenvolvimento, registrando ajuda e conclusão; RNF02 na implementação Android. Fontes/rubricas/imagens reais, vídeo e entrega formal seguem pendentes. Nenhum aceite ou envio ao Teams é declarado.

## Arquivos da revisão

Documentos atualizados: README, mapa, navegação, especificação, design system, validação e índice de protótipos; criado o parecer independente. Fontes novas: template/CSS/JS da UI, calculador demonstrativo, roteiro DOM, verificador matemático/contraste e capturador. Gerador/HTML/manifesto/verificadores documental e de navegador atualizados; projetor/expansor antigos identificados como históricos. SVG redesenhado com T00. Figma: script 05 e plano preparados, README do plugin com limite de versão; ledger, scripts de criação e ZIP anteriores preservados. PNGs/JSONs da rodada em `prototipos/evidencias/revisao-v03/`.
