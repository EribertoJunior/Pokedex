package com.eriberto.pokedex.repository.model

data class PokemonList(val next: String?, val results: List<PokemonData>)

data class PokemonData(val name: String, val url: String)