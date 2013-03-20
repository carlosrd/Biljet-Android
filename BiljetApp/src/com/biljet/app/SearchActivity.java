package com.biljet.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.adapters.FriendsAdapter;
import com.biljet.adapters.UpcomingEventsAdapter;
import com.biljet.types.Date;
import com.biljet.types.Event;
import com.biljet.types.Friend;

public class SearchActivity extends ActivitiesHeader {
	
	// ATRIBUTOS
 	// **************************************************************************************

	ArrayList<Event> eventsArray = new ArrayList<Event>();
	ArrayList<Friend> friendsArray = new ArrayList<Friend>();
	
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
						   filled(c);
					   }
				   });
		
		

	} // OnCreate()

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}
	
	/**
	 * Actions related to the menu options displayed when you press ··· or Config button on the device
	 */
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(SearchActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	    }
	    return true;
	}

	
	/**
	 * method who fills the listView depending c's value
	 * @param c
	 */
	private void filled(char c){
		switch(c){
		
			// buscar eventos
			case 'e':
				UpcomingEventsAdapter adapterEvent = new UpcomingEventsAdapter(this, eventsArray);
				ListView eventList = (ListView)findViewById(R.id.list);
			    
			    eventList.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> a, View v, int idEvent, long id) {
					//Acciones necesarias al hacer click
			
						Intent intentEvent = new Intent(SearchActivity.this, EventViewActivity.class);
						
						Event e = eventsArray.get(idEvent);					
						intentEvent.putExtra("event",e);
						intentEvent.putExtra("OWN?", false);
						
						startActivity(intentEvent);
								
					}
				});
			
			    eventList.setAdapter(adapterEvent);
			    break;
	
			case 'a':
				
		        FriendsAdapter adapterFriends = new FriendsAdapter(this, friendsArray);
		        ListView friendList = (ListView)findViewById(R.id.list);
				
		        friendList.setOnItemClickListener(new OnItemClickListener() {
					public void onItemClick(AdapterView<?> a, View v, int idFriend, long id) {
					//Acciones necesarias al hacer click
						Intent intentFriend = new Intent(SearchActivity.this, FriendViewActivity.class);

						Friend friend= friendsArray.get(idFriend);
						intentFriend.putExtra("friend",friend);

						startActivity(intentFriend);
									
						}
					});
		        
		        friendList.setAdapter(adapterFriends);
				break;
		}// switch
			
	}
	
	/**
	 * method who fills the event list or friend list depending c's value
	 * @param c
	 */
	private void searchList(char c){		
		
		switch(c){
		
			// buscar eventos
			case 'e':
				
				//cogemos el nombre del evento que el usuario ha escogido
				filterText = (EditText) findViewById(R.id.filterText);
				String ev = filterText.getText().toString();
				
				ArrayList<Event> eventsList = getEvents();
				eventsArray = filterEvents(ev,eventsList);	// Rellenamos la lista de eventos con los eventos encontrados

				break;
				
			// buscar amigos
			case 'a':
				
				//cogemos el nombre del amigo que el usuario ha escogido
				filterText = (EditText) findViewById(R.id.filterText);
				String am = filterText.getText().toString();
				
				ArrayList<Friend> friendsList = getFriends();
				friendsArray = filterFriends(am,friendsList);	// Rellenamos la lista de amigos con los amigos encontrados			

				break;
		}// switch
	}
	

	/**
	 * method which filters events whose name match with s
	 * @param s
	 * @param eventsList
	 * @return
	 */
	private ArrayList<Event> filterEvents(String s, ArrayList<Event> eventsList){
		ArrayList<Event> resultado = new ArrayList<Event>();
		
		for(Event e:eventsList)		
			if(e.getName().contains(s))	 
				resultado.add(e);
		
		return resultado;
	}// filterEvents
	
	/**
	 * method which filters events whose name match with s
	 * @param s
	 * @param friendsList
	 * @return
	 */
	private ArrayList<Friend> filterFriends(String s, ArrayList<Friend> friendsList){
		ArrayList<Friend> resultado = new ArrayList<Friend>();
		
		for(Friend f:friendsList)		
			if(f.getName().contains(s))	
				resultado.add(f);
		
		return resultado;
	}// filterFriends	
	
	private ArrayList<Event> getEvents(){
		ArrayList<Event> sampleItems = new ArrayList<Event>();

		Event Event1 = new Event("Concierto ACDC",1 ,R.drawable.acdc_evento ,"Concierto", "Valladolid", new Date(8,12,2012,22,10),0,3,15, 40, 30, 200, "Empresa1 Conciertos", "Ya puede comprar las entradas ACDC. Los pioneros australianos del hard rock, se subirán de nuevo a los escenarios, este año presentarán su nueva gira mundial. La última vez que estuvieron de gira, fue hace 8 años. ¡Las entradas para la gira europea de AC/DC, están aquí disponibles! ¡Aproveche la oportunidad que le brindamos y oiga los éxitos legendarios de ACDC en concierto! Podrá oir el sonido inigualable de guitarra, que con el paso de los años no ha cambiado lo más mínimo. Los AC/DC siempre han sabido atraer a las masas. Las entradas para ver a los ACDC, le asegurarán una fantástica actuación en directo y podrá oir los éxitos “Back in Black”, “Highway to Hell” y “High Voltage” como también hits del último álbum “The Razor’s Edge” y “Ballbreaker”. Si nunca ha presenciado en directo a AC/DC, entonces no sabe lo que se pierde. Esta gira del grupo de rock duro, será seguramente, la última que ofrezcan, por eso tendrá que darse prisa, antes de que se agoten las entradas para los conciertos de AC/DC", 6);
		Event Event2 = new Event("Jessie J en concierto",2 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,2,45, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Event Event3 = new Event("Carrera Atlética",3 ,R.drawable.maraton_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
		Event Event4 = new Event("Cine Forum",4 ,R.drawable.cine_forum_evento ,"Cine", "Madrid", new Date(24,12,2012,21,20),2,0,0, 3, 10, 5, "ONG", "organiza un cine fórum sobre la conocida película de Luis García Berlanga “Bienvenido Mr. Marshall” en el Ensanche de Vallecas, a la salida del metro Valdecarros (Avenida del Ensanche s/n), uno de los terrenos barajados en la Comunidad de Madrid como posible ubicación de Eurovegas", 7);
	    
		sampleItems.add(Event1);
	    sampleItems.add(Event2);
	    sampleItems.add(Event3);
	    sampleItems.add(Event4);
	    
		return sampleItems;
	}// getEvents
	
	private ArrayList<Friend> getFriends(){
		ArrayList<Friend> Samples = new ArrayList<Friend>();
	     Samples.add(new Friend(1, "Alan sdfds", "Londres", R.drawable.usr_alan , "Alan Mathison Turing, es un matemático, lógico, científico de la computación, criptógrafo y filósofo británico. Es considerado uno de los padres de la ciencia de la computación siendo el precursor de la informática moderna"));
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
