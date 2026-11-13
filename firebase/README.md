# Firestore Emulator — catálogo oficial simulado

Simula o catálogo oficial remoto (DEC-009, DEC-040) sem um projeto Firebase real. Usa o **Firebase Local Emulator Suite** oficial. O app usa o SDK real do Firestore, com listeners, cache offline e regras, apontando para a máquina local.

O projeto `demo-pdm-ahp-topsis` tem o prefixo `demo-`, por isso não precisa de credenciais nem de `google-services.json`. Ele nunca acessa serviços reais.

## Requisitos

- Node.js 20 ou superior.
- Java 11 ou superior, para o emulador.
- `npm install` dentro desta pasta. Instala o `firebase-tools` local; o jar do emulador é baixado na primeira execução.

## Uso

```bash
cd firebase
npm install
npm run emulador      # terminal 1: Firestore em :8085 e UI em http://localhost:4000
npm run popular       # terminal 2: limpa e publica dados/catalogo.json
npm run verificar     # confere a quantidade de documentos e os tipos gravados
npm run atualizar -- sp-aurora-pro   # republica um produto com preço 5% menor
npm run limpar        # apaga tudo do emulador
npm run teste         # sobe o emulador, popula, verifica, testa as regras e encerra
```

`npm run atualizar` serve para ver o listener em tempo real e a data da última atualização (DEC-020). Avaliações já salvas não podem mudar (DEC-042).

## Conexão do app Android

| Onde o app roda | Host do Firestore |
|---|---|
| Emulador Android (AVD) | `10.0.2.2:8085` |
| Aparelho via USB | `adb reverse tcp:8085 tcp:8085` e depois `127.0.0.1:8085` |

O app de debug inicializa o Firebase manualmente com `projectId = demo-pdm-ahp-topsis` e chama `useEmulator`. Os detalhes estão em `app/README.md`.

## Dados (`dados/catalogo.json`)

**Todos os dados são fictícios**, com nomes inventados (Aurora, Vértice, Lumen…). Cada categoria segue as regras transversais 3.3 e tem exatamente cinco critérios com IDs globalmente únicos.

São quatro smartphones e três BEVs completos. Há ainda dois produtos **incompletos de propósito**:

- `sp-nimbus-x-incompleto`: sem nota de câmeras.
- `bev-delta-incompleto`: sem seguro anual.

Eles servem para testar o dado incompleto e o ajuste do valor efetivo.

### Tipos gravados

O script grava pela API REST do emulador para controlar cada tipo:

| Campo | Tipo Firestore |
|---|---|
| `centavos`, `ordem`, `formatoContrato`, `escala`, `ano` | `integerValue` (inteiro64, DEC-041) |
| `numero` e limites de critérios `NUMERO` | `doubleValue` |
| `atualizadoEm` | `timestampValue` (momento da publicação) |
| `valores.<critério>: null` | ausência de valor |

Campos iniciados por `_` no JSON são comentários e não são publicados.

### Escolhas de publicação ainda não definidas nos documentos

- **Nomes dos limites:** `validacao.limites = {minimo, minimoInclusivo, maximo, maximoInclusivo}`, no mesmo tipo do valor. Só as notas de 1 a 10 têm limites; nenhum outro foi inventado.
- **Unidades:** códigos estáveis em maiúsculas (`BRL`, `BRL_POR_ANO`, `HORAS`, `KM`, `MINUTOS`, `GB`, `PONTOS`, `NOTA_1_10`).
- **`exigeInteiro`:** `false` em todos os critérios, por falta de definição aprovada.

O comando `npm run validar-dados` confere o arquivo contra o contrato antes da carga.

## Regras (`firestore.rules`)

- Leitura liberada em `categorias` e `produtos`.
- Qualquer escrita do cliente é negada.
- Qualquer outra coleção é negada.

A carga usa o acesso administrativo do emulador (`Bearer owner`), que ignora as regras. O script se recusa a rodar se `FIRESTORE_EMULATOR_HOST` não for local.
