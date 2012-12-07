package com.example.biljetapp;

import android.os.Bundle;
import android.view.Menu;

import com.example.biljetapp.R.drawable;

public class NuevoEventoActivity extends PlantillaMenu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_evento);
        
        // personalizamos la cabecera
        cabecera(false, drawable.home, HomeActivity.class, "Crear nuevo Evento", true, android.R.drawable.ic_menu_revert, MisEventosActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_nuevo_evento, menu);
        return true;
    }
}
