package com.eriberto.pokedex.view.lista

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eriberto.pokedex.util.GlideResquestListener
import com.eriberto.pokedex.R
import com.eriberto.pokedex.data.local.database.model.EntidadePokemon
import com.eriberto.pokedex.view.detalhe.DetalhePokemonFragmennt.Companion.CHAVE_IMAGE_POKEMON_DETALHE
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton

class ListaPokemonAdapter(
    var onItemClickListener: (idPokemon: Int, nomePokemon: String, extras: FragmentNavigator.Extras) -> Unit = { _, _, _ -> }
) :
    PagingDataAdapter<EntidadePokemon, ListaPokemonAdapter.MeuViewHolder>(POKEMON_COMPARATOR) {

    override fun onBindViewHolder(holder: MeuViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeuViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.poke_data_item, parent, false)
        return MeuViewHolder(inflate)
    }

    private fun getIdPokemon(urlPokemon: String): Int {
        val regex = "/\\d+".toRegex()
        return regex.find(urlPokemon)?.value?.replace("/", "")?.toInt() ?: 0
    }

    inner class MeuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvNomePokemon = view.findViewById<TextView>(R.id.tvNomePokemon)
        private val btDetalhesPokemon = view.findViewById<MaterialButton>(R.id.btDetalhesPokemon)
        private val ivPokemon = view.findViewById<ImageView>(R.id.imagemDoPokemonItem)
        private val ivEstrela = view.findViewById<ImageView>(R.id.ivEstrela)

        private val shimerConteiner = view.findViewById<ShimmerFrameLayout>(R.id.shimerConteiner)

        private lateinit var entidadePokemon: EntidadePokemon

        init {
            btDetalhesPokemon.setOnClickListener {
                if (::entidadePokemon.isInitialized) {
                    val extras = FragmentNavigatorExtras(ivPokemon to CHAVE_IMAGE_POKEMON_DETALHE)
                    onItemClickListener(
                        getIdPokemon(entidadePokemon.url),
                        entidadePokemon.name,
                        extras
                    )
                }
            }
        }

        fun bind(item: EntidadePokemon) {
            entidadePokemon = item
            tvNomePokemon.text = item.name
            ViewCompat.setTransitionName(ivPokemon, item.name)

            shimerConteiner.startShimmer()

            ivEstrela.isVisible = item.favorito

            Glide.with(ivPokemon)
                .load(ivPokemon.context.getString(R.string.url_imagem, getIdPokemon(item.url)))
                .placeholder(R.mipmap.pokeball_loading)
                .centerInside()
                .error(R.mipmap.pokeboll_error)
                .listener(GlideResquestListener(shimerConteiner))
                .into(ivPokemon)
        }
    }

    companion object {
        private val POKEMON_COMPARATOR = object : DiffUtil.ItemCallback<EntidadePokemon>() {

            override fun areItemsTheSame(
                oldItem: EntidadePokemon,
                newItem: EntidadePokemon
            ): Boolean {
                return oldItem.name == newItem.name
            }

            override fun areContentsTheSame(
                oldItem: EntidadePokemon,
                newItem: EntidadePokemon
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    interface OnClickListener {
        fun itemClick(idPokemon: Int, pokemon: EntidadePokemon)
    }

}