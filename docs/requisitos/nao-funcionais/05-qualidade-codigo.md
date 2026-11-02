# Qualidade do Código

**Versão:** 1.0 — requisitos validados.

[Índice de requisitos](../README.md)

### RNF09 — Modularidade e manutenibilidade

**Situação:** validado.

**Descrição:** organizar responsabilidades e dependências conforme Kotlin, Jetpack Compose e MVVM, mantendo os motores matemáticos independentes.

**Critérios de verificação:**

- Telas e componentes Compose apresentam informações e encaminham ações do usuário.
- ViewModels coordenam o estado e as operações da apresentação.
- Motores AHP e TOPSIS ficam separados da interface e podem ser testados sem Android, Room ou Firebase.
- Repositórios encapsulam o acesso a dados, evitando consultas diretas nas telas e nos motores.
- Categorias, critérios e produtos usam modelos genéricos; os motores não dependem de campos específicos de smartphones ou veículos.
- Acrescentar uma categoria futuramente não exige reescrever os algoritmos matemáticos.

**Método de verificação:** revisão das responsabilidades e dependências e testes isolados dos motores. Não exige múltiplos módulos Gradle nem escolha antecipada de biblioteca de injeção de dependências.

### RNF10 — Testabilidade

**Situação:** validado.

**Critérios de verificação:**

| Área | Cobertura mínima aprovada |
|---|---|
| Motores | Referências matemáticas, RC no limite, matrizes inválidas, empates e ausência de diferenciação. |
| Validação | Campos ausentes, valores inválidos, categoria incompatível e menos de duas alternativas. |
| Persistência | Salvar, reabrir, editar e excluir; preservar avaliações após mudanças no catálogo e a versão anterior quando uma gravação falhar. |
| Interface | Concluir avaliação, revisar AHP inconsistente, retomar rascunho, confirmar exclusão e avisar sobre alterações não salvas. |
| Firestore | Leitura permitida e escrita negada pelo cliente. |

- Automatizar testes dos motores, validações, persistência e regras do Firestore.
- Na interface, automatizar pelo menos uma avaliação até o resultado e o aviso de alterações não salvas. Os demais cenários podem ter execução manual documentada.
- Registrar resultado e ambiente de cada execução.
- Corrigir antes da entrega falhas que impeçam fluxos aprovados, comprometam dados, produzam cálculos incorretos ou permitam escrita indevida no catálogo.
- Não exigir porcentagem arbitrária de cobertura.

**Método de verificação:** execução dos testes automatizados e cenários manuais previstos, com registros de resultados. Esta aprovação documental não declara testes executados.

### RNF11 — Documentação técnica

**Situação:** validado.

**Critérios de verificação:**

- README com objetivo, tecnologias, pré-requisitos e instruções de configuração e execução.
- Requisitos, decisões, domínio, arquitetura e navegação acessíveis pelo índice de documentação.
- Referências das fórmulas AHP-TOPSIS e regras de consistência, precisão e empates.
- Instruções de configuração do Firebase e demais dependências, sem credenciais administrativas.
- Fontes, datas e métodos do catálogo, incluindo rubricas de câmeras e ADAS.
- Plano de testes e registros de execução que distingam testes planejados dos realizados.

**Método de verificação:** revisão documental e tentativa de configurar e executar o projeto por outro integrante seguindo o README. Não exige documentar cada função nem repetir informações em arquivos distintos.
