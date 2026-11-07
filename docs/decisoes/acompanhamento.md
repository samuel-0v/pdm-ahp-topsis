# 11. Pendências de especificação

[Índice de decisões](README.md) · [Registro](registro-decisoes.md)

Estas questões não alteram as decisões aprovadas; precisam ser detalhadas para requisitos, modelagem e testes:

- Selecionar modelos reais e registrar fontes e datas dos dados.
- Definir rubricas de câmeras e ADAS e os protocolos comparáveis de autonomia de smartphones.
- Definir perfil de referência do seguro e condições de medição da recarga rápida.
- Detalhar domínios, limites e precisão concretos por critério antes de habilitar os dados reais, respeitando o contrato Firestore aprovado na DEC-040 e a representação da DEC-041; tolerâncias matemáticas aprovadas nas DEC-027 e DEC-029.
- Implementar e testar os contratos Firestore/Room aprovados nas DEC-038–DEC-042, incluindo CHECK canônico, serialização e schema/migrations na etapa técnica. Casos de uso e RNF01–RNF13 já estão documentados; persistência transacional e perda de alterações não salvas seguem RNF05 e DEC-025. P01–P03 foram resolvidos pelas DEC-033–DEC-035.
- Confirmar com o professor, se necessário, as adaptações e a diferença metodológica do AHP.

## 12. Histórico

- **v1.15 (06/11/2026):** DEC-039–DEC-042 aprovadas conjuntamente pelo usuário após os ajustes da revisão; Partes 1–5 aprovadas e Etapa 3 concluída documentalmente. Dependências técnicas e limite de detecção do cliente preservados.

- **v1.14:** DEC-038 registra aprovação limitada da P5-A; defesa física AHP ainda requer validação e P5-B–P5-E permanecem pendentes.

- **v1.8:** DEC-032 aprovada: exportação local em PDF; revisão explícita da exclusão de relatórios da DEC-011. UC01–UC15 aceitos pelo usuário.
- **v1.9:** DEC-033 aprovada para P01: metadados genéricos de validação junto às definições remotas dos critérios; limites numéricos adicionais e schema permanecem para detalhamento.
- **v1.10:** DEC-034 aprovada para P02: identidade por origem e ID do registro; equivalência entre catálogos não é inferida automaticamente.
- **v1.11:** DEC-035 aprovada para P03: resultado válido sem diferenciação é concluído e listado com rótulo explícito.
- **v1.12:** DEC-036 aprovada para a organização conceitual da modelagem, incluindo distinção de CritérioPreservado, referências históricas independentes do catálogo e valores por critério nas alternativas.
- **v1.13:** DEC-037 aprovada para cardinalidades conceituais da Parte 3: critérios iniciais/futuros, exclusividade por categoria e diagnóstico AHP inconsistente em rascunhos.

Este documento consolida as decisões DEC-001 a DEC-042 aprovadas nas discussões da equipe até 06/11/2026. Novas decisões devem receber novo identificador e registrar status, contexto, decisão e consequências. Alterações em decisões existentes devem ser registradas, sem apagar silenciosamente o histórico.

## Validação dos requisitos não funcionais

RNF01–RNF13 validados. RNF05 consolida a integridade transacional e a preservação da última versão salva; RNF06 delimita as operações offline pela disponibilidade dos dados locais. Critérios completos em [requisitos não funcionais](../requisitos/README.md). RNF07 consolida leitura pública e bloqueio de escrita do catálogo oficial; RNF08 incorpora a DEC-030 sobre backup e transferência.

RNF09–RNF11 validados com critérios de separação de responsabilidades, cobertura mínima de testes e checklist documental. O detalhamento permanece no documento de RNFs, sem novos identificadores DEC.

RNF12–RNF13 validados com tags de entrega, histórico identificável, GitHub Projects e fluxo de colaboração aprovado na DEC-031.

RNF01 e RNF02 validados: protocolo de usabilidade com três participantes e matriz inicial de acessibilidade/responsividade aprovada. Todos os RNF01–RNF13 estão validados; os critérios completos ficam nos respectivos grupos de requisitos. Nenhum teste é declarado executado por essa validação documental.
