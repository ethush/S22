package com.S22.bloqueo;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;


public class Galeria extends Activity {

	GridView galeria;
	String[] imagenes = null;
	ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_galeria);
		
		//GoogleAnalytics.getInstance(getApplicationContext()).getLogger().setLogLevel(LogLevel.VERBOSE);
		//Tracker tracker  = GoogleAnalytics.getInstance(getApplicationContext())
		//		.newTracker(com.S22.bloqueo.AnalyticsTracker.PROPERTY_ID);
		//tracker.setScreenName("onGalleryScreen");
		//tracker.send(new HitBuilders.AppViewBuilder().build());
		
		progress = ProgressDialog.show(Galeria.this, "Cargando Galeria", "Espere, por favor.");
		
		Bundle extras = getIntent().getExtras();
		final String img = extras.getString("imagenes");
		
		imagenes = img.split(";");
		//galeria = (HorizontalScrollView) findViewById(R.id.scroll_view);
		galeria = (GridView) findViewById(R.id.galeria);

		//int tot_img = imagenes.length;
		
		GridViewAdapter customGridAdapter = new GridViewAdapter(this, R.layout.row, getData());
		
		galeria.setAdapter(customGridAdapter);
		galeria.dispatchSetSelected(true);
		galeria.setClickable(true);
		
		galeria.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Log.e("onItemClick", String.valueOf(id));
				//Toast.makeText(Galeria.this, position + "#onItemClickSelected" + String.valueOf(id),	Toast.LENGTH_SHORT).show();
				//Object imagen = galeria.getItemAtPosition(position);
				//ImageItem i = (ImageItem) imagen;
				
				//String ruta = i.getTitle();
				Intent intent = new Intent(getApplicationContext(), SliderActivity.class);
				intent.putExtra("imagenes", img);
				intent.putExtra("posicion", position);
				startActivity(intent);
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.galeria, menu);
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	private ArrayList<ImageItem> getData() {
		final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();
		//TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
		
		for (int i = 0; i < imagenes.length; i++) {
			
			GetImagen img = new GetImagen();
			//Adquiere la miniatura de la imagen para acelerar la carga :P
			img.execute("thumb_" +imagenes[i]);
			Bitmap bitmap = null;
			
			try {
				bitmap = img.get();
				imageItems.add(new ImageItem(bitmap, imagenes[i]));
			} catch (InterruptedException | ExecutionException e) {
				Toast.makeText(getApplicationContext(), "Error al intentar obtener la imagen", Toast.LENGTH_SHORT).show();
				//e.printStackTrace();
			}
			//imageItems.add(new ImageItem(bitmap, "Image#" + i));
		}
		if(progress.isShowing()) progress.dismiss();
		return imageItems;

	}
	
}
