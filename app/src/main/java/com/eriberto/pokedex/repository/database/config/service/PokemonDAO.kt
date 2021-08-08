package com.eriberto.pokedex.repository.database.config.service

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eriberto.pokedex.repository.database.model.EntidadePokemon

@Dao
interface PokemonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(entidadePokemon: EntidadePokemon)

    @Query("SELECT * FROM EntidadePokemon")
    fun buscarTodos(): List<EntidadePokemon>

    @Delete
    fun deletar(entidadePokemon: EntidadePokemon)

    @Query("SELECT * FROM EntidadePokemon WHERE name = :nomePokemon")
    fun verificarPokemonLocal(nomePokemon: String): LiveData<EntidadePokemon?>

    @Query("SELECT * FROM PokemonLocal WHERE name = :nomePokemon")
    fun verificarPokemonLocal(nomePokemon: String): LiveData<PokemonLocal?>
}