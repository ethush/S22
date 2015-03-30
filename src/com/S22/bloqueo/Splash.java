package com.S22.bloqueo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.android.push.BasePushMessageReceiver;
import com.arellomobile.android.push.PushManager;
import com.arellomobile.android.push.utils.RegisterBroadcastReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;



public class Splash extends Activity {
	
	//Registration receiver
	RegisterBroadcastReceiver mBroadcastReceiver = new RegisterBroadcastReceiver()
	{
	    @Override
	    public void onRegisterActionReceive(Context context, Intent intent)
	    {
	        checkMessage(intent);
	    }
	};
	 
	//Push message receiver
	private BasePushMessageReceiver mReceiver = new BasePushMessageReceiver()
	{
	    @Override
	    protected void onMessageReceive(Intent intent)
	    {
	        //JSON_DATA_KEY contains JSON payload of push notification.
	        //showMessage("push message is " + intent.getExtras().getString(JSON_DATA_KEY));
	    }
	};
	 
	//Registration of the receivers
	public void registerReceivers()
	{
	    IntentFilter intentFilter = new IntentFilter(getPackageName() + ".action.PUSH_MESSAGE_RECEIVE");
	 
	    registerReceiver(mReceiver, intentFilter, getPackageName() +".permission.C2D_MESSAGE", null);
	     
	    registerReceiver(mBroadcastReceiver, new IntentFilter(getPackageName() + "." + PushManager.REGISTER_BROAD_CAST_ACTION));       
	}
	 
	public void unregisterReceivers()
	{
	    //Unregister receivers on pause
	    try
	    {
	        unregisterReceiver(mReceiver);
	    }
	    catch (Exception e)
	    {
	        // pass.
	    }
	     
	    try
	    {
	        unregisterReceiver(mBroadcastReceiver);
	    }
	    catch (Exception e)
	    {
	        //pass through
	    }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		//Log.d("hash", Settings.getApplicationSignature(getApplicationContext()));
		
		 // Add code to print out the key hash
	    /*try {
	        PackageInfo info = getPackageManager().getPackageInfo(
	                "com.S22.bloqueo", 
	                PackageManager.GET_SIGNATURES);
	        for (Signature signature : info.signatures) {
	            MessageDigest md = MessageDigest.getInstance("SHA");
	            md.update(signature.toByteArray());
	            Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
	            }
		    } catch (NameNotFoundException e) {
	
		    } catch (NoSuchAlgorithmException e) {

	    }*/
		
		
	    if(existeConexion()) {
	    	registerReceivers();
			 
		    PushManager pushManager = PushManager.getInstance(this);
		    
		    try {
		           pushManager.onStartup(this);
		    }
		    catch(Exception e)
		    {
		    	//e.printStackTrace();
		       //    Log.e("PUSH MANAGER", e.getLocalizedMessage() );
		    }
		       
		    //Register for push!
		    pushManager.registerForPushNotifications();
		    
		    checkMessage(getIntent());
		    
			
	    }
	    else {
	    	Toast.makeText(getApplicationContext(), "Red no disponible, o no se encuentran activos sus paquetes de datos.",Toast.LENGTH_LONG).show();
	    	finish();
	    }
	    
		
		Typeface helveticaNeuelTh = Typeface.createFromAsset(getAssets(),"HelveticaNeueLTStd-Th.otf");
		Typeface steelfish_rg = Typeface.createFromAsset(getAssets(),"steelfish_rg.ttf");
		
		TextView tittle = (TextView) findViewById(R.id.tittle);
		TextView tittle_long = (TextView) findViewById(R.id.tittle_long);
		
		tittle.setTypeface(steelfish_rg);
		tittle_long.setTypeface(helveticaNeuelTh);
		   
		//Constants.IS_LOGGED_FACEBOOK = false;
		//Constants.IS_LOGGED_TWITTER = false;
		
		TimerTask task = new TimerTask() {
			
			@Override
			public void run() {
				
				//Intent intent = new Intent(getApplicationContext(), MainLogin.class);
				Intent intent = new Intent(getApplicationContext(), MapActivity.class);
				startActivity(intent);
				finish();
				
								
			}
		};
		/* Variable para revision de disponibilidad de Google Play Services */
		int resCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		
		Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resCode, this,10);
		/* Validacion para servicios de GooglePlay */
		switch(resCode){
			case ConnectionResult.SUCCESS:
				if(existeConexion()) {
					Timer t = new Timer();
					t.schedule(task, 2000);
				}
				else {
					Toast.makeText(getApplicationContext(), "No se encontraron paquetes de datos,  intentalo nuevamente mas tarde",Toast.LENGTH_LONG).show();
			    	finish();
				}
				break;
			case ConnectionResult.SERVICE_DISABLED:
				dialog.show();
				break;
			case ConnectionResult.SERVICE_INVALID:
				dialog.show();
				break;
			case ConnectionResult.SERVICE_MISSING:
				dialog.show();
				break;
			case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
				dialog.show();
				break;
			default:
				dialog.show();
				break;
		}	
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Re-register receivers on resume
	    registerReceivers();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		//Unregister receivers on pause
	    unregisterReceivers();
	}
	
	@Override
	protected void onNewIntent(Intent intent)
	{
	    super.onNewIntent(intent);
	    setIntent(intent);
	 
	    checkMessage(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return false;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
			
	private void checkMessage(Intent intent)
	{
	    if (null != intent)
	    {
	        if (intent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
	        {
	            //showMessage("push message is " + intent.getExtras().getString(PushManager.PUSH_RECEIVE_EVENT));
	        }
	        else if (intent.hasExtra(PushManager.REGISTER_EVENT))
	        {
	            //showMessage("register");
	        }
	        else if (intent.hasExtra(PushManager.UNREGISTER_EVENT))
	        {
	            //showMessage("unregister");
	        }
	        else if (intent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
	        {
	            //showMessage("register error");
	        }
	        else if (intent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
	        {
	            //showMessage("unregister error");
	        }
	 
	        resetIntentValues();
	    }
	}
			 
	/**
	 * Will check main Activity intent and if it contains any PushWoosh data, will clear it
	 */
	private void resetIntentValues()
	{
	    Intent mainAppIntent = getIntent();
	 
	    if (mainAppIntent.hasExtra(PushManager.PUSH_RECEIVE_EVENT))
	    {
	        mainAppIntent.removeExtra(PushManager.PUSH_RECEIVE_EVENT);
	    }
	    else if (mainAppIntent.hasExtra(PushManager.REGISTER_EVENT))
	    {
	        mainAppIntent.removeExtra(PushManager.REGISTER_EVENT);
	    }
	    else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_EVENT))
	    {
	        mainAppIntent.removeExtra(PushManager.UNREGISTER_EVENT);
	    }
	    else if (mainAppIntent.hasExtra(PushManager.REGISTER_ERROR_EVENT))
	    {
	        mainAppIntent.removeExtra(PushManager.REGISTER_ERROR_EVENT);
	    }
	    else if (mainAppIntent.hasExtra(PushManager.UNREGISTER_ERROR_EVENT))
	    {
	        mainAppIntent.removeExtra(PushManager.UNREGISTER_ERROR_EVENT);
	    }
	 
	    setIntent(mainAppIntent);
	}
	 /* for testing purposes */
	/*private void showMessage(String message)
	{
	    Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}*/
	
}
