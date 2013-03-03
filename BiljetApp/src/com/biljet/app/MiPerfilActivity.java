package com.biljet.app;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.biljet.adaptadores.HeaderActividades;
import com.biljet.tipos.Evento;
import com.biljet.tipos.Fecha;
import com.biljet.tipos.User;

public class MiPerfilActivity extends HeaderActividades {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_perfil);
        
    //cabecera(true, drawable.home, MenuActivity.class, "Mi Perfil", false, android.R.drawable.ic_input_add, NuevoEventoActivity.class);
		inicializaVistaHeader(R.drawable.header_back_button,"Mi Perfil", android.R.drawable.ic_input_add,false);
		setBotonVolver();
		
		User userPerfil = getUser();
		
	//image
		ImageView imagen = (ImageView)findViewById(R.id.perfil_imagen);
		imagen.setImageResource(userPerfil.getImage());	
		imagen.setScaleType(ImageView.ScaleType.CENTER);
		
	//name and surname
		TextView txtNombre = (TextView)findViewById(R.id.perfil_TxtNombre);
		txtNombre.setText(userPerfil.getName() + " " + userPerfil.getSurname());//txtNombre.setText(bundleDatos.getString(userPerfil.getName() + " " + userPerfil.getSurname()));
		
	//city
		TextView txtCity = (TextView)findViewById(R.id.perfil_TxtCiudad);
		txtCity.setText(userPerfil.getCity());	
		
	//biography
		TextView txtBiografia = (TextView)findViewById(R.id.perfil_TxtBiografia);
		txtBiografia.setText(userPerfil.getBiography());
		
	//twitter
		TextView txtTwitter = (TextView)findViewById(R.id.perfil_TxtTwitter);
		txtTwitter.setText(userPerfil.getTwitter());
		
	//facebook
		TextView txtFacebook = (TextView)findViewById(R.id.perfil_TxtFacebook);
		txtFacebook.setText(userPerfil.getFacebook());
		
	//lastLogin
		TextView txtLastLogin = (TextView)findViewById(R.id.perfil_TxtLastLogin);
		txtLastLogin.setText(userPerfil.getLastLogin());	
		
	//eventsFollow
		String evFollow = "";
		for(Evento ev:userPerfil.getEventsFollow())	 evFollow = evFollow + ev.getNombre() + "\n"; 
		
		TextView txtEventsFollow = (TextView)findViewById(R.id.perfil_TxtEventsFollow);
		txtEventsFollow.setText(evFollow);	
			
	//eventsOrganized
		String evOrganized = "";
		for(Evento ev:userPerfil.getEventsOrganized())	 evOrganized = evOrganized + ev.getNombre() + "\n"; 
	
		TextView txtEventsOrganized = (TextView)findViewById(R.id.perfil_TxtEventsOrganized);
		txtEventsOrganized.setText(evOrganized);	
		
	//eventsSignup
		String evSignup = "";
		for(Evento ev:userPerfil.getEventsSignup())	 evSignup = evSignup + ev.getNombre() + "\n"; 
		
		TextView txtEventsSignup = (TextView)findViewById(R.id.perfil_TxtEventsSignup);
		txtEventsSignup.setText(evSignup);
		
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_mi_perfil, menu);
        return true;
    }
    
/** Me creo un perfil de prueba, el real será cogido de la base de datos **/
    private User getUser(){
    	
    	// cogemos sólo los atributos que se van a ver en el perfil
    	String lastLogin = "24-2-2013";
    	ArrayList<Evento> eventsFollow = addEventsFollow();
    	ArrayList<Evento> eventsOrganized = addEventsOrganized();
    	ArrayList<Evento> eventsSignup = addEventsSignup();
    	int image = R.drawable.usr_alan;
    	String name = "Ramón";
		String surname = "García";
		String city = "Madrid";
		String biography = "Soy Ramón García y me gusta dar las campanadas aunque éste año no me han dejado";
		String twitter = "ramoncinTwitter";
		String facebook = "ramoncinFacebook";
    	
    	return new User(lastLogin, eventsFollow, eventsOrganized, eventsSignup, image, name, surname, city, biography, twitter, facebook);
    }
    
    private ArrayList<Evento> addEventsFollow(){
	    ArrayList<Evento> Opciones = new ArrayList<Evento>();
	    
	    Evento evento1 = new Evento("Evento al que voy 1",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Fecha(20,7,2013,20,30), 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Evento evento2 = new Evento("Evento al que voy 2",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Fecha(15,2,2013,19,45), 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
	
		Opciones.add(evento1);
	    Opciones.add(evento2);
	    
	    return Opciones;
    }
    
    private ArrayList<Evento> addEventsOrganized(){
	    ArrayList<Evento> Opciones = new ArrayList<Evento>();
	    
	    Evento evento1 = new Evento("Evento que organizo 1",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Fecha(20,7,2013,20,30), 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Evento evento2 = new Evento("Evento que organizo 2",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Fecha(15,2,2013,19,45), 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
	
		Opciones.add(evento1);
	    Opciones.add(evento2);
	    
	    return Opciones;
    }
    
    private ArrayList<Evento> addEventsSignup(){
	    ArrayList<Evento> Opciones = new ArrayList<Evento>();
	    
	    Evento evento1 = new Evento("Evento que sigo 1",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Fecha(20,7,2013,20,30), 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Evento evento2 = new Evento("Evento que sigo 2",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Fecha(15,2,2013,19,45), 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
	
		Opciones.add(evento1);
	    Opciones.add(evento2);
	    
	    return Opciones;
    }
    
}

