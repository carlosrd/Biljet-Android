package com.biljet.tipos;

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
}
