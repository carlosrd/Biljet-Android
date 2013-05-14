package com.biljet.types;

public class MenuOption {
	
	private int icon;
	
	private String title;
	private String subtitle;
	
	public MenuOption(String tit, String sub,int ico){
		icon = ico;
		title = tit;
		subtitle = sub;
	}

	public int getIcon() {
		return icon;
	}

	public String getTitle() {
		return title;
	}

	public String getSubtitle() {
		return subtitle;
	}
}