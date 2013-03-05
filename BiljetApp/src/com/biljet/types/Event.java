package com.biljet.types;


public class Event {

	// Event Data
	String name;
	int id;
	int image;
	String eventType;
	String site;
	Date date;
	int price;					// price de la entrada
	int confirmedPeople;		// número de personas que van a asistir
	int capacity;				// Número de entradas totales (disponibles: capacity - confirmedPeople
	String nameCreator;			// name del Creator
	String eventInfo;			// Información detallada del evento
	int score;				// valoración del evento sobre 10
	
	// Constructora
	public Event(String name, int id, int image, String eventType, String site, Date date, int price, int confirmedPeople,
			int capacity, String nameCreator, String eventInfo, int score){
		
		this.name = name;
		this.id = id;
		this.image = image;
		this.eventType = eventType;
		this.site = site;
		this.date = date;
		this.price = price;
		this.confirmedPeople = confirmedPeople;
		this.capacity = capacity;
		this.nameCreator = nameCreator;
		this.eventInfo = eventInfo;
		this.score = score;
		
	} // DatosEvento

	// GETTERS
	// ************************************************************************
	
	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}

	public int getImage() {
		return image;
	}

	public String getEventType() {
		return eventType;
	}

	public String getSite() {
		return site;
	}

	public Date getDate() {
		return date;
	}

	public int getPrice() {
		return price;
	}

	public int getConfirmedPeople() {
		return confirmedPeople;
	}

	public int getCapacity() {
		return capacity;
	}

	public String getNameCreator() {
		return nameCreator;
	}

	public String getEventInfo() {
		return eventInfo;
	}

	public int getScore() {
		return score;
	}

	// SETTERS
	// ************************************************************************
	
	public void setName(String name) {
		this.name = name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public void setEvent_type(String eventType) {
		this.eventType = eventType;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setConfirmedPeople(int confirmedPeople) {
		this.confirmedPeople = confirmedPeople;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public void setNameCreator(String nameCreator) {
		this.nameCreator = nameCreator;
	}

	public void setEventInfo(String eventInfo) {
		this.eventInfo = eventInfo;
	}

	public void setScore(int score) {
		this.score = score;
	}

	
	
}// DatosEvento
