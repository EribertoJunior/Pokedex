package com.eriberto.pokedex.viewmodel.detalhe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eriberto.pokedex.repository.DetalhePokemonRepo
import com.eriberto.pokedex.repository.model.PokeDetalhe
import com.eriberto.pokedex.repository.model.PokemonData
import com.eriberto.pokedex.repository.network.STATUS_RESULT
import com.eriberto.pokedex.util.Result
import com.eriberto.pokedex.viewmodel.detalhe.model.PokeDetalheData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetalhePokemonViewModel(private val detalhePokemonRepo: DetalhePokemonRepo) : ViewModel() {

    private val _detalhePokemonData = MutableLiveData<PokeDetalheData>()
    val detalhePokemonData: LiveData<PokeDetalheData> = _detalhePokemonData

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavoriteData: LiveData<Boolean> = _isFavorite

    fun getDetalhesPokemon(idPokemon: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            detalhePokemonRepo.getDetalhePokemon(
                idPokemon = idPokemon,
                result = object : Result<PokeDetalhe> {
                    override fun success(data: PokeDetalhe) {
                        _detalhePokemonData.postValue(
                            PokeDetalheData(
                                pokeDetalhe = data,
                                statusResult = STATUS_RESULT.Success
                            )
                        )
                    }

                    override fun error(errorMessage: String) {
                        _detalhePokemonData.postValue(
                            PokeDetalheData(
                                errorMessage = errorMessage,
                                statusResult = STATUS_RESULT.Error
                            )
                        )
                    }
                })
        }
    }

    fun favoritarPokemon(pokemonData: PokemonData) {
        viewModelScope.launch(Dispatchers.IO) {
            detalhePokemonRepo.favoritarPokemon(pokemonData)
            isFavorite(pokemonData.name)
        }
    }

    fun desfavoritarPokemon(pokemonData: PokemonData) {
        viewModelScope.launch(Dispatchers.IO) {
            detalhePokemonRepo.desfavoritarPokemon(pokemonData)
            isFavorite(pokemonData.name)
        }
    }

    fun isFavorite(namePokemon: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val isFavorite = detalhePokemonRepo.isFavoritePokemon(namePokemon)
            _isFavorite.postValue(isFavorite)
        }
    }
}