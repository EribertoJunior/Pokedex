package com.eriberto.pokedex.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chave_remota")
class ChaveRemota(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nomePokemon: String,
    val prevOffset: Int?,
    val nextOffset: Int?
)