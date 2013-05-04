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
import com.biljet.types.EncryptedData;
import com.biljet.types.Event;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class MyEventsActivity extends Activity {

	// ATRIBUTOS
 	// **************************************************************************************
	ActionBar actionBar;
	ListView eventList;
	
	// Tarea en segundo plano: Conectar con la BD
	DBConnection connector;							
	boolean connectionAlive;  
	
	ArrayList<Event> eventsToGo; 					// Evento a los que se asiste
	ArrayList<Event> eventsOrganized; 				// Eventos que organiza el propio usuario
	
	// Adaptadores para listas de eventos
	UpcomingEventsAdapter eventsToGoAdapter;		
	UpcomingEventsAdapter eventsOrganizedAdapter;
	
	// Opciones Spinner: Selector tipo de eventos (Asistir/Orgnizar)
	final String[] opEventsSpinner = new String[] {"Eventos a los que asistirás:",
											 	   "Eventos que organizas:" };
	
	// Indica si el evento es asistir (false) o propio (true)
	boolean isOwn = false;
	
	// Necesario en caso de fallo en la descarga del avatar del evento. Si falla, borramos el archivo
	String imagePath;
	
	// CONSTRUCTORA
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
		
		// Inicializar el listView: Lo volveremos invisible durante las conexiones para no provocar errores
		eventList = (ListView)findViewById(R.id.myEvents_List);
		
		Log.d("tag","\nComienzo seccion conexion DB");
		eventsToGo = new ArrayList<Event>();
		eventsOrganized = new ArrayList<Event>();
		
		connector = new DBConnection();
		connectionAlive = true;
		connector.execute();

		// LIST VIEW
		// **************************************************************************************
	
		eventsToGoAdapter = new UpcomingEventsAdapter(this,eventsToGo);
		eventsOrganizedAdapter = new UpcomingEventsAdapter(this, eventsOrganized);
		
		// Setear oyentes OnClick
        
        eventList.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int eventId, long id) {
						//Acciones necesarias al hacer click
							
							if (!connectionAlive){
								Intent eventIntent = new Intent(MyEventsActivity.this, EventViewActivity.class);
								
								Event e = eventsToGo.get(eventId);
								eventIntent.putExtra("EVENT", e);
								eventIntent.putExtra("OWN?", isOwn);
								eventIntent.putExtra("NO_TICKET", false);
								
								startActivity(eventIntent);
							}
							else
								 Toast.makeText(MyEventsActivity.this, " ! : Recargando eventos. Espere...", Toast.LENGTH_SHORT).show();

										
							}
						});
        
        eventList.setAdapter(eventsToGoAdapter);
    	
        // SPINNER: TIPO Event (Asistir/Organizado)
     	// **************************************************************************************
		
		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, opEventsSpinner);
		
		final Spinner eventSpinner = (Spinner)findViewById(R.id.myEvents_Spinner);
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

	@Override
	protected void onRestart() {
	    super.onRestart();  // Always call the superclass method first
	    
	    // Activity being restarted from stopped state    
	
		// CONEXION CON DB EN SEGUNDO PLANO
	   	// **************************************************************************************
		Log.d("RESTART","\nComienzo seccion conexion DB");
		eventsToGo.clear();
		eventsOrganized.clear();
		
		connector = new DBConnection();
		connectionAlive = true;
		connector.execute();

	}
	
	// METODOS PARA CONEXION DB
 	// **************************************************************************************
	  
	private String prepareUser(){
		
		String[] params = null;
	
		try {
			params = new EncryptedData(MyEventsActivity.this).decrypt();
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
		
		// TRADUCCION DE LOS BYTES DESCARGADOS A STRING (JSON)
		// **************************************************************************************

		String result = "";

		try {
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
		
		// EXTRACCION DE LOS DATOS DEL JSON OBTENIDO
		// **************************************************************************************
		
		String title, creatorId, _id, description, imageName, category, address;
		int postalCode, province, latitude, longitude, capacity;
		double price;
		long date;
		
		eventsToGo.clear();
	
		try {
			Log.d("tag","\nEntra bucle JSON: Eventos a los que asiste");
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("eventsToGo");
			
			for (int i = 0; i < jsonArray.length(); i++){
				jsonObject = jsonArray.getJSONObject(i);

				// DATOS JSON REQUERIDOS (NO lanzarán excepción; nunca seran "null")
				// ************************************************************************
				
				title = jsonObject.getString("title");
				creatorId = jsonObject.getString("creator");
				_id = jsonObject.getString("_id");
				description = jsonObject.getString("description");		
				category = jsonObject.getString("category");
				province = jsonObject.getInt("province"); 
				capacity = jsonObject.getInt("capacity"); 
				date = jsonObject.getLong("finishAt");
				price = jsonObject.getDouble("price");
				
				// CACHING IMAGENES EVENTOS
				// ************************************************************************
				
				imageName = jsonObject.getString("imageName");
				
				String imageURL = "http://www.biljetapp.com/img/" + imageName;
				imagePath = getFilesDir().getAbsolutePath()+"/eventsImage/"+imageName;
				
				File imgFolder = new File (getFilesDir().getAbsolutePath()+"/eventsImage");
				if(!imgFolder.exists())
					imgFolder.mkdir();
				
				File imgFile = new File(imagePath);
					
				try {
					// Si la imagen a descargar no esta almacenada en el telefono, la descargamos y guardamos en "data"
					if(!imgFile.exists())
						saveImageFromURL(imageURL,imagePath);
					
				} catch(IOException e2) {
						runOnUiThread(new Runnable() {
							  public void run() {
								  // Notificar error al guardar avatar
								  Toast.makeText(MyEventsActivity.this, "Error: No se pudo guardar avatar para el evento:", Toast.LENGTH_SHORT).show();
								  // Si se ha creado un archivo, borrarlo para que en la proxima conexion se vuelva a descargar
								  File fileToDelete = new File(imagePath);
								  if (fileToDelete.exists())
									  fileToDelete.delete();
							  }
						});

				} // catch
									
				// DATOS JSON NO REQUERIDOS (Pueden ser "null" => Lanzarán excepción)
				// ************************************************************************
				
				// Direccion (String => No lanza excepcion si es null)
				// ---------
				address = jsonObject.getString("address");
				if (address.equals("null"))
					address = "No especificado";
			
				
				// Codigo postal (Numero => Lanza excepcion si es null)
				// -------------
				try{
					postalCode = jsonObject.getInt("postalCode");
				} catch (JSONException e){
					postalCode = 0;
				}
				
				// Longitud
				// -------------
				try{
					latitude = jsonObject.getInt("latitude");;
				} catch (JSONException e){
					latitude = 0;
				}
				
				// Latitud
				// -------------
				try{
					longitude = jsonObject.getInt("longitude"); 
				} catch (JSONException e){
					longitude = 0;
				}

				Event event = new Event(title,
									    creatorId,
									   _id,
									   description,
									   imagePath,
									   category,
									   address,
									   "",
									   postalCode,
									   province,
									   longitude,
									   latitude,
									   date,
									   (float)price,
									   capacity,
									   0,0,0);
									
				eventsToGo.add(event);

				Log.d("eventToGo","\nAñadido "+ title +" al array");
			
			} //for
			
		} catch (JSONException e1) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(MyEventsActivity.this, "Error: No se pudieron leer los datos recibidos!", Toast.LENGTH_SHORT).show();
				  }
				});	
		}
	
		if (connector.isCancelled())
			return false;

		eventsOrganized.clear();
		
		try {
			Log.d("MyEvents","\nEntra bucle JSON: Eventos propios");
			JSONObject jsonObject = new JSONObject(result);
			JSONArray jsonArray = jsonObject.getJSONArray("eventsOrganized");
			
			for (int i = 0; i < jsonArray.length(); i++){
				jsonObject = jsonArray.getJSONObject(i);
				
				// DATOS JSON REQUERIDOS (NO lanzarán excepción; nunca seran "null")
				// ************************************************************************
				title = jsonObject.getString("title");
				creatorId = jsonObject.getString("creator");
				_id = jsonObject.getString("_id");
				description = jsonObject.getString("description");		
				category = jsonObject.getString("category");
				province = jsonObject.getInt("province"); 
				capacity = jsonObject.getInt("capacity"); 
				date = jsonObject.getLong("finishAt");
				price = jsonObject.getDouble("price");
				
				// CACHING IMAGENES EVENTOS
				// ************************************************************************
				imageName = jsonObject.getString("imageName");
				
				String imageURL = "http://www.biljetapp.com/img/" + imageName;
				imagePath = getFilesDir().getAbsolutePath()+"/eventsImage/"+imageName;
				
				File imgFolder = new File (getFilesDir().getAbsolutePath()+"/eventsImage");
				if(!imgFolder.exists())
					imgFolder.mkdir();
				
				File imgFile = new File(imagePath);
					
				try {
					// Si la imagen a descargar no esta almacenada en el telefono, la descargamos y guardamos en "data"
					if(!imgFile.exists())
						saveImageFromURL(imageURL,imagePath);
					
				} catch(IOException e2) {
						runOnUiThread(new Runnable() {
							  public void run() {
								  // Notificar error al guardar avatar
								  Toast.makeText(MyEventsActivity.this, "Error: No se pudo guardar avatar para el evento:", Toast.LENGTH_SHORT).show();
								  // Si se ha creado un archivo, borrarlo para que en la proxima conexion se vuelva a descargar
								  File fileToDelete = new File(imagePath);
								  if (fileToDelete.exists())
									  fileToDelete.delete();
							  }
						});

				} // catch
									
				// DATOS JSON NO REQUERIDOS (Pueden ser "null" => Lanzarán excepción)
				// ************************************************************************
				
				// Direccion (String => No lanza excepcion si es null)
				// ---------
				address = jsonObject.getString("address");
				if (address.equals("null"))
					address = "No especificado";
			
				
				// Codigo postal (Numero => Lanza excepcion si es null)
				// -------------
				try{
					postalCode = jsonObject.getInt("postalCode");
				} catch (JSONException e){
					postalCode = 0;
				}
				
				// Longitud
				// -------------
				try{
					latitude = jsonObject.getInt("latitude");;
				} catch (JSONException e){
					latitude = 0;
				}
				
				// Latitud
				// -------------
				try{
					longitude = jsonObject.getInt("longitude"); 
				} catch (JSONException e){
					longitude = 0;
				}

				Event event = new Event(title,
									    creatorId,
									   _id,
									   description,
									   imagePath,
									   category,
									   address,
									   "",
									   postalCode,
									   province,
									   longitude,
									   latitude,
									   date,
									   (float)price,
									   capacity,
									   0,0,0);
									
				eventsOrganized.add(event);

				Log.d("eventOrganized","\nAñadido "+ title +" al array");
			
			} //for
			
		} catch (JSONException e1) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(MyEventsActivity.this, "Error: No se pudieron leer los datos recibidos!", Toast.LENGTH_SHORT).show();
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
		Log.d("SaveImage","Inicio try saveImage");
		try {
			
		    input = url.openConnection().getInputStream();
			Log.d("tag3","Conexion hecha!");
		    output = new FileOutputStream(imgFile);

			Log.d("SaveImage","Abierto output!");
			
		    int read;
		    byte[] data = new byte[1024];
		    Log.d("tag3","Inicio bucle saveImage");
		    while ((read = input.read(data)) != -1)
		        output.write(data, 0, read);
		    
			Log.d("SaveImage","Termina bucle saveImage");
			
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
			eventList.setVisibility(View.INVISIBLE);
			actionBar.setProgressBarVisibility(View.VISIBLE);
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
			eventList.setVisibility(View.VISIBLE);
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
	
	// BOTON MENU TELEFONO
	// **************************************************************************************
	
	/*
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

