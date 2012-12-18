package com.biljet.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.biljet.app.R.drawable;
import com.biljet.app.menu.AdaptadorOpcionesEvento;

import eventos.ACDCActivity;
import eventos.EventoJessieJActivity;

public class ProximosEventosActivity extends PlantillaMenu {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximos_eventos);

        cabecera(true, drawable.home, MenuActivity.class, "Proximos Eventos", true, android.R.drawable.ic_menu_search, ProximosEventosActivity.class);

        AdaptadorOpcionesEvento adaptador = new AdaptadorOpcionesEvento(this);
        GridView menuEventos = (GridView)findViewById(R.id.gridView2);
        
		// Setear oyentes OnClick
        menuEventos.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int evento, long id) {
						//Acciones necesarias al hacer click
							switch(evento){
								case 0: Intent evento1 = new Intent(ProximosEventosActivity.this, ACDCActivity.class);
										startActivity(evento1);
										break;
								case 1: Intent evento2 = new Intent(ProximosEventosActivity.this, EventoJessieJActivity.class);
										startActivity(evento2);
										break;
								case 2: Intent evento3 = new Intent(ProximosEventosActivity.this, AmigosActivity.class);
										startActivity(evento3);
										break;
							}
						}
		});
        
        menuEventos.setAdapter(adaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_proximos_eventos, menu);
        return true;
    }
}
