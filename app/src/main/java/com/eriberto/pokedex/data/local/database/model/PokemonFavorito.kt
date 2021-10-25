package com.eriberto.pokedex.data.local.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_favorito")
class PokemonFavorito(
    @PrimaryKey
    val idPokemon: Int,
    val nomePokemon: String
)