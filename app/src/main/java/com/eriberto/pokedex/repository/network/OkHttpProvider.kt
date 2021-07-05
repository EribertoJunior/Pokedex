package com.eriberto.pokedex.repository.network

import android.content.Context
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object OkHttpProvider {
    private var okHttpClient: OkHttpClient? = null
    private var interceptador = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    fun getOkHttpClient(context: Context? = null): OkHttpClient {
        return if (okHttpClient == null) {
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(interceptador)
                .cache(getMyCache(context))
                .build()

            this.okHttpClient = okHttpClient
            okHttpClient
        } else {
            okHttpClient!!
        }
    }

    private fun getMyCache(context: Context?): Cache? {
        return if (context != null) {
            val cacheSize = (5 * 1024 * 1024).toLong()
            Cache(context.cacheDir, cacheSize)
        } else {
            null
        }
    }
}