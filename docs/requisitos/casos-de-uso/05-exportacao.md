# Caso de uso — Exportação

**Versão:** 1.0 — detalhamento da DEC-032 aprovada, registrado conforme autorização do usuário.

[Índice dos casos de uso](README.md) · [RF27](../funcionais/07-exportacao.md) · [Rastreabilidade](../rastreabilidade.md)

## UC16 — Exportar avaliação em PDF

**Objetivo:** obter um relatório legível da avaliação e disponibilizá-lo para compartilhamento.
**Ator principal:** usuário. **Sistema de apoio:** compartilhamento padrão do Android e aplicativo/destino escolhido.
**Gatilho:** solicitar exportação da avaliação.
**Pré-condição:** versão salva e processada elegível, ou edição com processamento atualizado que possa ser salva antes da exportação.
**Requisitos:** RF27, RF20, RF24, RF25; RNF01, RNF02, RNF04–RNF06, RNF08; DEC-032.

**Fluxo principal:**

1. O usuário solicita exportar uma avaliação salva e processada, sem alterações pendentes.
2. O sistema verifica que o processamento corresponde à versão salva.
3. Gera localmente o PDF com o conteúdo aprovado no RF27, a partir dos dados preservados nessa versão.
4. Abre o compartilhamento padrão do Android.
5. O usuário escolhe um destino disponível e decide enviar o arquivo.

**Alternativas e exceções:**

- A1 — Alterações pendentes: apresentar Salvar e exportar, Exportar última versão salva e Cancelar.
- A2 — Salvar e exportar: verificar processamento atualizado e salvar pelo UC12; somente depois do sucesso, retomar o passo 3. Se houver necessidade de recálculo, informar e encaminhar aos fluxos UC08–UC11/UC14 antes de permitir exportar a edição.
- A3 — Exportar última versão salva: opção disponível apenas se essa versão for processada; retomar o passo 2 usando somente os dados persistidos. A edição em memória permanece pendente, sem descarte ou gravação implícitos.
- A4 — Cancelar: voltar à avaliação sem exportar.
- A5 — Versão não elegível (rascunho ou processamento desatualizado): explicar indisponibilidade. Uma edição nova processada precisa ser salva com sucesso antes da geração.
- A6 — Avaliação sem diferenciação: gerar relatório com esse estado e indicadores calculáveis, sem C*, posição ou vencedor fictício.
- A7 — Falha ao salvar: preservar versão anterior e informar; não gerar relatório da edição nem mudar silenciosamente para a versão anterior.
- A8 — Falha na geração: informar erro e manter a avaliação salva intacta; salvamento já concluído não é desfeito.
- A9 — Compartilhamento cancelado, indisponível ou com falha: preservar avaliação e não afirmar envio; informar falha quando aplicável.

**Pós-condição de sucesso:** arquivo gerado a partir da versão salva escolhida e disponibilizado ao compartilhamento, sem alterar catálogos. A confirmação de entrega pelo destino não é garantida pelo aplicativo.
**Verificação:** VT17; conferir conteúdo e versão exportada, offline, empates, ausência de diferenciação, três escolhas de edição pendente, recálculo necessário, falhas e cancelamentos.

A exportação não introduz importação, backup restaurável ou envio automático. O conteúdo do relatório permanece centralizado no RF27 para evitar duplicação.
