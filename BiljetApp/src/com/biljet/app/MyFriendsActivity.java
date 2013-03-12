package com.biljet.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.adapters.FriendsAdapter;
import com.biljet.types.Friend;

public class MyFriendsActivity extends ActivitiesHeader {
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friends);
        
        // ACTION BAR
     	// **************************************************************************************
        
        createHeaderView(R.drawable.header_back_button,"Mis Amigos", android.R.drawable.ic_menu_search,true);
		setBackButton();
		setThisRightButtonAction(SearchActivity.class);
		
		// LIST VIEW
		// **************************************************************************************
        
        final ArrayList<Friend> itemsFriend = obtenerItems();
        FriendsAdapter adapter = new FriendsAdapter(this, itemsFriend);
        ListView friendsList = (ListView)findViewById(R.id.list_Friends);
          
        // Setear oyentes OnClick
        
        friendsList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int idFriend, long id) {
			//Acciones necesarias al hacer click
				Intent friendIntent = new Intent(MyFriendsActivity.this, FriendViewActivity.class);
				
//				Bundle dataBundle = new Bundle();
//				
//				//En realidad creo que se le puede pasar el tipo Evento usand (ya lo probaremos)
//				int i = 0;
//					i = i++; 
//				/*int img = itemsFriend.get(idFriend).getRutaImagen();
//				String name = itemsFriend.get(idFriend).getNombre();
//				String city = itemsFriend.get(idFriend).getCiudad();
//				*/
//				dataBundle.putInt("Avatar", itemsFriend.get(idFriend).getImagePath());
//				dataBundle.putString("Name", itemsFriend.get(idFriend).getName());
//				dataBundle.putString("City", itemsFriend.get(idFriend).getCity());	
//				dataBundle.putString("Bio", itemsFriend.get(idFriend).getBio());	
//				
//				friendIntent.putExtras(dataBundle);
		

				Friend friend= itemsFriend.get(idFriend);
				friendIntent.putExtra("friend",friend);

				startActivity(friendIntent);
							
				}
			});
        
        friendsList.setAdapter(adapter);
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_friends, menu);
		return true;
	}
    
    private ArrayList<Friend> obtenerItems() {
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
	    }
    
    public void setThisRightButtonAction(final Class actA){
		ImageButton rightButton = (ImageButton) findViewById(R.id.headerBotonDcho);
        rightButton.setOnClickListener(new OnClickListener() {
									     public void onClick(View arg0) {
									    	 Intent boton0 = new Intent(MyFriendsActivity.this, actA);
									    	 Bundle dataBundle = new Bundle();
									    	 dataBundle.putChar("amigo_evento", 'a');
									    	 boton0.putExtras(dataBundle);
									    	 startActivity(boton0);
									     }
								     });
	}
}


	
	

