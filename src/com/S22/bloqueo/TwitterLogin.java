package com.S22.bloqueo;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class TwitterLogin extends Activity {

	public static final int TWITTER_LOGIN_RESULT_CODE_SUCCESS = 1;
	public static final int TWITTER_LOGIN_RESULT_CODE_FAILURE = 2;
	
	private static final int TWITTER_LOGIN_REQUEST_CODE = 1;
	
	public static final String TWITTER_CONSUMER_KEY = "twitter_consumer_key";
	public static final String TWITTER_CONSUMER_SECRET = "twitter_consumer_secret";
	private WebView twitterLoginWebView;
	private ProgressDialog mProgressDialog;
	public String twitterConsumerKey;
	public String twitterConsumerSecret;
	private static Twitter twitter;
	private static RequestToken requestToken;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_login);
		
		
		twitterConsumerKey = getIntent().getStringExtra(TWITTER_CONSUMER_KEY);
		twitterConsumerSecret = getIntent().getStringExtra(TWITTER_CONSUMER_SECRET);
		
		if(twitterConsumerKey == null || twitterConsumerSecret == null){
			Log.e(Constants.TAG, "ERROR: Consumer Key and Consumer Secret required!");
			TwitterLogin.this.setResult(TWITTER_LOGIN_RESULT_CODE_FAILURE);
			TwitterLogin.this.finish();
		}
		 
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setCancelable(false);
		mProgressDialog.setCanceledOnTouchOutside(false);
		mProgressDialog.show();
		
		twitterLoginWebView = (WebView)findViewById(R.id.web_view);
		twitterLoginWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if( url.contains(Constants.TWITTER_CALLBACK_URL))
				{
					Uri uri = Uri.parse(url);
					TwitterLogin.this.saveAccessTokenAndFinish(uri);
					return true;
				}
				return false;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				
				super.onPageFinished(view, url);
				
				if(mProgressDialog != null) mProgressDialog.dismiss();
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				
				super.onPageStarted(view, url, favicon);
				
				if(mProgressDialog != null) mProgressDialog.show();
			}
		});
		
		//Log.d(Constants.TAG, "ASK OAUTH");
		askOAuth();
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 
		super.onActivityResult(requestCode, resultCode, data);
		
		 //Log.d("TAG", "ON ACTIVITY RESULT!");
		    if(requestCode == TWITTER_LOGIN_REQUEST_CODE){
		        //Log.d("TAG", "TWITTER LOGIN REQUEST CODE");
		        if(resultCode == TwitterLogin.TWITTER_LOGIN_RESULT_CODE_SUCCESS){
		        	//Log.d("TAG", "TWITTER LOGIN SUCCESS");
		        }else if(resultCode == TwitterLogin.TWITTER_LOGIN_RESULT_CODE_FAILURE){
		            //Log.d("TAG", "TWITTER LOGIN FAIL");
		        }else{
		        //
		        }
		    }
	}
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
		
		if(mProgressDialog != null) mProgressDialog.dismiss();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	private void saveAccessTokenAndFinish(final Uri uri){
		new Thread(new Runnable() {
		@Override
		public void run() {
			String verifier = uri.getQueryParameter(Constants.IEXTRA_OAUTH_VERIFIER);
			try {
				SharedPreferences sharedPrefs = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
				AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
				Editor e = sharedPrefs.edit();
				e.putString(Constants.PREF_KEY_TOKEN, accessToken.getToken());
				e.putString(Constants.PREF_KEY_SECRET, accessToken.getTokenSecret());
				e.commit();
				//Log.d(Constants.TAG, "TWITTER LOGIN SUCCESS!!!");
				TwitterLogin.this.setResult(TWITTER_LOGIN_RESULT_CODE_SUCCESS);
			} catch (Exception e) {
				e.printStackTrace();
				if(e.getMessage() != null) Log.e(Constants.TAG, e.getMessage());
				else Log.e(Constants.TAG, "ERROR: Twitter callback failed");
				TwitterLogin.this.setResult(TWITTER_LOGIN_RESULT_CODE_FAILURE);
			}
			
			TwitterLogin.this.finish();
		}
		}).start();
	}
	
	
	private void askOAuth() {
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
		configurationBuilder.setOAuthConsumerKey(twitterConsumerKey);
		configurationBuilder.setOAuthConsumerSecret(twitterConsumerSecret);
		Configuration configuration = configurationBuilder.build();
		twitter = new TwitterFactory(configuration).getInstance();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					requestToken = twitter.getOAuthRequestToken(Constants.TWITTER_CALLBACK_URL);
				} 
				catch (Exception e) {
					final String errorString = e.toString();
					TwitterLogin.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mProgressDialog.cancel();
							Toast.makeText(TwitterLogin.this, errorString.toString(), Toast.LENGTH_SHORT).show();
							finish();
						}
					});
					e.printStackTrace();
					return;
				}
				TwitterLogin.this.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						//Log.d(Constants.TAG,"LOADING AUTH URL");
						twitterLoginWebView.loadUrl(requestToken.getAuthenticationURL());
					}
				});
			}
		}).start();
	}
	
	//====== TWITTER HELPER METHODS ======
	public static boolean isConnected(Context ctx) {
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Constants.PREF_KEY_TOKEN, null) != null;
	}
	
	public static void logOutOfTwitter(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		Editor e = sharedPrefs.edit();
		e.putString(Constants.PREF_KEY_TOKEN, null);
		e.putString(Constants.PREF_KEY_SECRET, null);
		e.commit();
	}
	
	public static String getAccessToken(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Constants.PREF_KEY_TOKEN, null);
	}
	
	public static String getAccessTokenSecret(Context ctx){
		SharedPreferences sharedPrefs = ctx.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
		return sharedPrefs.getString(Constants.PREF_KEY_SECRET, null);
	}
}
