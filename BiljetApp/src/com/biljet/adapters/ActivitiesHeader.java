package com.biljet.adapters;

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

public class ActivitiesHeader extends Activity {
	
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}
		
		// Inicializa la cabecera para todas las secciones
		public void createHeaderView(int leftButtonImage,
									 String activityTitle,
									 int rightButtonImage,
									 boolean rightButtonVisibility){
			
			ViewStub stub = (ViewStub) findViewById(R.id.actionBar);
	        View inflated = stub.inflate();
	        
	        TextView title = (TextView) inflated.findViewById(R.id.headerTextoPpal);
	        title.setText(activityTitle);
	        title.setVisibility(View.VISIBLE);
	        
	        ImageButton leftButton = (ImageButton) inflated.findViewById(R.id.headerBotonIzq);
			leftButton.setImageResource(leftButtonImage);
			
			ImageButton rightButton = (ImageButton) inflated.findViewById(R.id.headerBotonDcho);
			rightButton.setImageResource(rightButtonImage);
			
	        if (!rightButtonVisibility) {
	        	rightButton.setVisibility(View.INVISIBLE);
	        }
		}
		
		// Setea el boton izquierdo como boton para volver a la actividad anterior
		public void setBackButton(){
			ImageButton backButton = (ImageButton) findViewById(R.id.headerBotonIzq);
	        backButton.setOnClickListener(new OnClickListener() {
		        							   public void onClick(View arg0) {
		        								   finish();		// Terminar actividad y volver a la anterior (menu)
		        							   }
	        							   });
		}
			
		
		// Setea el intent para el boton derecho
		@SuppressWarnings("rawtypes")
		public void setRightButtonAction(final Class actA){
			ImageButton rightButton = (ImageButton) findViewById(R.id.headerBotonDcho);
	        rightButton.setOnClickListener(new OnClickListener() {
										     public void onClick(View arg0) {
										    	 Intent boton0 = new Intent(getApplicationContext(),actA);
										    	 startActivity(boton0);
										     }
									     });
		}
		
		// Setea el intent para el boton derecho
		@SuppressWarnings("rawtypes")
		public void setRightButtonAction(final Class actA, final char c){
			ImageButton rightButton = (ImageButton) findViewById(R.id.headerBotonDcho);
	        rightButton.setOnClickListener(new OnClickListener() {
										     public void onClick(View arg0) {
										    	 Intent boton0 = new Intent(ActivitiesHeader.this, actA);
										    	 Bundle dataBundle = new Bundle();
										    	 dataBundle.putChar("amigo_evento", c);
										    	 boton0.putExtras(dataBundle);
										    	 startActivity(boton0);
										     }
									     });
		}
	
} // HeaderActividades

		
		
		
		

