# Matriz de rastreabilidade

**Versão:** 1.0 — consolidação documental alinhada às DEC-001–DEC-035.

[Índice de requisitos](README.md) · [Casos de uso](casos-de-uso/README.md) · [Verificações previstas](plano-verificacao.md) · [Revisão](revisao-etapa-2.md)

Esta matriz conecta requisitos aprovados ao detalhamento dos casos de uso e às verificações futuras. Cobertura documental não comprova implementação nem execução de testes. Intervalos incluem todos os identificadores entre os extremos. As decisões são consultadas no [registro central](../decisoes/registro-decisoes.md).

## Requisitos funcionais

| RF | Casos de uso | Decisões/regras relacionadas | Verificação planejada |
|---|---|---|---|
| [RF01](funcionais/01-catalogo-oficial.md) | UC01 | DEC-001, 008–010 | VT01 |
| [RF02](funcionais/01-catalogo-oficial.md) | UC01 | DEC-001, 008–010 | VT01 |
| [RF03](funcionais/01-catalogo-oficial.md) | UC02 | DEC-009, 014, 017, 020 | VT01 |
| [RF04](funcionais/01-catalogo-oficial.md) | UC01, UC02 | DEC-009, 010, 020, 033 | VT01, VT12 |
| [RF05](funcionais/01-catalogo-oficial.md) | UC01 | DEC-009, 010, 020 | VT01, VT11 |
| [RF06](funcionais/02-catalogo-pessoal.md) | UC03 | DEC-001, 004, 007, 012, 021, 033 | VT02, VT03 |
| [RF07](funcionais/02-catalogo-pessoal.md) | UC04 | DEC-003, 007, 012, 021, 033 | VT02 |
| [RF08](funcionais/02-catalogo-pessoal.md) | UC05 | DEC-007, 012 | VT02, VT10 |
| [RF09](funcionais/02-catalogo-pessoal.md) | UC03–UC05, UC07 | DEC-003, 004, 007, 012 | VT02, VT04, VT11 |
| [RF10](funcionais/03-configuracao-avaliacoes.md) | UC06 | DEC-001, 005, 008, 022, 025 | VT04 |
| [RF11](funcionais/03-configuracao-avaliacoes.md) | UC07, UC14 | DEC-003, 005, 007, 012, 022, 034 | VT03, VT04, VT09 |
| [RF12](funcionais/03-configuracao-avaliacoes.md) | UC08, UC14 | DEC-003, 006, 017, 025 | VT04, VT09 |
| [RF13](funcionais/03-configuracao-avaliacoes.md) | UC07, UC08, UC11 | DEC-005, 013, 014, 017, 018, 033, 034 | VT03 |
| [RF14](funcionais/04-ahp.md) | UC09, UC14 | DEC-018, 023 | VT05 |
| [RF15](funcionais/04-ahp.md) | UC10 | DEC-018, 023, 029 | VT06 |
| [RF16](funcionais/04-ahp.md) | UC10 | DEC-018, 029 | VT06 |
| [RF17](funcionais/04-ahp.md) | UC10, UC14 | DEC-018, 023; RNF04 | VT06, VT09 |
| [RF18](funcionais/05-topsis-resultados.md) | UC11, UC14 | DEC-003, 005, 018, 024–026, 029, 034, 035 | VT07, VT09, VT14 |
| [RF19](funcionais/05-topsis-resultados.md) | UC11 | DEC-018, 024, 027, 028 | VT07 |
| [RF20](funcionais/05-topsis-resultados.md) | UC11 | DEC-006, 018, 024, 025, 035 | VT07, VT09, VT15 |
| [RF21](funcionais/06-persistencia-avaliacoes.md) | UC12 | DEC-004, 006, 022, 025 | VT08, VT09 |
| [RF22](funcionais/06-persistencia-avaliacoes.md) | UC13 | DEC-004, 006, 019, 025, 035 | VT08 |
| [RF23](funcionais/06-persistencia-avaliacoes.md) | UC13 | DEC-004, 006, 022, 025 | VT08, VT11 |
| [RF24](funcionais/06-persistencia-avaliacoes.md) | UC12, UC14 | DEC-003, 006, 018, 022, 024, 025 | VT09 |
| [RF25](funcionais/06-persistencia-avaliacoes.md) | UC05, UC07, UC08, UC11–UC14 | DEC-003, 004, 006, 012, 020, 025, 033, 034, 035 | VT02, VT08–VT11 |
| [RF26](funcionais/06-persistencia-avaliacoes.md) | UC15 | DEC-012, 019, 025 | VT10 |
| [RF27](funcionais/07-exportacao.md) | UC16; UC12/UC14 quando necessário | DEC-011 revisada, 018, 024, 025, 027, 028, 032, 035 | VT17 |

## Requisitos não funcionais

| RNF | Aplicação | Decisões ou base aprovada | Verificação planejada |
|---|---|---|---|
| [RNF01](nao-funcionais/01-usabilidade-acessibilidade.md) | Interações UC01–UC16; protocolo específico no RNF | Critérios aprovados do RNF01 | VT15 |
| [RNF02](nao-funcionais/01-usabilidade-acessibilidade.md) | Interfaces; TalkBack nos fluxos de avaliação/resultados | Critérios aprovados do RNF02 | VT15 |
| [RNF03](nao-funcionais/02-desempenho-correcao.md) | UC10–UC11 | DEC-026 | VT14 |
| [RNF04](nao-funcionais/02-desempenho-correcao.md) | UC08–UC11, UC14 | DEC-018, 024, 027–029; comparação direta do RC | VT03, VT06, VT07 |
| [RNF05](nao-funcionais/03-integridade-offline.md) | UC12–UC15 e preservação RF25 | DEC-004, 006, 019, 025 | VT08–VT10 |
| [RNF06](nao-funcionais/03-integridade-offline.md) | UC01–UC16 conforme dados presentes | DEC-004, 009, 010, 019, 020, 025, 032 | VT11, VT17 |
| [RNF07](nao-funcionais/04-seguranca-privacidade.md) | Consultas oficiais e bloqueio de escrita | DEC-009; critérios aprovados do RNF07 | VT01, VT12 |
| [RNF08](nao-funcionais/04-seguranca-privacidade.md) | Dados pessoais e avaliações; configuração Android e compartilhamento explícito do PDF | DEC-004, 009, 011, 030, 032 | VT13, VT17 |
| [RNF09](nao-funcionais/05-qualidade-codigo.md) | Responsabilidades de apresentação, motores e dados | Diretriz Kotlin/Compose/MVVM e RNF09 | VT16 |
| [RNF10](nao-funcionais/05-qualidade-codigo.md) | Motores, validação, persistência, interface e Firestore | Critérios aprovados de automatização e registro | VT03, VT06–VT10, VT12, VT16 |
| [RNF11](nao-funcionais/05-qualidade-codigo.md) | Documentação, dados do catálogo e reprodução do ambiente | RNF11; DEC-013–018 | VT16 |
| [RNF12](nao-funcionais/06-processo-desenvolvimento.md) | Processo da equipe, sem UC de produto | DEC-031 | VT16 |
| [RNF13](nao-funcionais/06-processo-desenvolvimento.md) | Processo da equipe, sem UC de produto | DEC-031 | VT16 |

## Decisões transversais e limites

As DEC-001–035 estão contempladas nas relações acima ou nas restrições abaixo. Nem toda decisão gera uma funcionalidade própria.

| Decisão | Reflexo documental |
|---|---|
| DEC-001, 008 | UC01, UC03, UC06 e modelos genéricos do RNF09 |
| DEC-002 | Usuário único como papel funcional; público-alvo não cria recursos corporativos |
| DEC-003–007, 012 | Valores próprios, catálogo pessoal e ciclo de avaliação nos UC03–UC15 |
| DEC-009, 010, 020 | Catálogo remoto/cache em UC01–UC02 e disponibilidade offline |
| DEC-011 | Sem autenticação, sincronização pessoal, categorias personalizadas, colaboração no app ou importação; exportação PDF permitida pela DEC-032 |
| DEC-013–017 | Dados, unidades, fontes e rubricas nos UC02, UC03 e UC08; conteúdo concreto ainda pendente |
| DEC-018 | Motores UC10–UC11 e referência matemática escolhida |
| DEC-019 | UC15 e exclusão atômica no RNF05 |
| DEC-021, 022 | Categorias imutáveis em UC03–UC04 e UC06–UC14 |
| DEC-023 | UC09–UC10, progresso e revisão |
| DEC-024, 027, 028 | UC11, empates e ausência de diferenciação |
| DEC-025 | UC12–UC14, salvamento manual e versão persistida |
| DEC-026 | UC10–UC11 e VT14, sem máximo funcional de produtos |
| DEC-029 | VT06–VT07, referências matemáticas independentes |
| DEC-030 | RNF08 e VT13, exclusão de backup/transferência |
| DEC-031 | RNF12–RNF13 e VT16, branches/PRs e GitHub Projects |
| DEC-032 | RF27, UC16 e VT17; exportação local em PDF, conteúdo aprovado e tratamento de edição pendente |
| DEC-033 | RF04, RF06/RF07, RF13 e RF25; metadados reutilizáveis, cache e cópia das definições; VT01, VT03, VT08, VT11 |
| DEC-034 | RF11/RF13, UC07 e VT03/VT04; identidade por origem e ID, sem equivalência física automática |
| DEC-035 | RF20/RF22, UC11–UC13 e VT07/VT08/VT17; conclusão salva sem diferenciação |

## Limite da rastreabilidade com as fontes do professor

O [enunciado principal](../../references/ProjetoDisciplina_PDM20262v2.pdf) e a [referência matemática](../../references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf) estão presentes no repositório. A escolha matemática e a diferença entre procedimentos estão registradas na DEC-018. Esta matriz parte dos requisitos e decisões aprovados; não representa nova auditoria página a página dos PDFs nem confirmação de aceitação das adaptações pelo professor.
