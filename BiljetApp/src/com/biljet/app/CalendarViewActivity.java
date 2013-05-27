package com.biljet.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.biljet.adapters.CalendarAdapter;
import com.biljet.types.Category;
import com.biljet.types.EncryptedData;
import com.biljet.types.Event;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.IntentAction;


public class CalendarViewActivity extends Activity {

	// Objetos de la vista
	private ActionBar actionBar;
	private Button buttonNext;
	private Button buttonPrevious;
	private CalendarAdapter adapter;
		
	private Calendar month;				// Fecha / Mes actual del calendario
	private Calendar selectedDate; 		// Fecha seleccionada con ActionBar
	
	// Conjunto de días en los que se celebran eventos
	private HashSet<String> eventDaysToGo;
	private HashSet<String> eventDaysCreated;
	
	// Array con los eventos para pasarlos a la vista detallada de dia y esta a la vista evento
	private ArrayList<Event> eventsToGo;
	private ArrayList<Event> eventsCreated;
	
	// Conexion con la base de datos
	DBConnection connector;
	boolean connectionAlive;
	String imagePath;
	
	// Variables para los metodos ShowDialog Fecha 
    private final int DIALOG_DATE = 1;

	/**
	 * Method which creates the main view of the activity (ActionBar + Month Navigation Panel + Calendar)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_view);
		
		// Inicializar las variables de fechas
		month = Calendar.getInstance();
		month.set(Calendar.HOUR, 0);
		month.set(Calendar.MINUTE, 0);
		month.set(Calendar.SECOND, 0);
		month.set(Calendar.MILLISECOND, 0);
		month.set(Calendar.AM_PM,Calendar.AM);
		
	    selectedDate = Calendar.getInstance();
	    selectedDate.setTimeInMillis(0);
	    
        // ACTION BAR
     	// **************************************************************************************

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Mi Calendario");
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.addAction(new IntentWithFinishAction(this, new Intent(this, MyEventsActivity.class), R.drawable.actionbar_listview_action));
		actionBar.addAction(new GoToDateAction(R.drawable.actionbar_gotodate_action));
		
		
        // BARRA MES + BOTONES ANT/SIG MES
     	// **************************************************************************************
        
	    buttonPrevious  = (Button) findViewById(R.id.calendarView_Button_Previous);
	    buttonPrevious.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Si estamos en el mes de enero => Minimo mes del calendario
				if (month.get(Calendar.MONTH) == month.getActualMinimum(Calendar.MONTH)) 	
					// Actualizar el año al anterior y el mes al ultimo mes (diciembre)
					month.set((month.get(Calendar.YEAR)-1),month.getActualMaximum(Calendar.MONTH),1);
				else // Sino, es otro mes cualquiera
					// Actualizar el mes al anterior
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)-1);
				
				connector = new DBConnection();
				connector.execute();
				//refreshCalendar();
			}
		});
	    
	    TextView title = (TextView) findViewById(R.id.calendarView_Label_Month);
	    title.setText(android.text.format.DateFormat.format("Mmmm yyyy", month));
	    
	    buttonNext  = (Button) findViewById(R.id.calendarView_Button_Next);
	    buttonNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Si estamos en el mes de diciembre => Maximo mes del calendario
				if(month.get(Calendar.MONTH)== month.getActualMaximum(Calendar.MONTH)) 	
					// Actualizar el año al siguiente y el mes al primero (enero)
					month.set((month.get(Calendar.YEAR)+1),month.getActualMinimum(Calendar.MONTH),1);
				else // Sino, es otro mes cualquiera
					// Actualizar el mes al siguiente
					month.set(Calendar.MONTH,month.get(Calendar.MONTH)+1);
				
				connector = new DBConnection();
				connector.execute();
				//refreshCalendar();
				
			}
		});

		// CONEXION CON DB EN SEGUNDO PLANO
	   	// **************************************************************************************
		
		eventDaysToGo = new HashSet<String>();
		eventDaysCreated = new HashSet<String>();
		
		eventsToGo = new ArrayList<Event>();
		eventsCreated = new ArrayList<Event>();
		
		connector = new DBConnection();
		connector.execute();

	    
        // GRID VIEW: CALENDARIO
		// **************************************************************************************
   
	    // Acoplar el adaptador al GridView del calendario
		adapter = new CalendarAdapter(this,month);
		GridView calendarGrid = (GridView)findViewById(R.id.calendarView_gridCalendar);

		// Setear oyentes OnClick
		calendarGrid.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> a, View v, int position, long id) {
						
							//Acciones necesarias al hacer click
							// TODO Pasar a vista de dia
					    	TextView date = (TextView)v.findViewById(R.id.calendarItem_TxtDay);
					    	String choosedDay = date.getText().toString();
					    	
					        if (date instanceof TextView && !date.getText().equals("")) {
					        	
					        	// Rellenar los ArrayList con los eventos del dia pulsados
					        	Calendar c = Calendar.getInstance();
					        	ArrayList<Event> toGo = new ArrayList<Event>();
					        	
					        	for (int i = 0; i < eventsToGo.size(); i++){
					        		Event e = eventsToGo.get(i);
					        		c.setTimeInMillis(e.getDate());
					        		String eventDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
					        		
					        		if (eventDay.equals(choosedDay))
					        			toGo.add(e);
					        	}
					        	
					        	ArrayList<Event> created = new ArrayList<Event>();
					        	
					        	for (int i = 0; i < eventsCreated.size(); i++){
					        		Event e = eventsCreated.get(i);
					        		c.setTimeInMillis(e.getDate());
					        		String eventDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
					        		
					        		if (eventDay.equals(choosedDay))
					        			created.add(e);
					        	}
					        	
					        	Intent intent = new Intent(CalendarViewActivity.this, DayViewActivity.class);
					        	
					        	intent.putParcelableArrayListExtra("TO_GO", toGo);
					        	intent.putParcelableArrayListExtra("CREATED", created);
					        	
					        	if (choosedDay.length() == 1) 
					        		choosedDay = "0"+choosedDay;			        	
					        	
					        	intent.putExtra("DATE_STRING", choosedDay + "/" + android.text.format.DateFormat.format("MM/yyyy", month));
					        	
					        	c = (Calendar) month.clone();
					        	c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(choosedDay));
					        	
					        	intent.putExtra("DATE_MILLIS", c.getTimeInMillis());
					        	
					        	startActivity(intent);
					        	//setResult(RESULT_OK, intent);
					        	//finish();
					        }
						}
		});
		
		calendarGrid.setAdapter(adapter);
		
	}

	@Override
	protected void onRestart() {
	    super.onRestart();  // Always call the superclass method first
	    
	    // Activity being restarted from stopped state    
	
		// CONEXION CON DB EN SEGUNDO PLANO
	   	// **************************************************************************************

		connector = new DBConnection();
		connector.execute();

	}
	
	/**
	 * Refresh the calendar view: It updates month label and fills the calendar with the days in its correct position
	 */
	public void refreshCalendar(){
		
		TextView title  = (TextView) findViewById(R.id.calendarView_Label_Month);
		
		adapter.setSelectedDate(selectedDate);
		adapter.refreshDays(eventDaysToGo,eventDaysCreated);
		adapter.notifyDataSetChanged();				

		title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
	}
	
	// BOTON "ATRAS"
   	// **************************************************************************************
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

			switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (connectionAlive)
						connector.cancel(true);
					else
						finish();
					break;
			}

			return true;
	}
	
	// AUXILIARES PARA CONEXION CON DB 
   	// **************************************************************************************
	
	private void showCalendar(){
		// Volvemos la vista invisible durante la carga
		LinearLayout hostLayout = (LinearLayout)findViewById(R.id.calendarView_HostLayout);
		hostLayout.setVisibility(View.VISIBLE);
		
		buttonNext.setEnabled(true);
		buttonPrevious.setEnabled(true);
		
	}
	
	private void hideCalendar(){
		// Volvemos la vista invisible durante la carga
		LinearLayout hostLayout = (LinearLayout)findViewById(R.id.calendarView_HostLayout);
		hostLayout.setVisibility(View.INVISIBLE);
		
		buttonNext.setEnabled(false);
		buttonPrevious.setEnabled(false);
		
		TextView month = (TextView)findViewById(R.id.calendarView_Label_Month);
		month.setText("Cargando...");
	}
	
	private String getUserId(){
		
    	// Obtener _id de usuario
    	String[] params;
		try {
			params = new EncryptedData(CalendarViewActivity.this).decrypt();
			return params[2];
		} catch (InvalidKeyException e) {
			Log.e("Remember", "Error: Llave de cifrado invalida  | "+ e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			Log.e("Remember", "Error: Algoritmo no valido  | "+ e.getMessage());
		} catch (NoSuchPaddingException e) {
			Log.e("Remember", "Error: Padding  | "+ e.getMessage());
		} catch (IOException e) {
			Log.e("Remember", "Error: Entrada-Salida  | "+ e.getMessage());
		}
		
		return "";
		
	}
	
	private void saveImageFromURL(String imageURL,String destinationPath) throws IOException{
	    	
		URL url = new URL(imageURL);
		File imgFile = new File(destinationPath);
		imgFile.createNewFile();
		InputStream input = null;
		FileOutputStream output = null;
		Log.d("tag3","Inicio try saveImage");
		try {
			
		    input = url.openConnection().getInputStream();
			Log.d("tag3","Conexion hecha!");
		    output = new FileOutputStream(imgFile);

			Log.d("tag3","Abierto output!");
			
		    int read;
		    byte[] data = new byte[1024];
		    Log.d("tag3","Inicio bucle saveImage");
		    while ((read = input.read(data)) != -1)
		        output.write(data, 0, read);
		    
			Log.d("tag3","Termina bucle saveImage");
			
		} finally {
		    if (output != null)
		        output.close();
		    if (input != null)
		        input.close();
		}
    	
    }
	
	private boolean fillEventDays(String option, HashSet<String> eventDays, ArrayList<Event> eventItems){
		
		// CONEXION CON DB
	   	// **************************************************************************************		
    	InputStream is = null;
    	
    	// Setear la fecha inicial como el dia 1 del mes que se va a visualizar
    	Calendar mStart = Calendar.getInstance();
    	mStart.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH), 1,0,0);
    	
    	// Setear la fecha final como el maximo dia del mes que se va a visualizar
    	Calendar mEnd = Calendar.getInstance();
    	mEnd.set(month.get(Calendar.YEAR), month.get(Calendar.MONTH), month.getActualMaximum(Calendar.DAY_OF_MONTH),0,0);
    	
    	// Construir la URL
    	String URL = "http://www.biljetapp.com/api/user/" + getUserId() + "/e/" + option + "?after=" + mStart.getTimeInMillis() + "&before=" + mEnd.getTimeInMillis();
    	
		// CONSULTA EVENTOS PARA ASISTIR
	   	// **************************************************************************************	
    	
    	try {
			HttpClient httpclient = new DefaultHttpClient();
			Log.d("Calendar","GET: "+URL);
			HttpGet getRequest = new HttpGet(URL);
			HttpResponse response = httpclient.execute(getRequest);
			StatusLine responseStatus = response.getStatusLine();
			int statusCode = responseStatus.getStatusCode();
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				}
			}
		catch(Exception e) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(CalendarViewActivity.this,"Error al conectar con el servidor!",Toast.LENGTH_SHORT).show(); 
				  }
				});	
			
			return false;
			// En esta captura de excepción podemos ver que hay un problema con la
			// conexión e intentarlo más adelante.
		}
		
		if (connector.isCancelled())
			return false;
					
		String result = "";
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line="0";
			
			while ((line = reader.readLine()) != null) 
				sb.append(line + "\n");
			
			is.close();
			result = sb.toString();
			
			}
		catch(Exception e) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(CalendarViewActivity.this,"Error: No se ha podido completar la operacion!",Toast.LENGTH_SHORT).show(); 
				  }
				});	
			return false;
		}
		
		if (connector.isCancelled())
			return false;
		
		Calendar date = Calendar.getInstance();
		
		String title, creatorId, _id, description, imageName, category, 
		   address, city, place;
		int postalCode, province, capacity;
		double price, latitude, longitude;
		long timestamp;
		
		try {
			Log.d("Calendar","Entra bucle JSONToGo");
			JSONArray jsonArray = new JSONArray(result);
			JSONObject jsonObject = null;
			for(int i = 0; i < jsonArray.length(); i++){
				jsonObject = jsonArray.getJSONObject(i);
					
				// RECUPERAR FECHA Y AÑADIR AL CONJUNTO
				// ******************************************************************

				date.setTimeInMillis(jsonObject.getLong("finishAt"));
				
				int day = date.get(Calendar.DAY_OF_MONTH);
				if (!eventDays.contains(String.valueOf(day)))
					eventDays.add(String.valueOf(day));
				
				// DATOS JSON REQUERIDOS (NO lanzarán excepción; nunca seran "null")
				// ************************************************************************
				
				title = jsonObject.getString("title");
				creatorId = jsonObject.getString("creator");
				_id = jsonObject.getString("_id");
				description = jsonObject.getString("description");		
				category = jsonObject.getString("category");

				address = jsonObject.getString("address");
				city = jsonObject.getString("city");
				
				// Codigo postal (Numero => Lanza excepcion si no se puede convertir)
				// -------------
				try{
					postalCode = jsonObject.getInt("postalCode");
				} catch (JSONException e){
					postalCode = 0;
				}
				
				// Provincia (Numero => Lanza excepcion si no se puede convertir)
				// -------------
				try{
					province = jsonObject.getInt("province"); 
				} catch (JSONException e){
					province = 0;
				}
				
				capacity = jsonObject.getInt("capacity"); 
				timestamp = jsonObject.getLong("finishAt");
				price = jsonObject.getDouble("price");

				// CACHING IMAGENES EVENTOS
				// ************************************************************************
				imageName = jsonObject.getString("imageName");
				
				String imageURL = "http://www.biljetapp.com/img/" + imageName;
				imagePath = getFilesDir().getAbsolutePath()+"/eventsImage/"+imageName;
				
				File imgFolder = new File (getFilesDir().getAbsolutePath()+"/eventsImage");
				if(!imgFolder.exists())
					imgFolder.mkdir();
				
				File imgFile = new File(imagePath);
					
				try {
					// Si la imagen a descargar no esta almacenada en el telefono, la descargamos y guardamos en "data"
					if(!imgFile.exists())
						saveImageFromURL(imageURL,imagePath);
					
				} catch(IOException e2) {
						runOnUiThread(new Runnable() {
							  public void run() {
								  // Notificar error al guardar avatar
								  //Toast.makeText(UpcomingEventsActivity.this, "Error: No se pudo guardar avatar para el evento:", Toast.LENGTH_SHORT).show();
								  // Si se ha creado un archivo, borrarlo para que en la proxima conexion se vuelva a descargar
								  File fileToDelete = new File(imagePath);
								  if (fileToDelete.exists())
									  fileToDelete.delete();
							  }
						});

				} // catch
									
				// DATOS JSON NO REQUERIDOS (Pueden ser "null" => Lanzarán excepción)
				// ************************************************************************

				// Lugar (String => No lanza excepcion si es null)
				// ---------
				place = jsonObject.getString("place");
				if (place.equals("null"))
					place = "";		
				
				// Longitud
				// -------------
				try{
					latitude = jsonObject.getDouble("latitude");;
				} catch (JSONException e){
					latitude = -1;
				}
				
				// Latitud
				// -------------
				try{
					longitude = jsonObject.getDouble("longitude"); 
				} catch (JSONException e){
					longitude = -1;
				}

				Event event = new Event(title,
									    creatorId,
									    _id,
									    description,
									    imagePath,
									    new Category().getLabel(category),
									    place,	
									    address,
									    city,	
									    postalCode,
									   	province,
									   	longitude,
									   	latitude,
									   	timestamp,
									   	(float)price,
									   	capacity,
									   	0,0,0);
									
				eventItems.add(event);
				
				Log.d("Calendar","\nAñadido el evento "+ title +" al array");
				
				
			} // for
			
		} catch (JSONException e1) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(getBaseContext(), "Error: No se pudieron leer los datos recibidos!", Toast.LENGTH_SHORT).show();
				  }
				});	
			
			return false;
		}
		
		return true;
	}
	
	
    // CONEXION CON DB EN SEGUNDO PLANO
   	// **************************************************************************************
	
    /*  Se instancia con 3 tipos:
    		1º - Tipo de datos de ENTRADA para doInBackground() => Datos de entrada de la tarea en segundo plano 
    		2º - Tipo de datos de ENTRADA para onProgressUpdate() y de ENTRADA para publishProgress() => Datos para mostrar el progreso
    		3º - Tipo de datos de SALIDA de doInBackground() y de ENTRADA en onPostExecute() => Datos para mostrar el fin de la tarea	
    */
    private class DBConnection extends AsyncTask<Void,Integer,Boolean> {

	    @Override
	    protected void onPreExecute() {
			actionBar.setProgressBarVisibility(View.VISIBLE);
			connectionAlive = true;
			actionBar.removeActionAt(1);
			hideCalendar();
	    }
	 
		@Override
		protected Boolean doInBackground(Void... params) {
			
			eventDaysToGo.clear();
			eventDaysCreated.clear();
			eventsToGo.clear();
			eventsCreated.clear();
			
			fillEventDays("going", eventDaysToGo, eventsToGo);
			fillEventDays("created", eventDaysCreated, eventsCreated);
			
			return true;
		}
		
    	@Override
	    protected void onProgressUpdate(Integer... values) {
	
	    }
    	
	    @Override
	    protected void onPostExecute(Boolean result) {
			actionBar.setProgressBarVisibility(View.INVISIBLE);
			actionBar.addAction(new GoToDateAction(R.drawable.actionbar_gotodate_action));
			
			connectionAlive = false;		
			Log.d("Arrays","ToGo: "+eventsToGo.size()+"   Created: " +eventsCreated.size());
			// Actualizar y mostrar calendario
			refreshCalendar();
			showCalendar();
	    }
	 
	    @Override
	    protected void onCancelled() {
	    	actionBar.setProgressBarVisibility(View.INVISIBLE);
			actionBar.addAction(new GoToDateAction(R.drawable.actionbar_gotodate_action));
			
	    	connectionAlive = false;
	    	refreshCalendar();
	    	showCalendar();
	    	Toast.makeText(getBaseContext(), " ! : Actualización de calendario cancelado por el usuario!", Toast.LENGTH_LONG).show();
	    }


    }
	  
	// ACCIONES ADICIONALES PARA ACTIONBAR
 	// **************************************************************************************
 	
    private class IntentWithFinishAction extends AbstractAction {
        private Context mContext;
        private Intent mIntent;

        public IntentWithFinishAction(Context context, Intent intent, int drawable) {
            super(drawable);
            mContext = context;
            mIntent = intent;
        }

        @Override
        public void performAction(View view) {
            try {      
               mContext.startActivity(mIntent); 
               finish();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext,
                        mContext.getText(R.string.actionbar_activity_not_found),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    
    private class GoToDateAction extends AbstractAction {

        public GoToDateAction(int drawable) {
            super(drawable);
        }

		@Override
		public void performAction(View view) {
			showDialog(DIALOG_DATE);
		}
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
 	            	
 	                month.set(year, monthOfYear, 1);
 	                
 	                selectedDate = Calendar.getInstance();
 	                selectedDate.set(year, monthOfYear, dayOfMonth);
 	                //selectedDate.set(Calendar.AM_PM,Calendar.AM);
 	                
 	                connector = new DBConnection();
 					connector.execute();
 					
 	            }
 	        }, month.get(Calendar.YEAR),
 	           month.get(Calendar.MONTH),
 	           month.get(Calendar.DAY_OF_MONTH));
 	        

 	        }
 	        return null;
 	    }


 	
}
