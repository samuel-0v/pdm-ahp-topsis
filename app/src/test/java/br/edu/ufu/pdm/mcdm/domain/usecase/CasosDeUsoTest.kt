package br.edu.ufu.pdm.mcdm.domain.usecase

import br.edu.ufu.pdm.mcdm.domain.Fixtures
import br.edu.ufu.pdm.mcdm.domain.engine.assertProximo
import br.edu.ufu.pdm.mcdm.domain.model.Avaliacao
import br.edu.ufu.pdm.mcdm.domain.model.DirecaoJulgamento
import br.edu.ufu.pdm.mcdm.domain.model.EdicaoAvaliacao
import br.edu.ufu.pdm.mcdm.domain.model.EstadoSalvo
import br.edu.ufu.pdm.mcdm.domain.model.Julgamento
import br.edu.ufu.pdm.mcdm.domain.model.OrigemProduto
import br.edu.ufu.pdm.mcdm.domain.model.ParCriterios
import br.edu.ufu.pdm.mcdm.domain.model.ResultadoTopsis
import br.edu.ufu.pdm.mcdm.domain.model.ResumoAvaliacao
import br.edu.ufu.pdm.mcdm.domain.model.ValorAlternativa
import br.edu.ufu.pdm.mcdm.domain.model.ValorCanonico
import br.edu.ufu.pdm.mcdm.domain.model.Vigencia
import br.edu.ufu.pdm.mcdm.domain.repository.AvaliacaoRepository
import br.edu.ufu.pdm.mcdm.domain.validation.ValidadorAgregado
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class CasosDeUsoTest {
    private val c = Fixtures.criteriosSmartphone

    @Test
    fun `par canonico independe da ordem visual e inverte a direcao`() {
        val ab = Julgamento.deResposta("smartphones-preco", "smartphones-cameras", "smartphones-preco", 5)
        val ba = Julgamento.deResposta("smartphones-cameras", "smartphones-preco", "smartphones-preco", 5)
        assertEquals(ab, ba)
        assertEquals("smartphones-cameras", ab.par.menorId)
        assertEquals(DirecaoJulgamento.MAIOR_PREFERIDO, ab.direcao)
        assertProximo(0.2, ab.valorMenorSobreMaior)
    }

    @Test
    fun `intensidade 1 sempre e igualdade`() {
        val j = Julgamento.deResposta("a", "b", "a", 1)
        assertEquals(DirecaoJulgamento.IGUAL, j.direcao)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `par fora da ordem canonica e rejeitado ao carregar`() {
        ParCriterios.canonico("z", "a")
    }

    @Test
    fun `AHP bloqueia julgamentos incompletos e calcula quando completos`() = runTest {
        val calc = CalcularAhp(StandardTestDispatcher(testScheduler))
        assertTrue(calc(c, emptyMap()) is ResultadoCalculo.Bloqueado)
        val r = calc(c, Fixtures.julgamentosIguais()) as ResultadoCalculo.Sucesso
        c.forEach { assertProximo(0.2, r.valor.pesos.getValue(it.id)) }
        assertTrue(r.valor.consistente)
    }

    @Test
    fun `AHP respeita o sentido do julgamento na matriz`() = runTest {
        val js = Fixtures.julgamentosIguais().toMutableMap()
        val j = Julgamento.deResposta(c[0].id, c[1].id, c[1].id, 3) // Preço 3× Geekbench
        js[j.par] = j
        val r = CalcularAhp(StandardTestDispatcher(testScheduler))(c, js) as ResultadoCalculo.Sucesso
        assertTrue(r.valor.pesos.getValue(c[1].id) > r.valor.pesos.getValue(c[0].id))
    }

    private suspend fun processar(alts: List<br.edu.ufu.pdm.mcdm.domain.model.Alternativa>, d: kotlinx.coroutines.CoroutineDispatcher): ResultadoCalculo<ResultadoTopsis> {
        val ahp = (CalcularAhp(d)(c, Fixtures.julgamentosIguais()) as ResultadoCalculo.Sucesso).valor
        return ProcessarAvaliacao(d)(c, alts, ahp)
    }

    @Test
    fun `processamento produz ranking por identidade com empate`() = runTest {
        val d = StandardTestDispatcher(testScheduler)
        val r = processar(
            listOf(
                Fixtures.alternativa("a", listOf(9000, 4000, 10, 24, 1024), ordem = 0),
                Fixtures.alternativa("b", listOf(8000, 5000, 9, 20, 512), ordem = 1),
                Fixtures.alternativa("b", listOf(8000, 5000, 9, 20, 512), OrigemProduto.PESSOAL, ordem = 2).copy(alternativaId = "alt-b-pessoal"),
                Fixtures.alternativa("d", listOf(7000, 6000, 8, 16, 256), ordem = 3),
            ),
            d,
        ) as ResultadoCalculo.Sucesso
        val ranking = r.valor as ResultadoTopsis.Ranking
        assertEquals(1, ranking.detalhes.getValue("alt-a").posicao)
        assertEquals(2, ranking.detalhes.getValue("alt-b").posicao)
        assertEquals(2, ranking.detalhes.getValue("alt-b-pessoal").posicao)
        assertEquals(4, ranking.detalhes.getValue("alt-d").posicao)
    }

    @Test
    fun `mesma identidade de origem e bloqueada`() = runTest {
        val r = processar(listOf(Fixtures.alternativa("a", listOf(1, 1, 1, 1, 1)), Fixtures.alternativa("a", listOf(2, 2, 2, 2, 2)).copy(alternativaId = "x")), StandardTestDispatcher(testScheduler))
        assertTrue(r is ResultadoCalculo.Bloqueado)
    }

    @Test
    fun `valor ausente bloqueia e nao vira zero`() = runTest {
        val a = Fixtures.alternativa("a", listOf(9000, 4000, 10, 24, 1024))
        val semCamera = a.copy(valores = a.valores + (c[2].id to ValorAlternativa.copiaDoOriginal(null, a.valores.getValue(c[2].id).provenienciaOrigem)))
        val r = processar(listOf(semCamera, Fixtures.alternativa("b", listOf(1, 1, 1, 1, 1))), StandardTestDispatcher(testScheduler)) as ResultadoCalculo.Bloqueado
        assertEquals(c[2].id, r.problemas.single().criterioId)
    }

    @Test
    fun `nota fora de 1 a 10 bloqueia`() = runTest {
        val r = processar(listOf(Fixtures.alternativa("a", listOf(1, 1, 11, 1, 1)), Fixtures.alternativa("b", listOf(1, 1, 5, 1, 1))), StandardTestDispatcher(testScheduler))
        assertTrue(r is ResultadoCalculo.Bloqueado)
    }

    @Test
    fun `colisao monetaria na conversao bloqueia o processamento`() = runTest {
        val a = Fixtures.alternativa("a", listOf(1, 1, 5, 1, 1))
        val b = Fixtures.alternativa("b", listOf(2, 1, 5, 2, 1))
        fun comPreco(x: br.edu.ufu.pdm.mcdm.domain.model.Alternativa, centavos: Long) =
            x.copy(valores = x.valores + (c[1].id to ValorAlternativa.copiaDoOriginal(ValorCanonico.Monetario(centavos), x.valores.getValue(c[1].id).provenienciaOrigem)))
        val r = processar(listOf(comPreco(a, 9007199254740992L * 100), comPreco(b, 9007199254740992L * 100 + 1)), StandardTestDispatcher(testScheduler))
        assertTrue(r is ResultadoCalculo.Bloqueado)
        assertEquals(c[1].id, (r as ResultadoCalculo.Bloqueado).problemas.single().criterioId)
    }

    @Test
    fun `AHP inconsistente bloqueia TOPSIS`() = runTest {
        val d = StandardTestDispatcher(testScheduler)
        val ahp = (CalcularAhp(d)(c, Fixtures.julgamentosIguais()) as ResultadoCalculo.Sucesso).valor.copy(rc = 0.1000000001)
        val r = ProcessarAvaliacao(d)(c, listOf(Fixtures.alternativa("a", listOf(1, 1, 1, 1, 1)), Fixtures.alternativa("b", listOf(2, 2, 2, 2, 2))), ahp)
        assertTrue(r is ResultadoCalculo.Bloqueado)
    }

    @Test
    fun `alternativas iguais concluem sem diferenciacao`() = runTest {
        val r = processar(listOf(Fixtures.alternativa("a", listOf(8000, 5000, 8, 16, 256)), Fixtures.alternativa("b", listOf(8000, 5000, 8, 16, 256))), StandardTestDispatcher(testScheduler))
        assertTrue((r as ResultadoCalculo.Sucesso).valor is ResultadoTopsis.SemDiferenciacao)
    }

    @Test
    fun `edicao invalida resultados conforme a parte 4`() = runTest {
        val d = StandardTestDispatcher(testScheduler)
        val alts = listOf(Fixtures.alternativa("a", listOf(8000, 5000, 8, 16, 256)), Fixtures.alternativa("b", listOf(7000, 6000, 9, 20, 512), ordem = 1))
        var e = EdicaoAvaliacao.nova(Fixtures.avaliacao(alts))
        Fixtures.julgamentosIguais().values.forEach { e = e.comJulgamento(it) }
        val ahp = (CalcularAhp(d)(c, e.conteudo.julgamentos) as ResultadoCalculo.Sucesso).valor
        e = e.comAhp(ahp)
        val topsis = (ProcessarAvaliacao(d)(c, e.conteudo.alternativas, e.ahpVigente) as ResultadoCalculo.Sucesso).valor
        e = e.comTopsis(topsis)
        assertEquals(EstadoSalvo.CONCLUIDA_RANKING, e.conteudoSalvavel().estado)

        // Alterar valor desatualiza TOPSIS e preserva AHP.
        val alterada = e.comValor("alt-a", c[1].id, ValorAlternativa(ValorCanonico.Monetario(500000), e.conteudo.alternativas[0].valores.getValue(c[1].id).provenienciaOrigem, ValorCanonico.Monetario(480000), br.edu.ufu.pdm.mcdm.domain.model.Proveniencia.AJUSTE))
        assertEquals(Vigencia.DESATUALIZADO, alterada.vigenciaTopsis)
        assertEquals(Vigencia.VIGENTE, alterada.vigenciaAhp)
        assertNull(alterada.conteudoSalvavel().topsis)
        assertEquals(EstadoSalvo.RASCUNHO, alterada.conteudoSalvavel().estado)

        // Alterar julgamento desatualiza ambos.
        val j = Julgamento.deResposta(c[0].id, c[1].id, c[0].id, 3)
        val julg = e.comJulgamento(j)
        assertEquals(Vigencia.DESATUALIZADO, julg.vigenciaAhp)
        assertNull(julg.conteudoSalvavel().ahp)

        // Registrar a mesma resposta não muda nada.
        val mesma = e.comJulgamento(e.conteudo.julgamentos.values.first())
        assertEquals(e, mesma)
    }

    @Test
    fun `abrir versao salva nao gera pendencia e resultado novo gera`() = runTest {
        val d = StandardTestDispatcher(testScheduler)
        val alts = listOf(Fixtures.alternativa("a", listOf(8000, 5000, 8, 16, 256)), Fixtures.alternativa("b", listOf(7000, 6000, 9, 20, 512), ordem = 1))
        val base = Fixtures.avaliacao(alts).copy(julgamentos = Fixtures.julgamentosIguais(), salvaEm = Instant.EPOCH)
        val aberta = EdicaoAvaliacao.deSalva(base)
        assertFalse(aberta.pendente(base))
        val ahp = (CalcularAhp(d)(c, base.julgamentos) as ResultadoCalculo.Sucesso).valor
        assertTrue(aberta.comAhp(ahp).pendente(base))
    }

    @Test
    fun `salvar valida o agregado e grava, falha preserva`() = runTest {
        val repo = RepoFalso()
        val relogio = Clock.fixed(Instant.parse("2026-11-13T12:00:00Z"), ZoneOffset.UTC)
        val salvar = SalvarAvaliacao(repo, relogio)
        val e = EdicaoAvaliacao.nova(Fixtures.avaliacao(listOf(Fixtures.alternativa("a", listOf(1, 1, 1, 1, 1)))))
        val ok = salvar(e) as ResultadoOperacao.Sucesso
        assertEquals(Instant.parse("2026-11-13T12:00:00Z"), ok.valor.salvaEm)
        assertEquals(EstadoSalvo.RASCUNHO, repo.salvas.single().estado)
        repo.falhar = true
        assertTrue(salvar(e) is ResultadoOperacao.Erro)
        assertEquals(1, repo.salvas.size)
    }

    @Test
    fun `validador rejeita TOPSIS sem AHP e detalhe faltante`() {
        val alts = listOf(Fixtures.alternativa("a", listOf(1, 1, 1, 1, 1)), Fixtures.alternativa("b", listOf(2, 2, 2, 2, 2), ordem = 1))
        val inval = Fixtures.avaliacao(alts).copy(topsis = ResultadoTopsis.Ranking(emptyMap()))
        val p = ValidadorAgregado.problemas(inval)
        assertTrue(p.any { "AHP" in it })
        assertTrue(p.any { "faltante" in it })
    }

    private class RepoFalso : AvaliacaoRepository {
        val salvas = mutableListOf<Avaliacao>()
        var falhar = false
        override fun observarResumos(): Flow<List<ResumoAvaliacao>> = emptyFlow()
        override suspend fun obter(id: String) = salvas.lastOrNull { it.id == id }
        override suspend fun salvar(avaliacao: Avaliacao) {
            if (falhar) error("disco")
            salvas += avaliacao
        }
        override suspend fun excluir(id: String) { salvas.removeAll { it.id == id } }
    }
}
