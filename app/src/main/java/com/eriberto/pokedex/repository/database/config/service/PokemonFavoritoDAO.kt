package com.eriberto.pokedex.repository.database.config.service

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eriberto.pokedex.repository.database.model.PokemonFavorito

@Dao
interface PokemonFavoritoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salvar(pokemonFavorito: PokemonFavorito)

    @Delete
    fun deletar(pokemonFavorito: PokemonFavorito)

    @Query("SELECT * FROM pokemon_favorito WHERE idPokemon = :idPokemon")
    fun verificaFavorito(idPokemon: Int): LiveData<PokemonFavorito?>

    @Query("SELECT * FROM pokemon_favorito WHERE idPokemon = :idPokemon")
    fun buscarFavorito(idPokemon: Int): PokemonFavorito?
}