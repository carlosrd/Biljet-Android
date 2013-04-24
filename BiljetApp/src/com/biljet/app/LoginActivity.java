package com.biljet.app;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.biljet.types.EncryptedData;

public class LoginActivity extends Activity {

	// Campos de texto del formulario y boton de inicio de sesión
	EditText editTextUsername;
	EditText editTextPassword;
	Button buttonLogin;
	Button buttonSignUp;
	
	LoginConnection connector;			// Tarea en segundo plano para realizar el logueo
	ProgressDialog loginProgress;		// ProgressDialog para mostrar el progreso
	boolean connectionAlive;			// Booleano para controlar estado de la conexion

	// Constanstes para bloquear el sensor de movimiento durante los logueos. Si no se bloquea el
	// y el usuario gira el telefono, Android destruye la actividad y la vuelve a crear, por lo que
	// se cancela el logueo y aparecen errores que fuerzan el cierre de la aplicacion
	final int PORTRAIT = 1;
	final int LANDSCAPE = 0;
	final int REVERSE_PORTRAIT = 9;
	final int REVERSE_LANDSCAPE = 8;
	final int SENSOR_ON = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		connectionAlive = false;
		
		editTextUsername = (EditText)this.findViewById(R.id.login_EditText_User);
	    editTextPassword = (EditText)this.findViewById(R.id.login_EditText_Password);
	    
	    // TODO Quitar!! Solo para ahorrar el escribirlo en debug
	    editTextUsername.setText("android");
	    editTextPassword.setText("android");
	    
	    buttonLogin = (Button)this.findViewById(R.id.login_Button_Login);
	    buttonLogin.setOnClickListener(new OnClickListener(){
	        
	    	@Override
	        public void onClick(View v){
	        	String user = editTextUsername.getText().toString();
	        	String password = editTextPassword.getText().toString();
	        	
	        	// Preparar el dialogo de proceso para el inicio de sesion
	            loginProgress = new ProgressDialog(LoginActivity.this);
	            loginProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            loginProgress.setMessage("Iniciando sesión...");
	            loginProgress.setCancelable(true);
	            loginProgress.setOnCancelListener(new OnCancelListener(){

					@Override
					public void onCancel(DialogInterface dialog) {
						if (connectionAlive)
							connector.cancel(true);
					}

	            });
	            
	        	connector = new LoginConnection();
				connectionAlive = true;
	        	connector.execute(user,password);
	        }
	    	
	    });
	    
	    
	    buttonSignUp = (Button)this.findViewById(R.id.login_Button_SignUp);
	    buttonSignUp.setOnClickListener(new OnClickListener(){
	        @Override
	        public void onClick(View v){
	        	showRegisterConfirmDialog();
	        }
	    });
	    
	} //onCreate()

	private int validateLogin(String username, String pass){
		
		int statusCode = -1;	// -1 el valor por defecto
		
        /* Comprobamos que no venga alguno en blanco. */
        if (!username.equals("") && !pass.equals("")){
            /* Creamos el objeto cliente que realiza la petición al servidor */
            HttpClient client = new DefaultHttpClient();
            /* Definimos la ruta al servidor. En mi caso, es un servlet. */
            HttpPost post = new HttpPost("http://www.biljetapp.com/login");
 
            try{
                /* Defino los parámetros que enviaré. Primero el nombre del parámetro, seguido por el valor.*/
                List<NameValuePair> nvp = new ArrayList<NameValuePair>();
                nvp.add(new BasicNameValuePair("username", username));
                /* Encripto la contraseña en MD5. Definición más abajo */
                nvp.add(new BasicNameValuePair("password", toMd5(pass)));
                /* Agrego los parámetros a la petición */
                post.setEntity(new UrlEncodedFormEntity(nvp));
                /* Ejecuto la petición, y guardo la respuesta */
    			HttpResponse response = client.execute(post);
    			StatusLine responseStatus = response.getStatusLine();
    			statusCode = responseStatus.getStatusCode();
    			
            	}
            catch (Exception e) {
            	Log.e("Exception",e.getMessage());
            	}

            return statusCode;
        	}
        else
        	return -2; 	// Error -2: Alguno de los campos está vacío
    }
	
	private String toMd5(String pass){
        try{
            //Creando Hash MD5
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(pass.getBytes());
            byte messageDigest[] = digest.digest();
 
            //Creando Hex String
            StringBuffer hexString = new StringBuffer();
            for(int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            Log.w("Pass en MD5: ", hexString.toString());
            return hexString.toString();
            
        }catch(NoSuchAlgorithmException ex){
            Log.w("NoSuchAlgorithmException", ex.toString());
            return null;
        }
    }
	
    public int getCurrentOrientation(Context context){
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
    
    /*  Se instancia con 3 tipos:
	1º - Tipo de datos de ENTRADA para doInBackground() => Datos de entrada de la tarea en segundo plano 
	2º - Tipo de datos de ENTRADA para onProgressUpdate() y de ENTRADA para publishProgress() => Datos para mostrar el progreso
	3º - Tipo de datos de SALIDA de doInBackground() y de ENTRADA en onPostExecute() => Datos para mostrar el fin de la tarea	
    */
	private class LoginConnection extends AsyncTask<String,Integer,Integer> {
	
		String user;
		String password;
		String hash;
		
		@Override
		protected void onPreExecute() {
			//Mostrar dialogo de progreso
			loginProgress.show();
			// Bloquear la orientacion durante el logueo para evitar errores
			setRequestedOrientation(getCurrentOrientation(LoginActivity.this));
		}
		
		@Override
		protected Integer doInBackground(String... params) {
			user = params[0];
			password = params[1];
			hash = toMd5(params[1]);
			
			int statusCode = validateLogin(user,password);
			if (statusCode == 200 && !connector.isCancelled()){
				EncryptedData userData = new EncryptedData(LoginActivity.this);
				try {
					userData.encrypt(user,hash);
				} catch (InvalidKeyException e) {
					Log.e("Error","Clave de cifrado no valida");
				} catch (NoSuchAlgorithmException e) {
					Log.e("Error","El algoritmo no existe");
				} catch (NoSuchPaddingException e) {
					Log.e("Error","No such padding: "+e.getMessage());
				} catch (IOException e) {
					Log.e("Error","Entrada y salida");
				}
			}
			
			return statusCode;
		}
		
		@Override
		protected void onProgressUpdate(Integer... values) {
		
		}
		
		@Override
		protected void onPostExecute(Integer statusCode) {
			// Dejar de mostrar el dialogo, marcar la conexion como no activa y desbloquear sensor
			loginProgress.dismiss();
			connectionAlive = false;
			setRequestedOrientation(SENSOR_ON);
			
			// Devolver resultado
			switch(statusCode){
				case 200: Intent showMenu = new Intent(LoginActivity.this,IndexActivity.class);
						  startActivity(showMenu);
						  LoginActivity.this.finish();
						  break;
				case 401: Toast.makeText(LoginActivity.this, "Error: Contraseña incorrecta", Toast.LENGTH_LONG).show();
						  break;
				case -2:  Toast.makeText(LoginActivity.this, " ! : Campos vacíos", Toast.LENGTH_LONG).show();
						  break;
				default:  Toast.makeText(LoginActivity.this, "Error: No se puedo contactar con el servidor!", Toast.LENGTH_LONG).show();
						  break;
			}

		}
		
		@Override
		protected void onCancelled() {
			Log.d("Debug","Cancelar logueo");
			loginProgress.dismiss();
			connectionAlive = false;
			setRequestedOrientation(SENSOR_ON);
			Toast.makeText(LoginActivity.this, " ! : Inicio de sesión cancelado por el usuario", Toast.LENGTH_LONG).show();
		}
		
	}
	
	private void showExitConfirmDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage("¿Seguro que deseas salir?");
		builder.setTitle("Biljet");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setCancelable(false);
		builder.setPositiveButton("Sí",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
						System.exit(RESULT_OK);
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

			switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					if (connectionAlive)
						connector.cancel(true);
					else
						showExitConfirmDialog();
					break;
			}

			return true;
	}
	
	// TEMPORAL!!!
	
	private void showRegisterConfirmDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage("Por el momento, el registro solo se puede llevar a cabo a través de nuestro sitio web. ¿Quieres abrir el navegador y continuar con el proceso?");
		builder.setTitle("Biljet");
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setCancelable(false);
		builder.setPositiveButton("Sí",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
			        	Uri uri = Uri.parse("https://www.biljetapp.com/signup");
		                startActivity(new Intent(Intent.ACTION_VIEW, uri));
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
	
	
	// TECLA SUBMENU / BOTON ···
	// **************************************************************************************
	
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	*/
}
