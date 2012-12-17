package com.biljet.app;

import com.biljet.app.R.drawable;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MisEventosActivity extends PlantillaMenu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_eventos);
        
        cabecera(true, drawable.home, MenuActivity.class, "Mis Eventos", true, android.R.drawable.ic_input_add, NuevoEventoActivity.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mis_eventos, menu);
        return true;
    }
    
}
