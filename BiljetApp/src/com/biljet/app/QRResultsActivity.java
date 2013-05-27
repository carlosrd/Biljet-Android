package com.biljet.app;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class QRResultsActivity extends Activity {

	// ATRIBUTOS
    // **************************************************************************************
	ActionBar actionBar;
	Button buttonCancel;
	Button buttonReadQR;
	ImageView iconStatus;
	TextView textStatus;
	
	String qrData;
	String qrFormat;
	
	int statusCode;
	DBConnection connector;
	boolean connectionAlive;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrresults);
		
		// ACTION BAR
	    // **************************************************************************************
		
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Lectura de QR");
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		// RECOGER DATOS PRIMERA LECTURA
	    // **************************************************************************************
		
		iconStatus = (ImageView)findViewById(R.id.qrresults_ImageView_StatusIcon);
		textStatus = (TextView)findViewById(R.id.qrresults_TextView_Results);
		//qrData = getIntent().getExtras().getString("SCAN_RESULT");
		//qrFormat = getIntent().getExtras().getString("SCAN_RESULT_FORMAT");
	    
		buttonReadQR = (Button)this.findViewById(R.id.qrresults_Button_Scan);
	    buttonReadQR.setOnClickListener(new OnClickListener(){
	        
	    	@Override
	        public void onClick(View v){
				   // Intent para lanzar el lector de QR
				   Intent intent = new Intent("com.biljet.app.SCAN");
				   intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
				   startActivityForResult(intent, 0);
	        }
	    	
	    });		
		
	    buttonCancel = (Button)this.findViewById(R.id.qrresults_Button_Exit);
	    buttonCancel.setOnClickListener(new OnClickListener(){
	        
	    	@Override
	        public void onClick(View v){
					showCancelReadAlertDialog();
	        }
	    	
	    });
	}

	private void showCancelReadAlertDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setTitle("Biljet");
		builder.setMessage("La vista para lectura de QR se cerrará.\n¿Desea continuar?");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setCancelable(true);
		builder.setPositiveButton("Sí",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						QRResultsActivity.this.finish();
						
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
	
	// RECOGER DATOS QR
	// ***********************************************************************************
	// Metodo para recoger los resultados de leer el QR
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		   if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		    	 
		         qrData = intent.getStringExtra("SCAN_RESULT");
		         qrFormat = intent.getStringExtra("SCAN_RESULT_FORMAT");
		         
		         connector = new DBConnection();
		         connector.execute();
		         
		         // Handle successful scan
	           /*  Toast toast = Toast.makeText(this, "Contenido:" + contents + "       Formato:" + format , Toast.LENGTH_LONG);
	             toast.setGravity(Gravity.TOP, 25, 500);
	             toast.show();*/
		      } else if (resultCode == RESULT_CANCELED) {
		         // Handle cancel
	             Toast toast = Toast.makeText(this, " ! : Escaneo cancelado por el usuario", Toast.LENGTH_LONG);
	             toast.setGravity(Gravity.TOP, 25, 500);
	             toast.show();
		      }
		   }
	}
	
    public int checkQR(){

		// CONEXION CON DB
	   	// **************************************************************************************		
    	int statusCode;
    	
    	try {
			
			HttpClient httpclient = new DefaultHttpClient();
			qrData = qrData.replace(" ", "%20");
			HttpGet getRequest = new HttpGet("http://www.biljetapp.com/api/check-qr?qr=" + qrData);

			HttpResponse response = httpclient.execute(getRequest);
			StatusLine responseStatus = response.getStatusLine();
			statusCode = responseStatus.getStatusCode();
			Log.d("DEBUG","Status Code: "+statusCode);

			}
		catch(Exception e) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(QRResultsActivity.this,"Error al conectar con el servidor!",Toast.LENGTH_SHORT).show(); 
				  }
				});	
			
			return -1;
			// En esta captura de excepción podemos ver que hay un problema con la
			// conexión e intentarlo más adelante.
		}

		return statusCode;
	}
    
    
	// CONEXION CON DB EN SEGUNDO PLANO
   	// **************************************************************************************
	
    /*  Se instancia con 3 tipos:
    		1º - Tipo de datos de ENTRADA para doInBackground() => Datos de entrada de la tarea en segundo plano 
    		2º - Tipo de datos de ENTRADA para onProgressUpdate() y de ENTRADA para publishProgress() => Datos para mostrar el progreso
    		3º - Tipo de datos de SALIDA de doInBackground() y de ENTRADA en onPostExecute() => Datos para mostrar el fin de la tarea	
    */
    private class DBConnection extends AsyncTask<Void,Integer,Integer> {

	    @Override
	    protected void onPreExecute() {
			actionBar.setProgressBarVisibility(View.VISIBLE);
			connectionAlive = true;
			runOnUiThread(new Runnable() {
				  public void run() {
	
						iconStatus.setImageResource(R.drawable.qr_no_connection);
						textStatus.setText("Comprobando código... ");
		
				  }
				});	
	    }
	 
		@Override
		protected Integer doInBackground(Void... params) {
			return checkQR();
		}
		
    	@Override
	    protected void onProgressUpdate(Integer... values) {
	
	    }
    	
	    @Override
	    protected void onPostExecute(Integer result) {
			actionBar.setProgressBarVisibility(View.INVISIBLE);
			statusCode = result;
			runOnUiThread(new Runnable() {
				  public void run() {
						switch (statusCode){
						
						case 200: iconStatus.setImageResource(R.drawable.qr_ok);
								  textStatus.setText("Código QR validado correctamente!");
								  break;
						case 400: iconStatus.setImageResource(R.drawable.qr_wrong);
						  		  textStatus.setText("Código QR erroneo! ");
						  		  break;			  		  
						default: iconStatus.setImageResource(R.drawable.qr_init);
				  		  		  textStatus.setText("No se pudo conectar con el servidor. Reintentelo de nuevo...");
				  		  		  break;
					}
				  }
				});	

			connectionAlive = false;
			
	    }
	 
	    @Override
	    protected void onCancelled() {
	    	actionBar.setProgressBarVisibility(View.INVISIBLE);
	    	connectionAlive = false;
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(getBaseContext(), " ! : Obtención de QR cancelada por el usuario!", Toast.LENGTH_LONG).show();
					
				  }
			});	

				  
		}


    }

}
