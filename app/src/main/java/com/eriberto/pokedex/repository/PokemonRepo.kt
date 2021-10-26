package com.eriberto.pokedex.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.eriberto.pokedex.data.local.database.model.EntidadePokemon
import com.eriberto.pokedex.data.local.database.model.PokemonFavorito
import com.eriberto.pokedex.data.remote.network.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.data.remote.network.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface PokemonRepo {
    suspend fun buscarDetalhesDoPokemon(
        idPokemon: Int,
        success: (data: RetornoPokemonDetalhe) -> Unit,
        erro: (errorMessage: String) -> Unit
    )

    suspend fun favoritarPokemon(idPokemon: Int, nomePokemon: String)

    suspend fun desfavoritarPokemon(idPokemon: Int, nomePokemon: String)

    fun isFavorite(idPokemon: Int): LiveData<Boolean>

    fun getPokemonStream(): Flow<PagingData<Pokemon>>

    fun getPokemonStreamDB(): Flow<PagingData<EntidadePokemon>>
}

