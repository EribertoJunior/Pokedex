package com.eriberto.pokedex.data.remote

import androidx.paging.PagingData
import com.eriberto.pokedex.data.remote.network.model.Pokemon
import com.eriberto.pokedex.data.remote.network.model.RetornoPokemonDetalhe
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun buscarDetalhesDoPokemon(
        idPokemon: Int,
        success: (data: RetornoPokemonDetalhe) -> Unit,
        erro: (errorMessage: String) -> Unit
    )

    fun getPokemonStream(): Flow<PagingData<Pokemon>>
}