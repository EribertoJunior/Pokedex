package com.eriberto.pokedex.di

import com.eriberto.pokedex.repository.DetalhePokemonRepo
import com.eriberto.pokedex.repository.DetalhePokemonRepoImp
import com.eriberto.pokedex.viewmodel.MainActivityViewModel
import com.eriberto.pokedex.repository.network.RetroConfig
import com.eriberto.pokedex.repository.pagingSource.PokemonPagingSource
import com.eriberto.pokedex.viewmodel.DetalhePokemonViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

const val PROPERTY_BASE_URL = "PROPERTY_BASE_URL"

val appModule = module {
    single {
        val baseUrl = getProperty(PROPERTY_BASE_URL)
        RetroConfig(baseUrl = baseUrl,context = androidContext()).getPokeServide() }

    factory { PokemonPagingSource(pokeService = get()) }
    factory<DetalhePokemonRepo> { DetalhePokemonRepoImp(pokeService = get()) }

    viewModel { MainActivityViewModel(pokemonPagingSource = get()) }
    viewModel { DetalhePokemonViewModel(detalhePokemonRepo = get()) }
}