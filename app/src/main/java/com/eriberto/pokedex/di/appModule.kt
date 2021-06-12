package com.eriberto.pokedex.di

import com.eriberto.pokedex.network.RetroConfig
import org.koin.dsl.module

val appModule = module {
    single { RetroConfig() }
}