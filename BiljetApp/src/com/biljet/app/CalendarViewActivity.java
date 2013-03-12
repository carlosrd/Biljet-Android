package com.biljet.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.adapters.MenuOptionsAdapter;

public class CalendarViewActivity extends ActivitiesHeader {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_view);
		
        // ACTION BAR
     	// **************************************************************************************
        
		createHeaderView(R.drawable.header_back_button,"Mi Calendario", R.drawable.perfil,false);
		setBackButton();
		
        // GRID VIEW
		// **************************************************************************************
        // Acoplar el adaptador al GridView
        
		MenuOptionsAdapter adaptador = new MenuOptionsAdapter(this);
		GridView menuGrid = (GridView)findViewById(R.id.calendarView_gridCalendar);
		
		// Setear oyentes OnClick
		menuGrid.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int position, long id) {
						//Acciones necesarias al hacer click
							switch(position){
								case 0: 
										break;
							}
						}
		});
		
		menuGrid.setAdapter(adaptador);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.calendar_view, menu);
		return true;
	}

}
