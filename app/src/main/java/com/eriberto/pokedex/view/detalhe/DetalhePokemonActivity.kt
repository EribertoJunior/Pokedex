package com.eriberto.pokedex.view.detalhe

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.eriberto.pokedex.R
import com.eriberto.pokedex.repository.model.PokeDetalhe
import com.eriberto.pokedex.repository.network.STATUS_RESULT
import com.eriberto.pokedex.util.GlideResquestListener
import com.eriberto.pokedex.view.main.MainActivity.Companion.ID_POKEMON
import com.eriberto.pokedex.view.main.MainActivity.Companion.NOME_POKEMON
import com.eriberto.pokedex.viewmodel.DetalhePokemonViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetalhePokemonActivity : AppCompatActivity() {

    private val viewModel: DetalhePokemonViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_pokemon)


        if (intent.hasExtra(ID_POKEMON) && intent.hasExtra(NOME_POKEMON)) {
            val idPokemon = intent.getIntExtra(ID_POKEMON, 0)
            val nomePokemon = intent.getStringExtra(NOME_POKEMON)
            showNomePokemon(nomePokemon)
            showImagePokemon(idPokemon)
            initObserver(idPokemon)
        }

        configuraBotaoFavoritar()

    }

    private fun configuraBotaoFavoritar() {
        val fbFavoritar: FloatingActionButton = findViewById(R.id.fb_favoritar)
        var boolean = true
        fbFavoritar.setOnClickListener {
            if(boolean){
                fbFavoritar.apply {
                    setImageResource(R.drawable.star)
                    imageTintList = ContextCompat.getColorStateList(applicationContext, R.color.yellow)
                }
            }else{
                fbFavoritar.apply {
                    setImageResource(R.drawable.star_outline)
                    imageTintList = ContextCompat.getColorStateList(applicationContext, R.color.black)
                }
            }
            boolean = !boolean
        }
    }

    private fun showImagePokemon(idPokemon: Int) {
        val ivPokemon: ImageView = findViewById(R.id.ivPokemonDetalhe)
        val shimerConteiner = findViewById<ShimmerFrameLayout>(R.id.shimerConteinerDetalhe)
        Glide.with(ivPokemon)
            .load(getString(R.string.url_imagem, idPokemon))
            .placeholder(R.mipmap.pokeball_loading)
            .error(R.mipmap.pokeboll_error)
            .listener(GlideResquestListener(shimerConteiner))
            .into(ivPokemon)
    }

    private fun showNomePokemon(nomePokemon: String?) {
        val collapsingToolbar: CollapsingToolbarLayout = findViewById(R.id.collapsing_toolbar)
        collapsingToolbar.title = nomePokemon
    }

    private fun initObserver(idPokemon: Int) {
        viewModel.getDetalhesPokemon(idPokemon)
        viewModel.detalhePokemonData.observe(this, {
            when (it.statusResult) {
                STATUS_RESULT.Success -> {
                    exibirDetalhes(it.pokeDetalhe)
                }
                STATUS_RESULT.Error -> {
                    exibirMensagemDeErro(it)
                }
            }
        })
    }

    private fun exibirMensagemDeErro(it: DetalhePokemonViewModel.PokeDetalheData) {
        esconderProgressBar()
        Toast.makeText(this, it.errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun esconderProgressBar() {
        val progressBarTelaDetalhe: ProgressBar = findViewById(R.id.progressBarTelaDetalhe)
        progressBarTelaDetalhe.visibility = View.GONE
    }

    private fun exibirDetalhes(pokeDetalhe: PokeDetalhe?) {
        val tvTitleAbilities: TextView = findViewById(R.id.tvTitleAbilities)
        val tvHabilidade: TextView = findViewById(R.id.tvHabilidade)
        val tvAltura: TextView = findViewById(R.id.tvAltura)
        val tvPeso: TextView = findViewById(R.id.tvPeso)

        esconderProgressBar()

        pokeDetalhe?.let {
            var abilitiesNames = ""
            val tamanhoDaLista = it.abilities.size - 1
            it.abilities.forEachIndexed { index, abilitySlot ->
                abilitiesNames += abilitySlot.ability.name
                if (index < tamanhoDaLista){
                    abilitiesNames += "\n"
                }
            }

            tvHabilidade.text = abilitiesNames
            tvAltura.text = (it.height / 10).toString().plus(" m")
            tvPeso.text = (it.weight / 10).toString().plus(" kg")
        }

    }
}