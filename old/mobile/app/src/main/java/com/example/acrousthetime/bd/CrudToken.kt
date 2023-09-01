package com.example.acrousthetime.bd

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class CrudToken (val ctx : Context){
    private var mToken : StructureToken? = null
    private var bdd: SQLiteDatabase? = null

    val getToken : Token
    get() {
        openForRead()
        val c = bdd!!.query(
            StructureToken.TABLE_SERVEUR,
            arrayOf(StructureToken.COL_NAME),
            null,
            null,
            null,
            null,
            StructureToken.COL_NAME
        )
        if (c.count > 0) {
            if (c.moveToNext()) {
                return Token(c.getString(StructureToken.NUM_COL_ID))
            }
        }
        c.close()
        return Token("")
    }

    init {
        mToken = StructureToken(ctx, NOM_BDD, null, 1)
    }
    fun openForWrite() {
        bdd = mToken!!.writableDatabase
    }

    fun openForRead() {
        bdd = mToken!!.readableDatabase
    }

    fun close() {
        bdd!!.close()
    }

    fun insertToken(token: Token): Long{
        openForWrite()
        val cv = ContentValues()
        cv.put(StructureToken.COL_NAME, token.id)

        val retval = bdd!!.insert(StructureToken.TABLE_SERVEUR, null, cv)
        close()
        return retval
    }

    fun deleteToken(): Int{
        openForWrite()
        val retval = bdd!!.delete(StructureToken.TABLE_SERVEUR, null, null)
        close()
        return retval
    }

    companion object{
        var NOM_BDD = "databaseToken.db"
    }
}