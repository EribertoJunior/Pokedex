package com.eriberto.pokedex.repository

import com.eriberto.pokedex.repository.model.PokeDetalhe
import com.eriberto.pokedex.repository.network.PokeService
import com.eriberto.pokedex.util.Result
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback

class DetalhePokemonRepoImpTest {

    private var service: PokeService = mockk()

    private var resultMock = mockk<Result<PokeDetalhe>>()

    private var responseMock = mockk<retrofit2.Response<PokeDetalhe>>()

    private var callMock = mockk<Call<PokeDetalhe>>()

    @Test
    fun `deve notificar detalhes do pokemon quando resposta for bem sucedida`() {
        val detalhePokemonRepoImp = DetalhePokemonRepoImp(service)

        every { service.getDetalhePokemon(1) } returns callMock
        every { callMock.enqueue(any()) } answers {
            (args[0] as Callback<PokeDetalhe>).apply {
                onResponse(callMock, responseMock)
            }
        }

        responseMock.apply {
            every { isSuccessful } returns true
            every { body() } returns mockk()
        }

        every { resultMock.success(any()) } answers {}

        runBlocking { detalhePokemonRepoImp.getDetalhePokemon(1, resultMock) }

        coVerify { resultMock.success(any()) }
    }

    @Test
    fun `deve notificar mensagem de erro quando resposta for mal sucedida`() {
        val detalhePokemonRepoImp = DetalhePokemonRepoImp(service)

        every { service.getDetalhePokemon(1) } returns callMock
        every { callMock.enqueue(any()) } answers {
            (args[0] as Callback<PokeDetalhe>).apply {
                onResponse(callMock, responseMock)
            }
        }

        responseMock.apply {
            every { isSuccessful } returns false
            every { code() } returns 400
            every { message() } returns "Bad Request"
        }

        every { resultMock.error(any()) } answers {}

        runBlocking { detalhePokemonRepoImp.getDetalhePokemon(1, resultMock) }

        coVerify { resultMock.error(any()) }
    }

    @Test
    fun `deve notificar mensagem de erro quando a requisicao falhar`(){
        val detalhePokemonRepoImp = DetalhePokemonRepoImp(service)

        every { service.getDetalhePokemon(1) } returns callMock
        every { callMock.enqueue(any()) } answers {
            (args[0] as Callback<PokeDetalhe>).apply {
                onFailure(callMock, Throwable("Error"))
            }
        }
        every { resultMock.error(any()) } answers {}

        runBlocking { detalhePokemonRepoImp.getDetalhePokemon(1, resultMock) }

        coVerify { resultMock.error(any()) }

    }
}