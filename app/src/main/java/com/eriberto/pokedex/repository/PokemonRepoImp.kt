package com.eriberto.pokedex.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.eriberto.pokedex.data.local.database.model.EntidadePokemon
import com.eriberto.pokedex.data.local.database.model.PokemonFavorito
import com.eriberto.pokedex.data.remote.network.model.Pokemon
import com.eriberto.pokedex.data.remote.network.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.data.local.LocalDataSource
import com.eriberto.pokedex.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class PokemonRepoImp (
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : PokemonRepo {

    override suspend fun buscarDetalhesDoPokemon(
        idPokemon: Int,
        success: (data: RetornoPokemonDetalhe) -> Unit,
        erro: (errorMessage: String) -> Unit
    ) {
        remoteDataSource.buscarDetalhesDoPokemon(idPokemon, success, erro)
    }

    override suspend fun favoritarPokemon(idPokemon: Int, nomePokemon: String) {

        val pokemonFavorito = PokemonFavorito(idPokemon = idPokemon, nomePokemon = nomePokemon)
        localDataSource.salvaPokemonFavorito(pokemonFavorito)

        val entidadePokemon = localDataSource.getEntidadePokemonPorNome(nomePokemon)
        entidadePokemon?.let { pokemon ->
            pokemon.favorito = true
            localDataSource.salvaEntidadePokemon(pokemon)
        }
    }

    override suspend fun desfavoritarPokemon(idPokemon: Int, nomePokemon: String) {
        val pokemonFavorito = PokemonFavorito(idPokemon = idPokemon, nomePokemon = nomePokemon)
        val entidadePokemon = localDataSource.getEntidadePokemonPorNome(nomePokemon)

        localDataSource.deletaPokemonFavorito(pokemonFavorito)
        entidadePokemon?.let { pokemon ->
            pokemon.favorito = false
            localDataSource.salvaEntidadePokemon(pokemon)
        }
    }

    override fun isFavorite(idPokemon: Int): LiveData<Boolean> {
        val isFavoriteLiveData = MutableLiveData<Boolean>().apply { value = false }
        val favoritePokemonReturned = localDataSource.buscaPokemonFavoritoPorId(idPokemon)

        favoritePokemonReturned.observeForever {
            isFavoriteLiveData.value = it != null
        }
        return isFavoriteLiveData
    }

    override fun getPokemonStream(): Flow<PagingData<Pokemon>> {
        return remoteDataSource.getPokemonStream()
    }

    override fun getPokemonStreamDB(): Flow<PagingData<EntidadePokemon>> {
        return localDataSource.getPokemonStreamDB()
    }
}