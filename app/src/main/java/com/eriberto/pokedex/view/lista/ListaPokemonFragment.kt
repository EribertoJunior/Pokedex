package com.eriberto.pokedex.view.lista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.eriberto.pokedex.databinding.FragmentListPokemonBinding
import com.eriberto.pokedex.viewmodel.ListaPokemonViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListaPokemonFragment : Fragment() {

    private var _binding: FragmentListPokemonBinding? = null
    private val binding get() = _binding

    private val viewModel: ListaPokemonViewModel by viewModel()
    private val listaPokemonAdapter: ListaPokemonAdapter by inject()
    private val controller by lazy { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configObcerver()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListPokemonBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configRecyclerView()
        configLoadState()
        configRefreshButton()
        configItemClick()
    }

    private fun configObcerver() {
        lifecycleScope.launchWhenCreated {
            viewModel.getPokemonStream().collectLatest {
                listaPokemonAdapter.submitData(it)

                /*(view?.parent as? ViewGroup)?.doOnPreDraw {
                    startPostponedEnterTransition()
                }*/
            }
        }
    }

    private fun configLoadState() {
        lifecycleScope.launch {
            listaPokemonAdapter.loadStateFlow.collectLatest { loadState ->
                binding?.apply {
                    progressBarListaPokemon.isVisible = loadState.refresh is LoadState.Loading
                    recyclerViewListaPokemon.isVisible = loadState.refresh !is LoadState.Loading
                    telaDeFalha.telaDeFalhaConteiner.isVisible =
                        loadState.refresh is LoadState.Error
                }
            }
        }
    }

    private fun configItemClick() {
        listaPokemonAdapter.onItemClickListener = { idPokemon: Int, nomePokemon: String, extra: FragmentNavigator.Extras ->
            val direction =
                ListaPokemonFragmentDirections.actionListaPokemonFragmentToDetalhePokemonFragmennt(
                    idPokemon,
                    nomePokemon
                )
            controller.navigate(direction, extra)
        }
    }

    private fun configRefreshButton() {
        binding?.telaDeFalha?.botaoTentarNovamente?.setOnClickListener {
            listaPokemonAdapter.refresh()
        }
    }

    private fun configRecyclerView() {
        binding?.recyclerViewListaPokemon?.apply {
            adapter = listaPokemonAdapter
            layoutManager = LinearLayoutManager(context)
            postponeEnterTransition()
            viewTreeObserver.addOnPreDrawListener {
                startPostponedEnterTransition()
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}