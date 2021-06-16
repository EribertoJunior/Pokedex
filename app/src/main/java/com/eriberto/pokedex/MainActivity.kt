package com.eriberto.pokedex

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eriberto.pokedex.model.PokemonData
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var myAdapter: MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initRecyclerView()
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