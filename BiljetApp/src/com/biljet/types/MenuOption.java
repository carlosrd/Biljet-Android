package com.biljet.types;

public class MenuOption {
	
	private int icon;
	
	private String title;
	
	public MenuOption(String tit, int ico){
		icon = ico;
		title = tit;
	}

	public int getIcon() {
		return icon;
	}

	public String getTitle() {
		return title;
	}


}