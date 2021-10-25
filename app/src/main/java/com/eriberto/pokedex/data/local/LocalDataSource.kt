package com.eriberto.pokedex.data.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.eriberto.pokedex.data.local.database.model.EntidadePokemon
import com.eriberto.pokedex.data.local.database.model.PokemonFavorito
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getEntidadePokemonPorNome(nomePokemon: String): EntidadePokemon?

    fun deletaPokemonFavorito(pokemonFavorito: PokemonFavorito)

    fun salvaPokemonFavorito(pokemonFavorito: PokemonFavorito)

    fun salvaEntidadePokemon(pokemon: EntidadePokemon)

    fun buscarPaginadaPokemon(): PagingSource<Int, EntidadePokemon>

    fun buscaPokemonFavoritoPorId(idPokemon: Int): LiveData<PokemonFavorito?>

    fun getPokemonStreamDB(): Flow<PagingData<EntidadePokemon>>
}