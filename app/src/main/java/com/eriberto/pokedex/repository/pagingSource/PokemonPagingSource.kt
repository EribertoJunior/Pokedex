package com.eriberto.pokedex.repository.pagingSource

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eriberto.pokedex.repository.model.Pokemon
import com.eriberto.pokedex.repository.model.RetornoPokemon
import com.eriberto.pokedex.repository.network.PokeService
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class PokemonPagingSource(private val pokeService: PokeService) : PagingSource<Int, Pokemon>() {

    override fun getRefreshKey(state: PagingState<Int, Pokemon>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pokemon> {
        return try {
            val offsetAtual: Int = params.key ?: PRIMEIRO_DESLOCAMENTO_OFFSET
            val response = pokeService.getListPokemon(offset = offsetAtual)
            val nextOffsetNumber: Int? = getNextOffSet(response)

            loadResultSuccess(response, offsetAtual, nextOffsetNumber)

        } catch (e: IOException) {
            loadResultError(e)
        } catch (e: HttpException) {
            loadResultError(e)
        } catch (e: Exception) {
            loadResultError(e)
        }
    }

    fun loadResultError(e: Exception): LoadResult.Error<Int, Pokemon> {
        return LoadResult.Error(e)
    }

    fun loadResultSuccess(
        response: RetornoPokemon,
        offsetAtual: Int,
        nextOffsetNumber: Int?
    ): LoadResult.Page<Int, Pokemon> {
        return LoadResult.Page(
            data = response.results,
            prevKey = if (offsetAtual == PRIMEIRO_DESLOCAMENTO_OFFSET) null else offsetAtual - 1,
            nextKey = nextOffsetNumber
        )
    }

    private fun getNextOffSet(response: RetornoPokemon): Int? {
        var nextOffsetNumber: Int? = null
        if (response.next.isNullOrEmpty().not()) {
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