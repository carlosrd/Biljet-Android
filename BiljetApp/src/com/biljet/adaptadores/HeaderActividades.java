package com.biljet.adaptadores;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.TextView;

import com.biljet.app.R;



	/* Plantilla de la cabecera que tendrá cada actividad:
	   ***************************************************
		En cada nueva actividad hay que añadir:
		
		* En el archivo.JAVA :
		1) extends HeaderActividades
		2) cabecera(visibilidad Boton 1, icono Boton 1, actividad Boton 1, texto cabecera, visibilidad Boton 2, icono Boton 2, actividad Boton 2);
	
		* En el archivo.XML :
			<ViewStub
			android:id="@+id/menu_settings"
			android:layout_width="fill_parent"
			android:layout_height="wrap_content"
			android:inflatedId="@+id/menu"
			android:layout="@layout/menu" />
		*/

public class HeaderActividades extends Activity {
	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		// Inicializa la cabecera para todas las secciones
		public void inicializaVistaHeader(int imagenBotonIzq,
									  	  String tituloSeccion,
									  	  int imagenBotonDcho,
									  	  boolean visibilidadBotonDcho){
			
			ViewStub stub = (ViewStub) findViewById(R.id.actionBar);
	        View inflated = stub.inflate();
	        
	        TextView titulo = (TextView) inflated.findViewById(R.id.headerTextoPpal);
	        titulo.setText(tituloSeccion);
	        titulo.setVisibility(View.VISIBLE);
	        
	        ImageButton botonIzq = (ImageButton) inflated.findViewById(R.id.headerBotonIzq);
			botonIzq.setImageResource(imagenBotonIzq);
			
			ImageButton botonDcho = (ImageButton) inflated.findViewById(R.id.headerBotonDcho);
			botonDcho.setImageResource(imagenBotonDcho);
			
	        if (!visibilidadBotonDcho) {
	        	botonDcho.setVisibility(View.INVISIBLE);
	        }
		}
		
		// Setea el boton izquierdo como boton para volver a la actividad anterior
		public void setBotonVolver(){
			ImageButton botonVolver = (ImageButton) findViewById(R.id.headerBotonIzq);
	        botonVolver.setOnClickListener(new OnClickListener() {
		        							   public void onClick(View arg0) {
		        								   finish();		// Terminar actividad y volver a la anterior (menu)
		        							   }
	        							   });
		}
			
		
		// Setea el intent para el boton derecho
		@SuppressWarnings("rawtypes")
		public void setActionBotonDcho(final Class actA){
			ImageButton botonDcho = (ImageButton) findViewById(R.id.headerBotonDcho);
	        botonDcho.setOnClickListener(new OnClickListener() {
										     public void onClick(View arg0) {
										    	 Intent boton0 = new Intent(getApplicationContext(),actA);
										    	 startActivity(boton0);
										     }
									     });
		}

		/*
		// !!! ESTE METODO NO IRA EN ESTA CLASE, 
		public void creaEvento(String nombre, int id, String tipo, String lugar, Fecha fecha, int precio, int im){
			
			ViewStub stub = (ViewStub) findViewById(R.id.menu_settings1);
	        View inflated = stub.inflate();
	        TextView texto;
	        
	        texto = (TextView) inflated.findViewById(R.id.textView1);
	        texto.setText("Nombre del evento: " + nombre);
	        
	        texto = (TextView) inflated.findViewById(R.id.textView2);
	        texto.setText("ID: " + id);
	        
	        texto = (TextView) inflated.findViewById(R.id.textView3);
	        texto.setText("Tipo de evento: " + tipo);
	        
	        texto = (TextView) inflated.findViewById(R.id.textView4);
	        texto.setText("Lugar: " + lugar);
	        
	        texto = (TextView) inflated.findViewById(R.id.textView5);
	        texto.setText("Fecha: " + fecha.getDia() + "/" + fecha.getMes() + "/" + fecha.getAño() + " a las " + fecha.getHora().getHoras() + ":" + fecha.getHora().getMinutos());
	        
	        texto = (TextView) inflated.findViewById(R.id.textView6);
	        texto.setText("Precio de la entrada: " + precio + " €");
	        
	        ImageView imagen;
	        imagen = (ImageView) inflated.findViewById(R.id.imageView1);
	        texto.setText("Precio de la entrada: " + precio + " €");
	        imagen.setImageResource(im);

		} // creaEvento
		*/
} // HeaderActividades

	
	
	
	

