#!/usr/bin/env node
// Carga, verificação e simulação de atualização do catálogo oficial no Firestore Emulator.
// Usa a API REST do emulador para controlar o tipo exato de cada número:
//   - `centavos`, `ordem`, `formatoContrato` e `ano` => integerValue (inteiro64, DEC-041)
//   - `numero` e limites de NUMERO => doubleValue (Double, DEC-041)
// O SDK Admin do Node gravaria 16 como inteiro; aqui isso não acontece.
//
// Comandos: popular | limpar | verificar | atualizar [produtoId] | validar-dados

import { readFile } from 'node:fs/promises';
import { fileURLToPath } from 'node:url';
import path from 'node:path';

const PROJETO = process.env.GCLOUD_PROJECT || 'demo-pdm-ahp-topsis';
const HOST = process.env.FIRESTORE_EMULATOR_HOST || '127.0.0.1:8085';
const BASE = `http://${HOST}/v1/projects/${PROJETO}/databases/(default)/documents`;
const ADMIN = { Authorization: 'Bearer owner', 'Content-Type': 'application/json' };

const aqui = path.dirname(fileURLToPath(import.meta.url));
const ARQUIVO_CATALOGO = path.join(aqui, '..', 'dados', 'catalogo.json');

if (!/^(localhost|127\.0\.0\.1|0\.0\.0\.0|\[::1\])(:\d+)?$/.test(HOST)) {
  console.error(`Recusado: FIRESTORE_EMULATOR_HOST=${HOST} não é local. Este script só escreve no emulador.`);
  process.exit(2);
}

// ---------- validação do catálogo contra o contrato DEC-040/041 ----------

const TIPOS = new Set(['MONETARIO_BRL', 'NUMERO']);
const NATUREZAS = new Set(['BENEFICIO', 'CUSTO']);

export function validarCatalogo(cat) {
  const erros = [];
  const criteriosPorCategoria = new Map();
  const idsCriteriosGlobais = new Set();

  for (const c of cat.categorias) {
    const onde = `categoria ${c.id}`;
    if (!c.id) erros.push(`${onde}: id vazio`);
    if (!c.nome) erros.push(`${onde}: nome obrigatório`);
    if (c.formatoContrato !== 1) erros.push(`${onde}: formatoContrato deve ser 1`);
    if (typeof c.revisaoDefinicao !== 'string' || !c.revisaoDefinicao) erros.push(`${onde}: revisaoDefinicao obrigatória`);
    if (!Array.isArray(c.criterios) || c.criterios.length !== 5) erros.push(`${onde}: exatamente cinco critérios`);
    const ordens = new Set();
    const mapa = new Map();
    for (const cr of c.criterios ?? []) {
      const o = `${onde}/critério ${cr.id}`;
      if (!cr.id || !cr.nome || !cr.unidade) erros.push(`${o}: id, nome e unidade obrigatórios`);
      if (idsCriteriosGlobais.has(cr.id)) erros.push(`${o}: id repetido (deve ser globalmente único)`);
      idsCriteriosGlobais.add(cr.id);
      if (!Number.isInteger(cr.ordem) || cr.ordem < 0) erros.push(`${o}: ordem inteira não negativa`);
      if (ordens.has(cr.ordem)) erros.push(`${o}: ordem repetida`);
      ordens.add(cr.ordem);
      if (!NATUREZAS.has(cr.natureza)) erros.push(`${o}: natureza inválida`);
      const v = cr.validacao ?? {};
      if (!TIPOS.has(v.tipoValor)) erros.push(`${o}: tipoValor inválido`);
      for (const k of ['obrigatorioCadastro', 'obrigatorioProcessamento', 'exigeInteiro']) {
        if (typeof v[k] !== 'boolean') erros.push(`${o}: validacao.${k} deve ser explícito`);
      }
      if (v.tipoValor === 'MONETARIO_BRL' && (v.moeda !== 'BRL' || v.escala !== 2)) {
        erros.push(`${o}: monetário exige moeda BRL e escala 2`);
      }
      mapa.set(cr.id, cr);
    }
    criteriosPorCategoria.set(c.id, { categoria: c, criterios: mapa });
  }

  const idsProdutos = new Set();
  for (const p of cat.produtos) {
    const onde = `produto ${p.id}`;
    if (idsProdutos.has(p.id)) erros.push(`${onde}: id repetido`);
    idsProdutos.add(p.id);
    const ref = criteriosPorCategoria.get(p.categoriaId);
    if (!ref) { erros.push(`${onde}: categoria desconhecida ${p.categoriaId}`); continue; }
    if (p.formatoContrato !== 1) erros.push(`${onde}: formatoContrato deve ser 1`);
    if (p.revisaoDefinicao !== ref.categoria.revisaoDefinicao) erros.push(`${onde}: revisaoDefinicao difere da categoria`);
    if (!p.nome) erros.push(`${onde}: nome obrigatório`);
    for (const [criterioId, valor] of Object.entries(p.valores ?? {})) {
      const cr = ref.criterios.get(criterioId);
      if (!cr) { erros.push(`${onde}: critério desconhecido ${criterioId}`); continue; }
      if (valor === null) continue;
      if (valor.tipo !== cr.validacao.tipoValor) { erros.push(`${onde}/${criterioId}: variante ${valor.tipo} ≠ ${cr.validacao.tipoValor}`); continue; }
      if (valor.tipo === 'MONETARIO_BRL') {
        if (!Number.isSafeInteger(valor.centavos)) erros.push(`${onde}/${criterioId}: centavos deve ser inteiro`);
        if (valor.moeda !== 'BRL' || valor.escala !== 2) erros.push(`${onde}/${criterioId}: moeda/escala contraditórias`);
        if ('numero' in valor) erros.push(`${onde}/${criterioId}: mais de uma variante preenchida`);
      } else {
        if (typeof valor.numero !== 'number' || !Number.isFinite(valor.numero)) erros.push(`${onde}/${criterioId}: numero finito obrigatório`);
        if ('centavos' in valor) erros.push(`${onde}/${criterioId}: mais de uma variante preenchida`);
        const l = cr.validacao.limites;
        if (l && typeof valor.numero === 'number') {
          const n = valor.numero;
          const abaixo = l.minimo !== undefined && (l.minimoInclusivo ? n < l.minimo : n <= l.minimo);
          const acima = l.maximo !== undefined && (l.maximoInclusivo ? n > l.maximo : n >= l.maximo);
          if (abaixo || acima) erros.push(`${onde}/${criterioId}: ${n} fora dos limites`);
        }
      }
    }
  }
  return erros;
}

// ---------- conversão JSON -> valores tipados do Firestore ----------

const CAMPOS_INTEIROS = new Set(['centavos', 'ordem', 'formatoContrato', 'ano', 'escala']);

function paraFirestore(valor, chave = '', dentroLimitesMonetarios = false) {
  if (valor === null || valor === undefined) return { nullValue: null };
  if (typeof valor === 'boolean') return { booleanValue: valor };
  if (typeof valor === 'string') return { stringValue: valor };
  if (typeof valor === 'number') {
    if (CAMPOS_INTEIROS.has(chave) || dentroLimitesMonetarios) {
      if (!Number.isSafeInteger(valor)) throw new Error(`Campo inteiro ${chave} recebeu ${valor}`);
      return { integerValue: String(valor) };
    }
    return { doubleValue: valor };
  }
  if (Array.isArray(valor)) return { arrayValue: { values: valor.map((v) => paraFirestore(v, chave)) } };
  if (valor instanceof Date) return { timestampValue: valor.toISOString() };
  return { mapValue: { fields: camposFirestore(valor, dentroLimitesMonetarios) } };
}

function camposFirestore(obj, monetario = false) {
  const campos = {};
  for (const [k, v] of Object.entries(obj)) {
    if (k.startsWith('_')) continue; // comentários do arquivo de dados não são publicados
    // Limites de critérios monetários seriam codificados em centavos (mesmo tipo do valor).
    const limitesMonetarios = k === 'limites' && monetario;
    if (k === 'validacao' && v && v.tipoValor === 'MONETARIO_BRL') {
      campos[k] = { mapValue: { fields: camposFirestore(v, true) } };
      continue;
    }
    campos[k] = paraFirestore(v, k, limitesMonetarios);
  }
  return campos;
}

// ---------- acesso REST ao emulador ----------

async function req(metodo, url, corpo) {
  const r = await fetch(url, { method: metodo, headers: ADMIN, body: corpo ? JSON.stringify(corpo) : undefined });
  if (!r.ok) throw new Error(`${metodo} ${url} -> ${r.status} ${await r.text()}`);
  const texto = await r.text();
  return texto ? JSON.parse(texto) : {};
}

async function gravar(colecao, id, dados) {
  await req('PATCH', `${BASE}/${colecao}/${encodeURIComponent(id)}`, { fields: camposFirestore(dados) });
}

async function limpar() {
  await req('DELETE', `http://${HOST}/emulator/v1/projects/${PROJETO}/databases/(default)/documents`);
}

async function listar(colecao) {
  const r = await req('GET', `${BASE}/${colecao}?pageSize=300`);
  return r.documents ?? [];
}

async function carregarCatalogo() {
  return JSON.parse(await readFile(ARQUIVO_CATALOGO, 'utf8'));
}

// ---------- comandos ----------

async function popular() {
  const cat = await carregarCatalogo();
  const erros = validarCatalogo(cat);
  if (erros.length) { console.error('Catálogo inválido:\n- ' + erros.join('\n- ')); process.exit(1); }
  await limpar();
  const agora = new Date();
  for (const { id, ...c } of cat.categorias) await gravar('categorias', id, { ...c, atualizadoEm: agora });
  for (const { id, ...p } of cat.produtos) await gravar('produtos', id, { ...p, atualizadoEm: agora });
  console.log(`Emulador ${HOST} (${PROJETO}): ${cat.categorias.length} categorias e ${cat.produtos.length} produtos publicados.`);
}

function tipoDe(v) { return Object.keys(v)[0]; }

async function verificar() {
  const cat = await carregarCatalogo();
  const categorias = await listar('categorias');
  const produtos = await listar('produtos');
  const falhas = [];
  if (categorias.length !== cat.categorias.length) falhas.push(`categorias: ${categorias.length} ≠ ${cat.categorias.length}`);
  if (produtos.length !== cat.produtos.length) falhas.push(`produtos: ${produtos.length} ≠ ${cat.produtos.length}`);

  for (const d of categorias) {
    const f = d.fields;
    if (tipoDe(f.formatoContrato) !== 'integerValue') falhas.push(`${d.name}: formatoContrato não é inteiro`);
    if (tipoDe(f.atualizadoEm) !== 'timestampValue') falhas.push(`${d.name}: atualizadoEm não é Timestamp`);
    for (const cr of f.criterios.arrayValue.values) {
      if (tipoDe(cr.mapValue.fields.ordem) !== 'integerValue') falhas.push(`${d.name}: ordem não é inteira`);
    }
  }
  for (const d of produtos) {
    for (const [criterio, v] of Object.entries(d.fields.valores?.mapValue?.fields ?? {})) {
      if ('nullValue' in v) continue;
      const campos = v.mapValue.fields;
      const tipo = campos.tipo.stringValue;
      if (tipo === 'MONETARIO_BRL' && tipoDe(campos.centavos) !== 'integerValue') falhas.push(`${d.name}/${criterio}: centavos não é inteiro64`);
      if (tipo === 'NUMERO' && tipoDe(campos.numero) !== 'doubleValue') falhas.push(`${d.name}/${criterio}: numero não é Double`);
    }
  }
  if (falhas.length) { console.error('Verificação falhou:\n- ' + falhas.join('\n- ')); process.exit(1); }
  console.log(`Verificação ok: ${categorias.length} categorias, ${produtos.length} produtos; tipos inteiro64/Double/Timestamp conferidos.`);
}

// Simula uma publicação nova de um produto (para testar listeners em tempo real e DEC-020).
async function atualizar(produtoId = 'sp-aurora-pro') {
  const cat = await carregarCatalogo();
  const p = cat.produtos.find((x) => x.id === produtoId);
  if (!p) { console.error(`Produto ${produtoId} não existe no catálogo.`); process.exit(1); }
  const preco = Object.entries(p.valores).find(([, v]) => v?.tipo === 'MONETARIO_BRL');
  const { id, ...dados } = structuredClone(p);
  if (preco) dados.valores[preco[0]].centavos = Math.round(preco[1].centavos * 0.95);
  await gravar('produtos', id, { ...dados, atualizadoEm: new Date() });
  console.log(`Produto ${id} republicado${preco ? ` com ${preco[0]} reduzido em 5%` : ''}. Avaliações salvas não devem mudar.`);
}

const [comando, ...args] = process.argv.slice(2);
const comandos = {
  popular,
  limpar: async () => { await limpar(); console.log('Emulador limpo.'); },
  verificar,
  atualizar: () => atualizar(args[0]),
  'validar-dados': async () => {
    const erros = validarCatalogo(await carregarCatalogo());
    if (erros.length) { console.error('- ' + erros.join('\n- ')); process.exit(1); }
    console.log('dados/catalogo.json segue o contrato.');
  },
};

if (import.meta.url === `file://${process.argv[1]}`) {
  const fn = comandos[comando];
  if (!fn) { console.error(`Uso: node scripts/catalogo.mjs <${Object.keys(comandos).join('|')}>`); process.exit(2); }
  fn().catch((e) => {
    if (e.cause?.code === 'ECONNREFUSED') console.error(`Emulador não encontrado em ${HOST}. Rode "npm run emulador" antes.`);
    else console.error(e.message);
    process.exit(1);
  });
}
