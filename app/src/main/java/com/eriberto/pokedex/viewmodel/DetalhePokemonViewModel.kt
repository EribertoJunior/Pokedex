package com.eriberto.pokedex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eriberto.pokedex.repository.DetalhePokemonRepo
import com.eriberto.pokedex.repository.model.PokeDetalhe
import com.eriberto.pokedex.repository.network.STATUS_RESULT
import com.eriberto.pokedex.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetalhePokemonViewModel(private val detalhePokemonRepo: DetalhePokemonRepo) : ViewModel() {

    private val detalhePokemonData = MutableLiveData<PokeDetalheData>()

    fun getDetalhesPokemon(idPokemon: Int): LiveData<PokeDetalheData> {

        viewModelScope.launch(Dispatchers.IO) {
            detalhePokemonRepo.getDetalhePokemon(
                idPokemon = idPokemon,
                result = object : Result<PokeDetalhe> {
                    override fun success(data: PokeDetalhe) {
                        detalhePokemonData.postValue(PokeDetalheData(pokeDetalhe = data, statusResult = STATUS_RESULT.Success))
                    }

                    override fun error(errorMessage: String) {
                        detalhePokemonData.postValue(PokeDetalheData(errorMessage = errorMessage, statusResult = STATUS_RESULT.Error))
                    }
                })
        }

        return detalhePokemonData
    }

    inner class PokeDetalheData(
        var pokeDetalhe: PokeDetalhe? = null,
        var errorMessage: String? = null,
        var statusResult: STATUS_RESULT
    )

}