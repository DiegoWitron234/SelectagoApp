package com.example.selectagoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultadosEstimacion extends AppCompatActivity {

    private TextView txtFruto, txtProduccion, txtValor, txtRecolectores, txtCostales, txtTransporte;
    private String fruto, transporte, produccion;
    private double valor, tonelada;
    private int recolectores, costales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_estimacion);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //Poner el ícono al ActionBar
            actionBar.setIcon(R.drawable.tfl_logo);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        txtFruto = findViewById(R.id.txtTipoFruto);
        txtProduccion = findViewById(R.id.txtEstimacionProduccion);
        txtValor = findViewById(R.id.txtValorProduccion);
        txtRecolectores = findViewById(R.id.txtRecolectoresNecesarios);
        txtCostales = findViewById(R.id.txtNumeroCostales);
        txtTransporte = findViewById(R.id.txtNumeroTransportes);

        Intent intent = getIntent();
        fruto = intent.getStringExtra("fruto");
        produccion = intent.getStringExtra("produccion");
        valor = intent.getDoubleExtra("valor", 0);
        recolectores = intent.getIntExtra("recolectores",0 );
        costales = intent.getIntExtra("costales", 0);
        transporte = intent.getStringExtra("transporte");
        tonelada = intent.getDoubleExtra("tonelada", 0);

        mostrarDatos();

    }

    private void mostrarDatos(){
        txtFruto.setText("Tipo De Fruto: "+fruto);
        txtProduccion.setText("Estimación de producción: "+produccion +" frutos = "+tonelada+" t");
        txtValor.setText("Valor de producción: $"+valor);
        txtRecolectores.setText("Recolectores necesarios: "+recolectores);
        txtCostales.setText("Número de costales: "+costales);
        txtTransporte.setText(transporte);
    }

    public void salirEstimacion(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}