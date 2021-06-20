package com.eriberto.pokedex.repository.pagingSource

import androidx.paging.PagingSource
import com.eriberto.pokedex.repository.model.PokemonData
import com.eriberto.pokedex.repository.model.PokemonList
import com.eriberto.pokedex.repository.network.PokeService
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test

class PokemonPagingSourceTest {

    private val service = mockk<PokeService>()

    private val mockedPokemonList = PokemonList(next = null, results = listOf(PokemonData("", "")))


    @Ignore
    @Test
    fun `deve retornar lista de itens quando busca for bem sucedida`() {

        val spyPokemonPagingSource = spyk(PokemonPagingSource(service))

        coEvery { service.getListPokemon(offset = 0) } returns mockedPokemonList

        coEvery { spyPokemonPagingSource.getNextOffSet(mockedPokemonList) } returns null

        coEvery { spyPokemonPagingSource.load(any()) } returns PagingSource.LoadResult.Page(
            data = mockedPokemonList.results,
            prevKey = null,
            nextKey = null
        )

        runBlocking {
            val params: PagingSource.LoadParams<Int> = mockk()

            spyPokemonPagingSource.load(params)

        }
//edla
        //parallax
        coVerify { spyPokemonPagingSource.loadResultSuccess(any(), any()) }

    }

    @Test
    fun `deve retornar um excepition quando a busca nao for bem sucedida`() {

    }

}