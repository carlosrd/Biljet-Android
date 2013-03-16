package com.biljet.app;

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.adapters.CalendarAdapter;
import com.biljet.adapters.CalendarDaysAdapter;

public class CalendarViewActivity extends ActivitiesHeader {

	private Calendar month; 
	private CalendarAdapter adapter;
	//public Handler handler;
	//public ArrayList<String> items; // container to store some random calendar items
	
	/**
	 * Method which creates the main view of the activity (Activities Header + Month Navigation Panel + Calendar)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_view);
		
		month = Calendar.getInstance();
	    /*onNewIntent(getIntent());
	    items = new ArrayList<String>();
	    
	    handler = new Handler();
	    handler.post(calendarUpdater);*/
	    
        // ACTION BAR
     	// **************************************************************************************
        
		createHeaderView(R.drawable.header_back_button,"Mi Calendario", R.drawable.perfil,false);
		setBackButton();

		
        // BARRA MES + BOTONES ANT/SIG MES
     	// **************************************************************************************
        
	    Button previous  = (Button) findViewById(R.id.calendarView_ButtonPrevious);
	    previous.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Si estamos en el mes de enero => Minimo mes del calendario
				if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) 	
					// Actualizar el año al anterior y el mes al ultimo mes (diciembre)
					month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
				else // Sino, es otro mes cualquiera
					// Actualizar el mes al anterior
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
							
				refreshCalendar();
			}
		});
	    
	    TextView title = (TextView) findViewById(R.id.calendarView_LabelMonth);
	    title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	    
	    Button next  = (Button) findViewById(R.id.calendarView_ButtonNext);
	    next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Si estamos en el mes de diciembre => Maximo mes del calendario
				if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) 	
					// Actualizar el año al siguiente y el mes al primero (enero)
					month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),1);
				else // Sino, es otro mes cualquiera
					// Actualizar el mes al siguiente
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)+1);
				
				refreshCalendar();
				
			}
		});

        // GRID VIEW: CALENDARIO
		// **************************************************************************************
        
	    // Acoplar el adaptador al GridView de las etiquetas de los días
		CalendarDaysAdapter ad = new CalendarDaysAdapter(this);
		GridView labelDaysGrid = (GridView)findViewById(R.id.calendarView_gridCalendarLabelDays);
		labelDaysGrid.setClickable(false);
		labelDaysGrid.setFocusable(false);
		labelDaysGrid.setAdapter(ad);
	    
	    
	    // Acoplar el adaptador al GridView del calendario
		adapter = new CalendarAdapter(this,month);
		GridView calendarGrid = (GridView)findViewById(R.id.calendarView_gridCalendar);

		// Setear oyentes OnClick
		calendarGrid.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int position, long id) {
						//Acciones necesarias al hacer click // TODO Pasar a vista de dia
					    	TextView date = (TextView)v.findViewById(R.id.calendarItem_TxtDay);
					        if (date instanceof TextView && !date.getText().equals("")) {
					        	
					        	Intent choosedDay = new Intent(CalendarViewActivity.this, DayViewActivity.class);
					        	String day = date.getText().toString();
					        	if (day.length() == 1) {
					        		day = "0"+day;
					        	}
					        	// return chosen date as string format 
					        	choosedDay.putExtra("date", day + "-" + android.text.format.DateFormat.format("MM-yyyy", month));
					        	startActivity(choosedDay);
					        	//setResult(RESULT_OK, intent);
					        	//finish();
					        }
						}
		});
		
		calendarGrid.setAdapter(adapter);
		
	}
	
	/**
	 * Method that creates the menu displayed when you press ··· or Config button on the device
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar_view, menu);
		return true;
	}

	/**
	 * Refresh the calendar view: It updates month label and fills the calendar with the days in its correct position
	 */
	public void refreshCalendar(){
		TextView title  = (TextView) findViewById(R.id.calendarView_LabelMonth);
		
		adapter.refreshDays();
		adapter.notifyDataSetChanged();				
		//handler.post(calendarUpdater); // generate some random calendar items				
		
		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}

	/*
	public void onNewIntent(Intent intent) {
		String date = intent.getStringExtra("date");
		String[] dateArr = date.split("-"); // date format is yyyy-mm-dd
		month.set(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]), Integer.parseInt(dateArr[2]));
	}
	
	public Runnable calendarUpdater = new Runnable() {
		
		@Override
		public void run() {
			items.clear();
			// format random values. You can implement a dedicated class to provide real values
			for(int i=0; i<31; i++) {
				Random r = new Random();
				
					if(r.nextInt(10)>6)
					{
					items.add(Integer.toString(i));
					}
				}

			adapter.setItems(items);
			adapter.notifyDataSetChanged();
		}
	};

	*/
}
