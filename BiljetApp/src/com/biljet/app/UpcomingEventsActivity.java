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
        //cabecera(true, drawable.home, MenuActivity.class, "Proximos Events", true, android.R.drawable.ic_menu_search, ProximosEventsActivity.class);
        createHeaderView(R.drawable.header_back_button,"Proximos Eventos", android.R.drawable.ic_menu_search,true);
		setBackButton();
		setRightButtonAction(UpcomingEventsActivity.class);
		
		
		// LIST VIEW
		// **************************************************************************************

		UpcomingEventsAdapter adapter = new UpcomingEventsAdapter(this,itemsEvent);
        ListView eventList = (ListView)findViewById(R.id.list_Events);
        
		// Setear oyentes OnClick
        
        eventList.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int idEvent, long id) {
						//Acciones necesarias al hacer click
							
							Intent intentEvent = new Intent(UpcomingEventsActivity.this, EventViewActivity.class);
							
							Bundle dataBundle = new Bundle();
							
							//En realidad creo que se le puede pasar el tipo Event usand (ya lo probaremos)
							dataBundle.putInt("IMAGE-URL", itemsEvent.get(idEvent).getImage());
							dataBundle.putString("NAME", itemsEvent.get(idEvent).getName());
							dataBundle.putString("EVENT_TYPE", itemsEvent.get(idEvent).getEventType());
							dataBundle.putString("SITE", itemsEvent.get(idEvent).getSite());
							dataBundle.putInt("PRICE", itemsEvent.get(idEvent).getPrice());
							dataBundle.putInt("CONFIRMED_PEOPLE", itemsEvent.get(idEvent).getConfirmedPeople());
							dataBundle.putInt("CAPACITY", itemsEvent.get(idEvent).getCapacity());
							dataBundle.putString("INFO", itemsEvent.get(idEvent).getEventInfo());
							
							intentEvent.putExtras(dataBundle);
					
							startActivity(intentEvent);
										
							}
						});
        
        eventList.setAdapter(adapter);
    }

    private ArrayList<Event> getEvents() {
	    
    	ArrayList<Event> sampleEvents = new ArrayList<Event>();
	     
	    Event Event1 = new Event("Concierto ACDC",1 ,R.drawable.acdc_evento ,"Concierto", "Valladolid", new Date(8,12,2012,22,00), 40, 30, 200, "Empresa1 Conciertos", "Concierto de ACDC en Madrid a las 22:00, ¡ No te lo pierdas!", 6);
		Event Event2 = new Event("Concierto Jessie J",2 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30), 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event3 = new Event("Cumpleaños Hugo",3 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45), 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
		Event Event4 = new Event("Cine Forum",4 ,R.drawable.jessie_j_evento ,"Cine", "Madrid", new Date(24,12,2012,21,00), 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
	    
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
