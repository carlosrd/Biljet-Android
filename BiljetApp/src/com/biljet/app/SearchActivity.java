package com.biljet.app;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.biljet.types.Event;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class SearchActivity extends Activity {
	
	// ATRIBUTOS
 	// **************************************************************************************

	ArrayList<Event> eventsArray = new ArrayList<Event>();
//	ArrayList<Friend> friendsArray = new ArrayList<Friend>();
	
	EditText filterText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

        // ACTION BAR
     	// **************************************************************************************

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Buscar");
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// leemos el boton de busqueda
		Button searchButton = (Button)findViewById(R.id.search_ImageButton_Search);
		searchButton.setOnClickListener(new OnClickListener() {
					   public void onClick(View arg0) {
	
					   }
				   });
		
		

	} // OnCreate()



}// SearchActivity
