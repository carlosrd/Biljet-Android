package com.biljet.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.biljet.app.menu.AdaptadorOpcionesMenu;

public class MenuActivity extends PlantillaMenu {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		cabecera(false, R.drawable.header_menu, MenuActivity.class, "Menu Principal", true, R.drawable.perfil, MiPerfilActivity.class);
		
		
		// ACTION BAR
		// **************************************************************************************
		// Inflar y setear el Action Bar
		
		/*ViewStub stub = (ViewStub) findViewById(R.id.actionBar);
        View inflated = stub.inflate();
        
        TextView headerTextoMenu = (TextView)inflated.findViewById(R.id.headerMainText);
        headerTextoMenu.setText("Menu Principal");
        
        ImageButton headerImagenMenu = (ImageButton)inflated.findViewById(R.id.headerMainButton);
        headerImagenMenu.setImageResource(R.drawable.header_menu);*/
        
        
        // GRID VIEW
		// **************************************************************************************
        // Acoplar el adaptador al GridView
        
		AdaptadorOpcionesMenu adaptador = new AdaptadorOpcionesMenu(this);
		GridView menuGrid = (GridView)findViewById(R.id.gridView1);
		
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
								case 2: Intent boton2 = new Intent(MenuActivity.this, AmigosActivity.class);
										startActivity(boton2);
										break;
							}
						}
		});
		
		menuGrid.setAdapter(adaptador);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_menu, menu);
		return true;
	}

}
