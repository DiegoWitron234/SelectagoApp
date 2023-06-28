package com.example.selectagoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class confMuestra extends AppCompatActivity {
    final String[] frutos = new String[]{"Limon", "Mandarina (No Usar)"};
    private Spinner opcionFrutas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_muestra);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frutos);
        opcionFrutas = (Spinner) findViewById(R.id.opcionFrutas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcionFrutas.setAdapter(adapter);
        eventoFrutaOpcion(opcionFrutas, this);
  }

    private void eventoFrutaOpcion(Spinner opcionFrutas, confMuestra confMuestra){
        opcionFrutas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(confMuestra,"Fruto:"+ frutos[i], Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}