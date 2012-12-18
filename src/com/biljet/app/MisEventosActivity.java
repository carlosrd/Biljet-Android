package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;

import com.biljet.app.menu.HeaderActividades;

public class MisEventosActivity extends HeaderActividades {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_eventos);
        
        //cabecera(true, drawable.home, MenuActivity.class, "Mis Eventos", true, android.R.drawable.ic_input_add, NuevoEventoActivity.class);
		inicializaVistaHeader(R.drawable.header_back_button,"Mis Eventos", android.R.drawable.ic_input_add,true);
		setBotonVolver();
		setActionBotonDcho(NuevoEventoActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mis_eventos, menu);
        return true;
    }
}
