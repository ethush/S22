package com.S22.bloqueo;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

class GetImagen extends AsyncTask<String, Void, Bitmap> {

	protected Bitmap doInBackground(String... urls) {
	      String urldisplay = Constants.URL_UPLOADS + urls[0];
	      //Log.e("GET IMAGEN ", urldisplay);
	      Bitmap mIcon11 = null;
	      try {
	        InputStream in = new java.net.URL(urldisplay).openStream();
	        mIcon11 = BitmapFactory.decodeStream(in);
	      } catch (Exception e) {
	          e.printStackTrace();
	      }
	      return mIcon11;
	  }

}