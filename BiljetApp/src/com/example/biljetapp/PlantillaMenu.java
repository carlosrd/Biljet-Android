package com.example.biljetapp;

import com.example.biljetapp.R.drawable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

/** Plantilla de la cabecera que tendrá cada actividad:
 	Para una nueva actividad:
 		.JAVA (añadir)
 		1) extends PlantillaMenu
 		2) cabecera(visibilidad Boton 1, icono Boton 1, actividad Boton 1, texto cabecera, visibilidad Boton 2, icono Boton 2, actividad Boton 2);
 		
 		.xml (añadir)
 		<ViewStub
			android:id="@+id/menu_settings"
			android:layout_width="fill_parent"
			android:layout_height="wrap_content"
			android:inflatedId="@+id/menu"
			android:layout="@layout/menu" />
**/

public class PlantillaMenu extends Activity{
	Button botonUno;
	Class actividad1;
	TextView texto;
	Button botonDos;
	Class actividad2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
    // cabecera que queremos que aparezca en todas las actividades
	
	/** cabecera(visible Boton 1?, icono Boton 1, actividad Boton 1, texto cabecera, visible Boton 2?, icono Boton 2, actividad Boton 2) **/
	public void cabecera(boolean boton1, int dibujoUno, Class act1, String title, boolean boton2, int dibujoDos, Class act2){
		ViewStub stub = (ViewStub) findViewById(R.id.menu_settings);
        View inflated = stub.inflate();

        texto = (TextView) inflated.findViewById(R.id.txtHeading);
        texto.setText(title);
        
        // Primer Boton
        
        botonUno = (Button) inflated.findViewById(R.id.idBotonUno);
        if(boton1)	botonUno.setVisibility(View.VISIBLE);
        else		botonUno.setVisibility(View.INVISIBLE);
        botonUno.setBackgroundResource(dibujoUno);
        actividad1 = act1;

        // Segundo Boton
        botonDos = (Button) inflated.findViewById(R.id.idBotonDos);
        if(boton2)	botonDos.setVisibility(View.VISIBLE);
        else		botonDos.setVisibility(View.INVISIBLE);
        botonDos.setBackgroundResource(dibujoDos);
        actividad2 = act2;
        
	} // cabecera
	
	/* Implementación de los botones: Uno y Dos */
    
    public void botonUnoClick(View v){
	    Intent intent = new Intent(getApplicationContext(), actividad1); //actividad1.getClass());//HomeActivity.class);
	    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    startActivity(intent);
    }
    
    public void botonDosClick(View v){
	    Intent intent = new Intent(getApplicationContext(), actividad2); //actividad2.getClass());	//EventosProximosActivity.class);
	    intent.setFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    startActivity(intent);
    }
    
} // PlantillaMenu
