package com.eriberto.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eriberto.pokedex.repository.model.PokemonData
import com.eriberto.pokedex.repository.pagingSource.PokemonPagingSource
import kotlinx.coroutines.flow.Flow

class MainActivityViewModel(private val pokemonPagingSource: PokemonPagingSource) : ViewModel() {

    fun getListPokemon(): Flow<PagingData<PokemonData>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 50),
            pagingSourceFactory = { pokemonPagingSource }).flow.cachedIn(viewModelScope)
    }
}