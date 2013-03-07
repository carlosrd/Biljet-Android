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

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.adapters.UpcomingEventsAdapter;
import com.biljet.types.Date;
import com.biljet.types.Event;

public class MyEventsActivity extends ActivitiesHeader {

	// ATRIBUTOS
 	// **************************************************************************************
	   
	final ArrayList<Event> sampleEventsToGo = getEventsToGo();
	final ArrayList<Event> sampleEventsOrg = getEventsOrganized();
	
	final String[] opEventsSpinner = new String[] {"Eventos a los que asistirás:",
											 	   "Eventos que organizas:" };
	
	final UpcomingEventsAdapter eventToGoAdapter = new UpcomingEventsAdapter(this,sampleEventsToGo);
	final UpcomingEventsAdapter eventOrganizedAdapter = new UpcomingEventsAdapter(this,sampleEventsOrg);
	
	boolean isOwn = false;
	
	// OnCreate()
 	// **************************************************************************************
	  
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        // ACTION BAR
     	// **************************************************************************************
        
        createHeaderView(R.drawable.header_back_button,"Mis Eventos", android.R.drawable.ic_input_add,true);
		setBackButton();
		setRightButtonAction(NewEventActivity.class);
		
		// LIST VIEW
		// **************************************************************************************

		final ListView eventList = (ListView)findViewById(R.id.list_MyEvents);
        
		// Setear oyentes OnClick
        
        eventList.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int eventId, long id) {
						//Acciones necesarias al hacer click
							
							Intent eventIntent = new Intent(MyEventsActivity.this, EventViewActivity.class);
							
//							Bundle dataBundle = new Bundle();
//							
//							//En realidad creo que se le puede pasar el tipo Event usand (ya lo probaremos)
//							dataBundle.putInt("IMAGE-URL", sampleEventsToGo.get(eventId).getImage());
//							dataBundle.putString("NAME", sampleEventsToGo.get(eventId).getName());
//							dataBundle.putString("EVENT_TYPE", sampleEventsToGo.get(eventId).getEventType());
//							dataBundle.putString("SITE", sampleEventsToGo.get(eventId).getSite());
//							dataBundle.putInt("PRICE", sampleEventsToGo.get(eventId).getPrice());
//							dataBundle.putInt("CONFIRMED_PEOPLE", sampleEventsToGo.get(eventId).getConfirmedPeople());
//							dataBundle.putInt("CAPACITY", sampleEventsToGo.get(eventId).getCapacity());
//							dataBundle.putString("INFO", sampleEventsToGo.get(eventId).getEventInfo());
//							
//							dataBundle.putBoolean("OWN?", isOwn);
//							
//							eventIntent.putExtras(dataBundle);
					
							Event e= sampleEventsToGo.get(eventId);
							eventIntent.putExtra("event",e);
							eventIntent.putExtra("OWN?", isOwn);
							
							startActivity(eventIntent);
										
							}
						});
        
        eventList.setAdapter(eventToGoAdapter);
    	
        // SPINNER: TIPO Event (Asistir/Organizado)
     	// **************************************************************************************
		
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, opEventsSpinner);
		
		final Spinner eventSpinner = (Spinner)findViewById(R.id.spinner_MyEvents);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventSpinner.setAdapter(spinnerAdapter);
		
		eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
												@Override
												public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) {
													
													switch(position){
														// Events a los que asisto
														case 0: eventList.setAdapter(eventToGoAdapter);
																isOwn = false;
																break;
						
														// Events que organizo
														case 1: eventList.setAdapter(eventOrganizedAdapter);
																isOwn = true;
																break;
													}
													
												}

												@Override
												public void onNothingSelected(AdapterView<?> arg0){
													// ...
												}
												
										  });
		
	} // OnCreate()

    private ArrayList<Event> getEventsToGo() {
	    
    	ArrayList<Event> sampleItems = new ArrayList<Event>();
	     
	    Event Event1 = new Event("Concierto Jessie J",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30), 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event2 = new Event("Cumpleaños Hugo",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45), 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);

		sampleItems.add(Event1);
	    sampleItems.add(Event2);

	    return sampleItems;
	}
    
    private ArrayList<Event> getEventsOrganized() {
	    
    	ArrayList<Event> sampleItems = new ArrayList<Event>();
	     
		Event Event1 = new Event("Cine Forum",1 ,R.drawable.jessie_j_evento ,"Cine", "Madrid", new Date(24,12,2012,21,00), 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
	    
		sampleItems.add(Event1);
		
	    return sampleItems;
    }
		

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_events, menu);
        return true;
    }
}

