package com.eriberto.pokedex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.eriberto.pokedex.repository.PokemonRepo
import com.eriberto.pokedex.repository.database.model.EntidadePokemon
import com.eriberto.pokedex.repository.model.Pokemon
import kotlinx.coroutines.flow.Flow

class ListaPokemonViewModel(private val pokemonRepo: PokemonRepo) : ViewModel() {

    fun getPokemonStream(): Flow<PagingData<PokemonData>> {
        return pokemonRepo.getPokemonStream().cachedIn(viewModelScope)
    }

}