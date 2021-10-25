package com.eriberto.pokedex.view.detalhe

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.eriberto.pokedex.R
import com.eriberto.pokedex.data.remote.network.model.Type
import com.eriberto.pokedex.data.remote.network.model.TypeSlot

class TipoPokemonAdapter : RecyclerView.Adapter<TipoPokemonAdapter.TipoPokemonViewHolder>() {

    private var listTypeSlot: MutableList<TypeSlot> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipoPokemonViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.type_pokemon_item, parent, false)
        return TipoPokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: TipoPokemonViewHolder, position: Int) {
        listTypeSlot[position].let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = listTypeSlot.size

    fun submitData(types: List<TypeSlot>) {
        listTypeSlot.addAll(types)
        notifyDataSetChanged()
    }

    inner class TipoPokemonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageType: ImageView = view.findViewById(R.id.imageView_type)

        fun bind(item: TypeSlot) {
            val context = imageType.context
            val idDrawable: Int = getDrawableId(context, item.type)
            val backgroundColorId: Int? = getColorId(context, item)

            imageType.apply {
                backgroundColorId?.let {
                    backgroundTintList = ContextCompat.getColorStateList(context, backgroundColorId)
                }
                setImageResource(idDrawable)
            }
        }

        private fun getColorId(context: Context, item: TypeSlot): Int? {
            return item.type.name?.let { getResourceColor(context, it) }
        }

        private fun getDrawableId(context: Context, item: Type): Int {
            return context.resources.getIdentifier(
                "ic_${item.name}",
                "drawable",
                context.packageName
            )
        }

        private fun getResourceColor(context: Context, colorName: String): Int {
            return context.resources.getIdentifier(
                colorName,
                "color",
                context.packageName
            )
        }
    }
}