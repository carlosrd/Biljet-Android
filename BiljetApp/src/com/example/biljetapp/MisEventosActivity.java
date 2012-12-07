package com.example.biljetapp;

import android.os.Bundle;
import android.view.Menu;

import com.example.biljetapp.R.drawable;

public class MisEventosActivity extends PlantillaMenu{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_eventos);
        
        // personalizamos la cabecera
        cabecera(true, drawable.home, HomeActivity.class, "Mis Eventos", true, android.R.drawable.ic_input_add, NuevoEventoActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mis_eventos, menu);
        return true;
    }
}
