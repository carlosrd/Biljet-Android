package com.biljet.app;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.biljet.adaptadores.AdaptadorAmigos;
import com.biljet.adaptadores.HeaderActividades;
import com.biljet.tipos.Amigo;

public class MisAmigosActivity extends HeaderActividades {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_amigos);
        
        // ACTION BAR
     	// **************************************************************************************
		
        inicializaVistaHeader(R.drawable.header_back_button,"Mis Amigos", android.R.drawable.ic_menu_search,true);
		setBotonVolver();
		setActionBotonDcho(MisAmigosActivity.class);
		
		// LIST VIEW
		// **************************************************************************************
        
        final ArrayList<Amigo> itemsAmigo = obtenerItems();
        AdaptadorAmigos adapter = new AdaptadorAmigos(this, itemsAmigo);
        ListView listaAmigos = (ListView)findViewById(R.id.listaAmigos);
          
        // Setear oyentes OnClick
        
        listaAmigos.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> a, View v, int idAmigo, long id) {
			//Acciones necesarias al hacer click
				Intent intentAmigo = new Intent(MisAmigosActivity.this, VistaAmigoActivity.class);
				
				Bundle bundleDatos = new Bundle();
				
				//En realidad creo que se le puede pasar el tipo Evento usand (ya lo probaremos)
				int i = 0;
					i = i++; 
				/*int img = itemsAmigo.get(idAmigo).getRutaImagen();
				String name = itemsAmigo.get(idAmigo).getNombre();
				String city = itemsAmigo.get(idAmigo).getCiudad();
				*/
				bundleDatos.putInt("Imagen", itemsAmigo.get(idAmigo).getRutaImagen());
				bundleDatos.putString("Nombre", itemsAmigo.get(idAmigo).getNombre());
				bundleDatos.putString("Ciudad", itemsAmigo.get(idAmigo).getCiudad());	
				bundleDatos.putString("Biografia", itemsAmigo.get(idAmigo).getBiografia());	
				
				intentAmigo.putExtras(bundleDatos);
		
				startActivity(intentAmigo);
							
				}
			});
        
        listaAmigos.setAdapter(adapter);
    }
    
    private ArrayList<Amigo> obtenerItems() {
	     ArrayList<Amigo> Opciones = new ArrayList<Amigo>();
	     Opciones.add(new Amigo(1, "Alan Turing", "Londres", R.drawable.usr_alan , "Alan Mathison Turing, es un matem�tico, l�gico, cient�fico de la computaci�n, cript�grafo y fil�sofo brit�nico. Es considerado uno de los padres de la ciencia de la computaci�n siendo el precursor de la inform�tica moderna"));
	     Opciones.add(new Amigo(2, "Albert Einstein", "Ulm", R.drawable.usr_albert,"Albert Einstein es un f�sico alem�n de origen jud�o, nacionalizado despu�s suizo y estadounidense. Es considerado como el cient�fico m�s importante del siglo XX"));
	     Opciones.add(new Amigo(3, "Bill Gates", "Seattle", R.drawable.usr_bill,"William Henry Gates III, mejor conocido como Bill Gates, es un empresario y fil�ntropo estadounidense, cofundador de la empresa de software Microsoft."));
	     Opciones.add(new Amigo(4, "Gordon Earl Moore", "San Francisco", R.drawable.usr_gordon,"Gordon Earl Moore es el cofundador de Intel y autor de la Ley de Moore. Nacido en San Francisco, California el 3 de enero de 1929. Recibi� un certificado de bachiller de ciencias en qu�mica por la Universidad de California en Berkeley en 1950 y un Ph."));
	     Opciones.add(new Amigo(5, "Frank Gray", "Alpine", R.drawable.usr_gray,"Frank Gray es un fisico e investigador en los Laboratorios Bell. Hizo numerosas inovaciones mecanicas y electronicas en la televisi�n. Famoso por el C�digo Gray."));
	     Opciones.add(new Amigo(6, "Isaac Newton", "Londres", R.drawable.usr_isaac,"Sir Isaac Newton es un f�sico, fil�sofo, te�logo, inventor, alquimista y matem�tico ingl�s, autor de los Philosophiae naturalis principia mathematica, m�s conocidos como los Principia, donde describi� la ley de la gravitaci�n universal y estableci� las bases de la mec�nica cl�sica mediante las leyes que llevan su nombre."));
	     Opciones.add(new Amigo(7, "Linus B. Torvalds", "Helsinki", R.drawable.usr_linus," Linus Benedict Torvalds es un ingeniero de software finland�s estadounidense, conocido por iniciar y mantener el desarrollo del 'kernel' Linux, bas�ndose en el sistema operativo libre Minix creado por Andrew S. Tanenbaum. "));
	     Opciones.add(new Amigo(8, "Richard Stallman", "Manhattan", R.drawable.usr_richard,"Richard Matthew Stallman, con frecuencia abreviado como �rms�, es un programador estadounidense y fundador del movimiento por el software libre en el mundo."));
	     Opciones.add(new Amigo(9, "Sergey Brin", "Mosc�", R.drawable.usr_sergey,"Sergey Mijaylovich Brin es un empresario ruso, de origen jud�o, conocido por ser uno de los creadores de Google y con un patrimonio estimado en m�s de 17,5 mil millones de d�lares."));
	     Opciones.add(new Amigo(10, "Steve Jobs", "San Francisco", R.drawable.usr_steve,"Steven Paul Jobs, mejor conocido como Steve Jobs, fue un empresario y magnate de los negocios del sector inform�tico y de la industria del entretenimiento estadounidense."));
	     Opciones.add(new Amigo(11, "Thomas John Watson", "Campbell", R.drawable.usr_thomas,"Thomas John Watson fue el presidente de IBM, y quien supervis� el crecimiento de la empresa hasta convertirla en una multinacional."));
	     Opciones.add(new Amigo(12, "Tim Berners-Lee", "Londres", R.drawable.usr_tim,"Thomas John Watson fue el presidente de IBM, y quien supervis� el crecimiento de la empresa hasta convertirla en una multinacional."));
	    
	     return Opciones;
	    }
}
