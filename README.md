# PDM AHP-TOPSIS

Aplicativo acadêmico Android para apoiar a comparação multicritério de produtos, combinando AHP para derivar pesos de critérios e TOPSIS para ordenar alternativas.

## Estado do projeto

As etapas de Visão e Escopo e de Engenharia de Requisitos estão concluídas documentalmente. A Etapa 2 consolida 27 requisitos funcionais, 13 não funcionais, 16 casos de uso, matriz de rastreabilidade e plano de verificação. A [Etapa 3 está concluída documentalmente](docs/modelagem/fechamento-etapa-3.md): Partes 1–4 aprovadas conceitualmente e Parte 5 aprovada nas DEC-038–DEC-042, com aprovação conjunta de P5-B–P5-E em 06/11/2026. CHECK AHP, domínios/precisão e configuração de serialização permanecem dependências técnicas documentadas. A [Etapa 4 está encerrada documentalmente](docs/arquitetura/fechamento-etapa-4.md) após conferência final em 10/11/2026; suas escolhas técnicas foram adotadas por delegação do usuário nas DEC-043–DEC-048. A implementação Android continua posterior às definições documentais.

A [Etapa 5 v0.5 está aprovada pelo usuário e concluída documentalmente](docs/interface/finalizacao-v05.md#aprovação-e-fechamento-documental), incluindo mapa de telas, navegação, design system, HTML calculável e Figma com roteiros preparados. Os protótipos e seus limites estão no [índice de interface](docs/interface/README.md). Vídeo, pacote final, envio ao Teams e aceite do professor continuam separados desse fechamento.

## Documentação

Acesse o [índice da documentação](docs/README.md) para decisões, requisitos, domínio, arquitetura e navegação.

## Referências do professor

- [Avaliação Comparativa de Produtos com AHP-TOPSIS](references/ProjetoDisciplina_PDM20262_AHP+TOPSIS.pdf) — referência matemática principal, conforme DEC-018.
- [Enunciado do projeto — versão 2](references/ProjetoDisciplina_PDM20262v2.pdf) — escopo, requisitos e entregas.

## Entregas

O enunciado consultado indica a Fase 1 em 03/11/2026 e a Fase 2 em 11/12/2026. O texto fornecido pelo estudante relata adiamento de uma semana e estima 10/11/2026 para a Fase 1. Confirmar a nova data oficial com o professor.

## Execução

O app Android está em [`app/`](app/README.md) e o simulador do Firebase em [`firebase/`](firebase/README.md). Resumo para rodar no celular por USB:

```bash
cd firebase && npm install && npm run emulador     # terminal 1 (deixe aberto)
(cd firebase && npm run popular)                   # terminal 2: publica o catálogo fictício
adb reverse tcp:8085 tcp:8085                      # túnel USB celular → PC
./gradlew :app:installDebug -PfirestoreHost=127.0.0.1
```

Para o emulador Android, use apenas `./gradlew :app:installDebug`, que aponta para `10.0.2.2`. O passo a passo, os cenários de teste com o simulador e as soluções para problemas comuns estão em [app/README.md](app/README.md).
