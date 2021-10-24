package com.eriberto.pokedex.di

import androidx.paging.ExperimentalPagingApi
import com.eriberto.pokedex.data.PokemonRemoteMediator
import com.eriberto.pokedex.repository.PokemonRepo
import com.eriberto.pokedex.repository.PokemonRepoImp
import com.eriberto.pokedex.repository.database.config.PokemonDatabase
import com.eriberto.pokedex.repository.network.RetroConfig
import com.eriberto.pokedex.repository.pagingSource.PokemonPagingSource
import com.eriberto.pokedex.view.detalhe.TipoPokemonAdapter
import com.eriberto.pokedex.view.lista.ListaPokemonAdapter
import com.eriberto.pokedex.viewmodel.ListaPokemonViewModel
import com.eriberto.pokedex.viewmodel.detalhe.DetalhePokemonViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

const val PROPERTY_BASE_URL = "PROPERTY_BASE_URL"

@ExperimentalPagingApi
val appModule = module {
    single {
        val baseUrl = getProperty(PROPERTY_BASE_URL)
        RetroConfig(baseUrl = baseUrl, context = androidContext()).getPokeServide()
    }

    factory { PokemonPagingSource(pokeService = get()) }

    single { PokemonDatabase.getDatabase(androidContext()) }
    single { get<PokemonDatabase>().pokemonDAO() }
    single { get<PokemonDatabase>().pokemonFavoritoDAO() }
    single { get<PokemonDatabase>().chavesRemotasDAO() }

    factory {
        PokemonRemoteMediator(
            pokeService = get(),
            pokemonDAO = get(),
            chavesRemotasDAO = get(),
            pokemonFavoritoDAO = get()
        )
    }
    factory<PokemonRepo> {
        PokemonRepoImp(
            pokeService = get(),
            pokemonFavoritoDAO = get(),
            pokemonRemoteMediator = get(),
            pokemonDAO = get()
        )
    }

    factory { ListaPokemonAdapter() }
    factory { TipoPokemonAdapter() }

    viewModel { ListaPokemonViewModel(pokemonRepo = get()) }
    viewModel { DetalhePokemonViewModel(pokemonRepo = get()) }
}