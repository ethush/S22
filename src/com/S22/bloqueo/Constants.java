package com.S22.bloqueo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Constants {
	
	public static boolean IS_LOGGED_TWITTER = false;
	public static boolean IS_LOGGED_FACEBOOK = false;
	
	public static final String URL_REST = "http://www.s22.mx/administracion/rest/";
	public static final String URL_UPLOADS = "http://www.s22.mx/administracion/uploads/";
	public static final String REST_SCRIPT = "rest.php";
	 
	/*
	 *  CONSUMER KEY AND CONSUMER SECRET FOR TWITTER
	 * */
	
	public static final String CONSUMER_KEY = "hNNLJutIvRFWZqtalkFdubWbl";
	public static final String CONSUMER_SECRET = "nLK4jMTc8pxMOzmaXoGuzKgOPzOTygrF7Km7gOeoxDAtWRURPE";
	
	public static final String TAG = "T4JTwitterLogin";
	
	//TWITTER OAUTH====================================================
	public static final String PREFERENCE_NAME = "twitter_oauth";
	public static final String PREF_KEY_SECRET = "oauth_token_secret";
	public static final String PREF_KEY_TOKEN = "oauth_token";
	public static final String TWITTER_CALLBACK_URL = "x-oauthflow-twitter://twitterlogin";
	public static final String IEXTRA_AUTH_URL = "auth_url";
	public static final String IEXTRA_OAUTH_VERIFIER = "oauth_verifier";
	public static final String IEXTRA_OAUTH_TOKEN = "oauth_token";
	//=================================================================
	
	public static boolean existeConexion(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	     if (connectivity != null) {
	        NetworkInfo[] info = connectivity.getAllNetworkInfo();
	        if (info != null) {
	           for (int i = 0; i < info.length; i++) {
	              if (info[i].getState() == NetworkInfo.State.CONNECTED) {
	                 return true;
	              }
	           }
	        }
	     }
	     return false;
	}
}