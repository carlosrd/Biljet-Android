package com.biljet.types;

import android.os.Parcel;
import android.os.Parcelable;


public class Event implements Parcelable {

	
	/** EJEMPLO JSON EVENTO:
	 * 
	 *	"title": "IS Demo",
	 *	"createdAt": 1366903431960,
	 *	"price": 0,
     *  "creator": "51794a5a82890e4b08000001",
     *  "province": "32",
     *  "category": "Meeting",
     *  "capacity": "70",
     *  "description": "Evento para la demostración de IS en clase.",
     *  "_id": "51794a8782890e4b08000002",
     *  "__v": 0,
     *  "imageName": "eventDefault.png",
     *  "comments": [],
     *  "address": null,
     *  "longitude": null,
     *  "latitude": null,
     *  "postalCode": null,
     *  "followers": [],
     *  "attendee": [],
     *  "duration": null,
     *  "finishAt": 1369567500000
	 */
	
	// DEPRECATED
	int id;
	int image;	
	int score;					
	//Date date;					// Fecha del evento [finishAt] => TODO cambiar a java.Date
	
	// ATRIBUTOS
	// ***************************************************************************************************
	
	String title;				// Titulo del evento (REQUERIDO)
	String creatorId;			// Id del usuario creador del evento (REQUERIDO)
	String _id;					// Id del evento
	String description;
	String imagePath;			// Ruta de la imagen del evento en el terminal
	String category;			// Tipo de evento (REQUERIDO)
	String address;				// Direccion del evento
	String city;				// Ciudad del evento
	int postalCode;				// Codigo postal
	int province;				// Codigo de provincia (REQUERIDO)
	
	// Coordenadas geograficas para los mapas
	int latitude;				// Latitud
	int longitude;				// Longitud

	long date;					// Fecha del evento (TIMESTAMP: En miliseg desde 01/01/1970
	float price;				// Precio de la entrada (REQUERIDO)
	int confirmedPeople;		// Número de personas que van a asistir
	int capacity;				// Número de entradas totales (disponibles: capacity - confirmedPeople
	
	// Duración del evento
	int days_duration;			// Dias
	int hours_duration;			// Horas
	int minutes_duration;		// Minutos


	public Event(String title,
				 String creatorId,
				 String _id,
				 String description,
				 String imagePath,
				 String category,
				 String address,
				 String city,
				 int postalCode,
				 int province,
				 int longitude,
				 int latitude,
				 long date,
				 float price,
				 int capacity,
				 int days_duration,
				 int hours_duration,
				 int minutes_duration){
		
		this.title = title;				
		this.creatorId = creatorId;			
		this._id = _id;			
		this.description = description;
		this.imagePath = imagePath;			
		this.category = category;		
		
		this.address = address;				
		this.city = city;				
		this.postalCode = postalCode;				
		this.province = province;				
		
		this.latitude = latitude;				
		this.longitude = longitude;				
		
		this.date = date;					
		this.price = price;				
		//int confirmedPeople;		
		this.capacity = capacity;				
		
		
		this.days_duration = days_duration;			
		this.hours_duration = hours_duration;			
		this.minutes_duration = minutes_duration;		
			
	}
	
	
	/*
	public Event(String title, String id, String image, String eventType, 
			 String site, Date date, int days_duration, int hours_duration, int minutes_duration,
			 float price, int confirmedPeople, int capacity, String nameCreator, String eventInfo){
	
		this.title = title;
		this._id = id;
		this.imagePath = image;
		this.category = eventType;
		this.address = site;
		this.date = date;
		this.days_duration = days_duration;
		this.hours_duration = hours_duration;
		this.minutes_duration = minutes_duration;
		this.price = price;
		this.confirmedPeople = confirmedPeople;
		this.capacity = capacity;
		this.creatorId = nameCreator;
		this.description = eventInfo;
	
	} // DatosEvento*/
	
	// GETTERS
	// ************************************************************************
	
	public int getId() {
		return id;
	}

	public int getImage() {
		return image;
	}

	public int getScore() {
		return score;
	}

	public String getTitle() {
		return title;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public String get_id() {
		return _id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public String getCategory() {
		return category;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public int getPostalCode() {
		return postalCode;
	}

	public int getProvince() {
		return province;
	}

	public long getDate() {
		return date;
	}

	public float getPrice() {
		return price;
	}

	public int getConfirmedPeople() {
		return confirmedPeople;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getLength_days() {
		return days_duration;
	}

	public int getLength_hours() {
		return hours_duration;
	}

	public int getLength_minutes() {
		return minutes_duration;
	}

	public String getDescription() {
		return description;
	}

	// SETTERS
	// ************************************************************************
	

	//---------------------------Metodos de parcelable-----------------------------------------//
	
		public Event(Parcel in)
		{
			readFromParcel(in);
		}
		

		public static Parcelable.Creator<Event> getCreator() {
			return CREATOR;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) 
		{
			dest.writeString(title);
			//dest.writeInt(id);
			dest.writeString(_id);
			//dest.writeInt(image);
			dest.writeString(imagePath);
			dest.writeString(description);
			dest.writeString(category);
			dest.writeString(address);
			dest.writeInt(province);
			dest.writeInt(postalCode);
			dest.writeInt(longitude);
			dest.writeInt(latitude);
			dest.writeLong(date);
			//dest.writeParcelable(date, flags);//un object de tipo fecha...¿?
			dest.writeInt(days_duration);
			dest.writeInt(hours_duration);
			dest.writeInt(minutes_duration);
			dest.writeFloat(price);
			//dest.writeInt(confirmedPeople);
			dest.writeInt(capacity);
			dest.writeString(creatorId);
			

		}

		private void readFromParcel(Parcel in) 
		{
			title = in.readString();
			//id = in.readInt();
			_id = in.readString();
			//image  =  in.readInt();
			imagePath = in.readString();
			description = in.readString();
			category = in.readString();
			address = in.readString();
			province = in.readInt();
			postalCode = in.readInt();
			longitude = in.readInt();
			latitude = in.readInt();
			date = in.readLong();
			//date = in.readParcelable(Date.class.getClassLoader());
			days_duration = in.readInt();
			hours_duration = in.readInt();
			minutes_duration = in.readInt();
			price = in.readFloat();
			//confirmedPeople = in.readInt();
			capacity = in.readInt();
			creatorId = in.readString();

			// si hay objetos de una clase hay que hacer un CREATOR.
		}
		
		public static final Parcelable.Creator<Event> CREATOR
	    = new Parcelable.Creator<Event>() {
	        public Event createFromParcel(Parcel in) {
	            return new Event(in);
	        }
	 
	        public Event[] newArray(int size) {
	            return new Event[size];
	        }
	    };

		@Override
		public int describeContents() {

			return 0;
		}	
		
}// DatosEvento
