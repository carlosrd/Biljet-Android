package com.biljet.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Time implements Parcelable{
	int hours;
	int minutes;
	
	public Time(int h, int m){
		hours = h;
		minutes = m;
	}
	
	//---------Parcelable Methods---------------//
	public Time(Parcel in)
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
			dest.writeInt(hours);
			dest.writeInt(minutes);
		}
		
		private void readFromParcel(Parcel in) 
		{
			hours=in.readInt();
			minutes=in.readInt();
		}
		
		public static final Parcelable.Creator<Time> CREATOR
	    = new Parcelable.Creator<Time>() {
	        public Time createFromParcel(Parcel in) {
	            return new Time(in);
	        }
	        public Time[] newArray(int size) {
	            return new Time[size];
	        }
		};
		
		public String toString()
		{
			return hours+":"+minutes;
		}	
}
