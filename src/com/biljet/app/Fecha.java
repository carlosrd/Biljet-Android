package com.biljet.app;

public class Fecha {
	int dia;


	int mes;
	int a�o;
	Hora hora;
	
	public Fecha(int d, int m, int a, int h, int min){
		dia = d;
		mes = m;
		a�o = a;
		hora = new Hora(h,min);
	}
	
	// GETTERS
	// ******************************************************************
	
	public int getDia() {
		return dia;
	}

	public int getMes() {
		return mes;
	}

	public int getA�o() {
		return a�o;
	}

	public Hora getHora() {
		return hora;
	}
}
