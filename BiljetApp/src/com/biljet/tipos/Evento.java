package com.biljet.tipos;

public class Evento {
		
	// Datos del evento:
	String nombre;
	int id;
	int imagen;
	String tipo;
	String lugar;
	Fecha fecha;
	int precio;					// precio de la entrada
	int numPersonasAsistir;		// número de personas que van a asistir
	int aforo;					// Número de entradas totales (disponibles: aforo - numPersonasAsistir
	String nombreCreador;		// Nombre del creador
	String infoEvento;			// Información detallada del evento
	int valoracion;				// valoración del evento sobre 10
	
	// Constructora
	public Evento(String nombre, int id, int imagen, String tipo, String lugar, Fecha fecha, int precio, int numPersonasAsistir,
			int aforo, String nombreCreador, String infoEvento, int valoracion){
		
		this.nombre = nombre;
		this.id = id;
		this.imagen = imagen;
		this.tipo = tipo;
		this.lugar = lugar;
		this.fecha = fecha;
		this.precio = precio;
		this.numPersonasAsistir = numPersonasAsistir;
		this.aforo = aforo;
		this.nombreCreador = nombreCreador;
		this.infoEvento = infoEvento;
		this.valoracion = valoracion;
		
	} // DatosEvento

	public int getImagen() {
		return imagen;
	}

	public void setImagen(int imagen) {
		this.imagen = imagen;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getLugar() {
		return lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public Fecha getFecha() {
		return fecha;
	}

	public void setFecha(Fecha fecha) {
		this.fecha = fecha;
	}

	public int getPrecio() {
		return precio;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}

	public int getNumPersonasAsistir() {
		return numPersonasAsistir;
	}

	public void setNumPersonasAsistir(int numPersonasAsistir) {
		this.numPersonasAsistir = numPersonasAsistir;
	}

	public int getAforo() {
		return aforo;
	}

	public void setNumEntradasDisponibles(int aforo) {
		this.aforo = aforo;
	}

	public String getNombreCreador() {
		return nombreCreador;
	}

	public void setNombreCreador(String nombreCreador) {
		this.nombreCreador = nombreCreador;
	}

	public String getInfoEvento() {
		return infoEvento;
	}

	public void setInfoEvento(String infoEvento) {
		this.infoEvento = infoEvento;
	}

	public int getValoracion() {
		return valoracion;
	}

	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}
	
}// DatosEvento
