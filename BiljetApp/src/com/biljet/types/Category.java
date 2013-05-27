package com.biljet.types;

public class Category {
	/**
	 * option(value='leisure') Ocio
	 * (value='comedy') Comedia/Monólogos
	 * (value='cinema') Cine
	 * (value='concert') Concierto
	 * (value='conference') Conferencia
	 * (value='cultural') Cultural
	 * (value='competition') Concurso/Torneo
	 * (value='sport') Deportivo
	 * (value='trip') Excursión
	 * (value='exhibition') Exposición
	 * (value='party') Fiesta
	 * (value='musical') Musical
	 * (value='meeting') Reunión
	 * (value='theatre') Teatro
	 */
	public Category(){}
	
	/**
	 * Return the label (in spanish) to show on app
	 * @param category
	 * @return
	 */
	public String getLabel(String category){
		
		if (category.equals("leisure"))
			return "Ocio";
		else if (category.equals("comedy"))
				return "Comedia/Monólogos";
		else if (category.equals("cinema"))
				return "Cine";
		else if (category.equals("concert"))
				return "Concierto";
		else if (category.equals("conference"))
				return "Conferencia";
		else if (category.equals("cultural"))
				return "Cultural";
		else if (category.equals("competition"))
				return "Concurso/Torneo";
		else if (category.equals("sport"))
				return "Deportivo";
		else if (category.equals("trip"))
				return "Excursión";
		else if (category.equals("exhibition"))
				return "Exposicion";
		else if (category.equals("party"))
				return "Fiesta";
		else if (category.equals("musical"))
				return "Musical";
		else if (category.equals("meeting"))
				return "Reunión";
		else if (category.equals("theatre"))
				return "Teatro/Espectáculo";
		else
			return "Indefinido";
	}
	
	/**
	 * Translate the Label to a valid value for DB
	 * @param category
	 * @return
	 */
	public String getValue(String category){
		
		if (category.equals("Ocio"))
			return "leisure";
		else if (category.equals("Comedia/Monólogos"))
				return "comedy";
		else if (category.equals("Cine"))
				return "cinema";
		else if (category.equals("Concierto"))
				return "concert";
		else if (category.equals("Conferencia"))
				return "conference";
		else if (category.equals("Cultural"))
				return "cultural";
		else if (category.equals("Concurso/Torneo"))
				return "competition";
		else if (category.equals("Deportivo"))
				return "sport";
		else if (category.equals("Excursión"))
				return "trip";
		else if (category.equals("Exposición"))
				return "exhibition";
		else if (category.equals("Fiesta"))
				return "party";
		else if (category.equals("Musical"))
				return "musical";
		else if (category.equals("Reunión"))
				return "meeting";
		else if (category.equals("Teatro/Espectáculo"))
				return "theatre";
		else
			return "Indefinido";
	}
}
