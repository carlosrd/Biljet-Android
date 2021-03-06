package com.biljet.app;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.biljet.types.EncryptedData;
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
		actionBar.setTitle("Inicio");
		actionBar.addAction(new IntentAction(this, createShareIntent(), R.drawable.actionbar_share_action));
		actionBar.addAction(new IntentAction(this, new Intent(this, HelpActivity.class), R.drawable.actionbar_help_action));
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
		
		builder.setMessage("�Seguro que deseas salir?");
		builder.setTitle("Biljet");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setCancelable(false);
		builder.setPositiveButton("S�",
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
	
	private String prepareUsername(){
		
		String[] params = null;
	
		try {
			params = new EncryptedData(IndexActivity.this).decrypt();
			return params[0];
		} catch (InvalidKeyException e) {
			Log.e("Error","Clave de cifrado no valida");
		} catch (NoSuchAlgorithmException e) {
			Log.e("Error","El algoritmo no existe");
		} catch (NoSuchPaddingException e) {
			Log.e("Error","No hay padding");
		} catch (IOException e) {
			Log.e("Error","Entrada y salida");
		}
		
		return "";
	
	}
	
    public static Intent createIntent(Context context) {
        Intent i = new Intent(context, IndexActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return i;
    }

    private Intent createShareIntent() {
    		
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, prepareUsername() + " te invita a unirte Biljet! Descargate nuestra APK desde el siguiente enlace: http://db.tt/k0odWEqh \nM�s info: http://www.biljetapp.com");
        return Intent.createChooser(intent, "Comparte BiljetApp por...");
    }
    
	// TECLA SUBMENU / BOTON ���
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
