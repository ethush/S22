package com.S22.bloqueo;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class InfoScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_info_screen);
		
		//Tracker tracker  = GoogleAnalytics.getInstance(getApplicationContext())
		//		.newTracker(com.S22.bloqueo.AnalyticsTracker.PROPERTY_ID);
		//tracker.setScreenName("onInfoScreen");
		//tracker.send(new HitBuilders.AppViewBuilder().build());
		
		//Custon TTF
		Typeface helveticaNeuelTh = Typeface.createFromAsset(getAssets(),"HelveticaNeueLTStd-Th.otf");
		Typeface steelfish_rg = Typeface.createFromAsset(getAssets(),"steelfish_rg.ttf");
		
		TextView tittle_info = (TextView) findViewById(R.id.tittle_info);
		TextView tittle_long_info = (TextView) findViewById(R.id.tittle_long_info);
		
		TextView p1 = (TextView) findViewById(R.id.p_1_info);
		TextView p2 = (TextView) findViewById(R.id.p_2_info);
		TextView p3 = (TextView) findViewById(R.id.p_3_info);
		TextView p4 = (TextView) findViewById(R.id.p_4_info);
		TextView p5 = (TextView) findViewById(R.id.p_5_info);
		TextView p6 = (TextView) findViewById(R.id.p_6_info);
		TextView p7 = (TextView) findViewById(R.id.p_7_info);
		
		
		
		//Seccion de logotipo
		tittle_info.setTypeface(steelfish_rg);
		tittle_long_info.setTypeface(helveticaNeuelTh);
		
		//Parrafos
		p1.setTypeface(helveticaNeuelTh);
		p2.setTypeface(helveticaNeuelTh);
		p3.setTypeface(helveticaNeuelTh);
		p4.setTypeface(helveticaNeuelTh);
		p5.setTypeface(helveticaNeuelTh);
		p6.setTypeface(helveticaNeuelTh);
		p7.setTypeface(helveticaNeuelTh);
		
		
		
	}
}
