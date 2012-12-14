package com.biljet.app;

import com.biljet.app.R.drawable;

import android.os.Bundle;
import android.view.Menu;

public class AmigosActivity extends PlantillaMenu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        
        cabecera(true, drawable.home, MenuActivity.class, "Amigos", true, R.drawable.nuevoamigo, NuevoAmigoActivity.class);
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_amigos, menu);
        return true;
    }
}
