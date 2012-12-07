package com.example.biljetapp;

import android.os.Bundle;
import android.view.Menu;

import com.example.biljetapp.R.drawable;

public class NuevoAmigoActivity extends PlantillaMenu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_amigo);
        
        // personalizamos la cabecera
        cabecera(true, android.R.drawable.ic_menu_revert, AmigosActivity.class, "Nuevo Amigo", false, drawable.perfil, AmigosActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_nuevo_amigo, menu);
        return true;
    }
}
