package com.biljet.app;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
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

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.types.Date;
import com.biljet.types.Event;
import com.biljet.types.User;

/** 
 * This class provides methods to create a new event, where the user enters the event data, such as:
 * Name, type, date, time, price, approximate time, logo and details of the event.
 */
public class NewEventActivity extends ActivitiesHeader {
	
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
	       
	    private User userProfile = getUser();
	    
	    //Variable que contrala para que ningún campos se quede sin rellenar
	    private boolean faltanCamposPorRellenar = false; 
	    
	    
	    // OnCreate()
	  	// **************************************************************************************
	 	  
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_new_event);
	        
	
	        createHeaderView(R.drawable.header_back_button,"Nuevo Evento", R.drawable.amigo,false);
			setBackButton();
			
			// BOTON SET DATE: botón para cambiar la fecha
			buttonDatePicker = (Button) findViewById(R.id.newEvent_ButtonSetDate);			
			editTextDate = (EditText) findViewById(R.id.newEvent_EditDate);			
			editTextDate.setText(dateFormatter.format(dateTime.getTime()));	
			
			buttonDatePicker.setText("Seleccionar Fecha");
			buttonDatePicker.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                showDialog(DIALOG_DATE);
	            }
	        });
	 
			
			// BOTON SET DATE: botón para cambiar la fecha
			buttonTimePicker = (Button) findViewById(R.id.newEvent_ButtonSetTime);
			editTextTime = (EditText) findViewById(R.id.newEvent_EditTime);		
			editTextTime.setText(timeFormatter.format(dateTime.getTime()));
			
			buttonTimePicker.setText("Seleccionar Hora");
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
		 
		 /*
		  * PARA ACCEDER A GALERIA DE IMAGENES
	   @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	         
	        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
	            Uri selectedImage = data.getData();
	            String[] filePathColumn = { MediaStore.Images.Media.DATA };
	 
	            Cursor cursor = getContentResolver().query(selectedImage,
	                    filePathColumn, null, null, null);
	            cursor.moveToFirst();
	 
	            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
	            String picturePath = cursor.getString(columnIndex);	            
	            cursor.close();
	            
	            ImageView imageView = (ImageView) findViewById(R.id.nuevoEvento_ImagenEvento);      
	            
	            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePathG));	           
	         
	        }    
	    }
		  */
		 
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.new_event, menu);
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
	    	Intent eventIntent = new Intent(NewEventActivity.this, EventViewActivity.class);    	
	    	Event NewEvent=createNewEvent();    
	
			if(NewEvent!=null){			
				eventIntent.putExtra("event",NewEvent);			
				startActivity(eventIntent);
				finish();
			}
			else{			
				alertRequiredField();
			}				
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
	   /** Method to create a NEW EVENT
	    * 
	    * @return
	    */
	   private Event createNewEvent() {  
		   String nombre = getNameNewEvent();
		   int id = getIdNewEvent();  	 	   
		   int logo = getImageNewEvent();	   
		   String site = getSiteNewEvent();
		   //Date date = getDateNewEvent();	//FIXME Cambiar el formato de fecha de la clase Event
		   int price = getPriceNewEvent();	    	   
		   int capacity = getCapacityNewEvent();				
		   int length_days = getLengthDaysNewEvent();			
		   int length_hours = getLengthHoursNewEvent();
		   int length_minutes = getLengthMinutesNewEvent();
		   String nameCreator = getNameCreatorNewEvent();			// Nombre del Creator - OBTENER LOS DATOS DEL USUARIO, DESDE PERFIL
		   String eventInfo   = getInfoNewEvent();			 
	
		   if(!faltanCamposPorRellenar){ //Si TODOS los campos del "formualario" estan rellenados, SE CREA EL EVENTO
			   Event NewEvent = new Event(nombre,id ,logo ,typeEvent, site, new Date(20,7,2013,20,30), length_days, length_hours, length_minutes, price, 0, capacity,  nameCreator, eventInfo, 0);
			  //TODO Aqui se debe añadir el NewEvent a la BBDD. 
			   return NewEvent;
		   }
		   else{ //Si falta campos por rellenar, NO SE CREA EL EVENTO
			   return null; 
		   }       
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
		   return editTxtName.getText().toString();
	   }
	   
	   /**
	    * Returns the ID of the event will be the nth event organized by the user.
	    * @return 
	    */
	   private int getIdNewEvent(){   
		   return userProfile.getEventsOrganized().size()+1;	   
	   }   
	   
	   /**
	    * Returns the event logo.
	    * @return
	    */
	   private int getImageNewEvent(){
		 //Usamos imagenes locales que estan en drawable, pero mas adelante habra que coger de la galeria de imgánes del dispositivo móvil
		   return arrayIMG[postImage];  
	   }  
	   
	   /**
	    * Returns the event site.
	    * @return
	    */
	   private String getSiteNewEvent(){
		   EditText editSite = (EditText) findViewById(R.id.newEvent_EditSite);
		   if(editSite.getText().toString().equals("")){
			   faltanCamposPorRellenar = true;
		   }
		   return editSite.getText().toString();	   
	   }
	   
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
		    
		    Event Event1 = new Event("Event al que voy 1",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,0,0, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
			Event Event2 = new Event("Event al que voy 2",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
		
			sampleEvents.add(Event1);
		    sampleEvents.add(Event2);
		    
		    return sampleEvents;
	   }
	   
	   private ArrayList<Event> addEventsOrganized(){
		    ArrayList<Event> sampleEvents = new ArrayList<Event>();
		    
		    Event Event1 = new Event("Event que organizo 1",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,0,0, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
			Event Event2 = new Event("Event que organizo 2",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
		
			sampleEvents.add(Event1);
		    sampleEvents.add(Event2);
		    
		    return sampleEvents;
	   }
	   
	   private ArrayList<Event> addEventsSignup(){
		    ArrayList<Event> sampleEvents = new ArrayList<Event>();
		    
		    Event Event1 = new Event("Event que sigo 1",1 ,R.drawable.jessie_j_evento ,"Concierto", "Madrid", new Date(20,7,2013,20,30),0,0,0, 10, 40, 25, "Empresa2 Conciertos", "Concierto de Jessie J en Valladolid a las 20:30, ¿Lo has apuntado?", 5);
			Event Event2 = new Event("Event que sigo 2",2 ,R.drawable.jessie_j_evento ,"Fiesta", "Sevilla", new Date(15,2,2013,19,45),0,0,0, 5, 10, 20, "Hugo", "Celebro mi cumpleaños en mi casa, vente!", 3);
		
			sampleEvents.add(Event1);
		    sampleEvents.add(Event2);
		    
		    return sampleEvents;
	   }
   
}//NewEventActivity
