package com.eriberto.pokedex.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.eriberto.pokedex.repository.database.config.service.PokemonDAO
import com.eriberto.pokedex.repository.database.config.service.PokemonFavoritoDAO
import com.eriberto.pokedex.repository.database.model.EntidadePokemon
import com.eriberto.pokedex.repository.database.model.PokemonFavorito
import com.eriberto.pokedex.repository.model.Pokemon
import com.eriberto.pokedex.repository.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.repository.network.PokeService
import com.eriberto.pokedex.repository.pagingSource.PokemonPagingSource
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

@ExperimentalPagingApi
class PokemonRepoImp @ExperimentalPagingApi constructor(
    private val pokeService: PokeService,
    private val pokemonDAO: PokemonDAO,
    private val pokemonFavoritoDAO: PokemonFavoritoDAO,
    private val pokemonRemoteMediator: PokemonRemoteMediator
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

    override suspend fun favoritarPokemon(idPokemon: Int, nomePokemon: String) {
        val pokemonFavorito = PokemonFavorito(idPokemon = idPokemon, nomePokemon = nomePokemon)
        val entidadePokemon = getEntidadePokemon(nomePokemon)

        salvaPokemonFavorito(pokemonFavorito)
        entidadePokemon?.let { pokemon ->
            pokemon.favorito = true
            salvaEntidadePokemon(pokemon)
        }
    }

    override suspend fun desfavoritarPokemon(idPokemon: Int, nomePokemon: String) {
        val pokemonFavorito = PokemonFavorito(idPokemon = idPokemon, nomePokemon = nomePokemon)
        val entidadePokemon = getEntidadePokemon(nomePokemon)

        deletaPokemonFavorito(pokemonFavorito)
        entidadePokemon?.let { pokemon ->
            pokemon.favorito = false
            salvaEntidadePokemon(pokemon)
        }
    }

    private fun getEntidadePokemon(nomePokemon: String) = pokemonDAO.getEntidadePokemon(nomePokemon)

    fun deletaPokemonFavorito(pokemonFavorito: PokemonFavorito) = pokemonFavoritoDAO.deletar(pokemonFavorito)

    fun salvaPokemonFavorito(pokemonFavorito: PokemonFavorito) = pokemonFavoritoDAO.salvar(pokemonFavorito)

    fun salvaEntidadePokemon(pokemon: EntidadePokemon) = pokemonDAO.salva(pokemon)

    override fun buscaPokemonFavoritoPorId(idPokemon: Int): LiveData<PokemonFavorito?> {
        return getFavoritePokemon(idPokemon)
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

    override fun getPokemonStreamDB(): Flow<PagingData<EntidadePokemon>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_SIZE,
                maxSize = MAX_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = pokemonRemoteMediator,
            pagingSourceFactory = { pokemonDAO.buscarPaginadaPokemon() }).flow
    }

    private fun getFavoritePokemon(idPokemon: Int): LiveData<PokemonFavorito?> {
        return pokemonFavoritoDAO.verificaFavorito(idPokemon)
    }

    companion object {
        const val PAGE_SIZE = 10
        const val PREFETCH_SIZE = 50
        const val MAX_SIZE = PAGE_SIZE + PREFETCH_SIZE * 3
    }

}