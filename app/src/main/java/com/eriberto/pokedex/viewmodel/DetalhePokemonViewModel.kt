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

    private val _detalhePokemonData = MutableLiveData<PokeDetalheData>()

    val detalhePokemonData: LiveData<PokeDetalheData> = _detalhePokemonData

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

    inner class PokeDetalheData(
        var pokeDetalhe: PokeDetalhe? = null,
        var errorMessage: String? = null,
        var statusResult: STATUS_RESULT
    )

}