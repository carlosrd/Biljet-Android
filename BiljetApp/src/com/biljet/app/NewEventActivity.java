package com.biljet.app;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.biljet.types.EncryptedData;
import com.biljet.types.Event;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class NewEventActivity extends Activity {
	
		// ATRIBUTOS
	    // **************************************************************************************
			  
		private Calendar dateTime = Calendar.getInstance();    
		private SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM dd, yyyy");
	    private SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a"); 
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
	     
	    // Variables auxiliares para fechas y horas seleccionadas
	    private int dayPicked;
	    private int monthPicked;
	    private int yearPicked;
	    private int hourPicked;
	    private int minutePicked;
	    	
	    // Variable auxiliar para almacenar el evento creado
	    private Event newEventOrganized;
	    
	    private String typeEvent;
	    private int postImage = 0;	//?¿
	    
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
	    private boolean emptyFields = false;
	    private boolean invalidDate = false;
	    
	    // Array de imagales(locales) que vamos a utlizar como el logo del nuevo evento que vamos a crear.
	    final int [] arrayIMG = {R.drawable.logo_evento,R.drawable.android1,R.drawable.android2,R.drawable.android3};
	    
	    // Array de tipos de eventos.
	    final String [] arrayTypeEvents = {"Cine", "Cumpleaños", "Concierto", "Conferencia"}; 
	       
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
	        
			// BOTON: Fijar Fecha
			// **************************************************************************************

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

			Button buttonChangeImage = (Button) findViewById(R.id.newEvent_ButtonChangeImage);
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
		            
		            setRequestedOrientation(getCurrentOrientation(NewEventActivity.this));
		            postProgress.show();
		            
		            newEventOrganized = createNewEvent();
		            if (newEventOrganized != null){
		            	connector = new DBConnection();
						connectionAlive = true;
			        	connector.execute();
	            		}
		            else
		            	newEventOrganized = null;
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
			
			// SPINNER: para TIPO DE EVENTO (Cine/Cumpleaños/Concierto/Conferencia)
	     	// **************************************************************************************
			
			ArrayAdapter<String> adaptadorSpinner = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayTypeEvents);	
			spinnerCategory = (Spinner)findViewById(R.id.newEvent_Spinner_Category);
			adaptadorSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerCategory.setAdapter(adaptadorSpinner);		
			
			spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
	    
	// GALERIA DE IMAGENES
    // **************************************************************************************
		
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
	               dateTime.get(Calendar.MINUTE), false);
	        }
	        return null;
	    }
		
	// MOSTRAR DIALOGS: CAMPOS VACIOS + FECHA INVALIDA + CANCELAR EVENTO
	// **************************************************************************************
		
	   /**
	    * Method alert when user leaves empty fields
	    */
		private void showEmptyFieldsAlertDialog(){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setTitle("Biljet");
			builder.setMessage("Existen campos sin rellenar. Por favor complete el formulario antes de enviarlo");
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setCancelable(true);
			builder.setPositiveButton("Aceptar",null);

			AlertDialog alert = builder.create();
			postProgress.dismiss();
			setRequestedOrientation(SENSOR_ON);
			alert.show();
		}
		
	   /**
	    * Method alert when user leaves empty fields
	    */
		private void showInvalidDateAlertDialog(){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setTitle("Biljet");
			builder.setMessage("La fecha/hora no puede ser anterior a la actual. Por favor corrija el dato antes de enviar el formulario");
			builder.setIcon(android.R.drawable.ic_dialog_alert);
			builder.setCancelable(true);
			builder.setPositiveButton("Aceptar",null);

			AlertDialog alert = builder.create();
			postProgress.dismiss();
			
			invalidDate = false;
			setRequestedOrientation(SENSOR_ON);
			alert.show();
		}

	   /**
	    * Method alert when user press Cancel button
	    */
		private void showCancelEventAlertDialog(){
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			
			builder.setTitle("Biljet");
			builder.setMessage("El evento"+((EditText) findViewById(R.id.newEvent_EditText_Title)).getText().toString()+" se descartará. ¿Desea continuar?");
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
		
	// CREAR EVENTO + RECUPERAR DATOS DESDE EL FORMULARIO
	// **************************************************************************************
					
	   /**
	* Method to create a NEW EVENT with the parameters provided by form user
	* @return Event object with all data related it
	* 		  null if some field is empty
	*/
	   private Event createNewEvent() { 
		   
		   Event newEventOrganized = null;
		   
		   String title = getTitleForm();
		   String address = getAddressForm();
		   float price = getPriceForm();
		   long date = getDateForm();
		   int days_duration = getDaysDurationForm();
		   int hours_duration = getHoursDurationForm();
		   int minutes_duration = getMinutesDurationForm();
		   int capacity = getCapacityForm();
		   String description = getDescriptionForm();
		   
		   if (emptyFields)
			   showEmptyFieldsAlertDialog();
		   if (invalidDate)
			   showInvalidDateAlertDialog();
		   else {
			   newEventOrganized = new Event(title,
				 						     "",  // creator
		 						     "",  // id evento
		 						     description,
		 						     "eventDefault.png",  // imagePath
		 						     typeEvent,
		 						     address,
		 						     "",  // city
		 						     0,	  // postal code		 						   
		 						     40,	  // province					   
		 						     0,	  // latitude			 						   
		 						     0,	  // longitude			 						   
				 						     date,				 						   
				 						     price,				 						   
				 						     capacity,				 						   				 						   
				 						     days_duration,				 						   
				 						     hours_duration,				 						   
				 						     minutes_duration);		   							   
		   }
			
		   return newEventOrganized;
	   
		}//CrearNewEvent
	        
	   /**
	* Returns the event title from user form
	* @return String
	*/
	   private String getTitleForm(){
		   
		   EditText editTxtName = (EditText) findViewById(R.id.newEvent_EditText_Title);	   
		   
		   if (editTxtName.getText().toString().equals(""))
			   emptyFields = true;
		   
		   return editTxtName.getText().toString();
		   
	   }
	   
	   /**
	* Returns the event address from user form
	* @return
	*/
	   private String getAddressForm(){
		   
		   EditText editAdress = (EditText) findViewById(R.id.newEvent_EditText_Address);
		   
		   String address = editAdress.getText().toString();
		   
		   if (address.equals(""))
			   emptyFields = true;
		   
		   return address;
	   }
	
	   /**
	* Returns the event price from user form
	* @return
	*/
	   private float getPriceForm(){
		   
		   EditText editPrice = (EditText) findViewById(R.id.newEvent_EditText_Price);
		   
		   String price = editPrice.getText().toString();
		   
		   if (price.equals(""))
			   emptyFields = true;
		   
		   return Float.parseFloat(price);
	   }
	   
	   /**
	* Returns the event Date and Time from user from
	* @return Timestamp in milliseconds
	*/
	   private long getDateForm(){
		   
		   Calendar today = Calendar.getInstance();
		   Calendar aux = Calendar.getInstance();
		   aux.set(yearPicked, monthPicked, dayPicked, hourPicked, minutePicked);
		   
		   if (aux.getTimeInMillis() < today.getTimeInMillis())
			   invalidDate = true;
		   
		   return aux.getTimeInMillis();
	   }
	      
	   /**
	* Returns the event duration days from user form
	* @return
	*/
	   private int getDaysDurationForm(){
		   
		   EditText editDays = (EditText) findViewById(R.id.newEvent_EditText_DaysDuration);
		   
		   String days = editDays.getText().toString();
		   
		   if (days.equals(""))
			   emptyFields = true;
		   
		   return Integer.parseInt(days);
	   }
	   
	   /**
	* Returns the event duration hours from user form
	* @return
	*/
	   private int getHoursDurationForm(){
		   
		   EditText editHours = (EditText) findViewById(R.id.newEvent_EditText_HoursDuration);
		   
		   String hours = editHours.getText().toString();
		   
		   if(hours.equals(""))
			   emptyFields = true;
		   
		   return Integer.parseInt(hours);
	   }
	   
	   /**
	* Returns the event duration minutes from user form
	* @return
	*/
	   private int getMinutesDurationForm(){
		   
		   EditText editMinutes = (EditText) findViewById(R.id.newEvent_EditText_MinutesDuration);
		   
		   String minutes = editMinutes.getText().toString();
		   
		   if(minutes.equals(""))
			   emptyFields = true;
		   
		   return Integer.parseInt(minutes);
	   }
	   
	   /**
	* Returns the event capacity from user form
	* @return
	*/
	   private int getCapacityForm(){
		   
		   EditText editCapacity = (EditText) findViewById(R.id.newEvent_EditText_Capacity);
		   
		   String capacity = editCapacity.getText().toString();
		   
		   if(capacity.equals(""))
			   emptyFields = true;
		   
		   return Integer.parseInt(capacity);
	   }
	   
   /**
    * Returns the event description from user form
    * @return
    */
	   private String getDescriptionForm(){
		   EditText editDescription = (EditText) findViewById(R.id.newEvent_EditText_Description);
		   
			String info = editDescription.getText().toString();
		
			if (info.equals(""))
				emptyFields = true;
			   
			   return info;
	   }

	
	// CONEXION DB
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
    	
		String path = null;
		String[] authentication = new String[2];
		
		try {
			path = new EncryptedData(NewEventActivity.this).decrypt();
			if (path != null){
				File monitorFile = new File(path);
				Scanner s = new Scanner(monitorFile);
				s.nextLine(); // Descartado
				authentication[1] = s.nextLine();
				authentication[0] = s.nextLine();
				return authentication;
				} 

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
		InputStream is = null;
		String[] authentication = prepareAuthentication();
		int statusCode = -3; // Error desconocido!
		
		if (authentication == null)
			return -1;	// Error: No se ha podido obtener la autenticación
		
		HttpParams myParams = new BasicHttpParams();

    	HttpConnectionParams.setSoTimeout(myParams, 10000);
    	HttpConnectionParams.setConnectionTimeout(myParams, 10000); // Timeout


        /* Creamos el objeto cliente que realiza la petición al servidor */
        DefaultHttpClient client = new DefaultHttpClient(myParams);
        /* Definimos la ruta al servidor. En mi caso, es un servlet. */
        HttpPost post = new HttpPost("http://www.biljetapp.com/api/event");

		try {
			
			Log.d("POST","\nEntra en el try1 ");	
			JSONObject jsonObject = new JSONObject();
                  	
			// CAMPOS JSON REQUERIDOS            	
			// **********************************************************  
			jsonObject.put("title", newEventOrganized.getTitle() );
        	jsonObject.put("_id", authentication[0] );
        	jsonObject.put("creator", authentication[0]);
        	jsonObject.put("password", authentication[1]);
        	jsonObject.put("price", newEventOrganized.getPrice() );
            jsonObject.put("province", String.valueOf(newEventOrganized.getProvince()) );
            jsonObject.put("capacity", String.valueOf(newEventOrganized.getCapacity()) );
        	jsonObject.put("finishAt", newEventOrganized.getDate() );
            jsonObject.put("description", newEventOrganized.getDescription() );
            jsonObject.put("category", newEventOrganized.getCategory() );
        	jsonObject.put("imageName", newEventOrganized.getImagePath() );
            
            Log.d("POST","\nObjeto construido ");
            Log.d("POST",jsonObject.toString());
            
            // CAMPOS JSON NO REQUERIDOS
            // **********************************************************
            /*
            HttpPost post = new HttpPost("http://www.biljetapp.com/api/event");
            // post.setEntity(new StringEntity(request.toString(), "utf-8"));
            StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
            entity.setContentType("application/json");
            entity.setContentEncoding("gzip");
            entity.setChunked(true);
            post.setHeader("transfer-encoding","chunked");
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Accept", "application/json");
            post.setEntity(entity);
            
            HttpResponse response = httpclient.execute(post);
			StatusLine responseStatus = response.getStatusLine();
			statusCode = responseStatus.getStatusCode();
            */
            
            // Damos formato al JSON a enviar o el servidor lo rechazará
            StringEntity entity = new StringEntity(jsonObject.toString());
            entity.setChunked(true);
            entity.setContentEncoding(new BasicHeader(HTTP.UTF_8, "application/json"));
            entity.setContentType("application/json");
            post.setHeader("Content-Type", "application/json");
            post.setEntity(entity);
            
            // Ejecuto la petición, y guardo la respuesta 
			HttpResponse response = client.execute(post);
			StatusLine responseStatus = response.getStatusLine();
			statusCode = responseStatus.getStatusCode();
			
			Log.d("Login","Status Code:"+String.valueOf(statusCode));
			Log.d("POST","Status Code: "+String.valueOf(statusCode));
			
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.e("ERROR","No soportado codificacion: "+e.getMessage());
			return -2; // Error: Excepcion lanzada
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("ERROR","Entrada-Salida: "+e.getMessage());
			return -2; // Error: Excepcion lanzada
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("ERROR","JSON error "+e.getMessage());
			return -2;
		}
		
		return statusCode;	   
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
			// Actualizar mensaje en el dialogo de proceso
			postProgress.setTitle("Enviando evento al servidor...");
			postProgress.show();
		}
		
		@Override
		protected Integer doInBackground(Void... args) {
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
						  showEvent.putExtra("NEW_EVENT", newEventOrganized);
						  startActivity(showEvent);
						  NewEventActivity.this.finish();
						  break;
				case -2:  Toast.makeText(NewEventActivity.this, "Error: No se pudo codificar envío", Toast.LENGTH_LONG).show();
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
