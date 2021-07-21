package com.eriberto.pokedex.repository

import com.eriberto.pokedex.repository.database.config.service.PokemonDAO
import com.eriberto.pokedex.repository.database.model.PokemonLocal
import com.eriberto.pokedex.repository.model.PokeDetalhe
import com.eriberto.pokedex.repository.model.PokemonData
import com.eriberto.pokedex.repository.network.PokeService
import com.eriberto.pokedex.util.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class DetalhePokemonRepoImp(
    private val pokeService: PokeService,
    private val pokemonDAO: PokemonDAO
) : DetalhePokemonRepo {

    override suspend fun getDetalhePokemon(idPokemon: Int, result: Result<PokeDetalhe>) {
        val call = pokeService.getDetalhePokemon(idPokemon)
        call.enqueue(object : Callback<PokeDetalhe> {
            override fun onResponse(call: Call<PokeDetalhe>, response: Response<PokeDetalhe>) {
                if (response.isSuccessful) {
                    response.body()?.let { result.success(data = it) }
                } else
                    result.error(HttpException(response).message())
            }

            override fun onFailure(call: Call<PokeDetalhe>, t: Throwable) {
                t.message?.let { result.error(errorMessage = it) }
            }
        })
    }

    override suspend fun favoritarPokemon(pokemonData: PokemonData): Boolean {
        val pokemonFavorito = getFavoritePokemon(pokemonData.name)
        val idPokemonLocal: Long = pokemonFavorito?.id ?: 0
        val pokemonLocal = map(idPokemon = idPokemonLocal, pokemonData = pokemonData)
        pokemonDAO.salva(pokemonLocal)
        return true
    }

    override suspend fun desfavoritarPokemon(pokemonData: PokemonData): Boolean {
        val pokemonFavorito = getFavoritePokemon(pokemonData.name)
        val idPokemonLocal: Long = pokemonFavorito?.id ?: 0
        pokemonDAO.deletar(
            map(idPokemon = idPokemonLocal, pokemonData = pokemonData)
        )
        return true
    }

    override suspend fun isFavoritePokemon(namePokemon: String): Boolean {
        return getFavoritePokemon(namePokemon) != null
    }

    private fun getFavoritePokemon(namePokemon: String): PokemonLocal? {
        return pokemonDAO.verificarPokemonLocal(namePokemon)
    }

    private fun map(idPokemon: Long, pokemonData: PokemonData): PokemonLocal {
        return PokemonLocal(id = idPokemon, name = pokemonData.name, url = pokemonData.url)
    }

}