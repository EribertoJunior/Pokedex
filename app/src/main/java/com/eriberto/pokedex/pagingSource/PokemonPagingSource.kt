package com.eriberto.pokedex.pagingSource

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.eriberto.pokedex.model.PokemonData
import com.eriberto.pokedex.network.PokeService
import retrofit2.HttpException
import java.io.IOException

class PokemonPagingSource(private val pokeService: PokeService) : PagingSource<Int, PokemonData>() {

    override fun getRefreshKey(state: PagingState<Int, PokemonData>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PokemonData> {
        return try {
            val nextOffset: Int = params.key ?: PRIMEIRO_DESLOCAMENTO_OFFSET
            val response = pokeService.getListPokemon(offset = nextOffset)

            var nextOffsetNumber:Int? = null

            if (response.next != null){
                val uri = Uri.parse(response.next)
                val nextOffsetQuery = uri.getQueryParameter("offset")
                nextOffsetNumber = nextOffsetQuery?.toInt()
            }

            LoadResult.Page(data = response.results, prevKey = null , nextKey = nextOffsetNumber)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }catch (e: HttpException) {
             LoadResult.Error(e)
        }
    }

    companion object {
        const val PRIMEIRO_DESLOCAMENTO_OFFSET = 0
    }
}