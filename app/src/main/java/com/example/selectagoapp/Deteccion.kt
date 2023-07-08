package com.example.selectagoapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.selectagoapp.ObjectDetectorHelper.DetectorListener
import org.tensorflow.lite.task.vision.detector.Detection





class Deteccion : AppCompatActivity() {

    lateinit var detectar_btn: Button
    lateinit var seleccionar_btn: Button
    lateinit var resultados_txt: TextView
    lateinit var imagen: ImageView
    var bitmap: Bitmap? = null
    lateinit var objectDetectorHelper: ObjectDetectorHelper
    // Variables para almacenar la categoría y el puntaje del objeto detectado
    //var cantidad: Int? = null
    //var puntajes: MutableList<Float> = mutableListOf()
    var imagenSeleccionada: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deteccion)
        val actionBar: ActionBar? = supportActionBar

        if (actionBar != null) {
            //Poner el ícono al ActionBar
            actionBar.setIcon(R.drawable.tfl_logo)
            actionBar.setDisplayShowHomeEnabled(true)
        }

        permisoCamera()

        // Incializamos vistas
        detectar_btn = findViewById(R.id.detect_btn)
        seleccionar_btn = findViewById(R.id.select_btn);
        resultados_txt = findViewById(R.id.resultados_txt);
        imagen = findViewById(R.id.imagen);

        val resourceId = resources.getIdentifier("limonespersas", "drawable", packageName)
        //imagen.setImageResource(resourceId)
        val bitmapOriginal = BitmapFactory.decodeResource(resources, R.drawable.limonespersas)
        bitmap = ajustarBitmap(bitmapOriginal, 512, 512)
        imagen.setImageBitmap(bitmap)

        val objectDetectorListener: DetectorListener = object : DetectorListener {
            override fun onError(error: String) {
                // Manejar el error de detección
                println("Ocurrió un error en la detección")
            }

            override fun onResults(
                results: MutableList<Detection>?,
                inferenceTime: Long,
                imageHeight: Int,
                imageWidth: Int
            ) {
                var categoria: String? = null
                var puntaje: Float? = null
                var cantidad: Int? = null
                // Manejar los resultados de detección
                // Itera sobre los objetos Detection en la lista results
                if (results != null) {
                    //Obtenemos el tamaño de la lista para saber la cantidad de frutos detectados
                    cantidad = results.size
                    for (result in results) {
                        // Obtiene la lista de categorías del objeto Detection
                        val categorias = result.categories

                        // Verifica si hay al menos una categoría en la lista
                        if (categorias.isNotEmpty()) {
                            // Accede a la primera (Y única) categoría en la lista
                            val primeraCategoria = categorias[0]

                            // Guarda la categoría y el puntaje en las variables correspondientes
                            categoria = primeraCategoria.label
                            puntaje = primeraCategoria.score
                            //puntajes.add(puntaje!!)
                            // Rompe el bucle si solo deseas obtener la categoría y el puntaje del primer objeto detectado
                            //break
                        }
                    }
                }
                //Muestra un toast con la cantidad de frutos detectados
                Toast.makeText(applicationContext, "Frutos detectados: $cantidad", Toast.LENGTH_SHORT).show()
                // Imprime la categoría y el puntaje almacenados
                println("Resultados: $results")
                println("Cantidad: $cantidad")
                println("Categoría: $categoria")
                println("Puntaje: $puntaje")
            }
        }


        objectDetectorHelper = ObjectDetectorHelper(
            threshold = 0.5f,
            numThreads = 4,
            maxResults = 30,
            currentDelegate = 0,
            currentModel = 0,
            context = this,
            objectDetectorListener
        )

    }

    override fun onDestroy() {
        super.onDestroy()
        objectDetectorHelper.clearObjectDetector()
    }


    fun seleccionar(view: View?) {
        imagenSeleccionada = 0
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 11)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 11 && resultCode == RESULT_OK){
            try {
                val capturedBitmap = data?.extras?.get("data") as Bitmap
                bitmap = ajustarBitmap(capturedBitmap, 512, 512)
                //bitmap = data?.extras?.get("data") as Bitmap
                imagen.setImageBitmap(bitmap)
            }catch (e: Exception){
                e.printStackTrace()
                println("Ocurrió un error al conseguir la imagen de la cámara")
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    fun permisoCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(
                        Manifest.permission.CAMERA
                    ), 10
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        if (requestCode == 11) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    permisoCamera()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    fun detectar(view: View) {
        //Función del botón que analiza la imagen
        if(bitmap != null){
            objectDetectorHelper.detect(bitmap!!, 0)
        }else{
            println("El bitmap es nulo")
        }
    }

    fun fnCambiarImagen(view: View) {
        if (imagenSeleccionada == 0){
            imagenSeleccionada = 1
            val resourceId = resources.getIdentifier("limon", "drawable", packageName)
            imagen.setImageResource(resourceId)
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.limon)
        } else if (imagenSeleccionada == 1){
            imagenSeleccionada = 2
            val resourceId = resources.getIdentifier("limon2", "drawable", packageName)
            imagen.setImageResource(resourceId)
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.limon2)
        } else if (imagenSeleccionada == 2){
            imagenSeleccionada = 3
            val resourceId = resources.getIdentifier("limon3", "drawable", packageName)
            imagen.setImageResource(resourceId)
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.limon3)
        } else if (imagenSeleccionada == 3){
            imagenSeleccionada = 4
            val resourceId = resources.getIdentifier("limonespersas", "drawable", packageName)
            imagen.setImageResource(resourceId)
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.limonespersas)
        } else if (imagenSeleccionada == 4){
            imagenSeleccionada = 1
            val resourceId = resources.getIdentifier("limon", "drawable", packageName)
            imagen.setImageResource(resourceId)
            bitmap = BitmapFactory.decodeResource(resources, R.drawable.limon)
        } else{
            Toast.makeText(applicationContext, "Ha ocurrido un error con la imagen", Toast.LENGTH_SHORT).show()
        }
    }


    fun ajustarBitmap(bitmap: Bitmap, targetWidth: Int, targetHeight: Int): Bitmap {
        val scaleFactorX = targetWidth.toFloat() / bitmap.width
        val scaleFactorY = targetHeight.toFloat() / bitmap.height

        val scaledWidth = (bitmap.width * scaleFactorX).toInt()
        val scaledHeight = (bitmap.height * scaleFactorY).toInt()

        return Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
    }

}