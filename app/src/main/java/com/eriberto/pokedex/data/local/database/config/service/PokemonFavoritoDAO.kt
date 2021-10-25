package com.eriberto.pokedex.data.local.database.config.service

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eriberto.pokedex.data.local.database.model.PokemonFavorito

@Dao
interface PokemonFavoritoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salvar(pokemonFavorito: PokemonFavorito)

    @Delete
    fun deletar(pokemonFavorito: PokemonFavorito)

    @Query("SELECT * FROM pokemon_favorito WHERE idPokemon = :idPokemon")
    fun buscaPokemonFavoritoPorId(idPokemon: Int): LiveData<PokemonFavorito?>

    @Query("SELECT * FROM pokemon_favorito WHERE idPokemon = :idPokemon")
    fun buscarFavorito(idPokemon: Int): PokemonFavorito?
}