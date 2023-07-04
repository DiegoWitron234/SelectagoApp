package com.example.selectagoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConsultaDatos extends AppCompatActivity {

    final String[] frutos = new String[]{"Limon"};
    private String tipoFruta, fDesde, fHasta;
    private Spinner opcionFrutas;
    private TextView fechaDesde, fechaHasta;
    private TableLayout tablaDatos;
    private LineChart lineChart;
    private LineData lineData;
    private ArrayList<com.github.mikephil.charting.data.Entry> entradaLinea;
    private LineDataSet dataset;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_datos);
        // Instanciendo elementos de la vista
        opcionFrutas = findViewById(R.id.spnTipoFrutoCd);
        fechaDesde = findViewById(R.id.inDesde);
        fechaHasta = findViewById(R.id.inHasta);
        lineChart = findViewById(R.id.chProduccion);
        tablaDatos = findViewById(R.id.lstRProduccion);
        // Configuración de ArrayAdapters
        confArrayAdapter(opcionFrutas);
    }


    private void confArrayAdapter(Spinner aAFrutos){
        // Instanciando ArrayAdapters
        ArrayAdapter<String> adapterFrutas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frutos);
        // Configuración de Spinner Frutas
        adapterFrutas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aAFrutos.setAdapter(adapterFrutas);
        listenerArrayAdapter(aAFrutos);
    }

    @SuppressLint("ResourceType")
    private void cargaTablas(TableLayout tablaDatos, ArrayList <String> fechas,
                             ArrayList<String> produccion){
        // Agregar la cabecera de la tabla
        tablaDatos.removeAllViews();
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));
        // Creando objetos TextView para insertarlos como cabecera
        TextView header1 = new TextView(this);
        header1.setText("Fecha");
        header1.setBackgroundColor(Color.parseColor("#a2d001"));
        headerRow.addView(header1);

        TextView header2 = new TextView(this);
        header2.setText("Produccion");
        header2.setBackgroundColor(Color.parseColor("#a2d001"));
        headerRow.addView(header2);
        // Cargando datos
        tablaDatos.addView(headerRow);

        // Agregar los datos a la tabla
        for (int i = 0; i < produccion.size(); i++) {
            TableRow dataRow = new TableRow(this);
            // Estableciendo parametros para que las filas cubran el tamaño adecuado
            dataRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            // Creando objetos TextView para cargar los datos
            TextView data1 = new TextView(this);
            data1.setText(fechas.get(i));
            dataRow.addView(data1);

            TextView data2 = new TextView(this);
            data2.setText(produccion.get(i));
            dataRow.addView(data2);

            tablaDatos.addView(dataRow);
        }
    }

    public void fechaSeleccion(View view){
        Locale locale = new Locale("es", "LA");

        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        // DESDE
        if (view == fechaDesde){
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
               calendar.set(i, i1, i2);
               Date selectedDate = calendar.getTime();
               fDesde = sdf.format(selectedDate);
               fechaDesde.setText(fDesde);
            },year, mes, dia);
            datePickerDialog.show();
        // HASTA
        } else if (view == fechaHasta) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, (datePicker, i, i1, i2) -> {
                calendar.set(i, i1, i2);
                Date selectedDate = calendar.getTime();
                fHasta = sdf.format(selectedDate);
                fechaHasta.setText(fHasta);
            }, year, mes, dia);
            datePickerDialog.show();
        }
    }

    private void listenerArrayAdapter(Spinner aAFrutos){
        aAFrutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoFruta = frutos[i];
                //Toast.makeText(getApplicationContext(),"Fruto: "+ tipoFruta, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void obtenerDatos(ArrayList<String> fechas, ArrayList<String> produccion) {
        entradaLinea = new ArrayList<>();
        //SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        for (int i = 0; i < fechas.size(); i++){
            try{
                Date fecha = sdf.parse(fechas.get(i));
                double valor = Double.parseDouble(produccion.get(i));
                entradaLinea.add(new Entry(fecha.getTime(), (float) valor));
            }catch (Exception ignored){
                ignored.printStackTrace();
            }

        }
    }


    private void confGrafica(LineChart lineChart, ArrayList<String> fechas,
                             ArrayList<String> produccion){
        YAxis yIzq, yDer;
        XAxis xAxis = lineChart.getXAxis();
        yIzq = lineChart.getAxisLeft();
        yDer = lineChart.getAxisRight();

        obtenerDatos(fechas, produccion);
        dataset = new LineDataSet(entradaLinea, "");
        lineData = new LineData(dataset);
        lineChart.setData(lineData);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            //private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return sdf.format(new Date((long) value));
            }
        });
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(5);
        yIzq.setLabelCount(5);
        yDer.disableAxisLineDashedLine();

        lineChart.getDescription().setEnabled(false);
        dataset.setColor(ColorTemplate.JOYFUL_COLORS[3]);
        dataset.setValueTextColor(Color.BLACK);
        dataset.setValueTextSize(18f);

        lineChart.invalidate();
    }

    public void hallarDatos(View view){
        //Toast.makeText(this, "FRUTO: "+tipoFruta+" INICIO: "+fDesde+" FINAL: "+fHasta,
          //      Toast.LENGTH_SHORT).show();
        ArrayList<String> produccion = new ArrayList<>();
        ArrayList<String> fechas = new ArrayList<>();
        String [] selectionArgs = {tipoFruta, fDesde, fHasta};

        // OBJETOS DE BASE DATOS
        SQLiteHelperKotlin mydb = new SQLiteHelperKotlin(this);
        SQLiteDatabase db = mydb.getReadableDatabase();
        // ESTRUCTURA DE CONSULTA
        // SELECT fecha, cantidad_parcela FROM DETECCIONES WHERE fruto = 'Limon' AND fecha BETWEEN "03/07/2023" AND "07/07/2023" ORDER BY fecha DESC
        //String consulta = "select fecha, cantidad_parcela from detecciones WHERE fruto = ? AND fecha BETWEEN ? AND ? ORDER BY fecha DESC";
        try{
            String consulta = "select fecha, cantidad_parcela from detecciones WHERE fruto = ? " +
                    "AND fecha BETWEEN ? AND ?";
            Cursor cursor = db.rawQuery(consulta, selectionArgs);
            if (cursor.moveToFirst()){
                do{
                    String fecha = cursor.getString(0);
                    String prod = cursor.getString(1);
                    fechas.add(fecha);
                    produccion.add(prod);
                    //Toast.makeText(this,fecha+prod,Toast.LENGTH_SHORT).show();
                }while(cursor.moveToNext());
                //Toast.makeText(this, "Realizando consulta", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "No se realizo la consulta", Toast.LENGTH_LONG).show();
            }
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (db != null && db.isOpen()){
                db.close();
            }
        }
        // Carga de datos en tabla
        cargaTablas(tablaDatos, fechas, produccion);
        // Carga de datos en grafica
        confGrafica(lineChart, fechas, produccion);
    }


}