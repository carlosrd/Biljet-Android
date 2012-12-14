package com.biljet.app.menu;

public class OpcionEvento {
	private int icono;
	private String titulo;
	
	public OpcionEvento(String tit, int ico){
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