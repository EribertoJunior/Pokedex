package com.eriberto.pokedex.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.eriberto.pokedex.data.local.database.config.service.ChavesRemotasDAO
import com.eriberto.pokedex.data.local.database.config.service.PokemonDAO
import com.eriberto.pokedex.data.local.database.config.service.PokemonFavoritoDAO
import com.eriberto.pokedex.data.local.database.model.ChaveRemota
import com.eriberto.pokedex.data.local.database.model.EntidadePokemon
import com.eriberto.pokedex.data.remote.network.PokeService
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class PokemonRemoteMediator(
    private val pokeService: PokeService,
    private val pokemonDAO: PokemonDAO,
    private val chavesRemotasDAO: ChavesRemotasDAO,
    private val pokemonFavoritoDAO: PokemonFavoritoDAO
) : RemoteMediator<Int, EntidadePokemon>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, EntidadePokemon>
    ): MediatorResult {
        val offset = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = obterChaveRemotaMaisProximaDaPosicaoAtual(state)
                remoteKeys?.nextOffset?.minus(1) ?: PRIMEIRO_DESLOCAMENTO_OFFSET
            }
            LoadType.PREPEND -> {
                val remoteKeys = obterChaveRemotaParaOPrimeiroItem(state)
                // Se remoteKeys for nulo, significa que o resultado da atualização ainda não está no banco de dados.
                // Podemos retornar Success com `endOfPaginationReached = false` porque Paging
                // chamará esse método novamente se RemoteKeys se tornar não nulo.
                // Se remoteKeys for NOT NULL, mas seu prevKey for null, isso significa que alcançamos
                // fim da paginação para prefixo.
                val prevKey = remoteKeys?.prevOffset
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = obterChaveRemotaParaUltimoItem(state)
                // Se remoteKeys for nulo, significa que o resultado da atualização ainda não está no banco de dados.
                // Podemos retornar Success com endOfPaginationReached = false porque Paging
                // chamará esse método novamente se RemoteKeys se tornar não nulo.
                // Se remoteKeys for NOT NULL, mas seu prevKey for null, isso significa que alcançamos
                // fim da paginação para anexar.
                val nextKey = remoteKeys?.nextOffset
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        return try {
            val apiResponse = pokeService.getListPokemon(offset = offset)
            val listaPokemon = apiResponse.results
            val endOfPaginationReached = listaPokemon.isEmpty()

            coroutineScope {
                withContext(IO){
//                if (loadType == LoadType.REFRESH) { // se o aplicativo foi aberto
//                    pokemonDatabase.pokemonDAO().limparFavoritos()
//                    pokemonDatabase.chavesRemotasDAO().clearRemoteKeys()
//                }

                    val prevKey = if (offset == PRIMEIRO_DESLOCAMENTO_OFFSET) null else offset - 10
                    val nextKey = if (endOfPaginationReached) null else offset + 10

                    val entidades = listaPokemon.map {
                        val idPokemon = getIdPokemon(it.url)
                        val pokemonFavoritoEncontrado = pokemonFavoritoDAO.buscarFavorito(idPokemon)

                        EntidadePokemon(id = idPokemon, name = it.name, url = it.url, favorito = pokemonFavoritoEncontrado != null)
                    }
                    val chavesRemotas = listaPokemon.map {
                        ChaveRemota(nomePokemon = it.name, nextOffset = nextKey, prevOffset = prevKey)
                    }
                    chavesRemotasDAO.insertAll(chavesRemotas)
                    pokemonDAO.salvarTodos(entidades)
                }
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private fun getIdPokemon(urlPokemon: String): Int {
        val regex = "/\\d+".toRegex()
        return regex.find(urlPokemon)?.value?.replace("/","")?.toInt() ?: 0
    }

    private suspend fun obterChaveRemotaParaOPrimeiroItem(state: PagingState<Int, EntidadePokemon>): ChaveRemota? {
        // Obtenha a primeira página que foi recuperada, que continha itens.
        // Nessa primeira página, pegue o primeiro item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { pokemon ->
                // Obtenha as chaves remotas dos primeiros itens recuperados
                chavesRemotasDAO.remoteKeysPokemonName(pokemon.name)
            }
    }

    private suspend fun obterChaveRemotaMaisProximaDaPosicaoAtual(
        state: PagingState<Int, EntidadePokemon>
    ): ChaveRemota? {
        // A biblioteca de paginação está tentando carregar dados após a posição da âncora
        // Obtenha o item mais próximo da posição âncora
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.name?.let { nomePokemon ->
                chavesRemotasDAO.remoteKeysPokemonName(nomePokemon)
            }
        }
    }

    private suspend fun obterChaveRemotaParaUltimoItem(state: PagingState<Int, EntidadePokemon>): ChaveRemota? {
        // Obtenha a última página que foi recuperada, que continha itens.
        // A partir dessa última página, pegue o último item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { pokemon ->
                // Obtenha as chaves remotas do último item recuperado
                chavesRemotasDAO.remoteKeysPokemonName(pokemon.name)
            }
    }

    companion object {
        const val PRIMEIRO_DESLOCAMENTO_OFFSET = 0
    }
}