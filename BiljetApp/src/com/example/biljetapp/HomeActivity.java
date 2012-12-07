package com.example.biljetapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.GridView;

import com.example.biljetapp.R.drawable;

public class HomeActivity extends PlantillaMenu{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        // personalizamos la cabecera
        cabecera(false, drawable.home, HomeActivity.class, "HOME", true, drawable.perfil, MiPerfilActivity.class);
        
        AdaptadorOpcionesMenu adaptador = new AdaptadorOpcionesMenu(this);
		GridView menuGrid = (GridView)findViewById(R.id.gridView1);

		menuGrid.setAdapter(adaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    public void onButtonClicker(View v){
    	Intent intent;
    	switch (v.getId()) {
    		case R.id.gridView1:	// Eventos Prçoximos
	    		intent = new Intent(this, EventosProximosActivity.class);
				startActivity(intent);
				break;
    		case R.id.idBotonUno:	// Mis Eventos
	    		intent = new Intent(this, MisEventosActivity.class);
				startActivity(intent);
				break;
    		case R.id.idBotonDos:	// Amigos
	    		intent = new Intent(this, AmigosActivity.class);
				startActivity(intent);
				break;
				
    		default:
    		   break;

    	} // switch
    	
    } // onButtonClicker
    
} // HomeActivity
