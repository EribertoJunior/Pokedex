package com.eriberto.pokedex.util

import androidx.test.platform.app.InstrumentationRegistry

object FileReader {

    fun readStringFromFile(fileName:String): String {
        val assets = InstrumentationRegistry.getInstrumentation().context.assets
        return assets.open(fileName).reader().readText()
    }
}