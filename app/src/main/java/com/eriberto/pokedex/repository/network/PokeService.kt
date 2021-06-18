package com.eriberto.pokedex.repository.network

import com.eriberto.pokedex.repository.model.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeService {

    @GET("pokemon")
    suspend fun getListPokemon(
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int
    ): PokemonList

    @GET("pokemon/{idPokemon}")
    suspend fun getDetalhePokemon(@Path("idPokemon") idPokemon: Int)
}