package com.example.selectagoapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class AcercaDe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);

        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            //Poner el ícono al ActionBar
            actionBar.setIcon(R.drawable.tfl_logo);
            actionBar.setDisplayShowHomeEnabled(true);
        }

    }

    /*
    public void fnEnviarCorreo(View view) {
        String email = "ejemplo@correo.com";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + email));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
     */

    public void fnFacebook(View view) {
        String url = "https://www.facebook.com/profile.php?id=100086748576755";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    /*public void fnInstagram(View view) {
        String url = "https://www.facebook.com/profile.php?id=100086748576755";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }*/
}