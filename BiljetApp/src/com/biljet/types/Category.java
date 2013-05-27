package com.biljet.types;

public class Category {
	/**
	 * option(value='leisure') Ocio
	 * (value='comedy') Comedia/Mon�logos
	 * (value='cinema') Cine
	 * (value='concert') Concierto
	 * (value='conference') Conferencia
	 * (value='cultural') Cultural
	 * (value='competition') Concurso/Torneo
	 * (value='sport') Deportivo
	 * (value='trip') Excursi�n
	 * (value='exhibition') Exposici�n
	 * (value='party') Fiesta
	 * (value='musical') Musical
	 * (value='meeting') Reuni�n
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
				return "Comedia/Mon�logos";
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
				return "Excursi�n";
		else if (category.equals("exhibition"))
				return "Exposicion";
		else if (category.equals("party"))
				return "Fiesta";
		else if (category.equals("musical"))
				return "Musical";
		else if (category.equals("meeting"))
				return "Reuni�n";
		else if (category.equals("theatre"))
				return "Teatro/Espect�culo";
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
		else if (category.equals("Comedia/Mon�logos"))
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
		else if (category.equals("Excursi�n"))
				return "trip";
		else if (category.equals("Exposici�n"))
				return "exhibition";
		else if (category.equals("Fiesta"))
				return "party";
		else if (category.equals("Musical"))
				return "musical";
		else if (category.equals("Reuni�n"))
				return "meeting";
		else if (category.equals("Teatro/Espect�culo"))
				return "theatre";
		else
			return "Indefinido";
	}
}
