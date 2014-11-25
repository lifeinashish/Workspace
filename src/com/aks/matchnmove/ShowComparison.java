package com.aks.matchnmove;

import java.text.DecimalFormat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShowComparison extends ActionBarActivity {
	
	private String tel_prefix, location_string, location_code_string;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comparison);
		TextView distance = (TextView) findViewById(R.id.distance);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	    getSupportActionBar().setDisplayShowHomeEnabled(true);
	    LinearLayout easyLayout = (LinearLayout) findViewById(R.id.easy_cab_layout);
		TextView time = (TextView) findViewById(R.id.time);
		TextView location = (TextView) findViewById(R.id.location);
		TextView meru_day_cost_tv = (TextView) findViewById(R.id.meru_day_cost);
		TextView meru_night_cost_tv = (TextView) findViewById(R.id.meru_night_cost);
		TextView easy_day_cost_tv = (TextView) findViewById(R.id.easy_day_cost);
		TextView easy_night_cost_tv = (TextView) findViewById(R.id.easy_night_cost);
		TextView ola_sedan_cost_tv = (TextView) findViewById(R.id.ola_sedan_cost);
		TextView ola_mini_cost_tv = (TextView) findViewById(R.id.ola_mini_cost);
		TextView t4s_sedan_cost_tv = (TextView) findViewById(R.id.t4s_sedan_cost);
		TextView t4s_mini_cost_tv = (TextView) findViewById(R.id.t4s_mini_cost);
		String distanceString = "0", timeString = "0"; 
		
		if (savedInstanceState == null) {
		    Bundle extras = getIntent().getExtras();
		    if(extras != null) {
		    	distanceString = extras.getString("distance");
		    	timeString = extras.getString("time");
		    	location_string = extras.getString("location");
		    	location_code_string = extras.getString("locationCode");
		    	tel_prefix = extras.getString("tel_prefix");
		    }
		} else {
	    	distanceString= (String) savedInstanceState.getSerializable("distance");
	    	timeString= (String) savedInstanceState.getSerializable("time");
	    	location_string= (String) savedInstanceState.getSerializable("location");
	    	location_code_string= (String) savedInstanceState.getSerializable("location_code");
	    	tel_prefix= (String) savedInstanceState.getSerializable("tel_prefix");
		}
		int location_code = Integer.parseInt(location_code_string);
		Log.d("Location", location_string);
		location.setText(location_string);
		distance.setText("    " + distanceString);
		time.setText("     " + timeString);
		float distanceValue = Float.parseFloat((distanceString.split(" ")[0]).replace(",", ""));
		DecimalFormat twoDForm = new DecimalFormat("#.##");
		double[] meru_cost = new MeruCostCalculator().costCalulator(distanceValue, location_code);
		double[] ola_cost = new OlaCostCalculator().costCalulator(distanceValue, location_code);
		double[] t4s_cost = new TaxiForSureCostCalculator().costCalulator(distanceValue, location_code);
		

		meru_day_cost_tv.setText("Day Cost: " + Double.valueOf(twoDForm.format(meru_cost[0])) + " Rs.");
		meru_night_cost_tv.setText("Night Cost: " + Double.valueOf(twoDForm.format(meru_cost[1])) + " Rs.");
		ola_sedan_cost_tv.setText("Sedan Cost: " + Double.valueOf(twoDForm.format(ola_cost[0])) + " Rs.");
		ola_mini_cost_tv.setText("Mini Cost: " + Double.valueOf(twoDForm.format(ola_cost[1])) + " Rs.");
		t4s_sedan_cost_tv.setText("Sedan Cost: " + Double.valueOf(twoDForm.format(t4s_cost[0])) + " Rs.");
		t4s_mini_cost_tv.setText("Mini Cost: " + Double.valueOf(twoDForm.format(t4s_cost[1])) + " Rs.");
		
		if (location_code != 5) {
			easy_day_cost_tv.setText("Day Cost: " + Double.valueOf(twoDForm.format(meru_cost[0])) + " Rs.");
			easy_night_cost_tv.setText("Night Cost: " + Double.valueOf(twoDForm.format(meru_cost[1])) + " Rs.");
			easyLayout.setVisibility(View.VISIBLE);
		}

		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
        NavUtils.navigateUpFromSameTask(this);
        return true;
     }
		return super.onOptionsItemSelected(item);
	}

	public void callButtonHandler(View v) {
		String tel = null;
		switch (v.getId()) {
        case R.id.meru_call:
            tel = tel_prefix + "44224422";
            break;
        case R.id.ola_call:
        	tel = tel_prefix + "33553355";
            break;
        case R.id.t4s_call:
        	tel = tel_prefix + "60601010";
            break;
        case R.id.easy_call:
        	tel = tel_prefix + "43434343";
    }
		Intent callIntent = new Intent(Intent.ACTION_DIAL);          
        callIntent.setData(Uri.parse("tel:" + tel));          
        startActivity(callIntent);
	}
}
