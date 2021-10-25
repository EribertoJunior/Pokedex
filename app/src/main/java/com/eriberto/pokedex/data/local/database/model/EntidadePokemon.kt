package com.eriberto.pokedex.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class EntidadePokemon(
    @PrimaryKey
    var id: Int = 0,
    var name: String,
    var url: String,
    var favorito: Boolean = false
): Serializable