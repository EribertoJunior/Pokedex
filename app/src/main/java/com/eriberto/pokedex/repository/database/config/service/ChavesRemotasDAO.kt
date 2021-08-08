package com.eriberto.pokedex.repository.database.config.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eriberto.pokedex.repository.database.model.ChaveRemota

@Dao
interface ChavesRemotasDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<ChaveRemota>)

    @Query("SELECT * FROM chave_remota WHERE nomePokemon = :nomePokemon")
    suspend fun remoteKeysPokemonName(nomePokemon: String): ChaveRemota?

    @Query("DELETE FROM chave_remota")
    suspend fun clearRemoteKeys()
}