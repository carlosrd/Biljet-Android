package com.biljet.app;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.biljet.types.User;
import com.markupartist.android.widget.ActionBar;

public class SignUpActivity extends Activity {
	
	private User newUser;
	
	// Objetos del formulario
	private Button buttonCreateUser;
	private Button buttonCancelUser;
	
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
	
	// Variable que controla que ningún campo obligatorio se quede sin rellenar
    private boolean invalidFields = false;
    private boolean invalidPasswords = false;
    private ArrayList<String> fields = new ArrayList<String>();

	// CONSTRUCTOR
    // **************************************************************************************
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		
        // ACTION BAR
		// **************************************************************************************

		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setHomeLogo(R.drawable.actionbar_logo);
		actionBar.setTitle("Crear Cuenta Biljet");

		// BOTON: Crear Usuario
		// **************************************************************************************
		
	    buttonCreateUser = (Button)this.findViewById(R.id.signUp_Button_CreateUser);
	    buttonCreateUser.setOnClickListener(new OnClickListener(){
	        
	    	@Override
	        public void onClick(View v){
					
	        	// Preparar el dialogo de proceso para el inicio de sesion
	            postProgress = new ProgressDialog(SignUpActivity.this);
	            postProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	            postProgress.setMessage("Creando usuario...");
	            postProgress.setCancelable(true);
	            postProgress.setOnCancelListener(new OnCancelListener(){
	            

					@Override
					public void onCancel(DialogInterface dialog) {
						if (connectionAlive)
							connector.cancel(true);
					}

	            });
	            
	            createUser();

	        }
	    	
	    });
		   
		// BOTON: Cancelar Usuario
		// **************************************************************************************
		
	    buttonCancelUser = (Button)this.findViewById(R.id.signUp_Button_CancelUser);
	    buttonCancelUser.setOnClickListener(new OnClickListener(){
	        
	    	@Override
	        public void onClick(View v){
					showCancelUserAlertDialog();
	        }
	    	
	    });
		
	}

	// MOSTRAR DIALOGS: CAMPOS VACIOS + FECHA INVALIDA + CANCELAR EVENTO
	// **************************************************************************************
		
	/**
	* Method alert when user leaves empty fields
	*/
	private void showInvalidFieldsAlertDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Biljet - Error");
		
		String message = "Los campos: \n"; 
		for (int i = 0; i < fields.size(); i++)
			message += "> " + fields.get(i) + "\n";
		message += "están vacíos o contienen datos invalidos. Revise el formulario y reinténtelo de nuevo";
		builder.setMessage(message);
		
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setCancelable(true);
		builder.setPositiveButton("Aceptar",null);
	
		AlertDialog alert = builder.create();
		postProgress.dismiss();
		setRequestedOrientation(SENSOR_ON);
		alert.show();
		invalidFields = false;
	}
	
	/**
	* Method alert when password fields don't match
	*/
	private void showInvalidPasswordAlertDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Biljet - Error");
		
		builder.setMessage("Las contraseñas no coinciden. Reinténtelo de nuevo...");
		
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setCancelable(true);
		builder.setPositiveButton("Aceptar",null);
	
		AlertDialog alert = builder.create();
		postProgress.dismiss();
		setRequestedOrientation(SENSOR_ON);
		alert.show();
		invalidPasswords = false;
	}
	
   /**
    * Method alert when user press Cancel button
    */
	private void showCancelUserAlertDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Biljet");
		builder.setMessage("El registro en curso se descartará. ¿Desea continuar?");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setCancelable(true);
		builder.setPositiveButton("Sí",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						SignUpActivity.this.finish();
						
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
		
	// CREAR USUARIO + RECUPERAR DATOS DESDE EL FORMULARIO
	// **************************************************************************************
		
	private void createUser() { 
		   
		newUser = null;	
		fields.clear();
		
		String username = getUsernameForm();
		String mail = getEmailForm();
		String hash = getPasswordForm();;

		String name = getNameForm();
		String surname = getSurnameForm();
		String facebook = getFacebookForm();
		String twitter = getTwitterForm();
		
		if (invalidFields)
			showInvalidFieldsAlertDialog();
		else if (invalidPasswords)
			showInvalidPasswordAlertDialog();
		else {
			newUser = new User(username,
								   mail,
								   hash,
								   name,
								   surname,
								   facebook,
								   twitter);
			
			connector = new DBConnection();
			connectionAlive = true;
        	connector.execute();
		} // else
   
	} 

	// Campos obligatorios
	// ----------------------------
	private String getUsernameForm(){
	   
		EditText editTxtUsername = (EditText) findViewById(R.id.signUp_EditText_Username);	   	   
		String username = editTxtUsername.getText().toString();
		
		if (username.equals("") || !username.matches("([a-z]|[A-Z]|[0-9])*")){
			invalidFields = true;
			fields.add("Nickname");
		}
		
		return username;
	   
	}
	
	private String getEmailForm(){
	   
		EditText editTxtEmail = (EditText) findViewById(R.id.signUp_EditText_Email);	   
		String mail = editTxtEmail.getText().toString();
		
		if (mail.equals("") || !mail.matches("([a-z]|[A-Z]|[0-9])*@([a-z]|[A-Z]|[0-9])*.([a-z]|[A-Z])*")){
			invalidFields = true;
			fields.add("E-mail");
		}
		
		return mail;
	   
	}
	
	private String getPasswordForm(){
		   
		EditText editTxtPassword = (EditText) findViewById(R.id.signUp_EditText_Password);	   
		String password = editTxtPassword.getText().toString();
		
		EditText editTxtRepeatPassword = (EditText) findViewById(R.id.signUp_EditText_RepeatPassword);	   
		String repeatPassword = editTxtRepeatPassword.getText().toString();	
		
		if (!password.equals(repeatPassword)){
			invalidPasswords = true;
			editTxtPassword.setText("");
			editTxtRepeatPassword.setText("");
		}
		
		return LoginActivity.toMd5(password);
	   
	}
	
	// Campos Opcionales
	// ----------------------------
	private String getNameForm(){
		   
		EditText editTxtName = (EditText) findViewById(R.id.signUp_EditText_Name);	   
		return editTxtName.getText().toString();
	   
	}
	
	private String getSurnameForm(){
		   
		EditText editTxtSurname = (EditText) findViewById(R.id.signUp_EditText_Surname);	   
		return editTxtSurname.getText().toString();
	   
	}

	private String getFacebookForm(){
		   
		EditText editTxtFacebook = (EditText) findViewById(R.id.signUp_EditText_Facebook);	   
		return editTxtFacebook.getText().toString();
	   
	}

	private String getTwitterForm(){
		   
		EditText editTxtTwitter = (EditText) findViewById(R.id.signUp_EditText_Twitter);	   
		return editTxtTwitter.getText().toString();
	   
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
    
    private int postUserToDB() {
	    
 		// CONEXION CON DB
 		// **************************************************************************************

 		int statusCode = -1; // Error desconocido!
 		
         // Creamos el objeto cliente que realiza la petición al servidor 
         DefaultHttpClient client = new DefaultHttpClient();
         // Definimos la ruta al servidor. En mi caso, es un servlet. 
         HttpPost post = new HttpPost("http://www.biljetapp.com/api/user");

 		try {
 			
 			JSONObject jsonObject = new JSONObject();
                   	
 			// CAMPOS JSON REQUERIDOS            	
 			// **********************************************************  
 			
 			jsonObject.put("username", newUser.getUsername() );
 			jsonObject.put("email", newUser.getMail() );
 			jsonObject.put("password", newUser.getHash() );
 		       
             
            // CAMPOS JSON NO REQUERIDOS 
            // **********************************************************

 			if (!newUser.getName().equals(""))
 				jsonObject.put("name", newUser.getName() );
 			
 			if (!newUser.getSurname().equals(""))
 				jsonObject.put("surname", newUser.getSurname() );
 			
 			if (!newUser.getFacebook().equals(""))
 				jsonObject.put("facebook", newUser.getFacebook() );
 			
 			if (!newUser.getTwitter().equals(""))
 				jsonObject.put("twitter", newUser.getTwitter() );			
 			
	        // Damos formato al JSON a enviar o el servidor lo rechazará
	        StringEntity entity = new StringEntity(jsonObject.toString(),"UTF-8");
	        
	        post.setHeader("Content-Type", "application/json");
	        post.setEntity(entity);
	
	        // Ejecuto la petición, y guardo la respuesta 
			HttpResponse response = client.execute(post);
			StatusLine responseStatus = response.getStatusLine();
			statusCode = responseStatus.getStatusCode();
			
			Log.d("POST-USER","Status Code: "+String.valueOf(statusCode));

 		} catch (IOException e) {
 			Log.e("ERROR","Entrada-Salida: "+e.getMessage());
 			return -2; // Error: Excepcion lanzada
 		} catch (JSONException e) {
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
			postProgress.show();
			setRequestedOrientation(getCurrentOrientation(SignUpActivity.this));
		}
		
		@Override
		protected Integer doInBackground(Void... args) {
			return postUserToDB();
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
				case 200: Toast.makeText(SignUpActivity.this, "¡Usuario registrado correctamente! Ya puedes iniciar sesión", Toast.LENGTH_LONG).show();
						  SignUpActivity.this.finish();
						  break;
				case 400: Toast.makeText(SignUpActivity.this, "Error: El usuario ya existe en el sistema Biljet!", Toast.LENGTH_LONG).show();
						  break;
				case -2:  Toast.makeText(SignUpActivity.this, "Error: No se pudo codificar envío. Reintentelo de nuevo...", Toast.LENGTH_LONG).show();
						  break;
				case -1:  Toast.makeText(SignUpActivity.this, "Error: Desconocido! Reintentelo de nuevo...", Toast.LENGTH_LONG).show();
				  		  break;
				default:  Toast.makeText(SignUpActivity.this, "Error: No se pudo contactar con el servidor!", Toast.LENGTH_LONG).show();
						  break;
			}

		}
		
		@Override
		protected void onCancelled() {
			Log.d("Debug","Cancelar posteo");
			postProgress.dismiss();
			connectionAlive = false;
			setRequestedOrientation(SENSOR_ON);
			Toast.makeText(SignUpActivity.this, " ! : Registro cancelado por el usuario", Toast.LENGTH_LONG).show();
		}

	}
	   

}
