package com.biljet.types;

public class User {		// datos del usuario que serán usados en el perfil y en la configuración de la cuenta del usuario
	
	// ATRIBUTOS
	// ***************************************************************************************************
	
	String username;		// Nombre de usuario (Nick)
	String mail;			// Email del usuario
	String hash;			// Hash md5 de la contraseña

	String name;			// Nombre de la persona
	String surname;			// Apellidos
	String facebook;		// Perfil de FB
	String twitter;			// Perfil de Twitter
	
	// CONSTRUCTORA
	// ***************************************************************************************************
	
	public User(String username, 
				String mail, 
				String hash,
				String name,
				String surname,
				String facebook,
				String twitter){
		
		this.username = username;
		this.mail = mail;
		this.hash = hash;
		
		this.name = name;
		this.surname = surname;
		this.facebook = facebook;
		this.twitter = twitter;
		
	}
	
	// CONSULTORAS
	// ***************************************************************************************************
	
	public String getUsername() {
		return username;
	}

	public String getMail() {
		return mail;
	}

	public String getHash() {
		return hash;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getFacebook() {
		return facebook;
	}

	public String getTwitter() {
		return twitter;
	}

} // User
