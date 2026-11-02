# Exportação de avaliações

**Situação:** funcionalidade, conteúdo e comportamento aprovados na DEC-032; detalhamento documental registrado conforme autorização do usuário.

[Índice de requisitos](../README.md) · [Decisões](../../decisoes/registro-decisoes.md) · [UC16](../casos-de-uso/05-exportacao.md)

### RF27 — Exportar avaliação em PDF

**Descrição:** permitir exportar um relatório PDF de uma avaliação salva e processada, incluindo processamento sem diferenciação, com geração local e compartilhamento explícito pelo usuário.

**Ator principal:** usuário. **Sistema de apoio:** compartilhamento padrão do Android e destino escolhido pelo usuário.

**Pré-condições:** avaliação com versão salva e processamento correspondente aos dados dessa versão; ou edição processada e atualizada que possa ser salva antes de exportar. Rascunho ou resultado desatualizado não é exportável.

**Fluxo principal:**

1. Usuário solicita exportar uma avaliação salva e processada.
2. Sistema verifica a versão persistida e a correspondência entre dados e resultado.
3. Sistema gera o PDF localmente, usando os dados preservados na avaliação, sem substituir valores pelos atuais do catálogo e sem depender de rede.
4. Sistema abre o compartilhamento padrão do Android para o arquivo gerado.
5. Usuário escolhe um aplicativo ou destino disponível e decide compartilhar.

**Conteúdo:** identificação da avaliação e categoria; data de geração do PDF; produtos, valores usados e unidades; critérios, custo/benefício e pesos; λmax, IC e RC; ranking, C*, d+ e d− quando definidos; indicação de empates ou ausência de diferenciação; referência ao método AHP-TOPSIS. Não incluir matrizes intermediárias de cálculo. A data de geração não representa atualização dos valores de origem.

**Fluxos alternativos e exceções:**

- Alterações não salvas: oferecer **Salvar e exportar**, **Exportar última versão salva** e **Cancelar**.
- Salvar e exportar: exigir processamento válido e atualizado da edição, salvar pelo fluxo de persistência e gerar somente após sucesso. Se for necessário recalcular, informar e impedir a exportação dessa edição até o recálculo; não misturar entradas novas com resultados antigos.
- Exportar última versão salva: disponível apenas quando essa versão já está processada; usar integralmente essa versão e preservar a edição pendente, sem salvá-la nem descartá-la implicitamente.
- Cancelar: retornar à avaliação sem exportar nem salvar por consequência do cancelamento.
- Rascunho, dados incompletos ou resultado invalidado: informar por que a versão não é exportável.
- Sem diferenciação: exportar os dados e indicadores calculáveis, informando o estado sem coeficiente, posição ou vencedor fictício.
- Falha ao salvar: informar falha, preservar versão anterior e não prosseguir como se a edição tivesse sido salva; não trocar silenciosamente para a versão antiga.
- Falha ao gerar o PDF: informar erro e preservar a avaliação salva. Se o salvamento já tiver sido concluído, a falha de geração não o desfaz.
- Falha ao abrir o compartilhamento ou ausência de destino disponível: informar impossibilidade sem afirmar envio e sem modificar a avaliação.
- Cancelamento no compartilhamento: não alterar a avaliação nem afirmar que o relatório foi enviado.

**Pós-condição de sucesso:** PDF gerado a partir da versão salva elegível e disponibilizado ao compartilhamento. O envio efetivo depende da ação do usuário e do destino externo; abrir o compartilhamento não comprova entrega.

**Critérios de aceitação:**

- Gerar o relatório sem internet quando a avaliação elegível estiver disponível localmente; eventual dependência de rede do destino escolhido é externa à geração.
- Conferir os oito itens de conteúdo contra a versão persistida, inclusive depois de alterações ou exclusões nos catálogos.
- Respeitar empates e ausência de diferenciação, sem inserir resultados fictícios ou matrizes intermediárias.
- Verificar cada escolha diante de edição pendente, inclusive falta de versão anterior elegível, necessidade de recálculo e falha de salvamento.
- Falhas e cancelamentos não modificam a avaliação salva nem são apresentados como envio bem-sucedido.
- Rascunhos e resultados desatualizados não são exportados. Não há importação ou sincronização de avaliações nesta funcionalidade.

**Rastreabilidade:** DEC-011 (revisada pela DEC-032), DEC-018, DEC-024, DEC-025, DEC-027, DEC-028, DEC-032; RF20, RF24, RF25; RNF01, RNF02, RNF04, RNF05, RNF06 e RNF08. A meta de 500 ms da DEC-026 mede motores matemáticos, não geração de PDF.
