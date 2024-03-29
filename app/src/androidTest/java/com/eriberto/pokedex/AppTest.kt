package com.eriberto.pokedex

import androidx.paging.ExperimentalPagingApi
import com.eriberto.pokedex.di.BASE_URL

class AppTest: App() {

    private var url = "http://localhost:8080/"
    //private var url = "http://127.0.0.1:8080/"

    @ExperimentalPagingApi
    override fun onCreate() {
        super.onCreate()
        BASE_URL = getBaseUrl()
    }

    override fun getBaseUrl(): String {
        return url
    }
}