package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;

import com.biljet.adaptadores.HeaderActividades;

public class MiPerfilActivity extends HeaderActividades {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        //cabecera(true, drawable.home, MenuActivity.class, "Mi Perfil", false, android.R.drawable.ic_input_add, NuevoEventoActivity.class);
		inicializaVistaHeader(R.drawable.header_back_button,"Mi Perfil", android.R.drawable.ic_input_add,false);
		setBotonVolver();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mi_perfil, menu);
        return true;
    }
}
