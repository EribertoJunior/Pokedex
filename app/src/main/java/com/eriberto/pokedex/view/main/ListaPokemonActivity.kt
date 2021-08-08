package com.eriberto.pokedex.view.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eriberto.pokedex.R
import com.eriberto.pokedex.repository.database.model.EntidadePokemon
import com.eriberto.pokedex.repository.model.Pokemon
import com.eriberto.pokedex.view.detalhe.DetalhePokemonActivity
import com.eriberto.pokedex.viewmodel.ListaPokemonViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListaPokemonActivity : AppCompatActivity() {

    private lateinit var myAdapter: ListaPokemonAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView

    private val viewModel: ListaPokemonViewModel by viewModel()

    companion object{
        const val ID_POKEMON = "idPokemon"
        const val POKEMON_SELECIONADO = "nomePokemon"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        initObserverLoadState()
        initViewModel()
        initClickDoBotaoTentarNovamente()
    }

    private fun initClickDoBotaoTentarNovamente() {
        val btTentarNovamente : Button= findViewById(R.id.botaoTentarNovamente)
        btTentarNovamente.setOnClickListener {
            initViewModel()
        }
    }

    private fun initViewModel() {
        lifecycleScope.launchWhenCreated {
            viewModel.getPokemonStream().collectLatest {
                myAdapter.submitData(it)
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewListaPokemon)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ListaPokemonActivity)

            myAdapter = ListaPokemonAdapter(object : ListaPokemonAdapter.OnClickListener {
                override fun itemClick(idPokemon: Int, pokemon: EntidadePokemon) {
                    irParaTelaDeDetalhes(idPokemon, pokemon)
                }
            })
            adapter = myAdapter
        }

    }

    private fun irParaTelaDeDetalhes(idPokemon: Int, pokemon: EntidadePokemon) {
        startActivity(
            Intent(this, DetalhePokemonActivity::class.java)
                .putExtra(ID_POKEMON, idPokemon)
                .putExtra(POKEMON_SELECIONADO, pokemon)
        )
    }

    private fun initObserverLoadState() {
        progressBar = findViewById(R.id.progressBarListaPokemon)
        val telaDeFalha: View = findViewById(R.id.tela_de_falha)
        lifecycleScope.launch {
            myAdapter.loadStateFlow.collectLatest { loadState ->
                progressBar.isVisible = loadState.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.refresh !is LoadState.Loading
                telaDeFalha.isVisible = loadState.refresh is LoadState.Error

            }
        }
    }
}