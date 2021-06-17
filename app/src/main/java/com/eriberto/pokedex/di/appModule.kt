package com.eriberto.pokedex.di

import com.eriberto.pokedex.MainActivityViewModel
import com.eriberto.pokedex.network.RetroConfig
import com.eriberto.pokedex.pagingSource.PokemonPagingSource
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { RetroConfig(androidContext()).getPokeServide() }

    factory { PokemonPagingSource(pokeService = get()) }

    viewModel { MainActivityViewModel(pokemonPagingSource = get()) }
}