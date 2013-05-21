package com.biljet.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

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
		actionBar.addAction(new IntentAction(this, new Intent(this, MyProfileActivity.class), R.drawable.actionbar_myprofile_action));
		
	}	

	public void onClickDiscover(View v){
		Intent discover = new Intent(IndexActivity.this, UpcomingEventsActivity.class);
		startActivity(discover);
	}
	
	public void onClickMyEvents(View v){
		Intent myEvents = new Intent(IndexActivity.this, MyEventsActivity.class);
		startActivity(myEvents);
	}
	
	public void onClickMyCalendar(View v){
		Intent calendarView = new Intent(IndexActivity.this, CalendarViewActivity.class);
		startActivity(calendarView);
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
/*	
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
*/
}
