#!/usr/bin/env node
// Publicação inicial do catálogo fictício em um projeto real.
// Usa ADC e um commit atômico com precondição de inexistência: nunca substitui documentos.

import { readFile } from 'node:fs/promises';
import { fileURLToPath } from 'node:url';
import path from 'node:path';
import { camposFirestore, validarCatalogo } from './catalogo.mjs';

const aqui = path.dirname(fileURLToPath(import.meta.url));
const projeto = process.argv[2] === '--project' ? process.argv[3] : null;
const confirmacao = process.argv[4] === '--confirm' ? process.argv[5] : null;

if (!projeto || !/^[a-z][a-z0-9-]{4,28}[a-z0-9]$/.test(projeto) ||
    (process.argv.length > 4 && confirmacao !== projeto) || process.argv.length > 6) {
  console.error('Uso: npm run publicar-real -- --project ID_DO_PROJETO [--confirm ID_DO_PROJETO]');
  process.exit(2);
}
if (process.env.FIRESTORE_EMULATOR_HOST) {
  console.error('Remova FIRESTORE_EMULATOR_HOST antes de publicar no projeto real.');
  process.exit(2);
}

const catalogo = JSON.parse(await readFile(path.join(aqui, '..', 'dados', 'catalogo.json'), 'utf8'));
const erros = validarCatalogo(catalogo);
if (erros.length) {
  console.error('Catálogo inválido:\n- ' + erros.join('\n- '));
  process.exit(1);
}

const config = JSON.parse(await readFile(path.join(aqui, '..', '..', 'app', 'google-services.json'), 'utf8'));
if (config.project_info?.project_id !== projeto) {
  console.error('O ID informado difere de app/google-services.json.');
  process.exit(2);
}
const appRegistrado = config.client?.some((entry) =>
  entry.client_info?.android_client_info?.package_name === 'br.edu.ufu.pdm.mcdm');
if (!appRegistrado) {
  console.error('app/google-services.json não contém o app Android br.edu.ufu.pdm.mcdm.');
  process.exit(2);
}

console.log(`Projeto: ${projeto}`);
console.log(`Publicação inicial: ${catalogo.categorias.length} categorias e ${catalogo.produtos.length} produtos fictícios.`);
console.log('Documentos existentes com os mesmos IDs impedirão todo o commit; nenhum documento será removido.');
if (!confirmacao) {
  console.log('Prévia concluída. Para publicar, repita com --confirm ID_DO_PROJETO.');
  process.exit(0);
}

const { GoogleAuth } = await import('google-auth-library');
const auth = new GoogleAuth({ scopes: ['https://www.googleapis.com/auth/datastore'] });
const token = await auth.getAccessToken();
if (!token) throw new Error('Credenciais Google ausentes. Configure Application Default Credentials.');

const raiz = `projects/${projeto}/databases/(default)`;
const agora = new Date();
const writes = [
  ...catalogo.categorias.map(({ id, ...dados }) => ({
    update: { name: `${raiz}/documents/categorias/${id}`, fields: camposFirestore({ ...dados, atualizadoEm: agora }) },
    currentDocument: { exists: false },
  })),
  ...catalogo.produtos.map(({ id, ...dados }) => ({
    update: { name: `${raiz}/documents/produtos/${id}`, fields: camposFirestore({ ...dados, atualizadoEm: agora }) },
    currentDocument: { exists: false },
  })),
];

const resposta = await fetch(`https://firestore.googleapis.com/v1/${raiz}/documents:commit`, {
  method: 'POST',
  headers: { Authorization: `Bearer ${token}`, 'Content-Type': 'application/json' },
  body: JSON.stringify({ writes }),
});
if (!resposta.ok) {
  console.error(`Publicação recusada (${resposta.status}): ${await resposta.text()}`);
  process.exit(1);
}
const resultado = await resposta.json();
console.log(`Publicado com sucesso: ${resultado.writeResults?.length ?? 0} documentos em ${resultado.commitTime}.`);
