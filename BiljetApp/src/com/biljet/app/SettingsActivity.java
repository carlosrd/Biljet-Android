package com.biljet.app;

import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.biljet.adapters.ActivitiesHeader;
import com.biljet.types.Preferences;

public class SettingsActivity extends ActivitiesHeader {

	// ATRIBUTOS
	// **************************************************************************************
	
	// CheckBoxs de las NOTIFICACIONES	
	CheckBox checkBoxReceiveMsg;	
	CheckBox checkBoxRMsgBySound;
	CheckBox checkBoxRMsgByVibration;
	CheckBox checkBoxRMsgByLight;	
	CheckBox checkBoxRMsgByEmail;
	
	// CheckBoxs de para cambiar la contraseña.	
	CheckBox checkBoxChangePwd;
	
	//ESTADO de las notificaciones.	
	boolean stateReceiveMsg;
	boolean stateRMsgBySound, stateRMsgByVibration, stateRMsgByLight;		
	boolean stateRMsgByEmail;
			
	//EditTexts de los DATOS PERSONALES
	
	EditText editTextPhone;
	EditText editTextMobil;
	EditText editTextAddress; 
		
	//EditTexts de DATOS DE LA CUENTA BILJET
	
	EditText editTextCurrentPwd;	//pwd actual
	EditText editTextNewPwd;	//nuevo pwd 
	EditText editTextRepeatPwd;	// repetir pwd
	
	boolean changePwd = false;	
	
	// OnCreate()
  	// **************************************************************************************
	/**
	 * Method which creates the main view of the activity (Activities Header + initialization of the device configuration)
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.activity_settings);
        
        createHeaderView(R.drawable.header_back_button,"Configuración", -1,false);
        
		setBackButton();
        init();	
        addListenerOnChechBox();        
       
    }
	
	private void init() {
		checkBoxReceiveMsg = (CheckBox)findViewById(R.id.settingsView_CheckBoxReceiveMsg);
		checkBoxRMsgBySound = (CheckBox)findViewById(R.id.settingsView_CheckBoxRMsgBySound);
		checkBoxRMsgByVibration = (CheckBox)findViewById(R.id.settingsView_CheckBoxRMsgByVibration);
		checkBoxRMsgByLight = (CheckBox)findViewById(R.id.settingsView_checkBoxRMsgByLight);
		checkBoxRMsgByEmail = (CheckBox)findViewById(R.id.settingsView_CheckBoxRMsgByEmail);
		
		checkBoxChangePwd = (CheckBox)findViewById(R.id.settingsView_CheckBoxChangePassword);
		
		
		editTextPhone = (EditText)findViewById(R.id.settingsView_EditPhone);
		editTextMobil = (EditText)findViewById(R.id.settingsView_EditMobil);
		editTextAddress = (EditText)findViewById(R.id.settingsView_EditAddress);
		
		//Modo de entrada, NUMEROS 
		editTextPhone.setInputType(InputType.TYPE_CLASS_NUMBER);
		editTextMobil.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		editTextCurrentPwd =  (EditText)findViewById(R.id.settingsView_EditCurrentPassword);
		editTextNewPwd =  (EditText)findViewById(R.id.settingsView_EditNewPassword);	
		editTextRepeatPwd =  (EditText)findViewById(R.id.settingsView_EditRepeatNewPassword);
		
		//Leer la configuración del dispositivo
		readConfig();
	}
	
	//Botón Guardar y Volver
	public void savePreferences(View view) {
		
		//NOTIFICACIONES
		Preferences.writeBoolean(this, Preferences.RECEIVEMSG, stateReceiveMsg);
		Preferences.writeBoolean(this, Preferences.BYSOUND, stateRMsgBySound);		
		Preferences.writeBoolean(this, Preferences.BYVIBRATION, stateRMsgByVibration);
		Preferences.writeBoolean(this, Preferences.BYLIGHT, stateRMsgByLight);
		
		Preferences.writeBoolean(this, Preferences.RECEIVEBYEMAIL, stateRMsgByEmail);
		
		//DATOS PERSONALES
		String phoneText = editTextPhone.getText().toString();
		String mobilText = editTextMobil.getText().toString();
		String addressText = editTextAddress.getText().toString();				
		
		if (phoneText != null)
			Preferences.writeString(this, Preferences.PHONE,
					phoneText);
		
		if (mobilText != null)
			Preferences.writeString(this, Preferences.MOBIL,
					mobilText);
		
		if (addressText != null)
			Preferences.writeString(this, Preferences.ADDRESS,
					addressText);
		
		if(changePwd){
			if(chagePassword()){
				//La contraseña se ha cambiado correctamente.
				Toast.makeText(SettingsActivity.this,
			 		 	   "La contraseña se ha cambiado correctamente", Toast.LENGTH_SHORT).show();
			}
			else{
				//La contraseña no se ha cambiado, vuelva a repetir	.
				Toast.makeText(SettingsActivity.this,
			 		 	   "No se pudo cambiar la contraseña", Toast.LENGTH_SHORT).show();
			}
			
		}

		Toast.makeText(SettingsActivity.this,
 		 	   "Preferencias actualizadas correctamente!", Toast.LENGTH_LONG).show();
		
		//Terminamos la activity y regresamos a la actividad principal(Home)
 		finish(); 	   
	}
	
	//Boton Volver
	public void cancelPreferences(View view){
		//Mensaje que indica al usuario que no se han guardado los cambios modificados
		Toast.makeText(SettingsActivity.this,
	 		 	   "Preferencias descartadas!", Toast.LENGTH_LONG).show();
		//Terminación de la activity.
		finish();
	}
			
	/**
	 * Method to enable/disable notifications.
	 */
    //Listener on CHECKBOX
    public void addListenerOnChechBox() {    	 
    	
    	//Oyente de del CheckBox RECIBIR MENSAJES EN ESTE TERMINAL    	
    	checkBoxReceiveMsg.setOnClickListener(new OnClickListener() {
     
    	  public void onClick(View view) {
                    //is chkIos checked?
    		if (((CheckBox) view).isChecked()) {
    			stateReceiveMsg = true;
    			checkBoxRMsgBySound.setEnabled(stateReceiveMsg);
    			checkBoxRMsgByVibration.setEnabled(stateReceiveMsg);
    			checkBoxRMsgByLight.setEnabled(stateReceiveMsg);				
    			
    		}
    		else{
    			stateReceiveMsg = false;
		    	
    			checkBoxRMsgBySound.setChecked(stateReceiveMsg);		    	
    			checkBoxRMsgBySound.setEnabled(stateReceiveMsg);
		    	
    			checkBoxRMsgByVibration.setChecked(stateReceiveMsg);		    	
    			checkBoxRMsgByVibration.setEnabled(stateReceiveMsg);
		    	
    			checkBoxRMsgByLight.setChecked(stateReceiveMsg);		    	
    			checkBoxRMsgByLight.setEnabled(stateReceiveMsg);
		    	
    			stateRMsgBySound = false;
    			stateRMsgByVibration = false; 
    			stateRMsgByLight = false;  
    		}
     
    	  }
    	});
    	
    	
    	//Oyente de del CheckBox MENSAJE CON SONIDO    	
    	checkBoxRMsgBySound.setOnClickListener(new OnClickListener() {
    		 
    		  public void onClick(View v) {
    	                //is checkBoxRMsgBySound checked?
    			if (((CheckBox) v).isChecked()) {
    				stateRMsgBySound = true;
    			}
    			else{
    				stateRMsgBySound = false;
    			}    	 
    		  }
    		});
    
    	//Oyente de del CheckBox MENSAJE CON VIBRACION
    	checkBoxRMsgByVibration.setOnClickListener(new OnClickListener() {
   		 
  		  public void onClick(View v) {
  	                //is chkIos checked?
  			if (((CheckBox) v).isChecked()) {
  				stateRMsgByVibration = true;
  				
  			}
  			else{
  				stateRMsgByVibration = false;  				
  			}
  	 
  		  }
  		});
    	
    	
    	//Oyente de del CheckBox MENSAJE CON LUMINICENCIA.  	
    	checkBoxRMsgByLight.setOnClickListener(new OnClickListener() {
   		 
  		  public void onClick(View v) {
  	                //is checkBoxRMsgByLight checked?
  			if (((CheckBox) v).isChecked()) {
  				
  				stateRMsgByLight = true;
  			}
  			else{
  				stateRMsgByLight = false;
  			}  	 
  		  }
  		});
    	
    	//Oyente de del CheckBox MENSAJE POR EMAIL.    	
    	checkBoxRMsgByEmail.setOnClickListener(new OnClickListener() {
      		 
    		  public void onClick(View v) {
    	                //is checkBoxRMsgByLight checked?
    			if (((CheckBox) v).isChecked()) {
    				
    				stateRMsgByEmail = true;
    		
    			}
    			else{
    				stateRMsgByEmail = false;
    		
    			}
    	 
    		  }
    		});
    	
    	//Oyente de del CheckBox CABIAR LA CONTRASEÑA.    	
    	checkBoxChangePwd.setOnClickListener(new OnClickListener() {
      		 
    		  public void onClick(View v) {
    	                //is checkBoxRMsgByLight checked?
    			if (((CheckBox) v).isChecked()) {
    				
    				changePwd = true;
    				findViewById(R.id.settingsView_EditCurrentPassword).setEnabled(true);
    				findViewById(R.id.settingsView_EditNewPassword).setEnabled(true);
    				findViewById(R.id.settingsView_EditRepeatNewPassword).setEnabled(true);
    				
    				Toast.makeText(SettingsActivity.this,
    			 	   "ACTIVADO: Cambiar contraseña", Toast.LENGTH_LONG).show();
    			}
    			else{
    				changePwd = false;    				
    				findViewById(R.id.settingsView_EditCurrentPassword).setEnabled(false);
    				findViewById(R.id.settingsView_EditNewPassword).setEnabled(false);
    				findViewById(R.id.settingsView_EditRepeatNewPassword).setEnabled(false);
    				
    				Toast.makeText(SettingsActivity.this,
    				   "DESACTIVADO: Cambiar contraseña", Toast.LENGTH_LONG).show();
    			}
    	 
    		  }
    		});
  	
     
      }
    
    /**
	 * Method that creates the menu displayed when you press on config button.
	 */      
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
    	
	/**
	 * Method that read the data refer to saved config and visualize them into checkboxs and Edittexts. 
	 */
	private void readConfig() {
		
	//Lee el estado de las NOTIFICACIONES
		
	//Recibir notificaión en la terminal. 
		stateReceiveMsg =  Preferences.readBoolean(this, Preferences.RECEIVEMSG, stateReceiveMsg);
		checkBoxReceiveMsg.setChecked(Preferences.readBoolean(this, Preferences.RECEIVEMSG, stateReceiveMsg));		
		
	//Recibir notificación con sonido.
		stateRMsgBySound =  Preferences.readBoolean(this, Preferences.BYSOUND, stateRMsgBySound);
		checkBoxRMsgBySound.setChecked(Preferences.readBoolean(this, Preferences.BYSOUND, stateRMsgBySound));
		
	//Recibir notificación con vibración. 
		stateRMsgByVibration =  Preferences.readBoolean(this, Preferences.BYVIBRATION, stateRMsgByVibration);
		checkBoxRMsgByVibration.setChecked(Preferences.readBoolean(this, Preferences.BYVIBRATION, stateRMsgByVibration));
		
	//Recibir notificación con lumínicas. 
		stateRMsgByLight =  Preferences.readBoolean(this, Preferences.BYLIGHT, stateRMsgByLight);
		checkBoxRMsgByLight.setChecked(Preferences.readBoolean(this, Preferences.BYLIGHT, stateRMsgByLight));

	//Recibir notificación por email.  	
		stateRMsgByEmail =  Preferences.readBoolean(this, Preferences.RECEIVEBYEMAIL, stateRMsgByEmail);
		checkBoxRMsgByEmail.setChecked(Preferences.readBoolean(this, Preferences.RECEIVEBYEMAIL, stateRMsgByEmail));

	//habilitar/desabilitar notificaciones(disponible/no disponible)
		findViewById(R.id.settingsView_CheckBoxRMsgBySound).setEnabled(stateReceiveMsg);
		findViewById(R.id.settingsView_CheckBoxRMsgByVibration).setEnabled(stateReceiveMsg);
		findViewById(R.id.settingsView_checkBoxRMsgByLight).setEnabled(stateReceiveMsg);
		
	//Datos de personales.	
		editTextPhone.setText(Preferences.readString(this, Preferences.PHONE, null));		
		editTextMobil.setText(Preferences.readString(this, Preferences.MOBIL, null));		
		editTextAddress.setText(Preferences.readString(this, Preferences.ADDRESS, null));		
		
		
	}
	
	/**
	 * Method to change password.
	 * @returns true if the password has been successfully changed otherwise returns false.
	 */
	private boolean chagePassword(){
		
		boolean resp = false;	
		//Contraseña actual del usuario.
		//Este dato se cogerá desde la BBDD para este método
		//String pwdActual = getUserPassword();
		String pwdActual = "IS201213"; 
		
		String pwdText = editTextCurrentPwd.getText().toString();
		String newPwdText = editTextNewPwd.getText().toString();
		String repeatNewPwdText = editTextRepeatPwd.getText().toString();
		
		if (!pwdText.equals("")){
			
			if (pwdText.equals(pwdActual)){
				
				if (newPwdText.equals(repeatNewPwdText) && !newPwdText.equals("")){
					//Acceder a la BBDD para guardar la nueva contraseña.					
					resp = true;
				}
				else{
					Toast.makeText(SettingsActivity.this,
				 		 	   "La nueva contraseña y la contraseña repetida no se corresponden, inténtalo de nuevo" , Toast.LENGTH_SHORT).show();
					resp = false;
				}		
			}
			else{
				Toast.makeText(SettingsActivity.this,
			 		 	   "Su contraseña actual es incorrecta." , Toast.LENGTH_SHORT).show();
				resp = false;				
			}
		}
		else{
			Toast.makeText(SettingsActivity.this,
		 		 	   "Introduzca la contraseña actual y la nueva contraseña" , Toast.LENGTH_SHORT).show();
			resp = false;
		}
		
		return resp;
	}
    
}
