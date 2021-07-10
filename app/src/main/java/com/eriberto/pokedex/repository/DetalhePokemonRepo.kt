package com.eriberto.pokedex.repository

import com.eriberto.pokedex.repository.model.PokeDetalhe
import com.eriberto.pokedex.util.Result

interface DetalhePokemonRepo {
    suspend fun getDetalhePokemon(idPokemon: Int, result: Result<PokeDetalhe>)
}

