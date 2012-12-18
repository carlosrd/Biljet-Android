package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;

import com.biljet.app.menu.HeaderActividades;

public class AmigosActivity extends HeaderActividades {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        
        //cabecera(true, drawable.home, MenuActivity.class, "Amigos", true, R.drawable.nuevoamigo, NuevoAmigoActivity.class);
		inicializaVistaHeader(R.drawable.header_back_button,"Amigos", R.drawable.nuevoamigo,true);
		setBotonVolver();
		setActionBotonDcho(NuevoAmigoActivity.class);
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_amigos, menu);
        return true;
    }
}
