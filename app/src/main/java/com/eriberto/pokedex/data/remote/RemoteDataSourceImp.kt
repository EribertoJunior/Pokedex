package com.eriberto.pokedex.data.remote

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eriberto.pokedex.data.remote.network.model.Pokemon
import com.eriberto.pokedex.data.remote.network.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.data.remote.network.PokeService
import com.eriberto.pokedex.data.PokemonPagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class RemoteDataSourceImp(private val pokeService: PokeService) : RemoteDataSource {

    override suspend fun buscarDetalhesDoPokemon(
        idPokemon: Int,
        success: (data: RetornoPokemonDetalhe) -> Unit,
        erro: (errorMessage: String) -> Unit
    ) {
        val call = pokeService.getDetalhePokemon(idPokemon)
        call.enqueue(object : Callback<RetornoPokemonDetalhe> {
            override fun onResponse(
                call: Call<RetornoPokemonDetalhe>,
                response: Response<RetornoPokemonDetalhe>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { success(it) }
                } else
                    erro(HttpException(response).message())
            }

            override fun onFailure(call: Call<RetornoPokemonDetalhe>, t: Throwable) {
                t.message?.let { erro(it) }
            }
        })
    }

    override fun getPokemonStream(): Flow<PagingData<Pokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_SIZE,
                maxSize = MAX_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PokemonPagingSource(pokeService) }).flow
    }

    private companion object {
        const val PAGE_SIZE = 10
        const val PREFETCH_SIZE = 50
        const val MAX_SIZE = PAGE_SIZE + PREFETCH_SIZE * 3
    }
}