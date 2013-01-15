package com.biljet.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.biljet.adaptadores.AdaptadorProximosEventos;
import com.biljet.adaptadores.HeaderActividades;
import com.biljet.tipos.Evento;
import com.biljet.tipos.Fecha;

public class MisEventosActivity extends HeaderActividades {

	// ATRIBUTOS
 	// **************************************************************************************
	   
	final ArrayList<Evento> itemsEventosIr = obtenerEventosIr();
	final ArrayList<Evento> itemsEventosOrg = obtenerEventosOrg();
	final String[] opTipoEvento = new String[] {"Eventos a los que asistirás:",
											 	"Eventos que organizas:" };
	
	final AdaptadorProximosEventos adaptadorListaEventosIr = new AdaptadorProximosEventos(this,itemsEventosIr);
	final AdaptadorProximosEventos adaptadorListaEventosOrg = new AdaptadorProximosEventos(this,itemsEventosOrg);
	
	boolean esEventoPropio = false;
	
	// OnCreate()
 	// **************************************************************************************
	  
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_eventos);

        // ACTION BAR
     	// **************************************************************************************
        
        inicializaVistaHeader(R.drawable.header_back_button,"Mis Eventos", android.R.drawable.ic_input_add,true);
		setBotonVolver();
		setActionBotonDcho(NuevoEventoActivity.class);
		
		// LIST VIEW
		// **************************************************************************************

		final ListView listaEventos = (ListView)findViewById(R.id.listaMisEventos);
        
		// Setear oyentes OnClick
        
        listaEventos.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int idEvento, long id) {
						//Acciones necesarias al hacer click
							
							Intent intentEvento = new Intent(MisEventosActivity.this, VistaEventoActivity.class);
							
							Bundle bundleDatos = new Bundle();
							
							//En realidad creo que se le puede pasar el tipo Evento usand (ya lo probaremos)
							bundleDatos.putInt("IMAGEN-URL", itemsEventosIr.get(idEvento).getImagen());
							bundleDatos.putString("NOMBRE", itemsEventosIr.get(idEvento).getNombre());
							bundleDatos.putString("TIPO", itemsEventosIr.get(idEvento).getTipo());
							bundleDatos.putString("LUGAR", itemsEventosIr.get(idEvento).getLugar());
							bundleDatos.putInt("PRECIO", itemsEventosIr.get(idEvento).getPrecio());
							bundleDatos.putInt("PERSONAS", itemsEventosIr.get(idEvento).getNumPersonasAsistir());
							bundleDatos.putInt("AFORO", itemsEventosIr.get(idEvento).getAforo());
							bundleDatos.putString("INFO", itemsEventosIr.get(idEvento).getInfoEvento());
							
							bundleDatos.putBoolean("PROPIO?", esEventoPropio);
							
							intentEvento.putExtras(bundleDatos);
					
							startActivity(intentEvento);
										
							}
						});
        
        listaEventos.setAdapter(adaptadorListaEventosIr);
    	
        // SPINNER: TIPO EVENTO (Asistir/Organizado)
     	// **************************************************************************************
		
		ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, opTipoEvento);
		
		final Spinner cmbOpciones = (Spinner)findViewById(R.id.spinnerMisEventos);
		adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		cmbOpciones.setAdapter(adaptadorSpinner);
		
		cmbOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
													@Override
													public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) {
														
														switch(position){
															// Eventos a los que asisto
															case 0: listaEventos.setAdapter(adaptadorListaEventosIr);
																	esEventoPropio = false;
																	break;
							
															// Eventos que organizo
															case 1: listaEventos.setAdapter(adaptadorListaEventosOrg);
																	esEventoPropio = true;
																	break;
														}
														
													}

													@Override
													public void onNothingSelected(AdapterView<?> arg0){
														// ...
													}
													
											  });
		
	} // OnCreate()

    private ArrayList<Evento> obtenerEventosIr() {
	    
    	ArrayList<Evento> Opciones = new ArrayList<Evento>();
	     
	    Evento evento1 = new Evento("Concierto Jessie J",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Fecha(20,7,2013,20,30), 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Evento evento2 = new Evento("Cumpleaños Hugo",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Fecha(15,2,2013,19,45), 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);

		Opciones.add(evento1);
	    Opciones.add(evento2);

	    return Opciones;
	}
    
    private ArrayList<Evento> obtenerEventosOrg() {
	    
    	ArrayList<Evento> Opciones = new ArrayList<Evento>();
	     
		Evento evento1 = new Evento("Cine Forum",1 ,R.drawable.jessie_j_evento ,"Cine", "Madrid", new Fecha(24,12,2012,21,00), 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
	    
		Opciones.add(evento1);
		
	    return Opciones;
    }
		

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mis_eventos, menu);
        return true;
    }
}

