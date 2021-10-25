package com.eriberto.pokedex.data.remote.network

import android.content.Context
import com.eriberto.pokedex.data.remote.network.OkHttpProvider.getOkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroConfig(private val baseUrl: String, private val context: Context) {

    private fun getRetroInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getPokeServide(): PokeService = getRetroInstance().create(PokeService::class.java)

}