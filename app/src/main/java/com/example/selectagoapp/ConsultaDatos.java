package com.example.selectagoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConsultaDatos extends AppCompatActivity {

    final String[] frutos = new String[]{"Limon"};
    private String tipoFruta, fDesde, fHasta;
    private TextView fechaDesde, fechaHasta;
    private TableLayout tablaDatos;
    private LineChart lineChart;
    private ArrayList<com.github.mikephil.charting.data.Entry> entradaLinea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_datos);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //Poner el ícono al ActionBar
            actionBar.setIcon(R.drawable.tfl_logo);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#62aa00")));
        }
        // Instanciendo elementos de la vista
        Spinner opcionFrutas = findViewById(R.id.spnTipoFrutoCd);
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
        aAFrutos.setBackgroundColor(Color.parseColor("#62aa00"));
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
        header1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        header1.setTextSize(20);
        header1.setTypeface(null, Typeface.BOLD);
        header1.setBackgroundColor(Color.parseColor("#a2d001"));
        header1.setPadding(20,20,20,20);
        headerRow.addView(header1);

        TextView header2 = new TextView(this);
        header2.setText("Producción");
        header2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        header2.setTextSize(20);
        header2.setTypeface(null, Typeface.BOLD);
        header2.setBackgroundColor(Color.parseColor("#a2d001"));
        header2.setPadding(20,20,20,20);
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
            data1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            data1.setTextSize(20);
            data1.setTextColor(Color.BLACK);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

            Date date = null;
            try {
                date = dateFormat.parse(fechas.get(i));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            SimpleDateFormat dateFormatFecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String fecha = dateFormatFecha.format(date);


            data1.setText(fecha);
            data1.setPadding(20,20,20,20);
            dataRow.addView(data1);

            TextView data2 = new TextView(this);
            data2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            data2.setTextSize(20);
            data2.setTextColor(Color.BLACK);
            data2.setText(produccion.get(i));
            data2.setPadding(20,20,20,20);
            dataRow.addView(data2);

            tablaDatos.addView(dataRow);
        }
    }

    public void fechaSeleccion(View view){
        // Obtener la fecha actual
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void obtenerDatos(ArrayList<String> fechas, ArrayList<String> produccion,
                              SimpleDateFormat sdf1) {
        entradaLinea = new ArrayList<>();
        for (int i = 0; i < fechas.size(); i++){
            try{
                Date fecha = sdf1.parse(fechas.get(i));
                double valor = Double.parseDouble(produccion.get(i));
                assert fecha != null;
                entradaLinea.add(new Entry(fecha.getTime(), (float) valor));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void confGrafica(LineChart lineChart, ArrayList<String> fechas,
                             ArrayList<String> produccion){
        XAxis xAxis = lineChart.getXAxis();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        obtenerDatos(fechas, produccion, sdf1);
        LineDataSet dataset = new LineDataSet(entradaLinea, "");
        LineData lineData = new LineData(dataset);
        lineChart.setData(lineData);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return sdf1.format(new Date((long) value));
            }
        });

        lineChart.getDescription().setEnabled(false);
        dataset.setColor(ColorTemplate.JOYFUL_COLORS[3]);
        dataset.setValueTextColor(Color.BLACK);
        dataset.setValueTextSize(18f);
        lineChart.setTouchEnabled(false);
        lineChart.invalidate();
    }

    public void hallarDatos(View view){
        ArrayList<String> produccion = new ArrayList<>();
        ArrayList<String> fechas = new ArrayList<>();
        String [] selectionArgs = {tipoFruta, fDesde, fHasta};
        if (tipoFruta != null && fDesde != null && fHasta != null){
            // OBJETOS DE BASE DATOS
            SQLiteHelperKotlin mydb = new SQLiteHelperKotlin(this);
            SQLiteDatabase db = mydb.getReadableDatabase();
            try{
                // ESTRUCTURA DE CONSULTA
                // SELECT fecha, cantidad_parcela FROM DETECCIONES WHERE fruto = 'Limon' AND fecha BETWEEN "03/07/2023" AND "07/07/2023" ORDER BY fecha DESC
                String consulta = "select fecha, cantidad_parcela from detecciones WHERE fruto = ? " +
                        "AND fecha BETWEEN ? AND ? ORDER BY fecha DESC";
                Cursor cursor = db.rawQuery(consulta, selectionArgs);
                if (cursor.moveToFirst()){
                    do{
                        String fecha = cursor.getString(0);
                        String prod = cursor.getString(1);
                        fechas.add(fecha);
                        produccion.add(prod);
                    }while(cursor.moveToNext());
                    // CARGAR DATOS A LA TABLA
                    cargaTablas(tablaDatos, fechas, produccion);
                    // CARGAR DATOS A LA GRAFICA
                    confGrafica(lineChart, fechas, produccion);
                }else{
                    Toast.makeText(this, "No se hallaron estimaciones", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (db != null && db.isOpen()){
                    db.close();
                }
            }
        }else{
            Toast.makeText(this, "Existen campos sin completar",
                    Toast.LENGTH_SHORT).show();
        }
    }
}