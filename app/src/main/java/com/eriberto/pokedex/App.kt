package com.eriberto.pokedex

import android.app.Application
import com.eriberto.pokedex.di.BASE_URL
import com.eriberto.pokedex.di.setUpDI

open class App : Application() {
    override fun onCreate() {
        super.onCreate()
        BASE_URL = getBaseUrl()
        setUpDI()
    }

    protected open fun getBaseUrl(): String {
        return BuildConfig.SERVER_URL
    }
}