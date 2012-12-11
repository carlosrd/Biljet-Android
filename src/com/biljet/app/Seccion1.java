package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;

import com.biljet.app.R.drawable;

public class Seccion1 extends PlantillaMenu {

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seccion1);
		
		cabecera(true, R.drawable.header_back_button, MenuActivity.class, "Seccion 1", false, drawable.perfil, MenuActivity.class);
		
		
		// ACTION BAR
		// **************************************************************************************
		// Inflar y setear el Action Bar
		
		/*ViewStub stub = (ViewStub) findViewById(R.id.actionBar);
        View inflated = stub.inflate();
        
        TextView headerTextoMenu = (TextView)inflated.findViewById(R.id.headerMainText);
        headerTextoMenu.setText("Seccion 1");*/
        
        /*ImageButton headerImagenMenu = (ImageButton)inflated.findViewById(R.id.headerMainButton);
        headerImagenMenu.setImageResource(R.drawable.header_back_button);
        headerImagenMenu.setOnClickListener(new OnClickListener() {
        								public void onClick(View arg0) {
        									finish();		// Terminar actividad y volver a la anterior (menu)
        								}
        							});*/

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_seccion1, menu);
		return true;
	}

}
