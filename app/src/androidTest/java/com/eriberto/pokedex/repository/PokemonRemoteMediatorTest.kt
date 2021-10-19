package com.eriberto.pokedex.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.eriberto.pokedex.repository.database.config.PokemonDatabase
import com.eriberto.pokedex.util.MockWebServerUtil
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PokemonRemoteMediatorTest {

    private lateinit var mockWebServer: MockWebServer

    private val mockDB: PokemonDatabase =
        Room.inMemoryDatabaseBuilder(getApplicationContext(), PokemonDatabase::class.java).build()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)

        MockWebServerUtil.registraOkHttp()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        mockDB.clearAllTables()
    }

}