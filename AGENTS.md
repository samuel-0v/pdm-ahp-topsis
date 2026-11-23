# Orientações para agentes

## Projeto

Este repositório contém um aplicativo acadêmico Android para comparar produtos com AHP e TOPSIS. O app está em `app/` (Kotlin, Jetpack Compose e MVVM); `firebase/` contém um catálogo fictício servido pelo Firestore Emulator; `docs/` registra requisitos, decisões, modelagem, arquitetura e interface. Responda e escreva documentação em português do Brasil, preservando nomes de código e comandos.

## Fontes de referência

- Comece por `README.md` e `docs/README.md` para localizar o material vigente.
- Para regras de negócio, consulte `docs/decisoes/registro-decisoes.md`, `docs/decisoes/regras-transversais.md`, os contratos consolidados em `docs/modelagem/` e o plano em `docs/requisitos/plano-verificacao.md` antes de alterar os cálculos ou a persistência.
- Trate propostas e estudos históricos como contexto, sem apresentá-los como decisões aprovadas. Ao mudar uma regra documentada, atualize os documentos afetados e a rastreabilidade pertinente. Não declare aprovação, execução de testes ou entrega acadêmica sem evidência.
- Para comportamento de telas e navegação, consulte `docs/interface/README.md`, `docs/interface/navegacao.md` e `docs/interface/design-system.md`.

## Organização do código

- `app/src/main/java/br/edu/ufu/pdm/mcdm/domain/`: modelos, validações, motores AHP/TOPSIS, casos de uso e contratos de repositório; mantenha essa camada independente do Android.
- `data/`: Room, mapeamento do Firestore, repositórios e geração de PDF. O catálogo remoto é somente leitura; avaliações salvas usam cópias locais.
- `presentation/`: telas Compose, ViewModels e navegação; `di/AppContainer.kt` reúne a injeção manual.
- `app/src/test/`: testes de motores, validações, casos de uso, Room e mapeamento Firestore. `app/schemas/` guarda os schemas exportados do Room.

## Execução e verificação

- Pré-requisitos: JDK 17+, Android SDK platform 35, Node.js 20+ para o simulador. Configure `local.properties` com `sdk.dir`; esse arquivo não é versionado.
- Após alterações no app, rode `./gradlew :app:testDebugUnitTest :app:assembleDebug`. Para alterações no banco, confira também o schema exportado em `app/schemas/`.
- Após alterações em `firebase/dados/catalogo.json`, rode `cd firebase && npm run validar-dados`. Para validar a integração e as regras do emulador, instale as dependências em `firebase/` e rode `npm run teste` nessa pasta.
- Para usar o app com o catálogo local, siga `app/README.md` e `firebase/README.md`. No emulador Android, o host padrão é `10.0.2.2:8085`; no aparelho por USB, use `adb reverse tcp:8085 tcp:8085` e instale com `-PfirestoreHost=127.0.0.1`.

## Cuidados

- Preserve as fórmulas, critérios, precisão, política de ranking e comportamento de snapshots definidos nos contratos; cubra mudanças de cálculo com testes de casos relevantes.
- Use apenas dados fictícios no catálogo do emulador. O projeto de desenvolvimento usa `demo-pdm-ahp-topsis` e não requer `google-services.json`.
- Não confunda o fechamento documental das etapas com implementação, validação prática ou aceite do professor. Registre limites e pendências com precisão.
