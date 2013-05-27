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

import javax.crypto.NoSuchPaddingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.biljet.types.EncryptedData;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.IntentAction;

public class QRViewActivity extends Activity {

	ActionBar actionBar;
	ImageView qrCode;
	
	String eventId;
	
	File imgFile;
	String imagePath;
	
	DBConnection connector;
	boolean connectionAlive;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qrview);
		
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("Visor QR");
		actionBar.setHomeAction(new IntentAction(this, IndexActivity.createIntent(this), R.drawable.actionbar_logo));
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		eventId = getIntent().getExtras().getString("EVENT_ID");
		
		connector = new DBConnection();
		connectionAlive = true;
		connector.execute();
	
	} // onCreate()

	private String prepareUserId(){
		
		String[] params = null;
	
		try {
			params = new EncryptedData(QRViewActivity.this).decrypt();
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
		
		return "";
	
	}
	
	private boolean getQR(){
		
	
		// CONEXION CON DB
	   	// **************************************************************************************		
    	InputStream is = null;
    	
    	try {
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet("http://www.biljetapp.com/api/qr?user="+prepareUserId()+"&event="+eventId);
			Log.d("DEBUG","http://www.biljetapp.com/api/qr?user="+prepareUserId()+"&event="+eventId);
			
			HttpResponse response = httpclient.execute(getRequest);
			StatusLine responseStatus = response.getStatusLine();
			int statusCode = responseStatus.getStatusCode();
			Log.d("DEBUG","Status Code: "+statusCode);
			
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				}
			}
		catch(Exception e) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(QRViewActivity.this,"Error al conectar con el servidor!",Toast.LENGTH_SHORT).show(); 
				  }
				});	
			
			return false;
			// En esta captura de excepción podemos ver que hay un problema con la
			// conexión e intentarlo más adelante.
		}
		
		if (connector.isCancelled())
			return false;
					
		String imageName = "";
		
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine());
			String line="0";
			
			while ((line = reader.readLine()) != null) 
				sb.append(line);
			
			is.close();
			imageName = sb.toString();
			Log.d("DEBUG","ImageName:" +imageName);
			}
		catch(Exception e) {
			runOnUiThread(new Runnable() {
				  public void run() {
					  Toast.makeText(QRViewActivity.this,"Error: No se ha podido completar la operacion!",Toast.LENGTH_SHORT).show(); 
				  }
				});	
			return false;
		}
		
		if (connector.isCancelled())
			return false;	
		
		String imageURL = "https://s3-eu-west-1.amazonaws.com/biljet/"+ imageName;
		imagePath = getFilesDir().getAbsolutePath()+"/eventsQR/"+imageName;
		Log.d("DEBUG","ImageURL: https://s3-eu-west-1.amazonaws.com/biljet/"+ imageName);
		Log.d("DEBUG","ImagePath: "+ imagePath);
		File imgFolder = new File (getFilesDir().getAbsolutePath()+"/eventsQR");
		if(!imgFolder.exists())
			imgFolder.mkdir();
		
		imgFile = new File(imagePath);
			
		try {
			// Si la imagen a descargar no esta almacenada en el telefono, la descargamos y guardamos en "data"
			if(!imgFile.exists())
				saveImageFromURL(imageURL,imagePath);
			
		} catch(IOException e2) {
				runOnUiThread(new Runnable() {
					  public void run() {
						  // Notificar error al guardar avatar
						  Toast.makeText(QRViewActivity.this, "Error: No se pudo descargar QR para el evento", Toast.LENGTH_SHORT).show();
						  // Si se ha creado un archivo, borrarlo para que en la proxima conexion se vuelva a descargar
						  File fileToDelete = new File(imagePath);
						  if (fileToDelete.exists())
							  fileToDelete.delete();
					  }
				});

		} // catch
		
		if (connector.isCancelled())
			return false;	
		
		qrCode = (ImageView)findViewById(R.id.qrview_ImageView_QR);
		
		runOnUiThread(new Runnable() {
			  public void run() {
					if(imgFile.exists()){
						Bitmap myBitmap = BitmapFactory.decodeFile(imagePath);
						qrCode.setImageBitmap(myBitmap);
						}
					else
						qrCode.setImageResource(R.drawable.eventdefault);
			  }
		});


		
		return true;
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

	    }
	 
		@Override
		protected Boolean doInBackground(Void... params) {
			return getQR();
		}
		
    	@Override
	    protected void onProgressUpdate(Integer... values) {
	
	    }
    	
	    @Override
	    protected void onPostExecute(Boolean result) {
			actionBar.setProgressBarVisibility(View.INVISIBLE);
			connectionAlive = false;
			
	    }
	 
	    @Override
	    protected void onCancelled() {
	    	actionBar.setProgressBarVisibility(View.INVISIBLE);
	    	connectionAlive = false;
	    	Toast.makeText(getBaseContext(), " ! : Obtención de QR cancelada por el usuario!", Toast.LENGTH_LONG).show();
	    }


    }
	

}
