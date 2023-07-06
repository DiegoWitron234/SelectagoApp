package com.example.selectagoapp;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;

public class TFModelo extends AppCompatActivity {
    ImageView visualizacion;
    TextView arbolesPendientes;

    Button siguiente;
    private Bitmap inputImage;
    private int detecciones = 0, promedio, arboles, muestra, pendientes = 1;
    private String fruto;

    boolean operar = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfmodelo);
        visualizacion = findViewById(R.id.resDeteccion);
        arbolesPendientes = findViewById(R.id.arbolesPendientes);
        siguiente = findViewById(R.id.btnSiguiente);
        // Rescatando datos de activity pasada
        Intent intent = getIntent();
        fruto = intent.getStringExtra("fruto");
        arboles = intent.getIntExtra("arboles", 0);
        muestra = intent.getIntExtra("muestra", 1);

        arbolesPendientes.setText("Arboles: "+pendientes+"/"+muestra);
    }

    private MappedByteBuffer cargaModelos(String ruta) throws IOException {
        AssetFileDescriptor fileDescriptor = getAssets().openFd(ruta);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private void verDeteccion(List<DetectionResult> resultado){
        // Crear un nuevo bitmap mutable para dibujar las cajas delimitadoras y etiquetas
        Bitmap resultBitmap = inputImage.copy(Bitmap.Config.ARGB_8888, true);

        // Obtener el objeto Canvas para dibujar en el bitmap
        Canvas canvas = new Canvas(resultBitmap);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2.0f);
        paint.setTextSize(20.0f);
        paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        System.out.println(resultado.size());
        // Dibujar los bounding boxes y las etiquetas en la imagen
        for (DetectionResult result : resultado) {

            RectF boundingBox = result.getBoundingBox();
            String label = "Limon";
            float confidence = result.getConfidence();
            //System.out.println("CAJA: "+boundingBox+" CONFIANZA: "+confidence);
            // Dibujar el bounding box
            canvas.drawRect(boundingBox, paint);

            String text = label + ": " + confidence;
            float textSize = 16.0f;
            paint.setTextSize(textSize);
            float textHeight = -paint.getFontMetrics().top;
            float textOffsetY = boundingBox.top - textHeight;
            canvas.drawText(text, boundingBox.left, textOffsetY, paint);
        }

        // Actualizar el ImageView con el resultado dibujado
        visualizacion.setImageBitmap(resultBitmap);
        //datos.setText(String.valueOf(resultado.size()));
    }

    public void dSiguiente(View view) {
        if (!operar){
            try {
                System.out.println("PENDIENTES: "+pendientes);
                // Cargar el modelo TFLite al iniciar la actividad
                Inferencias inModel = new Inferencias(cargaModelos("assets.tflite"));
                // Cargando imagen
                if (pendientes == 1){
                    inputImage = ((BitmapDrawable)Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.limon))).getBitmap();
                }else if (pendientes == 2){
                    inputImage = ((BitmapDrawable)Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.limon2))).getBitmap();
                }else{
                    inputImage = ((BitmapDrawable)Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.limon3))).getBitmap();
                }
                List<DetectionResult> resultado = inModel.detectar(inputImage);
                detecciones = detecciones + resultado.size();
                verDeteccion(resultado);
                pendientes++;

                if (pendientes > muestra){
                    siguiente.setText("Estimar");
                    operar = true;

                }else{
                    arbolesPendientes.setText("Arboles: "+pendientes+"/"+muestra);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else{
            sigActivity();
        }

    }

    private void sigActivity() {
        promedio = detecciones / muestra;
        int cantidadFrutos = promedio * arboles;
        System.out.println("ESTIMACION: "+cantidadFrutos);
        Intent intent = new Intent(this, ResultadosDeteccion.class);
        intent.putExtra("fruto", fruto);
        intent.putExtra("arboles", arboles);
        intent.putExtra("muestra", muestra);
        intent.putExtra("detecciones", detecciones);
        intent.putExtra("promedio", promedio);
        intent.putExtra("estimacion", cantidadFrutos);
        startActivity(intent);
    }
}