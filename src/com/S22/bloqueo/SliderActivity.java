package com.S22.bloqueo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;
import ru.truba.touchgallery.GalleryWidget.UrlPagerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger.LogLevel;

public class SliderActivity extends Activity {

	private GalleryViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_slider);
		
		
		GoogleAnalytics.getInstance(getApplicationContext()).getLogger().setLogLevel(LogLevel.VERBOSE);
		//Tracker tracker  = GoogleAnalytics.getInstance(getApplicationContext())
		//					.newTracker(com.S22.bloqueo.AnalyticsTracker.PROPERTY_ID);
		//tracker.setScreenName("onSliderGalleryScreen");
		//tracker.send(new HitBuilders.AppViewBuilder().build());
		
		
		Bundle extras = getIntent().getExtras();
		String img = extras.getString("imagenes");
		Integer posicion = extras.getInt("posicion");
		//Log.d("Strings", extras.getString("imagenes"));
		String[] urls  = img.split(";");
		
		for(int i=0; i<urls.length;i++) {
			String temp = urls[i];
			
			urls[i] = "http://www.s22.mx/administracion/uploads/" + temp;
			
			//Log.d("URL SET", urls[i]);
		}
		
		List<String> items = new ArrayList<String>();
		Collections.addAll(items, urls);
			
		UrlPagerAdapter pagerAdapter = new UrlPagerAdapter(this, items);
		/*pagerAdapter.setOnItemChangeListener(new OnItemChangeListener() {
			@Override
			public void onItemChange(int currentPosition)
			{
				Toast.makeText(SliderActivity.this, "Current item is " + currentPosition, Toast.LENGTH_SHORT).show();
			}
		});*/
			 
		mViewPager = (GalleryViewPager)findViewById(R.id.viewer);
		
		mViewPager.setOffscreenPageLimit(posicion);
		mViewPager.setAdapter(pagerAdapter);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.slider, menu);
		return false;
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
}
