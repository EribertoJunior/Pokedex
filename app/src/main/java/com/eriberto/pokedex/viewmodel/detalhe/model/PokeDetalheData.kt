package com.eriberto.pokedex.viewmodel.detalhe.model

import com.eriberto.pokedex.repository.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.repository.network.STATUS_RESULT

data class PokeDetalheData(
    var retornoPokemonDetalhe: RetornoPokemonDetalhe? = null,
    var errorMessage: String? = null,
    var statusResult: STATUS_RESULT
)
