package com.biljet.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.adaptadores.HeaderActividades;

public class VistaEventoActivity extends HeaderActividades {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vista_evento);
		
		Bundle bundleDatos = getIntent().getExtras();
		
		inicializaVistaHeader(R.drawable.header_back_button,"Evento: "+bundleDatos.getString("NOMBRE"), R.drawable.perfil,false);
		setBotonVolver();
		

		ImageView imagenEvento = (ImageView)findViewById(R.id.vistaEvento_ImagenEvento);
		imagenEvento.setImageResource(bundleDatos.getInt("IMAGEN-URL"));
		imagenEvento.setScaleType(ImageView.ScaleType.CENTER);
	
		Button botonLeerQR = (Button)findViewById(R.id.vistaEvento_BotonLeerQR);
		if (bundleDatos.getBoolean("PROPIO?")){
			botonLeerQR.setVisibility(View.VISIBLE);
	        botonLeerQR.setOnClickListener(new OnClickListener() {
		        							   public void onClick(View arg0) {
		        								   // Intent para lanzar el lector de QR
		        								   Intent intent = new Intent("com.biljet.app.SCAN");
		        								   intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		        								   startActivityForResult(intent, 0);
		        							   }
	        							   });
			}
		else
			botonLeerQR.setVisibility(View.INVISIBLE);
		
		TextView txtNombre = (TextView)findViewById(R.id.vistaEvento_TxtNombre);
		txtNombre.setText(bundleDatos.getString("NOMBRE"));
	
		TextView txtTipo = (TextView)findViewById(R.id.vistaEvento_TxtTipo);
		txtTipo.setText(bundleDatos.getString("TIPO"));
		
		TextView txtLugar = (TextView)findViewById(R.id.vistaEvento_TxtLugar);
		txtLugar.setText(bundleDatos.getString("LUGAR"));
	
		TextView txtPrecio = (TextView)findViewById(R.id.vistaEvento_TxtPrecio);
		int auxInt = bundleDatos.getInt("PRECIO");
		String auxString = auxInt+"";
		txtPrecio.setText(auxString);
		
		TextView txtInvitados = (TextView)findViewById(R.id.vistaEvento_TxtNumInvitados);
		auxInt = bundleDatos.getInt("PERSONAS");
		auxString = auxInt+"";
		txtInvitados.setText(auxString);

		
		TextView txtAforo = (TextView)findViewById(R.id.vistaEvento_TxtAforo);
		auxInt = bundleDatos.getInt("AFORO");
		auxString = auxInt+"";
		txtAforo.setText(auxString);

		
		TextView txtInfo = (TextView)findViewById(R.id.vistaEvento_TxtInfo);
		txtInfo.setText(bundleDatos.getString("INFO"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_vista_evento, menu);
		return true;
	}

}
