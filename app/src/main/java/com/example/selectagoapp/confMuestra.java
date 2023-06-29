package com.example.selectagoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class confMuestra extends AppCompatActivity {
    final String[] frutos = new String[]{"Tipo de fruto","Limon"};
    final String[] precision = new String[]{"Nivel de precisión","0.5","0.75","0.95"};
    private Spinner opcionFrutas;
    private Spinner opcionPrecision;
    private TextView cantidadArboles;
    private String nivelPrecision, tipoFruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_muestra);
        // Instanciando elementos de vista
        opcionFrutas = (Spinner) findViewById(R.id.opcionFrutas);
        opcionPrecision = (Spinner) findViewById(R.id.opcionPrecision);
        cantidadArboles = findViewById(R.id.intCantArbol);
        // Configuración de ArrayAdapters
        confArrayAdapters(opcionFrutas, opcionPrecision);
        // Estableciendo evento Listener en los ArrayAdapters
        eventoOpcionArray(opcionFrutas, opcionPrecision);
  }

    private void confArrayAdapters(Spinner opcionFrutas, Spinner opcionPrecision){
        ArrayAdapter<String> adapterFrutas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frutos);
        ArrayAdapter<String> adapterPrecision = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, precision);
        // ArrayAdapter de Frutas
        adapterFrutas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcionFrutas.setAdapter(adapterFrutas);
        // ArrayAdapter de Precision
        adapterPrecision.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcionPrecision.setAdapter(adapterPrecision);
    }

    private void eventoOpcionArray(Spinner opcionFrutas, Spinner opcionPrecision){
        opcionFrutas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoFruta = frutos[i];
                Toast.makeText(getApplicationContext(),"Fruto: "+ tipoFruta, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        opcionPrecision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                nivelPrecision = precision[i];
                Toast.makeText(getApplicationContext(),"Precisión:"+ nivelPrecision, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void aceptarConfMu(View view) {
        int numArboles = Integer.parseInt((String) cantidadArboles.getText());
        Toast.makeText(this, numArboles + nivelPrecision + tipoFruta,Toast.LENGTH_LONG).show();
    }
}