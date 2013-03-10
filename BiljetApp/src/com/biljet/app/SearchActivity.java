package com.biljet.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.adapters.UpcomingEventsAdapter;
import com.biljet.types.Date;
import com.biljet.types.Event;
import com.biljet.types.Friend;

public class SearchActivity extends ActivitiesHeader {
	
	// ATRIBUTOS
 	// **************************************************************************************
	   
	//final ArrayList<Event> searchEvents = new ArrayList<Event>();
	//final ArrayList<Friend> searchFriends = new ArrayList<Friend>();
	
	ArrayList<Event> pruebaEventos = new ArrayList<Event>();
	
	EditText filterText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

        // ACTION BAR
     	// **************************************************************************************
	    createHeaderView(R.drawable.header_back_button,"Buscar", android.R.drawable.ic_input_add,false);
		setBackButton();
		
		// leemos el boton de busqueda
		Button searchButton = (Button)findViewById(R.id.search_button);
		searchButton.setOnClickListener(new OnClickListener() {
					   public void onClick(View arg0) {
						   Bundle b = getIntent().getExtras();
						   char c = b.getChar("amigo_evento");
						   searchList(c);
						   filled();
					   }
				   });
		
		

	} // OnCreate()

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	private void filled(){
		
		UpcomingEventsAdapter adapter = new UpcomingEventsAdapter(this, pruebaEventos);
	       ListView eventList = (ListView)findViewById(R.id.list);
	        
	        eventList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> a, View v, int idEvent, long id) {
				//Acciones necesarias al hacer click
					
					Intent intentEvent = new Intent(SearchActivity.this, EventViewActivity.class);
					
					Event e = pruebaEventos.get(idEvent);					
					intentEvent.putExtra("event",e);
					intentEvent.putExtra("OWN?", false);
						
					startActivity(intentEvent);
								
					}
				});

	        eventList.setAdapter(adapter);
	        
	}
	
	private void searchList(char c){		
		
		switch(c){
		
			// buscar eventos
			case 'e':
				
				//cogemos el nombre del evento que el usuario ha escogido
				filterText = (EditText) findViewById(R.id.filterText);
				String s = filterText.getText().toString();
				
				ArrayList<Event> eventsList = getEvents();
				pruebaEventos = filterEvents(s,eventsList);	// Rellenamos la lista de eventos con los eventos encontrados

				Toast.makeText(this, s,Toast.LENGTH_SHORT).show();
				break;
				
				
			// buscar amigos
			case 'a':
				Toast.makeText(this,"Buscar amigos",Toast.LENGTH_SHORT).show(); 
				break;
		}// switch
	}
	
	private ArrayList<Event> filterEvents(String s, ArrayList<Event> eventsList){
		ArrayList<Event> resultado = new ArrayList<Event>();
		
		for(Event e:eventsList)		
			if(e.getName().equals(s))	
				resultado.add(e);
		
		return resultado;
	}// filterEvents
	
	
	private ArrayList<Event> getEvents(){
		ArrayList<Event> sampleItems = new ArrayList<Event>();
		Event Event1 = new Event("Concierto Jessie J",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,2,45, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event2 = new Event("Cumpleaños Hugo",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
		Event Event3 = new Event("ho",1 ,R.drawable.jessie_j_evento ,"Cine", "Madrid", new Date(24,12,2012,21,00), 0,4,10, 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
	    
		sampleItems.add(Event1);
	    sampleItems.add(Event2);
	    sampleItems.add(Event3);
	    
		return sampleItems;
	}// getEvents
	
	private ArrayList<Friend> getFriends(){
		ArrayList<Friend> Samples = new ArrayList<Friend>();
	     Samples.add(new Friend(1, "Alan Turing", "Londres", R.drawable.usr_alan , "Alan Mathison Turing, es un matemático, lógico, científico de la computación, criptógrafo y filósofo británico. Es considerado uno de los padres de la ciencia de la computación siendo el precursor de la informática moderna"));
	     Samples.add(new Friend(2, "Albert Einstein", "Ulm", R.drawable.usr_albert,"Albert Einstein es un físico alemán de origen judío, nacionalizado después suizo y estadounidense. Es considerado como el científico más importante del siglo XX"));
	     Samples.add(new Friend(3, "Bill Gates", "Seattle", R.drawable.usr_bill,"William Henry Gates III, mejor conocido como Bill Gates, es un empresario y filántropo estadounidense, cofundador de la empresa de software Microsoft."));
	     Samples.add(new Friend(4, "Gordon Earl Moore", "San Francisco", R.drawable.usr_gordon,"Gordon Earl Moore es el cofundador de Intel y autor de la Ley de Moore. Nacido en San Francisco, California el 3 de enero de 1929. Recibió un certificado de bachiller de ciencias en química por la Universidad de California en Berkeley en 1950 y un Ph."));
	     Samples.add(new Friend(5, "Frank Gray", "Alpine", R.drawable.usr_gray,"Frank Gray es un fisico e investigador en los Laboratorios Bell. Hizo numerosas inovaciones mecanicas y electronicas en la televisión. Famoso por el Código Gray."));
	     Samples.add(new Friend(6, "Isaac Newton", "Londres", R.drawable.usr_isaac,"Sir Isaac Newton es un físico, filósofo, teólogo, inventor, alquimista y matemático inglés, autor de los Philosophiae naturalis principia mathematica, más conocidos como los Principia, donde describió la ley de la gravitación universal y estableció las bases de la mecánica clásica mediante las leyes que llevan su nombre."));
	     Samples.add(new Friend(7, "Linus B. Torvalds", "Helsinki", R.drawable.usr_linus," Linus Benedict Torvalds es un ingeniero de software finlandés estadounidense, conocido por iniciar y mantener el desarrollo del 'kernel' Linux, basándose en el sistema operativo libre Minix creado por Andrew S. Tanenbaum. "));
	     Samples.add(new Friend(8, "Richard Stallman", "Manhattan", R.drawable.usr_richard,"Richard Matthew Stallman, con frecuencia abreviado como «rms», es un programador estadounidense y fundador del movimiento por el software libre en el mundo."));
	     Samples.add(new Friend(9, "Sergey Brin", "Moscú", R.drawable.usr_sergey,"Sergey Mijaylovich Brin es un empresario ruso, de origen judío, conocido por ser uno de los creadores de Google y con un patrimonio estimado en más de 17,5 mil millones de dólares."));
	     Samples.add(new Friend(10, "Steve Jobs", "San Francisco", R.drawable.usr_steve,"Steven Paul Jobs, mejor conocido como Steve Jobs, fue un empresario y magnate de los negocios del sector informático y de la industria del entretenimiento estadounidense."));
	     Samples.add(new Friend(11, "Thomas John Watson", "Campbell", R.drawable.usr_thomas,"Thomas John Watson fue el presidente de IBM, y quien supervisó el crecimiento de la empresa hasta convertirla en una multinacional."));
	     Samples.add(new Friend(12, "Tim Berners-Lee", "Londres", R.drawable.usr_tim,"Thomas John Watson fue el presidente de IBM, y quien supervisó el crecimiento de la empresa hasta convertirla en una multinacional."));
	    
	     return Samples;
	}// getFriends
	
}// SearchActivity
