package br.edu.ufu.pdm.mcdm.data.mapper

import br.edu.ufu.pdm.mcdm.data.local.AlternativaEntity
import br.edu.ufu.pdm.mcdm.data.local.AvaliacaoEntity
import br.edu.ufu.pdm.mcdm.data.local.Codecs
import br.edu.ufu.pdm.mcdm.data.local.CriterioPreservadoEntity
import br.edu.ufu.pdm.mcdm.data.local.JulgamentoEntity
import br.edu.ufu.pdm.mcdm.data.local.LinhasAvaliacao
import br.edu.ufu.pdm.mcdm.data.local.ProdutoPessoalEntity
import br.edu.ufu.pdm.mcdm.data.local.RepresentacaoInvalidaException
import br.edu.ufu.pdm.mcdm.data.local.ValorAlternativaEntity
import br.edu.ufu.pdm.mcdm.domain.model.Alternativa
import br.edu.ufu.pdm.mcdm.domain.model.Avaliacao
import br.edu.ufu.pdm.mcdm.domain.model.CriterioDefinicao
import br.edu.ufu.pdm.mcdm.domain.model.DetalheRanking
import br.edu.ufu.pdm.mcdm.domain.model.DirecaoJulgamento
import br.edu.ufu.pdm.mcdm.domain.model.Distancias
import br.edu.ufu.pdm.mcdm.domain.model.IdentificacaoProduto
import br.edu.ufu.pdm.mcdm.domain.model.Julgamento
import br.edu.ufu.pdm.mcdm.domain.model.Natureza
import br.edu.ufu.pdm.mcdm.domain.model.OrigemProduto
import br.edu.ufu.pdm.mcdm.domain.model.ParCriterios
import br.edu.ufu.pdm.mcdm.domain.model.ProdutoPessoal
import br.edu.ufu.pdm.mcdm.domain.model.ResultadoAhp
import br.edu.ufu.pdm.mcdm.domain.model.ResultadoTopsis
import br.edu.ufu.pdm.mcdm.domain.model.ValorAlternativa
import java.time.Instant
import java.util.UUID

object AvaliacaoMapper {
    private const val AUSENTE = "AUSENTE"
    private const val RANKING = "RANKING"
    private const val SEM_DIFERENCIACAO = "SEM_DIFERENCIACAO"

    fun paraLinhas(a: Avaliacao): LinhasAvaliacao {
        val id = a.id
        val ahp = a.ahp
        val topsis = a.topsis
        val raiz = AvaliacaoEntity(
            avaliacaoId = id,
            categoriaId = a.categoriaId,
            categoriaNome = a.categoriaNome,
            revisaoDefinicao = a.revisaoDefinicao,
            titulo = a.titulo,
            estadoSalvo = a.estado.name,
            tipoTopsis = when (topsis) {
                null -> AUSENTE
                is ResultadoTopsis.Ranking -> RANKING
                is ResultadoTopsis.SemDiferenciacao -> SEM_DIFERENCIACAO
            },
            criadaEm = a.criadaEm.toEpochMilli(),
            salvaEm = requireNotNull(a.salvaEm) { "salvaEm obrigatório ao gravar." }.toEpochMilli(),
            lambdaMax = ahp?.lambdaMax, ic = ahp?.ic, ri = ahp?.ri, rc = ahp?.rc,
        )
        val criterios = a.criterios.map { c ->
            CriterioPreservadoEntity(
                avaliacaoId = id, criterioId = c.id, nome = c.nome, ordem = c.ordem, unidade = c.unidade,
                natureza = c.natureza.name, validacaoJson = Codecs.validacaoParaJson(c.validacao), descricao = c.descricao,
                pesoAhp = ahp?.pesos?.get(c.id),
            )
        }
        val alternativas = a.alternativas.map { alt ->
            val rank = (topsis as? ResultadoTopsis.Ranking)?.detalhes?.get(alt.alternativaId)
            val semDif = (topsis as? ResultadoTopsis.SemDiferenciacao)?.distancias?.get(alt.alternativaId)
            AlternativaEntity(
                avaliacaoId = id, alternativaId = alt.alternativaId, origemProduto = alt.origemProduto.name,
                idProdutoOrigem = alt.idProdutoOrigem, nome = alt.identificacao.nome, fabricante = alt.identificacao.fabricante,
                modelo = alt.identificacao.modelo, versao = alt.identificacao.versao, ano = alt.identificacao.ano, ordem = alt.ordem,
                atualizadoNaOrigemEm = alt.atualizadoNaOrigemEm?.toEpochMilli(),
                dMais = rank?.dMais ?: semDif?.dMais, dMenos = rank?.dMenos ?: semDif?.dMenos,
                coeficiente = rank?.coeficiente, posicao = rank?.posicao,
            )
        }
        val valores = a.alternativas.flatMap { alt ->
            alt.valores.map { (criterioId, v) ->
                val o = Codecs.paraColunas(v.origem)
                val e = Codecs.paraColunas(v.efetivo)
                ValorAlternativaEntity(
                    avaliacaoId = id, alternativaId = alt.alternativaId, criterioId = criterioId,
                    origemTipo = o.tipo, origemCentavos = o.centavos, origemNumero = o.numero,
                    origemProveniencia = Codecs.provParaJson(v.provenienciaOrigem),
                    efetivoTipo = e.tipo, efetivoCentavos = e.centavos, efetivoNumero = e.numero,
                    efetivoProveniencia = Codecs.provParaJson(v.provenienciaEfetivo),
                    entradaPendenteEfetiva = v.entradaPendente,
                )
            }
        }
        val julgamentos = a.julgamentos.values.map {
            JulgamentoEntity(id, it.par.menorId, it.par.maiorId, it.intensidade, it.direcao.name)
        }
        return LinhasAvaliacao(raiz, criterios, alternativas, valores, julgamentos)
    }

    /** Reconstrói o agregado; representação inválida lança [RepresentacaoInvalidaException]. */
    fun deLinhas(l: LinhasAvaliacao): Avaliacao {
        val r = l.avaliacao
        val criterios = l.criterios.map { c ->
            CriterioDefinicao(
                id = c.criterioId, nome = c.nome, ordem = c.ordem, unidade = c.unidade,
                natureza = Codecs.enumOuErro<Natureza>(c.natureza), validacao = Codecs.validacaoDeJson(c.validacaoJson), descricao = c.descricao,
            )
        }

        val ahp: ResultadoAhp? = when {
            r.lambdaMax == null && r.ic == null && r.ri == null && r.rc == null && l.criterios.all { it.pesoAhp == null } -> null
            r.lambdaMax != null && r.ic != null && r.ri != null && l.criterios.all { it.pesoAhp != null } ->
                ResultadoAhp(l.criterios.associate { it.criterioId to it.pesoAhp!! }, r.lambdaMax, r.ic, r.ri, r.rc)
            else -> throw RepresentacaoInvalidaException("Bloco AHP incompleto.")
        }

        val valoresPorAlt = l.valores.groupBy { it.alternativaId }
        val alternativas = l.alternativas.map { e ->
            validarUuid(e.alternativaId)
            Alternativa(
                alternativaId = e.alternativaId,
                origemProduto = Codecs.enumOuErro<OrigemProduto>(e.origemProduto),
                idProdutoOrigem = e.idProdutoOrigem,
                identificacao = IdentificacaoProduto(e.nome, e.fabricante, e.modelo, e.versao, e.ano),
                ordem = e.ordem,
                valores = valoresPorAlt[e.alternativaId].orEmpty().associate { v ->
                    v.criterioId to ValorAlternativa(
                        origem = Codecs.deColunas(v.origemTipo, v.origemCentavos, v.origemNumero),
                        provenienciaOrigem = Codecs.provDeJson(v.origemProveniencia),
                        efetivo = Codecs.deColunas(v.efetivoTipo, v.efetivoCentavos, v.efetivoNumero),
                        provenienciaEfetivo = Codecs.provDeJson(v.efetivoProveniencia),
                        entradaPendente = v.entradaPendenteEfetiva,
                    )
                },
                atualizadoNaOrigemEm = e.atualizadoNaOrigemEm?.let(Instant::ofEpochMilli),
            )
        }

        val topsis: ResultadoTopsis? = when (r.tipoTopsis) {
            AUSENTE -> {
                if (l.alternativas.any { it.dMais != null || it.dMenos != null || it.coeficiente != null || it.posicao != null }) {
                    throw RepresentacaoInvalidaException("Detalhe TOPSIS em avaliação sem TOPSIS.")
                }
                null
            }
            RANKING -> ResultadoTopsis.Ranking(
                l.alternativas.associate { e ->
                    if (e.dMais == null || e.dMenos == null || e.coeficiente == null || e.posicao == null) {
                        throw RepresentacaoInvalidaException("Detalhe de ranking incompleto.")
                    }
                    e.alternativaId to DetalheRanking(e.dMais, e.dMenos, e.coeficiente, e.posicao)
                },
            )
            SEM_DIFERENCIACAO -> ResultadoTopsis.SemDiferenciacao(
                l.alternativas.associate { e ->
                    if (e.dMais == null || e.dMenos == null || e.coeficiente != null || e.posicao != null) {
                        throw RepresentacaoInvalidaException("Sem diferenciação com C*/posição ou distância ausente.")
                    }
                    e.alternativaId to Distancias(e.dMais, e.dMenos)
                },
            )
            else -> throw RepresentacaoInvalidaException("tipoTopsis desconhecido: ${r.tipoTopsis}.")
        }

        val julgamentos = l.julgamentos.associate { j ->
            val par = try {
                ParCriterios.canonico(j.criterioMenorId, j.criterioMaiorId)
            } catch (e: IllegalArgumentException) {
                throw RepresentacaoInvalidaException(e.message ?: "Par inválido.")
            }
            par to Julgamento(par, j.intensidade, Codecs.enumOuErro<DirecaoJulgamento>(j.direcao))
        }

        val a = Avaliacao(
            id = r.avaliacaoId, categoriaId = r.categoriaId, categoriaNome = r.categoriaNome, revisaoDefinicao = r.revisaoDefinicao,
            titulo = r.titulo, criterios = criterios, alternativas = alternativas, julgamentos = julgamentos,
            ahp = ahp, topsis = topsis, criadaEm = Instant.ofEpochMilli(r.criadaEm), salvaEm = Instant.ofEpochMilli(r.salvaEm),
        )
        if (a.estado.name != r.estadoSalvo) throw RepresentacaoInvalidaException("estadoSalvo diverge do resultado TOPSIS.")
        return a
    }

    fun validarUuid(s: String) {
        val ok = try {
            UUID.fromString(s).toString() == s
        } catch (_: IllegalArgumentException) {
            false
        }
        if (!ok) throw RepresentacaoInvalidaException("UUID não canônico: $s.")
    }

    // ----- produto pessoal -----

    fun produtoParaEntidade(p: ProdutoPessoal) = ProdutoPessoalEntity(
        produtoId = p.id, categoriaId = p.categoriaId, categoriaNome = p.categoriaNome, revisaoDefinicao = p.revisaoDefinicao,
        nome = p.identificacao.nome, fabricante = p.identificacao.fabricante, modelo = p.identificacao.modelo,
        versao = p.identificacao.versao, ano = p.identificacao.ano,
        payloadJson = Codecs.payloadParaJson(p.definicoes, p.valores, p.referencias), formatoPayload = Codecs.FORMATO_PAYLOAD,
    )

    fun produtoDeEntidade(e: ProdutoPessoalEntity): ProdutoPessoal {
        validarUuid(e.produtoId)
        val p = Codecs.payloadDeJson(e.payloadJson, e.formatoPayload)
        return ProdutoPessoal(
            id = e.produtoId, categoriaId = e.categoriaId, categoriaNome = e.categoriaNome, revisaoDefinicao = e.revisaoDefinicao,
            identificacao = IdentificacaoProduto(e.nome, e.fabricante, e.modelo, e.versao, e.ano),
            definicoes = p.definicoes, valores = p.valores, referencias = p.referencias,
        )
    }
}
