package com.eriberto.pokedex.network

import com.eriberto.pokedex.model.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {

    @GET("pokemon")
    fun getListPokemon(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int
    ): PokemonList

    @GET("pokemon/{idPokemon}")
    fun getDetalhePokemon(@Path("idPokemon") idPokemon: Int)
}