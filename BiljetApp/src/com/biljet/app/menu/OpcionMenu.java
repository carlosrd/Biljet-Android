package com.biljet.app.menu;

public class OpcionMenu {
	private int icono;
	private String titulo;
	
	public OpcionMenu(String tit, int ico){
		icono = ico;
		titulo = tit;
	}

	public int getIcono() {
		return icono;
	}

	public String getTitulo() {
		return titulo;
	}


}