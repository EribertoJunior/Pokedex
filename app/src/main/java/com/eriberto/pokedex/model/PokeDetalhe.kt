package com.eriberto.pokedex.model

data class PokeDetalhe(
    val name: String?,
    val types: List<TypeSlot>,
    val weight: Int,
    val height: Int,
    val abilities: List<AbilitySlot>,
    val game_indices: List<Game>
)

data class TypeSlot(val slot: Int, val type: Type)

data class Type(val name: String?)

data class AbilitySlot(val ability: Ability)

data class Ability(val name: String?)

data class Game(val game_index: Int, val version: Versao)

data class Versao(val name: String?)
