package com.example.biljetapp;

import android.os.Bundle;
import android.view.Menu;

import com.example.biljetapp.R.drawable;

public class MiPerfilActivity extends PlantillaMenu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);
        
        // personalizamos la cabecera
        cabecera(true, drawable.home, HomeActivity.class, "Mi Perfil", false, android.R.drawable.ic_menu_revert, HomeActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mi_perfil, menu);
        return true;
    }
    
} // MiPerfilActivity
