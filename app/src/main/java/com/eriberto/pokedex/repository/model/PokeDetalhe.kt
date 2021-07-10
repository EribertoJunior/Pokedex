package com.eriberto.pokedex.repository.model

data class PokeDetalhe(
    val name: String?,
    val types: List<TypeSlot>,
    val weight: Double,
    val height: Double,
    val abilities: List<AbilitySlot>,
    val game_indices: List<Game>
)

