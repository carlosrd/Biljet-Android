package com.biljet.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.adapters.UpcomingEventsAdapter;
import com.biljet.types.Date;
import com.biljet.types.Event;

public class UpcomingEventsActivity extends ActivitiesHeader {

	final ArrayList<Event> itemsEvent = getEvents();;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
     
        // ACTION BAR
     	// **************************************************************************************
        createHeaderView(R.drawable.header_back_button,"Proximos Eventos", R.drawable.buscar,true);
		setBackButton();
		setRightButtonAction(SearchActivity.class, 'e');
		
		
		// LIST VIEW
		// **************************************************************************************

		UpcomingEventsAdapter adapter = new UpcomingEventsAdapter(this,itemsEvent);
        ListView eventList = (ListView)findViewById(R.id.list_Events);
        
		// Setear oyentes OnClick
        
        eventList.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int idEvent, long id) {
						//Acciones necesarias al hacer click
							
							Intent intentEvent = new Intent(UpcomingEventsActivity.this, EventViewActivity.class);
							
//							Bundle dataBundle = new Bundle();
//							
//							//En realidad creo que se le puede pasar el tipo Event usand (ya lo probaremos)
//							dataBundle.putInt("IMAGE-URL", itemsEvent.get(idEvent).getImage());
//							dataBundle.putString("NAME", itemsEvent.get(idEvent).getName());
//							dataBundle.putString("EVENT_TYPE", itemsEvent.get(idEvent).getEventType());
//							dataBundle.putString("SITE", itemsEvent.get(idEvent).getSite());
//							dataBundle.putInt("PRICE", itemsEvent.get(idEvent).getPrice());
//							dataBundle.putInt("CONFIRMED_PEOPLE", itemsEvent.get(idEvent).getConfirmedPeople());
//							dataBundle.putInt("CAPACITY", itemsEvent.get(idEvent).getCapacity());
//							dataBundle.putString("INFO", itemsEvent.get(idEvent).getEventInfo());
//							
//							intentEvent.putExtras(dataBundle);
							
							Event e= itemsEvent.get(idEvent);
							intentEvent.putExtra("event",e);
							intentEvent.putExtra("OWN?", false);
							
							startActivity(intentEvent);
										
							}
						});
        
        eventList.setAdapter(adapter);
    }

    private ArrayList<Event> getEvents() {
	    
    	ArrayList<Event> sampleEvents = new ArrayList<Event>();
	     
	    Event Event1 = new Event("Concierto ACDC",1 ,R.drawable.acdc_evento ,"Concierto", "Valladolid", new Date(8,12,2012,22,10),0,3,15, 40, 30, 200, "Empresa1 Conciertos", "Ya puede comprar las entradas ACDC. Los pioneros australianos del hard rock, se subir�n de nuevo a los escenarios, este a�o presentar�n su nueva gira mundial. La �ltima vez que estuvieron de gira, fue hace 8 a�os. �Las entradas para la gira europea de AC/DC, est�n aqu� disponibles! �Aproveche la oportunidad que le brindamos y oiga los �xitos legendarios de ACDC en concierto! Podr� oir el sonido inigualable de guitarra, que con el paso de los a�os no ha cambiado lo m�s m�nimo. Los AC/DC siempre han sabido atraer a las masas. Las entradas para ver a los ACDC, le asegurar�n una fant�stica actuaci�n en directo y podr� oir los �xitos �Back in Black�, �Highway to Hell� y �High Voltage� como tambi�n hits del �ltimo �lbum �The Razor�s Edge� y �Ballbreaker�. Si nunca ha presenciado en directo a AC/DC, entonces no sabe lo que se pierde. Esta gira del grupo de rock duro, ser� seguramente, la �ltima que ofrezcan, por eso tendr� que darse prisa, antes de que se agoten las entradas para los conciertos de AC/DC", 6);
		Event Event2 = new Event("Jessie J en concierto",2 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,2,45, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, �Lo has apuntado?", 5);
		Event Event3 = new Event("Carrera Atl�tica",3 ,R.drawable.maraton_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atl�tica 10 K VIVA! Surge como una actividad en la que la participaci�n de los atletas nace de los sentimientos m�s profundos como una manera de expresar libremente el bienestar que produce la actividad f�sica sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegr�a plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos est�ndares de calidad", 3);
		Event Event4 = new Event("Cine Forum",4 ,R.drawable.cine_forum_evento ,"Cine", "Madrid", new Date(24,12,2012,21,20),2,0,0, 3, 10, 5, "ONG", "organiza un cine f�rum sobre la conocida pel�cula de Luis Garc�a Berlanga �Bienvenido Mr. Marshall� en el Ensanche de Vallecas, a la salida del metro Valdecarros (Avenida del Ensanche s/n), uno de los terrenos barajados en la Comunidad de Madrid como posible ubicaci�n de Eurovegas", 7);
	    
		sampleEvents.add(Event1);
	    sampleEvents.add(Event2);
	    sampleEvents.add(Event3);
	    sampleEvents.add(Event4);

	    
	     return sampleEvents;
	    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upcoming_events, menu);
        return true;
    }

}
