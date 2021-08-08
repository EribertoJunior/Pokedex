package com.eriberto.pokedex.view.detalhe

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.eriberto.pokedex.R
import com.eriberto.pokedex.repository.model.Pokemon
import com.eriberto.pokedex.util.MockWebServerUtil.getDispatchResponse
import com.eriberto.pokedex.util.MockWebServerUtil.registraOkHttp
import com.eriberto.pokedex.util.ViewMatcher.apareceNoCollapsibleToolbarOTitilo
import com.eriberto.pokedex.util.ViewMatcher.apareceTipoPokemonNaPosicao
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.CoreMatchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DetalhePokemonActivityTest {

    private val nomePokemon = Pokemon(name = "bulbasaur", url = "/1")
    private val idPokemon = 1
    companion object{
        const val ID_POKEMON = "idPokemon"
        const val POKEMON_SELECIONADO = "nomePokemon"
    }

    private val startActivityIntent =
        Intent(ApplicationProvider.getApplicationContext(), DetalhePokemonActivity::class.java)
            .putExtra(ID_POKEMON, idPokemon)
            .putExtra(POKEMON_SELECIONADO, nomePokemon)

    @get:Rule
    val activity = ActivityScenarioRule<DetalhePokemonActivity>(startActivityIntent)

    lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
        registraOkHttp()
    }

    @Test
    fun deve_ExibirDadosDoPokemon_QuandoABuscaDeDetalhesForBemSucedida() {
        configuraRetornoPaginadoAPI()
        // depende de putExtra
        onView(withId(R.id.ivPokemonDetalhe))
            .check(matches(isDisplayed()))

        onView(
            withId(R.id.collapsing_toolbar_detalhes_pokemon)
        ).check(matches(apareceNoCollapsibleToolbarOTitilo("bulbasaur")))

        onView(allOf(withId(R.id.tvAltura), withText("0.7 m")))
            .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.tvPeso), withText("6.9 kg")))
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.tvHabilidade),
                withText("overgrow\nchlorophyll")
            )
        ).check(matches(isDisplayed()))

        onView(withId(R.id.recyclerViewTipoPokemon))
            .check(matches(allOf(apareceTipoPokemonNaPosicao(0), apareceTipoPokemonNaPosicao(1))))
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private fun configuraRetornoPaginadoAPI() {
        mockWebServer.dispatcher = getDispatchResponse()
    }
}