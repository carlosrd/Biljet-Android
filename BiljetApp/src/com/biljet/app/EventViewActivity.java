package com.biljet.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.types.Event;

public class EventViewActivity extends ActivitiesHeader {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_view);
		
//		Bundle dataBundle = getIntent().getExtras();
//		createHeaderView(R.drawable.header_back_button,"Evento: "+dataBundle.getString("NAME"), R.drawable.perfil,false);
//		setBackButton();

		Event e = getIntent().getParcelableExtra("event");

		createHeaderView(R.drawable.header_back_button,"Evento: "+e.getName()/*dataBundle.getString("NAME")*/, R.drawable.perfil,false);
		setBackButton();
		
		

//		ImageView eventImage = (ImageView)findViewById(R.id.eventView_Image);
//		eventImage.setImageResource(dataBundle.getInt("IMAGE-URL"));
//		eventImage.setScaleType(ImageView.ScaleType.CENTER);
//	
//		Button buttonReadQR = (Button)findViewById(R.id.eventView_Button_ReadQR);
//		if (dataBundle.getBoolean("OWN?")){
//			buttonReadQR.setVisibility(View.VISIBLE);
//	        buttonReadQR.setOnClickListener(new OnClickListener() {
//		        							   public void onClick(View arg0) {
//		        								   // Intent para lanzar el lector de QR
//		        								   Intent intent = new Intent("com.biljet.app.SCAN");
//		        								   intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//		        								   startActivityForResult(intent, 0);
//		        							   }
//	        							   });
//			}
//		else
//			buttonReadQR.setVisibility(View.INVISIBLE);
//		
//		TextView txtName = (TextView)findViewById(R.id.eventView_TxtName);
//		txtName.setText(dataBundle.getString("NAME"));
//	
//		TextView txtEventType = (TextView)findViewById(R.id.eventView_TxtEventType);
//		txtEventType.setText(dataBundle.getString("EVENT_TYPE"));
//		
//		TextView txtSite = (TextView)findViewById(R.id.eventView_TxtSite);
//		txtSite.setText(dataBundle.getString("SITE"));
//	
//		TextView txtPrice = (TextView)findViewById(R.id.eventView_TxtPrice);
//		int auxInt = dataBundle.getInt("PRICE");
//		String auxString = auxInt+"";
//		txtPrice.setText(auxString);
//		
//		TextView txtConfirmed = (TextView)findViewById(R.id.eventView_TxtConfirmedPeople);
//		auxInt = dataBundle.getInt("CONFIRMED_PEOPLE");
//		auxString = auxInt+"";
//		txtConfirmed.setText(auxString);
//
//		
//		TextView txtCapacity = (TextView)findViewById(R.id.eventView_TxtCapacity);
//		auxInt = dataBundle.getInt("CAPACITY");
//		auxString = auxInt+"";
//		txtCapacity.setText(auxString);
//
//		
//		TextView txtInfo = (TextView)findViewById(R.id.eventView_TxtInfo);
//		txtInfo.setText(dataBundle.getString("INFO"));
		
		ImageView eventImage = (ImageView)findViewById(R.id.eventView_Image);
		eventImage.setImageResource(e.getImage());
		eventImage.setScaleType(ImageView.ScaleType.CENTER);
	
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
	
		TextView txtDate = (TextView)findViewById(R.id.eventView_TxtDate);
		String auxDate= e.getDate().toString();
		auxDate = auxDate+"";
		txtDate.setText(auxDate);
		
		TextView txtLength = (TextView)findViewById(R.id.eventView_TxtLength);
		String auxString = "";
		int auxInt = e.getLength_days();
		if (auxInt > 0)
			auxString = auxInt+" días ";
		auxInt = e.getLength_hours();
		if (auxInt > 0)
			auxString += auxInt+" h ";
		auxInt = e.getLength_minutes();
		if (auxInt > 0)
			auxString += auxInt+" min. ";
		if (auxString.equals(""))
			auxString = "Indeterminado";
		txtLength.setText(auxString);
		
		TextView txtPrice = (TextView)findViewById(R.id.eventView_TxtPrice);
		float auxFloat = e.getPrice();
		auxString = auxFloat+" €";
		txtPrice.setText(auxString);
		
		TextView txtGuests = (TextView)findViewById(R.id.eventView_TxtConfirmedPeople);
		auxInt = e.getConfirmedPeople();
		auxString = auxInt+"";
		txtGuests.setText(auxString);

		TextView txtCapacity = (TextView)findViewById(R.id.eventView_TxtCapacity);
		auxInt = e.getCapacity();
		auxString = auxInt+"";
		txtCapacity.setText(auxString);

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
	             Toast toast = Toast.makeText(this, "Contenido:" + contents + " Formato:" + format , Toast.LENGTH_LONG);
	             toast.setGravity(Gravity.TOP, 25, 500);
	             toast.show();
		      } else if (resultCode == RESULT_CANCELED) {
		         // Handle cancel
	             Toast toast = Toast.makeText(this, "! : Escaneo cancelado", Toast.LENGTH_LONG);
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

}
