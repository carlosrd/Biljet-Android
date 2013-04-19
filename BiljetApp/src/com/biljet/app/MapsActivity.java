package com.biljet.app;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 *This class provides methods to show address on map, as:
 *
 *Show map in 3D
 *show map type normal
 *show map type satellite
 *
 *show map type hybrid
 *show map type terrain
 *
 */

public class MapsActivity extends FragmentActivity {
	
	private GoogleMap map,mapa = null;
	
	//Latitud y longitud de las coordenadas 
	private double latitude;
	private double longitude;	
	private String nameEvent;
	
	private final String [] arrayTypeView = {"Vista normal","Vista híbrida","Vista satelite","Vista mapa"}; 
	//
	private boolean view3D=false;
	//Tipo de vista del mapa.
	private int typeView = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
	
		//Recuperamos latitud, longitud y nombre que hemos pasado desde la actividad EventViewActivity		
		Bundle bundle = getIntent().getExtras();
		latitude = bundle.getDouble("latitude");
		longitude = bundle.getDouble("longitude");
		nameEvent = bundle.getString("name");
		
		map = ((SupportMapFragment) getSupportFragmentManager()
				   .findFragmentById(R.id.map)).getMap();
		
		
		CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16);
		map.animateCamera(camUpd);		
		
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(nameEvent));
		
		

		// SPINNER: para MODOS DE VISTA DEL map(VISTA map, vista satélite, vista hibrida, vista relieve)
     	// **************************************************************************************

		ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayTypeView);	
		final Spinner spinnerTypesEvent = (Spinner)findViewById(R.id.maps_SpinnerTypeView);
		adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTypesEvent.setAdapter(adaptadorSpinner);		
		
		//Spinner para seleccionar el tipo de vista del mapa que queremos vizualizar. 
		spinnerTypesEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) {
				
				typeView = position;
				switch(position){
							
					case 0: //Vista Normal
						alternateViewMap(position); 							
							break;
														
					case 1: //Vista híbrida
							alternateViewMap(position);
							break;
							
					case 2: //Vista satélite
							alternateViewMap(position);
							break;
							
					case 3: //Vista map
							alternateViewMap(position);
							break;		
					default://Vista normal
							alternateViewMap(0);
				}				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0){
				// ...
			}
			
		});

		
		//Boton para mostrar el map en 3D
		Button buttonShow3D = (Button) findViewById(R.id.maps_ButtonShow3D);
		
		buttonShow3D.setOnClickListener(new View.OnClickListener() {            
            public void onClick(View arg0) {
            	
            	if(!view3D){
	            	LatLng latlng = new LatLng(latitude, longitude);  //Coordenadas de la dirección 
	        		CameraPosition camPos = new CameraPosition.Builder()
	        			    .target(latlng)   //Centramos el map en la dirección
	        			    .zoom(17)         //Establecemos el zoom en 17
	        			    .bearing(45)      //Establecemos la orientación con el noreste arriba
	        			    .tilt(70)         //Bajamos el punto de vista de la cámara 70 grados
	        			    .build();
	        		
	        		CameraUpdate camUpd = CameraUpdateFactory.newCameraPosition(camPos);	
	        		//mapa = map;
	        		map.animateCamera(camUpd);      
	        		view3D = true;
	        		Toast.makeText(MapsActivity.this,"Vista 3D activada",Toast.LENGTH_LONG).show();
	        				   
            	}
            	else{
            		
            		map.clear();
            		            		
            		map = ((SupportMapFragment) getSupportFragmentManager()
         				   .findFragmentById(R.id.map)).getMap();
         		
         		
	         		CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16);
	         		map.animateCamera(camUpd);		
	         		
	         		alternateViewMap(typeView);
	         		
	         		map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(nameEvent));
	        		Toast.makeText(MapsActivity.this,"Vista 3D desactivada",Toast.LENGTH_LONG).show();
	        		view3D = false;	 
	        			   
            	}
            }
        });    
		
		
		//Boton para mostrar el mapa modo street view
		Button buttonStreetView = (Button) findViewById(R.id.maps_ButtonStreetView);
		
		buttonStreetView.setOnClickListener(new View.OnClickListener() {            
            public void onClick(View arg0) {
            	
            	Intent streetViewIntent = new Intent(Intent.ACTION_VIEW, 
        			   Uri.parse("google.streetview:cbll="+latitude+","+longitude+"&cbp=13,198.24,,0,-8.19&mz=7")); 		         
        		startActivity(streetViewIntent); 
            }
        }); 
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.maps, menu);
		return true;
	}
	
	/**
	 * Method to switch various types of map views.
	 * @param vista
	 */
	private void alternateViewMap(int view){		
		switch(view){

			case 0:
				//Vista map tipo NORMAL
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				break;
			case 1:
				//Vista map tipo HIBRIDO
				map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				break;
			case 2:
				//Vista map tipo SATELITE
				map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
				break;
			case 3:
				//Vista map tipo TERRENO
				map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
				
				break;
		}			
	}		
    
} //MapsActivity



