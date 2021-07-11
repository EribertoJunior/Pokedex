package com.eriberto.pokedex.view.detalhe

import com.eriberto.pokedex.R

enum class POKEMON_TYPE(val typeName: String, val backgroundColorId: Int) {
    BUG("bug", R.color.bug),
    DARK("dark", R.color.dark),
    DRAGON("dragon", R.color.dragon),
    ELECTRIC("electric", R.color.electric),
    FAIRY("fairy", R.color.fairy),
    FIGHTING("fighting", R.color.fighting),
    FIRE("fire", R.color.fire),
    FLYING("flying", R.color.flying),
    GHOST("ghost", R.color.ghost),
    GRASS("grass", R.color.grass),
    GROUND("ground", R.color.ground),
    ICE("ice", R.color.ice),
    NORMAL("normal", R.color.normal),
    POISON("poison", R.color.poison),
    PSYCHIC("psychic", R.color.psychic),
    ROCK("rock", R.color.rock),
    STEEL("steel", R.color.steel),
    WATER("water", R.color.water);

    companion object {
        fun getType(typeName: String): POKEMON_TYPE = values().first { it.typeName == typeName }
    }
}