package com.example.selectagoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultadosDeteccion extends AppCompatActivity {

    TextView txtArboles, txtMuestra, txtDetectados, txtPromedio, txtEstimacion;
    private String fruto;
    private int arboles, muestra, detecciones, promedio, estimacion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados_deteccion);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //Poner el ícono al ActionBar
            actionBar.setIcon(R.drawable.tfl_logo);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        txtArboles = findViewById(R.id.txtNumeroArboles);
        txtMuestra = findViewById(R.id.txtArbolesMuestreados);
        txtDetectados = findViewById(R.id.txtFrutosDetectados);
        txtPromedio = findViewById(R.id.txtFrutosArbol);
        txtEstimacion = findViewById(R.id.txtResultado);

        Intent intent = getIntent();
        fruto = intent.getStringExtra("fruto");
        arboles = intent.getIntExtra("arboles", 0);
        muestra = intent.getIntExtra("muestra", 0);
        detecciones = intent.getIntExtra("detecciones", 0);
        promedio = intent.getIntExtra("promedio", 0);
        estimacion = intent.getIntExtra("estimacion", 0);

        mostrarDeteccion();
    }

    private void mostrarDeteccion(){
        txtArboles.setText("Número de árboles: "+arboles);
        txtMuestra.setText("Árboles muestreados: "+muestra);
        txtDetectados.setText("Frutos Detectados: "+detecciones);
        txtPromedio.setText("Promedio por árbol: "+promedio);
        txtEstimacion.setText(estimacion+" frutos");
    }

    public void insertarDatosDeteccion(View view) {
        try (SQLiteHelperKotlin miBaseDeDatos = new SQLiteHelperKotlin(this)){
        SQLiteDatabase db = miBaseDeDatos.getWritableDatabase();
            if  (db != null){
                // Insertar datos en la tabla
                ContentValues valores = new ContentValues();
                valores.put("fruto", fruto);
                valores.put("fecha",fechasFormatos());
                valores.put("cantidad_arbol", arboles);
                valores.put("cantidad_parcela", estimacion);
                db.insert("detecciones", null, valores);
                Toast.makeText(this, "Resultados guardados",Toast.LENGTH_SHORT).show();
                salirDatos(view);
            }else{
                Toast.makeText(this, "Error al guardar los resultados", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception ignored){
            Toast.makeText(this, "No se pudieron guardar los resultados", Toast.LENGTH_SHORT).show();
        }
    }

    private String fechasFormatos(){

        // Obtener la fecha actual
        Date currentDate = new Date();

        // Definir el patrón de formato deseado
        String pattern = "dd/MM/yyyy HH:mm:ss";

        // Crear un objeto SimpleDateFormat con el patrón
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());

        // Formatear la fecha
        return dateFormat.format(currentDate);
    }

    public void salirDatos(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}