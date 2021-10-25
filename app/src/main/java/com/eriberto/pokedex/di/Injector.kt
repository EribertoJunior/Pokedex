package com.eriberto.pokedex.di

import android.app.Application
import androidx.paging.ExperimentalPagingApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

var BASE_URL: String = ""

@ExperimentalPagingApi
fun Application.setUpDI() {

    startKoin {
        androidLogger()
        androidContext(this@setUpDI)
        androidLogger(Level.ERROR)
        properties(
            mapOf(
                PROPERTY_BASE_URL to BASE_URL
            )
        )
        modules(listOf(appModule, viewModels, adapters, database, remote, dataSource))
    }

}