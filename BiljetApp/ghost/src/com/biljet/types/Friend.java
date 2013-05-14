package com.biljet.types;

import android.os.Parcel;
import android.os.Parcelable;

public class Friend implements Parcelable{

	protected long id;
	protected int avatar;
	protected String name;
	protected String city;
	protected String bio;
	
	public Friend() {
	this.name = "";
	this.city = "";
	
	}
	
	public Friend(long id, String name, String city, String bio) {
	this.id = id;
	this.name = name;
	this.city = city;	
	this.bio = bio;
	}
	
	public Friend(long id, String name, String city, int avatar, String bio) {
	this.id = id;
	this.name = name;
	this.city = city;
	this.avatar = avatar;
	this.bio = bio;
	}
	
	public long getId() {
	return id;
	}
	
	public void setId(long id) {
	this.id = id;
	}
	
	public int getImagePath() {
	return avatar;
	}
	
	public void setImagePath(int avatar) {
	this.avatar = avatar;
	}
	
	public String getName() {
	return name;
	}
	
	public void setName(String name) {
	this.name = name;
	}
	
	public String getCity() {
	return city;
	}
	
	public void setCity(String city) {
	this.city = city;
	}

	public String getBio() {
	return bio;
	}
	
	public void setBio(String bio) {
	this.bio = bio;
	}

	//---------------------------Metodos de parcelable-----------------------------------------//

		public Friend(Parcel in)
		{
			readFromParcel(in);
		}
		
		@Override
		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}
		
		private void readFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			id=in.readLong();
			avatar=in.readInt();
			name=in.readString();
			city=in.readString();
			bio=in.readString();		
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			dest.writeLong(id);
			dest.writeInt(avatar);
			dest.writeString(name);
			dest.writeString(city);
			dest.writeString(bio);
		}
		
		public static final Parcelable.Creator<Friend> CREATOR
	    = new Parcelable.Creator<Friend>() {
	        public Friend createFromParcel(Parcel in) {
	            return new Friend(in);
	        }
	 
	        public Friend[] newArray(int size) {
	            return new Friend[size];
	        }
	    };
}
