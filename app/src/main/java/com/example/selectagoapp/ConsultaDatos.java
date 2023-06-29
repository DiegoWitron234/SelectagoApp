package com.example.selectagoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConsultaDatos extends AppCompatActivity {

    final String[] frutos = new String[]{"limon"};
    final ArrayList<String> fechas = new ArrayList<>();
    final ArrayList<String> produccion = new ArrayList<>();
    private String tipoFruta, fDesde, fHasta;
    private Spinner opcionFrutas;
    private DatePicker fechaDesde, fechaHasta;
    private ListView lstRFecha, lstRProduccion;
    private LineChart lineChart;
    private LineData lineData;
    private ArrayList<com.github.mikephil.charting.data.Entry> entradaLinea;
    private LineDataSet dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_datos);
        // Instanciendo elementos de la vista
        opcionFrutas = findViewById(R.id.spnTipoFrutoCd);
        fechaDesde = findViewById(R.id.dpDesde);
        fechaHasta = findViewById(R.id.dpHasta);
        lineChart = findViewById(R.id.chProduccion);
        lstRFecha = findViewById(R.id.lstRFecha);
        lstRProduccion = findViewById(R.id.lstRProducto);
        // INCORPORAR METODO QUE RESCATA LOS DATOS DE LA BASE DE DATOS
        obtenerDatos();
        // Configuración de Grafica
        confGrafica(lineChart);
        // Configuración de ArrayAdapters
        confArrayAdapter(opcionFrutas, lstRFecha, lstRProduccion);
        // Configuración de DatePicker
        listenerDatePicker(fechaDesde, fechaHasta);
    }

    private void obtenerDatos() {
        fechas.add("14-05-2023");
        fechas.add("10-06-2023");
        fechas.add("05-07-2023");
        produccion.add("10000");
        produccion.add("23000");
        produccion.add("17000");
        entradaLinea = new ArrayList<>();
        entradaLinea.add(new Entry(1f, 10000));
        entradaLinea.add(new Entry(2f, 23000));
        entradaLinea.add(new Entry(3f, 17000));
    }

    private Date getDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void confArrayAdapter(Spinner aAFrutos, ListView lstRFecha, ListView lstRProduccion){
        // Instanciando ArrayAdapters
        ArrayAdapter<String> adapterFrutas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frutos);
        ArrayAdapter<String> adapterFechas = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, fechas);
        ArrayAdapter<String> adapterProduccion = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, produccion);
        // Configuración de Spinner Frutas
        adapterFrutas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aAFrutos.setAdapter(adapterFrutas);
        listenerArrayAdapter(aAFrutos);
        // Configuración de ListViews
        lstRFecha.setAdapter(adapterFechas);
        lstRProduccion.setAdapter(adapterProduccion);
    }

    private void listenerArrayAdapter(Spinner aAFrutos){
        aAFrutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipoFruta = frutos[i];
                Toast.makeText(getApplicationContext(),"Fruto: "+ tipoFruta, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void listenerDatePicker(DatePicker desde, DatePicker hasta){
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        desde.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                fDesde = i2+"/"+i1+"/"+i;
                Toast.makeText(ConsultaDatos.this, fDesde, Toast.LENGTH_SHORT).show();
            }
        });
        hasta.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                fHasta = i2+"/"+i1+"/"+i;
                Toast.makeText(ConsultaDatos.this, fHasta, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confGrafica(LineChart lineChart){
        dataset = new LineDataSet(entradaLinea, "");
        lineData = new LineData(dataset);
        lineChart.setData(lineData);
        dataset.setColor(ColorTemplate.JOYFUL_COLORS[3]);
        dataset.setValueTextColor(Color.BLACK);
        dataset.setValueTextSize(18f);
    }
    public void hallarDatos(View view){
        Toast.makeText(this, tipoFruta+fDesde+fHasta, Toast.LENGTH_SHORT).show();
    }


}