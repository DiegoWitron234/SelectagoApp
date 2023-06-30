package com.example.selectagoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

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

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //Poner el ícono al ActionBar
            actionBar.setIcon(R.drawable.tfl_logo);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Instanciando elementos de vista
        opcionFrutas = findViewById(R.id.opcionFrutas);
        opcionPrecision = findViewById(R.id.opcionPrecision);
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

        try (SQLiteHelperKotlin miBaseDeDatos = new SQLiteHelperKotlin(this)){
        SQLiteDatabase db = miBaseDeDatos.getWritableDatabase();
            if  (db != null){
                // Insertar datos en la tabla
                ContentValues valores = new ContentValues();
                valores.put("fruto", "Limon");
                valores.put("fecha",fechasFormatos());
                valores.put("cantidad_arboles", 10000);
                valores.put("cantidad_parcelas", 270000);
                long resultado = db.insert("detecciones", null, valores);
                Toast.makeText(this, String.valueOf(resultado), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "No se creó la base de datos", Toast.LENGTH_LONG).show();
            }
        }catch(Exception ignored){
            Toast.makeText(this, "No se pudieron guardar los datos", Toast.LENGTH_SHORT).show();
        }

    }

    private String fechasFormatos(){

    // Obtener la fecha actual
        Date currentDate = new Date();

    // Definir el patrón de formato deseado
        String pattern = "dd/MM/yyyy";

    // Crear un objeto SimpleDateFormat con el patrón
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);

    // Formatear la fecha
        return dateFormat.format(currentDate);
    }
}