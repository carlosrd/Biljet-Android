package com.biljet.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.biljet.adapters.UpcomingEventsAdapter;
import com.biljet.types.Date;
import com.biljet.types.EncryptedData;
import com.biljet.types.Event;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class MyEventsActivity extends Activity {

	// ATRIBUTOS
 	// **************************************************************************************
	ActionBar actionBar;
	ListView eventList;
	
	DBConnection connector;
	boolean connectionAlive;  
	
	ArrayList<Event> eventsToGo; //= getEventsToGo();
	ArrayList<Event> eventsOrganized; //= getEventsOrganized();
	UpcomingEventsAdapter eventsToGoAdapter;
	UpcomingEventsAdapter eventsOrganizedAdapter;
	
	final String[] opEventsSpinner = new String[] {"Eventos a los que asistirás:",
											 	   "Eventos que organizas:" };
	
	boolean isOwn = false;
	
	// OnCreate()
 	// **************************************************************************************
	  
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        // ACTION BAR
     	// **************************************************************************************       
        
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Mis Eventos");
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.addAction(new IntentAction(this, new Intent(this, NewEventActivity.class), R.drawable.mas));
		
		// CONEXION CON DB EN SEGUNDO PLANO
	   	// **************************************************************************************
		Log.d("tag","\nComienzo seccion conexion DB");
		eventsToGo = new ArrayList<Event>();
		eventsOrganized = new ArrayList<Event>();
		
		connector = new DBConnection();
		connectionAlive = true;
		connector.execute();

		// LIST VIEW
		// **************************************************************************************

		eventList = (ListView)findViewById(R.id.list_MyEvents);
		
		eventsToGoAdapter = new UpcomingEventsAdapter(this,eventsToGo);
		eventsOrganizedAdapter = new UpcomingEventsAdapter(this, eventsOrganized);
		
		// Setear oyentes OnClick
        
        eventList.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int eventId, long id) {
						//Acciones necesarias al hacer click
							
							Intent eventIntent = new Intent(MyEventsActivity.this, EventViewActivity.class);
					
							Event e = eventsToGo.get(eventId);
							eventIntent.putExtra("event",e);
							eventIntent.putExtra("OWN?", isOwn);
							
							startActivity(eventIntent);
										
							}
						});
        
        eventList.setAdapter(eventsToGoAdapter);
    	
        // SPINNER: TIPO Event (Asistir/Organizado)
     	// **************************************************************************************
		
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, opEventsSpinner);
		
		final Spinner eventSpinner = (Spinner)findViewById(R.id.spinner_MyEvents);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		eventSpinner.setAdapter(spinnerAdapter);
		
		eventSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
												@Override
												public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) {
													
													switch(position){
														// Events a los que asisto
														case 0: eventList.setAdapter(eventsToGoAdapter);
																isOwn = false;
																break;
						
														// Events que organizo
														case 1: eventList.setAdapter(eventsOrganizedAdapter);
																isOwn = true;
																break;
													}
													
												}

												@Override
												public void onNothingSelected(AdapterView<?> arg0){
													// ...
												}
												
										  });
		
	} // OnCreate()

	private String prepareUser(){
		
		String path = null;
	
		try {
			path = new EncryptedData(MyEventsActivity.this).decrypt();
			if (path != null){
				File monitorFile = new File(path);
				Scanner s = new Scanner(monitorFile);
				return s.nextLine();
				} 

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
	
	private boolean getEventsFromDB() {
	    
		// CONEXION CON DB
		// **************************************************************************************
		InputStream is = null;
		String username = prepareUser();
		
		if (username.equals(""))
			return false;
		
		try {
			Log.d("tag","\nEntra en el try1");
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet("http://www.biljetapp.com/api/user/u/"+username);
			HttpResponse response = httpclient.execute(getRequest);
			StatusLine responseStatus = response.getStatusLine();
			int statusCode = responseStatus.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				}
			}
		catch(Exception e) {
			runOnUiThread(new Runnable() {
				  public void run() {
						Toast.makeText(MyEventsActivity.this,"Error al conectar con el servidor!",Toast.LENGTH_LONG).show(); 
				  }
			});

			// En esta captura de excepción podemos ver que hay un problema con la
			// conexión e intentarlo más adelante.	
		}

		if (connector.isCancelled())
			return false;
	

		String result = "";

		try {
			//BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line="0";

			while ((line = reader.readLine()) != null) 
				sb.append(line + "\n");
			
			is.close();
			result = sb.toString();
			}
		catch(Exception e) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(MyEventsActivity.this,"Error al obtener la cadena desde el buffer!",Toast.LENGTH_LONG).show(); 
				  }
			});	
		} // catch

		if (connector.isCancelled())
			return false;
		
		String title, creator,province, category,_id, imageName, 
					  longitude, latitude, finishAt;
		long createdAt;
		int __v;
		JSONArray attendee, followers, comments;
		double price;
		ArrayList<Event> events = new ArrayList<Event>();
			
		eventsToGo.clear();
	
		try {
			Log.d("tag","\nEntra en el try3");
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("eventsToGo");
			
			for (int i = 0; i < jsonArray.length(); i++){
				jsonObject = jsonArray.getJSONObject(i);
				
				title = jsonObject.getString("title");
				createdAt = jsonObject.getLong("createdAt");
				price = jsonObject.getDouble("price");
				creator = jsonObject.getString("creator");
				province = jsonObject.getString("province"); 
				category = jsonObject.getString("category");
				_id = jsonObject.getString("_id");
				__v = jsonObject.getInt("__v");
				imageName = jsonObject.getString("imageName");
			
				// CACHING IMAGENES EVENTOS
				String imageURL = "http://www.biljetapp.com/img/" + imageName;
				String imagePath = getFilesDir().getAbsolutePath()+"/eventsImage/"+imageName;
				
				File imgFolder = new File (getFilesDir().getAbsolutePath()+"/eventsImage");
				if(!imgFolder.exists())
					imgFolder.mkdir();
				
				File imgFile = new File(imagePath);
				if(!imgFile.exists())
					saveImageFromURL(imageURL,imagePath);
				
				// SEGUIR EXTRAYENDO DATOS JSON
				
				//comments = jsonObject.getJSONArray("comments");
				//longitude = jsonObject.getString("longitude"); 
				//latitude = jsonObject.getString("latitude"); 
				//followers= jsonObject.getJSONArray("followers");
				//attendee = jsonObject.getJSONArray("attendee");
				//finishAt = jsonObject.getString("finishAt");
				
				Event event = new Event(title, _id , imagePath ,category,""+province,new Date(0,0,0,0,0),0,0,0,(int) price,0,0,"","",0);
				
				eventsToGo.add(event);

				Log.d("eventToGo","\nAñadido "+ title +" al array");
			
			} //for
			
		} catch(IOException e2) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(MyEventsActivity.this, "Error al guardar avatar para el evento!", Toast.LENGTH_SHORT).show();
				  }
			});
			Log.e("IOExc",e2.getMessage());
		} catch (JSONException e1) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(MyEventsActivity.this, "Error al traducir los datos!", Toast.LENGTH_LONG).show();
				  }
			});
			
		}
	
		if (connector.isCancelled())
			return false;

		eventsOrganized.clear();
		
		try {
			Log.d("tag","\nEntra en el try3");
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("eventsOrganized");
			
			for (int i = 0; i < jsonArray.length(); i++){
				jsonObject = jsonArray.getJSONObject(i);
				
				title = jsonObject.getString("title");
				createdAt = jsonObject.getLong("createdAt");
				price = jsonObject.getDouble("price");
				creator = jsonObject.getString("creator");
				province = jsonObject.getString("province"); 
				category = jsonObject.getString("category");
				_id = jsonObject.getString("_id");
				__v = jsonObject.getInt("__v");
				imageName = jsonObject.getString("imageName");
			
				// CACHING IMAGENES EVENTOS
				String imageURL = "http://www.biljetapp.com/img/" + imageName;
				String imagePath = getFilesDir().getAbsolutePath()+"/eventsImage/"+imageName;
				
				File imgFolder = new File (getFilesDir().getAbsolutePath()+"/eventsImage");
				if(!imgFolder.exists())
					imgFolder.mkdir();
				
				File imgFile = new File(imagePath);
				if(!imgFile.exists())
					saveImageFromURL(imageURL,imagePath);
				
				// SEGUIR EXTRAYENDO DATOS JSON
				
				//comments = jsonObject.getJSONArray("comments");
				//longitude = jsonObject.getString("longitude"); 
				//latitude = jsonObject.getString("latitude"); 
				//followers= jsonObject.getJSONArray("followers");
				//attendee = jsonObject.getJSONArray("attendee");
				//finishAt = jsonObject.getString("finishAt");
				
				Event event = new Event(title, _id , imagePath ,category,""+province,new Date(0,0,0,0,0),0,0,0,(int) price,0,0,"","",0);
				
				eventsOrganized.add(event);

				Log.d("eventOrganized","\nAñadido "+ title +" al array");
			
			} //for
			
		} catch(IOException e2) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(MyEventsActivity.this, "Error al guardar avatar para el evento!", Toast.LENGTH_SHORT).show();
				  }
			});
			Log.e("IOExc",e2.getMessage());
		} catch (JSONException e1) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(MyEventsActivity.this, "Error al traducir los datos!", Toast.LENGTH_LONG).show();
				  }
			});
			
		}
		
		if (connector.isCancelled())
			return false;
		
		return true;
	}	
	
    private void saveImageFromURL(String imageURL,String destinationPath) throws IOException{
    	
		URL url = new URL(imageURL);
		File imgFile = new File(destinationPath);
		imgFile.createNewFile();
		InputStream input = null;
		FileOutputStream output = null;
		Log.d("tag3","Inicio try saveImage");
		try {
			
		    input = url.openConnection().getInputStream();
			Log.d("tag3","Conexion hecha!");
		    output = new FileOutputStream(imgFile);

			Log.d("tag3","Abierto output!");
			
		    int read;
		    byte[] data = new byte[1024];
		    Log.d("tag3","Inicio bucle saveImage");
		    while ((read = input.read(data)) != -1)
		        output.write(data, 0, read);
		    
			Log.d("tag3","Termina bucle saveImage");
			
		} finally {
		    if (output != null)
		        output.close();
		    if (input != null)
		        input.close();
		}
    	
    }	
	
    /*  Se instancia con 3 tipos:
	1º - Tipo de datos de ENTRADA para doInBackground() => Datos de entrada de la tarea en segundo plano 
	2º - Tipo de datos de ENTRADA para onProgressUpdate() y de ENTRADA para publishProgress() => Datos para mostrar el progreso
	3º - Tipo de datos de SALIDA de doInBackground() y de ENTRADA en onPostExecute() => Datos para mostrar el fin de la tarea	
	*/
	private class DBConnection extends AsyncTask<Void,Integer,Boolean> {
	
		@Override
		protected void onPreExecute() {
			actionBar.setProgressBarVisibility(View.VISIBLE);
			Toast.makeText(getBaseContext(), "Cargando eventos...", Toast.LENGTH_SHORT).show();
		}
		
		@Override
		protected Boolean doInBackground(Void... params) {
			return getEventsFromDB();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
		
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			actionBar.setProgressBarVisibility(View.INVISIBLE);
			connectionAlive = false;
			if (result){
				eventsToGoAdapter.notifyDataSetChanged();
				eventsOrganizedAdapter.notifyDataSetChanged();
			}
		}
		
		@Override
		protected void onCancelled() {
			actionBar.setProgressBarVisibility(View.INVISIBLE);
			connectionAlive = false;
			Toast.makeText(getBaseContext(), "Conexión cancelada por el usuario!", Toast.LENGTH_LONG).show();
		}
	
	
	}
    
    
	/*
    private ArrayList<Event> getEventsToGo() {
	    
    	ArrayList<Event> sampleItems = new ArrayList<Event>();

    	Event Event1 = new Event("Cine Forum","1" ,getFilesDir().getAbsolutePath()+"/eventsImage/eventDefault.png","Cine", "Madrid", new Date(24,12,2012,21,30),0 ,4,10, 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
		Event Event2 = new Event("Jessie J en concierto","2" ,getFilesDir().getAbsolutePath()+"/eventsImage/eventDefault.png","Concierto", "Madrid", new Date(20,7,2013,20,30),0,2,45, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event3 = new Event("Carrera Atlética","3" ,getFilesDir().getAbsolutePath()+"/eventsImage/eventDefault.png" ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
		
		sampleItems.add(Event1);
	    sampleItems.add(Event2);
	    sampleItems.add(Event3);

	    return sampleItems;
	}
    
    private ArrayList<Event> getEventsOrganized() {
	    
    	ArrayList<Event> sampleItems = new ArrayList<Event>();
	    
    	Event Event1 = new Event("Carrera Atlética",3 ,R.drawable.maraton_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
		Event Event2 = new Event("Cine Forum",1 ,R.drawable.cine_forum_evento ,"Cine", "Madrid", new Date(24,12,2012,21,20),0 ,4,10, 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
	    
		sampleItems.add(Event1);
		sampleItems.add(Event2);
		
	    return sampleItems;
    }
		

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_events, menu);
        return true;
    }
    
	
	/**
	 * Actions related to the menu options displayed when you press ··· or Config button on the device
	 
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(MyEventsActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	    }
	    return true;
	}
	 */
}

