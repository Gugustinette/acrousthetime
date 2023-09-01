package com.example.acrousthetime

import com.example.wezer.http.Api
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiTest {
    @Before
    fun init_API() {
        Api.MODE = "offline"
        if (Api.MODE == "online") {
            Api.register("test", "test", "test@test.fr","P@ssw0rd")
            Api.authenticate("test@test.fr", "P@ssw0rd")
        }
    }
    @Test
    fun etudiants_get() {
        val etudiants = Api.getEtudiantList()
        assertNotEquals(etudiants, null)
        assertNotEquals(etudiants.size, 0)
    }
}