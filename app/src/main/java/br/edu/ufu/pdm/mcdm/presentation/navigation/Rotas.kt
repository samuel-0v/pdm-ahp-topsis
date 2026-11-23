package br.edu.ufu.pdm.mcdm.presentation.navigation

import kotlinx.serialization.Serializable

// Destinos tipados do Navigation Compose (DEC-047). IDs de tela da Etapa 5 nos comentários.

@Serializable data object RotaCatalogo // T01
@Serializable data object RotaAvaliacoes // T05
@Serializable data class RotaProdutos(val categoriaId: String) // T02
@Serializable data class RotaDetalheProduto(val categoriaId: String, val origem: String, val produtoId: String) // T03
@Serializable data class RotaFormularioPessoal(val categoriaId: String, val produtoId: String? = null) // T04

/** Grafo aninhado da sessão de avaliação: dono do AvaliacaoViewModel (DEC-045). */
@Serializable data class RotaAvaliacao(val avaliacaoId: String? = null, val categoriaId: String? = null)

@Serializable data object PassoCarregar
@Serializable data object PassoSelecao // T06
@Serializable data object PassoValores // T07
@Serializable data object PassoAhp // T08
@Serializable data object PassoPesos // T09
@Serializable data object PassoRevisao // T10/T11
@Serializable data object PassoResultado // T12
@Serializable data object PassoDetalhes // T12-DETALHES
