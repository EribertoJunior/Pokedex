package com.eriberto.pokedex.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokemonLocal(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val url: String
)