package com.example.selectagoapp;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.Objects;

public class TFModelo extends AppCompatActivity {
    ImageView visualizacion;
    private Interpreter interpreter;
    private static final int NUM_THREADS = 2;
    private Bitmap inputImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tfmodelo);
        visualizacion = findViewById(R.id.resDeteccion);
        try {
            // Cargar el modelo TFLite al iniciar la actividad
            Inferencias inModel = new Inferencias(cargaModelos("assets.tflite"));
            // Cargando imagen
            inputImage = ((BitmapDrawable)Objects.requireNonNull(ContextCompat.getDrawable(this, R.drawable.limon))).getBitmap();
            List<DetectionResult> resultado = inModel.detectar(inputImage);
            verDeteccion(resultado);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    }
}