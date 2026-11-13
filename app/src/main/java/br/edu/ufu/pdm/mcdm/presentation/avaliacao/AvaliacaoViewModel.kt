package br.edu.ufu.pdm.mcdm.presentation.avaliacao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.edu.ufu.pdm.mcdm.domain.exportacao.RelatorioGerado
import br.edu.ufu.pdm.mcdm.domain.model.Avaliacao
import br.edu.ufu.pdm.mcdm.domain.model.Categoria
import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.EdicaoAvaliacao
import br.edu.ufu.pdm.mcdm.domain.model.EstadoConsulta
import br.edu.ufu.pdm.mcdm.domain.model.EstadoSalvo
import br.edu.ufu.pdm.mcdm.domain.model.IdentificacaoProduto
import br.edu.ufu.pdm.mcdm.domain.model.Julgamento
import br.edu.ufu.pdm.mcdm.domain.model.OrigemProduto
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoOficial
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoPessoal
import br.edu.ufu.pdm.mcdm.domain.model.Proveniencia
import br.edu.ufu.pdm.mcdm.domain.model.ValorAlternativa
import br.edu.ufu.pdm.mcdm.domain.repository.AvaliacaoCorrompidaException
import br.edu.ufu.pdm.mcdm.domain.repository.AvaliacaoRepository
import br.edu.ufu.pdm.mcdm.domain.repository.CatalogoRepository
import br.edu.ufu.pdm.mcdm.domain.repository.ProdutoPessoalRepository
import br.edu.ufu.pdm.mcdm.domain.usecase.CalcularAhp
import br.edu.ufu.pdm.mcdm.domain.usecase.Elegibilidade
import br.edu.ufu.pdm.mcdm.domain.usecase.ExportarAvaliacao
import br.edu.ufu.pdm.mcdm.domain.usecase.PreparacaoTopsis
import br.edu.ufu.pdm.mcdm.domain.usecase.PrepararAlternativa
import br.edu.ufu.pdm.mcdm.domain.usecase.Problema
import br.edu.ufu.pdm.mcdm.domain.usecase.ProcessarAvaliacao
import br.edu.ufu.pdm.mcdm.domain.usecase.ResultadoCalculo
import br.edu.ufu.pdm.mcdm.domain.usecase.ResultadoOperacao
import br.edu.ufu.pdm.mcdm.domain.usecase.SalvarAvaliacao
import br.edu.ufu.pdm.mcdm.domain.validation.EntradaPtBr
import br.edu.ufu.pdm.mcdm.domain.validation.Formatos
import br.edu.ufu.pdm.mcdm.domain.validation.ResultadoEntrada
import br.edu.ufu.pdm.mcdm.presentation.components.Tom
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.Instant
import java.util.UUID

enum class Passo { SELECAO, VALORES, AHP, PESOS, REVISAO, RESULTADO }

/** Produto disponível para seleção, com elegibilidade calculada contra os critérios da avaliação. */
data class Candidato(
    val chave: String,
    val origem: OrigemProduto,
    val id: String,
    val identificacao: IdentificacaoProduto,
    val elegibilidade: Elegibilidade,
    val oficial: ProdutoOficial? = null,
    val pessoal: ProdutoPessoal? = null,
)

data class SessaoUiState(
    val carregando: Boolean = true,
    val erroCarga: String? = null,
    val edicao: EdicaoAvaliacao? = null,
    val salva: Avaliacao? = null,
    val textos: Map<String, String> = emptyMap(),
    val errosValores: Map<String, String> = emptyMap(),
    val calculandoAhp: Boolean = false,
    val processando: Boolean = false,
    val salvando: Boolean = false,
    val exportando: Boolean = false,
    val mensagem: Pair<String, Tom>? = null,
    val problemas: List<Problema> = emptyList(),
    val erroSaida: String? = null,
) {
    val pendente: Boolean get() = edicao?.pendente(salva) ?: false
    val criterios: List<CriterioDefinicao> get() = edicao?.conteudo?.criteriosOrdenados.orEmpty()
}

/** Eventos de uso único consumidos pela UI. */
sealed interface EventoSessao {
    data class Navegar(val passo: Passo, val substituirInicio: Boolean = false) : EventoSessao
    data object Sair : EventoSessao
    data class Compartilhar(val relatorio: RelatorioGerado) : EventoSessao
    data class FocarValor(val chave: String) : EventoSessao
}

fun chaveValor(alternativaId: String, criterioId: String) = "$alternativaId|$criterioId"

/**
 * Sessão de avaliação (DEC-045): dono = entrada do grafo aninhado de navegação.
 * A edição em memória é separada da versão salva; nada é salvo sem ação explícita (DEC-025).
 * Cada alteração de entrada incrementa [geracao]; respostas de cálculo de outra geração são
 * descartadas (sem publicação tardia).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AvaliacaoViewModel(
    private val avaliacaoId: String?,
    private val categoriaId: String?,
    private val catalogo: CatalogoRepository,
    private val pessoais: ProdutoPessoalRepository,
    private val avaliacoes: AvaliacaoRepository,
    private val calcularAhp: CalcularAhp,
    private val processarAvaliacao: ProcessarAvaliacao,
    private val salvarAvaliacao: SalvarAvaliacao,
    private val exportarAvaliacao: ExportarAvaliacao,
    private val preparar: PrepararAlternativa,
    private val relogio: Clock,
) : ViewModel() {

    private val _estado = MutableStateFlow(SessaoUiState())
    val estado: StateFlow<SessaoUiState> = _estado.asStateFlow()

    private val _eventos = MutableStateFlow<List<EventoSessao>>(emptyList())
    val eventos = _eventos.asStateFlow()
    fun consumir(e: EventoSessao) = _eventos.update { it - e }
    private fun emitir(e: EventoSessao) = _eventos.update { it + e }

    private var geracao = 0L
    private val categoriaDaSessao = MutableStateFlow<String?>(null)

    init {
        viewModelScope.launch { carregar() }
    }

    private suspend fun carregar() {
        if (avaliacaoId != null) {
            val salva = try {
                avaliacoes.obter(avaliacaoId)
            } catch (e: AvaliacaoCorrompidaException) {
                _estado.value = SessaoUiState(carregando = false, erroCarga = "A avaliação salva está inconsistente e não pode ser apresentada como íntegra: ${e.problemas.first()}")
                return
            } catch (e: Exception) {
                _estado.value = SessaoUiState(carregando = false, erroCarga = "Falha ao ler a avaliação salva.")
                return
            }
            if (salva == null) {
                _estado.value = SessaoUiState(carregando = false, erroCarga = "Avaliação não encontrada.")
                return
            }
            iniciar(EdicaoAvaliacao.deSalva(salva), salva)
            emitir(EventoSessao.Navegar(primeiraPendencia(salva), substituirInicio = true))
        } else {
            val catId = requireNotNull(categoriaId)
            val cats = catalogo.observarCategorias().filter { it !is EstadoConsulta.Carregando }.first()
            val categoria = (cats as? EstadoConsulta.Dados)?.dados?.firstOrNull { it.id == catId }
            if (categoria == null) {
                _estado.value = SessaoUiState(carregando = false, erroCarga = "Definições da categoria indisponíveis. Uma nova avaliação exige as definições do catálogo; conecte-se e tente novamente.")
                return
            }
            iniciar(EdicaoAvaliacao.nova(novaAvaliacao(categoria)), null)
            emitir(EventoSessao.Navegar(Passo.SELECAO, substituirInicio = true))
        }
    }

    private fun novaAvaliacao(c: Categoria) = Avaliacao(
        id = UUID.randomUUID().toString(),
        categoriaId = c.id, categoriaNome = c.nome, revisaoDefinicao = c.revisaoDefinicao, titulo = null,
        criterios = c.criteriosOrdenados, alternativas = emptyList(), julgamentos = emptyMap(),
        ahp = null, topsis = null, criadaEm = Instant.now(relogio), salvaEm = null,
    )

    private fun iniciar(edicao: EdicaoAvaliacao, salva: Avaliacao?) {
        _estado.value = SessaoUiState(carregando = false, edicao = edicao, salva = salva, textos = textosDe(edicao.conteudo))
        categoriaDaSessao.value = edicao.conteudo.categoriaId
    }

    private fun textosDe(a: Avaliacao): Map<String, String> = buildMap {
        for (alt in a.alternativas) for (c in a.criterios) {
            val v = alt.valores[c.id]
            put(chaveValor(alt.alternativaId, c.id), v?.entradaPendente ?: Formatos.paraEdicao(v?.efetivo))
        }
    }

    /** Retomada abre a primeira pendência útil; concluída abre o resultado (consulta). */
    private fun primeiraPendencia(a: Avaliacao): Passo = when {
        a.topsis != null -> Passo.RESULTADO
        a.alternativas.size < 2 -> Passo.SELECAO
        a.alternativas.any { alt -> a.criterios.any { alt.valores[it.id]?.efetivo == null } } -> Passo.VALORES
        !a.julgamentosCompletos -> Passo.AHP
        a.ahp == null -> Passo.AHP
        else -> Passo.REVISAO
    }

    // ------------------------------------------------------------ seleção

    /** Candidatos oficiais + pessoais da categoria, com elegibilidade. */
    val candidatos: StateFlow<Pair<EstadoConsulta<*>, List<Candidato>>> = categoriaDaSessao.flatMapLatest { catId ->
        if (catId == null) return@flatMapLatest emptyFlow()
        combine(
            catalogo.observarCategorias(),
            catalogo.observarProdutos(catId),
            pessoais.observarPorCategoria(catId).catch { emit(emptyList()) },
            _estado.map { it.criterios }.filter { it.isNotEmpty() }.distinctUntilChanged(),
        ) { cats, oficiais, lista, destino ->
            val categoria = (cats as? EstadoConsulta.Dados)?.dados?.firstOrNull { it.id == catId }
            val ofs = (oficiais as? EstadoConsulta.Dados)?.dados.orEmpty().map {
                Candidato("OFICIAL:${it.id}", OrigemProduto.OFICIAL, it.id, it.identificacao, preparar.elegibilidadeOficial(it, categoria, destino), oficial = it)
            }
            val pes = lista.map {
                Candidato("PESSOAL:${it.id}", OrigemProduto.PESSOAL, it.id, it.identificacao, preparar.elegibilidadePessoal(it, destino), pessoal = it)
            }
            (oficiais as EstadoConsulta<*>) to (ofs + pes)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EstadoConsulta.Carregando to emptyList())

    private fun alterar(bloco: (EdicaoAvaliacao) -> EdicaoAvaliacao) {
        _estado.update { s ->
            val e = s.edicao ?: return@update s
            val nova = bloco(e)
            if (nova != e) geracao++
            s.copy(edicao = nova, mensagem = null, problemas = emptyList())
        }
    }

    fun adicionar(c: Candidato) {
        val e = _estado.value.edicao ?: return
        if (c.elegibilidade is Elegibilidade.Bloqueada) return
        if (e.conteudo.alternativas.any { it.chaveOrigem == c.chave }) return
        val ordem = e.conteudo.alternativas.size
        val alt = when {
            c.oficial != null -> preparar.deOficial(c.oficial, e.conteudo.criterios, ordem)
            c.pessoal != null -> preparar.dePessoal(c.pessoal, e.conteudo.criterios, ordem)
            else -> return
        }
        alterar { it.comAlternativas(it.conteudo.alternativasOrdenadas + alt) }
        _estado.update { s -> s.copy(textos = s.textos + textosDe(s.edicao!!.conteudo.copy(alternativas = listOf(alt)))) }
    }

    fun remover(alternativaId: String) {
        alterar { it.comAlternativas(it.conteudo.alternativasOrdenadas.filterNot { a -> a.alternativaId == alternativaId }) }
        _estado.update { s -> s.copy(textos = s.textos.filterKeys { !it.startsWith("$alternativaId|") }, errosValores = s.errosValores.filterKeys { !it.startsWith("$alternativaId|") }) }
    }

    fun alterarTitulo(t: String) = alterar { it.comTitulo(t) }

    // ------------------------------------------------------------ valores

    fun alterarValor(alternativaId: String, criterio: CriterioDefinicao, texto: String) {
        val e = _estado.value.edicao ?: return
        val alt = e.conteudo.alternativas.firstOrNull { it.alternativaId == alternativaId } ?: return
        val atual = alt.valores[criterio.id] ?: ValorAlternativa.copiaDoOriginal(null, Proveniencia.DESCONHECIDA)
        val chave = chaveValor(alternativaId, criterio.id)
        val (efetivo, pendente, erro) = when (val r = EntradaPtBr.interpretar(texto, criterio)) {
            ResultadoEntrada.Vazio -> Triple(null, null, null)
            is ResultadoEntrada.Valido -> Triple(r.valor, null, null)
            is ResultadoEntrada.Invalido -> Triple(null, texto, r.mensagem)
        }
        // Efetivo igual ao original mantém a proveniência original; ajuste nunca herda fonte/data.
        val prov = if (pendente == null && efetivo == atual.origem) atual.provenienciaOrigem else Proveniencia.AJUSTE
        val novo = atual.copy(efetivo = efetivo, provenienciaEfetivo = prov, entradaPendente = pendente)
        if (novo != atual) alterar { it.comValor(alternativaId, criterio.id, novo) }
        _estado.update { s ->
            s.copy(textos = s.textos + (chave to texto), errosValores = if (erro != null) s.errosValores + (chave to erro) else s.errosValores - chave)
        }
    }

    fun restaurarOriginal(alternativaId: String, criterio: CriterioDefinicao) {
        val alt = _estado.value.edicao?.conteudo?.alternativas?.firstOrNull { it.alternativaId == alternativaId } ?: return
        alterarValor(alternativaId, criterio, Formatos.paraEdicao(alt.valores[criterio.id]?.origem))
    }

    /** Avança se todos os valores efetivos são válidos; senão foca o primeiro campo inválido. */
    fun validarValoresEAvancar() {
        val s = _estado.value
        val e = s.edicao ?: return
        val primeiro = e.conteudo.alternativasOrdenadas.firstNotNullOfOrNull { a ->
            e.conteudo.criteriosOrdenados.firstOrNull { c -> a.valores[c.id]?.efetivo == null }?.let { chaveValor(a.alternativaId, it.id) }
        }
        if (primeiro != null) {
            _estado.update {
                it.copy(
                    errosValores = it.errosValores + (primeiro to (it.errosValores[primeiro] ?: "Valor obrigatório para o processamento.")),
                    mensagem = "Há valores ausentes ou inválidos. Você pode salvar como rascunho ou corrigir." to Tom.AVISO,
                )
            }
            emitir(EventoSessao.FocarValor(primeiro))
        } else {
            emitir(EventoSessao.Navegar(Passo.AHP))
        }
    }

    // ------------------------------------------------------------ AHP

    fun responder(a: String, b: String, preferidoId: String?, intensidade: Int) {
        alterar { it.comJulgamento(Julgamento.deResposta(a, b, preferidoId, intensidade)) }
    }

    fun calcularPesos() {
        val s = _estado.value
        val e = s.edicao ?: return
        if (s.calculandoAhp) return
        val g = geracao
        _estado.update { it.copy(calculandoAhp = true, problemas = emptyList(), mensagem = null) }
        viewModelScope.launch {
            val r = calcularAhp(e.conteudo.criterios, e.conteudo.julgamentos)
            _estado.update { st ->
                if (g != geracao) return@update st.copy(calculandoAhp = false) // resposta tardia descartada
                when (r) {
                    is ResultadoCalculo.Sucesso -> st.copy(calculandoAhp = false, edicao = st.edicao!!.comAhp(r.valor))
                    is ResultadoCalculo.Bloqueado -> st.copy(calculandoAhp = false, problemas = r.problemas)
                    is ResultadoCalculo.Falha -> st.copy(calculandoAhp = false, mensagem = "Falha no cálculo AHP: ${r.problema.mensagem}" to Tom.ERRO)
                }
            }
            if (r is ResultadoCalculo.Sucesso && g == geracao) emitir(EventoSessao.Navegar(Passo.PESOS))
        }
    }

    // ------------------------------------------------------------ revisão / TOPSIS

    /** Pré-validação para a revisão (sem executar o motor). */
    fun problemasRevisao(): List<Problema> {
        val e = _estado.value.edicao ?: return emptyList()
        return when (val p = PreparacaoTopsis.preparar(e.conteudo.criterios, e.conteudo.alternativas, e.ahpVigente)) {
            is ResultadoCalculo.Bloqueado -> p.problemas
            else -> emptyList()
        }
    }

    fun processar() {
        val s = _estado.value
        val e = s.edicao ?: return
        if (s.processando) return
        val g = geracao
        _estado.update { it.copy(processando = true, problemas = emptyList(), mensagem = null) }
        viewModelScope.launch {
            val r = processarAvaliacao(e.conteudo.criterios, e.conteudo.alternativas, e.ahpVigente)
            var sucesso = false
            _estado.update { st ->
                if (g != geracao) return@update st.copy(processando = false)
                when (r) {
                    is ResultadoCalculo.Sucesso -> st.copy(processando = false, edicao = st.edicao!!.comTopsis(r.valor)).also { sucesso = true }
                    is ResultadoCalculo.Bloqueado -> st.copy(processando = false, problemas = r.problemas)
                    is ResultadoCalculo.Falha -> st.copy(
                        processando = false,
                        problemas = listOf(r.problema),
                        mensagem = "Falha matemática: nenhum resultado foi gerado." to Tom.ERRO,
                    )
                }
            }
            if (sucesso) emitir(EventoSessao.Navegar(Passo.RESULTADO))
        }
    }

    // ------------------------------------------------------------ salvar / sair

    fun salvar(sairDepois: Boolean = false, aposSalvar: (suspend (Avaliacao) -> Unit)? = null) {
        val s = _estado.value
        val e = s.edicao ?: return
        if (s.salvando) return
        val g = geracao
        _estado.update { it.copy(salvando = true, erroSaida = null) }
        viewModelScope.launch {
            when (val r = salvarAvaliacao(e)) {
                is ResultadoOperacao.Sucesso -> {
                    _estado.update { st ->
                        val salva = r.valor
                        val texto = when (salva.estado) {
                            EstadoSalvo.RASCUNHO -> "Rascunho salvo neste dispositivo."
                            EstadoSalvo.CONCLUIDA_RANKING -> "Avaliação concluída salva neste dispositivo."
                            EstadoSalvo.CONCLUIDA_SEM_DIFERENCIACAO -> "Avaliação concluída — sem diferenciação — salva neste dispositivo."
                        }
                        st.copy(
                            salvando = false, salva = salva,
                            edicao = if (g == geracao) EdicaoAvaliacao.deSalva(salva) else st.edicao,
                            mensagem = texto to Tom.SUCESSO,
                        )
                    }
                    if (sairDepois) emitir(EventoSessao.Sair)
                    aposSalvar?.invoke(r.valor)
                }
                is ResultadoOperacao.Erro -> _estado.update {
                    it.copy(salvando = false, mensagem = r.mensagem to Tom.ERRO, erroSaida = r.mensagem)
                }
            }
        }
    }

    // ------------------------------------------------------------ exportação (DEC-032)

    /** Pode salvar e exportar: há TOPSIS vigente, então o salvamento será uma conclusão. */
    val podeSalvarEExportar: Boolean get() = _estado.value.edicao?.topsisVigente != null
    val podeExportarSalva: Boolean get() = _estado.value.salva?.let { it.estado != EstadoSalvo.RASCUNHO } == true

    fun exportarSalva() {
        val id = _estado.value.salva?.id ?: return
        if (_estado.value.exportando) return
        _estado.update { it.copy(exportando = true) }
        viewModelScope.launch {
            when (val r = exportarAvaliacao(id)) {
                is ResultadoOperacao.Sucesso -> {
                    _estado.update { it.copy(exportando = false) }
                    emitir(EventoSessao.Compartilhar(r.valor))
                }
                is ResultadoOperacao.Erro -> _estado.update { it.copy(exportando = false, mensagem = r.mensagem to Tom.ERRO) }
            }
        }
    }

    /** Salvar e exportar: só gera o PDF após persistência confirmada. */
    fun salvarEExportar() {
        if (!podeSalvarEExportar) return
        salvar(aposSalvar = { exportarSalva() })
    }

    fun excluirSalva() {
        val id = _estado.value.salva?.id ?: return
        viewModelScope.launch {
            try {
                avaliacoes.excluir(id)
                emitir(EventoSessao.Sair)
            } catch (e: Exception) {
                _estado.update { it.copy(mensagem = "Falha ao excluir. A avaliação foi mantida." to Tom.ERRO) }
            }
        }
    }

    fun limparMensagem() = _estado.update { it.copy(mensagem = null) }

    // ------------------------------------------------------------ D01

    private val _dialogoSaida = MutableStateFlow(false)
    val dialogoSaida = _dialogoSaida.asStateFlow()

    /** Encerrar a sessão: sem pendências sai direto; com pendências oferece D01. */
    fun pedirSaida() {
        val s = _estado.value
        if (s.edicao == null || !s.pendente) emitir(EventoSessao.Sair) else _dialogoSaida.value = true
    }

    fun salvarESair() = salvar(sairDepois = true)

    /** Descartar: a sessão é encerrada e a versão salva permanece intacta. */
    fun descartarESair() {
        _dialogoSaida.value = false
        emitir(EventoSessao.Sair)
    }

    fun continuarEditando() {
        _dialogoSaida.value = false
        _estado.update { it.copy(erroSaida = null) }
    }
}
