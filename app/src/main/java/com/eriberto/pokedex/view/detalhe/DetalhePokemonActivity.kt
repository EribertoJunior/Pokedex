package com.eriberto.pokedex.view.detalhe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eriberto.pokedex.R
import com.eriberto.pokedex.repository.network.STATUS_RESULT
import com.eriberto.pokedex.view.main.MainActivity.Companion.ID_POKEMON
import com.eriberto.pokedex.viewmodel.DetalhePokemonViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetalhePokemonActivity : AppCompatActivity() {

    val viewModel: DetalhePokemonViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_pokemon)

        if (intent.hasExtra(ID_POKEMON)) {
            val idPokemon = intent.getIntExtra(ID_POKEMON, 0)
            initObserver(idPokemon)
        }

    }

    private fun initObserver(idPokemon: Int) {
        viewModel.getDetalhesPokemon(idPokemon = idPokemon).observe(this, {
            when (it.statusResult) {
                STATUS_RESULT.Success -> {
                    Toast.makeText(this, it.pokeDetalhe?.name+" >>>", Toast.LENGTH_SHORT).show()
                }
                STATUS_RESULT.Error -> {
                    Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}