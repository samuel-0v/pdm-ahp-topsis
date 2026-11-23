# Firebase real para o app Android

O app usa Cloud Firestore apenas para ler o catálogo oficial. Produtos pessoais e avaliações continuam no Room, no aparelho. O emulador permanece como padrão de desenvolvimento; o acesso ao projeto real é ativado explicitamente no build.

## Estado verificado em 22/11/2026

- Projeto `pdm-ahp-topsis`; banco `(default)` em `southamerica-east1` (São Paulo), modo Firestore Native.
- App Android `br.edu.ufu.pdm.mcdm` registrado; `app/google-services.json` local corresponde ao projeto e permanece fora do Git.
- Regras ativas conferidas pela API de gerenciamento: conteúdo equivalente a `firebase/firestore.rules` (ignorando comentários e espaços).
- Catálogo fictício inicial publicado: 2 categorias e 9 produtos. Leitura pública conferiu IDs e tipos `integerValue`, `doubleValue` e `timestampValue`.
- `:app:assembleDebug -PfirebaseMode=real` e `:app:installDebug -PfirebaseMode=real` passaram. No celular conectado por USB, o app exibiu as duas categorias com “Confirmado no servidor”; a lista de Smartphones Premium exibiu os cinco produtos oficiais, incluindo o aviso de dados incompletos do Nimbus X.

## 1. Criar o projeto e o banco

1. Entre em [console.firebase.google.com](https://console.firebase.google.com/) com a conta que administrará o projeto e crie um projeto Firebase. Guarde o **ID do projeto** (diferente do nome exibido); ele não poderá ser alterado depois. Google Analytics é opcional para este app.
2. Em **Databases & Storage → Firestore**, crie o banco **`(default)`**, edição **Standard**, modo **Native**, com regras iniciais de **produção**. Esse modo começa negando acesso do app até a publicação das regras deste repositório. Não use o modo de teste, que permite escrita temporária de clientes.
3. Escolha a região antes de criar o banco. Para usuários no Brasil, `southamerica-east1` (São Paulo) é uma opção de baixa latência; a localização do banco não pode ser alterada depois. Confirme disponibilidade e custos aplicáveis no console.

## 2. Registrar o app Android

No projeto Firebase, adicione um app **Android** com o identificador de pacote exato `br.edu.ufu.pdm.mcdm`. Baixe o arquivo `google-services.json` e coloque-o em `app/google-services.json`. Ele é ignorado pelo Git neste repositório. O Gradle lê `project_id`, `mobilesdk_app_id` e a chave de API desse arquivo para a inicialização manual do Firebase; não é necessário adicionar o plugin Google Services para o Firestore usado aqui.

O arquivo de configuração identifica o projeto, mas não concede permissão administrativa. Não coloque chaves de conta de serviço no app ou no repositório.

## 3. Publicar regras

As regras em `firebase/firestore.rules` permitem leitura pública apenas de `categorias` e `produtos`; negam escrita pelo app e acesso às demais coleções. Portanto, publique somente dados de catálogo nessas coleções.

Na pasta `firebase/`, instale as dependências e autentique o CLI com a conta administradora:

```bash
cd firebase
npm ci
npx firebase login
npm run teste
npx firebase deploy --only firestore:rules --project ID_DO_PROJETO
```

Substitua `ID_DO_PROJETO` pelo ID real. Passe `--project` explicitamente: o alias `default` de `firebase/.firebaserc` aponta para `demo-pdm-ahp-topsis`, usado apenas pelo emulador. O deploy sobrescreve as regras do projeto de destino; confira o ID e o arquivo antes de executá-lo. O app usa apenas consultas simples e não requer índices compostos.

## 4. Publicar o catálogo

O arquivo `firebase/dados/catalogo.json` contém **dados fictícios**. O comando `npm run popular` funciona apenas no emulador e se recusa a escrever fora de um host local. Não o use como método de carga do projeto real.

Para testar o projeto real com o catálogo fictício, use o script de publicação inicial. Ele preserva os tipos exigidos pelo contrato e faz um único commit atômico. Se qualquer documento com um dos IDs já existir, o commit falha e nada é sobrescrito. Não há operação de limpeza no projeto real.

Configure [Application Default Credentials](https://docs.cloud.google.com/docs/authentication/set-up-adc-local-dev-environment) para a conta com permissão de escrita no Firestore. No computador, a opção usual é `gcloud auth application-default login`; no Cloud Shell, a autenticação da sessão pode ser usada. Então, em `firebase/`:

```bash
npm run publicar-real -- --project ID_DO_PROJETO
npm run publicar-real -- --project ID_DO_PROJETO --confirm ID_DO_PROJETO
```

O primeiro comando mostra a prévia sem acessar o serviço. O segundo publica. O script exige que `app/google-services.json` pertença ao mesmo projeto e recusa a presença de `FIRESTORE_EMULATOR_HOST`. Para um catálogo real, substitua primeiro os dados fictícios por dados verificados, seguindo `docs/modelagem/contratos/04-firestore.md` e o contrato de valores antes da publicação.

A carga inicial já foi executada em `pdm-ahp-topsis`. Uma nova execução com os mesmos IDs falhará por segurança; ela não é um comando de atualização do catálogo.

## 5. Compilar e conferir

Com `app/google-services.json` presente, a partir da raiz do repositório:

```bash
./gradlew :app:installDebug -PfirebaseMode=real
```

Esse build **não** chama `useEmulator` e consulta o Firestore do projeto indicado no JSON. É necessário acesso à internet no aparelho. Confira no console se há documentos nas duas coleções e, no app, se o catálogo aparece com origem “Confirmado no servidor”. Se aparecer “Permissão negada”, confira as regras publicadas. Se o catálogo vier vazio, confira os documentos e o ID do projeto no JSON.

Para voltar ao ambiente local, compile novamente sem `-PfirebaseMode=real` e siga `app/README.md`. O host `10.0.2.2:8085` é usado apenas nesse modo padrão.

## Referências oficiais

- [Criar projeto e registrar app Android](https://firebase.google.com/docs/android/setup)
- [Criar o Cloud Firestore](https://firebase.google.com/docs/firestore/quickstart)
- [Regiões do Firestore](https://firebase.google.com/docs/firestore/locations)
- [Inicialização manual com `FirebaseOptions`](https://firebase.google.com/docs/projects/multiprojects)
- [Publicar regras pelo Firebase CLI](https://firebase.google.com/docs/rules/manage-deploy)
- [Cotas e cobrança do Firestore](https://firebase.google.com/docs/firestore/pricing)
