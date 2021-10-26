package com.eriberto.pokedex.di

import androidx.paging.ExperimentalPagingApi
import com.eriberto.pokedex.data.PokemonRemoteMediator
import com.eriberto.pokedex.repository.PokemonRepo
import com.eriberto.pokedex.repository.PokemonRepoImp
import com.eriberto.pokedex.data.local.database.config.PokemonDatabase
import com.eriberto.pokedex.data.remote.network.RetroConfig
import com.eriberto.pokedex.data.PokemonPagingSource
import com.eriberto.pokedex.data.local.LocalDataSource
import com.eriberto.pokedex.data.local.LocalDataSourceImp
import com.eriberto.pokedex.data.local.database.config.service.PokemonFavoritoDAO
import com.eriberto.pokedex.data.remote.RemoteDataSource
import com.eriberto.pokedex.data.remote.RemoteDataSourceImp
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
    factory { PokemonPagingSource(pokeService = get()) }

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
            localDataSource = get(),
            remoteDataSource = get()
        )
    }
}

@ExperimentalPagingApi
val dataSource = module {
    single<LocalDataSource> {
        LocalDataSourceImp(
            pokemonFavoritoDAO = get(),
            pokemonDAO = get(),
            pokemonRemoteMediator = get()
        )
    }

    single<RemoteDataSource> { RemoteDataSourceImp(pokeService = get()) }
}

val remote = module {
    single {
        val baseUrl = getProperty(PROPERTY_BASE_URL)
        RetroConfig(baseUrl = baseUrl, context = androidContext()).getPokeServide()
    }
}

val database = module {
    single { PokemonDatabase.getDatabase(androidContext()) }
    single { get<PokemonDatabase>().pokemonDAO() }
    single { get<PokemonDatabase>().pokemonFavoritoDAO() }
    single { get<PokemonDatabase>().chavesRemotasDAO() }
}

val adapters = module {
    factory { ListaPokemonAdapter() }
    factory { TipoPokemonAdapter() }
}

val viewModels = module {
    viewModel { ListaPokemonViewModel(pokemonRepo = get()) }
    viewModel { DetalhePokemonViewModel(pokemonRepo = get()) }
}