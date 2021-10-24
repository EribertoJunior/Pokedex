package com.eriberto.pokedex.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.eriberto.pokedex.repository.database.model.EntidadePokemon
import com.eriberto.pokedex.repository.database.model.PokemonFavorito
import com.eriberto.pokedex.repository.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.repository.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepo {
    suspend fun buscarDetalhesDoPokemon(
        idPokemon: Int,
        success: (data: RetornoPokemonDetalhe) -> Unit,
        erro: (errorMessage: String) -> Unit
    )

    suspend fun favoritarPokemon(pokemon: EntidadePokemon)

    suspend fun favoritarPokemon(idPokemon: Int, nomePokemon: String)

    suspend fun desfavoritarPokemon(pokemon: EntidadePokemon)

    suspend fun desfavoritarPokemon(idPokemon: Int, nomePokemon: String)

    fun buscaPokemonFavoritoPorId(idPokemon: Int): LiveData<PokemonFavorito?>

    fun getPokemonStream(): Flow<PagingData<Pokemon>>

    fun getPokemonStreamDB(): Flow<PagingData<EntidadePokemon>>
}

