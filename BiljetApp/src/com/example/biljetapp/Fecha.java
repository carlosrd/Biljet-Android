package com.example.biljetapp;

public class Fecha {
	int dia;
	int mes;
	int año;
	Hora hora;
	
	public Fecha(int d, int m, int a, int h, int min){
		dia = d;
		mes = m;
		año = a;
		hora = new Hora(h,min);
	}
}
