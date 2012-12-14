package com.biljet.app;

import com.biljet.app.R.drawable;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NuevoAmigoActivity extends PlantillaMenu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo_amigo);

		cabecera(true, android.R.drawable.ic_menu_revert, AmigosActivity.class, "Nuevo Amigo", false, drawable.perfil, MenuActivity.class);
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_nuevo_amigo, menu);
        return true;
    }
}
