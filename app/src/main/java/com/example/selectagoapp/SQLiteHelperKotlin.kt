package com.example.selectagoapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val BD_NOMBRE : String = "registros"
val VERSION : Int = 1
val tabla : String = "detecciones"

class SQLiteHelperKotlin(context: Context) : SQLiteOpenHelper(context, BD_NOMBRE, null, VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {
        TODO("Not yet implemented")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

}