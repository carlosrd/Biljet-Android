package com.biljet.app;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.biljet.adapters.EventListAdapter;
import com.biljet.types.Event;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;

public class DayViewActivity extends Activity{

	// ATRIBUTOS
 	// **************************************************************************************
	
	private ArrayList<Event> eventsCreated;
	private ArrayList<Event> eventsToGo;

	private boolean activeHostA = false; // Puede tener eventos ToGo o Created
	private boolean activeHostB = false; // Solo Created
	
	// Fecha actual a mostrar en milisegundos
	private long choosedDate; 			
	
	// CONSTRUCTORA
 	// **************************************************************************************
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_view);
		
		// LEER BUNDLE
     	// **************************************************************************************
        	
		String date = getIntent().getStringExtra("DATE_STRING");
		eventsToGo = getIntent().getParcelableArrayListExtra("TO_GO");
		eventsCreated = getIntent().getParcelableArrayListExtra("CREATED");
		choosedDate = getIntent().getLongExtra("DATE_MILLIS", 0);
				
        // ACTION BAR
     	// **************************************************************************************
        
        ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Agenda " + date);
		actionBar.setHomeAction(new IntentWithFinishAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);

		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		today.set(Calendar.AM_PM,Calendar.AM);

		Log.d("TIME","Choosed: " + choosedDate +"    Instance: "+ today.getTimeInMillis());
		
		if (choosedDate >= today.getTimeInMillis()){
			Intent newEventIntent = new Intent(this, NewEventActivity.class);
			newEventIntent.putExtra("DATE_SELECTED", choosedDate);
			actionBar.addAction(new IntentWithFinishAction(this, newEventIntent, R.drawable.actionbar_newevent_action));
		}
        // HEADER LISTA A
     	// **************************************************************************************
        
		TextView titleListA = (TextView) findViewById(R.id.dayView_LabelHeader_ListA);
		TextView titleListB = (TextView) findViewById(R.id.dayView_LabelHeader_ListB);
		
		LinearLayout hostA = (LinearLayout) findViewById(R.id.dayView_LayoutHost_ListA);
		LinearLayout hostB = (LinearLayout) findViewById(R.id.dayView_LayoutHost_ListB);
		

		if (eventsToGo.size() > 0){
			
			titleListA.setText("Asistirás a...");
			activeHostA = true;
			
			if (eventsCreated.size() > 0){
				titleListB.setText("Organizas...");
				activeHostB = true;
			}
			else
				hostB.setVisibility(View.INVISIBLE);
			
		
		} else if (eventsCreated.size() > 0){
			titleListA.setText("Organizas...");
			activeHostA = true;
			hostB.setVisibility(View.INVISIBLE);
		} else {		
			hostA.setVisibility(View.INVISIBLE);
			hostB.setVisibility(View.INVISIBLE);	
		}

	   
	    
		// LIST VIEW A
		// **************************************************************************************

		ListView eventsListA = (ListView)findViewById(R.id.dayView_ListA);
			
        EventListAdapter adapterListA = null;
        if (activeHostA){
        	if (eventsToGo.size() > 0)
        		adapterListA = new EventListAdapter(DayViewActivity.this, eventsToGo);
        	else if (eventsCreated.size() > 0)
        		adapterListA = new EventListAdapter(DayViewActivity.this, eventsCreated);
        
        
        	// Setear oyentes OnClick
        	eventsListA.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int position, long id) {
						//Acciones necesarias al hacer click
							
							Intent eventIntent = new Intent(DayViewActivity.this, EventViewActivity.class);
							
							if (eventsToGo.size() > 0)
								eventIntent.putExtra("EVENT", eventsToGo.get(position));
							else
								eventIntent.putExtra("EVENT", eventsCreated.get(position));
							
							// TODO Comprobar si el evento es propio o no
							/*if (currentDay.equals("13")){
								e = eventsOwn.get(eventId);
								eventIntent.putExtra("event",e);
								eventIntent.putExtra("OWN?", true);
							}
							else {
								e = eventsToGo.get(eventId);
								eventIntent.putExtra("event",e);
								eventIntent.putExtra("OWN?", false);
							}*/
							
							startActivity(eventIntent);
										
							}
						});
        
        	eventsListA.setAdapter(adapterListA);
        
        }
        
		// LIST VIEW B
		// **************************************************************************************

        ListView eventsListB = (ListView)findViewById(R.id.dayView_ListB);
		
        EventListAdapter adapterListB = null;
        
        if (activeHostB){
        	adapterListB = new EventListAdapter(DayViewActivity.this, eventsCreated);
        
        
        	// Setear oyentes OnClick
        	eventsListB.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int position, long id) {
						//Acciones necesarias al hacer click
							
							Intent eventIntent = new Intent(DayViewActivity.this, EventViewActivity.class);
							
							eventIntent.putExtra("EVENT", eventsCreated.get(position));
							
							// TODO Comprobar si el evento es propio o no
							/*if (currentDay.equals("13")){
								e = eventsOwn.get(eventId);
								eventIntent.putExtra("event",e);
								eventIntent.putExtra("OWN?", true);
							}
							else {
								e = eventsToGo.get(eventId);
								eventIntent.putExtra("event",e);
								eventIntent.putExtra("OWN?", false);
							}*/
							
							startActivity(eventIntent);
										
							}
						});
        
        	eventsListB.setAdapter(adapterListB);
        
        }
			
	} // onCreate()

	
	// ACCIONES ADICIONALES PARA ACTIONBAR
 	// **************************************************************************************
    
	private class IntentWithFinishAction extends AbstractAction {
        private Context mContext;
        private Intent mIntent;

        public IntentWithFinishAction(Context context, Intent intent, int drawable) {
            super(drawable);
            mContext = context;
            mIntent = intent;
        }

        @Override
        public void performAction(View view) {
            try {      
               mContext.startActivity(mIntent); 
               finish();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext,
                        mContext.getText(R.string.actionbar_activity_not_found),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
	
}
