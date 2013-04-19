package com.biljet.app;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.biljet.types.AddressEvent;
import com.biljet.types.Date;
import com.biljet.types.Event;
import com.biljet.types.User;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

/**
 *This class provides methods to save the preferences (settings) as:
 *
 *Sound notification.
 *Vibration notification.
 *Notification lighting.
 *
 *Receive e-mail notifications.
 *
 *Update personal information (phone, mobile, address)
 *
 *Change the user's password.
 */
@SuppressLint("SimpleDateFormat")
public class NewEventActivity extends Activity {
	
		// ATRIBUTOS
	    // **************************************************************************************
			  
		private Calendar dateTime = Calendar.getInstance();    
		
		private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
	    private SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a"); 
	    private static final int DIALOG_DATE = 1;
	    private static final int DIALOG_TIME = 2;
		protected static final int RESULT_LOAD_IMAGE = 1;	
		
	    private Button buttonDatePicker; 
	    private Button buttonTimePicker;    
	    private EditText editTextDate;
	    private EditText editTextTime;    
	    
	    private String typeEvent; //Tipo de evento que vamos a crear.
	    private int postImage = 0;
	    
	    // Array de imagales(locales) que vamos a utlizar como el logo del nuevo evento que vamos a crear.
	    final int [] arrayIMG = {R.drawable.logo_evento,R.drawable.android1,R.drawable.android2,R.drawable.android3};
	    
	    // Array de tipos de eventos.
	    final String [] arrayTypeEvents = {"Cine", "Cumpleaños", "Concierto", "Conferencia"}; 
	    
	    // Array de tipos de eventos.
	    final String [] arrayTypeRoads = {"Calle", "Avenida", "Plaza", "Paseo", "Parque", "Otro"}; 
	       
	    private User userProfile = getUser();
	    
	    //Variable que contrala para que ningún campos se quede sin rellenar
	    private boolean faltanCamposPorRellenar = false; 
	    
	    //Coordenadas del evento que la vamos a crear, éstas calcularemos a partir de la dirección dada.
	    
	    
	    Event NewEvent = null;
	    
	    //DATOS DEL EVENTO
	    private String name;
	    private int id;
	    private int logo;
	    
	    private AddressEvent  address;
			//Direccion 
	    private String typeRoad; 	//Tipo de vía
	    private String roadName; 	//Nombre de la vía
	    private int number;			//Número de la vía
	    private String cp;			//Código postal, es String poque hay CP que empiezan poc el 0.
	    private String city;		//Ciudad
	    private String province;	//Provincia
	    private double latitude;	//latitud
	    private double longitude;	//longitud
	    //private Date date;
	    private int length_days;
	    private int length_hours;
	    private int length_minutes;
	    private int price;
	    //private int confirmedPeople; por defecto se crea con 0
	    private int capacity;
	    private String nameCreator;
	    private String eventInfo;
	    //private int score; //por defecto se crea con 0	
		
	  
	    // OnCreate()
	  	// **************************************************************************************
	 	  
	   
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       
	        setContentView(R.layout.activity_new_event);	        
	
	        /*createHeaderView(R.drawable.header_back_button,"Nuevo Evento", -1,false);
			setBackButton();*/
			
			ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
			actionBar.setTitle("Crear Nuevo Evento");
			actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
			actionBar.setDisplayHomeAsUpEnabled(true);
			
			
			// BOTON SET DATE: botón para cambiar la fecha
			buttonDatePicker = (Button) findViewById(R.id.newEvent_ButtonSetDate);			
			editTextDate = (EditText) findViewById(R.id.newEvent_EditDate);			
			editTextDate.setText(dateFormatter.format(dateTime.getTime()));	
			
			buttonDatePicker.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                showDialog(DIALOG_DATE);
	            }
	        });
	 
			
			// BOTON SET DATE: botón para cambiar la fecha
			buttonTimePicker = (Button) findViewById(R.id.newEvent_ButtonSetTime);
			editTextTime = (EditText) findViewById(R.id.newEvent_EditTime);		
			editTextTime.setText(timeFormatter.format(dateTime.getTime()));
			
			buttonTimePicker.setOnClickListener(new View.OnClickListener() {
	 
	            public void onClick(View v) {
	                showDialog(DIALOG_TIME);
	            }
	        });			
			
			
			//BOTON  para cambiar la imagen.
			Button buttonChangeImage = (Button) findViewById(R.id.newEvent_ButtonChangeImage);
			buttonChangeImage.setOnClickListener(new View.OnClickListener() {             
	            public void onClick(View arg0) {
	                 
	                Intent intent = new Intent(
	                        Intent.ACTION_PICK,
	                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
	                 
	                startActivityForResult(intent, RESULT_LOAD_IMAGE);
	            }
	        });  //Listener buttonChangeImage  
			
			
			
			// SPINNER: para TIPO DE EVENT (Cine/Cumpleaños/Concierto/Conferencia)
	     	// **************************************************************************************
			
			ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayTypeEvents);	
			final Spinner spinnerTypesEvent = (Spinner)findViewById(R.id.newEvent_SpinnerEventType);
			adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTypesEvent.setAdapter(adaptadorSpinner);		
			
			spinnerTypesEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) {
					
					switch(position){
								//Cine
						case 0: typeEvent = arrayTypeEvents[position]; 
								break;
								//Cumpleaños							
						case 1: typeEvent = arrayTypeEvents[position];
								break;
								//Concierto
						case 2: typeEvent = arrayTypeEvents[position];
								break;
								//Conferencia
						case 3: typeEvent = arrayTypeEvents[position];
								break;		
						default:typeEvent = "Otro";		
					}				
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> arg0){
					// ...
				}
				
		  });		
			
			// SPINNER: para TIPO DE VIA (Calle/Avenida/Plaza/Paseo/Parque,...)
	     	// **************************************************************************************
			
			ArrayAdapter<String> adaptadorSpinnerRoad = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayTypeRoads);	
			final Spinner spinnerTypesRoad = (Spinner)findViewById(R.id.newEvent_SpinnerRoadType);
			adaptadorSpinnerRoad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerTypesRoad.setAdapter(adaptadorSpinnerRoad);		
			
			spinnerTypesRoad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) {
					
					switch(position){
								//Calle
						case 0: typeRoad = arrayTypeRoads[position]; 
								break;
								//Avenida							
						case 1: typeRoad = arrayTypeRoads[position];
								break;
								//Plaza
						case 2: typeRoad = arrayTypeRoads[position];
								break;
								//Paseo
						case 3: typeRoad = arrayTypeRoads[position];
								break;		
								//Parque
						case 4: typeRoad = arrayTypeRoads[position];
								//Otro
								break;		
						case 5: typeRoad = arrayTypeRoads[position];
								//Otro
								break;
						default:typeRoad = "Otro";	
					}				
				}
	
				@Override
				public void onNothingSelected(AdapterView<?> arg0){
					// ...
				}
				
		  });	
			
			addListenerChangeImage();
			
	    }  //onCreate
	    
	    //Método para cambiar las imagenes, mas adelante utulizaremos el metodo para acceder a la galeria de imagemes.    
		public void addListenerChangeImage() {				
				
				final ImageView image = (ImageView) findViewById(R.id.newEvent_Image);	 
				Button button = (Button) findViewById(R.id.newEvent_ButtonChangeImage);
				
				button.setOnClickListener(new OnClickListener() {				
					public void onClick(View arg0) {
						postImage = (postImage+1)%(arrayIMG.length);
						image.setImageResource(arrayIMG[postImage]);
					}
				});
		}
		 
		 
		 
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.new_event, menu);
	        return true;
	    }
	    
		/**
		 * Actions related to the menu options displayed when you press ··· or Config button on the device
		 */
		@Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
		        case R.id.indexSubmenu_optionSettings:
		        	Intent openSettings = new Intent(NewEventActivity.this,SettingsActivity.class);
		        	startActivity(openSettings);
		        	break;
		    }
		    return true;
		}

	    
	    /**
	     * Method to show the dialog date and time
	     */
	    @Override
	    protected Dialog onCreateDialog(int id) {
	        switch (id) {
	        
	        //Si se ha presionado el botón SET DATE, se abre la ventana de diálogo(Calendario) para seleccionar la fecha
	        case DIALOG_DATE:
	            return new DatePickerDialog(this, new OnDateSetListener() {                
	                public void onDateSet(DatePicker view, int year,
	                        int monthOfYear, int dayOfMonth) {
	                    dateTime.set(year, monthOfYear, dayOfMonth);
	                    editTextDate.setText(dateFormatter
	                            .format(dateTime.getTime()));
	                }
	            }, dateTime.get(Calendar.YEAR),
	               dateTime.get(Calendar.MONTH),
	               dateTime.get(Calendar.DAY_OF_MONTH));
	            
	            //Si se ha presionado el botón SET TIME, se abre la ventana de diálogo para seleccionar la fecha 
	        case DIALOG_TIME:
	            return new TimePickerDialog(this, new OnTimeSetListener() {	 
	
	                public void onTimeSet(TimePicker view, int hourOfDay,
	                        int minute) {
	                    dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
	                    dateTime.set(Calendar.MINUTE, minute);
	                    editTextTime.setText(timeFormatter
	                            .format(dateTime.getTime()));
	 
	                }
	            }, dateTime.get(Calendar.HOUR_OF_DAY),
	               dateTime.get(Calendar.MINUTE), false);
	        }
	        return null;
	    }
	    
	    /**
	     * Method that creates a new event with the data entered by user.
	     * @param view
	     */   
	    @SuppressLint("NewApi")
		public void buttonCreateNewEvent(View view) {    
	    	   	
	    	getDataNewEvent();  
	    	String direccion = address.toString();
	    	findLatitudeLongitude(direccion);
	    	
	    }
	    
	    /**
	     * Method alert to cancel
	     * @param view
	     */
	    public void alertCancelNewEvent(View view) {  //Alerta de cancelación del evento 	
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);
	    		//Título
				alertDialogBuilder.setTitle("Biljet");
	 
				// Mensaje del díalogo
				alertDialogBuilder
					.setMessage("¿Estás seguro que quieres cancelar la creación del evento "+((EditText) findViewById(R.id.newEvent_EditName)).getText().toString()+"?")
					.setCancelable(false)
					.setPositiveButton("Si",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							//Si se confirma la cancelacion, termina NewEventActivity
							NewEventActivity.this.finish();
						}
					  })
					.setNegativeButton("No",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							// Si se presiona el boton cancelar, no hace nada, NewEventActivity permanece activa.
							dialog.cancel();
						}
					}); 
	
					AlertDialog alertDialog = alertDialogBuilder.create();
					//Muestra la ventaga de alerta.
					alertDialog.show();
			}	    
	   
	   /**
	    * Method alert to required field
	    */
	   private void alertRequiredField(){
		   AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
	        dlgAlert.setMessage("Es obligatorio rellenar todos los campos");
	        dlgAlert.setTitle("Biljet");
	        dlgAlert.setPositiveButton("OK", null);
	        dlgAlert.setCancelable(true);
	        dlgAlert.create().show();  
	   }
	   
	   //Creación del NUEVO EVENTO.
	   /**
 		* Method to create a NEW EVENT
	    * @return Event object with all data related to it
	    */
	   
	   private void getDataNewEvent() {  
		   name = getNameNewEvent();
		   id = getIdNewEvent();  	 	   
		   
		   logo = getImageNewEvent();	
		   
		   //String site = getSiteNewEvent();
		   address = getAddressNewEvent(); 		 		   		  
			   roadName = getRoadNameNewEvent();				   
			   number = getNumberNewEvent();		   
			   cp = getCPNewEvent();
			   city = getCityNewEvent();		   
			   province = getProvinceNewEvent();			  
			   //latitude
			   //longitude ...las actualizamos con findLatitudeLongitude(dir...);
		   //Date date = getDateNewEvent();	//FIXME Cambiar el formato de fecha de la clase Event
		   price = getPriceNewEvent();	    	   
		   capacity = getCapacityNewEvent();				
		   length_days = getLengthDaysNewEvent();			
		   length_hours = getLengthHoursNewEvent();
		   length_minutes = getLengthMinutesNewEvent();
		   nameCreator = getNameCreatorNewEvent();			// Nombre del Creator - OBTENER LOS DATOS DEL USUARIO, DESDE PERFIL
		   eventInfo   = getInfoNewEvent();	
		  
		   
		}//CrearNewEvent
	          
	   
		// FUNCIONES
		// **************************************************************************************
	   
	   /**
	    * Returns the event name.
	    * @return
	    */
	   private String getNameNewEvent(){
		   EditText editTxtName = (EditText) findViewById(R.id.newEvent_EditName);	   
		   if(editTxtName.getText().toString().equals("")){
			   faltanCamposPorRellenar = true;
		   }
		   //Toast.makeText(NewEventActivity.this,"Nombre evento: "+editTxtName.getText().toString(), //Toast.LENGTH_LONG).show();
		   return editTxtName.getText().toString();
		   
	   }
	   
	   /**
	    * Returns the ID of the event will be the nth event organized by the user.
	    * @return 
	    */
	   private int getIdNewEvent(){   
		   //Toast.makeText(NewEventActivity.this,"id evento: "+userProfile.getEventsOrganized().size()+1, //Toast.LENGTH_LONG).show();
		   return userProfile.getEventsOrganized().size()+1;	   
	   }   
	   
	   /**
	    * Returns the event logo.
	    * @return
	    */
	   private int getImageNewEvent(){
		 //Usamos imagenes locales que estan en drawable, pero mas adelante habra que coger de la galeria de imgánes del dispositivo móvil
		  //Toast.makeText(NewEventActivity.this,"imagen evento: "+arrayIMG[postImage], //Toast.LENGTH_LONG).show();
		   return arrayIMG[postImage];  
	   }  
	   
	   /**
	    * Returns the event site.
	    * @return
	    */
	   
	   
	   /*
	    * SE PODRAN UTLIZAR ÉSTOS UNA VEZ CAMBIADO EL FORMATO FECHA DE LA CLASE Event
	   private String getDateNewEvent(){
		   EditText editTxtDate = (EditText) findViewById(R.id.newEvent_EditDate);
		   return editTxtDate.getText().toString();
	   }
	   
	   private String getTimeNewEvent(){
	   	   EditText editTxtTime = (EditText) findViewById(R.id.newEvent_EditTime);
	  	   return editTxtTime.getText().toString();	   
	   }
	   */
	   
	   /**
	    * Returns the event price.
	    * @return Ticket price
	    */
	   private int getPriceNewEvent(){
		   EditText editPrice = (EditText) findViewById(R.id.newEvent_EditPrice);	   
		   if(editPrice.getText().toString().equals("")){
			   faltanCamposPorRellenar = true;
			   return 0;
		   }
		   else{
			  return Integer.parseInt(editPrice.getText().toString());			   
		   } 	   
	   }  
	   
	   /**
	    * Returns place capacity where the event it's going to be celebrated
	    * @return capacity
	    */
	   private int getCapacityNewEvent(){				// Número de entradas totales (disponibles: capacity - confirmedPeople //AFORO
		   EditText editCapacity = (EditText) findViewById(R.id.newEvent_EditCapacity);
		   if(editCapacity.getText().toString().equals("")){		   
			   faltanCamposPorRellenar = true;
			   return 0;
		   }
		   else{
			   return Integer.parseInt(editCapacity.getText().toString());	
		   }
	   }   
	     
	   /**
	    * Returns the event length days
	    * @return
	    */
	   private int getLengthDaysNewEvent(){
		   EditText editDays = (EditText) findViewById(R.id.newEvent_EditLengthDays);
		   if(editDays.getText().toString().equals("")){
			   faltanCamposPorRellenar = true;
			   return 0;
		   }
		   else{
			   return Integer.parseInt(editDays.getText().toString());
		   }
	   }
	   
	   /**
	    * Returns the event length hours
	    * @return
	    */
	   private int getLengthHoursNewEvent(){
		   EditText editHours = (EditText) findViewById(R.id.newEvent_EditLengthHours);
		   if(editHours.getText().toString().equals("")){
			   faltanCamposPorRellenar = true;
			   return 0;
		   }
		   else{
			   return Integer.parseInt(editHours.getText().toString());
		   }
	   }
	   
	   /**
	    * Returns the event length minutes
	    * @return
	    */
	   private int getLengthMinutesNewEvent(){
		   EditText editMinutes = (EditText) findViewById(R.id.newEvent_EditLengthMinutes);
		   if(editMinutes.getText().toString().equals("")){		   
			   faltanCamposPorRellenar = true;
			   return 0;
		   }
		   else{
			   return Integer.parseInt(editMinutes.getText().toString());
		   }
	   }
	   
	   /**
	    * Return the name of the creator of the event 
	    * @return
	    */
	   private String getNameCreatorNewEvent(){			// Nombre del creador del evento- OBTENEMOS LOS DATOS DEL USUARIO DESDE SU PERFIL.
		   return userProfile.getName();
	   }
	   
	   /**
	    * Returns detailed event information
	    * @return
	    */
	   private String getInfoNewEvent(){			
		   EditText editInfo = (EditText) findViewById(R.id.newEvent_EditInfo);
		   if(editInfo.getText().toString().equals("")){
			   faltanCamposPorRellenar = true;
		   }
		   return editInfo.getText().toString();
	   }	   
	   
	   /**
	    * Returns the address where you will hold the event.
	    * @return
	    */
	   private AddressEvent getAddressNewEvent(){
		   
		   String tr = getRoadTypeNewEvent();
		   String tn = getRoadNameNewEvent();
		   int n = getNumberNewEvent();
		   String cp = getCPNewEvent();
		   String c = getCityNewEvent();
		   String p = getProvinceNewEvent();
		   
		   
		   return new AddressEvent(tr, tn, n, cp, c, p, 0, 0);
	   }
	   
	   /**
	    * Return the type of road of the location.
	    * @return
	    */
	   private String getRoadTypeNewEvent(){			   
		  return typeRoad;
	   }
	   
	   /**
	    * Returns the name of the road of the location
	    * @return
	    */
	   private String getRoadNameNewEvent(){			
		   EditText editRoadName = (EditText) findViewById(R.id.newEvent_EditRoadName);
		   if(editRoadName.getText().toString().equals("")){
			   faltanCamposPorRellenar = true;
		   }
		   
		   return editRoadName.getText().toString();
	   }
	   
	   /**
	    * Returns the number of the road of the location  
	    * @return
	    */
	   private int getNumberNewEvent(){			
		   EditText editNumber = (EditText) findViewById(R.id.newEvent_EditNumber);
		   if(editNumber.getText().toString().equals("")){
			   faltanCamposPorRellenar = true;
			   return 0;
		   }		  
		   
		   return Integer.parseInt(editNumber.getText().toString());
	   }
	   
	   /**
	    * Returns the postal code of the road of the location  
	    * @return
	    */
	   private String getCPNewEvent(){	//Código postal		
		   EditText editCP = (EditText) findViewById(R.id.newEvent_EditCP);
		   if(editCP.getText().toString().equals("")){
			   faltanCamposPorRellenar = true;
		   }
		   
		   return editCP.getText().toString();
	   }
	   
	   /**
	    * Returns the city of the location  
	    * @return
	    */
	   private String getCityNewEvent(){			
		   EditText editCity = (EditText) findViewById(R.id.newEvent_EditCity);
		   if(editCity.getText().toString().equals("")){
			   faltanCamposPorRellenar = true;
		   }

		   return editCity.getText().toString();
	   }
	   
	   /**
	    * Returns the province of the location  
	    * @return
	    */
	   private String getProvinceNewEvent(){			
		   EditText editProvince = (EditText) findViewById(R.id.newEvent_EditProvince);
		   if(editProvince.getText().toString().equals("")){
			   faltanCamposPorRellenar = true;
		   }
		   return editProvince.getText().toString();
	   }
	   
	
	   //Nos creamos un perfil de prueba consultar el nombre del usuario y el número de eventos organizados.
	   /**
	    * Method to create a user profile
	    * @return
	    */
	   private User getUser(){   	
		   String lastLogin = "24-2-2013";
		   ArrayList<Event> eventsFollow = addEventsFollow();
		   ArrayList<Event> eventsOrganized = addEventsOrganized();
		   ArrayList<Event> eventsSignup = addEventsSignup();
		   int image = R.drawable.usr_alan;
		   String name = "Ramón";
				String surname = "García";
				String city = "Madrid";
				String biography = "Soy Ramón García y me gusta dar las campanadas aunque éste año no me han dejado";
				String twitter = "ramoncinTwitter";
				String facebook = "ramoncinFacebook";
		   	
		   return new User(lastLogin, eventsFollow, eventsOrganized, eventsSignup, image, name, surname, city, biography, twitter, facebook);
	   }
	   
	   private ArrayList<Event> addEventsFollow(){
		   ArrayList<Event> sampleEvents = new ArrayList<Event>();
		    
		   Event Event1 = new Event("Cine Forum",1 ,R.drawable.cine_forum_evento ,"Cine", new AddressEvent("calle","Goya",3,"28001","Madrid", "Madrid", 40.42486156352708,-3.677171899999962), new Date(24,12,2012,21,30),0 ,4,10, 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
		   Event Event2 = new Event("Jessie J en concierto",2 ,R.drawable.jessie_j_evento ,"Concierto", new AddressEvent("Avenida","Complutense",10,"28040","Madrid", "Madrid", 40.447291113537815,-3.7274684000000207), new Date(20,7,2013,20,30),0,2,45, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		   Event Event3 = new Event("Carrera Atlética",3 ,R.drawable.maraton_evento ,"Fiesta", new AddressEvent("calle","Paseo de la Castellana",50,"28046","Madrid", "Madrid", 40.43522741353204,-3.6879860000000235), new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
			
		   sampleEvents.add(Event1);
		   sampleEvents.add(Event2);
		   sampleEvents.add(Event3);
					
		   return sampleEvents;
	    }
	    		
	   private ArrayList<Event> addEventsOrganized(){
		   ArrayList<Event> sampleEvents = new ArrayList<Event>();
	    	
		   Event Event1 = new Event("Carrera Atlética",3 ,R.drawable.maraton_evento ,"Fiesta", new AddressEvent("calle","Paseo de la Castellana",50,"28046","Madrid", "Madrid", 40.43522741353204,-3.6879860000000235), new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
		   Event Event2 = new Event("Cine Forum",1 ,R.drawable.cine_forum_evento ,"Cine", new AddressEvent("calle","Goya",3,"28001","Madrid", "Madrid", 40.42486156352708,-3.677171899999962), new Date(24,12,2012,21,20),0 ,4,10, 3, 10, 5, "ONG", "Película: Navidad, en Madrid a las 21:00 ¿La has visto? Coméntala", 7);
		    
		   sampleEvents.add(Event1);
		   sampleEvents.add(Event2);
		    
		   return sampleEvents;
	   }
	    
	   private ArrayList<Event> addEventsSignup(){
		   ArrayList<Event> sampleEvents = new ArrayList<Event>();
		    
		   Event Event1 = new Event("Jessie J en concierto",2 ,R.drawable.jessie_j_evento ,"Concierto",new AddressEvent("Avenida","Complutense",10,"28040","Madrid", "Madrid", 40.447291113537815,-3.7274684000000207), new Date(20,7,2013,20,30),0,2,45, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
		   Event Event2 = new Event("Carrera Atlética",3 ,R.drawable.maraton_evento ,"Fiesta", new AddressEvent("calle","Paseo de la Castellana",50,"28046","Madrid", "Madrid", 40.43522741353204,-3.6879860000000235), new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Empresa", "La Carrera Atlética 10 K VIVA! Surge como una actividad en la que la participación de los atletas nace de los sentimientos más profundos como una manera de expresar libremente el bienestar que produce la actividad física sumando este elemento a un estilo y forma de vida saludable, en un espacio para compartir, disfrutar, gozar, aprender y llegar a una alegría plena en busca de la excelencia en el mantenimiento de una vida sana, en una carrera con altos estándares de calidad", 3);
		   Event Event3 = new Event("Cine Forum",4 ,R.drawable.cine_forum_evento ,"Cine", new AddressEvent("calle","Goya",3,"28001","Madrid", "Madrid", 40.42486156352708,-3.677171899999962), new Date(24,12,2012,21,20),2,0,0, 3, 10, 5, "ONG", "organiza un cine fórum sobre la conocida película de Luis García Berlanga “Bienvenido Mr. Marshall” en el Ensanche de Vallecas, a la salida del metro Valdecarros (Avenida del Ensanche s/n), uno de los terrenos barajados en la Comunidad de Madrid como posible ubicación de Eurovegas", 7);
		    
		   sampleEvents.add(Event1);
		   sampleEvents.add(Event2);
		   sampleEvents.add(Event3);
		    
		   return sampleEvents;
	   }
	   
	   /**
	    * Method of finding the latitude and longitude of a given address using google geocoding
	    * @param address
	    */
	   public void findLatitudeLongitude(String address){
			   
            if(address!=null && !address.equals("")){
                //new GeocoderTask().execute(direccion);
                GeocoderTask Gt = new GeocoderTask();
                Gt.execute(address);               
                
               
            }
            else{
            	 
            	Toast.makeText(NewEventActivity.this, "Debe introducir la dirección donde se ha de realizar el evento", Toast.LENGTH_LONG).show();
           			 
            }
            
	       	
		}	   
	   
	   /**
	    * GeocoderTask class extends AsyncTask class, where it is to access the Web Service GeoCoding
	    */
	   //Clase GeocoderTask para calcular las coordenadas de la dirección
	   //*********************************************************************	   
	    public class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
	    	
	        @Override
	        protected List<Address> doInBackground(String... locationName) {
	            
	        	// Creating an instance of Geocoder class
	            Geocoder geocoder = new Geocoder(getBaseContext());
	            List<Address> addresses = null;
	 
	            try {
	                //Buscamos un máximo de 3 direcciones que coincidan con la dirección de entrada.
	            	
	                addresses = geocoder.getFromLocationName(locationName[0], 3);
	                
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	            return addresses;
	        }
	        @Override
	        protected void onPostExecute(List<Address> addresses) {
	        	
	        
                
	            if(addresses==null || addresses.size()==0){
	                //Toast.makeText(getBaseContext(), "No hemos encontrado su dirección", Toast.LENGTH_LONG).show();
	                Toast.makeText(NewEventActivity.this, "No hemos encontrado su dirección", Toast.LENGTH_LONG).show();
	                
	            }
	 
	            if(addresses.size()>0){
	 
	                Address address = (Address) addresses.get(0);
	                
	                //Obtenemos la latitud y longitud de la dirección dada. 
	                latitude = address.getLatitude();
	                longitude = address.getLongitude();	                
	                
	                Intent eventIntent = new Intent(NewEventActivity.this, EventViewActivity.class); 
	                
	                //Creamos el nuevo evento con los datos introducidos en el "formulario"
	                Event NewEvent = new Event(name ,id ,logo ,typeEvent, new AddressEvent(typeRoad , roadName, number, cp, city, province, latitude, longitude) , new Date(20,7,2013,20,30), length_days, length_hours, length_minutes, price, 0, capacity,  nameCreator, eventInfo, 0);
	               
	    	    	
	    			if(!faltanCamposPorRellenar){			
	    				eventIntent.putExtra("event",NewEvent);			
	    				startActivity(eventIntent);
	    				finish();
	    				 
	 	                
	    			}
	    			else{
	    				
	    				alertRequiredField();
	    			}	
	    			
	    		
	            }
	            else{
	            	//Toast.makeText(getBaseContext(), "Debe introducir una dirección corres", Toast.LENGTH_SHORT).show();
	            	Toast.makeText(NewEventActivity.this, "No hemos encontrado su dirección", Toast.LENGTH_LONG).show();
		                
	            }
	            
	        }
	    } //Class GeocoderTask
	    
	
}//NewEventActivity
