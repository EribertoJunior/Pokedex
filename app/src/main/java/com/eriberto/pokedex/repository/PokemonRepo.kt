package com.eriberto.pokedex.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.eriberto.pokedex.repository.database.model.PokemonLocal
import com.eriberto.pokedex.repository.model.PokeDetalhe
import com.eriberto.pokedex.repository.model.PokemonData
import kotlinx.coroutines.flow.Flow

interface PokemonRepo {
    suspend fun buscarDetalhesDoPokemon(
        idPokemon: Int,
        success: (data: PokeDetalhe) -> Unit,
        erro: (errorMessage: String) -> Unit
    )

    suspend fun favoritarPokemon(pokemonData: PokemonData): Boolean

    suspend fun desfavoritarPokemon(pokemonData: PokemonData): Boolean

    fun isFavoritePokemon(namePokemon: String): LiveData<PokemonLocal?>

    fun getPokemonStream(): Flow<PagingData<PokemonData>>
}

