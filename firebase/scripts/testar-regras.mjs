#!/usr/bin/env node
// Confere as regras como um cliente sem privilégio (o app): leitura permitida, escrita negada.
const PROJETO = process.env.GCLOUD_PROJECT || 'demo-pdm-ahp-topsis';
const HOST = process.env.FIRESTORE_EMULATOR_HOST || '127.0.0.1:8085';
const BASE = `http://${HOST}/v1/projects/${PROJETO}/databases/(default)/documents`;

const casos = [
  ['GET', `${BASE}/categorias/smartphones-premium`, undefined, true],
  ['GET', `${BASE}/produtos/sp-aurora-pro`, undefined, true],
  ['PATCH', `${BASE}/produtos/sp-aurora-pro`, { fields: { nome: { stringValue: 'x' } } }, false],
  ['PATCH', `${BASE}/categorias/nova`, { fields: { nome: { stringValue: 'x' } } }, false],
  ['GET', `${BASE}/avaliacoes/qualquer`, undefined, false],
];

let falhou = false;
for (const [metodo, url, corpo, deveAceitar] of casos) {
  const r = await fetch(url, { method: metodo, headers: { 'Content-Type': 'application/json' }, body: corpo && JSON.stringify(corpo) });
  const aceito = r.ok;
  const ok = aceito === deveAceitar;
  if (!ok) falhou = true;
  console.log(`${ok ? 'ok ' : 'ERRO'} ${metodo} ${url.replace(BASE, '')} -> ${r.status} (esperado ${deveAceitar ? 'permitido' : 'negado'})`);
}
process.exit(falhou ? 1 : 0);
