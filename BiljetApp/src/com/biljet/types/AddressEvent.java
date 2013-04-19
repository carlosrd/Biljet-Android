package com.biljet.types;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressEvent implements Parcelable{
	String roadType; 	//Tipo de vía
	String roadName; 	//Nombre de la vía
	int number;			//Número de la vía
	String cp;			//Código postal, es String poque hay CP que empiezan poc el 0.
	String city;		//Ciudad
	String province;	//Provincia
	double latitude;	//latitud
	double longitude;	//longitud
	
	public AddressEvent(String rt, String rn, int n, String cp, String c, String p, double lat, double lng){
		this.roadType = rt;
		this.roadName = rn;
		this.number = n;
		this.cp = cp;
		this.city = c;
		this.province = p;
		this.latitude = lat;
		this.longitude = lng;
		
	}
	
	// GETTERS
	// ************************************************************************
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	//---------Parcelable Methods---------------//
	public AddressEvent(Parcel in)
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
		dest.writeString(roadType);
		dest.writeString(roadName);
		dest.writeInt(number);
		dest.writeString(cp);
		dest.writeString(city);
		dest.writeString(province);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		
	}
	
	private void readFromParcel(Parcel in) 
	{
		roadType = in.readString();
		roadName = in.readString();
		number = in.readInt();
		cp = in.readString();
		city = in.readString();
		province = in.readString();
		latitude = in.readDouble();
		longitude = in.readDouble();
	}
	
	public static final Parcelable.Creator<AddressEvent> CREATOR
    = new Parcelable.Creator<AddressEvent>() {
        public AddressEvent createFromParcel(Parcel in) {
            return new AddressEvent(in);
        }
 
        public AddressEvent[] newArray(int size) {
            return new AddressEvent[size];
        }
    };
    
    public String toString()
	{
    	return roadType+" "+roadName+", "+number+", "+cp+" "+city+" "+province;
    			//+latitude ", "+longitude
	}
}
