package com.example.biljetapp;

import android.os.Bundle;
import android.view.Menu;

import com.example.biljetapp.R.drawable;

public class AmigosActivity extends PlantillaMenu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        
        // personalizamos la cabecera
        cabecera(false, drawable.home, HomeActivity.class, "Mis Amigos", true, drawable.nuevoamigo, NuevoAmigoActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_amigos, menu);
        return true;
    }
}
