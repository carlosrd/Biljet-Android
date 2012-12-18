package amigos;

public class ItemAmigo {
	protected long id;
	protected String rutaImagen;
	protected String nombre;
	protected String ciudad;
	
	
	public ItemAmigo() {
		this.nombre = "";
		this.ciudad = "";
		this.rutaImagen = "";
	}
	
	public ItemAmigo(long id, String nombre, String ciudad) {
		this.id = id;
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.rutaImagen = "";
	}
	
	public ItemAmigo(long id, String nombre, String ciudad, String rutaImagen) {
		this.id = id;
		this.nombre = nombre;
		this.ciudad = ciudad;
		this.rutaImagen = rutaImagen;
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getRutaImagen() {
		return rutaImagen;
	}
	
	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getCiudad() {
		return ciudad;
	}
	
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
}
