package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;

import com.biljet.adaptadores.HeaderActividades;

public class NuevoEventoActivity extends HeaderActividades {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_evento);
        
        //cabecera(true, android.R.drawable.ic_menu_revert, ProximosEventosActivity.class, "Nuevo Evento", false, R.drawable.amigo, MisEventosActivity.class);
		inicializaVistaHeader(R.drawable.header_back_button,"Nuevo Evento", R.drawable.amigo,false);
		setBotonVolver();
		setActionBotonDcho(MisEventosActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_nuevo_evento, menu);
        return true;
    }
}
