package com.biljet.app;

import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.biljet.adapters.ActivitiesHeader;

public class DayViewActivity extends ActivitiesHeader {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_day_view);
		
        // ACTION BAR
     	// **************************************************************************************
        
		createHeaderView(R.drawable.header_back_button,"Mi Calendario",R.drawable.mas,true);
		setBackButton();
		setRightButtonAction(NewEventActivity.class);
		
		TextView title = (TextView) findViewById(R.id.dayView_LabelDate);
	    
	    title.setText(getIntent().getExtras().getString("date"));
	    
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.day_view, menu);
		return true;
	}

}
