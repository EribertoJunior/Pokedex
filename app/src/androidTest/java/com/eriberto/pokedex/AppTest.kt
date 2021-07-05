package com.eriberto.pokedex

import com.eriberto.pokedex.di.BASE_URL

class AppTest: App() {

    private var url = "http://127.0.0.1:8080"

    override fun onCreate() {
        super.onCreate()
        BASE_URL = getBaseUrl()
    }

    override fun getBaseUrl(): String {
        return url
    }
}