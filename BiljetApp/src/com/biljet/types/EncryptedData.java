package com.biljet.types;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import android.content.Context;
import android.util.Log;

public class EncryptedData {

	// [0] user
	// [1] hash
	// [2] id
	// [3] last login

	private String dir = "/7a3dd8Z";			// Directorio de almacenamiento de la cookie
	private String store = "/53a1fd.blj";		// Archivo de almacenamiento de la cookie encriptada
	private String monitor = "/6413dM.blj";		// Archivo temporal donde consultar la cookie desencriptada
	
	public EncryptedData(Context c){
		
		// Preparacion de las rutas de acceso
		String aux = dir;
		dir = c.getFilesDir().getAbsolutePath() + aux;

		File imgFolder = new File(dir);
		if(!imgFolder.exists())
			imgFolder.mkdir();
		
		aux = store;
		store = dir + aux;
		
		File storeFile = new File(store);
		try {
			if (!storeFile.exists())
				storeFile.createNewFile();
		} catch (IOException e) {
			Log.e("Error","No se ha podido crear el almacen de autenticacion");
		}
		
		aux = monitor;
		monitor = dir + aux;
		
		File monitorFile = new File(monitor);
		try {
			if (!monitorFile.exists())
				monitorFile.createNewFile();
		} catch (IOException e) {
			Log.e("Error","No se ha podido crear el monitor de autenticacion");
		}
		
	}
	
	public void encrypt(String...params) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException  {

		// LLENAR MONITOR
		// *************************************************************************
		File fileToEncrypt = new File(monitor);
		PrintWriter pw = null;
		try {
			fileToEncrypt.createNewFile();
			pw = new PrintWriter(fileToEncrypt);
			
			for (int i = 0; i < params.length; i++)
				pw.println(params[i]);
			
		} catch (FileNotFoundException e) {
			Log.e("Error","No se pudo crear el archivo de autenticacion previo");
		} catch (IOException e) {
			Log.e("Error","No se pudo escribir el archivo de autenticacion");
		} finally {
			if (pw != null)
				pw.close();
		}
		
		// ENCRIPTADO
		// **************************************************************************
		
		// Here you read the cleartext.
	    FileInputStream fis = new FileInputStream(monitor);
	    // This stream write the encrypted text. This stream will be wrapped by another stream.
	    FileOutputStream fos = new FileOutputStream(store);
	    
	    // Length is 16 byte
	    SecretKeySpec sks = new SecretKeySpec("87a30f30cd209ef2309af20c309d3fb9".getBytes(), "AES");
	    // Create cipher
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.ENCRYPT_MODE, sks);
	    // Wrap the output stream
	    CipherOutputStream cos = new CipherOutputStream(fos, cipher);
	    // Write bytes
	    int b;
	    byte[] d = new byte[8];
	    while((b = fis.read(d)) != -1) {
	        cos.write(d, 0, b);
	    }

	    // Flush and close streams.
	    cos.flush();
	    cos.close();
	    fis.close();
	}
	
	public String[] decrypt() throws  NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {
	    
		// Abrir el fichero encriptador y desencriptarlo
		FileInputStream fis = new FileInputStream(store);

	    FileOutputStream fos = new FileOutputStream(monitor);
	    Log.d("Cypher","Abierto flujos");
	    SecretKeySpec sks = new SecretKeySpec("87a30f30cd209ef2309af20c309d3fb9".getBytes(), "AES");
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, sks);
	    CipherInputStream cis = new CipherInputStream(fis, cipher);
	    int b;
	    byte[] d = new byte[8];
	    while((b = cis.read(d)) != -1) {
	        fos.write(d, 0, b);
	    }
	    Log.d("Cypher","Descifrado concluido");
	    fos.flush();
	    fos.close();
	    cis.close();
	    
	    // Extraer los parametros
	    String[] params = new String[4];
	    
		if (monitor != null){
			File monitorFile = new File(monitor);
			Scanner s = new Scanner(monitorFile);
			
			int i = 0;
			while (s.hasNext()){
				params[i] = s.nextLine();
				i++;
			}
			
			s.close();
			
			return params;
		} 

		return null;
	}

}
