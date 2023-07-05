package com.example.selectagoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RealEstimacion extends AppCompatActivity {

    final String[] frutos = new String[]{"limon"};
    private Spinner opcionFrutas;
    private EditText txtPrecioVenta, txtDia;

    private String tipoFruta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_estimacion);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //Poner el ícono al ActionBar
            actionBar.setIcon(R.drawable.tfl_logo);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Instanciando elementos de la vista
        opcionFrutas = findViewById(R.id.spnTipoFruto);
        txtPrecioVenta = findViewById(R.id.txtPrecioVenta);
        txtDia = findViewById(R.id.txtDias);
        //Configuración de ArrayAdapter
        confArrayAdapters(opcionFrutas);
        // Estableciendo evento Listener en los ArrayAdaptador

    }

    private void confArrayAdapters(Spinner opcionFrutas) {
        ArrayAdapter<String> adapterFrutas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frutos);
        // ArrayAdapter de Frutas
        adapterFrutas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        opcionFrutas.setAdapter(adapterFrutas);
        opcionFrutas.setPrompt("Tipo de fruto");
        eventoOpcionArray(opcionFrutas);
    }

    private void eventoOpcionArray(Spinner opcionFrutas) {
        opcionFrutas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoFruta = frutos[i];
                Toast.makeText(getApplicationContext(), "Fruto: " + tipoFruta, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void aceptarConfEst(View view) {


        String dias = String.valueOf(txtDia.getText());
        String precioVenta = String.valueOf(txtPrecioVenta.getText());
        Toast.makeText(this, dias + " " + precioVenta + " " + tipoFruta, Toast.LENGTH_SHORT).show();
    }

    private void actualizarRegistro(int parcela){
        // Instanciando clase de la base de datos
        try(SQLiteHelperKotlin mydb = new SQLiteHelperKotlin(this)){
            SQLiteDatabase db = mydb.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("cantidad_parcela", parcela);

            String whereClause = "fruto = ? ORDER BY id_deteccion DESC LIMIT 1"; // Condición para seleccionar registros con celda vacía
            String[] whereArgs = {tipoFruta}; // Argumentos para la cláusula WHERE si es necesario

            int numRowsUpdated = db.update("detecciones", values, whereClause, whereArgs);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}