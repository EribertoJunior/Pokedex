package com.eriberto.pokedex.repository

import androidx.paging.ExperimentalPagingApi
import com.eriberto.pokedex.repository.database.model.EntidadePokemon
import com.eriberto.pokedex.repository.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.repository.network.PokeService
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
@ExperimentalPagingApi
class PokemonRepoImpTest {
    private var pokeServiceMock: PokeService = mockk()

    private val pokemonRepoImp = spyk(
        PokemonRepoImp(
            pokeService = pokeServiceMock,
            pokemonDAO = mockk(),
            pokemonFavoritoDAO = mockk(),
            pokemonRemoteMediator = mockk()
        )
    )

    private val pokemonRepo: PokemonRepo = pokemonRepoImp

    @Test
    fun `deve notificar detalhes do pokemon quando resposta da requisicao de detalhes do pokemon for bem sucedida`() {
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
        runBlocking { pokemonRepo.buscarDetalhesDoPokemon(1, success, erro = { }) }
        coVerify { success(responseRetornoPokemonDetalheMock.body()!!) }
    }
    
    @Test
    fun `deve notificar mensagem de erro quando a resposta da requisicao de detalhes do pokemon for mal sucedida`() {
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
        runBlocking { pokemonRepo.buscarDetalhesDoPokemon(1, success = { }, erro) }
        coVerify { erro(responseRetornoPokemonDetalheMock.message()) }
    }

    @Test
    fun `deve notificar mensagem de erro quando a requisicao de detalhes do pokemon falhar`() {
        val callRetornoPokemonDetalheMock: Call<RetornoPokemonDetalhe> = mockk()
        val erro: (mensagemDeErro: String) -> Unit = {}
        val throwable = spyk(Throwable("Error"))

        every { pokeServiceMock.getDetalhePokemon(1) } returns callRetornoPokemonDetalheMock
        every { callRetornoPokemonDetalheMock.enqueue(any()) } answers {
            (args[0] as Callback<RetornoPokemonDetalhe>).apply {
                onFailure(callRetornoPokemonDetalheMock, throwable)
            }
        }
        runBlocking { pokemonRepo.buscarDetalhesDoPokemon(1, success = { }, erro) }
        coVerify { erro(throwable.message!!) }
    }

    @Test
    fun `deve salvar um pokemon como favorito e atualizar a entidade para favorito quando solicitado`() {
        val entidadePokemon =
            EntidadePokemon(id = 1, name = "Pikachu", url = "/1", favorito = false)

        every { pokemonRepoImp.salvaPokemonFavorito(any()) } answers { }

        every { pokemonRepoImp.salvaEntidadePokemon(any()) } answers { }

        runBlocking { pokemonRepo.favoritarPokemon(entidadePokemon) }

        coVerify { pokemonRepoImp.salvaPokemonFavorito(any()) }
        assertThat(entidadePokemon.favorito, `is`(true))
        coVerify { pokemonRepoImp.salvaEntidadePokemon(any()) }
    }

    @Test
    fun `deve deletar um pokemon favorito e atualzar a entidade para nao favorito quando solicitado`() {
        val entidadePokemon = EntidadePokemon(1, "Pikachu", "/1", true)

        every { pokemonRepoImp.deletaPokemonFavorito(any()) } answers { }
        every { pokemonRepoImp.salvaEntidadePokemon(any()) } answers { }

        runBlocking { pokemonRepo.desfavoritarPokemon(entidadePokemon) }

        coVerify { pokemonRepoImp.deletaPokemonFavorito(any()) }
        assertThat(entidadePokemon.favorito, `is`(false))
        coVerify { pokemonRepoImp.salvaEntidadePokemon(any()) }
    }
}