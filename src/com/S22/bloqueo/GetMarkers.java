package com.S22.bloqueo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.os.AsyncTask;

public class GetMarkers extends AsyncTask<String, String, String> {

	@Override
	protected String doInBackground(String... params) {
		/** HttpURLConnection urlconnection: Variable de tipo HttpURLConnection para hacer la petición */
    	HttpURLConnection urlconnection = null;
    	/** InputStream inputStream: Variable para almacenar la respuesta del servidor */
    	InputStream inputStream = null;
    	/** BufferedReader br: Variable para almacenar y tratar la respuesta.*/
		BufferedReader br = null;
		
		String data = null;
		
		try {
			/* Creamos la conexión y se ejecuta la petición */
			URL url = new URL(Constants.URL_REST + Constants.REST_SCRIPT);
			urlconnection = (HttpURLConnection)url.openConnection();
			urlconnection.connect();
						
			inputStream = urlconnection.getInputStream();
			
			/* Se tratan los datos devueltos y se traducen a String. */
			br = new BufferedReader(new InputStreamReader(inputStream));
			
			StringBuffer sb = new StringBuffer();
			String linea = "";
			
			while((linea = br.readLine()) != null) {
				sb.append(linea + "\n");
			}
						
			br.close();
			inputStream.close();
			/* Hecho el tratamiento se almacena en una variable de tipo String. */
			data = sb.toString();
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		finally{ 
			/* Cerramos las conexiones. */	
			urlconnection.disconnect();
		}
    	
		return data;
	}

}
