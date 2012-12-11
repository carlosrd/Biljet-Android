package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;

import com.biljet.app.R.drawable;

public class MiPerfilActivity extends PlantillaMenu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);

        cabecera(true, drawable.home, MenuActivity.class, "Mi Perfil", false, android.R.drawable.ic_input_add, NuevoEventoActivity.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mi_perfil, menu);
        return true;
    }
}
