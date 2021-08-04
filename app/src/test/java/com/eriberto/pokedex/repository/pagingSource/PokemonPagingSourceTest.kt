package com.eriberto.pokedex.repository.pagingSource

import androidx.paging.PagingSource
import com.eriberto.pokedex.repository.model.PokemonList
import com.eriberto.pokedex.repository.network.PokeService
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.IOException

class PokemonPagingSourceTest {

    private val service = mockk<PokeService>()
    private val loadParamsMock = mockk<PagingSource.LoadParams<Int>>()
    private val responseMock = mockk<PokemonList>()

    @Test
    fun `deve notificar sucesso quando busca for bem sucedida`() {

        val pokemonPagingSource = spyk(PokemonPagingSource(service))

        coEvery { loadParamsMock.key } returns null
        coEvery { service.getListPokemon(offset = 0) } returns responseMock

        responseMock.apply {
            every { next } returns null
            every { results } returns listOf()
        }

        runBlocking { pokemonPagingSource.load(loadParamsMock) }

        coVerify { pokemonPagingSource.loadResultSuccess(any(), any(), any()) }

    }

    @Test
    fun `deve notificar um throwable quando a busca nao for bem sucedida`() {

        val pokemonPagingSource = spyk(PokemonPagingSource(service))

        coEvery { loadParamsMock.key } returns 0
        coEvery { service.getListPokemon(offset = 0) } throws IOException()

        runBlocking { pokemonPagingSource.load(loadParamsMock) }

        coVerify { pokemonPagingSource.loadResultError(any()) }
    }

}