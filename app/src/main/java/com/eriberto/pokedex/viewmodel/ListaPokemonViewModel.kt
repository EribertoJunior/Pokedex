package com.eriberto.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eriberto.pokedex.repository.PokemonRepo
import com.eriberto.pokedex.repository.model.PokemonData
import kotlinx.coroutines.flow.Flow

class ListaPokemonViewModel(private val pokemonRepo: PokemonRepo) : ViewModel() {

    fun getPokemonStream(): Flow<PagingData<PokemonData>> {
        return pokemonRepo.getPokemonStream().cachedIn(viewModelScope)
    }

    companion object {
        const val PAGE_SIZE = 10
        const val PREFETCH_SIZE = 50
        const val MAX_SIZE = PAGE_SIZE + PREFETCH_SIZE * 3
    }
}