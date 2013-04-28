package com.biljet.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.adapters.ImageAdapter;
import com.biljet.types.Date;
import com.biljet.types.Event;
import com.biljet.types.User;

public class MyProfileActivity extends ActivitiesHeader {
   
	final ArrayList<Integer> array_e_f = new ArrayList<Integer>();
	final ArrayList<Integer> array_e_o = new ArrayList<Integer>();
	final ArrayList<Integer> array_e_s = new ArrayList<Integer>();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
   
    //cabecera(true, drawable.home, MenuActivity.class, "Mi Profile", false, android.R.drawable.ic_input_add, NuevoEventActivity.class);
        createHeaderView(R.drawable.header_back_button,"Mi Perfil", android.R.drawable.ic_input_add,false);
		setBackButton();

		User userProfile = getUser();

	//image
		ImageView image = (ImageView)findViewById(R.id.myProfile_Avatar);
		image.setImageResource(userProfile.getImage());	
		image.setScaleType(ImageView.ScaleType.CENTER);
		
	//name and surname
		TextView txtNombre = (TextView)findViewById(R.id.myProfile_TxtName);
		txtNombre.setText(userProfile.getName() + " " + userProfile.getSurname());//txtNombre.setText(bundleDatos.getString(userProfile.getName() + " " + userProfile.getSurname()));
		
	//city
		TextView txtCity = (TextView)findViewById(R.id.myProfile_TxtCiudad);
		txtCity.setText(userProfile.getCity());	
		
	//biography
		TextView txtBiografia = (TextView)findViewById(R.id.myProfile_TxtBio);
		txtBiografia.setText(userProfile.getBiography());
		
	//twitter
		TextView txtTwitter = (TextView)findViewById(R.id.myProfile_TxtTwitter);
		txtTwitter.setText(userProfile.getTwitter());
		
	//facebook
		TextView txtFacebook = (TextView)findViewById(R.id.myProfile_TxtFacebook);
		txtFacebook.setText(userProfile.getFacebook());
		
	//lastLogin
		TextView txtLastLogin = (TextView)findViewById(R.id.myProfile_TxtLastLogin);
		txtLastLogin.setText(userProfile.getLastLogin());	
		
		
	// metemos solo las imagenes de los eventos en la galería pictures_events_follow
		final ArrayList<String> nombres_f = new ArrayList<String>();
		for(Event ev:userProfile.getEventsFollow()){
			array_e_f.add(ev.getImage());
			nombres_f.add(ev.getName());
		}
		
			
	// metemos solo las imagenes de los eventos en la galería myProfile_LabelEventsOrganized
		final ArrayList<String> nombres_o = new ArrayList<String>();
		for(Event ev:userProfile.getEventsOrganized()){
			array_e_o.add(ev.getImage());
			nombres_o.add(ev.getName());
		}
		
		
	// metemos solo las imagenes de los eventos en la galería pictures_events_signup
		final ArrayList<String> nombres_s = new ArrayList<String>();
		for(Event ev:userProfile.getEventsSignup()){
			array_e_s.add(ev.getImage());
			nombres_s.add(ev.getName());
		}

	// Adaptadores
		
		ImageAdapter adapter_e_f = new ImageAdapter(this, array_e_f);
		
		Gallery gallery_events_follow = (Gallery)findViewById(R.id.pictures_events_follow);
		gallery_events_follow.setAdapter(adapter_e_f);
		
		gallery_events_follow.setOnItemClickListener(new OnItemClickListener(){
			@SuppressWarnings("rawtypes")
			public void onItemClick(AdapterView parent, View v, int position, long id){
				Toast.makeText(MyProfileActivity.this, nombres_f.get(position), Toast.LENGTH_SHORT).show();
			}
		});
		
		ImageAdapter adapter_e_o = new ImageAdapter(this, array_e_o);
		
		Gallery gallery_events_organized = (Gallery)findViewById(R.id.pictures_events_organized);
		gallery_events_organized.setAdapter(adapter_e_o);
		
		gallery_events_organized.setOnItemClickListener(new OnItemClickListener(){
			@SuppressWarnings("rawtypes")
			public void onItemClick(AdapterView parent, View v, int position, long id){
				Toast.makeText(MyProfileActivity.this, nombres_o.get(position), Toast.LENGTH_SHORT).show();
			}
		});
		
		ImageAdapter adapter_e_s = new ImageAdapter(this, array_e_s);
		
		Gallery gallery_events_signup = (Gallery)findViewById(R.id.pictures_events_signup);
		gallery_events_signup.setAdapter(adapter_e_s);
		
		gallery_events_signup.setOnItemClickListener(new OnItemClickListener(){
			@SuppressWarnings("rawtypes")
			public void onItemClick(AdapterView parent, View v, int position, long id){
				Toast.makeText(MyProfileActivity.this, nombres_s.get(position), Toast.LENGTH_SHORT).show();
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_profile, menu);
        return true;
    }
    
	/**
	 * Actions related to the menu options displayed when you press ··· or Config button on the device
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(MyProfileActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	    }
	    return true;
	}

    
/** Me creo un Profile de prueba, el real será cogido de la base de datos **/
    private User getUser(){
    	
    	// cogemos sólo los atributos que se van a ver en el Profile
    	String lastLogin = "24-2-2013";
    	ArrayList<Event> eventsFollow = addEventsFollow();
    	ArrayList<Event> eventsOrganized = addEventsOrganized();
    	ArrayList<Event> eventsSignup = addEventsSignup();
    	int image = R.drawable.usr_alan;
    	String name = "Ramón";
		String surname = "García";
		String city = "Madrid";
		String biography = "Soy Ramón García y me gusta dar las campanadas aunque éste año no me han dejado";
		String twitter = "ramoncinTwitter";
		String facebook = "ramoncinFacebook";
    	
    	return new User(lastLogin, eventsFollow, eventsOrganized, eventsSignup, image, name, surname, city, biography, twitter, facebook);
    }
    
    private ArrayList<Event> addEventsFollow(){
	    ArrayList<Event> sampleEvents = new ArrayList<Event>();
	    
	    Event Event1 = new Event("Cine Forum",1 ,R.drawable.cine_forum_evento ,"Cine", "Madrid", new Date(24,12,2012,21,30),0 ,4,10, 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
		Event Event2 = new Event("Jessie J en concierto",2 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,2,45, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event3 = new Event("Carrera Atlética",3 ,R.drawable.maraton_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
		
		sampleEvents.add(Event1);
		sampleEvents.add(Event2);
		sampleEvents.add(Event3);
	    
	    return sampleEvents;
    }
    
    private ArrayList<Event> addEventsOrganized(){
	    ArrayList<Event> sampleEvents = new ArrayList<Event>();
	    
	    Event Event1 = new Event("Carrera Atlética",3 ,R.drawable.maraton_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
		Event Event2 = new Event("Cine Forum",1 ,R.drawable.cine_forum_evento ,"Cine", "Madrid", new Date(24,12,2012,21,20),0 ,4,10, 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
	    
		sampleEvents.add(Event1);
	    sampleEvents.add(Event2);
	    
	    return sampleEvents;
    }
    
    private ArrayList<Event> addEventsSignup(){
	    ArrayList<Event> sampleEvents = new ArrayList<Event>();
	    
	    Event Event1 = new Event("Jessie J en concierto",2 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,2,45, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event2 = new Event("Carrera Atlética",3 ,R.drawable.maraton_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
		Event Event3 = new Event("Cine Forum",4 ,R.drawable.cine_forum_evento ,"Cine", "Madrid", new Date(24,12,2012,21,20),2,0,0, 3, 10, 5, "ONG", "organiza un cine fórum sobre la conocida película de Luis García Berlanga “Bienvenido Mr. Marshall” en el Ensanche de Vallecas, a la salida del metro Valdecarros (Avenida del Ensanche s/n), uno de los terrenos barajados en la Comunidad de Madrid como posible ubicación de Eurovegas", 7);
	    
		sampleEvents.add(Event1);
	    sampleEvents.add(Event2);
	    sampleEvents.add(Event3);
	    
	    return sampleEvents;
    }
    
}

