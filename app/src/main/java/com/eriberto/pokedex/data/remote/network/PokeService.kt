package com.eriberto.pokedex.data.remote.network

import com.eriberto.pokedex.data.remote.network.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.data.remote.network.model.RetornoPokemon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {

    @GET("pokemon")
    suspend fun getListPokemon(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int
    ): RetornoPokemon

    @GET("pokemon/{idPokemon}")
    fun getDetalhePokemon(@Path("idPokemon") idPokemon: Int) : Call<RetornoPokemonDetalhe>
}