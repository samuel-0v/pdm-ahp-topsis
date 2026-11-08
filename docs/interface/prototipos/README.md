# Protótipos editáveis e retomada

[Etapa 5](../README.md) · [Validação](../validacao.md)

## Abrir a demonstração

Abrir [index.html](index.html) diretamente no navegador. O HTML é autocontido, não instala dependências, não consulta rede para dados/fontes e não requer servidor. O link Figma só abre se acionado. Os controles externos à interface simulada selecionam cenários, largura 360/600, texto 100%/200%, falha no próximo salvamento e Voltar.

Roteiro principal: Smartphones Premium → Iniciar avaliação → selecionar Aurora Pro e Vértice Max → Conferir valores → Comparar critérios → confirmar Igual importância nos dez pares → Validar AHP → Revisar avaliação → Processar TOPSIS → Salvar avaliação. Usar rolagem para acessar todos os dados; não há informação essencial dependente de arrastar horizontalmente.

O rascunho simula em memória tela, categoria, seleção e respostas já dadas. Para testar, salvar, reabrir e continuar; ou escolher “Retomar rascunho parcial”. Recarregar a página reinicia a simulação. O catálogo pessoal simulado é separado da avaliação: salvar um produto retorna à seleção preservada e não o seleciona automaticamente. O laboratório representa um registro de avaliação por vez; não é gerenciador real de múltiplos registros. Não há Room, Firebase, armazenamento web persistente, envio de dados ou PDF real.

Na continuação de **08/11/2026**, a branch `docs/etapa5-interface-navegacao` foi criada e selecionada. O percurso BEV passou a usar os cinco critérios próprios durante seleção, valores, dez pares AHP, pesos, revisão, ranking e salvamento. É possível cadastrar um pessoal durante seleção, selecionar oficiais/pessoais de mesmo nome, processar duas ou três alternativas e preservar empates/ausência de diferenciação conforme fixtures. A abertura de uma concluída para edição agora começa sem alteração pendente.

## O que foi criado no Figma

- [Arquivo de design real](https://www.figma.com/design/S1Ba7mzFVfE1d0ds21FR36), em área separada, após o usuário informar que não existia arquivo anterior.
- Página **01 · Protótipo** com 84 frames editáveis: telas, variantes, vinte estados AHP, diálogos, exceções e amostras de responsividade.
- Página **02 · Componentes** com paleta, estilos de texto, variáveis de cor/espaço/raio, mensagens reutilizáveis em quatro variantes e amostras Material 3.
- Instâncias de Button, Button - outline, Text field, Basic dialog, Horizontal card e Linear-determinate progress indicator, da biblioteca Material 3 Design Kit. Os frames usam texto, instâncias e auto-layout, sem capturas de tela achatadas como UI.

**O arquivo ainda não tem conexões de fluxo aplicadas.** Em 08/11 a leitura confirmou os 84 frames; as reações observadas são internas aos componentes importados e não comprovam navegação entre telas. A tentativa de executar `04-ajustes-layout.js` foi novamente bloqueada com `You've reached the Figma MCP tool call limit on the Starter plan`; `whoami` confirmou Starter. Nenhuma mutação dessa continuação foi confirmada. Nenhum link de apresentação navegável é declarado. Permissões por outra conta não foram testadas.

## Limites e achados registrados antes de corrigir

| Achado na revisão Figma | Tratamento preparado / trabalho restante |
|---|---|
| Botões M3 estreitos quebravam rótulos e aumentavam altura | Ajustado nas primeiras telas e no construtor antes de criar as demais; ainda revisar Voltar/Sair e todas as instâncias. |
| Título ampliado da variante 360/200% ultrapassa limites; captura de resultado mostrou barra superior comprometida | `04-ajustes-layout.js` prepara largura fixa do contêiner e quebra por altura. **Não executado nem validado por screenshot.** |
| Biblioteca de progresso só oferece 0/10/20/50/80/100%; alguns pares usam aproximação visual | Contagem textual é exata; `04-ajustes-layout.js` prepara componentes 0–10 exatos. **Não aplicado.** |
| Detalhe compartilhado oferece excluir também vindo de resultado não salvo | No HTML, opção depende da existência de versão salva. O script de layout prepara mover a ação para consulta salva. **Pendente no Figma.** |
| M3 importado mantém variáveis próprias | Revisar consistência das cores com a paleta proposta e contraste de todos os estados importados. |
| Frames sem reações e sem pontos iniciais | `03-navegacao.js` prepara reações, diálogos, variáveis de simulação e pontos iniciais; **não executado**. |

Esses achados não mudam RFs/DECs. Os estados corretos são a referência para corrigir o protótipo, e não o inverso.

## Alcance das interações locais

Seleção de zero/uma/duas alternativas, dez respostas explícitas, revisão, resultado e salvamento são clicáveis. O aviso de saída demonstra Salvar, Descartar alterações e Continuar editando; falha injetada mantém a mesma edição. É possível retomar rascunho, consultar versão salva, editar uma concluída, exportar a versão anterior sem descartar a edição, confirmar exclusão e demonstrar ausência de diferenciação.

Os dados são cenários preparados. Campos não recebem texto livre; AHP confirma igualdade. Outras intensidades/sentidos estão especificados, sem motor matemático no HTML/Figma. O projetor escolhe fixtures previamente verificadas para categoria e conjunto de alternativas, sem calcular um ranking livre no navegador. Há oito combinações processáveis adicionais: duas categorias × pares oficial/oficial, oficial/pessoal, duas cópias equivalentes e três alternativas. O terceiro registro tem o mesmo nome de um oficial e outra identidade. Inconsistência/falha continuam cenários separados, não resultados de uma resposta isolada do fluxo principal.

## Fontes editáveis

| Arquivo | Finalidade |
|---|---|
| [telas.json](telas.json) | 86 descritores locais; somente os 84 originais têm IDs Figma. Projeções em `contextos.js` adaptam as famílias conforme categoria/seleção. |
| [contextos.js](contextos.js) | Projeções de seleção, valores, pares AHP, revisão e resultados a partir de fixtures fixas; incorporado ao HTML. |
| [expandir_cenarios.py](expandir_cenarios.py) | Preparação reproduzível de cenários BEV/mistos e descritores locais adicionais; não grava no Figma. |
| [index.html](index.html) | Protótipo navegável e estilos editáveis, autocontido. |
| [mapa-navegacao.svg](mapa-navegacao.svg) | Resumo visual editável do fluxo e das escolhas de saída. O diagrama detalhado permanece em `navegacao.md`. |
| [atualizar_html.py](atualizar_html.py) | Reincorpora `telas.json` no HTML, preservando código e estilos. |
| [dados-demonstracao.json](dados-demonstracao.json) | Treze cenários numéricos, matrizes AHP, valores, natureza dos critérios e resultados. Não é catálogo real. |
| [validar_fixture.py](validar_fixture.py) | Conferência independente dos números ilustrativos. Não é código ou teste do app Android. |
| [verificar_navegador.py](verificar_navegador.py) | Roteiro automatizado de cliques em Chrome headless e verificação de geometria local. |
| [verificar_documentacao.py](verificar_documentacao.py) | Verifica links locais, destinos, IDs, cobertura RF/UC, dados embutidos e SVG. |
| [figma/estado-criacao.json](figma/estado-criacao.json) | IDs realmente retornados pelo Figma, frames, dependências e registro da criação. Não contém conexão aplicada. |
| [figma/01-fundacoes.js](figma/01-fundacoes.js) | Fonte da criação de tokens/componentes; **já executada**. Não repetir sobre o arquivo atual. |
| [figma/02-telas.js](figma/02-telas.js) | Fonte da montagem em lotes; **já executada** para todos os 84 frames. |
| [figma/04-ajustes-layout.js](figma/04-ajustes-layout.js) | Correções **preparadas, não executadas**, a inspecionar e validar. |
| [figma/03-navegacao.js](figma/03-navegacao.js) | Conexões **preparadas, não executadas**; dependem dos IDs do arquivo criado. |
| [figma/spec-criada-v01.json](figma/spec-criada-v01.json) | Metadados dos 84 frames nativos, separados das projeções locais posteriores. |
| [Registro da continuação de 08/11](figma/continuacao-2026-11-08.json) | Branch, leitura confirmada, bloqueio de gravação e alcance dos novos artefatos. |
| [Plugin local de aplicação](figma/plugin-local/README.md) | Fontes, empacotador, interface e [ZIP de revisão](figma/plugin-local/pacote-revisao.zip). Sem ID real, não é pacote instalável; não foi executado no Figma. |
| [evidencias/](evidencias/) | Conferência de números/contraste/interações e capturas locais; não são capturas finais do Figma. |

## Retomar no Figma sem sobrescrever

1. Carregar as skills `figma-use`, `figma-generate-design` e `figma-generate-library`; ler o ledger e a especificação nativa v0.1 e inspecionar o arquivo/IDs antes de mutar. Reutilizar a sessão da mesma atividade quando válida, conforme o servidor. Nunca repetir criação em arquivo existente sem inspeção. Alternativa ao MCP bloqueado: seguir o pacote local de aplicação, ainda não executado.
2. Injetar `LEDGER` a partir de `estado-criacao.json` e `SPEC` a partir de `telas.json` no script 04. Aplicar correções em lotes seguros e registrar todos os IDs retornados. Ajustes preparados precisam de validação de API e visual, não são declarados solução já verificada.
3. Corrigir qualquer corte, sobreposição, campo ou contraste ainda incorreto. Conferir títulos em 360/600 e texto 200%, estados importados e progresso exato. Capturar evidência após correção.
4. Injetar os mesmos dados e `RANGE=[início,fim]` no script 03, por lotes de frames; o payload de cada chamada deve ficar abaixo do limite de 50.000 caracteres observado. Validar especialmente expressões/variáveis condicionais e retorno dos overlays no runtime real.
5. Conferir reações e seus destinos, fluxo principal, três ações da DEC-025, rascunhos, exportação e falha ao salvar. Acrescentar as conexões de falha simulada, que ainda não estão completas no script Figma. Não considerar apenas a presença de uma linha de conexão como teste de navegação.
6. Completar intensidades/sentidos AHP, ajustes livres e erros secundários; portar as projeções locais de seleção mista/BEV para frames/componentes Figma quando autorizado e disponível. Testar o link com a conta do avaliador e registrar URL de apresentação somente quando o fluxo estiver navegável.

## Reproduzir as verificações locais

```bash
python3 docs/interface/prototipos/atualizar_html.py
python3 docs/interface/prototipos/verificar_documentacao.py
python3 docs/interface/prototipos/validar_fixture.py
python3 docs/interface/prototipos/verificar_navegador.py
git diff --check
```

O último script pressupõe Chrome em `/opt/google/chrome/chrome`, usado neste ambiente, e escreve temporários/relatório em `/tmp`. Usa `--no-sandbox` apenas no navegador de validação isolado com dados fictícios; pode exigir autorização fora do sandbox de execução. Não usa sessão pessoal do navegador. Não instala pacote, não inicia serviço e não envia relatórios. Essa limitação de ambiente deve ser adaptada em outra máquina.
