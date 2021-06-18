package com.eriberto.pokedex.view.main

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eriberto.pokedex.viewmodel.MainActivityViewModel
import com.eriberto.pokedex.R
import com.eriberto.pokedex.repository.model.PokemonData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: MyRecyclerViewAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
        initObserverLoadState()
        initViewModel()
    }

    private fun initViewModel() {
        val viewModel: MainActivityViewModel by viewModel()
        lifecycleScope.launchWhenCreated {
            viewModel.getListPokemon().collectLatest {
                myAdapter.submitData(it)
            }
        }
    }

    private fun initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)

            myAdapter = MyRecyclerViewAdapter(object : MyRecyclerViewAdapter.OnClickListener {
                override fun itemClick(item: PokemonData) {
                    Toast.makeText(this@MainActivity, item.name, Toast.LENGTH_LONG).show()
                }
            })
            adapter = myAdapter
        }

    }

    private fun initObserverLoadState() {
        progressBar = findViewById(R.id.progressBar)
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