package com.biljet.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.biljet.app.menu.AdaptadorOpcionesMenu;

public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		
		// ACTION BAR
		// **************************************************************************************
		// Inflar y setear el Action Bar
		
		ViewStub stub = (ViewStub) findViewById(R.id.actionBar);
        View inflated = stub.inflate();
        
        TextView headerTextoMenu = (TextView)inflated.findViewById(R.id.headerMainText);
        headerTextoMenu.setText("Menu Principal");
        
        ImageButton headerImagenMenu = (ImageButton)inflated.findViewById(R.id.headerMainButton);
        headerImagenMenu.setImageResource(R.drawable.header_menu);
        
        
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
								case 0: Intent intent = new Intent(MenuActivity.this, Seccion1.class);
										startActivity(intent);
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
