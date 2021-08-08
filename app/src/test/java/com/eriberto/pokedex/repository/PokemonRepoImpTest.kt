package com.eriberto.pokedex.repository

import com.eriberto.pokedex.repository.database.config.PokemonDatabase
import com.eriberto.pokedex.repository.database.config.service.PokemonDAO
import com.eriberto.pokedex.repository.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.repository.network.PokeService
import com.eriberto.pokedex.util.Result
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback

class PokemonRepoImpTest {

    private var service: PokeService = mockk()
    private var pokemonDatabase: PokemonDatabase = mockk()
    private var pokemonDAO: PokemonDAO = mockk()

    private var resultMock = mockk<Result<RetornoPokemonDetalhe>>()

    private var responseMock = mockk<retrofit2.Response<RetornoPokemonDetalhe>>()

    private var callMock = mockk<Call<RetornoPokemonDetalhe>>()

    @Test
    fun `deve notificar detalhes do pokemon quando resposta for bem sucedida`() {
        val detalhePokemonRepoImp = PokemonRepoImp(service, pokemonDatabase)

        every { service.getDetalhePokemon(1) } returns callMock
        every { callMock.enqueue(any<Callback<RetornoPokemonDetalhe>>()) } answers {
            (args[0] as Callback<RetornoPokemonDetalhe>).apply {
                onResponse(callMock, responseMock)
            }
        }

        responseMock.apply {
            every { isSuccessful } returns true
            every { body() } returns mockk()
        }

        every { resultMock.success(any()) } answers {}

        runBlocking { detalhePokemonRepoImp.buscarDetalhesDoPokemon(1, success = { }, erro = { }) }

        coVerify { resultMock.success(any()) }
    }

    @Test
    fun `deve notificar mensagem de erro quando resposta for mal sucedida`() {
        val detalhePokemonRepoImp = PokemonRepoImp(service, pokemonDatabase)

        every { service.getDetalhePokemon(1) } returns callMock
        every { callMock.enqueue(any()) } answers {
            (args[0] as Callback<RetornoPokemonDetalhe>).apply {
                onResponse(callMock, responseMock)
            }
        }

        responseMock.apply {
            every { isSuccessful } returns false
            every { code() } returns 400
            every { message() } returns "Bad Request"
        }

        every { resultMock.error(any()) } answers {}

        runBlocking { detalhePokemonRepoImp.buscarDetalhesDoPokemon(1, success = { }, erro = { }) }

        coVerify { resultMock.error(any()) }
    }

    @Test
    fun `deve notificar mensagem de erro quando a requisicao falhar`() {
        val detalhePokemonRepoImp = PokemonRepoImp(service, pokemonDatabase)
        every { service.getDetalhePokemon(1) } returns callMock
        every { callMock.enqueue(any()) } answers {
            (args[0] as Callback<RetornoPokemonDetalhe>).apply {
                onFailure(callMock, Throwable("Error"))
            }
        }
        every { resultMock.error(any()) } answers {}

        runBlocking { detalhePokemonRepoImp.buscarDetalhesDoPokemon(1, success = { }, erro = { }) }

        coVerify { resultMock.error(any()) }

    }
}