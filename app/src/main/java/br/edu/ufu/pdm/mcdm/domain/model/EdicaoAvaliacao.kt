package br.edu.ufu.pdm.mcdm.domain.model

/** Validade de um resultado na edição (Parte 4). */
enum class Vigencia { AUSENTE, VIGENTE, DESATUALIZADO }

/**
 * Edição em memória, separada da versão salva (DEC-025/045).
 * Mantém o último resultado calculado e sua vigência; resultado desatualizado nunca é salvo nem
 * publicado como vigente.
 */
data class EdicaoAvaliacao(
    val conteudo: Avaliacao,
    val vigenciaAhp: Vigencia,
    val vigenciaTopsis: Vigencia,
) {
    val ahpVigente: ResultadoAhp? get() = conteudo.ahp.takeIf { vigenciaAhp == Vigencia.VIGENTE }
    val topsisVigente: ResultadoTopsis? get() = conteudo.topsis.takeIf { vigenciaTopsis == Vigencia.VIGENTE && ahpVigente?.consistente == true }

    /** Conteúdo que seria gravado agora: resultados desatualizados ficam NULL (DEC-039). */
    fun conteudoSalvavel(): Avaliacao = conteudo.copy(ahp = ahpVigente, topsis = topsisVigente)

    /** Há alterações não salvas, incluindo resultados recém-calculados. */
    fun pendente(salva: Avaliacao?): Boolean {
        val atual = conteudoSalvavel()
        if (salva == null) return true
        return atual.copy(salvaEm = salva.salvaEm) != salva
    }

    // ----- alterações de entrada -----

    private fun alterarEntradasTopsis(nova: Avaliacao) = copy(
        conteudo = nova,
        vigenciaTopsis = if (vigenciaTopsis == Vigencia.AUSENTE) Vigencia.AUSENTE else Vigencia.DESATUALIZADO,
    )

    fun comAlternativas(alternativas: List<Alternativa>) =
        alterarEntradasTopsis(conteudo.copy(alternativas = alternativas.mapIndexed { i, a -> a.copy(ordem = i) }))

    fun comValor(alternativaId: String, criterioId: String, valor: ValorAlternativa): EdicaoAvaliacao {
        val alts = conteudo.alternativas.map { a ->
            if (a.alternativaId != alternativaId) a else a.copy(valores = a.valores + (criterioId to valor))
        }
        return alterarEntradasTopsis(conteudo.copy(alternativas = alts))
    }

    /** Alterar julgamento desatualiza AHP e TOPSIS. Registrar resposta idêntica não muda nada. */
    fun comJulgamento(julgamento: Julgamento): EdicaoAvaliacao {
        if (conteudo.julgamentos[julgamento.par] == julgamento) return this
        return copy(
            conteudo = conteudo.copy(julgamentos = conteudo.julgamentos + (julgamento.par to julgamento)),
            vigenciaAhp = if (vigenciaAhp == Vigencia.AUSENTE) Vigencia.AUSENTE else Vigencia.DESATUALIZADO,
            vigenciaTopsis = if (vigenciaTopsis == Vigencia.AUSENTE) Vigencia.AUSENTE else Vigencia.DESATUALIZADO,
        )
    }

    fun comTitulo(titulo: String?) = copy(conteudo = conteudo.copy(titulo = titulo?.takeIf { it.isNotBlank() }))

    // ----- resultados -----

    /** Novo AHP: se diferente do anterior (ou o anterior não era vigente), TOPSIS fica desatualizado. */
    fun comAhp(ahp: ResultadoAhp): EdicaoAvaliacao {
        val igual = vigenciaAhp == Vigencia.VIGENTE && conteudo.ahp == ahp
        return copy(
            conteudo = conteudo.copy(ahp = ahp),
            vigenciaAhp = Vigencia.VIGENTE,
            vigenciaTopsis = when {
                igual -> vigenciaTopsis
                vigenciaTopsis == Vigencia.AUSENTE -> Vigencia.AUSENTE
                else -> Vigencia.DESATUALIZADO
            },
        )
    }

    fun comTopsis(topsis: ResultadoTopsis) = copy(conteudo = conteudo.copy(topsis = topsis), vigenciaTopsis = Vigencia.VIGENTE)

    companion object {
        fun nova(avaliacao: Avaliacao) = EdicaoAvaliacao(avaliacao, Vigencia.AUSENTE, Vigencia.AUSENTE)

        /** Abrir uma versão salva não marca mudança e preserva seus resultados como vigentes. */
        fun deSalva(salva: Avaliacao) = EdicaoAvaliacao(
            conteudo = salva,
            vigenciaAhp = if (salva.ahp != null) Vigencia.VIGENTE else Vigencia.AUSENTE,
            vigenciaTopsis = if (salva.topsis != null) Vigencia.VIGENTE else Vigencia.AUSENTE,
        )
    }
}
