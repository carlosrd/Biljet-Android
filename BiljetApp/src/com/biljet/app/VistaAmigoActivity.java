package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.adaptadores.HeaderActividades;

public class VistaAmigoActivity extends HeaderActividades {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vista_amigo);
		
		Bundle bundleDatos = getIntent().getExtras();
		
		inicializaVistaHeader(R.drawable.header_back_button,"Amigo: "+bundleDatos.getString("Nombre"), R.drawable.perfil,false);
		setBotonVolver();	

		ImageView imagenAmigo = (ImageView)findViewById(R.id.vistaAmigo_ImagenAmigo);	
		
		imagenAmigo.setImageResource(bundleDatos.getInt("Imagen"));	
		imagenAmigo.setScaleType(ImageView.ScaleType.CENTER);
	
		TextView txtNombre = (TextView)findViewById(R.id.vistaAmigo_TxtNombre);
		txtNombre.setText(bundleDatos.getString("Nombre"));
			
		TextView txtLugar = (TextView)findViewById(R.id.vistaAmigo_TxtCiudad);
		txtLugar.setText(bundleDatos.getString("Ciudad"));	
		
		
		TextView txtBiografia = (TextView)findViewById(R.id.vistaAmigo_TxtBiografia);
		txtBiografia.setText(bundleDatos.getString("Biografia"));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_vista_amigo, menu);
		return true;
	}
}
