package com.S22.bloqueo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity{

	//private static final int TWITTER_LOGIN_REQUEST_CODE = 1;
	/* Declaraciones globales para manejo de el mapa y marcadores. */
	GoogleMap mapa;
	
	String t_marker = null, 
		   d_marker = null,
		   detalles[] = null;
	
	LatLngBounds.Builder bounds = new LatLngBounds.Builder();
	ArrayList<LatLng> lugares = new ArrayList<LatLng>();
	ArrayList<String> descripciones = new ArrayList<String>();
	ArrayList<String> snippet = new ArrayList<String>();
	ArrayList<String> tipos = new ArrayList<>();
	
	AdView adView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_map);
		
		//View title = getWindow().findViewById(android.R.id.title);
		//View titleBar = (View) title.getParent();
		//titleBar.setBackgroundColor(getResources().getColor(R.color.com_facebook_blue));
		//Log.d("LOGGED", "TWITTER LOGGED " + String.valueOf(Constants.IS_LOGGED_TWITTER));
		//Log.d("LOGGED", "FACEBOOK LOOGED " + String.valueOf(Constants.IS_LOGGED_FACEBOOK));

		//GoogleAnalytics.getInstance(getApplicationContext()).getLogger().setLogLevel(LogLevel.VERBOSE);
		//**************inicio del bloqueo de ANALYTICS
		//Tracker tracker  = GoogleAnalytics.getInstance(getApplicationContext())
		//					.newTracker(com.S22.bloqueo.AnalyticsTracker.PROPERTY_ID);
		//tracker.setScreenName("onMapScreen");
		//tracker.send(new HitBuilders.AppViewBuilder().build());
		//*****************termina bloqueo de ANALYTICS
		// Crear adView.
	    adView = new AdView(this);
	    adView.setAdUnitId(getString(R.string.banner_ad_unit_id));
	    adView.setAdSize(AdSize.BANNER);

	    // Buscar AdView como recurso y cargar una solicitud.
	    AdView adView = (AdView)this.findViewById(R.id.adView);
	    AdRequest adRequest = new AdRequest.Builder()
	    		.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
	    		//.addTestDevice("FF9672ED523D9304062D1CA92B1D8562")
	    		.build();
	    
	    
	    adView.loadAd(adRequest);


		
		try {
			MapsInitializer.initialize(getApplicationContext());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		mapa = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapa)).getMap();
		
		mapa.setMyLocationEnabled(true);
	    mapa.getUiSettings().setMyLocationButtonEnabled(false);
		
		mapa.setOnMarkerClickListener(new OnMarkerClickListener() {
			
			@Override
			public boolean onMarkerClick(Marker marker) {
				
				//GoogleAnalytics.getInstance(getApplicationContext()).getLogger().setLogLevel(LogLevel.VERBOSE);
				//Tracker tracker  = GoogleAnalytics.getInstance(getApplicationContext())
				//		.newTracker(com.S22.bloqueo.AnalyticsTracker.PROPERTY_ID);
				//tracker.send(new HitBuilders.EventBuilder().setCategory("MarkerClicked").setAction("MarkerClicked").build());
				
				//Centramos el marcador en el mapa
				mapa.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
				
				/*
				 * Extrae y crea la vista popup
				 * */
				LinearLayout viewGroup = (LinearLayout) MapActivity.this.findViewById(R.id.info);
				LayoutInflater layoutInflater = (LayoutInflater) MapActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				
				View layout = layoutInflater.inflate(R.layout.info, viewGroup);
				
				//Custom TTF
				Typeface helveticaNeuelTh = Typeface.createFromAsset(getAssets(),"HelveticaNeueLTStd-Th.otf");
				Typeface helveticaNeuelLt = Typeface.createFromAsset(getAssets(),"HelveticaNeueLTStd-Lt.otf");
				
				/*
				 * Extrae los datos del marcador y los asigna a los controles de vista
				 * */
				TextView titulo = (TextView) layout.findViewById(R.id.titulo);
				titulo.setTypeface(helveticaNeuelLt);
				//TextView descripcion = (TextView) layout.findViewById(R.id.descripcion);
				TextView descripcion = (TextView) layout.findViewById(R.id.descripcion);
				descripcion.setTypeface(helveticaNeuelTh);
				/*
				 * posicion 0 descripcion
				 * posicion 1 url con imagenes
				 * */
				
				detalles = marker.getSnippet().split(":"); 
						
				titulo.setText(marker.getTitle());
				descripcion.setText(detalles[0]);
								
				/*
				 * Botones de accion para popup
				 * */
				
				Button twitterButton = (Button) layout.findViewById(R.id.twitter);
				Button galeria = (Button) layout.findViewById(R.id.galeria);
				Button facebookButton = (Button) layout.findViewById(R.id.facebook);
				
				
				/*
				 * Valores para enviar via Twitter
				 * 
				 * */
				t_marker = marker.getTitle();
				d_marker = detalles[0];
				
				
				twitterButton.setOnClickListener(new OnClickListener() {
					
					/* Calcular en el area de captura que el titulo y la descripcion corta sean 135 caracteres descontando el hashtag #S22  */
					@Override
					public void onClick(View v) {
						try	{
							
							//GoogleAnalytics.getInstance(getApplicationContext()).getLogger().setLogLevel(LogLevel.VERBOSE);
							//Tracker tracker  = GoogleAnalytics.getInstance(getApplicationContext())
							//		.newTracker(com.S22.bloqueo.AnalyticsTracker.PROPERTY_ID);
							//tracker.send(new HitBuilders.EventBuilder()
							//			.setAction("GalleryButtonClicked")
							//			.setCategory("ClickedButton")
							//			.build());
							
							String url = "https://twitter.com/intent/tweet?text="+t_marker + " - " + d_marker + "&hashtags=S22";
							Intent i = new Intent(Intent.ACTION_VIEW);
							i.setData(Uri.parse(url));
							startActivity(i);
							
						}
						catch (ActivityNotFoundException ex) {
							Log.e("NO APP", ex.getMessage());
							Toast.makeText(getApplicationContext(),"No se encontraron aplicaciones instaladas para la acción.", Toast.LENGTH_SHORT).show();
						}
					}
				});
				
				facebookButton.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
					    
						//GoogleAnalytics.getInstance(getApplicationContext()).getLogger().setLogLevel(LogLevel.VERBOSE);
						//Tracker tracker  = GoogleAnalytics.getInstance(getApplicationContext())
						//		.newTracker(com.S22.bloqueo.AnalyticsTracker.PROPERTY_ID);
						//tracker.send(new HitBuilders.EventBuilder()
						//			.setAction("GalleryButtonClicked")
						//			.setCategory("ClickedButton")
						//			.build());
						
						List<Intent> targetShareIntents=new ArrayList<Intent>();
					    Intent shareIntent=new Intent();
					    shareIntent.setAction(Intent.ACTION_SEND);
					    shareIntent.setType("text/plain");
					    List<ResolveInfo> resInfos=getPackageManager().queryIntentActivities(shareIntent, 0);
					    if(!resInfos.isEmpty()){
					        //System.out.println("Have package");
					        for(ResolveInfo resInfo : resInfos){
					            String packageName=resInfo.activityInfo.packageName;
					            //Log.i("Package Name", packageName);
					            if(packageName.contains("com.facebook.katana")){
					                Intent intent=new Intent();
					                intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
					                intent.setAction(Intent.ACTION_SEND);
					                intent.setType("text/plain");
					                intent.putExtra(Intent.EXTRA_TEXT, t_marker + " - " + d_marker);
					                intent.putExtra(Intent.EXTRA_SUBJECT, t_marker + " - " + d_marker);
					                intent.setPackage(packageName);
					                targetShareIntents.add(intent);
					            }
					        }
					        if(!targetShareIntents.isEmpty()){
					            //System.out.println("Have Intent");
					            Intent chooserIntent=Intent.createChooser(targetShareIntents.remove(0), "Compartir");
					            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
					            startActivity(chooserIntent);
					        }else{
					            //System.out.println("Do not Have Intent");
					            Toast.makeText(getApplicationContext(), "No se han encontrado aplicaciones instaladas para esta acción.", Toast.LENGTH_LONG).show();
					        }
					    }
											    
					}
				});
				
				galeria.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//Intent intent = new Intent(getApplicationContext(), SliderActivity.class);
						//intent.putExtra("imagenes", detalles[1]);
						//startActivity(intent);
						
						if (detalles.length > 1) {
							if(detalles[1] != null && !detalles[1].isEmpty()) {
								
								//GoogleAnalytics.getInstance(getApplicationContext()).getLogger().setLogLevel(LogLevel.VERBOSE);
								//Tracker tracker  = GoogleAnalytics.getInstance(getApplicationContext())
								//		.newTracker(com.S22.bloqueo.AnalyticsTracker.PROPERTY_ID);
								//tracker.send(new HitBuilders.EventBuilder()
								//		.setAction("GalleryButtonClicked")
								//		.setCategory("ClickedButton")
								//		.build());
								
								Intent intent = new Intent(getApplicationContext(), Galeria.class);
								//Intent intent = new Intent(getApplicationContext(), SliderActivity.class);
								intent.putExtra("imagenes", detalles[1]);
								startActivity(intent);
							}
							else {
								Toast.makeText(getApplicationContext(), "Sin imagenes disponibles, intente mas tarde", Toast.LENGTH_SHORT).show();
							}
						}
						else {
							Toast.makeText(getApplicationContext(), "Sin imagenes disponibles, intente mas tarde", Toast.LENGTH_SHORT).show();
						}
					}
				});
				
				DisplayMetrics displayMetrics = new DisplayMetrics();
				WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); 
				wm.getDefaultDisplay().getMetrics(displayMetrics);
				
				/*
				 * Obtiene la resolucion de pantalla y en base a proporciones se dibuja la ventana popup
				 * */
				Display screen = getWindowManager().getDefaultDisplay();
				Point point = new Point();
				screen.getSize(point);
				
				int width = (int)(point.x*0.85);
				int height = (int)(point.y*0.28);
				
				
				PopupWindow popup = new PopupWindow(MapActivity.this);
				popup.setContentView(layout);
				//popup.setWindowLayoutMode(displayMetrics.widthPixels, LayoutParams.WRAP_CONTENT);
				Drawable draw = getResources().getDrawable(R.drawable.popup_bg9);
				popup.setBackgroundDrawable(draw);
				//popup.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
				popup.setWidth(width);
				popup.setHeight(height);
				popup.setFocusable(true);
				popup.showAtLocation(layout, Gravity.CENTER, 0, 0-(height/2));
				popup.update();
				
				
				
				return true;
			}
			
		});
		
		
		/** Peticion para marcadores a mostrar en el mapa **/		
		GetMarkers getMarkers = new GetMarkers();
		
		int total_registros = 0;
		String json = null;
		
		JSONObject marcadores = null;
		
	    
		try {
			getMarkers.execute();
			//Log.e("PETICION", "INICIANDO");
			if(existeConexion()) {
				json = getMarkers.get();
				marcadores = new JSONObject(json);
				//Log.e("TO JSON", marcadores.toString(4));
				/** Obtenemos el total de registros **/
				total_registros = Integer.parseInt(marcadores.getString("registros"));
			}
			else {
				Toast.makeText(getApplicationContext(), "No se encontraron paquetes de datos,  intentalo nuevamente mas tarde",Toast.LENGTH_LONG).show();
		    	finish();
			}
			//Log.e("JSON ORIGINAL", json);
			
			//Log.e("REGISTROS", String.valueOf(total_registros));
		} catch (InterruptedException | ExecutionException | JSONException e) {
			e.printStackTrace();
			//Log.e("Error", e.getMessage());
		}
	    			//Log.e("EXTRACT INFO", "Start");
		//Si hay registros los mostramos
		if(total_registros > 0) {
			setMarcadores(total_registros, marcadores);
		}
		// En caso contrario centramos la ciudad de oaxaca y un mensaje de todo bien! 
		else {
			LatLng posicion = new LatLng(17.065974586856083,-96.72260284423828);
			mapa.moveCamera(CameraUpdateFactory.newLatLng(posicion));
			CameraPosition cameraPosition = new CameraPosition.Builder()
			.target(posicion)
			.zoom(12)
			.bearing(0)
			.build();
			//efecto de zoom si la version de android la soporta.
			mapa.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			
			Toast.makeText(getApplicationContext(), "Todo bien!", Toast.LENGTH_LONG).show();
		}
	    
	}

	@Override
	protected void onPause() {
		super.onPause();
		adView.pause();
	}
	@Override
	protected void onResume() {
		super.onResume();
		adView.resume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		adView.destroy();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
		//return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		
		if (id == R.id.action_settings) {
			return true;
		}/*
		if(id == R.id.login_setting) {
			//Log.e("LOGIN", "LOGIN OPTION CLICKED");
			Intent intent = new Intent(getApplicationContext(), MainLogin.class);
			startActivity(intent);
		}
		if(id == R.id.logout_setting) {
			//Log.e("LOGIN", "LOGOUT OPTION CLICKED");
			Constants.IS_LOGGED_FACEBOOK = false;
			Constants.IS_LOGGED_TWITTER = false;
			
			//Log.d("FACEBOOK SESSION BEFORE CLOSE",Session.getActiveSession().getState().toString());
			if (Session.getActiveSession() != null) {
				if (Session.getActiveSession().isOpened() || Session.getActiveSession().getState().equals(Session.ACTION_ACTIVE_SESSION_OPENED)) {
					Session.getActiveSession().closeAndClearTokenInformation();
					Session.getActiveSession().close();
				}
			}
			//Log.d("FACEBOOK SESSION AFTER CLOSE",Session.getActiveSession().getState().toString());
			TwitterLogin.logOutOfTwitter(getApplicationContext());
		}
		*/
		if(id == R.id.ayuda) {
			Intent intent = new Intent(getApplicationContext(), InfoScreen.class);
			startActivity(intent);
		}
		
		if(id == R.id.faq) {
			Intent intent = new Intent(getApplicationContext(), FAQActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		/*
		 * map.xml
		 * item 0: login_settings
		 * item 1: logout_settings
		 * */
		/*
		if(TwitterLogin.isConnected(getApplicationContext()) || Constants.IS_LOGGED_FACEBOOK){
			menu.getItem(0).setVisible(false);
			menu.getItem(1).setVisible(true);
		}
		else {
			menu.getItem(0).setVisible(true);
			menu.getItem(1).setVisible(false);
		}
		*/
		return super.onPrepareOptionsMenu(menu);
	}
	
	private boolean existeConexion() {
		boolean connected = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || 
		            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
		        //we are connected to a network
		        connected = true;
		    }
		    else
		        connected = false;
		
		    return connected;
	}
	/*
	 * Funcion para cargar marcadores y agregarlos al mapa
	 * */
	private void setMarcadores(int total_registros, JSONObject marcadores) {
		
		for(int i=0;i<total_registros;i++) {
			try {
				JSONObject marcador = marcadores.getJSONObject("m_"+String.valueOf(i));
				//Log.e("Titulo_"+String.valueOf(i), marcador.getString("titulo"));
				
				/** Asigna los valores en los arrays correspondientes **/
				lugares.add(new LatLng(Double.parseDouble(marcador.getString("latitud")),Double.parseDouble(marcador.getString("longitud"))));
				descripciones.add(marcador.getString("titulo"));
				snippet.add(marcador.getString("descripcion")+":"+marcador.getString("imagenes"));
				tipos.add(marcador.getString("tipo_bloqueo"));
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		}
		//Log.e("TOTAL LUGARES", String.valueOf(lugares.size()));
		//Log.e("TOTAL DESCRIPCIONES", String.valueOf(descripciones.size()));
		//Log.e("TOTAL SNIPPET", String.valueOf(snippet.size()));
		//Log.e("TOTAL TIPOS", String.valueOf(tipos.size()));

		for(int i=0; i<lugares.size();i++) {
			
			bounds.include(lugares.get(i));
			
			MarkerOptions mo = new MarkerOptions()
							.position(lugares.get(i))
							.title(descripciones.get(i))
							.snippet(snippet.get(i)
							);
			
			switch(tipos.get(i)) {
				case "1": //icono verde
					mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.verde_pin));
					break;
				case "2": //amarillo
					mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.amarillo_pin));
					break;
				case "3": //rojo
					mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.rojo_pin));
					break;
				case "4": //morado
					mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.morado_pin));
					break;
				case "5": //obra_pin
					mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.obra_pin));
					break;
				default: //icono de falla en caso de iniciar iconos de prueba
					mo.icon(BitmapDescriptorFactory.fromResource(R.drawable.fail_ico));
					break;
			}
			
						
			mapa.addMarker(mo);
		}
		
		LatLngBounds bnds = bounds.build();
		mapa.moveCamera(CameraUpdateFactory.newLatLngBounds(bnds, 100,100, 0));
		
	}
	
	
	
}
