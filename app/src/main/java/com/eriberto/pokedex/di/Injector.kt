package com.eriberto.pokedex.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

fun Application.setUpDI(){

    startKoin {
        androidLogger()
        androidContext(this@setUpDI)

        modules(appModule)
    }

}