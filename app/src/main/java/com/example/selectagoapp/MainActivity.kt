package com.example.selectagoapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val actionBar: ActionBar? = supportActionBar

        if (actionBar != null) {
            //Poner el ícono al ActionBar
            actionBar.setIcon(R.drawable.tfl_logo)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setBackgroundDrawable(ColorDrawable(Color.parseColor("#62aa00")))
        }

    }

    fun fnTomarMuestras(view: View) {
        val intent = Intent(this, ConfMuestra::class.java)
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

    fun fnRealizarEstimacion(view: View) {
        val intent = Intent(this, RealEstimacion::class.java)
        startActivity(intent)
    }

    fun fnConsultarDatos(view: View) {
        val intent = Intent(this, ConsultaDatos::class.java)
        startActivity(intent)
    }

}