package br.edu.ufu.pdm.mcdm.presentation.produtopessoal

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.EstadoConsulta
import br.edu.ufu.pdm.mcdm.domain.model.IdentificacaoProduto
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoPessoal
import br.edu.ufu.pdm.mcdm.domain.model.Unidades
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import br.edu.ufu.pdm.mcdm.domain.repository.CatalogoRepository
import br.edu.ufu.pdm.mcdm.domain.repository.ProdutoPessoalRepository
import br.edu.ufu.pdm.mcdm.domain.usecase.ResultadoOperacao
import br.edu.ufu.pdm.mcdm.domain.usecase.SalvarProdutoPessoal
import br.edu.ufu.pdm.mcdm.domain.validation.EntradaPtBr
import br.edu.ufu.pdm.mcdm.domain.validation.Formatos
import br.edu.ufu.pdm.mcdm.domain.validation.ResultadoEntrada
import br.edu.ufu.pdm.mcdm.presentation.components.BotaoPrincipal
import br.edu.ufu.pdm.mcdm.presentation.components.Carregando
import br.edu.ufu.pdm.mcdm.presentation.components.DialogoSaida
import br.edu.ufu.pdm.mcdm.presentation.components.Mensagem
import br.edu.ufu.pdm.mcdm.presentation.components.TelaBase
import br.edu.ufu.pdm.mcdm.presentation.components.Tom
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class FormularioUiState(
    val carregando: Boolean = true,
    val indisponivel: String? = null,
    val edicao: Boolean = false,
    val categoriaNome: String = "",
    val definicoes: List<CriterioDefinicao> = emptyList(),
    val nome: String = "",
    val fabricante: String = "",
    val modelo: String = "",
    val versao: String = "",
    val ano: String = "",
    val valores: Map<String, String> = emptyMap(),
    val erros: Map<String, String> = emptyMap(),
    val salvando: Boolean = false,
    val erroGeral: String? = null,
    val salvo: Boolean = false,
    /** Incrementado a cada validação com erro: leva o foco ao primeiro campo inválido. */
    val focarErro: Int = 0,
) {
    val campos get() = listOf(nome, fabricante, modelo, versao, ano) + definicoes.map { valores[it.id].orEmpty() }
}

/** T04: cadastro/edição pessoal. Categoria fixa (DEC-021); definições copiadas no registro (DEC-042). */
class FormularioPessoalViewModel(
    private val categoriaId: String,
    private val produtoId: String?,
    catalogo: CatalogoRepository,
    private val repositorio: ProdutoPessoalRepository,
    private val salvar: SalvarProdutoPessoal,
) : ViewModel() {
    private val _estado = MutableStateFlow(FormularioUiState())
    val estado = _estado.asStateFlow()
    private var inicial: List<String> = emptyList()
    private var existente: ProdutoPessoal? = null
    private var revisao: String? = null

    init {
        viewModelScope.launch {
            if (produtoId != null) {
                val p = runCatching { repositorio.obter(produtoId) }.getOrNull()
                if (p == null) {
                    _estado.value = FormularioUiState(carregando = false, indisponivel = "Produto pessoal não encontrado.")
                    return@launch
                }
                existente = p
                revisao = p.revisaoDefinicao
                _estado.value = FormularioUiState(
                    carregando = false, edicao = true, categoriaNome = p.categoriaNome, definicoes = p.definicoes.sortedBy { it.ordem },
                    nome = p.identificacao.nome, fabricante = p.identificacao.fabricante.orEmpty(), modelo = p.identificacao.modelo.orEmpty(),
                    versao = p.identificacao.versao.orEmpty(), ano = p.identificacao.ano?.toString().orEmpty(),
                    valores = p.definicoes.associate { it.id to Formatos.paraEdicao(p.valores[it.id]) },
                )
            } else {
                val cats = catalogo.observarCategorias().filter { it !is EstadoConsulta.Carregando }.first()
                val c = (cats as? EstadoConsulta.Dados)?.dados?.firstOrNull { it.id == categoriaId }
                if (c == null) {
                    _estado.value = FormularioUiState(carregando = false, indisponivel = "Definições da categoria indisponíveis. Conecte-se e tente novamente.")
                    return@launch
                }
                revisao = c.revisaoDefinicao
                _estado.value = FormularioUiState(carregando = false, categoriaNome = c.nome, definicoes = c.criteriosOrdenados)
            }
            inicial = _estado.value.campos
        }
    }

    val pendente: Boolean get() = !_estado.value.salvo && _estado.value.campos != inicial

    fun alterar(campo: String, valor: String) = _estado.update {
        when (campo) {
            "nome" -> it.copy(nome = valor)
            "fabricante" -> it.copy(fabricante = valor)
            "modelo" -> it.copy(modelo = valor)
            "versao" -> it.copy(versao = valor)
            "ano" -> it.copy(ano = valor)
            else -> it.copy(valores = it.valores + (campo to valor), erros = it.erros - campo)
        }.copy(erros = it.erros - campo)
    }

    fun salvar(aoConcluir: () -> Unit = {}) {
        val s = _estado.value
        val erros = linkedMapOf<String, String>()
        if (s.nome.isBlank()) erros["nome"] = "Informe o nome."
        val ano = s.ano.trim().takeIf { it.isNotEmpty() }?.let { t -> t.toIntOrNull().also { if (it == null || it !in 1900..2100) erros["ano"] = "Ano inválido." } }
        val valores = mutableMapOf<String, ValorCanonico>()
        for (c in s.definicoes) {
            when (val r = EntradaPtBr.interpretar(s.valores[c.id].orEmpty(), c)) {
                ResultadoEntrada.Vazio -> if (c.validacao.obrigatorioCadastro) erros[c.id] = "Valor obrigatório."
                is ResultadoEntrada.Invalido -> erros[c.id] = r.mensagem
                is ResultadoEntrada.Valido -> valores[c.id] = r.valor
            }
        }
        if (erros.isNotEmpty()) {
            _estado.update { it.copy(erros = erros, erroGeral = "Corrija os campos indicados.", focarErro = it.focarErro + 1) }
            return
        }
        _estado.update { it.copy(salvando = true, erroGeral = null) }
        viewModelScope.launch {
            val anterior = existente
            val produto = ProdutoPessoal(
                id = anterior?.id ?: UUID.randomUUID().toString(),
                categoriaId = anterior?.categoriaId ?: categoriaId,
                categoriaNome = s.categoriaNome,
                revisaoDefinicao = revisao,
                identificacao = IdentificacaoProduto(
                    s.nome.trim(), s.fabricante.trim().ifEmpty { null }, s.modelo.trim().ifEmpty { null }, s.versao.trim().ifEmpty { null }, ano,
                ),
                definicoes = s.definicoes,
                valores = valores,
                // Valor alterado perde a referência anterior: ela não descreve o novo número.
                referencias = anterior?.referencias.orEmpty().filterKeys { anterior?.valores?.get(it) == valores[it] },
            )
            when (val r = salvar(produto)) {
                is ResultadoOperacao.Sucesso -> {
                    _estado.update { it.copy(salvando = false, salvo = true) }
                    aoConcluir()
                }
                is ResultadoOperacao.Erro -> _estado.update { it.copy(salvando = false, erroGeral = r.mensagem) }
            }
        }
    }
}

@Composable
fun TelaFormularioPessoal(vm: FormularioPessoalViewModel, onSair: () -> Unit) {
    val s by vm.estado.collectAsStateWithLifecycle()
    var dialogoSaida by rememberSaveable { mutableStateOf(false) }
    val tentarSair = { if (vm.pendente) dialogoSaida = true else onSair() }
    BackHandler(onBack = tentarSair)
    LaunchedEffect(s.salvo) { if (s.salvo && !dialogoSaida) onSair() }

    val ordemCampos = listOf("nome", "fabricante", "modelo", "versao", "ano") + s.definicoes.map { it.id }
    val focos = remember(ordemCampos) { ordemCampos.associateWith { FocusRequester() } }
    LaunchedEffect(s.focarErro) {
        if (s.focarErro > 0) ordemCampos.firstOrNull { it in s.erros }?.let { runCatching { focos.getValue(it).requestFocus() } }
    }

    TelaBase(
        titulo = if (s.edicao) "Editar produto pessoal" else "Novo produto pessoal",
        subtitulo = s.categoriaNome.ifEmpty { null },
        onVoltar = tentarSair,
        rodape = if (s.carregando || s.indisponivel != null) null else ({
            BotaoPrincipal(if (s.salvando) "Salvando…" else "Salvar produto", { vm.salvar() }, habilitado = !s.salvando)
        }),
    ) {
        when {
            s.carregando -> Carregando()
            s.indisponivel != null -> Mensagem(s.indisponivel!!, Tom.ERRO)
            else -> {
                Text("Categoria: ${s.categoriaNome} — definida no cadastro e não pode ser alterada.", style = MaterialTheme.typography.bodySmall)
                s.erroGeral?.let { Mensagem(it, Tom.ERRO) }
                Campo("Nome *", s.nome, s.erros["nome"], focos.getValue("nome")) { vm.alterar("nome", it) }
                Campo("Fabricante", s.fabricante, null, focos.getValue("fabricante")) { vm.alterar("fabricante", it) }
                Campo("Modelo", s.modelo, null, focos.getValue("modelo")) { vm.alterar("modelo", it) }
                Campo("Versão", s.versao, null, focos.getValue("versao")) { vm.alterar("versao", it) }
                Campo("Ano", s.ano, s.erros["ano"], focos.getValue("ano"), numerico = true) { vm.alterar("ano", it) }
                Text("Valores dos critérios", style = MaterialTheme.typography.titleMedium)
                s.definicoes.forEach { c ->
                    val ajuda = buildString {
                        append(Unidades.rotulo(c.unidade)); append(" · ")
                        append(if (c.natureza == Natureza.BENEFICIO) "benefício" else "custo")
                        if (c.validacao.minimo != null || c.validacao.maximo != null) append(" · intervalo ${br.edu.ufu.pdm.mcdm.domain.validation.ValidadorValor.descreverLimites(c.validacao)}")
                    }
                    Campo(c.nome + if (c.validacao.obrigatorioCadastro) " *" else "", s.valores[c.id].orEmpty(), s.erros[c.id], focos.getValue(c.id), ajuda = ajuda, numerico = true) {
                        vm.alterar(c.id, it)
                    }
                }
                Text("Use vírgula para decimais (ex.: 5.000,00).", style = MaterialTheme.typography.bodySmall)
            }
        }
    }

    if (dialogoSaida) {
        DialogoSaida(
            salvando = s.salvando,
            erro = s.erroGeral,
            onSalvar = { vm.salvar { dialogoSaida = false; onSair() } },
            onDescartar = { dialogoSaida = false; onSair() },
            onContinuar = { dialogoSaida = false },
        )
    }
}

@Composable
private fun Campo(
    rotulo: String,
    valor: String,
    erro: String?,
    foco: FocusRequester,
    ajuda: String? = null,
    numerico: Boolean = false,
    onChange: (String) -> Unit,
) {
    Column {
        OutlinedTextField(
            value = valor,
            onValueChange = onChange,
            label = { Text(rotulo) },
            isError = erro != null,
            supportingText = (erro ?: ajuda)?.let { t -> { Text(t) } },
            singleLine = true,
            keyboardOptions = if (numerico) KeyboardOptions(keyboardType = KeyboardType.Decimal) else KeyboardOptions.Default,
            modifier = Modifier.fillMaxWidth().focusRequester(foco),
        )
    }
}
