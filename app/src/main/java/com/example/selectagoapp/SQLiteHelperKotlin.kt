package com.example.selectagoapp

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

val BD_NOMBRE : String = "registros"
val VERSION : Int = 1
val tabla : String = "detecciones"

class SQLiteHelperKotlin(context: Context) : SQLiteOpenHelper(context, BD_NOMBRE, null, VERSION) {
    override fun onCreate(p0: SQLiteDatabase?) {
        // Aquí defines la estructura de tus tablas y creas las tablas iniciales si es necesario
        val crearTabla = "CREATE TABLE IF NOT EXISTS detecciones (id_deteccion INTEGER PRIMARY KEY, fruto TEXT, fecha TEXT, cantidad_arbol INTEGER, cantidad_parcela INTEGER)"
        p0?.execSQL(crearTabla)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        // Eliminar una tabla existente
        if (p1 < 2) {
            p0?.execSQL("DROP TABLE IF EXISTS tabla_antigua");
        }
    }

}