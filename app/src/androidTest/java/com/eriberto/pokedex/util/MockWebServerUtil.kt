package com.eriberto.pokedex.util

import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import com.eriberto.pokedex.repository.network.OkHttpProvider
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest

object MockWebServerUtil {

    private fun readStringFromFile(fileName: String): String? {
        val assets = InstrumentationRegistry.getInstrumentation().context.assets
        return assets.open(fileName).reader().readText()
    }

    fun registraOkHttp() {
        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                OkHttpProvider.getOkHttpClient()
            )
        )
    }

    fun getDispatchResponse(): Dispatcher {
        return object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/pokemon?limit=10&offset=0" -> {
                        getMockResponse(assets = PAG_1)
                    }
                    "/pokemon?limit=10&offset=10" -> {
                        getMockResponse(assets = PAG_2)
                    }
                    "/pokemon?limit=10&offset=20" -> {
                        getMockResponse(assets = PAG_3)
                    }
                    "/pokemon?limit=10&offset=30" -> {
                        getMockResponse(assets = PAG_4)
                    }
                    "/pokemon?limit=10&offset=40" -> {
                        getMockResponse(assets = PAG_5)
                    }
                    "/pokemon?limit=10&offset=50" -> {
                        getMockResponse(assets = PAG_6)
                    }
                    "/pokemon/1" -> {
                        getMockResponse(assets = DETALHES)
                    }
                    else -> {
                        getMockResponse(responseCode = 400)
                    }
                }
            }

        }
    }

    fun getMockResponse(assets: String? = null, responseCode: Int = 200): MockResponse {
        return MockResponse().apply {
            setResponseCode(responseCode)
            if (assets != null)
                readStringFromFile(assets)?.let { setBody(it) }
        }
    }


    //success
    private const val PAG_1: String = "list_pag_1_success_response.json"
    private const val PAG_2: String = "list_pag_2_success_response.json"
    private const val PAG_3: String = "list_pag_3_success_response.json"
    private const val PAG_4: String = "list_pag_4_success_response.json"
    private const val PAG_5: String = "list_pag_5_success_response.json"
    private const val PAG_6: String = "list_pag_6_success_response.json"
    private const val DETALHES: String = "details_bulbasaur_success_response.json"


}