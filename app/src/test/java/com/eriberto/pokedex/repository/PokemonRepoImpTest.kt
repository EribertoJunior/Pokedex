package com.eriberto.pokedex.repository

import com.eriberto.pokedex.repository.database.config.PokemonDatabase
import com.eriberto.pokedex.repository.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.repository.network.PokeService
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
class PokemonRepoImpTest {
    private var pokeServiceMock: PokeService = mockk()
    private var pokemonDatabaseMock: PokemonDatabase = mockk()

    @Test
    fun `deve notificar detalhes do pokemon quando resposta da requisicao de detalhes do pokemon for bem sucedida`() {
        val detalhePokemonRepoImp = PokemonRepoImp(pokeServiceMock, pokemonDatabaseMock)
        val responseRetornoPokemonDetalheMock: Response<RetornoPokemonDetalhe> = mockk()
        val callRetornoPokemonDetalheMock: Call<RetornoPokemonDetalhe> = mockk()
        val success: (data: RetornoPokemonDetalhe) -> Unit = {}

        every { pokeServiceMock.getDetalhePokemon(1) } returns callRetornoPokemonDetalheMock
        every { callRetornoPokemonDetalheMock.enqueue(any<Callback<RetornoPokemonDetalhe>>()) } answers {
            (args[0] as Callback<RetornoPokemonDetalhe>).apply {
                onResponse(callRetornoPokemonDetalheMock, responseRetornoPokemonDetalheMock)
            }
        }
        responseRetornoPokemonDetalheMock.apply {
            every { isSuccessful } returns true
            every { body() } returns mockk()
        }
        runBlocking { detalhePokemonRepoImp.buscarDetalhesDoPokemon(1, success, erro = { }) }
        coVerify { success(responseRetornoPokemonDetalheMock.body()!!) }
    }

    @Test
    fun `deve notificar mensagem de erro quando a resposta da requisicao de detalhes do pokemon for mal sucedida`() {
        val detalhePokemonRepoImp = PokemonRepoImp(pokeServiceMock, pokemonDatabaseMock)
        val responseRetornoPokemonDetalheMock: Response<RetornoPokemonDetalhe> = mockk()
        val callRetornoPokemonDetalheMock: Call<RetornoPokemonDetalhe> = mockk()
        val erro: (errorMessage: String) -> Unit = {}

        every { pokeServiceMock.getDetalhePokemon(1) } returns callRetornoPokemonDetalheMock
        every { callRetornoPokemonDetalheMock.enqueue(any()) } answers {
            (args[0] as Callback<RetornoPokemonDetalhe>).apply {
                onResponse(callRetornoPokemonDetalheMock, responseRetornoPokemonDetalheMock)
            }
        }
        responseRetornoPokemonDetalheMock.apply {
            every { isSuccessful } returns false
            every { code() } returns 400
            every { message() } returns "Bad Request"
        }
        runBlocking { detalhePokemonRepoImp.buscarDetalhesDoPokemon(1, success = { }, erro) }
        coVerify { erro(responseRetornoPokemonDetalheMock.message()) }
    }

    @Test
    fun `deve notificar mensagem de erro quando a requisicao de detalhes do pokemon falhar`() {
        val detalhePokemonRepoImp = PokemonRepoImp(pokeServiceMock, pokemonDatabaseMock)
        val callRetornoPokemonDetalheMock: Call<RetornoPokemonDetalhe> = mockk()
        val erro: (mensagemDeErro: String) -> Unit = {}
        val throwable = spyk(Throwable("Error"))

        every { pokeServiceMock.getDetalhePokemon(1) } returns callRetornoPokemonDetalheMock
        every { callRetornoPokemonDetalheMock.enqueue(any()) } answers {
            (args[0] as Callback<RetornoPokemonDetalhe>).apply {
                onFailure(callRetornoPokemonDetalheMock, throwable)
            }
        }
        runBlocking { detalhePokemonRepoImp.buscarDetalhesDoPokemon(1, success = { }, erro) }
        coVerify { erro(throwable.message!!) }
    }
}