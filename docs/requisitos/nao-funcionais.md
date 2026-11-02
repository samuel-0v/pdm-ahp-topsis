# Especificação de Requisitos Não Funcionais

**Projeto:** Sistema de Avaliação Comparativa Multicritério de Produtos (AHP-TOPSIS)
**Disciplina:** FACOM32503 — Programação para Dispositivos Móveis
**Versão:** 0.1
**Situação:** RNF03 validado; RNF01–RNF02 e RNF04–RNF13 em revisão
**Referências:** Enunciado do projeto, plano de ensino e decisões DEC-001 a DEC-026

---

## Grupo 1 — Usabilidade e Interface

### RNF01 — Usabilidade

**Descrição:** A interface deve permitir que o usuário compreenda e execute o processo de avaliação multicritério de maneira clara, com orientações e mensagens de retorno apropriadas.

**Critérios de verificação:**
- As ações principais de cada tela devem ser identificáveis.
- O progresso das comparações AHP deve ser indicado.
- Erros de preenchimento devem ser apresentados próximos das informações que precisam de correção, quando aplicável.
- Operações destrutivas devem solicitar confirmação.
- Deve ser possível concluir os fluxos principais de consulta, criação de avaliação e visualização do ranking nos testes de usabilidade planejados.

**Método de verificação:** testes de interface e execução dos cenários dos casos de uso.

### RNF02 — Responsividade e acessibilidade

**Descrição:** A interface deve adaptar-se às dimensões suportadas de dispositivos Android, preservando a legibilidade e a possibilidade de interação.

**Critérios de verificação propostos:**
- Não deve ocorrer sobreposição ou corte de controles essenciais nos tamanhos de tela selecionados para teste.
- Textos e controles importantes devem permanecer utilizáveis com o aumento da escala de fonte.
- Elementos interativos devem possuir identificação acessível quando necessário.
- Informações de erro, aprovação e destaque não devem depender exclusivamente da cor.
- A navegação deve funcionar nos dispositivos e emuladores definidos no plano de testes.

**Método de verificação:** testes visuais, testes de interface e verificação de acessibilidade.

---

## Grupo 2 — Desempenho e Correção Matemática

### RNF03 — Desempenho dos cálculos

**Situação:** validado, conforme DEC-026 e complementação aprovada.

A meta quantitativa e seu protocolo estão na [DEC-026](../decisoes/registro-decisoes.md#dec-026--desempenho-dos-calculos).

Os cálculos executarão fora da thread principal. Durante o processamento, a navegação e os controles habilitados devem continuar respondendo conforme o fluxo previsto. Enquanto houver processamento, apresentar indicação de atividade, sem atraso artificial para exibir o indicador. Verificar no dispositivo físico de referência, incluindo o cenário de 50 produtos.

**Métodos de verificação:** medições dos motores conforme DEC-026 e verificação de interação durante o processamento.

### RNF04 — Precisão e correção numérica

**Descrição:** Os algoritmos AHP e TOPSIS devem produzir resultados numericamente corretos, conforme a metodologia definida no documento `ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf`.

**Critérios de verificação propostos:**
- O cálculo dos pesos deve corresponder ao método da média geométrica normalizada.
- Os indicadores λmax, IC e RC devem ser verificados com exemplos matemáticos conhecidos.
- Os resultados TOPSIS devem ser comparados com valores de referência.
- Diferenças decorrentes de aritmética de ponto flutuante devem respeitar tolerâncias estabelecidas nos testes.
- Casos matematicamente indefinidos não devem produzir resultados fictícios.
- Os cálculos devem utilizar precisão completa, sem arredondamentos intermediários para apresentação.

**Método de verificação:** testes unitários automatizados com matrizes e resultados de referência.

---

## Grupo 3 — Confiabilidade, Persistência e Disponibilidade

### RNF05 — Integridade dos dados persistidos

**Descrição:** O sistema deve preservar a consistência dos dados salvos localmente, evitando registros parciais ou resultados incompatíveis com a configuração da avaliação.

**Critérios de verificação:**
- Uma operação de salvamento deve preservar integralmente os dados associados à avaliação.
- Falhas durante o salvamento não devem resultar em sucesso informado incorretamente.
- Operações que alterem múltiplos registros relacionados devem utilizar mecanismos de persistência consistentes.
- As alterações não salvas não devem modificar a última versão persistida.
- Resultados matemáticos inválidos não devem ser persistidos como avaliações concluídas.

**Método de verificação:** testes de persistência, tratamento de erros e integridade transacional.

**Decisões relacionadas:** DEC-004, DEC-006, DEC-025.

### RNF06 — Funcionamento offline

**Descrição:** As funcionalidades dependentes exclusivamente de dados locais devem permanecer disponíveis sem conexão com a internet.

**Critérios de verificação:**
- Produtos pessoais persistidos devem continuar acessíveis offline.
- Avaliações salvas devem poder ser consultadas, editadas e recalculadas offline.
- Os cálculos AHP e TOPSIS não devem depender da disponibilidade do Firebase.
- O catálogo oficial poderá ser consultado offline quando seus dados estiverem disponíveis no cache.
- Na ausência de dados oficiais em cache, o sistema deverá informar a indisponibilidade.
- A aplicação deverá distinguir os dados provenientes do cache de informações confirmadas pelo servidor.

**Método de verificação:** execução dos cenários principais com a conectividade desativada.

**Decisões relacionadas:** DEC-004, DEC-009, DEC-010, DEC-020.

---

## Grupo 4 — Segurança e Privacidade

### RNF07 — Segurança do catálogo remoto

**Descrição:** O acesso ao Firebase Cloud Firestore deve respeitar as permissões definidas para a primeira versão, impedindo alterações não autorizadas no catálogo oficial.

**Critérios de verificação:**
- Os clientes Android devem possuir somente as permissões necessárias para consultar o catálogo oficial.
- As regras de segurança do Firestore devem impedir escritas realizadas por clientes não autorizados.
- Credenciais administrativas e chaves privadas de serviço não devem ser incorporadas ao aplicativo.
- Falhas de permissão devem ser tratadas adequadamente.

**Método de verificação:** testes das regras de segurança do Firebase e revisão da configuração do aplicativo.

**Decisão relacionada:** DEC-009.

### RNF08 — Privacidade dos dados pessoais

**Descrição:** O aplicativo deve manter os produtos pessoais e as avaliações do usuário no armazenamento local, sem sincronizá-los com o catálogo remoto.

**Critérios de verificação:**
- Produtos pessoais não devem ser enviados ao Firestore durante as operações previstas.
- Matrizes de comparação e resultados das avaliações devem permanecer no armazenamento local.
- O funcionamento básico não deve exigir autenticação.
- O aplicativo não deve solicitar permissões Android desnecessárias às funcionalidades implementadas.

**Método de verificação:** inspeção das operações de rede, revisão das permissões e testes dos repositórios.

**Decisões relacionadas:** DEC-004, DEC-009, DEC-011.

---

## Grupo 5 — Qualidade do Código

### RNF09 — Modularidade e manutenibilidade

**Descrição:** O código-fonte deve possuir responsabilidades bem definidas, permitindo manutenção e evolução sem acoplamento desnecessário entre a interface, os motores matemáticos e o acesso aos dados.

**Critérios de verificação:**
- A aplicação deve seguir a arquitetura MVVM definida para o projeto.
- A interface deve utilizar Jetpack Compose.
- A lógica dos cálculos AHP e TOPSIS deve ser separada da interface.
- Os motores matemáticos devem poder ser testados sem inicializar o ambiente Android ou acessar o Firebase.
- O acesso a dados deve ser encapsulado em componentes apropriados.
- A estrutura deve permitir a introdução de novas categorias sem reescrever os motores matemáticos.

**Método de verificação:** revisão arquitetural, inspeção de dependências e testes isolados dos módulos.

### RNF10 — Testabilidade

**Descrição:** O sistema deve possuir mecanismos de verificação automatizada que permitam identificar falhas e regressões.

**Critérios de verificação:**
- Os motores AHP e TOPSIS devem possuir testes unitários.
- As validações de dados devem possuir testes.
- A persistência local deve possuir testes de integração.
- Os fluxos críticos de interação devem ser cobertos por testes de interface, conforme o plano de testes.
- Os testes devem abranger entradas válidas, inválidas e casos extremos relevantes.
- Falhas identificadas devem ser corrigidas antes da entrega final.

**Método de verificação:** execução automatizada da suíte de testes e registro dos resultados.

### RNF11 — Documentação técnica

**Descrição:** O projeto deve possuir documentação suficiente para que a equipe compreenda, execute, mantenha e valide a aplicação.

**Critérios de verificação:**
- Existência de README com descrição do projeto e instruções de execução.
- Registro das decisões técnicas relevantes.
- Documentação dos requisitos e da arquitetura.
- Documentação das fórmulas e procedimentos utilizados nos motores matemáticos.
- Instruções para configurar as dependências necessárias ao funcionamento do aplicativo, sem expor segredos.
- Identificação das fontes dos dados utilizados no catálogo oficial.

**Método de verificação:** revisão dos documentos no repositório.

---

## Grupo 6 — Processo de Desenvolvimento

### RNF12 — Controle de versão

**Descrição:** O desenvolvimento deve utilizar Git, com repositório compartilhado para gerenciamento do código-fonte e da documentação.

**Critérios de verificação:**
- O projeto deve estar versionado em Git.
- Os integrantes devem utilizar o repositório compartilhado.
- Alterações relevantes devem possuir histórico identificável.
- Código e documentação necessários à entrega devem estar disponíveis no repositório.

**Método de verificação:** inspeção do histórico e da estrutura do repositório.

**Repositório definido:** `github.com/samuel-0v/pdm-ahp-topsis`

### RNF13 — Gestão das atividades

**Descrição:** A equipe deve utilizar uma ferramenta de acompanhamento das atividades do projeto.

**Critérios de verificação:**
- As tarefas devem possuir identificação e descrição.
- As atividades devem possuir responsáveis quando atribuídas.
- O andamento das tarefas deve ser acompanhado.
- Pendências e entregas devem ser rastreáveis.

**Método de verificação:** inspeção do quadro de acompanhamento da equipe.

**Ferramenta sugerida:** GitHub Projects.

---

## Observações

Os RNF01–RNF13 constituem uma proposta inicial baseada nas orientações do professor e nas decisões funcionais do projeto.

Os requisitos RNF12 e RNF13 representam restrições de processo e gestão da equipe, incluídas nesta especificação porque o enunciado da disciplina as apresenta entre os requisitos não funcionais.

A meta de desempenho foi aprovada na DEC-026. Dispositivos suportados e demais critérios de teste ainda exigem validação.

**Situação:** validação parcial; somente RNF03 aprovado.
## Proposta pendente — DEC-027

Considerar empatados coeficientes TOPSIS cuja diferença absoluta seja menor ou igual a 10⁻⁹, usando valores internos antes do arredondamento para exibição. **Ainda não aprovada.** Tolerâncias dos testes matemáticos e tratamento do limite RC = 0,10 serão discutidos separadamente.
