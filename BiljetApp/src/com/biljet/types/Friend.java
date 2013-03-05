package com.biljet.types;

public class Friend {

	protected long id;
	protected int avatar;
	protected String name;
	protected String city;
	protected String bio;
	
	public Friend() {
	this.name = "";
	this.city = "";
	
	}
	
	public Friend(long id, String name, String city, String bio) {
	this.id = id;
	this.name = name;
	this.city = city;	
	this.bio = bio;
	}
	
	public Friend(long id, String name, String city, int avatar, String bio) {
	this.id = id;
	this.name = name;
	this.city = city;
	this.avatar = avatar;
	this.bio = bio;
	}
	
	public long getId() {
	return id;
	}
	
	public void setId(long id) {
	this.id = id;
	}
	
	public int getImagePath() {
	return avatar;
	}
	
	public void setImagePath(int avatar) {
	this.avatar = avatar;
	}
	
	public String getName() {
	return name;
	}
	
	public void setName(String name) {
	this.name = name;
	}
	
	public String getCity() {
	return city;
	}
	
	public void setCity(String city) {
	this.city = city;
	}

	public String getBio() {
	return bio;
	}
	
	public void setBio(String bio) {
	this.bio = bio;
	}

	


}
