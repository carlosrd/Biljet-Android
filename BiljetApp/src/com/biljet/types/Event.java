package com.biljet.types;

import android.os.Parcel;
import android.os.Parcelable;


public class Event implements Parcelable {

	
	/** EJEMPLO JSON EVENTO:
	 * 
	 *	"title": "Prueba2sdfs",
     *  "createdAt": 1368407246265,
     *  "price": 10,
     *  "creator": "518b0f03579e4f0000000001",
     *  "province": "3",
     *  "city": "Madrid",
     *  "postalCode": 23003,
     *  "address": "Calle de la amargura",
     *  "category": "teatro",
     *  "capacity": 24,
     *  "description": "Eventazo para todos",
     *  "_id": "51903cce086c7b0000000002",
     *  "__v": 0,
     *  "imageName": "eventDefault.png",
     *  "comments": [],
     *  "longitude": null,
     *  "latitude": null,
     *  "place": null,
     *  "followers": [],
     *  "attendee": [],
     *  "duration": null,
     *  "finishAt": 23423526346
	 */				
	
	// DEPRECATED
	int id;
	
	// ATRIBUTOS
	// ***************************************************************************************************
	
	String title;				// Titulo del evento (REQUERIDO)
	String creatorId;			// Id del usuario creador del evento (REQUERIDO)
	String _id;					// Id del evento
	String description;
	String imagePath;			// Ruta de la imagen del evento en el terminal
	String category;			// Tipo de evento (REQUERIDO)
	
	// Datos de la direccion
	String place;				// Nombre del lugar (Ej: Fac. de Informatica, Museo del Prado)
	String address;				// Direccion del evento
	String city;				// Ciudad del evento
	int postalCode;				// Codigo postal
	int province;				// Codigo de provincia (REQUERIDO)
	
	// Coordenadas geograficas para los mapas
	double latitude;				// Latitud
	double longitude;				// Longitud

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
				 String place,
				 String address,
				 String city,
				 int postalCode,
				 int province,
				 double longitude,
				 double latitude,
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
		
		this.place = place;
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
	
	// GETTERS
	// ************************************************************************
	
	public int getId() {
		return id;
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

	public String getSiteName() {
		return place;
	}
	
	public String getPlace() {
		return place;
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

	public int getDaysDuration() {
		return days_duration;
	}

	public int getHoursDuration() {
		return hours_duration;
	}

	public int getMinutesDuration() {
		return minutes_duration;
	}

	public String getDescription() {
		return description;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
	
	// SETTERS
	// ************************************************************************

	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	
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
			dest.writeString(_id);
			dest.writeString(imagePath);
			dest.writeString(description);
			dest.writeString(category);
			
			dest.writeString(place);
			dest.writeString(address);
			dest.writeString(city);
			dest.writeInt(province);
			dest.writeInt(postalCode);
			dest.writeDouble(longitude);
			dest.writeDouble(latitude);
			
			dest.writeLong(date);
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
			_id = in.readString();
			imagePath = in.readString();
			description = in.readString();
			category = in.readString();
			place = in.readString();
			address = in.readString();
			city = in.readString();
			province = in.readInt();
			postalCode = in.readInt();
			longitude = in.readDouble();
			latitude = in.readDouble();
			date = in.readLong();
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
