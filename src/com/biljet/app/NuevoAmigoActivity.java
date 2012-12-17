package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;

import com.biljet.app.menu.HeaderActividades;

public class NuevoAmigoActivity extends HeaderActividades {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_amigo);

		//cabecera(true, android.R.drawable.ic_menu_revert, AmigosActivity.class, "Nuevo Amigo", false, drawable.perfil, MenuActivity.class);
		inicializaVistaHeader(R.drawable.header_back_button,"Nuevo Amigo", R.drawable.perfil,false);
		setBotonVolver();
		//setActionBotonDcho(NuevoAmigoActivity.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_nuevo_amigo, menu);
        return true;
    }
}
