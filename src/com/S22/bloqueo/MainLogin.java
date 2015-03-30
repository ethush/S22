package com.S22.bloqueo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;



public class MainLogin extends Activity {

	private static final int TWITTER_LOGIN_REQUEST_CODE = 1;
	//private static final int FACEBOOK_LOGIN_REQUEST_CODE = 64206;
	
	//private MainFragment mainFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main_login);
		
		
	    
		/*
		if (savedInstanceState == null) {
			// Add the fragment on initial activity setup
			mainFragment = new MainFragment();
				getSupportFragmentManager()
				.beginTransaction()
				.add(android.R.id.content, mainFragment)
				.commit();
			} else {
				// Or set the fragment from restored state info
				mainFragment = (MainFragment) getSupportFragmentManager()
				.findFragmentById(android.R.id.content);
			}
		*/
		//Custom TTF
		Typeface helveticaNeuelTh = Typeface.createFromAsset(getAssets(),"HelveticaNeueLTStd-Th.otf");
	
		
		TextView texto = (TextView) findViewById(R.id.texto);
		Button siguiente = (Button) findViewById(R.id.button4);
		Button ir_a_sistema = (Button) findViewById(R.id.button5);
		
		texto.setTypeface(helveticaNeuelTh);
		siguiente.setTypeface(helveticaNeuelTh);
		ir_a_sistema.setTypeface(helveticaNeuelTh);
		
		ir_a_sistema.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MapActivity.class);
	            startActivity(intent);
	            finish();
			}
		});
		
		siguiente.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), InfoScreen.class);
				startActivity(intent);
			}
		});
		/*
		Button loginTwitter = (Button) findViewById(R.id.button1);
		loginTwitter.setTypeface(helveticaNeuelTh);
		
		Button no_login = (Button) findViewById(R.id.button2);
		no_login.setTypeface(helveticaNeuelTh);
		button infoView = (Button) findViewById(R.id.button4);
		
		loginTwitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!TwitterLogin.isConnected(getApplicationContext())){
				    Intent twitterLoginIntent = new Intent(getApplicationContext(), TwitterLogin.class);
				    twitterLoginIntent.putExtra(TwitterLogin.TWITTER_CONSUMER_KEY, Constants.CONSUMER_KEY);
				    twitterLoginIntent.putExtra(TwitterLogin.TWITTER_CONSUMER_SECRET, Constants.CONSUMER_SECRET);
				    startActivityForResult(twitterLoginIntent, TWITTER_LOGIN_REQUEST_CODE);
				  }
				
			}
		});
		
		
		no_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), MapActivity.class);
	            startActivity(intent);
	            finish();
			}
		});
		
		infoView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), InfoScreen.class);
				startActivity(intent);
			}
		});
		*/
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//Log.d("TAG", "ON ACTIVITY RESULT!");
	    if(requestCode == TWITTER_LOGIN_REQUEST_CODE){
	        //Log.d("TAG", "TWITTER LOGIN REQUEST CODE");
	        if(resultCode == TwitterLogin.TWITTER_LOGIN_RESULT_CODE_SUCCESS){
	            /* Si el login es exitoso, redirecciona a la pagina de mapa */
	        	//Log.d("TAG", "TWITTER LOGIN SUCCESS");
	        	Constants.IS_LOGGED_TWITTER = true;
	            Intent intent = new Intent(this,MapActivity.class);
	            startActivity(intent);
	            finish();
	        }else if(resultCode == TwitterLogin.TWITTER_LOGIN_RESULT_CODE_FAILURE){
	           // Log.d("TAG", "TWITTER LOGIN FAIL");
	        }
	        else{
	        	Constants.IS_LOGGED_TWITTER = false;
	        }	
	    }
	   /* if(requestCode == FACEBOOK_LOGIN_REQUEST_CODE && Session.getActiveSession().isOpened()) {
	    	//Log.d("TAG", "FACEBOOK LOGIN REQUEST CODE");
	    	Constants.IS_LOGGED_FACEBOOK = true;
	    	//Log.d("FACEBOOK SESSION",Session.getActiveSession().getState().toString());
	    	Intent intent = new Intent(this,MapActivity.class);
            startActivity(intent);
            finish();
        }*/
	    
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
	
	
	
}
