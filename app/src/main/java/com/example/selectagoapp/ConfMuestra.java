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

public class ConfMuestra extends AppCompatActivity {
    final String[] frutos = new String[]{"Limon"};
    final String[] precision = new String[]{"0.5","0.75","0.95"};
    private Spinner opcionFrutas, opcionPrecision;
    private EditText cantidadArboles;
    private String nivelPrecision, tipoFruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_muestra);
        // Instanciando elementos de vista
        opcionFrutas = findViewById(R.id.opcionFrutas);
        opcionPrecision = findViewById(R.id.opcionPrecision);
        cantidadArboles = findViewById(R.id.intCantArbol);
        // Configuración de ArrayAdapters
        confArrayAdapters(opcionFrutas, opcionPrecision);
  }

    private void confArrayAdapters(Spinner opcionFrutas, Spinner opcionPrecision){
        ArrayAdapter<String> adapterFrutas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frutos);
        ArrayAdapter<String> adapterPrecision = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, precision);
        // ArrayAdapter de Frutas
        adapterFrutas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcionFrutas.setAdapter(adapterFrutas);
        opcionFrutas.setPrompt("Tipo de fruto");
        // ArrayAdapter de Precision
        adapterPrecision.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcionPrecision.setAdapter(adapterPrecision);
        opcionPrecision.setPrompt("Nivel de precisión");
        // Estableciendo evento Listener en los ArrayAdapters
        eventoOpcionArray(opcionFrutas, opcionPrecision);
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
        String numArboles = String.valueOf(cantidadArboles.getText());
        Toast.makeText(this, numArboles + " " + nivelPrecision + " " + tipoFruta,Toast.LENGTH_LONG).show();
    }
}