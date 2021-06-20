package com.eriberto.pokedex.repository.network

import android.content.Context
import com.eriberto.pokedex.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroConfig(context: Context) {
    private val cacheSize = (5 * 1024 * 1024).toLong()
    private val myCache = Cache(context.cacheDir, cacheSize)

    private val baseUrl = BuildConfig.SERVER_URL
    var interceptador = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private var client: OkHttpClient = OkHttpClient.Builder()
        .cache(myCache)
        .addInterceptor(interceptador)
        .build()

    private fun getRetroInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getPokeServide(): PokeService = getRetroInstance().create(PokeService::class.java)

}