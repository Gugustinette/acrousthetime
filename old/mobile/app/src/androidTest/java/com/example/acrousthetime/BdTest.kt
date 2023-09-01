package com.example.acrousthetime

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.acrousthetime.bd.CrudToken
import com.example.acrousthetime.bd.StructureToken
import com.example.acrousthetime.bd.Token
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BdTest {
    private lateinit var crudToken: CrudToken

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        crudToken = CrudToken(appContext)
    }

    @After
    fun tearDown() {
        val dbHelper = StructureToken(
            ApplicationProvider.getApplicationContext<Context>(),
            CrudToken.NOM_BDD,
            null,
            1
        )
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM ${StructureToken.TABLE_SERVEUR}")
        db.close()
    }

    @Test
    fun testInsertAndGetToken() {
        val token = Token("exampleToken")
        val insertedId = crudToken.insertToken(token)
        assertEquals(1, insertedId)

        val retrievedToken = crudToken.getToken
        assertEquals(token.id, retrievedToken.id)
    }

    @Test
    fun testDeleteToken() {
        val token = Token("exampleToken")
        crudToken.insertToken(token)

        val deletedRows = crudToken.deleteToken()
        assertEquals(1, deletedRows)

        val retrievedToken = crudToken.getToken
        assertEquals("", retrievedToken.id)
    }
}