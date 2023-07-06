package com.example.selectagoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RealEstimacion extends AppCompatActivity {

    final String[] frutos = new String[]{"Limon"};
    private String transporte [][] = new String[4][2];
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
        // Instanciandos transportes
        datos_transporte();
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
                //Toast.makeText(getApplicationContext(), "Fruto: " + tipoFruta, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void aceptarConfEst(View view) {
        String [] valores = registro();
        System.out.println("CONSULTA: "+valores[0]);
        double precioVenta = Double.parseDouble(String.valueOf(txtPrecioVenta.getText()));
        int dias = Integer.parseInt(String.valueOf(txtDia.getText()));
        double capacidad_costal = 20;
        double gramoFruto = 80;
        int costalesDiarios = 50;
        String traslado;

        double pesoTotal = (Double.parseDouble(valores[0]) * gramoFruto) / 1000;
        double costales = pesoTotal / capacidad_costal;
        double valorProduccion = pesoTotal * precioVenta;
        double diasHombre = Math.round(costales / costalesDiarios);
        double trabajadores = diasHombre / dias;
        double tonelada = pesoTotal/1000;
        String[]camiones= medio_traslado(pesoTotal/1000);

        if (camiones[0].equals("Trailer")){
            if(!camiones[1].equals("1.0")){
                traslado =(int) Double.parseDouble(camiones[1])+" Trailers de 25 t";
            }else{
                traslado = "1 Trailer de 25 t";
            }
        }else{
            traslado = "1 "+ camiones[0];
        }

        Intent intent = new Intent (this, ResultadosEstimacion.class);
        intent.putExtra("fruto", tipoFruta);
        intent.putExtra("produccion", valores[0]);
        intent.putExtra("valor", valorProduccion);
        intent.putExtra("recolectores", (int) Math.ceil(trabajadores));
        intent.putExtra("costales", (int) Math.ceil(costales));
        intent.putExtra("transporte", traslado);
        intent.putExtra("tonelada", tonelada);
        startActivity(intent);
    }

    private String[] registro(){
        String[] datos = new String[2];
        SQLiteHelperKotlin mydb = new SQLiteHelperKotlin(this);
        SQLiteDatabase db = mydb.getReadableDatabase();
        // ESTRUCTURA DE CONSULTA
        System.out.println("FRUTO:" + tipoFruta);
        try{
            String[] whereArgs = {tipoFruta}; // Argumentos para la cláusula WHERE si es necesario
            String consulta = "select cantidad_parcela, fecha from detecciones WHERE fruto = ? ORDER BY fecha DESC LIMIT 1";
            Cursor cursor = db.rawQuery(consulta, whereArgs);
            if (cursor.moveToFirst()){
                do{
                    System.out.println("VALOR: "+cursor.getString(0));
                    datos[0] = cursor.getString(0);
                }while (cursor.moveToNext());
            }else{
                Toast.makeText(this, "No se realizo la consulta", Toast.LENGTH_LONG).show();
            }
            cursor.close();
            return datos;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            if (db != null && db.isOpen()){
                db.close();
            }
        }
    }

    private void datos_transporte(){
        //TIPO
        transporte[0][0]="Camioneta de Carga";
        transporte[1][0]="Rabón";
        transporte[2][0]="Torton";
        transporte[3][0]="Trailer";
        // CAPACIDAD MAXIMA
        transporte[0][1]="3.5";
        transporte[1][1]="15";
        transporte[2][1]="20";
        transporte[3][1]="25";
    }

    private String[] medio_traslado(double estimacionToneladas) {
        double cantidad_transporte = 1, max;
        String[] camiones = new String[2];
        int i;
        for (i = 0; i < 4; i++) {
            max = Double.parseDouble(transporte[i][1]);
            if (estimacionToneladas <= max) {
                break;
            } else {
                if (max == 25) {
                    cantidad_transporte = Math.ceil(estimacionToneladas / max);
                    break;
                }
            }
        }
        camiones[0] = transporte[i][0];
        camiones[1] = "" + cantidad_transporte;

        return camiones;
    }
}