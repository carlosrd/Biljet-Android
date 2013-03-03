package com.biljet.tipos;

public class Amigo {
	
	protected long id;
	protected int imagen;
	protected String nombre;
	protected String ciudad;
	protected String biografia;
	
	public Amigo() {
	this.nombre = "";
	this.ciudad = "";
	
	}
	
	public Amigo(long id, String nombre, String ciudad, String biografia) {
	this.id = id;
	this.nombre = nombre;
	this.ciudad = ciudad;	
	this.biografia = biografia;
	}
	
	public Amigo(long id, String nombre, String ciudad, int imagen, String biografia) {
	this.id = id;
	this.nombre = nombre;
	this.ciudad = ciudad;
	this.imagen = imagen;
	this.biografia = biografia;
	}
	
	public long getId() {
	return id;
	}
	
	public void setId(long id) {
	this.id = id;
	}
	
	public int getRutaImagen() {
	return imagen;
	}
	
	public void setRutaImagen(int imagen) {
	this.imagen = imagen;
	}
	
	public String getNombre() {
	return nombre;
	}
	
	public void setNombre(String nombre) {
	this.nombre = nombre;
	}
	
	public String getCiudad() {
	return ciudad;
	}
	
	public void setCiudad(String ciudad) {
	this.ciudad = ciudad;
	}

	public String getBiografia() {
	return biografia;
	}
	
	public void setBiografia(String biografia) {
	this.biografia = biografia;
	}

	

}

