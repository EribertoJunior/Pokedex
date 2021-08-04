package com.eriberto.pokedex.repository

import androidx.paging.PagingData
import com.eriberto.pokedex.repository.model.PokeDetalhe
import com.eriberto.pokedex.repository.model.PokemonData
import kotlinx.coroutines.flow.Flow

interface PokemonRepo {
    suspend fun getDetalhePokemon(
        idPokemon: Int,
        success: (data: PokeDetalhe) -> Unit,
        erro: (errorMessage: String) -> Unit
    )

    suspend fun favoritarPokemon(pokemonData: PokemonData): Boolean

    suspend fun desfavoritarPokemon(pokemonData: PokemonData): Boolean

    suspend fun isFavoritePokemon(namePokemon: String): Boolean

    fun getPokemonStream(): Flow<PagingData<PokemonData>>
}
