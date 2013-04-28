package com.biljet.app;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

@SuppressLint("SimpleDateFormat")
public class EventViewActivity extends Activity {

	Event currentEvent;
	
	DBConnection connector;			// Tarea en segundo plano para realizar el logueo
	ProgressDialog postProgress;		// ProgressDialog para mostrar el progreso
	boolean connectionAlive;			// Booleano para controlar estado de la conexion

	// Constanstes para bloquear el sensor de movimiento durante los posteos. Si no se bloquea el
	// y el usuario gira el telefono, Android destruye la actividad y la vuelve a crear, por lo que
	// se cancela el logueo y aparecen errores que fuerzan el cierre de la aplicacion
	final int PORTRAIT = 1;
	final int LANDSCAPE = 0;
	final int REVERSE_PORTRAIT = 9;
	final int REVERSE_LANDSCAPE = 8;
	final int SENSOR_ON = 4;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_view);

        // ACTION BAR
     	// **************************************************************************************
        
		currentEvent = getIntent().getParcelableExtra("event");
		
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Evento: "+ currentEvent.getTitle());
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		int auxInt = 0;
		
		ImageView eventImageView = (ImageView)findViewById(R.id.eventView_ImageView_Avatar);
		//eventImage.setImageResource(e.getImage());		
		File imgFile = new File(currentEvent.getImagePath());
		if(imgFile.exists()){
		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		    eventImageView.setImageBitmap(myBitmap);
			}
		
		eventImageView.setScaleType(ImageView.ScaleType.CENTER);
	
		
		Button buttonMultipurpose = (Button)findViewById(R.id.eventView_Button_Multipurpose);
		if (getIntent().getBooleanExtra("OWN?", true)){
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
		else
			if (getIntent().getBooleanExtra("NO_TICKET", true)){
				buttonMultipurpose.setVisibility(View.VISIBLE);
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
			        											if (connectionAlive)
			        												connector.cancel(true);
			        										}

			        						            });
			        						            
			        						        	connector = new DBConnection();
			        									connectionAlive = true;
			        						        	connector.execute(true);
			        							   }
		        							   });
				}
			else
			buttonMultipurpose.setVisibility(View.INVISIBLE);
		
		TextView txtName = (TextView)findViewById(R.id.eventView_TextView_Title);
		txtName.setText(currentEvent.getTitle());
	
		TextView txtEventType = (TextView)findViewById(R.id.eventView_TextView_Category);
		txtEventType.setText(currentEvent.getCategory());
		
		TextView txtSite = (TextView)findViewById(R.id.eventView_TextView_Address);
		txtSite.setText(currentEvent.getAddress());
	
		TextView txtPrice = (TextView)findViewById(R.id.eventView_TextView_Price);
		String auxString2 = "";
		float auxFloat = currentEvent.getPrice();
		auxString2 = auxFloat+" €";
		txtPrice.setText(auxString2);
		
		TextView txtDate = (TextView)findViewById(R.id.eventView_TextView_Date);
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd MMMM, yyyy");
		String auxDate = dateFormatter.format(new Timestamp(currentEvent.getDate()));
		auxDate = auxDate+"";
		txtDate.setText(auxDate);
		
		TextView txtLength = (TextView)findViewById(R.id.eventView_TextView_Duration);
		String auxString = "";
		auxInt = currentEvent.getLength_days();
		if (auxInt > 0)
			auxString = auxInt+" días ";
		auxInt = currentEvent.getLength_hours();
		if (auxInt > 0)
			auxString += auxInt+" h ";
		auxInt = currentEvent.getLength_minutes();
		if (auxInt > 0)
			auxString += auxInt+" min. ";
		if (auxString.equals(""))
			auxString = "Indeterminado";
		txtLength.setText(auxString);
		/*
		TextView txtGuests = (TextView)findViewById(R.id.eventView_TextView_ConfirmedPeople);
		String auxString3 = "";
		auxInt = currentEvent.getConfirmedPeople();
		auxString3 = auxInt+"";
		txtGuests.setText(auxString3);
		 */
		TextView txtCapacity = (TextView)findViewById(R.id.eventView_TextView_Capacity);
		String auxString4 = "";
		auxInt = currentEvent.getCapacity();
		auxString4 = auxInt+"";
		txtCapacity.setText(auxString4);

		TextView txtInfo = (TextView)findViewById(R.id.eventView_TxtInfo);
		txtInfo.setText(currentEvent.getDescription());
	}

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
    
	private String[] prepareUser(){
		
		String path = null;
		String[] params = new String[2];
		try {
			path = new EncryptedData(EventViewActivity.this).decrypt();
			if (path != null){
				File monitorFile = new File(path);
				Scanner s = new Scanner(monitorFile);
				s.nextLine();
				
				params[0] = s.nextLine();
				params[1] = s.nextLine();
				
				return params;
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
	
	private int postToGo(String user, String password){
		
		int statusCode = -1;
        /* Creamos el objeto cliente que realiza la petición al servidor */
        DefaultHttpClient client = new DefaultHttpClient();
        /* Definimos la ruta al servidor. En mi caso, es un servlet. */
        HttpPost post = new HttpPost("http://www.biljetapp.com/api/event/go/"+ currentEvent.get_id());
        
        Log.d("POST","http://www.biljetapp.com/api/event/go/"+ currentEvent.get_id());
        try{
            // Agrego los parámetros a la petición 
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("_id", user );
            jsonObject.put("password", password );
            Log.d("POST","ID: "+user+" PASS: "+password);
            
            // Damos formato al JSON a enviar o el servidor lo rechazará
            StringEntity entity = new StringEntity(jsonObject.toString());
            entity.setContentEncoding(new BasicHeader(HTTP.UTF_8, "application/json"));
            entity.setContentType("application/json");
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

    /*  Se instancia con 3 tipos:
	1º - Tipo de datos de ENTRADA para doInBackground() => Datos de entrada de la tarea en segundo plano 
	2º - Tipo de datos de ENTRADA para onProgressUpdate() y de ENTRADA para publishProgress() => Datos para mostrar el progreso
	3º - Tipo de datos de SALIDA de doInBackground() y de ENTRADA en onPostExecute() => Datos para mostrar el fin de la tarea	
    */
	private class DBConnection extends AsyncTask<Boolean,Integer,Integer> {
		
		@Override
		protected void onPreExecute() {
			//Mostrar dialogo de progreso
			postProgress.show();
			// Bloquear la orientacion durante el logueo para evitar errores
			setRequestedOrientation(getCurrentOrientation(EventViewActivity.this));
		}
		
		@Override
		protected Integer doInBackground(Boolean... params) {

			String[] authentication = prepareUser();
			String user = authentication[1];
			String pass = authentication[0];
			boolean toGo = params[0]; 
			int statusCode = -1;
			
			if (toGo)
				statusCode = postToGo(user,pass);
			
			return statusCode;
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
			connectionAlive = false;
			setRequestedOrientation(SENSOR_ON);
			Toast.makeText(EventViewActivity.this, " ! : Obtener pase descartado por el usuario!", Toast.LENGTH_LONG).show();
		}
		
	}
	
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
