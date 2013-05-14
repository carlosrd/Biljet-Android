package com.biljet.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.biljet.types.EncryptedData;
import com.biljet.types.Event;
import com.biljet.types.Province;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

@SuppressLint("SimpleDateFormat")
public class EventViewActivity extends FragmentActivity {

	private ActionBar actionBar;
	private Button buttonMultipurpose;	
	
	private Event currentEvent;
	private GoogleMap miniMap = null;
	
	// Variables para las conexiones con la base de datos
	DBPost connectorToGo;				// Tarea en segundo plano para adquirir el pase
	DBGet connectorIsGoing;				// Tarea en segundo plano para saber si el usuario va al evento
	ProgressDialog postProgress;		// ProgressDialog para mostrar el progreso
	boolean postAlive;					// Booleano para controlar estado de la conexion
	boolean getAlive;
	
	// Constanstes para bloquear el sensor de movimiento durante los posteos. Si no se bloquea el
	// y el usuario gira el telefono, Android destruye la actividad y la vuelve a crear, por lo que
	// se cancela el logueo y aparecen errores que fuerzan el cierre de la aplicacion
	final int PORTRAIT = 1;
	final int LANDSCAPE = 0;
	final int REVERSE_PORTRAIT = 9;
	final int REVERSE_LANDSCAPE = 8;
	final int SENSOR_ON = 4;
	
	// CONSTRUCTORA
   	// **************************************************************************************
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_view);
		
        // ACTION BAR
     	// **************************************************************************************
        
		currentEvent = getIntent().getParcelableExtra("EVENT");

		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Evento: "+ currentEvent.getTitle());
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		
		// IMAGEN DEL EVENTO
     	// **************************************************************************************

		int auxInt = 0;
		
		ImageView eventImageView = (ImageView)findViewById(R.id.eventView_ImageView_Avatar);
		//eventImage.setImageResource(e.getImage());		
		File imgFile = new File(currentEvent.getImagePath());
		if(imgFile.exists()){
		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    eventImageView.setImageBitmap(myBitmap);
			}
		
		//eventImageView.setScaleType(ImageView.ScaleType.CENTER);
		
		// BOTON MULTIPROPOSITO (Ir a evento, leer lista, etc..)
     	// **************************************************************************************
		
		connectorIsGoing = new DBGet();
		
		// Si es la vista de un evento para el que no tenemos pase, lo comprobamos
		// para saber si mostrar el boton de "Adquirir Pase"
		if (getIntent().getBooleanExtra("NO_TICKET", false))
			connectorIsGoing.execute(prepareUser());
		
		buttonMultipurpose = (Button)findViewById(R.id.eventView_Button_Multipurpose);
		if (getIntent().getBooleanExtra("OWN?", false)){
			buttonMultipurpose.setVisibility(View.VISIBLE);
			buttonMultipurpose.setText("Leer lista de invitados");
	        buttonMultipurpose.setOnClickListener(new OnClickListener() {
		        							   public void onClick(View arg0) {
		        								   // Intent para lanzar el lector de QR
		        								   Intent intent = new Intent("com.biljet.app.SCAN");
		        								   intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		        								   startActivityForResult(intent, 0);
		        							   }
	        							   });
			}
		else if (getIntent().getBooleanExtra("NO_TICKET", false)){
				buttonMultipurpose.setVisibility(View.INVISIBLE);
				buttonMultipurpose.setText("Adquirir un pase");
		        buttonMultipurpose.setOnClickListener(new OnClickListener() {
			        							   public void onClick(View arg0) {
			        						        	// Preparar el dialogo de proceso para el inicio de sesion
			        						            postProgress = new ProgressDialog(EventViewActivity.this);
			        						            postProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			        						            postProgress.setMessage("Solicitando un pase...");
			        						            postProgress.setCancelable(true);
			        						            postProgress.setOnCancelListener(new OnCancelListener(){

			        										@Override
			        										public void onCancel(DialogInterface dialog) {
			        											if (postAlive)
			        												connectorToGo.cancel(true);
			        										}

			        						            });
			        						            
			        						        	connectorToGo = new DBPost();
			        									postAlive = true;
			        						        	connectorToGo.execute(prepareUser());
			        							   }
		        							   });
				}
		else
			buttonMultipurpose.setVisibility(View.INVISIBLE);
  
		// INFORMACION DEL EVENTO
     	// **************************************************************************************

		// Titulo
     	// *****************

		TextView txtName = (TextView)findViewById(R.id.eventView_TextView_Title);
		txtName.setText(currentEvent.getTitle());
	
		// Categoria 
     	// *****************
		TextView txtEventType = (TextView)findViewById(R.id.eventView_TextView_Category);
		txtEventType.setText(currentEvent.getCategory());
		
		// Nombre de lugar
		// Direccion
		// Codigo Postal + Ciudad
		// Provincia
     	// *****************
		TextView txtAddress = (TextView)findViewById(R.id.eventView_TextView_Address);
		 
		String address = "";
		if (!currentEvent.getSiteName().equals(""))
			address = currentEvent.getSiteName()+"\n";
		address += currentEvent.getAddress()+"\n";
		address += currentEvent.getPostalCode()+" "+currentEvent.getCity()+"\n";
		address += new Province().toString(currentEvent.getProvince());
		txtAddress.setText(address);

		// Precio
     	// *****************
		TextView txtPrice = (TextView)findViewById(R.id.eventView_TextView_Price);
		String auxString2 = "";
		float auxFloat = currentEvent.getPrice();
		
		if (Float.compare(0, auxFloat) ==  0)
			auxString2 = "Gratis!";
		else
			auxString2 = auxFloat+" €";
		
		txtPrice.setText(auxString2);
		
		// Fecha
     	// *****************
		TextView txtDate = (TextView)findViewById(R.id.eventView_TextView_Date);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM, yyyy - HH:mm");
		String auxDate = dateFormatter.format(new Timestamp(currentEvent.getDate()));
		auxDate = auxDate+"";
		txtDate.setText(auxDate);
		
		// Duracion
     	// *****************
		TextView txtLength = (TextView)findViewById(R.id.eventView_TextView_Duration);
		String auxString = "";
		auxInt = currentEvent.getDaysDuration();
		if (auxInt > 0)
			auxString = auxInt+" días ";
		auxInt = currentEvent.getHoursDuration();
		if (auxInt > 0)
			auxString += auxInt+" h ";
		auxInt = currentEvent.getMinutesDuration();
		if (auxInt > 0)
			auxString += auxInt+" min. ";
		if (auxString.equals(""))
			auxString = "Indeterminado";
		txtLength.setText(auxString);
		
		// Aforo
     	// *****************
		TextView txtCapacity = (TextView)findViewById(R.id.eventView_TextView_Capacity);
		String auxString4 = "";
		auxInt = currentEvent.getCapacity();
		auxString4 = auxInt+"";
		txtCapacity.setText(auxString4);

		// Descripcion
     	// *****************
		TextView txtInfo = (TextView)findViewById(R.id.eventView_TextView_Description);
		txtInfo.setText(currentEvent.getDescription());
		
		//INCRUSTAMOS EL MAPA EN LA ACTIVIDAD.
		 	
		double latitude = currentEvent.getLatitude();  //e.getAddress().getLatitude();
		double longitude = currentEvent.getLongitude(); //e.getAddress().getLongitude();
		
		Fragment f = getSupportFragmentManager().findFragmentById(R.id.minimap);
		Button buttonHowToArrive = (Button)findViewById(R.id.eventView_Button_HowToArrive);

		// Si latitud = long = -1, asumimos que no se ha podido localizar en el mapa
		if (Double.compare(latitude, -1.0) == 0 && Double.compare(longitude, -1.0) == 0){
		    
			// Ocultar el mapa
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();  
		    ft.hide(f);  
		    ft.commit(); 
		    
		    // Ocultar boton "Como llegar"
		    buttonHowToArrive.setVisibility(View.INVISIBLE); 
		    
		} else {
			
			// Preparar el mapa
			miniMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.minimap)).getMap();
			CameraUpdate camUpd = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16);
			miniMap.animateCamera(camUpd);
			
			// Vista normal del mapa
			miniMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);				
					
			// Marcamos el a dirección del evento.
			miniMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Aquí estamos"));
			
			// Preparar boton "Como llegar"
			buttonHowToArrive.setVisibility(View.VISIBLE);
			buttonHowToArrive.setOnClickListener(new OnClickListener() {
				   public void onClick(View arg0) {
					   
				    	Intent intent = new Intent(EventViewActivity.this, MapsActivity.class);
				    	
				    	//Pasamos latitud, longitud y nombre a la MapsActivity.
				    	intent.putExtra("LATITUDE", currentEvent.getLatitude());
				    	intent.putExtra("LONGITUDE", currentEvent.getLongitude());
				    	intent.putExtra("TITLE", currentEvent.getTitle());
				    	
						startActivity(intent);	    
				   }
			   });
			
		}
		
		
		

		

	}

	// RECOGER DATOS QR
	// ***********************************************************************************
	// Metodo para recoger los resultados de leer el QR
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		         String contents = intent.getStringExtra("SCAN_RESULT");
		         String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		         // Handle successful scan
	             Toast toast = Toast.makeText(this, "Contenido:" + contents + "       Formato:" + format , Toast.LENGTH_LONG);
	             toast.setGravity(Gravity.TOP, 25, 500);
	             toast.show();
		      } else if (resultCode == RESULT_CANCELED) {
		         // Handle cancel
	             Toast toast = Toast.makeText(this, " ! : Escaneo cancelado", Toast.LENGTH_LONG);
	             toast.setGravity(Gravity.TOP, 25, 500);
	             toast.show();
		      }
		   }
		}
	
	
	// AUXILIARES PARA CONEXION CON DB 
   	// **************************************************************************************
	
	// COMUNES
	
	private String[] prepareUser(){
		
		try {
			return new EncryptedData(EventViewActivity.this).decrypt();
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

	// POST
	
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
    
	private int postToGo(String userId, String password){
		
		int statusCode = -1;
        /* Creamos el objeto cliente que realiza la petición al servidor */
        DefaultHttpClient client = new DefaultHttpClient();
        /* Definimos la ruta al servidor. En mi caso, es un servlet. */
        HttpPost post = new HttpPost("http://www.biljetapp.com/api/event/go/"+ currentEvent.get_id());
        
        try{
            // Agrego los parámetros a la petición 
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", userId );
            jsonObject.put("password", password );
            Log.d("POST","ID: "+ userId +" PASS: "+password);
            
            // Damos formato al JSON a enviar o el servidor lo rechazará
            StringEntity entity = new StringEntity(jsonObject.toString());
            post.setHeader("Content-Type", "application/json");
            post.setEntity(entity);
            
            // Ejecuto la petición, y guardo la respuesta 
			HttpResponse response = client.execute(post);
			StatusLine responseStatus = response.getStatusLine();
			statusCode = responseStatus.getStatusCode();
			
			Log.d("Login","Status Code:"+String.valueOf(statusCode));
        	}
        catch (Exception e) {
        	Log.e("Exception",e.getMessage());
        	}

        return statusCode;
		
	}
	
	// GET IS-GOING
	
	/**
	 * 
	 * @param user
	 * @param password
	 * @return 0 = false, 1 = true, -1 = error
	 */
	private int postIsGoing(String userId, String password){
		
		InputStream is = null;
        /* Creamos el objeto cliente que realiza la petición al servidor */
        DefaultHttpClient client = new DefaultHttpClient();
        /* Definimos la ruta al servidor. En mi caso, es un servlet. */
        HttpPost post = new HttpPost("http://www.biljetapp.com/api/event/is-going/"+ currentEvent.get_id());
        
        try{
            // Agrego los parámetros a la petición 
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", userId );
            jsonObject.put("password", password );
            Log.d("POST","ID: "+ userId +" PASS: "+password);
            
            // Damos formato al JSON a enviar o el servidor lo rechazará
            StringEntity entity = new StringEntity(jsonObject.toString());
            post.setHeader("Content-Type", "application/json");
            post.setEntity(entity);
            
            // Ejecuto la petición, y guardo la respuesta 
			HttpResponse response = client.execute(post);
			StatusLine responseStatus = response.getStatusLine();
			int statusCode = responseStatus.getStatusCode();
			if (statusCode == 200) {
				HttpEntity e = response.getEntity();
				is = e.getContent();
				}
			
				
			if (connectorIsGoing.isCancelled())
				return -1;
						
			String result = "";
			
			// Volcar la respueta a String
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine());
			is.close();
			result = sb.toString();
			
			Log.d("Is-Going",result);
			Log.d("equals",String.valueOf(result.equals("true")+" Long: "+result.length()));
			
			// Uso "contains()" porque siempre se guarda un caracter mas al final ¿Quiza \n del bucle anterior?
			if (result.equals("true"))
				return 1;
			else 
				return 0;

	        }
        catch (Exception e) {
        	Log.e("Exception",e.getMessage());
        	}
        
        

        return -1;
	}
	
	// CONEXION CON DB EN SEGUNDO PLANO
   	// **************************************************************************************
   
	/*  Se instancia con 3 tipos:
	1º - Tipo de datos de ENTRADA para doInBackground() => Datos de entrada de la tarea en segundo plano 
	2º - Tipo de datos de ENTRADA para onProgressUpdate() y de ENTRADA para publishProgress() => Datos para mostrar el progreso
	3º - Tipo de datos de SALIDA de doInBackground() y de ENTRADA en onPostExecute() => Datos para mostrar el fin de la tarea	
    */
	private class DBPost extends AsyncTask<String,Integer,Integer> {
		
		@Override
		protected void onPreExecute() {
			//Mostrar dialogo de progreso
			postProgress.show();
			// Bloquear la orientacion durante el logueo para evitar errores
			setRequestedOrientation(getCurrentOrientation(EventViewActivity.this));
		}
		
		@Override
		protected Integer doInBackground(String... params) {

			return postToGo(params[2],params[3]);
			
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
		
		}
		
		@Override
		protected void onPostExecute(Integer statusCode) {
			// Dejar de mostrar el dialogo, marcar la conexion como no activa y desbloquear sensor
			postProgress.dismiss();
			postAlive = false;
			buttonMultipurpose.setVisibility(View.INVISIBLE);
			setRequestedOrientation(SENSOR_ON);
			
			// Devolver resultado
			switch(statusCode){
				case 200: /*Intent showMenu = new Intent(LoginActivity.this,IndexActivity.class);
						  startActivity(showMenu);
						  LoginActivity.this.finish();*/
						  Toast.makeText(EventViewActivity.this, "Registrado satisfactoriamente en el evento!", Toast.LENGTH_LONG).show();
						  break;
				case 401: Toast.makeText(EventViewActivity.this, "Error: Contraseña incorrecta", Toast.LENGTH_LONG).show();
						  break;
				default:  Toast.makeText(EventViewActivity.this, "Error: No se pudo contactar con el servidor!", Toast.LENGTH_LONG).show();
						  break;
			}

		}
		
		@Override
		protected void onCancelled() {
			Log.d("Debug","Cancelar logueo");
			postProgress.dismiss();
			postAlive = false;
			setRequestedOrientation(SENSOR_ON);
			Toast.makeText(EventViewActivity.this, " ! : Obtener pase descartado por el usuario!", Toast.LENGTH_LONG).show();
		}
		
	}
	
    private class DBGet extends AsyncTask<String,Integer,Integer> {

	    @Override
	    protected void onPreExecute() {
			actionBar.setProgressBarVisibility(View.VISIBLE);
			getAlive = true;
	    }
	 
		@Override
		protected Integer doInBackground(String... params) {
			return postIsGoing(params[2],params[3]);
		}
		
    	@Override
	    protected void onProgressUpdate(Integer... values) {
	
	    }
    	
	    @Override
	    protected void onPostExecute(Integer result) {
			actionBar.setProgressBarVisibility(View.INVISIBLE);
			getAlive = false;
			if (result == 0)
				buttonMultipurpose.setVisibility(View.VISIBLE);
	    }
	 
	    @Override
	    protected void onCancelled() {
	    	actionBar.setProgressBarVisibility(View.INVISIBLE);
	    	getAlive = false;
	    }


    }
	
	
	// BOTON "MENU" DEL DISPOSITIVO
   	// **************************************************************************************
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.event_view, menu);
		return true;
	}

	/**
	 * Actions related to the menu options displayed when you press ··· or Config button on the device
	 *
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(EventViewActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	    }
	    return true;
	}
	*/

}
