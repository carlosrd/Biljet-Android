package com.biljet.types;

public class Date {

	int day;
	int month;
	int year;
	Time time;
	
	public Date(int d, int m, int y, int h, int min){
		day = d;
		month = m;
		year = y;
		time = new Time(h,min);
	}
}
