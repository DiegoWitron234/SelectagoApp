package com.example.selectagoapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        val actionBar: ActionBar? = supportActionBar

        if (actionBar != null) {
            //Poner el ícono al ActionBar
            actionBar.setIcon(R.drawable.tfl_logo)
            actionBar.setDisplayShowHomeEnabled(true)
            //Poner la flecha para regresar a la parent activity
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        */
    }

    fun fnTomarMuestras(view: View) {
        val intent = Intent(this, confMuestra::class.java)
        startActivity(intent)
    }

    fun fnAyuda(view: View) {
        val intent = Intent(this, Ayuda::class.java)
        startActivity(intent)
    }
    fun fnAcercaDe(view: View) {
        val intent = Intent(this, AcercaDe::class.java)
        startActivity(intent)
    }


}