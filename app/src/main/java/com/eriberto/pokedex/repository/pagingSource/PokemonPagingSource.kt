package com.eriberto.pokedex.repository.pagingSource

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eriberto.pokedex.repository.model.PokemonData
import com.eriberto.pokedex.repository.model.PokemonList
import com.eriberto.pokedex.repository.network.PokeService
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class PokemonPagingSource(private val pokeService: PokeService) : PagingSource<Int, PokemonData>() {

    override fun getRefreshKey(state: PagingState<Int, PokemonData>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonData> {
        return try {
            val nextOffset: Int = params.key ?: PRIMEIRO_DESLOCAMENTO_OFFSET
            val response = pokeService.getListPokemon(offset = nextOffset)

            val nextOffsetNumber:Int? = getNextOffSet(response)

            loadResultSuccess(response, nextOffsetNumber)
        } catch (e: IOException) {
            loadResultError(e)
        }catch (e: HttpException) {
            loadResultError(e)
        }
    }

    fun loadResultError(e: Exception): LoadResult.Error<Int, PokemonData> =
        LoadResult.Error(e)

    fun loadResultSuccess(
        response: PokemonList,
        nextOffsetNumber: Int?
    ) = LoadResult.Page(
        data = response.results,
        prevKey = null,
        nextKey = nextOffsetNumber
    )

    fun getNextOffSet(response: PokemonList): Int? {
        var nextOffsetNumber: Int? = null
        if (response.next != null) {
            val uri = Uri.parse(response.next)
            val nextOffsetQuery = uri.getQueryParameter("offset")
            nextOffsetNumber = nextOffsetQuery?.toInt()
        }
        return nextOffsetNumber
    }

    companion object {
        const val PRIMEIRO_DESLOCAMENTO_OFFSET = 0
    }
}