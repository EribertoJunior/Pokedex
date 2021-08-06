package com.eriberto.pokedex.viewmodel.detalhe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eriberto.pokedex.repository.PokemonRepo
import com.eriberto.pokedex.repository.database.model.PokemonLocal
import com.eriberto.pokedex.repository.model.PokemonData
import com.eriberto.pokedex.repository.network.STATUS_RESULT
import com.eriberto.pokedex.viewmodel.detalhe.model.PokeDetalheData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetalhePokemonViewModel(private val pokemonRepo: PokemonRepo) : ViewModel() {

    private val _detalhePokemonData = MutableLiveData<PokeDetalheData>()
    val detalhePokemonData: LiveData<PokeDetalheData> = _detalhePokemonData

    fun getDetalhesPokemon(idPokemon: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonRepo.getDetalhePokemon(
                idPokemon = idPokemon,
                success = { detalhesDoPokemon ->
                    _detalhePokemonData.postValue(
                        PokeDetalheData(
                            pokeDetalhe = detalhesDoPokemon,
                            statusResult = STATUS_RESULT.Success
                        )
                    )
                },
                erro = { errorMessage ->
                    _detalhePokemonData.postValue(
                        PokeDetalheData(
                            errorMessage = errorMessage,
                            statusResult = STATUS_RESULT.Error
                        )
                    )
                }
            )
        }
    }

    fun favoritarPokemon(pokemonData: PokemonData) {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonRepo.favoritarPokemon(pokemonData)
        }
    }

    fun desfavoritarPokemon(pokemonData: PokemonData) {
        viewModelScope.launch(Dispatchers.IO) {
            pokemonRepo.desfavoritarPokemon(pokemonData)
        }
    }

    fun isFavorite(namePokemon: String): LiveData<PokemonLocal?> {
        return pokemonRepo.isFavoritePokemon(namePokemon)
    }
}