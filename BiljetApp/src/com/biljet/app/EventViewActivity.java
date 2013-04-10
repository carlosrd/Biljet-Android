package com.biljet.app;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biljet.types.Event;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class EventViewActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_view);
		
//		Bundle dataBundle = getIntent().getExtras();
//		createHeaderView(R.drawable.header_back_button,"Evento: "+dataBundle.getString("NAME"), R.drawable.perfil,false);
//		setBackButton();

		
        // ACTION BAR
     	// **************************************************************************************
        
		//createHeaderView(R.drawable.header_back_button,"Evento: "+e.getName()/*dataBundle.getString("NAME")*/, R.drawable.perfil,false);
		//setBackButton();
		
		Event e = getIntent().getParcelableExtra("event");
		
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Evento: "+e.getName());
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		int auxInt = 0;
		
		ImageView eventImageView = (ImageView)findViewById(R.id.eventView_Image);
		//eventImage.setImageResource(e.getImage());		
		File imgFile = new File(e.getImagePath());
		if(imgFile.exists()){
		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    eventImageView.setImageBitmap(myBitmap);
			}
		
		eventImageView.setScaleType(ImageView.ScaleType.CENTER);
	
		
		Button buttonReadQR = (Button)findViewById(R.id.eventView_Button_ReadQR);
		if (getIntent().getBooleanExtra("OWN?", true)){
			buttonReadQR.setVisibility(View.VISIBLE);
	        buttonReadQR.setOnClickListener(new OnClickListener() {
		        							   public void onClick(View arg0) {
		        								   // Intent para lanzar el lector de QR
		        								   Intent intent = new Intent("com.biljet.app.SCAN");
		        								   intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		        								   startActivityForResult(intent, 0);
		        							   }
	        							   });
			}
		else
			buttonReadQR.setVisibility(View.INVISIBLE);
		
		TextView txtName = (TextView)findViewById(R.id.eventView_TxtName);
		txtName.setText(e.getName());
	
		TextView txtEventType = (TextView)findViewById(R.id.eventView_TxtEventType);
		txtEventType.setText(e.getEventType());
		
		TextView txtSite = (TextView)findViewById(R.id.eventView_TxtSite);
		txtSite.setText(e.getSite());
	
		TextView txtPrice = (TextView)findViewById(R.id.eventView_TxtPrice);
		String auxString2 = "";
		float auxFloat = e.getPrice();
		auxString2 = auxFloat+" �";
		txtPrice.setText(auxString2);
		
		TextView txtDate = (TextView)findViewById(R.id.eventView_TxtDate);
		String auxDate= e.getDate().toString();
		auxDate = auxDate+"";
		txtDate.setText(auxDate);
		
		TextView txtLength = (TextView)findViewById(R.id.eventView_TxtLength);
		String auxString = "";
		auxInt = e.getLength_days();
		if (auxInt > 0)
			auxString = auxInt+" d�as ";
		auxInt = e.getLength_hours();
		if (auxInt > 0)
			auxString += auxInt+" h ";
		auxInt = e.getLength_minutes();
		if (auxInt > 0)
			auxString += auxInt+" min. ";
		if (auxString.equals(""))
			auxString = "Indeterminado";
		txtLength.setText(auxString);
		
		TextView txtGuests = (TextView)findViewById(R.id.eventView_TxtConfirmedPeople);
		String auxString3 = "";
		auxInt = e.getConfirmedPeople();
		auxString3 = auxInt+"";
		txtGuests.setText(auxString3);

		TextView txtCapacity = (TextView)findViewById(R.id.eventView_TxtCapacity);
		String auxString4 = "";
		auxInt = e.getCapacity();
		auxString4 = auxInt+"";
		txtCapacity.setText(auxString4);

		TextView txtInfo = (TextView)findViewById(R.id.eventView_TxtInfo);
		txtInfo.setText(e.getEventInfo());
	}

	// Metodo para recoger los resultados de leer el QR
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		         String contents = intent.getStringExtra("SCAN_RESULT");
		         String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		         // Handle successful scan
	             Toast toast = Toast.makeText(this, "Contenido:" + contents + "       Formato:" + format , Toast.LENGTH_LONG);
	             toast.setGravity(Gravity.TOP, 25, 500);
	             toast.show();
		      } else if (resultCode == RESULT_CANCELED) {
		         // Handle cancel
	             Toast toast = Toast.makeText(this, " ! : Escaneo cancelado", Toast.LENGTH_LONG);
	             toast.setGravity(Gravity.TOP, 25, 500);
	             toast.show();
		      }
		   }
		}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_view, menu);
		return true;
	}

	/**
	 * Actions related to the menu options displayed when you press ��� or Config button on the device
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(EventViewActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	    }
	    return true;
	}

}
