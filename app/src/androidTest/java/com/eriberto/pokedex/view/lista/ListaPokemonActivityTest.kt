package com.eriberto.pokedex.view.lista

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eriberto.pokedex.R
import com.eriberto.pokedex.util.MockWebServerUtil.getDispatchResponse
import com.eriberto.pokedex.util.MockWebServerUtil.getMockResponse
import com.eriberto.pokedex.util.MockWebServerUtil.registraOkHttp
import com.eriberto.pokedex.util.ViewMatcher.aparecePokemonNaPosicao
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListaPokemonActivityTest {

    //@get:Rule
    //val activity = ActivityScenarioRule(ListaPokemonActivity::class.java)

    lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        registraOkHttp()
    }

    @Test
    fun deve_ExibirCinquentaPokemons_QuandoReceberCinquentaPokemonsDaAPI() {
        configuraRetornoPaginadoAPI()
        onView(withId(R.id.recyclerViewListaPokemon))
            .perform(scrollToPosition<ListaPokemonAdapter.MeuViewHolder>(49))
            .check(matches(aparecePokemonNaPosicao(position = 49, namePokemon = "diglett")))
    }

    @Test
    fun deve_ExibirTelaDeFalha_QuandoABuscaFalhar() {
        mockWebServer.enqueue(getMockResponse(responseCode = 400))
        onView(withId(R.id.imageViewPokemonFalha))
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withText("Falha na busca do catalogo, quer tentar de novo?"),
                withId(R.id.textViewMensagemDeFalha)
            )
        )
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withText("Tentar novamente"),
                withId(R.id.botaoTentarNovamente)
            )
        )
            .check(matches(isDisplayed()))
    }

    @Test
    fun deve_SerPossivelClicarNoBotaoTentarNovamente_QuandoABuscaFalhar() {
        mockWebServer.enqueue(getMockResponse(responseCode = 400))

        onView(
            allOf(
                withText("Tentar novamente"),
                withId(R.id.botaoTentarNovamente),
                isDisplayed()
            )
        ).check(matches(isClickable()))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun configuraRetornoPaginadoAPI() {
        mockWebServer.dispatcher = getDispatchResponse()
    }
}