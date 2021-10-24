package com.eriberto.pokedex.repository.database.config.service

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import com.eriberto.pokedex.repository.database.model.EntidadePokemon

@Dao
interface PokemonDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun salva(entidadePokemon: EntidadePokemon)

    @Query("SELECT * FROM EntidadePokemon")
    fun buscarTodos(): List<EntidadePokemon>

    @Query("SELECT * FROM EntidadePokemon WHERE name = :nomePokemon")
    fun getEntidadePokemon(nomePokemon: String): EntidadePokemon?

    @Delete
    fun deletar(entidadePokemon: EntidadePokemon)

    @Query("DELETE FROM EntidadePokemon")
    suspend fun limparFavoritos()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvarTodos(entidadePokemon:List<EntidadePokemon>)

    @Query("SELECT * FROM EntidadePokemon order by favorito desc")
    fun buscarPaginadaPokemon(): PagingSource<Int, EntidadePokemon>
}