package com.example.selectagoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
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
    public ArrayList<String> produccion = new ArrayList<>();
    public ArrayList<String> fechas = new ArrayList<>();
    private String tipoFruta, fDesde, fHasta;
    private Spinner opcionFrutas;
    private TextView fechaDesde, fechaHasta;
    private TableLayout tablaDatos;
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
        fechaDesde = findViewById(R.id.inDesde);
        fechaHasta = findViewById(R.id.inHasta);
        lineChart = findViewById(R.id.chProduccion);
        tablaDatos = findViewById(R.id.lstRProduccion);
        // INCORPORAR METODO QUE RESCATA LOS DATOS DE LA BASE DE DATOS
        obtenerDatos();
        // Configuración de Grafica
        confGrafica(lineChart);
        // Configuración de ArrayAdapters
        confArrayAdapter(opcionFrutas);
        // Configuración de Tabla de datos
        confTable(tablaDatos);
    }

    private void obtenerDatos() {
        produccion.add("10000");
        produccion.add("23000");
        produccion.add("17000");
        fechas.add("14-05-2023");
        fechas.add("10-06-2023");
        fechas.add("05-07-2023");
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

    private void confArrayAdapter(Spinner aAFrutos){
        // Instanciando ArrayAdapters
        ArrayAdapter<String> adapterFrutas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, frutos);
        // Configuración de Spinner Frutas
        adapterFrutas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aAFrutos.setAdapter(adapterFrutas);
        listenerArrayAdapter(aAFrutos);
    }

    private void confTable(TableLayout tablaDatos){
        // Agregar la cabecera de la tabla
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView header1 = new TextView(this);
        header1.setText("Cabecera 1");
        headerRow.addView(header1);

        TextView header2 = new TextView(this);
        header2.setText("Cabecera 2");
        headerRow.addView(header2);

        tablaDatos.addView(headerRow);

        // Agregar los datos a la tabla
        for (int i = 0; i < produccion.size(); i++) {
            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

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
        // Obtener la fecha actual
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        if (view == fechaDesde){
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    fDesde = i2+"/"+i1+"/"+i;
                    fechaDesde.setText(fDesde);
                }
            },dia, mes, year);
            datePickerDialog.show();
        } else if (view == fechaHasta) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    fHasta = i2+"/"+i1+"/"+i;
                    fechaDesde.setText(fHasta);
                }
            },dia, mes, year);
            datePickerDialog.show();
        }
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