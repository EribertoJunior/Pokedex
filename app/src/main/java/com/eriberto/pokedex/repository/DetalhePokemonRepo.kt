package com.eriberto.pokedex.repository

import com.eriberto.pokedex.repository.model.PokeDetalhe
import com.eriberto.pokedex.repository.model.PokemonData
import com.eriberto.pokedex.util.Result

interface DetalhePokemonRepo {
    suspend fun getDetalhePokemon(idPokemon: Int, result: Result<PokeDetalhe>)

    suspend fun favoritarPokemon(pokemonData: PokemonData):Boolean

    suspend fun desfavoritarPokemon(pokemonData: PokemonData):Boolean

    suspend fun isFavoritePokemon(namePokemon: String):Boolean
}

