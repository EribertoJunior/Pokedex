package com.eriberto.pokedex.data.local.database.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.eriberto.pokedex.data.local.database.config.service.ChavesRemotasDAO
import com.eriberto.pokedex.data.local.database.config.service.PokemonDAO
import com.eriberto.pokedex.data.local.database.config.service.PokemonFavoritoDAO
import com.eriberto.pokedex.data.local.database.model.ChaveRemota
import com.eriberto.pokedex.data.local.database.model.EntidadePokemon
import com.eriberto.pokedex.data.local.database.model.PokemonFavorito

@Database(entities = [EntidadePokemon::class, ChaveRemota::class, PokemonFavorito::class], version = 1, exportSchema = false)
abstract class PokemonDatabase : RoomDatabase(){

    abstract fun pokemonDAO(): PokemonDAO
    abstract fun chavesRemotasDAO(): ChavesRemotasDAO
    abstract fun pokemonFavoritoDAO(): PokemonFavoritoDAO

    companion object{
        @Volatile
        private var INSTANCE: PokemonDatabase? = null

        fun getDatabase(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this){
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        PokemonDatabase::class.java,
                        "pokemon_favorito_database"
                    )//.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                    instance
            }
        }
//        private val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//
//                database.execSQL("CREATE TABLE pokemon_favorito (idPokemon INTEGER NOT NULL, nomePokemon TEXT NOT NULL, PRIMARY KEY(idPokemon))")
//
//                database.execSQL("CREATE TABLE IF NOT EXISTS `EntidadePokemon_Novo` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, `url` TEXT NOT NULL, PRIMARY KEY(`id`))")
//
//                database.execSQL("INSERT INTO EntidadePokemon_Novo (id, name,url) SELECT id, name, url FROM EntidadePokemon")
//
//                database.execSQL("drop table EntidadePokemon")
//
//                database.execSQL("ALTER TABLE EntidadePokemon_Novo RENAME TO EntidadePokemon")
//            }
//        }
//
//        private val MIGRATION_2_3 = object : Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//
//                database.execSQL("ALTER TABLE EntidadePokemon ADD COLUMN favorito INTEGER NOT NULL DEFAULT 0")
//            }
//        }
    }
}