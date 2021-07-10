package com.eriberto.pokedex.repository

import com.eriberto.pokedex.repository.model.PokeDetalhe
import com.eriberto.pokedex.repository.network.PokeService
import com.eriberto.pokedex.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class DetalhePokemonRepoImp(private val pokeService: PokeService) : DetalhePokemonRepo {
    override suspend fun getDetalhePokemon(idPokemon: Int, result: Result<PokeDetalhe>) {
        val call = pokeService.getDetalhePokemon(idPokemon)
        call.enqueue(object : Callback<PokeDetalhe> {
            override fun onResponse(call: Call<PokeDetalhe>, response: Response<PokeDetalhe>) {
                if(response.isSuccessful){
                    response.body()?.let { result.success(data = it) }
                }else
                    result.error(HttpException(response).message())
            }

            override fun onFailure(call: Call<PokeDetalhe>, t: Throwable) {
                t.message?.let { result.error(errorMessage = it) }
            }
        })
    }

}