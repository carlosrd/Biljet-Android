package com.biljet.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.biljet.adapters.UpcomingEventsAdapter;
import com.biljet.types.Date;
import com.biljet.types.Event;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class UpcomingEventsActivity extends Activity {

	ActionBar actionBar;
	DBConnection connector;
	UpcomingEventsAdapter eventListAdapter;
	boolean connectionAlive;
	ArrayList<Event> itemsEvent;// = getEvents();

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
     
        // ACTION BAR
     	// **************************************************************************************
		
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Próximos Eventos");
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.addAction(new IntentAction(this, new Intent(this, SearchActivity.class), R.drawable.buscar));
		
		// CONEXION CON DB EN SEGUNDO PLANO
	   	// **************************************************************************************
		Log.e("tag","\nComienzo seccion conexion DB");
		itemsEvent = new ArrayList<Event>();
		
		connector = new DBConnection();
		connectionAlive = true;
		connector.execute();

		// LIST VIEW
		// **************************************************************************************
		
		
		eventListAdapter = new UpcomingEventsAdapter(this,itemsEvent);
        ListView eventList = (ListView)findViewById(R.id.list_Events);
        
		// Setear oyentes OnClick
        
        eventList.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int idEvent, long id) {
						//Acciones necesarias al hacer click
							
							Intent intentEvent = new Intent(UpcomingEventsActivity.this, EventViewActivity.class);
							
//							Bundle dataBundle = new Bundle();
//							
//							//En realidad creo que se le puede pasar el tipo Event usand (ya lo probaremos)
//							dataBundle.putInt("IMAGE-URL", itemsEvent.get(idEvent).getImage());
//							dataBundle.putString("NAME", itemsEvent.get(idEvent).getName());
//							dataBundle.putString("EVENT_TYPE", itemsEvent.get(idEvent).getEventType());
//							dataBundle.putString("SITE", itemsEvent.get(idEvent).getSite());
//							dataBundle.putInt("PRICE", itemsEvent.get(idEvent).getPrice());
//							dataBundle.putInt("CONFIRMED_PEOPLE", itemsEvent.get(idEvent).getConfirmedPeople());
//							dataBundle.putInt("CAPACITY", itemsEvent.get(idEvent).getCapacity());
//							dataBundle.putString("INFO", itemsEvent.get(idEvent).getEventInfo());
//							
//							intentEvent.putExtras(dataBundle);
							
							Event e = itemsEvent.get(idEvent);
							intentEvent.putExtra("event",e);
							intentEvent.putExtra("OWN?", false);
							
							startActivity(intentEvent);
										
							}
						});
        
        eventList.setAdapter(eventListAdapter);
    }

    private boolean getEventsFromDB() {
	    
		// CONEXION CON DB
	   	// **************************************************************************************
		InputStream is = null;
		
		try {
			Log.e("tag","\nEntra en el try1");
			HttpClient httpclient = new DefaultHttpClient();
			//HttpPost httppost = new HttpPost("http://www.biljetapp.com/api/event");
			//HttpResponse response = httpclient.execute(httppost);
			HttpGet getRequest = new HttpGet("http://www.biljetapp.com/api/event");
			HttpResponse response = httpclient.execute(getRequest);
			StatusLine responseStatus = response.getStatusLine();
			int statusCode = responseStatus.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				}
			}
		catch(Exception e) {
			//System.out.println("Error en la conexión");
        	Toast.makeText(this,"Error al conectar con el servidor!",Toast.LENGTH_LONG).show(); 
			// En esta caputra de excepción podemos ver que hay un problema con la
			// conexión e intentarlo más adelante.
			}
		
		if (connector.isCancelled())
			return false;
			
		
		String result = "";
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line="0";
			
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			is.close();
			result = sb.toString();
			}
		catch(Exception e) {
        	Toast.makeText(this,"Error al obtener la cadena desde el buffer!",Toast.LENGTH_LONG).show(); 
			//System.out.println("Error al obtener la cadena desde el buffer");
			}
		
		if (connector.isCancelled())
			return false;
		
		String title, creator,province, category,_id, imageName, 
					  longitude, latitude, finishAt;
		long createdAt;
		int __v;
		JSONArray attendee, followers, comments;
		double price;
		ArrayList<Event> events = new ArrayList<Event>();
			
		itemsEvent.clear();
		
		try {
			Log.e("tag","\nEntra en el try3");
			JSONArray jsonArray = new JSONArray(result);
			JSONObject jsonObject = null;
			for(int i = 0; i < jsonArray.length(); i++){
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
				
				
				//comments = jsonObject.getJSONArray("comments");
				//longitude = jsonObject.getString("longitude"); 
				//latitude = jsonObject.getString("latitude"); 
				//followers= jsonObject.getJSONArray("followers");
				//attendee = jsonObject.getJSONArray("attendee");
				//finishAt = jsonObject.getString("finishAt");
				
				Event event = new Event(title, _id ,R.drawable.jessie_j_evento,category,""+province,new Date(0,0,0,0,0),0,0,0,(int) price,0,0,"","",0);
				
				itemsEvent.add(event);
				
				Log.e("tag","\nAñadido el evento "+title+" al array");
				}
			}
		catch(JSONException e1){
			Toast.makeText(getBaseContext(), "No se encuentran los datos", Toast.LENGTH_LONG).show();
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		if (connector.isCancelled())
			return false;
		
		return true;
    }
    
    
    public File getTempFile(Context context, String url) {
        File file = null;
        try {
            String fileName = Uri.parse(url).getLastPathSegment();
            file = File.createTempFile(fileName, null, context.getCacheDir());
        }
        catch (IOException e) {
            // Error while creating file
        }
        return file;
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
	    }
	 
		@Override
		protected Boolean doInBackground(Void... params) {
			getEventsFromDB();
			return true;
		}
		
    	@Override
	    protected void onProgressUpdate(Integer... values) {
	
	    }
    	
	    @Override
	    protected void onPostExecute(Boolean result) {
			actionBar.setProgressBarVisibility(View.INVISIBLE);
			connectionAlive = false;
			eventListAdapter.notifyDataSetChanged();
			
	    }
	 
	    @Override
	    protected void onCancelled() {
	    	actionBar.setProgressBarVisibility(View.INVISIBLE);
	    	connectionAlive = false;
	    	Toast.makeText(getBaseContext(), "Conexión cancelada por el usuario!", Toast.LENGTH_LONG).show();
	    }


    }
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

			switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (connectionAlive)
						connector.cancel(true);
					else
						finish();
					break;
			}

			return true;
	}
    
	// TECLA MENU ó ···
	// **************************************************************************************
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upcoming_events, menu);
        return true;
    }

	/**
	 * Actions related to the menu options displayed when you press ··· or Config button on the device
	 
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(UpcomingEventsActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	    }
	    return true;
	}*/

}
