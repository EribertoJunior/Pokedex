package com.eriberto.pokedex.view.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eriberto.pokedex.repository.network.OkHttpProvider
import com.eriberto.pokedex.util.FileReader.readStringFromFile
import com.jakewharton.espresso.OkHttp3IdlingResource
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    val activity = ActivityScenarioRule(MainActivity::class.java)

    lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.dispatcher = getDispatchResponse()
        mockWebServer.start(8080)

        IdlingRegistry.getInstance().register(
            OkHttp3IdlingResource.create(
                "okhttp",
                OkHttpProvider.getOkHttpClient()
            )
        )
    }

    @Test
    fun deve_ExibirDezPokemons_QuandoReceberDezPokemonsDaAPI() {
        onView(withText("bulbasaur2")).check(ViewAssertions.matches(isDisplayed()))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun getDispatchResponse(): Dispatcher {
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
                    else -> {
                        getMockResponse(responseCode = 400)
                    }
                }
            }

            private fun getMockResponse(
                assets: String = "{}",
                responseCode: Int = 200
            ): MockResponse {
                return MockResponse()
                    .setResponseCode(responseCode)
                    .setBody(readStringFromFile(assets))
            }
        }
    }

    companion object {
        const val PAG_1: String = "list_pag_1_success_response.json"
        const val PAG_2: String = "list_pag_2_success_response.json"
        const val PAG_3: String = "list_pag_3_success_response.json"
        const val PAG_4: String = "list_pag_4_success_response.json"
        const val PAG_5: String = "list_pag_5_success_response.json"
        const val PAG_6: String = "list_pag_6_success_response.json"
    }
}