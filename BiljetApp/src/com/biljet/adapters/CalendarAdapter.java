package com.biljet.adapters;

import java.util.Calendar;
import java.util.HashSet;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.app.R;
import com.biljet.app.R.drawable;

public class CalendarAdapter extends BaseAdapter {
	
	static final int FIRST_DAY_OF_WEEK = 1; // Sunday = 0, Monday = 1 (XXX AL reves)?
	
    public String[] days;		// Array para rellenar el Grid => Relleno inicial + D�as del mes
	
	private Context mContext;	// Context heredado de la anterior actividad

    private Calendar month;			// Fecha/Mes actual del calendario (timestamp)
    private Calendar today;			// Almacenar la fecha de hoy
    private Calendar selectedDate;  // Fecha seleccionada por el usuario
	
    // Conjuntos de dias con eventos (para mostrar iconito)
    private HashSet<String> eventDaysToGo;
    private HashSet<String> eventDaysCreated;
    
    
    public CalendarAdapter(Context c, Calendar monthCalendar) {
    	
    	month = monthCalendar;
    	today = (Calendar)monthCalendar.clone();
    	selectedDate = Calendar.getInstance();
    	selectedDate.setTimeInMillis(0);
    	
    	mContext = c;
        month.set(Calendar.DAY_OF_MONTH, 1);

        refreshDays(new HashSet<String>(),new HashSet<String>());
    }
    
	@Override
	public int getCount() {
		return days.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// if it's not recycled, initialize some attributes 
	    View v = convertView;
        if (convertView == null) {  
        	LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.griditem_calendar, null);
        }
	    
        // Obtenemos de la celda el visor de texto de los d�as
        TextView dayView = (TextView)v.findViewById(R.id.calendarItem_TxtDay);
	        
        // Si el d�a esta vac�o (relleno), lo deshabilitamos
        if (days[position].equals("")) {
        	dayView.setClickable(false);
        	dayView.setFocusable(false);
        	v.setBackgroundResource(drawable.calendar_white_bg);
        } // Si el d�a contiene algo (es un d�a del mes)
        else {
        	// Si es el d�a actual, lo resaltamos
        	if (month.get(Calendar.YEAR) == today.get(Calendar.YEAR) && 
        		month.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
        		days[position].equals("" + today.get(Calendar.DAY_OF_MONTH))) {
        		v.setBackgroundResource(drawable.calendar_today_bg);
        		dayView.setTextColor(Color.parseColor("#0B173B"));
        		//v.setBackgroundResource(R.drawable.item_background_focused);
        	}
        	else { 
            	// Si es el d�a actual, lo resaltamos
            	if (month.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) && 
            		month.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
            		days[position].equals("" + selectedDate.get(Calendar.DAY_OF_MONTH))) {
            		v.setBackgroundResource(drawable.calendar_selected_date_bg);
            		dayView.setTextColor(Color.WHITE);
            		//v.setBackgroundResource(R.drawable.item_background_focused);
            	}
            	else {// Sino, aplicamos el estilo normal
        		v.setBackgroundResource(drawable.calendar_day_bg);
        		dayView.setTextColor(Color.WHITE);
        		//v.setBackgroundResource(R.drawable.list_item_background);
            	}
        	}
        }
        
        // Seteamos el texto del d�a con el correspondiente del array de d�as
        dayView.setText(days[position]);
        
        // Setear Iconos si hay eventos
        ImageView iconEvent = (ImageView)v.findViewById(R.id.calendarItem_IconDay);
        
        if (eventDaysToGo.contains(days[position])){
        	
        	if (eventDaysCreated.contains(days[position]))
        		iconEvent.setBackgroundResource(drawable.calendar_event_created);
    		else
    			iconEvent.setBackgroundResource(drawable.calendar_event_to_go);
    		
    		iconEvent.setVisibility(View.VISIBLE); 
    		
        	} 
        else if (eventDaysCreated.contains(days[position])){
        		   iconEvent.setVisibility(View.VISIBLE);
        		   iconEvent.setBackgroundResource(drawable.calendar_event_created);
        	} 
        else
        	iconEvent.setVisibility(View.INVISIBLE);
        
        return v;
	}

	public void setSelectedDate(Calendar selectedDate) {
		this.selectedDate = selectedDate;
	}
	
	public void setMonth(Calendar selectedDate) {
		this.month = selectedDate;
	}

	public void refreshDays(HashSet<String> eventDaysToGo, HashSet<String> eventDaysCreated){
    	
    	this.eventDaysToGo = eventDaysToGo;
    	this.eventDaysCreated = eventDaysCreated;
    	
    	// Obtener cual es el << ultimo d�a del mes actual >> = Max d�a del mes actual => 28,30,31
        // --------------------------------------------------------------------
    	int lastDay = month.getActualMaximum(Calendar.DAY_OF_MONTH);
    	
    	// Obtener cual es el << primer d�a del mes actual >> => Lunes(1),Martes(2)...
        // --------------------------------------------------------------------
        int firstDay = (int)month.get(Calendar.DAY_OF_WEEK);
        
        // Setear el tama�o de arrays de d�as (d�as del mes + relleno inicial)
        // --------------------------------------------------------------------
        	// Si el d�a 1 es lunes
        if(firstDay == 1){ 
        	// Necesitamos tantos d�as como tenga el mes para representarlo
        	days = new String[lastDay+(FIRST_DAY_OF_WEEK*6)];	// 
        	}
        else { // Sino, si el d�a 1 es cualquier d�a excepto lunes
        	// Necesitamos a�adir d�as en blanco del anterior mes hasta alcanzar el d�a 1
        	days = new String[lastDay+firstDay-(FIRST_DAY_OF_WEEK+1)];
        }
        
        int j = FIRST_DAY_OF_WEEK;		// j = 0
        
        // Rellenar el array de d�as con los d�as en blanco hasta el d�a 1
        // --------------------------------------------------------------------
        	// Si el d�a 1 es cualquier d�a excepto lunes
        if (firstDay > 1) {
        	// Llenamos hasta el d�a de la semana
	        for(j = 0; j < firstDay-FIRST_DAY_OF_WEEK; j++) 
	        	days[j] = "";
        	}	
	    else { // Sino, si el d�a 1 es lunes
	    	for(j = 0; j < FIRST_DAY_OF_WEEK*6; j++) 
	        	days[j] = "";
	        
	    	j = FIRST_DAY_OF_WEEK*6+1; // sunday => 1, monday => 7
	    }
        
        // Seguir rellenando desde el d�a 1 despu�s del relleno inicial
        // --------------------------------------------------------------------
        int dayNumber = 1;
        for(int i= j-1; i < days.length; i++) {
        	days[i] = ""+dayNumber;
        	dayNumber++;
        }
    }

}
