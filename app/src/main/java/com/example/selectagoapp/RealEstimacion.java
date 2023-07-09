package com.example.selectagoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
    private final String[][] transporte = new String[4][2];
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
        Spinner opcionFrutas = findViewById(R.id.spnTipoFruto);
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
        try {
            double precioVenta = Double.parseDouble(String.valueOf(txtPrecioVenta.getText())),
                    capacidad_costal = 20, gramoFruto = 80;
            int costalesDiarios = 50, dias = Integer.parseInt(String.valueOf(txtDia.getText()));
            String traslado;

            String valores = registro();
            if (!valores.isEmpty()){
                double pesoTotal = (Double.parseDouble(valores) * gramoFruto) / 1000; // < EN KILOS
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
                intent.putExtra("produccion", valores);
                intent.putExtra("valor", valorProduccion);
                intent.putExtra("recolectores", (int) Math.ceil(trabajadores));
                intent.putExtra("costales", (int) Math.ceil(costales));
                intent.putExtra("transporte", traslado);
                intent.putExtra("tonelada", tonelada);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Sin estimaciones realizadas",
                        Toast.LENGTH_LONG).show();
            }
        }catch (NumberFormatException e) {
            Toast.makeText(this, "Existen campos sin completar",
                    Toast.LENGTH_LONG).show();
        }
    }

    private String registro(){
        String datos = "";
        SQLiteHelperKotlin mydb = new SQLiteHelperKotlin(this);
        SQLiteDatabase db = mydb.getReadableDatabase();
        //System.out.println("FRUTO:" + tipoFruta);
        try{
            String[] whereArgs = {tipoFruta}; // Argumentos para la cláusula WHERE si es necesario
            String consulta = "SELECT cantidad_parcela, fecha FROM detecciones WHERE fruto = ? ORDER BY fecha DESC LIMIT 1";
            Cursor cursor = db.rawQuery(consulta, whereArgs);
            if (cursor.moveToFirst()){
                do{
                    datos = cursor.getString(0);
                }while (cursor.moveToNext());
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (db != null && db.isOpen()){
                db.close();
            }
        }
        return datos;
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