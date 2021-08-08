package com.eriberto.pokedex.di

import com.eriberto.pokedex.repository.PokemonRepo
import com.eriberto.pokedex.repository.PokemonRepoImp
import com.eriberto.pokedex.repository.database.config.PokemonDatabase
import com.eriberto.pokedex.repository.network.RetroConfig
import com.eriberto.pokedex.repository.pagingSource.PokemonPagingSource
import com.eriberto.pokedex.viewmodel.ListaPokemonViewModel
import com.eriberto.pokedex.viewmodel.detalhe.DetalhePokemonViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

const val PROPERTY_BASE_URL = "PROPERTY_BASE_URL"

val appModule = module {
    single {
        val baseUrl = getProperty(PROPERTY_BASE_URL)
        RetroConfig(baseUrl = baseUrl, context = androidContext()).getPokeServide()
    }

    factory { PokemonPagingSource(pokeService = get()) }
    single { PokemonDatabase.getDatabase(androidContext()).pokemonDAO() }
    factory<PokemonRepo> { PokemonRepoImp(pokeService = get(), pokemonDAO = get()) }

    viewModel { ListaPokemonViewModel(pokemonRepo = get()) }
    viewModel { DetalhePokemonViewModel(pokemonRepo = get()) }
}