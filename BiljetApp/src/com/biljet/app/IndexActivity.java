package com.biljet.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.biljet.adapters.MenuOptionsAdapter;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class IndexActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		
		// ACTION BAR
		// **************************************************************************************

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeLogo(R.drawable.actionbar_logo);
		actionBar.setTitle("Menu Principal");
		actionBar.addAction(new IntentAction(this, createShareIntent(), android.R.drawable.ic_menu_share));
		actionBar.addAction(new IntentAction(this, new Intent(this, MyProfileActivity.class), R.drawable.perfil));
		
        // GRID VIEW
		// **************************************************************************************
        // Acoplar el adaptador al GridView
        
		MenuOptionsAdapter adapter = new MenuOptionsAdapter(this);
		GridView gridMenu = (GridView)findViewById(R.id.indexActivity_gridMenu);
		
		// Setear oyentes OnClick
		gridMenu.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int position, long id) {
						//Acciones necesarias al hacer click
							switch(position){
								case 0: Intent upcomingEventsOption = new Intent(IndexActivity.this, UpcomingEventsActivity.class);
										startActivity(upcomingEventsOption);
										break;
								case 1: Intent myEventsOption = new Intent(IndexActivity.this, MyEventsActivity.class);
										startActivity(myEventsOption);
										break;
								case 2: Intent calendarViewOption = new Intent(IndexActivity.this, CalendarViewActivity.class);
										startActivity(calendarViewOption);
										break;
								case 3: Intent settingsOption = new Intent(IndexActivity.this, SettingsActivity.class);
										startActivity(settingsOption);
										break;
										
								default: break;

							}
						}
		});
		
		gridMenu.setAdapter(adapter);
	}	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				showExitConfirmDialog();
				break;
			case KeyEvent.KEYCODE_MENU:
				this.openOptionsMenu();
				break;
		}

		return true;
	}
	
	private void showExitConfirmDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage("¿Seguro que deseas salir?");
		builder.setTitle("Biljet");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setCancelable(false);
		builder.setPositiveButton("Sí",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
						System.exit(RESULT_OK);
					}
				});
		builder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, IndexActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    private Intent createShareIntent() {
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Comparte BiljetApp con tus amigos! Para descargar seguir el siguiente enlace: https://dl.dropbox.com/u/16354811/Android/BiljetApp.apk");
        return Intent.createChooser(intent, "Share");
    }
    
	// TECLA SUBMENU / BOTON ···
	// **************************************************************************************
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.index, menu);
	
		return true;
	}
	
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(IndexActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	        case R.id.indexSubmenu_ButtonAbout:
	        	Toast.makeText(this,"BiljetApp V.0 ALPHA-2 (IS 2012/2013)",Toast.LENGTH_LONG).show(); 
	        	break;
		    case R.id.indexSubmenu_ButtonExit: 
				showExitConfirmDialog();
		    	break;
	    }
	    return true;
	}

}
