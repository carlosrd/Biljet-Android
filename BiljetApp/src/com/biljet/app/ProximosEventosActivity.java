package com.biljet.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.biljet.adaptadores.AdaptadorProximosEventos;
import com.biljet.adaptadores.HeaderActividades;
import com.biljet.tipos.Evento;
import com.biljet.tipos.Fecha;

public class ProximosEventosActivity extends HeaderActividades {

	final ArrayList<Evento> itemsEvento = obtenerEventos();;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximos_eventos);
     
        // ACTION BAR
     	// **************************************************************************************
        //cabecera(true, drawable.home, MenuActivity.class, "Proximos Eventos", true, android.R.drawable.ic_menu_search, ProximosEventosActivity.class);
		inicializaVistaHeader(R.drawable.header_back_button,"Proximos Eventos", android.R.drawable.ic_menu_search,true);
		setBotonVolver();
		setActionBotonDcho(ProximosEventosActivity.class);
		
		
		// LIST VIEW
		// **************************************************************************************
        
	

		AdaptadorProximosEventos adaptador = new AdaptadorProximosEventos(this,itemsEvento);
        ListView listaEventos = (ListView)findViewById(R.id.listaEventos);
        
		// Setear oyentes OnClick
        
        listaEventos.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int idEvento, long id) {
						//Acciones necesarias al hacer click
							
							Intent intentEvento = new Intent(ProximosEventosActivity.this, VistaEventoActivity.class);
							
							Bundle bundleDatos = new Bundle();
							
							//En realidad creo que se le puede pasar el tipo Evento usand (ya lo probaremos)
							bundleDatos.putInt("IMAGEN-URL", itemsEvento.get(idEvento).getImagen());
							bundleDatos.putString("NOMBRE", itemsEvento.get(idEvento).getNombre());
							bundleDatos.putString("TIPO", itemsEvento.get(idEvento).getTipo());
							bundleDatos.putString("LUGAR", itemsEvento.get(idEvento).getLugar());
							bundleDatos.putInt("PRECIO", itemsEvento.get(idEvento).getPrecio());
							bundleDatos.putInt("PERSONAS", itemsEvento.get(idEvento).getNumPersonasAsistir());
							bundleDatos.putInt("AFORO", itemsEvento.get(idEvento).getAforo());
							bundleDatos.putString("INFO", itemsEvento.get(idEvento).getInfoEvento());
							
							intentEvento.putExtras(bundleDatos);
					
							startActivity(intentEvento);
										
							}
						});
        
        listaEventos.setAdapter(adaptador);
    }

    private ArrayList<Evento> obtenerEventos() {
	    
    	ArrayList<Evento> Opciones = new ArrayList<Evento>();
	     
	    Evento evento1 = new Evento("Concierto ACDC",1 ,R.drawable.acdc_evento ,"Concierto", "Valladolid", new Fecha(8,12,2012,22,00), 40, 30, 200, "Empresa1 Conciertos", "Concierto de ACDC en Madrid a las 22:00, ¡ No te lo pierdas!", 6);
		Evento evento2 = new Evento("Concierto Jessie J",2 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Fecha(20,7,2013,20,30), 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Evento evento3 = new Evento("Cumpleaños Hugo",3 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Fecha(15,2,2013,19,45), 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
		Evento evento4 = new Evento("Cine Forum",4 ,R.drawable.jessie_j_evento ,"Cine", "Madrid", new Fecha(24,12,2012,21,00), 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
	    
		Opciones.add(evento1);
	    Opciones.add(evento2);
	    Opciones.add(evento3);
	    Opciones.add(evento4);

	    
	     return Opciones;
	    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_proximos_eventos, menu);
        return true;
    }
}
