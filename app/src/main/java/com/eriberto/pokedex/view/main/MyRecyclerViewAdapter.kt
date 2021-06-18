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

class MyRecyclerViewAdapter(private val onClickListener: OnClickListener) :
    PagingDataAdapter<PokemonData, MyRecyclerViewAdapter.MeuViewHolder>(DiffUtilCallBack()) {

    inner class MeuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvNomePokemon = view.findViewById<TextView>(R.id.tvNomePokemon)
        private val tvDetalhesPokemon = view.findViewById<TextView>(R.id.tvDetalhesPokemon)
        private val ivPokemon = view.findViewById<ImageView>(R.id.ivPokemon)
        private val ivEstrela = view.findViewById<ImageView>(R.id.ivEstrela)
        private val shimerConteiner = view.findViewById<ShimmerFrameLayout>(R.id.shimerConteiner)

        fun bind(item: PokemonData) {
            tvNomePokemon.text = item.name
            tvDetalhesPokemon.setOnClickListener { onClickListener.itemClick(item) }
            shimerConteiner.startShimmer()
            Glide.with(ivPokemon)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${getIdPokemon(item.url)}.png")
                .error(R.drawable.pokeboll_error)
                .placeholder(R.drawable.pokeball_loading)
                .listener(GlideResquestListener(shimerConteiner))
                .circleCrop()
                .into(ivPokemon)
        }
    }

    private fun getIdPokemon(urlPokemon: String): String {
        val regex = "/\\d{1,5}".toRegex()
        return regex.find(urlPokemon)?.value?.replace("/","")?: ""
    }

    override fun onBindViewHolder(holder: MeuViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeuViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.poke_data_item, parent, false)
        return MeuViewHolder(inflate)
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
        fun itemClick(item: PokemonData)
    }

}