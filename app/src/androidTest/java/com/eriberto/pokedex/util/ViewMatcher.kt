package com.eriberto.pokedex.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.eriberto.pokedex.R
import com.google.android.material.appbar.CollapsingToolbarLayout
import org.hamcrest.Description
import org.hamcrest.Matcher

object ViewMatcher {
    fun apareceNoCollapsibleToolbarOTitilo(textMatcher: String): Matcher<in View> {
        return object :
            BoundedMatcher<View, CollapsingToolbarLayout>(CollapsingToolbarLayout::class.java) {
            override fun describeTo(description: Description?) {
                description!!.appendText("Título da barra de ferramentas com descrição: ")
                    .appendValue(textMatcher)
                    .appendText(", não foi encontrado. ")
            }

            override fun matchesSafely(toolbarLayout: CollapsingToolbarLayout?): Boolean {
                return textMatcher == toolbarLayout?.title
            }

        }
    }

    fun apareceTipoPokemonNaPosicao(position: Int): Matcher<in View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description?) {
                description!!.appendText("View na posição ").appendValue(position)
                    .appendText(" não foi encontrada. ")
                description.appendDescriptionOf(isDisplayed())
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                val viewHolderDevolvido: RecyclerView.ViewHolder =
                    item?.findViewHolderForAdapterPosition(position)
                        ?: throw IndexOutOfBoundsException("View do ViewHolder na posição $position não foi encontrada.")

                val viewDoViewHolder:View = viewHolderDevolvido.itemView
                return isDisplayed().matches(viewDoViewHolder)
            }

        }
    }

    fun aparecePokemonNaPosicao(position: Int, namePokemon: String): Matcher<in View> {
        return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {

            private val displayed: Matcher<View> = isDisplayed()

            override fun describeTo(description: Description?) {
                description!!.appendText("View com descrição ").appendValue(namePokemon)
                    .appendText(" na posição ").appendValue(position)
                    .appendText(" não foi encontrada. ")
                description.appendDescriptionOf(displayed)
            }

            override fun matchesSafely(item: RecyclerView?): Boolean {
                val viewHolderDevolvido: RecyclerView.ViewHolder =
                    item?.findViewHolderForAdapterPosition(position)
                        ?: throw IndexOutOfBoundsException("View do ViewHolder na posição $position não foi encontrada.")

                val viewDoViewHolder: View = viewHolderDevolvido.itemView
                val temNomeEsperado = apareceNomeEsperado(viewDoViewHolder)
                val temImagemEstaVisivel = apareceImagemDoPokemon(viewDoViewHolder)

                return temNomeEsperado && temImagemEstaVisivel && displayed.matches(viewDoViewHolder)
            }

            private fun apareceImagemDoPokemon(viewDoViewHolder: View): Boolean {
                val imagemDoPokemonItem =
                    viewDoViewHolder.findViewById<ImageView>(R.id.imagemDoPokemonItem)
                return displayed.matches(imagemDoPokemonItem)
            }

            private fun apareceNomeEsperado(viewDoViewHolder: View): Boolean {
                val tvNomePokemon: TextView = viewDoViewHolder.findViewById(R.id.tvNomePokemon)
                return tvNomePokemon.text.toString() == namePokemon && displayed.matches(
                    tvNomePokemon
                )
            }
        }
    }
}