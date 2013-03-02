package com.biljet.tipos;

import java.util.ArrayList;


public class User{		// datos del usuario que serán usados en el perfil y en la configuración de la cuenta del usuario
	
// Atributes 
	protected long id;

	// atributos que se van a ver en el "perfil"
	protected String lastLogin;		// tipo Fecha????
	protected ArrayList<Evento> eventsFollow;		// eventos a los que sigo
	protected ArrayList<Evento> eventsOrganized;	// eventos que organizo yo
	protected ArrayList<Evento> eventsSignup;		// eventos en los que estoy registrado
	
	
	// atributos que se van a ver en el "perfil" y en la "configuración de la cuenta"
	protected int image;
	protected String name;
	protected String surname;
	protected String city;
	protected String biography;
	protected String twitter;
	protected String facebook;
	
	
	// atributos que se van a ver en la "configuración de la cuenta"
	protected boolean company;
	protected String userName;		// es el mismo que "name"??
	protected String password;
	protected String email;
	
	
// Constructor
	
	// constructora para el perfil
	public User(String lastLogin, ArrayList<Evento> eventsFollow, ArrayList<Evento> eventsOrganized, ArrayList<Evento> eventsSignup, int image, String name,
				String surname, String city, String biography, String twitter, String facebook)
	{
		super();
		this.lastLogin = lastLogin;
		this.eventsFollow = eventsFollow;
		this.eventsOrganized = eventsOrganized;
		this.eventsSignup = eventsSignup;
		this.image = image;
		this.name = name;
		this.surname = surname;
		this.city = city;
		this.biography = biography;
		this.twitter = twitter;
		this.facebook = facebook;
	}
	
	// constructora para la configuración de la cuenta
	public User(int image, String name, String surname, String city, String biography, String twitter, String facebook, boolean company, String userName, String password, String email)
	{
		super();
		this.image = image;
		this.name = name;
		this.surname = surname;
		this.city = city;
		this.biography = biography;
		this.twitter = twitter;
		this.facebook = facebook;
		this.company = company;
		this.userName = userName;
		this.password = password;
		this.email = email;
	}

// Methods
	public long getId() { return id; }
	
	public void setId(long id) { this.id = id; }
	
	public String getLastLogin() { return lastLogin; }
	
	public void setLastLogin(String lastLogin) { this.lastLogin = lastLogin; }
	
	public ArrayList<Evento> getEventsFollow() { return eventsFollow; }
	
	public void setEventsFollow(ArrayList<Evento> eventsFollow) { this.eventsFollow = eventsFollow; }
	
	public ArrayList<Evento> getEventsOrganized() { return eventsOrganized; }
	
	public void setEventsOrganized(ArrayList<Evento> eventsOrganized) { this.eventsOrganized = eventsOrganized; }
	
	public ArrayList<Evento> getEventsSignup() { return eventsSignup; }
	
	public void setEventsSignup(ArrayList<Evento> eventsSignup) { this.eventsSignup = eventsSignup; }
	
	public int getImage() { return image; }
	
	public void setImage(int image) { this.image = image; }
	
	public String getName() { return name; }
	
	public void setName(String name) { this.name = name; }
	
	public String getSurname() { return surname; }
	
	public void setSurname(String surname) { this.surname = surname; }
	
	public String getCity() { return city; }
	
	public void setCity(String city) { this.city = city; }
	
	public String getBiography() { return biography; }
	
	public void setBiography(String biography) { this.biography = biography; }
	
	public String getTwitter() { return twitter; }
	
	public void setTwitter(String twitter) { this.twitter = twitter; }
	
	public String getFacebook() { return facebook; }
	
	public void setFacebook(String facebook) { this.facebook = facebook; }
	
	public boolean isCompany() { return company; }
	
	public void setCompany(boolean company) { this.company = company; }
	
	public String getUserName() { return userName; }
	
	public void setUserName(String userName) { this.userName = userName; }
	
	public String getPassword() { return password; }
	
	public void setPassword(String password) { this.password = password; }
	
	public String getEmail() { return email; }
	
	public void setEmail(String email) { this.email = email; }
	
} // User
