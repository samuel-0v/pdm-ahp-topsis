# App Android — PDM AHP-TOPSIS

O app foi escrito em Kotlin com Jetpack Compose e segue o padrão MVVM (DEC-043). Ele usa:

- Room para os dados locais;
- Firestore para o catálogo oficial, que é somente leitura;
- `PdfDocument` para exportar os relatórios.

Durante o desenvolvimento, o catálogo vem do **simulador do Firebase** (Firestore Emulator), na pasta [`../firebase`](../firebase/README.md). Não é preciso ter um projeto Firebase real nem o `google-services.json`.

## Requisitos

| Ferramenta | Versão |
|---|---|
| JDK | 17 ou superior (testado com 21) |
| Android SDK | platform 35 e build-tools 34 ou superior |
| Node.js | 20 ou superior, para o simulador do Firebase |
| Aparelho | Android 8.0 (API 26) ou superior |

Crie o arquivo `local.properties` na raiz do repositório. Ele não é versionado.

```properties
sdk.dir=/caminho/para/android-sdk
```

## Passo a passo: rodar no celular (recomendado)

O celular fica ligado ao PC por USB. O comando `adb reverse` faz `127.0.0.1:8085` no aparelho apontar para o simulador que roda no PC. Assim não depende da rede Wi-Fi nem do IP da máquina.

1. **Ative a depuração USB no celular.**
   1. Vá em Configurações → Sobre o telefone → Informações do software e toque 7 vezes em "Número de compilação".
   2. Em Opções do desenvolvedor, ative "Depuração USB".
   3. Conecte o cabo e aceite o aviso "Permitir depuração USB?".
   4. Confira se o aparelho aparece com o comando abaixo. Se aparecer `unauthorized`, desbloqueie o celular e aceite o aviso.

   ```bash
   adb devices
   ```

2. **Suba o simulador do Firebase.** Use o terminal 1 e deixe-o aberto.

   ```bash
   cd firebase
   npm install          # só na primeira vez
   npm run emulador
   ```

   Espere a mensagem `All emulators ready!`. A interface do simulador fica em http://localhost:4000.

3. **Publique o catálogo fictício.** Use o terminal 2.

   ```bash
   cd firebase
   npm run popular
   ```

4. **Crie o túnel e instale o app.** Ainda no terminal 2, a partir da raiz do repositório:

   ```bash
   adb reverse tcp:8085 tcp:8085
   ./gradlew :app:installDebug -PfirestoreHost=127.0.0.1
   ```

5. Abra o app **PDM AHP-TOPSIS** no celular.

O túnel se desfaz quando o cabo é desconectado ou o `adb` reinicia. Nesse caso, rode `adb reverse tcp:8085 tcp:8085` de novo. O host gravado no APK só muda quando você reinstala o app.

## Rodar no emulador Android (AVD)

No emulador, o PC é acessado pelo endereço `10.0.2.2`, que é o padrão do build. Siga os passos 2 e 3 acima e depois rode:

```bash
./gradlew :app:installDebug
```

O AVD ocupa cerca de 12 GB de disco por padrão. Se faltar espaço, prefira o celular.

## Como usar o simulador do Firebase durante os testes

Os comandos abaixo são rodados na pasta `firebase/`, com o simulador ligado.

| Quero testar… | Faça |
|---|---|
| Catálogo carregado do servidor | `npm run popular` e abra o app. A faixa mostra "Confirmado no servidor". |
| Atualização em tempo real (DEC-020) | Com a lista de produtos aberta, rode `npm run atualizar -- sp-aurora-pro`. O preço cai 5% na tela sem recarregar. |
| Cache offline | Abra o catálogo uma vez e pare o simulador (Ctrl+C). O app passa a mostrar os dados do cache, com aviso de origem. |
| Catálogo vazio | `npm run limpar`. Avaliações já salvas continuam abrindo, porque são cópias locais (DEC-042). |
| Produto incompleto | Os produtos `Nimbus X` e `BEV Delta` vêm sem um valor de propósito. O app avisa e pede o valor na etapa de valores. |
| Ver ou editar documentos | Abra http://localhost:4000/firestore e altere um campo. O app recebe a mudança pelo listener. |
| Conferir dados e regras | `npm run teste`, que sobe um simulador separado, valida e encerra. |

Os dados ficam só na memória do simulador e se perdem quando ele é encerrado. Depois de reiniciá-lo, rode `npm run popular` de novo.

## Problemas comuns

| Sintoma | Causa e solução |
|---|---|
| "Catálogo indisponível" ou "Carregando" sem fim | O simulador está parado, o túnel caiu ou o host está errado. Confira `curl localhost:8085`, rode `adb reverse tcp:8085 tcp:8085` e reinstale com o `-PfirestoreHost` correto. |
| Nenhuma categoria aparece | Falta publicar os dados: rode `npm run popular`. |
| `adb devices` mostra `unauthorized` | Desbloqueie o celular e aceite o aviso de depuração. |
| A porta 8085 está ocupada | Mude a porta em `firebase/firebase.json` e passe `-PfirestorePort=<porta>` ao Gradle. |
| O PDF não abre ao compartilhar | Instale um leitor de PDF ou envie por um app de mensagens ou e-mail. |

## Build e testes

```bash
./gradlew :app:assembleDebug        # gera app/build/outputs/apk/debug/app-debug.apk
./gradlew :app:testDebugUnitTest    # 55 testes: motores AHP/TOPSIS, validações, casos de uso, Room (Robolectric), mapeamento Firestore
```

| Propriedade Gradle | Padrão | Uso |
|---|---|---|
| `firestoreHost` | `10.0.2.2` | Host do simulador visto pelo aparelho |
| `firestorePort` | `8085` | Porta do Firestore Emulator |

## Estrutura

```
app/src/main/java/br/edu/ufu/pdm/mcdm/
├── domain/        modelos, motores AHP/TOPSIS, validações, casos de uso, portas (sem Android)
├── data/          Room (6 tabelas, DEC-046), Firestore, gerador de PDF
├── di/            AppContainer — injeção manual (DEC-044)
├── presentation/  telas Compose, ViewModels, navegação tipada (DEC-045/047)
└── MainActivity.kt
```

O schema do Room é exportado em `app/schemas/`.
