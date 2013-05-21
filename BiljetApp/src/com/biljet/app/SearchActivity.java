package com.biljet.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.biljet.adapters.EventListAdapter;
import com.biljet.types.Category;
import com.biljet.types.Event;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class SearchActivity extends Activity {
	
	// ATRIBUTOS
 	// **************************************************************************************
	
	// Objetos de la actividad
	ActionBar actionBar;
	EditText filterText;
	ListView eventList;
	EventListAdapter eventListAdapter;
	
	// ArrayList para recoger resultados de la busqueda
	ArrayList<Event> itemsEvent;// = getEvents();
	
	// Conexion con la base de datos
	DBConnection connector;
	boolean connectionAlive;
	
	// Necesario en caso de fallo en la descarga del avatar del evento. Si falla, borramos el archivo
	String imagePath;		
	
	// CONSTRUCTORA
 	// **************************************************************************************
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

        // ACTION BAR
     	// **************************************************************************************

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Buscar");
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		filterText = (EditText)findViewById(R.id.search_EditText_Input);
		
		// leemos el boton de busqueda
		ImageButton searchButton = (ImageButton)findViewById(R.id.search_ImageButton_Search);
		searchButton.setOnClickListener(new OnClickListener() {
					   public void onClick(View arg0) {
							
						   	connector = new DBConnection();
							connectionAlive = true;
							connector.execute(filterText.getText().toString());
							
					   }
				   });
		
		// LIST VIEW
		// **************************************************************************************
		
		itemsEvent = new ArrayList<Event>();
		eventListAdapter = new EventListAdapter(this,itemsEvent);
        
		// Setear oyentes OnClick
		eventList = (ListView)findViewById(R.id.search_MatchesList);
        eventList.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int idEvent, long id) {
						//Acciones necesarias al hacer click
							
							Intent intentEvent = new Intent(SearchActivity.this, EventViewActivity.class);

							Event e = itemsEvent.get(idEvent);
							intentEvent.putExtra("EVENT",e);
							intentEvent.putExtra("OWN?", false);
							intentEvent.putExtra("NO_TICKET", true);
							
							startActivity(intentEvent);
										
							}
						});
        
        eventList.setAdapter(eventListAdapter);
    
		

	} // OnCreate()

	// AUXILIARES CONEXION DB
   	// **************************************************************************************
	
	private boolean getSearchQuery(String query){
	    
			// CONEXION CON DB
		   	// **************************************************************************************		
	    	InputStream is = null;
	    	
	    	try {
				HttpClient httpclient = new DefaultHttpClient();
				String URL = "http://www.biljetapp.com/api/event/search/" + query;
				URL = URL.replace(" ","%20");
				HttpGet getRequest = new HttpGet(URL);
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
						  Toast.makeText(SearchActivity.this,"Error al conectar con el servidor!",Toast.LENGTH_SHORT).show(); 
					  }
					});	
				
				return false;
				// En esta captura de excepci�n podemos ver que hay un problema con la
				// conexi�n e intentarlo m�s adelante.
			}
			
			if (connector.isCancelled())
				return false;
						
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
						  Toast.makeText(SearchActivity.this,"Error: No se ha podido completar la operacion!",Toast.LENGTH_SHORT).show(); 
					  }
					});	
				return false;
			}
			
			if (connector.isCancelled())
				return false;
			
			
			String title, creatorId, _id, description, imageName, category, 
				   address, city, place;
			int postalCode, province, capacity;
			double price, latitude, longitude;
			long date;
				
			itemsEvent.clear();
			
			try {
				Log.d("Search","Entra bucle JSON");
				JSONArray jsonArray = new JSONArray(result);
				JSONObject jsonObject = null;
				
				if (jsonArray.length() == 0){
					runOnUiThread(new Runnable() {
						  public void run() {
							  Toast.makeText(getBaseContext(), " ! : La b�squeda no encontr� resultados", Toast.LENGTH_LONG).show();
						  }
						});	
				}
				
				for(int i = 0; i < jsonArray.length(); i++){
					jsonObject = jsonArray.getJSONObject(i);
					
					// DATOS JSON REQUERIDOS (NO lanzar�n excepci�n; nunca seran "null")
					// ************************************************************************
					
					title = jsonObject.getString("title");
					creatorId = jsonObject.getString("creator");
					_id = jsonObject.getString("_id");
					description = jsonObject.getString("description");		
					category = jsonObject.getString("category");

					address = jsonObject.getString("address");
					city = jsonObject.getString("city");
					
					// Codigo postal (Numero => Lanza excepcion si no se puede convertir)
					// -------------
					try{
						postalCode = jsonObject.getInt("postalCode");
					} catch (JSONException e){
						postalCode = 0;
					}
					
					// Provincia (Numero => Lanza excepcion si no se puede convertir)
					// -------------
					try{
						province = jsonObject.getInt("province"); 
					} catch (JSONException e){
						province = 0;
					}
					
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
									  //Toast.makeText(UpcomingEventsActivity.this, "Error: No se pudo guardar avatar para el evento:", Toast.LENGTH_SHORT).show();
									  // Si se ha creado un archivo, borrarlo para que en la proxima conexion se vuelva a descargar
									  File fileToDelete = new File(imagePath);
									  if (fileToDelete.exists())
										  fileToDelete.delete();
								  }
							});

					} // catch
										
					// DATOS JSON NO REQUERIDOS (Pueden ser "null" => Lanzar�n excepci�n)
					// ************************************************************************

					// Lugar (String => No lanza excepcion si es null)
					// ---------
					place = jsonObject.getString("place");
					if (place.equals("null"))
						place = "";		
					
					// Longitud
					// -------------
					try{
						latitude = jsonObject.getDouble("latitude");;
					} catch (JSONException e){
						latitude = -1;
					}
					
					// Latitud
					// -------------
					try{
						longitude = jsonObject.getDouble("longitude"); 
					} catch (JSONException e){
						longitude = -1;
					}

					Event event = new Event(title,
										    creatorId,
										   _id,
										   description,
										   imagePath,
										   new Category().getLabel(category),
										   place,	
										   address,
										   city,	
										   postalCode,
										   province,
										   longitude,
										   latitude,
										   date,
										   (float)price,
										   capacity,
										   0,0,0);
										
					itemsEvent.add(event);
					
					Log.d("UpcomingEvents","\nA�adido el evento "+ title +" al array");
					
				} // for
				
			} catch (JSONException e1) {
				runOnUiThread(new Runnable() {
					  public void run() {
						  Toast.makeText(getBaseContext(), "Error: No se pudieron leer los datos recibidos!", Toast.LENGTH_SHORT).show();
					  }
					});	
				
				return false;
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
	
  
	// CONEXION CON DB EN SEGUNDO PLANO
   	// **************************************************************************************
	
    /*  Se instancia con 3 tipos:
    		1� - Tipo de datos de ENTRADA para doInBackground() => Datos de entrada de la tarea en segundo plano 
    		2� - Tipo de datos de ENTRADA para onProgressUpdate() y de ENTRADA para publishProgress() => Datos para mostrar el progreso
    		3� - Tipo de datos de SALIDA de doInBackground() y de ENTRADA en onPostExecute() => Datos para mostrar el fin de la tarea	
    */
    private class DBConnection extends AsyncTask<String,Integer,Boolean> {

	    @Override
	    protected void onPreExecute() {
			actionBar.setProgressBarVisibility(View.VISIBLE);
			eventList.setVisibility(View.INVISIBLE);
	    }
	 
		@Override
		protected Boolean doInBackground(String... params) {
			return getSearchQuery(params[0]);
		}
		
    	@Override
	    protected void onProgressUpdate(Integer... values) {
	
	    }
    	
	    @Override
	    protected void onPostExecute(Boolean result) {
			actionBar.setProgressBarVisibility(View.INVISIBLE);
			connectionAlive = false;
			eventList.setVisibility(View.VISIBLE);
			eventListAdapter.notifyDataSetChanged();
			
	    }
	 
	    @Override
	    protected void onCancelled() {
	    	actionBar.setProgressBarVisibility(View.INVISIBLE);
	    	connectionAlive = false;
	    	Toast.makeText(getBaseContext(), "Conexi�n cancelada por el usuario!", Toast.LENGTH_LONG).show();
	    }


    }

}// SearchActivity
