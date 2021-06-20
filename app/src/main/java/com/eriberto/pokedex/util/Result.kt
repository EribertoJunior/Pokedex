package com.eriberto.pokedex.util

interface Result<R> {
    fun success(data: R)
    fun error(errorMessage: String)
}