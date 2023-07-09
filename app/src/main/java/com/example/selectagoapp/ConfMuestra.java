package com.example.selectagoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class ConfMuestra extends AppCompatActivity {
    final String[] frutos = new String[]{"Limon"};
    final String[] precision = new String[]{"0.5","0.75","0.95"};
    private EditText cantidadArboles;
    private String nivelPrecision, tipoFruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_muestra);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //Poner el ícono al ActionBar
            actionBar.setIcon(R.drawable.tfl_logo);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Instanciando elementos de vista
        Spinner opcionFrutas = findViewById(R.id.opcionFrutas);
        Spinner opcionPrecision = findViewById(R.id.opcionPrecision);
        cantidadArboles = findViewById(R.id.intCantArbol);
        // Configuración de ArrayAdapters
        confArrayAdapter(opcionFrutas, opcionPrecision);
  }

    private void confArrayAdapter(Spinner opcionFrutas, Spinner opcionPrecision){
        ArrayAdapter<String> adapterFrutas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frutos);
        ArrayAdapter<String> adapterPrecision = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, precision);
        // ArrayAdapter de Frutas
        adapterFrutas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcionFrutas.setAdapter(adapterFrutas);
        // ArrayAdapter de Precision
        adapterPrecision.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcionPrecision.setAdapter(adapterPrecision);
        // Estableciendo evento Listener en los ArrayAdapters
        listenerArrayAdapter(opcionFrutas, opcionPrecision);
    }

    private void listenerArrayAdapter(Spinner opcionFrutas, Spinner opcionPrecision){
        opcionFrutas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoFruta = frutos[i];
                //Toast.makeText(getApplicationContext(),"Fruto: "+ tipoFruta, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        opcionPrecision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nivelPrecision = precision[i];
                //Toast.makeText(getApplicationContext(),"Precisión:"+ nivelPrecision, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void aceptarConfMu(View view) {
        try{
            int numArboles = Integer.parseInt(String.valueOf(cantidadArboles.getText()));
            double nivelConfianza = 1.94, estimacion = 0.5, margenError = 0.5;
            //Toast.makeText(this, numArboles + " " + nivelPrecision + " " + tipoFruta,Toast.LENGTH_LONG).show();
            double tMuestra = (numArboles * Math.pow(nivelConfianza, 2) *
                    estimacion * (1-estimacion))/((numArboles-1) *
                    Math.pow(margenError,2) + Math.pow(nivelConfianza,2) * estimacion * (1-estimacion));

            System.out.println("MUESTRA: "+ tMuestra);
            /*Intent intent = new Intent(this, Deteccion.class);
            intent.putExtra("fruto",tipoFruta);
            intent.putExtra("arboles", numArboles);
            intent.putExtra("muestra", (int)tMuestra);
            startActivity(intent);*/

            Intent intent = new Intent(this, Deteccion.class);
            startActivity(intent);
        }catch(NumberFormatException e){
            Toast.makeText(this, "Existen campos sin completar",
                    Toast.LENGTH_LONG).show();
        }
    }
}