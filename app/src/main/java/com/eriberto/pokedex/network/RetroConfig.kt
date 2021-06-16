package com.eriberto.pokedex.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroConfig {

    companion object {
        val baseUrl = "https://pokeapi.co/api/v2/"
        var interceptador: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        var client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptador).build()

        private fun getRetroInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    fun getPokeServide(): PokeService = getRetroInstance().create(PokeService::class.java)

}