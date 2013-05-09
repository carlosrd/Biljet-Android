package com.biljet.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.biljet.types.EncryptedData;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class MyProfileActivity extends Activity {
   
	ActionBar actionBar;
	TextView txtUsername;
	TextView txtEmail;
	
	DBConnection connector;
	boolean connectionAlive = false;
	
	String user,email;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        // ACTION BAR
     	// **************************************************************************************
        
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Mi Perfil");
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);

		
		connectionAlive = true;
		connector = new DBConnection();
		connector.execute();
		
	//name and surname
		txtUsername = (TextView)findViewById(R.id.myProfile_TextView_Username);
		txtEmail = (TextView)findViewById(R.id.myProfile_TextView_Email);//txtNombre.setText(bundleDatos.getString(userProfile.getName() + " " + userProfile.getSurname()));
    }

    private String prepareUser(){
		
		String[] params = null;
		try {
			params = new EncryptedData(MyProfileActivity.this).decrypt();
			return params[2];
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
	
    private int getUserProfileFromDB() {
	    

  		// CONEXION CON DB
  	   	// **************************************************************************************
  		InputStream is = null;
  		String userId = prepareUser();
  		int statusCode = -1;
  		try {
  			Log.d("tag","\nEntra en el try1");
  			HttpClient httpclient = new DefaultHttpClient();
  			//HttpPost httppost = new HttpPost("http://www.biljetapp.com/api/event");
  			//HttpResponse response = httpclient.execute(httppost);
  			HttpGet getRequest = new HttpGet("http://www.biljetapp.com/api/user/"+userId);
  			HttpResponse response = httpclient.execute(getRequest);
  			StatusLine responseStatus = response.getStatusLine();
  			statusCode = responseStatus.getStatusCode();
  			if (statusCode == 200) {
  				HttpEntity entity = response.getEntity();
  				is = entity.getContent();
  				}
  			}
  		catch(Exception e) {
  			runOnUiThread(new Runnable() {
  				  public void run() {
  					  Toast.makeText(MyProfileActivity.this,"Error: No se pudo conectar con el servidor!",Toast.LENGTH_SHORT).show(); 
  				  }
  				});	
  			// En esta captura de excepción podemos ver que hay un problema con la
  			// conexión e intentarlo más adelante.
  		}
  		
  		if (connector.isCancelled())
  			return statusCode;
  					
  		String result = "";
  		
  		try {
  			//BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
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
  					  Toast.makeText(MyProfileActivity.this,"Error: No se pudo obtener la cadena desde el buffer!",Toast.LENGTH_SHORT).show(); 
  				  }
  				});	
  		}
  		
  		if (connector.isCancelled())
  			return statusCode;
  		

  		
  		try {

  			JSONObject jsonObject = new JSONObject(result);

  			user = jsonObject.getString("username");
  			email = jsonObject.getString("email");
  			
  			runOnUiThread(new Runnable() {
				  public void run() {
						txtUsername.setText(user);
			  			txtEmail.setText(email);
			  		
				  }
				});	
  	
  			
  		} catch (JSONException e1) {
  			runOnUiThread(new Runnable() {
  				  public void run() {
  					  Toast.makeText(getBaseContext(), "Error: No se pudieron leer los datos recibidos!", Toast.LENGTH_SHORT).show();
  				  }
  				});	
  		}

  		
  		return statusCode;
  		
      }
      
    /*  Se instancia con 3 tipos:
	1º - Tipo de datos de ENTRADA para doInBackground() => Datos de entrada de la tarea en segundo plano 
	2º - Tipo de datos de ENTRADA para onProgressUpdate() y de ENTRADA para publishProgress() => Datos para mostrar el progreso
	3º - Tipo de datos de SALIDA de doInBackground() y de ENTRADA en onPostExecute() => Datos para mostrar el fin de la tarea	
    */
	private class DBConnection extends AsyncTask<Void,Integer,Integer> {
		
		@Override
		protected void onPreExecute() {
			//Mostrar dialogo de progreso
			actionBar.setProgressBarVisibility(View.VISIBLE);
		}
		
		@Override
		protected Integer doInBackground(Void... params) {
			return getUserProfileFromDB();
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
		
		}
		
		@Override
		protected void onPostExecute(Integer statusCode) {
			// Dejar de mostrar el dialogo, marcar la conexion como no activa y desbloquear sensor
			actionBar.setProgressBarVisibility(View.INVISIBLE);
			connectionAlive = false;
			
			// Devolver resultado
			switch(statusCode){
				case 200: 
						  break;
				default:  Toast.makeText(MyProfileActivity.this, "Error: No se pudo contactar con el servidor!", Toast.LENGTH_LONG).show();
						  break;
			}

		}
		
		@Override
		protected void onCancelled() {
			Log.d("Debug","Cancelar logueo");
			actionBar.setProgressBarVisibility(View.INVISIBLE);
			connectionAlive = false;
			Toast.makeText(MyProfileActivity.this, " ! : Obtener perfil cancelado por el usuario!", Toast.LENGTH_LONG).show();
		}
		
	}
	
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_profile, menu);
        return true;
    }
    
	/**
	 * Actions related to the menu options displayed when you press ··· or Config button on the device
	 *
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
	        case R.id.indexSubmenu_optionSettings:
	        	Intent openSettings = new Intent(MyProfileActivity.this,SettingsActivity.class);
	        	startActivity(openSettings);
	        	break;
	    }
	    return true;
	}

    */

}

