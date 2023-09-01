package com.example.acrousthetime.bd

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StructureToken (
    context : Context?,
    name: String?,
    factory: SQLiteDatabase.CursorFactory?,
    version: Int
    ) : SQLiteOpenHelper(context, name, factory, version) {
    private var db: SQLiteDatabase? = null
    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        db = sqLiteDatabase
        db!!.execSQL(CREATE_BDD)
    }
    override fun onUpgrade(db: SQLiteDatabase, nVersion: Int, oVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_SERVEUR")
        onCreate(db!!)
    }

    companion object{
        val TABLE_SERVEUR = "token"

        val COL_NAME = "ID"
        val NUM_COL_ID = 0


        private val CREATE_BDD = "CREATE TABLE $TABLE_SERVEUR (" +
                "$COL_NAME TEXT NOT NULL PRIMARY KEY );"
    }
}