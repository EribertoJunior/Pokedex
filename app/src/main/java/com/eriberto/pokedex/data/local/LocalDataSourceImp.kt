package com.eriberto.pokedex.data.local

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.eriberto.pokedex.data.PokemonRemoteMediator
import com.eriberto.pokedex.data.local.database.config.service.PokemonDAO
import com.eriberto.pokedex.data.local.database.config.service.PokemonFavoritoDAO
import com.eriberto.pokedex.data.local.database.model.EntidadePokemon
import com.eriberto.pokedex.data.local.database.model.PokemonFavorito
import kotlinx.coroutines.flow.Flow

@ExperimentalPagingApi
class LocalDataSourceImp(
    private val pokemonDAO: PokemonDAO,
    private val pokemonFavoritoDAO: PokemonFavoritoDAO,
    private val pokemonRemoteMediator: PokemonRemoteMediator
) : LocalDataSource {
    override fun getEntidadePokemonPorNome(nomePokemon: String): EntidadePokemon? {
        return pokemonDAO.getEntidadePokemon(nomePokemon)
    }

    override fun deletaPokemonFavorito(pokemonFavorito: PokemonFavorito) {
        pokemonFavoritoDAO.deletar(pokemonFavorito)
    }

    override fun salvaPokemonFavorito(pokemonFavorito: PokemonFavorito) {
        pokemonFavoritoDAO.salvar(pokemonFavorito)
    }

    override fun salvaEntidadePokemon(pokemon: EntidadePokemon) {
        pokemonDAO.salva(pokemon)
    }

    override fun buscarPaginadaPokemon(): PagingSource<Int, EntidadePokemon> {
        return pokemonDAO.buscarPaginadaPokemon()
    }

    override fun buscaPokemonFavoritoPorId(idPokemon: Int): LiveData<PokemonFavorito?> {
        return pokemonFavoritoDAO.buscaPokemonFavoritoPorId(idPokemon)
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

    private companion object {
        const val PAGE_SIZE = 10
        const val PREFETCH_SIZE = 50
        const val MAX_SIZE = PAGE_SIZE + PREFETCH_SIZE * 3
    }
}