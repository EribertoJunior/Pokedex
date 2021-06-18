package com.eriberto.pokedex.view.main

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eriberto.pokedex.viewmodel.MainActivityViewModel
import com.eriberto.pokedex.R
import com.eriberto.pokedex.repository.model.PokemonData
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: MyRecyclerViewAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)

        initRecyclerView()
        initViewModel()
    }

    private fun initViewModel() {
        val viewModel: MainActivityViewModel by viewModel()
        lifecycleScope.launchWhenCreated {
            viewModel.getListPokemon().collectLatest {
                progressBar.visibility = View.GONE
                myAdapter.submitData(it)
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

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
}