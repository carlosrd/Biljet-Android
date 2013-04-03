package com.biljet.app;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.biljet.adapters.UpcomingEventsAdapter;
import com.biljet.types.Date;
import com.biljet.types.Event;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class DayViewActivity extends Activity{

	// ATRIBUTOS
 	// **************************************************************************************
	
	final ArrayList<Event> eventsOwn = getEventsOwned();
	final ArrayList<Event> eventsToGo = getEventsToGo();
	final UpcomingEventsAdapter adapterToGo = new UpcomingEventsAdapter(this,eventsToGo);
	final UpcomingEventsAdapter adapterOwned = new UpcomingEventsAdapter(this,eventsOwn);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_view);
		
        // ACTION BAR
     	// **************************************************************************************
        
		/*createHeaderView(R.drawable.header_back_button,"Mi Calendario",R.drawable.mas,true);
		setBackButton();
		setRightButtonAction(NewEventActivity.class);*/
		
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Mi Calendario");
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.addAction(new IntentAction(this, new Intent(this, NewEventActivity.class), R.drawable.mas));
		
        // DAY SUBTITLE BAR
     	// **************************************************************************************
        
		TextView title = (TextView) findViewById(R.id.dayView_LabelDate);
	    title.setText(getIntent().getExtras().getString("date"));
	   
	    
		// LIST VIEW
		// **************************************************************************************

		ListView eventsOnDayList = (ListView)findViewById(R.id.list_EventsOnDay);
		final String currentDay = (String) getIntent().getExtras().getString("date").subSequence(0, 2);
		
		// Setear oyentes OnClick
		if (currentDay.equals("13") || currentDay.equals("23")){
	        eventsOnDayList.setOnItemClickListener(new OnItemClickListener() {
							public void onItemClick(AdapterView<?> a, View v, int eventId, long id) {
							//Acciones necesarias al hacer click
								
								Intent eventIntent = new Intent(DayViewActivity.this, EventViewActivity.class);
						
								Event e;
								
								if (currentDay.equals("13")){
									e = eventsOwn.get(eventId);
									eventIntent.putExtra("event",e);
									eventIntent.putExtra("OWN?", true);
								}
								else {
									e = eventsToGo.get(eventId);
									eventIntent.putExtra("event",e);
									eventIntent.putExtra("OWN?", false);
								}
								
								startActivity(eventIntent);
											
								}
							});
	        
	    	if (currentDay.equals("13"))
	    		eventsOnDayList.setAdapter(adapterOwned);	
			else 
				eventsOnDayList.setAdapter(adapterToGo);
		}
			
	}

    private ArrayList<Event> getEventsToGo() {
	    
    	ArrayList<Event> sampleItems = new ArrayList<Event>();
    	
    	Event Event2 = new Event("Jessie J en concierto",2 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,2,45, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		sampleItems.add(Event2);

	    return sampleItems;
	}
	
    private ArrayList<Event> getEventsOwned() {
	    
    	ArrayList<Event> sampleItems = new ArrayList<Event>();
    
    	Event Event1 = new Event("Cine Forum",1 ,R.drawable.cine_forum_evento ,"Cine", "Madrid", new Date(24,12,2012,21,30),0 ,4,10, 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
		sampleItems.add(Event1);

	    return sampleItems;
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.day_view, menu);
		return true;
	}
	
	/**
	 * Actions related to the menu options displayed when you press ··· or Config button on the device
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(DayViewActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	    }
	    return true;
	}


}
