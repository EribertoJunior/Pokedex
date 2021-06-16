package com.eriberto.pokedex

import android.app.Application
import com.eriberto.pokedex.di.setUpDI

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        setUpDI()
    }
}