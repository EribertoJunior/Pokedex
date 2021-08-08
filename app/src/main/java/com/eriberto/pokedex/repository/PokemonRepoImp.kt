package com.eriberto.pokedex.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eriberto.pokedex.repository.database.config.PokemonDatabase
import com.eriberto.pokedex.repository.database.model.EntidadePokemon
import com.eriberto.pokedex.repository.database.model.PokemonFavorito
import com.eriberto.pokedex.repository.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.repository.model.Pokemon
import com.eriberto.pokedex.repository.network.PokeService
import com.eriberto.pokedex.repository.pagingSource.PokemonPagingSource
import com.eriberto.pokedex.viewmodel.ListaPokemonViewModel
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class PokemonRepoImp(
    private val pokeService: PokeService,
    private val pokemonDAO: PokemonDAO
) : PokemonRepo {

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

    override suspend fun favoritarPokemon(pokemonData: PokemonData): Boolean {
        val pokemonFavorito = getFavoritePokemon(pokemonData.name).value
        val idPokemonLocal: Long = pokemonFavorito?.id ?: 0
        val pokemonLocal = map(idPokemon = idPokemonLocal, pokemonData = pokemonData)
        pokemonDAO.salva(pokemonLocal)
        return true
    }

    override suspend fun desfavoritarPokemon(pokemonData: PokemonData): Boolean {
        val pokemonFavorito = getFavoritePokemon(pokemonData.name).value
        val idPokemonLocal: Long = pokemonFavorito?.id ?: 0
        pokemonDAO.deletar(
            map(idPokemon = idPokemonLocal, pokemonData = pokemonData)
        )
        return true
    }

    override fun isFavoritePokemon(namePokemon: String): LiveData<PokemonLocal?> {
        return getFavoritePokemon(namePokemon)
    }

    override fun getPokemonStream(): Flow<PagingData<PokemonData>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_SIZE,
                maxSize = MAX_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PokemonPagingSource(pokeService) }).flow
    }

    private fun getFavoritePokemon(namePokemon: String): LiveData<PokemonLocal?> {
        return pokemonDAO.verificarPokemonLocal(namePokemon)
    }

    private fun map(idPokemon: Long, pokemonData: PokemonData): PokemonLocal {
        return PokemonLocal(id = idPokemon, name = pokemonData.name, url = pokemonData.url)
    }

}