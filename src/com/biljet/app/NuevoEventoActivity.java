package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;

public class NuevoEventoActivity extends PlantillaMenu{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_evento);
        
        cabecera(true, android.R.drawable.ic_menu_revert, ProximosEventosActivity.class, "Nuevo Evento", false, R.drawable.amigo, MisEventosActivity.class);
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_nuevo_evento, menu);
        return true;
    }
}
