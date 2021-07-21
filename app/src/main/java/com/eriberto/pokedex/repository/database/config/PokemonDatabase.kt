package com.eriberto.pokedex.repository.database.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eriberto.pokedex.repository.database.config.service.PokemonDAO
import com.eriberto.pokedex.repository.database.model.PokemonLocal

@Database(entities = [PokemonLocal::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase(){

    abstract fun pokemonDAO(): PokemonDAO

    companion object{
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        fun getDatabase(context: Context): PokemonDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        PokemonDatabase::class.java,
                        "pokemon_favorito_database"
                    ).build()
                    INSTANCE = instance
                    // return instance
                    instance
            }
        }
    }
}