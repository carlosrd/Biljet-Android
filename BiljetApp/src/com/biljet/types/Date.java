package com.biljet.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Date implements Parcelable{

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
	
	//---------Parcelable Methods---------------//
	public Date(Parcel in)
	{
		readFromParcel(in);
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(day);
		dest.writeInt(month);
		dest.writeInt(year);
		dest.writeParcelable(time, flags);
	}
	
	private void readFromParcel(Parcel in) 
	{
		day=in.readInt();
		month=in.readInt();
		year=in.readInt();
		time=(Time)in.readParcelable(Time.class.getClassLoader());
	}
	
	public static final Parcelable.Creator<Date> CREATOR
    = new Parcelable.Creator<Date>() {
        public Date createFromParcel(Parcel in) {
            return new Date(in);
        }
 
        public Date[] newArray(int size) {
            return new Date[size];
        }
    };
    
    public String toString()
	{
    	return day+"/"+month+"/"+year+" - "+time;
	}
}
