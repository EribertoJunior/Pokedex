package com.eriberto.pokedex.repository.database.config.service

import androidx.room.*
import com.eriberto.pokedex.repository.database.model.PokemonLocal

@Dao
interface PokemonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(pokemonLocal: PokemonLocal)

    @Query("SELECT * FROM PokemonLocal")
    fun buscarTodos(): List<PokemonLocal>

    @Delete
    fun deletar(pokemonLocal: PokemonLocal)

    @Query("SELECT * FROM PokemonLocal WHERE name = :nomePokemon")
    fun verificarPokemonLocal(nomePokemon: String): PokemonLocal?
}