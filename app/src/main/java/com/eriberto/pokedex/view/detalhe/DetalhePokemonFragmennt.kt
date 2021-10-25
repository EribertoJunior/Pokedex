package com.eriberto.pokedex.view.detalhe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.eriberto.pokedex.R
import com.eriberto.pokedex.databinding.FragmentDetalhePokemonBinding
import com.eriberto.pokedex.data.remote.network.model.RetornoPokemonDetalhe
import com.eriberto.pokedex.data.remote.network.STATUS_RESULT
import com.eriberto.pokedex.util.GlideResquestListener
import com.eriberto.pokedex.viewmodel.detalhe.DetalhePokemonViewModel
import com.eriberto.pokedex.viewmodel.detalhe.model.PokeDetalheData
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetalhePokemonFragmennt : Fragment() {

    private var _binding: FragmentDetalhePokemonBinding? = null
    private val binding get() = _binding!!

    private val arguments by navArgs<DetalhePokemonFragmenntArgs>()
    private val idPokemon by lazy { arguments.idPokemon }
    private val nomePokemon by lazy { arguments.nomePokemon }

    private val viewModel: DetalhePokemonViewModel by viewModel()
    private val tipoPokemonAdapter: TipoPokemonAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val animate = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animate
        sharedElementReturnTransition = animate

        initObserver()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetalhePokemonBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setTransitionName(binding.ivPokemonDetalhe, CHAVE_IMAGE_POKEMON_DETALHE)
        showNamePokemon()
        showImagePokemon()
        configRecyclerView()
    }

    private fun showImagePokemon() {
        context?.let {
            Glide.with(it)
                .load(getString(R.string.url_imagem, idPokemon))
                .placeholder(R.mipmap.pokeball_loading)
                .error(R.mipmap.falha_no_carregamento)
                .centerInside()
                .listener(GlideResquestListener(binding.shimerConteinerDetalhe))
                .into(binding.ivPokemonDetalhe)
        }
    }

    private fun showNamePokemon() {
        binding.collapsingToolbarDetalhesPokemon.title = nomePokemon
    }

    private fun initObserver() {
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

        viewModel.isFavorite(idPokemon).observe(this, { pokemonFavorito ->
            if (pokemonFavorito != null) {
                goldenIconSet()
            } else {
                blackIconSet()
            }
        })
    }

    private fun exibirDetalhes(retornoPokemonDetalhe: RetornoPokemonDetalhe?) {
        esconderProgressBar()

        retornoPokemonDetalhe?.let {
            val abilitiesNames = preparaListaDeHabilidades(it)

            binding.detalhesPokemonContainer.apply {
                tvHabilidade.text = abilitiesNames
                tvAltura.text = calculaAlturaDoPokemon(it.height)
                tvPeso.text = calculaPesoDoPokemon(it.weight)
                tvTitleAbilities.text =
                    resources.getQuantityText(R.plurals.habilidades, it.abilities.size)

                tipoPokemonAdapter.submitData(retornoPokemonDetalhe.types)
            }
        }
    }

    private fun calculaPesoDoPokemon(peso: Double) =
        (peso / 10).toString().plus(" kg")

    private fun calculaAlturaDoPokemon(altura: Double) =
        (altura / 10).toString().plus(" m")

    private fun preparaListaDeHabilidades(it: RetornoPokemonDetalhe): String {
        var abilitiesNames = ""
        val tamanhoDaLista = it.abilities.size - 1
        it.abilities.forEachIndexed { index, abilitySlot ->
            abilitiesNames += abilitySlot.ability.name
            if (index < tamanhoDaLista) {
                abilitiesNames += "\n"
            }
        }
        return abilitiesNames
    }

    private fun configRecyclerView() {
        binding.detalhesPokemonContainer.recyclerViewTipoPokemon.apply {
            adapter = tipoPokemonAdapter
        }
    }

    private fun exibirMensagemDeErro(pokeDetalheData: PokeDetalheData) {
        esconderProgressBar()
        Toast.makeText(context, pokeDetalheData.errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun esconderProgressBar() {
        binding.detalhesPokemonContainer.progressBarTelaDetalhe.visibility = View.GONE
    }

    private fun blackIconSet() {
        binding.fbFavoritar.apply {
            setImageResource(R.drawable.star_outline)
            imageTintList =
                ContextCompat.getColorStateList(context, R.color.black)
            setOnClickListener { viewModel.favoritarPokemon(idPokemon, nomePokemon) }
        }
    }

    private fun goldenIconSet() {
        binding.fbFavoritar.apply {
            setImageResource(R.drawable.star)
            imageTintList =
                ContextCompat.getColorStateList(context, R.color.yellow)
            setOnClickListener { viewModel.desfavoritarPokemon(idPokemon, nomePokemon) }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val CHAVE_IMAGE_POKEMON_DETALHE: String = "image_deteil"
    }
}