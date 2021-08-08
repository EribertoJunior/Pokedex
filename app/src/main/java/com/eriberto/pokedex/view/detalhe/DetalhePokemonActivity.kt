package com.eriberto.pokedex.view.detalhe

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eriberto.pokedex.R
import com.eriberto.pokedex.repository.database.model.EntidadePokemon
import com.eriberto.pokedex.repository.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.repository.model.TypeSlot
import com.eriberto.pokedex.repository.network.STATUS_RESULT
import com.eriberto.pokedex.util.GlideResquestListener
import com.eriberto.pokedex.view.main.ListaPokemonActivity.Companion.ID_POKEMON
import com.eriberto.pokedex.view.main.ListaPokemonActivity.Companion.POKEMON_SELECIONADO
import com.eriberto.pokedex.viewmodel.detalhe.DetalhePokemonViewModel
import com.eriberto.pokedex.viewmodel.detalhe.model.PokeDetalheData
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetalhePokemonActivity : AppCompatActivity() {

    private val fbFavoritar: FloatingActionButton by lazy {
        findViewById(R.id.fb_favoritar)
    }

    private val viewModel: DetalhePokemonViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_pokemon)

        if (intent.hasExtra(ID_POKEMON) && intent.hasExtra(POKEMON_SELECIONADO)) {
            val idPokemon = intent.getIntExtra(ID_POKEMON, 0)
            val pokemonRecebido = intent.getSerializableExtra(POKEMON_SELECIONADO) as EntidadePokemon
            showNomePokemon(pokemonRecebido.name)
            showImagePokemon(idPokemon)
            initDetalhesObserver(idPokemon)
            initFavoritesObserver(pokemonRecebido)
        }
    }

    private fun initFavoritesObserver(pokemonRecebido: EntidadePokemon) {
        viewModel.isFavorite(pokemonRecebido.id).observe(this, { pokemonFavorito ->
            if (pokemonFavorito != null) {
                goldenIconSet(pokemonRecebido)
            } else {
                backIconSet(pokemonRecebido)
            }
        })
    }

    private fun backIconSet(pokemonRecebido: EntidadePokemon) {
        fbFavoritar.apply {
            setImageResource(R.drawable.star_outline)
            imageTintList =
                ContextCompat.getColorStateList(applicationContext, R.color.black)
            setOnClickListener { viewModel.favoritarPokemon(pokemonRecebido) }
        }
    }

    private fun goldenIconSet(pokemonRecebido: EntidadePokemon) {
        fbFavoritar.apply {
            setImageResource(R.drawable.star)
            imageTintList =
                ContextCompat.getColorStateList(applicationContext, R.color.yellow)
            setOnClickListener { viewModel.desfavoritarPokemon(pokemonRecebido) }

        }
    }

    private fun initDetalhesObserver(idPokemon: Int) {
        viewModel.getDetalhesPokemon(idPokemon)
        viewModel.detalhePokemonData.observe(this, {
            when (it.statusResult) {
                STATUS_RESULT.Success -> {
                    exibirDetalhes(it.retornoPokemonDetalhe)
                }
                STATUS_RESULT.Error -> {
                    exibirMensagemDeErro(it)
                }
            }
        })
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
        val collapsingToolbar: CollapsingToolbarLayout =
            findViewById(R.id.collapsing_toolbar_detalhes_pokemon)
        collapsingToolbar.title = nomePokemon
    }

    private fun exibirMensagemDeErro(pokeDetalheData: PokeDetalheData) {
        esconderProgressBar()
        Toast.makeText(this, pokeDetalheData.errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun esconderProgressBar() {
        val progressBarTelaDetalhe: ProgressBar = findViewById(R.id.progressBarTelaDetalhe)
        progressBarTelaDetalhe.visibility = View.GONE
    }

    private fun exibirDetalhes(retornoPokemonDetalhe: RetornoPokemonDetalhe?) {
        val tvTitleAbilities: TextView = findViewById(R.id.tvTitleAbilities)
        val tvHabilidade: TextView = findViewById(R.id.tvHabilidade)
        val tvAltura: TextView = findViewById(R.id.tvAltura)
        val tvPeso: TextView = findViewById(R.id.tvPeso)

        esconderProgressBar()

        retornoPokemonDetalhe?.let {
            var abilitiesNames = ""
            val tamanhoDaLista = it.abilities.size - 1
            it.abilities.forEachIndexed { index, abilitySlot ->
                abilitiesNames += abilitySlot.ability.name
                if (index < tamanhoDaLista) {
                    abilitiesNames += "\n"
                }
            }
            configuraRecyclerView(it.types)

            tvHabilidade.text = abilitiesNames
            tvAltura.text = (it.height / 10).toString().plus(" m")
            tvPeso.text = (it.weight / 10).toString().plus(" kg")
            tvTitleAbilities.text =
                resources.getQuantityText(R.plurals.habilidades, it.abilities.size)
        }

    }

    private fun configuraRecyclerView(types: List<TypeSlot>) {
        val recyclerViewTipoPokemon: RecyclerView = findViewById(R.id.recyclerViewTipoPokemon)
        recyclerViewTipoPokemon.apply {
            adapter = TipoPokemonAdapter(types)
        }
    }
}