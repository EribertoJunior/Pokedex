package com.eriberto.pokedex.repository.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class EntidadePokemon(
    @PrimaryKey
    var id: Int = 0,
    val name: String,
    val url: String,
    var favorito: Boolean = false
): Serializable