package com.example.biljetapp;

import android.os.Bundle;
import android.view.Menu;

import com.example.biljetapp.R.drawable;

public class EventosProximosActivity extends PlantillaMenu{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventos_proximos);
        
        // personalizamos la cabecera
        cabecera(true, drawable.home, HomeActivity.class, "Eventos Próximos", true, android.R.drawable.ic_menu_my_calendar, MisEventosActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_eventos_proximos, menu);
        return true;
    }
}

