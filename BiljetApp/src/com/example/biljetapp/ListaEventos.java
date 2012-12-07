package com.example.biljetapp;

import java.util.HashMap;


public class ListaEventos{
	
	// Tabla de eventos: clave = identificador del evento; valor = Evento
	HashMap<Integer,Evento> tablaEventos = new HashMap<Integer,Evento>();
	
	// Constructora
	public ListaEventos(){
		
		/** Creamos varios eventos para meter en la tabla de eventos **/
		Evento evento1 = new Evento("Concierto ACDC",1 ,"Concierto", "Madrid", new Fecha(8,12,2012,22,00), 40, 30, 200, "Empresa1 Conciertos", "Concierto de ACDC en Madrid a las 22:00, ¡ No te lo pierdas!", 6);
		Evento evento2 = new Evento("Cumpleaños Hugo",2 ,"Fiesta", "Sevilla", new Fecha(15,2,2013,19,45), 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
		Evento evento3 = new Evento("Concierto Melendi",3 ,"Concierto", "Valladolid", new Fecha(20,7,2013,20,30), 10, 40, 25, "Empresa2 Conciertos", "Concierto de Melendi en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		Evento evento4 = new Evento("Cine Forum",4 ,"Cine", "Madrid", new Fecha(24,12,2012,21,00), 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);

		/** Insertamos los eventos en la tabla **/
		tablaEventos.put(evento1.getId(), evento1);
		tablaEventos.put(evento2.getId(), evento2);
		tablaEventos.put(evento3.getId(), evento3);
		tablaEventos.put(evento4.getId(), evento4);
	}
	
} // ListaEventos
