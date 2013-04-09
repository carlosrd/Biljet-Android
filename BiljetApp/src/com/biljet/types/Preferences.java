package com.biljet.types;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 *The class Preferences is a class useful to
 *simplify you the interaction with your app preferences.
 *
 *In fact it has methods that interact with the basical features
 *of SharedPreferences.
 *
 */
public class Preferences{
	
	
	public static final String SHARED_PREF = "SHARED_PREFERENCES";
	public static final int MODE = Context.MODE_PRIVATE;
	
	// NOTIFICACIONES  
	
	public static String RECEIVEMSG ="Recibir mensaje en este terminal";
	public static String BYSOUND ="Recibir mensajes con sonido";
	public static String BYVIBRATION = "Recibir mensajes con vibración";	
	public static String BYLIGHT = "Recibir mensajes con luminicas";
		
	public static String RECEIVEBYEMAIL = "Recibir mensajes por email";
	
	//DATOS PERSONALES
	
	public static String PHONE  = "Teléfono";
	public static String MOBIL  = "Movil";
	public static String ADDRESS  = "Dirección";
	
	
	//TU CUENTA BILJET
	
	public static String PWD  = "Password actual";
	public static String NEWPWD  = "Nuevo password";
	public static String PEATNEWPWD  = "Repetir nuevo password";
	
	
	//ESTADO DE LOS BOTONES
	
	public static String BNSAVE  = "Botón guardar y volver";
	public static String BNBACK  = "Botón volver";
	
	
	public static void writeBoolean(Context context, String key, boolean value) {
		getEditor(context).putBoolean(key, value).commit();
	}
				
	public static boolean readBoolean(Context context, String key, boolean defValue) {
		return getPreferences(context).getBoolean(key, defValue);
	}

	public static void writeInteger(Context context, String key, int value) {
		getEditor(context).putInt(key, value).commit();

	}

	public static int readInteger(Context context, String key, int defValue) {
		return getPreferences(context).getInt(key, defValue);
	}

	public static void writeString(Context context, String key, String value) {
		getEditor(context).putString(key, value).commit();

	}
	
	public static String readString(Context context, String key, String defValue) {
		return getPreferences(context).getString(key, defValue);
	}
	
	public static void writeFloat(Context context, String key, float value) {
		getEditor(context).putFloat(key, value).commit();
	}

	public static float readFloat(Context context, String key, float defValue) {
		return getPreferences(context).getFloat(key, defValue);
	}
	
	public static void writeLong(Context context, String key, long value) {
		getEditor(context).putLong(key, value).commit();
	}

	public static long readLong(Context context, String key, long defValue) {
		return getPreferences(context).getLong(key, defValue);
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(SHARED_PREF, MODE);
	}

	public static Editor getEditor(Context context) {
		return getPreferences(context).edit();
	}

}
