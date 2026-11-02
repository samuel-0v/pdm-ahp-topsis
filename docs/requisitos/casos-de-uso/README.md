# Casos de uso

**Versão:** 0.1 — rascunho, proposta para revisão da equipe.
**Base:** RF01–RF26, RNF01–RNF13 e DEC-001–DEC-031 aprovados.

[Requisitos](../README.md) · [Rastreabilidade](../rastreabilidade.md) · [Revisão da Etapa 2](../revisao-etapa-2.md)

## Atores e limite do sistema

- **Usuário:** consulta catálogos, mantém produtos pessoais, configura comparações e gerencia avaliações locais. Não exige conta ou login.
- **Firebase Cloud Firestore:** sistema externo de apoio às consultas do catálogo oficial. Dados em cache não garantem consulta atual ao servidor.
- Room, motores AHP/TOPSIS, ViewModels e repositórios são componentes internos, não atores.
- Administração do catálogo por responsáveis externos não é funcionalidade do aplicativo nesta versão. Professor e equipe são interessados no projeto, não novos perfis de acesso.

## Organização proposta

Não foi localizada uma lista UC01–UC15 nos arquivos atuais. A correspondência abaixo é uma proposta de organização dos RFs existentes, sujeita à revisão, e não uma recuperação de identificadores anteriormente documentados. Casos de uso descrevem objetivos do usuário; não determinam quantidade de telas nem classes Kotlin.

| ID | Caso de uso | Documento |
|---|---|---|
| UC01 | Consultar catálogo oficial | [Catálogos](01-catalogos.md#uc01--consultar-catálogo-oficial) |
| UC02 | Consultar detalhes de produto oficial | [Catálogos](01-catalogos.md#uc02--consultar-detalhes-de-produto-oficial) |
| UC03 | Cadastrar produto pessoal | [Catálogos](01-catalogos.md#uc03--cadastrar-produto-pessoal) |
| UC04 | Consultar e editar produto pessoal | [Catálogos](01-catalogos.md#uc04--consultar-e-editar-produto-pessoal) |
| UC05 | Excluir produto pessoal | [Catálogos](01-catalogos.md#uc05--excluir-produto-pessoal) |
| UC06 | Criar avaliação | [Configuração](02-configuracao.md#uc06--criar-avaliação) |
| UC07 | Selecionar alternativas | [Configuração](02-configuracao.md#uc07--selecionar-alternativas) |
| UC08 | Ajustar e validar valores da avaliação | [Configuração](02-configuracao.md#uc08--ajustar-e-validar-valores-da-avaliação) |
| UC09 | Realizar e revisar comparações AHP | [Cálculo e resultados](03-calculo-resultados.md#uc09--realizar-e-revisar-comparações-ahp) |
| UC10 | Calcular pesos e validar consistência AHP | [Cálculo e resultados](03-calculo-resultados.md#uc10--calcular-pesos-e-validar-consistência-ahp) |
| UC11 | Processar TOPSIS e consultar resultados | [Cálculo e resultados](03-calculo-resultados.md#uc11--processar-topsis-e-consultar-resultados) |
| UC12 | Salvar avaliação e tratar saída com alterações | [Persistência](04-persistencia.md#uc12--salvar-avaliação-e-tratar-saída-com-alterações) |
| UC13 | Consultar e retomar avaliações salvas | [Persistência](04-persistencia.md#uc13--consultar-e-retomar-avaliações-salvas) |
| UC14 | Editar e recalcular avaliação concluída | [Persistência](04-persistencia.md#uc14--editar-e-recalcular-avaliação-concluída) |
| UC15 | Excluir avaliação salva | [Persistência](04-persistencia.md#uc15--excluir-avaliação-salva) |

## Convenções e regras comuns

Os fluxos principais descrevem sucesso; alternativas e exceções registram desvios. Referências a outros UCs indicam reutilização narrativa, sem impor relações UML include/extend.

1. Categoria de produto cadastrado e de avaliação criada é imutável (DEC-021/022).
2. A seleção exige dois produtos distintos para processar, mas rascunhos podem estar incompletos. Não existe máximo funcional de 50 produtos (DEC-005/026).
3. Cálculos usam dados da avaliação. Alterações do catálogo não substituem essas cópias (RF25).
4. Edição em memória não altera dados persistidos. Resultados deixam de representar a edição assim que entradas mudam; a invalidação persistida ocorre ao salvar (DEC-025, RNF05).
5. Comparar, processar e consultar não significam salvar; saída com mudanças segue UC12.
6. Regras matemáticas ficam centralizadas nas DEC-018 e DEC-024–029 e no RNF04; as fórmulas detalhadas permanecem nos RFs.
7. Usabilidade e acessibilidade aplicam-se aos fluxos pertinentes; disponibilidade offline depende dos dados existentes (RNF01, RNF02, RNF06).
8. Critérios de verificação dos UCs são roteiros documentais, não testes já executados.

## Relações entre os fluxos

- Consulta: UC01 → UC02; cadastro/manutenção pessoal: UC03–UC05.
- Nova comparação: UC06 → UC07 → UC08 → UC09 → UC10 → UC11.
- UC07 pode utilizar UC03; UC10 pode devolver ao UC09 quando houver inconsistência.
- UC12 pode ocorrer durante a edição e após processamento; UC13 restaura a versão salva.
- UC14 reutiliza seleção, ajustes, julgamentos e cálculo conforme a mudança realizada.
- UC15 exclui a avaliação, preservando os catálogos.

Essa sequência explica dependências funcionais; não obriga navegação linear nem substitui o futuro protótipo.
