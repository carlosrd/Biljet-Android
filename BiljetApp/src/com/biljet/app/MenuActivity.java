package com.biljet.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.biljet.adaptadores.AdaptadorOpcionesMenu;
import com.biljet.adaptadores.HeaderActividades;


public class MenuActivity extends HeaderActividades {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		// ACTION BAR
		// **************************************************************************************
		//cabecera(false, R.drawable.header_menu, MenuActivity.class, "Menu Principal", true, R.drawable.perfil, MiPerfilActivity.class);
		inicializaVistaHeader(R.drawable.imagen_menu,"Menu Principal", R.drawable.perfil,true);
		setActionBotonDcho(MiPerfilActivity.class);
		
        
        // GRID VIEW
		// **************************************************************************************
        // Acoplar el adaptador al GridView
        
		AdaptadorOpcionesMenu adaptador = new AdaptadorOpcionesMenu(this);
		GridView menuGrid = (GridView)findViewById(R.id.gridViewMenuPrincipal);
		
		// Setear oyentes OnClick
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int position, long id) {
						//Acciones necesarias al hacer click
							switch(position){
								case 0: Intent boton0 = new Intent(MenuActivity.this, ProximosEventosActivity.class);
										startActivity(boton0);
										break;
								case 1: Intent boton1 = new Intent(MenuActivity.this, MisEventosActivity.class);
										startActivity(boton1);
										break;
								case 2: Intent boton2 = new Intent(MenuActivity.this, MisAmigosActivity.class);
										startActivity(boton2);
										break;
							}
						}
		});
		
		menuGrid.setAdapter(adaptador);
	}	

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				builder.setMessage("¿Seguro que deseas salir?");
				builder.setTitle("Biljet");
				builder.setIcon(android.R.drawable.ic_dialog_info);
				builder.setCancelable(false);
				builder.setPositiveButton("Sí",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
								System.exit(RESULT_OK);
							}
						});
				builder.setNegativeButton("No",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
							}
						});
				break;
		}
		AlertDialog alert = builder.create();
		alert.show();
		return true;
	}
	
	// TECLA SUBMENU / BOTON ···
	// **************************************************************************************
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_menu, menu);
	
		return true;
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.subMenuConfigBotonAcercaDe:
	        	Toast.makeText(this,"Aqui explicamos en que consiste la aplicación",Toast.LENGTH_SHORT).show(); 
	        	break;
		    case R.id.subMenuConfigBotonSalir: 
		    	finish();
		    	break;
	    }
	    return true;
	}
}