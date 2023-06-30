package com.example.selectagoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.selectagoapp.ml.Metadata;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ModeloIA extends AppCompatActivity {
    Bitmap bitmap;
    Button seleccionar_btn, detectar_btn;
    TextView resultados_txt;
    ImageView imagen;
    String[]labels = new String[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modelo_ia);

        permisoCamera();

        // Incializamos vistas
        detectar_btn = findViewById(R.id.detect_btn);
        seleccionar_btn = findViewById(R.id.select_btn);
        resultados_txt = findViewById(R.id.resultados_txt);
        imagen = findViewById(R.id.imagen);
    }

    public void detectar(View view) {
        try {
            Metadata model = Metadata.newInstance(this);

            TensorImage byteBuffer = ajusteImagen(bitmap);
            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 512, 512, 3}, DataType.FLOAT32);
            inputFeature0.loadBuffer(byteBuffer.getBuffer());

            // Runs model inference and gets result.
            Metadata.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getCategoryAsTensorBuffer();

            int resultado = getMax(outputFeature0.getFloatArray());

            if (resultado == 0){
                resultados_txt.setText("Limon");
            }else{
                resultados_txt.setText("No hay limones identificados");
            }

            //Toast.makeText(this, "Resultado: "+getMax(outputFeature0.getFloatArray()), Toast.LENGTH_LONG).show();
            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }

    }
    private void etiquetas(){
        int cont = 0;
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(getAssets().open("labelmap.txt")));
            String line = bf.readLine();
            while(line != null){
                labels[cont] = line;
                cont++;
                line = bf.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getMax(float[] arr){
        int max = 0;
        for (int i =0; i<arr.length; i++){
            if(arr[i]>arr[max]) max=i;
        }
        return max;
    }

    public TensorImage ajusteImagen(Bitmap bitmap){
        // Se crea objeto ImageProcessor para tratar la imagen
        // 640 es la dimensión de imagen configurada en el modelo
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(512, 512, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(new NormalizeOp(127.5F, 127.5F))
                        .build();

        // Crea un objeto TensorImage
        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        // Cargamos al objeto TensorImage la imagen
        tensorImage.load(bitmap);
        // Procesamos el objeto
        tensorImage = imageProcessor.process(tensorImage);
        return tensorImage;
    }

    public void seleccionar(View view){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 11);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 11){
            try{
                bitmap = (Bitmap) data.getExtras().get("data");
                imagen.setImageBitmap(bitmap);
            }
            catch(NullPointerException e){

            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void permisoCamera(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA
                }, 10);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 11){
            if(grantResults.length > 0){
                if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
                    this.permisoCamera();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

