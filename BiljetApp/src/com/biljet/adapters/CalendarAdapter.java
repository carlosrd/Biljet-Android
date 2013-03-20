package com.biljet.adapters;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.app.R;

public class CalendarAdapter extends BaseAdapter {
	
	static final int FIRST_DAY_OF_WEEK = 1; // Sunday = 0, Monday = 1 (XXX AL reves)?
	
    public String[] days;		// Array para rellenar el Grid => Relleno inicial + D�as del mes
	
	private Context mContext;	// Context heredado de la anterior actividad

    private Calendar month;			// Mes actual del calendario
    private Calendar selectedDate;	//
	
    public CalendarAdapter(Context c, Calendar monthCalendar) {
    	month = monthCalendar;
    	selectedDate = (Calendar)monthCalendar.clone();
    	mContext = c;
        month.set(Calendar.DAY_OF_MONTH, 1);
        //this.items = new ArrayList<String>();
        refreshDays();
    }
    
	@Override
	public int getCount() {
		return days.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
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
        	v.setBackgroundColor(Color.parseColor("#F0F8FF"));
        } // Si el d�a contiene algo (es un d�a del mes)
        else {
        	// Si es el d�a actual, lo resaltamos
        	if (month.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) && 
        		month.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
        		days[position].equals(""+selectedDate.get(Calendar.DAY_OF_MONTH))) {
        		v.setBackgroundColor(Color.parseColor("#00BFFF"));
        		dayView.setTextColor(Color.parseColor("#0B173B"));
        		//v.setBackgroundResource(R.drawable.item_background_focused);
        	}
        	else { // Sino, aplicamos el estilo normal
        		v.setBackgroundColor(Color.parseColor("#084B8A"));
        		dayView.setTextColor(Color.WHITE);
        		//v.setBackgroundResource(R.drawable.list_item_background);
        	}
        }
        
        // Seteamos el texto del d�a con el correspondiente del array de d�as
        dayView.setText(days[position]);
        
        // Eventos en dias 13 y 23 (PRUEBA LOCAL)
        ImageView iconEvent = (ImageView)v.findViewById(R.id.calendarItem_IconDay);
        if (days[position].equals("13") || days[position].equals("23")) 
    		iconEvent.setVisibility(View.VISIBLE); 	
        else
        	iconEvent.setVisibility(View.INVISIBLE); 	
        
        /*// create date string for comparison
        String date = days[position];
    	
        if (date.length()==1) {
    		date = "0"+date;
    	}
        
    	String monthStr = ""+(month.get(Calendar.MONTH)+1);
    	
    	if(monthStr.length()==1) {
    		monthStr = "0"+monthStr;
    	}
       
        // show icon if date is not empty and it exists in the items array
        ImageView iw = (ImageView)v.findViewById(R.id.date_icon);
        if(date.length()>0 && items!=null && items.contains(date)) {        	
        	iw.setVisibility(View.VISIBLE);
        }
        else {
        	iw.setVisibility(View.INVISIBLE);
        }*/
        
        return v;
	}

	
    public void refreshDays()
    {
    	// clear items
    	//items.clear();
    	
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

    // references to our items

}
