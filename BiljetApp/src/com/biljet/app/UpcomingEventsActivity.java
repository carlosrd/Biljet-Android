package com.biljet.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

	ArrayList<Event> itemsEvent;// = getEvents();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
     
        // ACTION BAR
     	// **************************************************************************************
        /*createHeaderView(R.drawable.header_back_button,"Proximos Eventos", R.drawable.buscar,true);
		setBackButton();*/
		
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Próximos Eventos");
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.addAction(new IntentAction(this, new Intent(this, SearchActivity.class), R.drawable.buscar));
		
		Log.e("tag","\nComienzo seccion conexion DB");
		// CONEXION CON DB
		InputStream is = null;
		try {
			Log.e("tag","\nEntra en el try1");
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("www.biljetapp.com/api/event");
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
			}
		catch(Exception e) {
			System.out.println("Error en la conexión");
			// En esta caputra de excepción podemos ver que hay un problema con la
			// conexión e intentarlo más adelante.
			}
		
		String cadena = null;
		
		try {
			Log.e("tag","\nEntra en el try2");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line="0";
			
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			
			is.close();
			cadena = sb.toString();
			}
		catch(Exception e) {
			System.out.println("Error al obtener la cadena desde el buffer");
			}
		    
		//Creamos los atributos de nuestra clase persona
		String title, creator,province, category,id, imageName, 
					  longitude, latitude, finishAt;
		int createdAt, _v;
		JSONArray attendee,followers,comments;
		double price;
	
		try {
			Log.e("tag","\nEntra en el try3");
			JSONArray jsonArray = new JSONArray(cadena);
			JSONObject jsonObject=null;
			for(int i=0;i<jsonArray.length();i++){
				jsonObject = jsonArray.getJSONObject(i);
				
				title = jsonObject.getString("title");
				createdAt = jsonObject.getInt("createdAt");
				price = jsonObject.getDouble("price");
				creator = jsonObject.getString("creator");
				province = jsonObject.getString("province"); 
				category = jsonObject.getString("category");
				id = jsonObject.getString("id");
				_v = jsonObject.getInt("_v");
				imageName = jsonObject.getString("imageName"); 
				//comments = jsonObject.getJSONArray("comments");
				//longitude = jsonObject.getString("longitude"); 
				//latitude = jsonObject.getString("latitude"); 
				//followers= jsonObject.getJSONArray("followers");
				//attendee = jsonObject.getJSONArray("attendee");
				//finishAt = jsonObject.getString("finishAt");
				
				Event event = new Event(title,Integer.parseInt(id),0,category,""+province,null,0,0,0,(int) price,0,0,"","",0);
				itemsEvent.add(event);
				Log.e("tag","\nAñadido el evento "+title+" al array");
				}
			}
		catch(JSONException e1){
			Toast.makeText(getBaseContext(), "No se encuentran los datos"
			,Toast.LENGTH_LONG).show();
		} 
		catch (ParseException e1) {
			e1.printStackTrace();
		}
		

		
		// LIST VIEW
		// **************************************************************************************

		UpcomingEventsAdapter adapter = new UpcomingEventsAdapter(this,itemsEvent);
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
        
        eventList.setAdapter(adapter);
    }

    private ArrayList<Event> getEvents() {
	    
    	ArrayList<Event> sampleEvents = new ArrayList<Event>();
	     
	    Event Event1 = new Event("Concierto ACDC",1 ,R.drawable.acdc_evento ,"Concierto", "Valladolid", new Date(8,12,2012,22,10),0,3,15, 40, 30, 200, "Empresa1 Conciertos", "Ya puede comprar las entradas ACDC. Los pioneros australianos del hard rock, se subirán de nuevo a los escenarios, este año presentarán su nueva gira mundial. La última vez que estuvieron de gira, fue hace 8 años. ¡Las entradas para la gira europea de AC/DC, están aquí disponibles! ¡Aproveche la oportunidad que le brindamos y oiga los éxitos legendarios de ACDC en concierto! Podrá oir el sonido inigualable de guitarra, que con el paso de los años no ha cambiado lo más mínimo. Los AC/DC siempre han sabido atraer a las masas. Las entradas para ver a los ACDC, le asegurarán una fantástica actuación en directo y podrá oir los éxitos “Back in Black”, “Highway to Hell” y “High Voltage” como también hits del último álbum “The Razor’s Edge” y “Ballbreaker”. Si nunca ha presenciado en directo a AC/DC, entonces no sabe lo que se pierde. Esta gira del grupo de rock duro, será seguramente, la última que ofrezcan, por eso tendrá que darse prisa, antes de que se agoten las entradas para los conciertos de AC/DC", 6);
		Event Event2 = new Event("Jessie J en concierto",2 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,2,45, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event3 = new Event("Carrera Atlética",3 ,R.drawable.maraton_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
		Event Event4 = new Event("Cine Forum",4 ,R.drawable.cine_forum_evento ,"Cine", "Madrid", new Date(24,12,2012,21,20),2,0,0, 3, 10, 5, "ONG", "organiza un cine fórum sobre la conocida película de Luis García Berlanga “Bienvenido Mr. Marshall” en el Ensanche de Vallecas, a la salida del metro Valdecarros (Avenida del Ensanche s/n), uno de los terrenos barajados en la Comunidad de Madrid como posible ubicación de Eurovegas", 7);
	    
		sampleEvents.add(Event1);
	    sampleEvents.add(Event2);
	    sampleEvents.add(Event3);
	    sampleEvents.add(Event4);

	    
	     return sampleEvents;
	    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.upcoming_events, menu);
        return true;
    }

	/**
	 * Actions related to the menu options displayed when you press ··· or Config button on the device
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(UpcomingEventsActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	    }
	    return true;
	}

}
