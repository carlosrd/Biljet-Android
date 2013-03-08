package com.biljet.app;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.types.Date;
import com.biljet.types.Event;
import com.biljet.types.User;

public class MyProfileActivity extends ActivitiesHeader {

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
		
	//eventsFollow
		String evFollow = "";
		for(Event ev:userProfile.getEventsFollow())	 evFollow = evFollow + ev.getName() + "\n"; 
		
		TextView txtEventsFollow = (TextView)findViewById(R.id.myProfile_TxtEventsFollow);
		txtEventsFollow.setText(evFollow);	
			
	//eventsOrganized
		String evOrganized = "";
		for(Event ev:userProfile.getEventsOrganized())	 evOrganized = evOrganized + ev.getName() + "\n"; 
	
		TextView txtEventsOrganized = (TextView)findViewById(R.id.myProfile_TxtEventsOrganized);
		txtEventsOrganized.setText(evOrganized);	
		
	//eventsSignup
		String evSignup = "";
		for(Event ev:userProfile.getEventsSignup())	 evSignup = evSignup + ev.getName() + "\n"; 
		
		TextView txtEventsSignup = (TextView)findViewById(R.id.myProfile_TxtEventsSignup);
		txtEventsSignup.setText(evSignup);
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_profile, menu);
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
	    
	    Event Event1 = new Event("Event al que voy 1",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,0,0, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event2 = new Event("Event al que voy 2",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
	
		sampleEvents.add(Event1);
	    sampleEvents.add(Event2);
	    
	    return sampleEvents;
    }
    
    private ArrayList<Event> addEventsOrganized(){
	    ArrayList<Event> sampleEvents = new ArrayList<Event>();
	    
	    Event Event1 = new Event("Event que organizo 1",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,0,0, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event2 = new Event("Event que organizo 2",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
	
		sampleEvents.add(Event1);
	    sampleEvents.add(Event2);
	    
	    return sampleEvents;
    }
    
    private ArrayList<Event> addEventsSignup(){
	    ArrayList<Event> sampleEvents = new ArrayList<Event>();
	    
	    Event Event1 = new Event("Event que sigo 1",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,0,0, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event2 = new Event("Event que sigo 2",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
	
		sampleEvents.add(Event1);
	    sampleEvents.add(Event2);
	    
	    return sampleEvents;
    }
    
}

