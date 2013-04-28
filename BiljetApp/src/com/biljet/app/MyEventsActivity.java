package com.biljet.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        
        createHeaderView(R.drawable.header_back_button,"Mis Eventos", R.drawable.mas,true);
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
					
							Event e = sampleEventsToGo.get(eventId);
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
    
    	Event Event1 = new Event("Cine Forum",1 ,R.drawable.cine_forum_evento ,"Cine", "Madrid", new Date(24,12,2012,21,30),0 ,4,10, 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
		Event Event2 = new Event("Jessie J en concierto",2 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,2,45, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event3 = new Event("Carrera Atlética",3 ,R.drawable.maraton_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
		
		sampleItems.add(Event1);
	    sampleItems.add(Event2);
	    sampleItems.add(Event3);

	    return sampleItems;
	}
    
    private ArrayList<Event> getEventsOrganized() {
	    
    	ArrayList<Event> sampleItems = new ArrayList<Event>();
	    
    	Event Event1 = new Event("Carrera Atlética",3 ,R.drawable.maraton_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
		Event Event2 = new Event("Cine Forum",1 ,R.drawable.cine_forum_evento ,"Cine", "Madrid", new Date(24,12,2012,21,20),0 ,4,10, 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
	    
		sampleItems.add(Event1);
		sampleItems.add(Event2);
		
	    return sampleItems;
    }
		

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_events, menu);
        return true;
    }
    
	/**
	 * Actions related to the menu options displayed when you press ··· or Config button on the device
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(MyEventsActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	    }
	    return true;
	}

}

