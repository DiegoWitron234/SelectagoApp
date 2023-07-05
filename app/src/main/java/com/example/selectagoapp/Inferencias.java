package com.example.selectagoapp;

import android.graphics.Bitmap;
import android.graphics.RectF;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inferencias {

    private Interpreter interpreter;
    private static final int NUM_THREADS = 2;

    public Inferencias (MappedByteBuffer modelo){
        Interpreter.Options options = new Interpreter.Options();
        options.setNumThreads(NUM_THREADS);
        interpreter = new Interpreter(modelo, options);
    }

    public List<DetectionResult> detectar(Bitmap bitmapImage) {
        /* VARIABLES DONDE SE ALMACENARAN LOS RESULTADOS. VERIFICAR EL ORDEN DE SALIDA DE LOS
         METADATOS (OUTPUTS)*/

        float[] numDetecciones = new float[1];
        float[][][] boundingBoxes = new float[1][10][4];
        float[][] scores = new float[1][10];
        float[][] categoria = new float[1][10];

        TensorImage imageAjustada = ajusteImagen(bitmapImage);

        Object[] inputs = {imageAjustada.getBuffer()};
        Map<Integer, Object> outputs = new HashMap<>();

        // INSTANCIANDO SALIDAS DE RESULTADOS
        outputs.put(0, scores);
        outputs.put(1, boundingBoxes);
        outputs.put(2, numDetecciones);
        outputs.put(3, categoria);

        // Ejecucion de Inferencia
        interpreter.runForMultipleInputsOutputs(inputs, outputs);

        // RESULTADOS
        System.out.println("SCORES: "+ Arrays.deepToString(scores));
        System.out.println("DETECCIONES: "+ Arrays.deepToString(boundingBoxes));
        System.out.println("NUM DETECCIONES: "+ Arrays.toString(numDetecciones));
        System.out.println("CATEGORIA: "+ Arrays.deepToString(categoria));

        List<DetectionResult> resultados = obtenerResultadosDeteccion(scores,
                boundingBoxes, categoria, numDetecciones, imageAjustada.getBitmap());

        return resultados;
    }

    private TensorImage ajusteImagen(Bitmap bitmap){
        // Se crea objeto ImageProcessor para tratar la imagen
        // 640 es la dimensión de imagen configurada en el modelo
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(512, 512, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(new NormalizeOp(-1, 1))
                        .build();

        // Crea un objeto TensorImage
        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        // Cargamos al objeto TensorImage la imagen
        tensorImage.load(bitmap);
        // Procesamos el objeto
        tensorImage = imageProcessor.process(tensorImage);
        return tensorImage;
    }

    private List<DetectionResult> obtenerResultadosDeteccion(float[][] scores, float[][][] boundingBoxes, float[][] categoria, float[] numDetecciones, Bitmap bitmapImage) {
        List<DetectionResult> resultados = new ArrayList<>();

        int totalDetecciones = (int) numDetecciones[0];
        for (int i = 0; i < totalDetecciones; i++) {
            float score = scores[0][i];
            float left = boundingBoxes[0][i][1] * bitmapImage.getWidth();
            float top = boundingBoxes[0][i][0]* bitmapImage.getHeight();
            float right = boundingBoxes[0][i][3]* bitmapImage.getWidth();
            float bottom = boundingBoxes[0][i][2]* bitmapImage.getHeight();
            System.out.println(left +" "+top +" "+ right +" "+ bottom);
            RectF boundingBox = new RectF(left, top, right, bottom);
            // Crear un objeto DetectionResult y agregarlo a la lista
            DetectionResult resultado = new DetectionResult(score, boundingBox);
            resultados.add(resultado);
        }
        return resultados;
    }
}
