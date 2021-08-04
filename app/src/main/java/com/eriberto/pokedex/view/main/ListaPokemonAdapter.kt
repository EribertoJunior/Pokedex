package com.eriberto.pokedex.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eriberto.pokedex.util.GlideResquestListener
import com.eriberto.pokedex.R
import com.eriberto.pokedex.repository.model.PokemonData
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.button.MaterialButton

class ListaPokemonAdapter(private val onClickListener: OnClickListener) :
    PagingDataAdapter<PokemonData, ListaPokemonAdapter.MeuViewHolder>(DiffUtilCallBack()) {

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
        return regex.find(urlPokemon)?.value?.replace("/","")?.toInt() ?: 0
    }

    inner class MeuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvNomePokemon = view.findViewById<TextView>(R.id.tvNomePokemon)
        private val btDetalhesPokemon = view.findViewById<MaterialButton>(R.id.btDetalhesPokemon)
        private val ivPokemon = view.findViewById<ImageView>(R.id.imagemDoPokemonItem)

        private val shimerConteiner = view.findViewById<ShimmerFrameLayout>(R.id.shimerConteiner)
        fun bind(item: PokemonData) {
            tvNomePokemon.text = item.name
            btDetalhesPokemon.setOnClickListener { onClickListener.itemClick(getIdPokemon(item.url), item) }
            shimerConteiner.startShimmer()
            Glide.with(ivPokemon)
                .load(ivPokemon.context.getString(R.string.url_imagem, getIdPokemon(item.url)))
                .placeholder(R.mipmap.pokeball_loading)
                .error(R.mipmap.pokeboll_error)
                .listener(GlideResquestListener(shimerConteiner))
                .into(ivPokemon)
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<PokemonData>() {

        override fun areItemsTheSame(oldItem: PokemonData, newItem: PokemonData): Boolean {
            return oldItem.name == newItem.name
        }
        override fun areContentsTheSame(oldItem: PokemonData, newItem: PokemonData): Boolean {
            return oldItem == newItem
        }
    }

    interface OnClickListener {
        fun itemClick(idPokemon: Int, pokemonData: PokemonData)
    }

}