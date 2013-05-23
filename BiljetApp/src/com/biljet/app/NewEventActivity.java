package com.biljet.app;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.biljet.adapters.SpinnerAdapter;
import com.biljet.types.Category;
import com.biljet.types.EncryptedData;
import com.biljet.types.Event;
import com.biljet.types.Province;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

@SuppressLint("SimpleDateFormat")
public class NewEventActivity extends Activity {
	
	// ATRIBUTOS
    // **************************************************************************************
	
	// Atributos para los Date y Time Pickers
	private Calendar dateTime;    
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM , yyyy");
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm"); 
    private SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd MMMM yyyy - HH:mm");
    private static final int DIALOG_DATE = 1;
    private static final int DIALOG_TIME = 2;
	protected static final int RESULT_LOAD_IMAGE = 1;	
	
	// Objetos del formulario
    private Button buttonDatePicker; 
    private Button buttonTimePicker;   
    private Button buttonCreateEvent;
    private Button buttonCancelEvent;
    private EditText editTextDate;
    private EditText editTextTime;    
    private Spinner spinnerCategory;
    private Spinner spinnerProvince;
    private Spinner spinnerAddress;
    
    // Variables auxiliares para almacenar datos de los formularios
    private int dayPicked;
    private int monthPicked;
    private int yearPicked;
    private int hourPicked;
    private int minutePicked;
    private String addressPrefix;
    private int province = 1;
    private String category;
    
    // Almacena la ruta de la imagen en el telefono seleccionada por el usuario
    private String imagePath = "";
    private String imageName = "";
    
    // Variable auxiliar para almacenar el evento creado
    private Event newEventCreated;
    
    // Variables para la conexion con la BD
	private DBConnection connector;				// Tarea en segundo plano para realizar el post a la DB
	private ProgressDialog postProgress;		// ProgressDialog para mostrar el progreso
	private boolean connectionAlive;			// Booleano para controlar estado de la conexion
	
	// Constanstes para bloquear el sensor de movimiento durante el posteo. Si no se bloquea el
	// y el usuario gira el telefono, Android destruye la actividad y la vuelve a crear, por lo que
	// se cancela el posteo y aparecen errores que fuerzan el cierre de la aplicacion
	final int PORTRAIT = 1;
	final int LANDSCAPE = 0;
	final int REVERSE_PORTRAIT = 9;
	final int REVERSE_LANDSCAPE = 8;
	final int SENSOR_ON = 4;
	
	// Variable que controla que ningún campo se quede sin rellenar y la fecha/hora sea correcta
    private boolean invalidFields = false;
    private ArrayList<String> fields = new ArrayList<String>();
    private boolean invalidDate = false;

    // Array de imagales(locales) que vamos a utlizar como el logo del nuevo evento que vamos a crear.
    final int [] arrayIMG = {R.drawable.logo_evento,R.drawable.android1,R.drawable.android2,R.drawable.android3};
    
    // Spinner: Array de tipos de eventos.
    final String [] arrayTypeEvents = {"Comedia/Monólogos","Cine","Concierto","Concurso/Torneo","Conferencia","Cultural",
    								   "Deportivo","Excursión","Exposición","Fiesta","Musical","Ocio","Reunión","Teatro/Espectáculo"}; 
    // Spinner: Array prefijo de direccion
    final String[] arrayAddressPrefix = {"C/","Avda.","Plaza","Urb.","Paseo","Otro (Especificar)"};
       
    // CONSTRUCTOR
    // **************************************************************************************
		
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);	        
		
        // ACTION BAR
		// **************************************************************************************

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Crear Nuevo Evento");
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
        
		
        // AJUSTAR FECHAS
		// **************************************************************************************
		
		// Recoger Fecha por Bundle desde Calendario o 0 en otro caso
		long dateSelected = 0;
		if (getIntent().getExtras() != null)
			dateSelected = getIntent().getExtras().getLong("DATE_SELECTED", 0);
		
		// Obtener la fecha actual/hora actuales
		dateTime = Calendar.getInstance();
		
		// Inicializar la fecha a 0
		dateTime.set(Calendar.HOUR, 0);
		dateTime.set(Calendar.MINUTE, 0);
		dateTime.set(Calendar.SECOND, 0);
		
		// Si le pasamos una fecha por Intent (desde el calendario) setear la
		// fecha al valor obtenido
		if (dateSelected != 0)
			dateTime.setTimeInMillis(dateSelected);
			
		// Inicializamos las variables que recogen los datos
		dayPicked = dateTime.get(Calendar.DAY_OF_MONTH);
		monthPicked = dateTime.get(Calendar.MONTH);
		yearPicked = dateTime.get(Calendar.YEAR);
		
		
		// BOTON: Fijar Fecha
		// **************************************************************************************
		
		// Comprobamos si la llamada se hace desde la vista calendario desde un dia
		// concreto para ajusta los valores a los del dia seleccionado en el calenario	
		
		buttonDatePicker = (Button) findViewById(R.id.newEvent_Button_SetDate);	
		editTextDate = (EditText) findViewById(R.id.newEvent_EditText_Date);			
		editTextDate.setText(dateFormatter.format(dateTime.getTime()));	
		
		buttonDatePicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_DATE);
            }
        });        
		
		// BOTON: Fijar Hora
		// **************************************************************************************

		buttonTimePicker = (Button) findViewById(R.id.newEvent_Button_SetTime);
		editTextTime = (EditText) findViewById(R.id.newEvent_EditText_Time);		
		editTextTime.setText(timeFormatter.format(dateTime.getTime()));
		
		buttonTimePicker.setOnClickListener(new View.OnClickListener() {
 
            public void onClick(View v) {
                showDialog(DIALOG_TIME);
            }
        });			
		
		// BOTON: Cambiar imagen
		// **************************************************************************************

		Button buttonChangeImage = (Button) findViewById(R.id.newEvent_Button_OpenGallery);
		buttonChangeImage.setOnClickListener(new View.OnClickListener() {             
            public void onClick(View arg0) {
                 
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                 
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }
        });  //Listener buttonChangeImage  

		// BOTON: Crear Evento
		// **************************************************************************************
		
	    buttonCreateEvent = (Button)this.findViewById(R.id.newEvent_Button_CreateEvent);
	    buttonCreateEvent.setOnClickListener(new OnClickListener(){
	        
	    	@Override
	        public void onClick(View v){
					
	        	// Preparar el dialogo de proceso para el inicio de sesion
	            postProgress = new ProgressDialog(NewEventActivity.this);
	            postProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            postProgress.setMessage("Creando evento...");
	            postProgress.setCancelable(true);
	            postProgress.setOnCancelListener(new OnCancelListener(){
	            

					@Override
					public void onCancel(DialogInterface dialog) {
						if (connectionAlive)
							connector.cancel(true);
					}

	            });
	            
	            //setRequestedOrientation(getCurrentOrientation(NewEventActivity.this));
	            //postProgress.show();
	            
	            createNewEvent();
	           /* if (newEventCreated != null){
	            	connector = new DBConnection();
					connectionAlive = true;
		        	connector.execute();
            		}
	            else
	            	newEventCreated = null;*/
	        }
	    	
	    });
		
		// BOTON: Cancelar Evento
		// **************************************************************************************
		
	    buttonCancelEvent = (Button)this.findViewById(R.id.newEvent_Button_CancelEvent);
	    buttonCancelEvent.setOnClickListener(new OnClickListener(){
	        
	    	@Override
	        public void onClick(View v){
					showCancelEventAlertDialog();
	        }
	    	
	    });
		
		// SPINNER: CATEGORIA EVENTO (Cine/Cumpleaños/Concierto/Conferencia)
     	// **************************************************************************************

		SpinnerAdapter spinnerCategoryAdapter = new SpinnerAdapter(this, arrayTypeEvents);	
		spinnerCategory = (Spinner)findViewById(R.id.newEvent_Spinner_Category);
		spinnerCategoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_biljet_view);
		spinnerCategory.setAdapter(spinnerCategoryAdapter);		
		
		spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) {
				
				category = arrayTypeEvents[position];
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0){
				// ...
			}
			
	  });	
		
		// SPINNER: PROVINCIA
     	// **************************************************************************************
		
		SpinnerAdapter spinnerProvinceAdapter = new SpinnerAdapter(NewEventActivity.this, new Province().toArrayString());	
		spinnerProvince = (Spinner)findViewById(R.id.newEvent_Spinner_Province);
		spinnerProvinceAdapter.setDropDownViewResource(R.layout.spinner_dropdown_biljet_view);
		spinnerProvince.setAdapter(spinnerProvinceAdapter);		
		
		spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) {
			
				province = position+1;		
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0){
				// ...
			}
			
	  });
			
		
		// SPINNER: PREFIJO DE DIRECCION (C/, Pza, Avda, ... etc)
     	// **************************************************************************************
		
		SpinnerAdapter spinnerAddressAdapter = new SpinnerAdapter(NewEventActivity.this, arrayAddressPrefix);	
		spinnerAddress = (Spinner)findViewById(R.id.newEvent_Spinner_AddressType);
		spinnerAddressAdapter.setDropDownViewResource(R.layout.spinner_dropdown_biljet_view);
		spinnerAddress.setAdapter(spinnerAddressAdapter);		
		
		spinnerAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent,android.view.View v, int position, long id) {
			
				addressPrefix = arrayAddressPrefix[position];
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0){
				// ...
			}
			
	  });
			
		//addListenerChangeImage();
			
	    }  //onCreate

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				showCancelEventAlertDialog();
				break;
		}

		return true;
	}
    
	// GALERIA DE IMAGENES
    // **************************************************************************************

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
            
            ImageView imageView = (ImageView) findViewById(R.id.newEvent_Image);      
            
            if (checkImageSelected(picturePath))
            	imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));	           
            else
            	showInvalidImageAlertDialog();
        }    
    }
	  
	private boolean checkImageSelected(String path){
		
		File image = new File(path);
		
		// Si el tamaño es menor de 300KB
		if (image.length() <= 307200){
			imagePath = path;
			imageName = image.getName();
			return true;
			}
		else
			return false;
		
	}
	
	
	// MOSTRAR DIALOGS: FECHA / HORA
    // **************************************************************************************
		
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
	            	dayPicked = dayOfMonth;
	            	monthPicked = monthOfYear;
	            	yearPicked = year;
	                dateTime.set(year, monthOfYear, dayOfMonth);
	                editTextDate.setText(dateFormatter.format(dateTime.getTime()));
	            }
	        }, dateTime.get(Calendar.YEAR),
	           dateTime.get(Calendar.MONTH),
	           dateTime.get(Calendar.DAY_OF_MONTH));
	        
	        //Si se ha presionado el botón SET TIME, se abre la ventana de diálogo para seleccionar la fecha 
	        case DIALOG_TIME:
	            return new TimePickerDialog(this, new OnTimeSetListener() {	 
	
	                public void onTimeSet(TimePicker view, int hourOfDay,
	                        int minute) {
	                	
	                	hourPicked = hourOfDay;
	                	minutePicked = minute;
	                    dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
	                    dateTime.set(Calendar.MINUTE, minute);
	                    editTextTime.setText(timeFormatter
	                            .format(dateTime.getTime()));
	 
	                }
	            }, dateTime.get(Calendar.HOUR_OF_DAY),
	               dateTime.get(Calendar.MINUTE), true);
	        }
	        return null;
	    }
		
	// MOSTRAR DIALOGS: CAMPOS VACIOS + FECHA INVALIDA + CANCELAR EVENTO
	// **************************************************************************************
		
	/**
	* Method alert when user leaves empty fields
	*/
	private void showInvalidFieldsAlertDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Biljet");
		
		String message = "Los campos: \n"; 
		for (int i = 0; i < fields.size(); i++)
			message += "> " + fields.get(i) + "\n";
		message += "están vacíos o contienen datos invalidos. Revise el formulario y reinténtelo de nuevo";
		builder.setMessage(message);
		
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setCancelable(true);
		builder.setPositiveButton("Aceptar",null);
	
		AlertDialog alert = builder.create();

		alert.show();
		invalidFields = false;
	}
		
   /**
    * Method alert when user leaves empty fields
    */
	private void showInvalidDateAlertDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Biljet - Error");
		builder.setMessage("La fecha/hora introducida no es válida. Recuerda que no puedes crear eventos con menos de 2 horas de antelación.\n\nPor favor, revisa el formulario y reinténtelo de nuevo");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setCancelable(true);
		builder.setPositiveButton("Aceptar",null);

		AlertDialog alert = builder.create();
		
		invalidDate = false;
		alert.show();
	}
		
	/**
	* Method alert when user leaves empty fields
	*/
	private void showInvalidImageAlertDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Biljet - Error");
		builder.setMessage("La imágen seleccionada no es válida: El tamaño excede los 300KB.\n\n¿Deseas seleccionar otra imagen o usar una por defecto?");
	
		
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setCancelable(false);
		builder.setPositiveButton("Elegir otra",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						
					}
				});
		builder.setNegativeButton("Usar por defecto",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						imageName = "eventDefault.png";
					}
				});
		
	
		AlertDialog alert = builder.create();

		alert.show();
		invalidFields = false;
	}
	
   /**
    * Method alert when user press Cancel button
    */
	private void showCancelEventAlertDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Biljet");
		builder.setMessage("El evento "+((EditText) findViewById(R.id.newEvent_EditText_Title)).getText().toString()+" se descartará. ¿Desea continuar?");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setCancelable(true);
		builder.setPositiveButton("Sí",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						NewEventActivity.this.finish();
						
					}
				});
		builder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	 /** 
	  * Method to show a confirm dialog before to post it to DB
	  */
	private void showConfirmEventDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Biljet - Confirmar evento");
		String message = "Confirme los siguientes datos:\n";
		message += " > Título:\n    " + newEventCreated.getTitle() + "\n";
		message += " > Categoría:\n    " + newEventCreated.getCategory() + "\n";
		
		float price = newEventCreated.getPrice();
		
		if (Float.compare(0, price) ==  0)
			message += " > Precio:\n    Gratis\n";
		else
			message += " > Precio:\n  "+ price + "\n";
		
		message += " > Aforo:\n    " + newEventCreated.getCapacity() + "\n";
		message += " > Fecha:\n    " + dateTimeFormatter.format(newEventCreated.getDate()) + "\n";
		
		int days = newEventCreated.getDaysDuration();
		int hours = newEventCreated.getHoursDuration();
		int minutes = newEventCreated.getMinutesDuration();
		
		if (days == 0 && hours == 0 && minutes == 0)
			message += " > Duración:\n    Indeterminado\n";
		else
			message += " > Duración:\n    " + days +" días " + hours + " h " + minutes + " min\n";
		
		if (!newEventCreated.getPlace().equals(""))
			message += " > Lugar:\n    " + newEventCreated.getPlace() +  "\n";
		
		message += " > Dirección:\n    " + newEventCreated.getAddress() + ", " + newEventCreated.getCity() + "\n";
		message += " > Código Postal:\n    " + newEventCreated.getPostalCode() + "\n";
		message += " > Provincia:\n    " + new Province().toString(newEventCreated.getProvince()) + "\n";
		message += " > Descripción:\n    " + newEventCreated.getDescription() + "\n\n";
		message += "Si son correctos, pulse 'Enviar'. Si necesita corregir alguno, pulse 'Cancelar'";
		
		builder.setMessage(message);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setCancelable(false);
		builder.setPositiveButton("Enviar...",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
		            	
						connector = new DBConnection();
						connectionAlive = true;
			        	connector.execute();
						
					}
				});
		builder.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	// CREAR EVENTO + RECUPERAR DATOS DESDE EL FORMULARIO
	// **************************************************************************************
					
	   /**
	* Method to create a NEW EVENT with the parameters provided by form user
	* @return Event object with all data related it
	* 		  null if some field is empty
	*/
	private void createNewEvent() { 
	   
		newEventCreated = null;
		fields.clear();
		
		String title = getTitleForm();

		float price = getPriceForm();
		long date = getDateForm();
		int days_duration = getDaysDurationForm();
		int hours_duration = getHoursDurationForm();
		int minutes_duration = getMinutesDurationForm();
		int capacity = getCapacityForm();
		String description = getDescriptionForm();
		
		String site = getSiteNameForm();
		String address = getAddressForm();
		String city = getCityForm();
		int postalCode = getPostalCodeForm();	
		
		if (imagePath.equals(""))
			imageName ="eventDefault.png";
		
		if (invalidFields)
			showInvalidFieldsAlertDialog();
		else if (invalidDate)
			showInvalidDateAlertDialog();
		else {
			newEventCreated = new Event(title,
										  "",  // creator
										  "",  // id evento
										  description,
										  imagePath, //"eventDefault.png",  // imagePath
										  category,
										  site,
										  address,
										  city,  // city
										  postalCode,	  // postal code		 						   
										  province,	  // province					   
										  0,	  // latitude			 						   
										  0,	  // longitude			 						   
										  date,				 						   
										  price,				 						   
										  capacity,				 						   				 						   
										  days_duration,				 						   
										  hours_duration,				 						   
										  minutes_duration);
			
			showConfirmEventDialog();
		} // else
		
	   //return newEventCreated;
   
	} //CrearNewEvent
    
	   /**
	* Returns the event title from user form
	* @return String
	*/
	private String getTitleForm(){
	   
		EditText editTxtTitle = (EditText) findViewById(R.id.newEvent_EditText_Title);	   
	    String title = editTxtTitle.getText().toString();
	    
		if (title.equals("")){
			invalidFields = true;
			fields.add("Titulo");
		}
		
		return adjustString(title);
	   
	}
	 
	/**
	* Returns the event price from user form
	* @return
	*/
	private float getPriceForm(){
	   
		EditText editPrice = (EditText) findViewById(R.id.newEvent_EditText_Price);
	   
		float price = 0;
		try {
			price = Float.parseFloat(editPrice.getText().toString());
		} catch (Exception e){
			invalidFields = true;
			fields.add("Precio");
		}
	   
		return price;
	}

	/**
	* Returns the event capacity from user form
	* @return
	*/
	private int getCapacityForm(){
		
		EditText editCapacity = (EditText) findViewById(R.id.newEvent_EditText_Capacity);
	   
		int capacity = 0;
		try {
			capacity = Integer.parseInt(editCapacity.getText().toString());
		} catch (Exception e){
			invalidFields = true;
			fields.add("Aforo");
		}
	   
		return capacity;
	}

   /**
    * Returns the event description from user form
    * @return
    */
	private String getDescriptionForm(){
	   
		EditText editDescription = (EditText) findViewById(R.id.newEvent_EditText_Description);
	   
		String info = editDescription.getText().toString();
	
		if (info.equals("")){
			invalidFields = true;
			fields.add("Descripción");
		}
		   
		return info;
	}

	   /**
	* Returns the event duration days from user form
	* @return
	*/
	private int getDaysDurationForm(){	   
	   
		EditText editDays = (EditText) findViewById(R.id.newEvent_EditText_DaysDuration);
	   
		int daysDuration = -1;
		try {
			daysDuration = Integer.parseInt(editDays.getText().toString());
		} catch (Exception e){
			invalidFields = true;
			fields.add("Duración (Días)");
		}
	   
		return daysDuration;
	}
	   
	   /**
	* Returns the event duration hours from user form
	* @return
	*/
	private int getHoursDurationForm(){
	   
		EditText editHours = (EditText) findViewById(R.id.newEvent_EditText_HoursDuration);
	   
		int hoursDuration = -1;
		try {
			hoursDuration = Integer.parseInt(editHours.getText().toString());
		} catch (Exception e){
			invalidFields = true;
			fields.add("Duración (Horas)");
		}
   
		return hoursDuration;
	}
	   
	   /**
	* Returns the event duration minutes from user form
	* @return
	*/
	private int getMinutesDurationForm(){
	   
		EditText editMinutes = (EditText) findViewById(R.id.newEvent_EditText_MinutesDuration);
	   
		int minutesDuration = -1;
		try {
			minutesDuration = Integer.parseInt(editMinutes.getText().toString());
		} catch (Exception e){
			invalidFields = true;
			fields.add("Duración (Minutos)");
		}
	   
	   
		return minutesDuration;
	}
	   
	/**
	* Returns the event Date and Time from user from
	* @return Timestamp in milliseconds
	*/
	private long getDateForm(){
	   
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR, now.get(Calendar.HOUR)+2);
		
		Calendar aux = Calendar.getInstance();
		aux.set(yearPicked, monthPicked, dayPicked, hourPicked, minutePicked,0);
	   
		if (aux.getTimeInMillis() < now.getTimeInMillis())
			invalidDate = true;
	   
		return aux.getTimeInMillis();
	}
	
	/**
	* Returns the facilities name where the event is going to be celebrated (Optional)
	* @return
	*/
	private String getSiteNameForm(){

		EditText editPlace = (EditText) findViewById(R.id.newEvent_EditText_Site);
		String place = editPlace.getText().toString();
		return adjustString(place);
		
	}
	
	/**
	* Returns the event address from user form
	* @return
	*/
	private String getAddressForm(){

		EditText editAdress = (EditText) findViewById(R.id.newEvent_EditText_Address);
		String address = editAdress.getText().toString();
		
		// Comprobamos si el campo esta vacio
		if (!address.equals("")){
			
			address = "";
			// Añadimos prefijo de direccion si es distinto de "Otro"
			if (!addressPrefix.equals("Otro (Especificar)"))
				address = addressPrefix + " ";
			
			address += editAdress.getText().toString();
			
			address = adjustString(address);
		}
		else {
			invalidFields = true;
			fields.add("Dirección");
		}
	   
		return address;
	}
   
	/**
	* Returns the event city from user form
	* @return
	*/
	private String getCityForm(){
	   
		EditText editCity = (EditText) findViewById(R.id.newEvent_EditText_City);
	   
		String city = editCity.getText().toString();
	   
		if (city.equals("")){
			invalidFields = true;
			fields.add("Ciudad");
		}
	   
		return adjustString(city);
	}
	 
	/**
	* Returns the event postal code from user form
	* @return
	*/
	private int getPostalCodeForm(){
	   
		EditText editPostalCode = (EditText) findViewById(R.id.newEvent_EditText_PostalCode);
		int postalCode = -1;
		try {
			postalCode = Integer.parseInt(editPostalCode.getText().toString());
		} catch (Exception e){
			invalidFields = true;
			fields.add("Código Postal");
		}

		return postalCode;
	}
		      	   
	
	private String adjustString(String s){
		
		s = s.replace(" De ", " de ");
		s = s.replace(" Del "," del ");
		s = s.replace(" El "," el ");
		s = s.replace(" La "," la ");
		s = s.replace(" Los "," los ");
		s = s.replace(" Las "," las ");
		s = s.replace(" Y ", " y ");
		
		return s;
		
	}
	
	// AUXILIARES CONEXION DB
	// **************************************************************************************
		   
    private int getCurrentOrientation(Context context){
    	final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
        	case Surface.ROTATION_0:
        		Log.d("Orientation","Portrait");
        		return PORTRAIT;
            case Surface.ROTATION_90:
            	Log.d("Orientation","Landscape");
                return LANDSCAPE;
            case Surface.ROTATION_180:
            	Log.d("Orientation","Reverse Portrait");
                return REVERSE_PORTRAIT;
            default:
            	Log.d("Orientation","Reverse Landscpae");
                return REVERSE_LANDSCAPE;
            }
        }  
    
    private String[] prepareAuthentication(){
    		
		try {
			return new EncryptedData(NewEventActivity.this).decrypt();
		} catch (InvalidKeyException e) {
			Log.e("Error","Clave de cifrado no valida");
		} catch (NoSuchAlgorithmException e) {
			Log.e("Error","El algoritmo no existe");
		} catch (NoSuchPaddingException e) {
			Log.e("Error","No hay padding");
		} catch (IOException e) {
			Log.e("Error","Entrada y salida");
		}
		
		return null;
	
	}
    
    private int postEventToDB() {
	    
		// CONEXION CON DB
		// **************************************************************************************

		String[] authentication = prepareAuthentication();
		int statusCode = -3; // Error desconocido!
		
		if (authentication == null)
			return -1;	// Error: No se ha podido obtener la autenticación

        // Creamos el objeto cliente que realiza la petición al servidor 
        DefaultHttpClient client = new DefaultHttpClient();
        // Definimos la ruta al servidor. En mi caso, es un servlet. 
        HttpPost post = new HttpPost("http://www.biljetapp.com/api/event");

		try {
			
			JSONObject jsonObject = new JSONObject();
                  	
			// CAMPOS JSON REQUERIDOS            	
			// **********************************************************  
			jsonObject.put("title", newEventCreated.getTitle() );
        	jsonObject.put("id", authentication[2] );
        	jsonObject.put("creator", authentication[2]);
        	jsonObject.put("password", authentication[3]);
        	jsonObject.put("price", newEventCreated.getPrice() );
        	
            jsonObject.put("address", newEventCreated.getAddress() );
            jsonObject.put("city",newEventCreated.getCity() );
            jsonObject.put("postalCode", newEventCreated.getPostalCode() );
            jsonObject.put("province", String.valueOf(newEventCreated.getProvince()) );
            
            jsonObject.put("capacity", String.valueOf(newEventCreated.getCapacity()) );
        	jsonObject.put("finishAt", newEventCreated.getDate() );
            jsonObject.put("description", newEventCreated.getDescription() );
            jsonObject.put("category", new Category().getValue(newEventCreated.getCategory()) );
        	jsonObject.put("imageName", imageName );
            
            // CAMPOS JSON NO REQUERIDOS 
            // **********************************************************
        	if (!newEventCreated.getPlace().equals(""))
        		jsonObject.put("place", newEventCreated.getPlace());
        	
            jsonObject.put("longitude", newEventCreated.getLongitude());
            jsonObject.put("latitude", newEventCreated.getLatitude());
            jsonObject.put("duration", "Dura un cojon y medio!");
            // Damos formato al JSON a enviar o el servidor lo rechazará
            StringEntity entity = new StringEntity(jsonObject.toString(),"UTF-8");
           
            post.setHeader("Content-Type", "application/json");
            post.setEntity(entity);

            // Ejecuto la petición, y guardo la respuesta 
			HttpResponse response = client.execute(post);
			StatusLine responseStatus = response.getStatusLine();
			statusCode = responseStatus.getStatusCode();
			
			Log.d("POST-EVENT","Status Code: "+String.valueOf(statusCode));

		} catch (IOException e) {
			Log.e("ERROR","Entrada-Salida: "+e.getMessage());
			return -2; // Error: Excepcion lanzada
		} catch (JSONException e) {
			Log.e("ERROR","JSON error "+e.getMessage());
			return -2;
		}
		
		return statusCode;	   
    }  

    private boolean postEventImage(){
    	    	
    	//Bitmap bm = BitmapFactory.decodeFile("/sdcard/DCIM/forest.png");
    	boolean imageUploaded = false;
    	
        try {
        	// Si queremos comprimirla antes, utilizar este codigo
        	// y situar "bab" en el FileBody
        	/*  
        	 * ByteArrayOutputStream bos = new ByteArrayOutputStream();
        	 * bm.compress(CompressFormat.PNG, 90, bos);
        	 * 
        	 * byte[] data = bos.toByteArray();
        	 * ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");
        	 */ 
        	
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost("http://www.biljetapp.com/upload");
            
            //File file = new File(imagePath);
            //FileBody bin = new FileBody(file);

            String contentType = "image/png";
            if (imagePath.endsWith(".jpg") || imagePath.endsWith(".JPG"))
            	contentType = "image/jpeg";
            else if (imagePath.endsWith(".png") || imagePath.endsWith(".PNG"))
            	contentType = "image/png";
            else if (imagePath.endsWith(".gif") || imagePath.endsWith(".GIF"))
            	contentType = "image/gif";
           	
            
            ContentBody cbFile = new FileBody(new File(imagePath),contentType);

            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            //reqEntity.addPart("uploaded", bab);
            reqEntity.addPart("eventImage",cbFile); // bin);

            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            
            if (statusCode == 200)
            	imageUploaded = true;
            else 
            	imageUploaded = false;

            
            //Recoger respuesta
            /*
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();
         
            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            
            Log.d("POST_IMAGE","Response: " + s);*/
        } catch (Exception e) {
        	imageUploaded = false;
            // handle exception here

        }
            
        return imageUploaded;

    }
    
    private void setPosition(){
    	
		String address =  newEventCreated.getAddress() + ", " +
						  newEventCreated.getPostalCode() + " " +
						  newEventCreated.getCity() + " " +
						  new Province().toString(newEventCreated.getProvince());
		
		String siteAddress = null;
		
		if (!newEventCreated.getSiteName().equals(""))
			siteAddress = newEventCreated.getSiteName() + " " + address;
		else
			siteAddress = "";
		
		// Creating an instance of Geocoder class
        Geocoder geocoder = new Geocoder(getBaseContext());
        List<Address> addresses = null;
        
        // En caso de fallo o no resultados, intentamos 3 veces buscar la direccion
        int i = 0;
        boolean noResults = addresses == null || addresses.size() == 0;
        
        while (!siteAddress.equals("") && noResults && i < 3){
            try {
                //Buscamos un máximo de 3 direcciones que coincidan con la dirección de entrada.	
                addresses = geocoder.getFromLocationName(siteAddress, 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("setPostion","Intento A: "+i);
            noResults = addresses == null || addresses.size() == 0;
            i++;
        }
        
        i = 0;
        // Si no encontro resultados con el nombre del lugar + direccion, probamos con solo direccion
        while (noResults && i < 3){
            try {
                //Buscamos un máximo de 3 direcciones que coincidan con la dirección de entrada.	
                addresses = geocoder.getFromLocationName(address, 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("setPostion","Intento B: "+i);
            noResults = addresses == null || addresses.size() == 0;
            i++;
        }
  
        // TODO CLASES: EventView (+ layout) + NUEVA MapsActivity
        
        if (noResults){
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(NewEventActivity.this, " ! : No se ha encontrado la dirección especificada para situarla en el mapa", Toast.LENGTH_LONG).show();
				  }
			});   
			
            newEventCreated.setLatitude(-1);
            newEventCreated.setLongitude(-1);
             
        }
        else {
        	//Obtenemos la latitud y longitud de la dirección dada.  
        	Address a = (Address) addresses.get(0);
        	
            newEventCreated.setLatitude(a.getLatitude());
            newEventCreated.setLongitude(a.getLongitude());

        }
      Log.d("setPOsition","Lat: "+newEventCreated.getLatitude()+"   Long: "+newEventCreated.getLongitude());
    }
    
    
	// CONEXION DB
	// **************************************************************************************
		        
    /*  Se instancia con 3 tipos:
	1º - Tipo de datos de ENTRADA para doInBackground() => Datos de entrada de la tarea en segundo plano 
	2º - Tipo de datos de ENTRADA para onProgressUpdate() y de ENTRADA para publishProgress() => Datos para mostrar el progreso
	3º - Tipo de datos de SALIDA de doInBackground() y de ENTRADA en onPostExecute() => Datos para mostrar el fin de la tarea	
    */
	private class DBConnection extends AsyncTask<Void,Integer,Integer> {
		
		@Override
		protected void onPreExecute() {
			postProgress.show();
			setRequestedOrientation(getCurrentOrientation(NewEventActivity.this));
		}
		
		@Override
		protected Integer doInBackground(Void... args) {
			// Obtener la longitud y latitud de la localizacion
			runOnUiThread(new Runnable() {
				  public void run() {
					  postProgress.setMessage("Situando evento..."); 
				  }
				});	
			
			setPosition();
			
			if (connector.isCancelled())
				return -3;
			
			if (!imagePath.equals("")){
				runOnUiThread(new Runnable() {
					  public void run() {
						  postProgress.setMessage("Cargando imágen..."); 
					  }
					});	
				postEventImage();
			}
				
			
			if (connector.isCancelled())
				return -3;
			
			runOnUiThread(new Runnable() {
				  public void run() {
					  postProgress.setMessage("Envíando evento..."); 
				  }
				});	
			
			return postEventToDB();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
		
		}
		
		@Override
		protected void onPostExecute(Integer statusCode) {
			// Dejar de mostrar el dialogo, marcar la conexion como no activa y desbloquear sensor
			postProgress.dismiss();
			connectionAlive = false;
			setRequestedOrientation(SENSOR_ON);
			
			// Devolver resultado
			switch(statusCode){
				case 200: Intent showEvent = new Intent(NewEventActivity.this,EventViewActivity.class);
						  showEvent.putExtra("EVENT", newEventCreated);
						  //showEvent.putExtra("IMAGE_PATH",imagePath);
						  startActivity(showEvent);
						  NewEventActivity.this.finish();
						  break;
				case -2:  Toast.makeText(NewEventActivity.this, "Error: No se pudo codificar envío. Reintentelo de nuevo...", Toast.LENGTH_LONG).show();
						  break;
				case -1:  Toast.makeText(NewEventActivity.this, "Error: Fallo la autenticación de usuario. Reintentelo de nuevo...", Toast.LENGTH_LONG).show();
				  		  break;
				case -3:  // Nada (Cancelado por usuario y se notifica en OnCancel())
						  break;
				default:  Toast.makeText(NewEventActivity.this, "Error: No se pudo contactar con el servidor!", Toast.LENGTH_LONG).show();
						  break;
			}

		}
		
		@Override
		protected void onCancelled() {
			Log.d("Debug","Cancelar posteo");
			postProgress.dismiss();
			connectionAlive = false;
			setRequestedOrientation(SENSOR_ON);
			Toast.makeText(NewEventActivity.this, " ! : Envío de evento cancelado por el usuario", Toast.LENGTH_LONG).show();
		}

	}
	   
	
	
	// BOTON MENU TELEFONO
	// **************************************************************************************
		/* 
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.new_event, menu);
	        return true;
	    }
	    
		/**
		 * Actions related to the menu options displayed when you press ··· or Config button on the device
		 
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
		 */

}//NewEventActivity
