package com.eriberto.pokedex.di

import com.eriberto.pokedex.viewmodel.MainActivityViewModel
import com.eriberto.pokedex.repository.network.RetroConfig
import com.eriberto.pokedex.repository.pagingSource.PokemonPagingSource
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetroConfig(androidContext()).getPokeServide() }

    factory { PokemonPagingSource(pokeService = get()) }

    viewModel { MainActivityViewModel(pokemonPagingSource = get()) }
}