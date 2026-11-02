# Revisão de consolidação da Etapa 2

**Versão:** 0.1 — rascunho, preparada para revisão, sem aprovação automática.
**Data:** 02/11/2026.
**Escopo:** detalhamento dos casos de uso, rastreabilidade e preparação da verificação. Não inclui implementação, modelagem final, novas decisões ou execução de testes.

[Requisitos](README.md) · [Casos de uso](casos-de-uso/README.md) · [Matriz](rastreabilidade.md) · [Plano de verificação](plano-verificacao.md)

## Diagnóstico

Os RF01–RF26, RNF01–RNF13 e DEC-001–DEC-031 estão registrados como aprovados. Há base suficiente para detalhar fluxos, persistência e cálculo, sem reiniciar a elicitação. A presente consolidação propõe UC01–UC15 e relaciona todos os RFs e RNFs a verificações futuras.

A Etapa 2 ainda não deve ser declarada integralmente encerrada: a organização dos UCs aguarda revisão, e existem ambiguidades pontuais que afetam critérios de aceitação. Não foram encontradas regras aprovadas contraditórias sobre salvamento quando se distingue estado em memória da versão persistida; algumas redações antigas, porém, não explicitam essa distinção.

## Fontes efetivamente examinadas

- Visão e Escopo, registro central DEC-001–031, seis arquivos de RFs e seis de RNFs.
- Índices do projeto e documentação, questões de arquitetura e fluxo inicial de navegação.
- Os dois PDFs do professor estão presentes em `references/`; este trabalho utiliza a referência e a divergência metodológica já registradas na DEC-018, sem nova auditoria integral dos PDFs.
- Não foi localizada uma lista UC01–UC15 nos arquivos atuais. Os identificadores propostos precisam ser conferidos pela equipe caso exista outra lista fora do repositório.

## Material preparado

| Material | Conteúdo | Situação |
|---|---|---|
| Casos de uso | Atores, objetivos, gatilhos, pré-condições, fluxos, alternativas, pós-condições e verificações de UC01–UC15 | Proposta para revisão |
| Rastreabilidade | 26 RFs, 13 RNFs e relação das 31 decisões com os fluxos/qualidade/processo | Proposta para revisão |
| Plano de verificação | VT01–VT16, respeitando mínimos aprovados e distinguindo execução futura | Proposta para revisão |
| RFs e RNFs existentes | Conteúdo normativo preservado | Aprovação anterior mantida |

## Pontos que precisam de validação antes de encerrar a especificação

As recomendações abaixo NÃO são decisões aprovadas. Mantêm-se os identificadores atuais dos RFs/RNFs; nenhuma DEC-032 foi criada.

### P01 — Domínio dos valores de entrada

**Evidência:** RF06/RF13 exigem valores válidos e limites definidos, mas somente as notas de câmeras/ADAS possuem intervalo explícito (1–10). O registro de decisões mantém limites de entrada como pendência.

**Impacto:** ainda não é possível decidir todos os testes de aceitação de preço, autonomia, armazenamento, desempenho, tempo de recarga e seguro; principalmente zero, negativos e precisão de entrada.

**Recomendação para discutir:** preparar um dicionário por critério contendo unidade, benefício/custo, tipo, domínio permitido e regra para ausência. Usar limites de significado físico e evitar tetos arbitrários. Não fixar novos intervalos neste pacote. A forma de guardar entrada incompleta no rascunho deve preservar a distinção entre ausência e valor válido, sem permitir processamento inválido.

**Destino:** completar regras de validação RF06/RF13 antes da implementação dos validadores; detalhamento de representação na modelagem.

### P02 — Produtos distintos entre catálogos

**Evidência:** RF11 impede inclusão duplicada do mesmo produto e exige pelo menos dois distintos; não define equivalência entre produto oficial e cadastro pessoal que descrevem o mesmo modelo/versão.

**Impacto:** deduplicar por identidade de registro ou por produto físico pode gerar seleções diferentes. Empate TOPSIS não resolve a identidade dos produtos.

**Recomendação para discutir:** explicitar que a prevenção mínima usa identidade do registro de origem, sem deduplicação automática por nome. Se a equipe desejar equivalência entre catálogos, definir uma regra própria antes de implementar. Esta recomendação não foi aplicada como obrigação aos RFs.

**Destino:** esclarecer RF11 e o conceito de identidade na modelagem.

### P03 — Persistência do caso sem diferenciação

**Evidência:** RF18–RF20/DEC-024 permitem informar ausência de diferenciação quando C* é indefinido; RF22 distingue rascunho e concluída e RNF05 proíbe resultado matematicamente inválido como concluído. Falta explicitar a classificação persistida do processamento válido que termina sem coeficiente definido.

**Impacto:** as telas de avaliações salvas e os testes precisam distinguir ausência legítima de diferenciação de falha matemática ou rascunho incompleto.

**Recomendação para discutir:** tratar como avaliação processada/concluída com resultado explícito “sem diferenciação”, preservando dados e indicadores calculáveis, sem inventar C*, posição ou vencedor. Isso precisa de aprovação antes de consolidar o estado persistido; não foi imposto no UC11.

**Destino:** esclarecer RF20–RF25 e estados do modelo, mantendo o comportamento matemático aprovado.

## Ajustes editoriais identificados

| Local | Observação | Tratamento neste pacote |
|---|---|---|
| Registro, regra transversal 3.2 | “Editar invalida” pode ser lido como alteração imediata no banco; DEC-025 e RNF05 já distinguem memória e persistência | UC12/UC14 explicitam a distinção; regra aprovada não foi alterada |
| Registro, pendências seção 11 | Ainda pede especificar RNFs e garantias em encerramento inesperado, já tratados em RNF05 e DEC-025 | Atualização editorial sugerida abaixo, sem reabrir a aprovação |
| RF19 | Rastreabilidade antiga não lista DEC-027/028 | Matriz inclui ambas; recomendado acrescentar referências no RF após revisão |
| RF15/RF16 | Critérios numéricos antigos não referenciam DEC-029 | Matriz e plano apontam a tolerância aprovada, sem modificar fórmulas |
| Fluxo de navegação | “Salvar” após saída usa nó de persistir avaliação/resultado, podendo sugerir resultado para rascunho; abertura para edição também passa diretamente por “alterações pendentes” | UCs distinguem abrir, modificar, rascunho e resultado; revisar diagrama quando detalhar navegação |
| Visão e registro | Alguns rótulos dos links ainda usam nomes antigos como 02-requisitos.md, embora os destinos existam | Ajuste cosmético possível; não altera escopo |

Redação sugerida para substituir a pendência genérica antiga: “Revisar os casos de uso e resolver os pontos de entrada, identidade e estado sem diferenciação; detalhar contratos e estrutura Firestore/Room na modelagem/arquitetura. RNF01–RNF13 e política de perda de alterações não salvas já estão aprovados.” A substituição não foi feita no registro para manter sua revisão explícita.

## Pendências das etapas seguintes

Não é necessário escolher agora bibliotecas, classes ou esquema de banco para aprovar os fluxos. Permanecem para o momento apropriado:

- **Preparação dos dados:** modelos reais, fontes e datas, rubricas de câmeras/ADAS, protocolo de autonomia de smartphones, perfil do seguro e condições de recarga. DEC-013–017 já orientam essas escolhas; elas precisam estar prontas antes de validar o catálogo real.
- **Modelagem:** entidades, identidade dos registros, cópias dos dados utilizados, relações e representação dos estados aprovados.
- **Arquitetura:** contratos Room/Firestore, dependências, navegação, versões Android suportadas, configuração de backup, concorrência e tratamento de erros.
- **Testes:** referências matemáticas, dispositivo de desempenho, procedimento de aquecimento, ferramentas e dados dos cenários.
- **Gestão:** configuração efetiva do GitHub Projects e tags das entregas reais. Este pacote não criou quadro, PR, tag ou commit.
- **Disciplina:** manter a diferença entre os métodos dos PDFs visível na DEC-018; confirmar com o professor as adaptações quando necessário. Não há evidência aqui de confirmação externa.

## Critério proposto para encerrar a Etapa 2

1. Equipe revisa e aceita organização e fluxos UC01–UC15.
2. Resolve P01–P03, um ponto por vez, e registra aprovações sem alterar silenciosamente decisões anteriores.
3. Incorpora os esclarecimentos nos RFs/UCs e atualiza as pendências editoriais correspondentes.
4. Confere matriz e plano, garantindo cobertura documental dos 26 RFs e 13 RNFs.
5. Registra versão aprovada da especificação e encaminha as pendências técnicas às próximas etapas.

A criação de commit ocorrerá somente após autorização do usuário. A conclusão documental não significa que o app atende aos requisitos em execução nem substitui entregáveis específicos exigidos pelo professor.
