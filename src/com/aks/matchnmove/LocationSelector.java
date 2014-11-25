package com.aks.matchnmove;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

public class LocationSelector extends MapActivity  {
	private String LOG_TAG = "LocationSelector";
	LocationClient mLocationClient;
	Location mCurrentLocation;
	private GoogleMap map;
	LatLng locLatLng;
	Marker marker;
	Polyline routeMap;
	AutoCompleteTextView searchTerm;
	Button searchButton;
    
    public static class MapFragment extends Fragment {
    	  @Override
    	  public View onCreateView(
    	      LayoutInflater inflater,
    	      ViewGroup container,
    	      Bundle savedInstanceState) {
    	  // Inflate the layout for this fragment
    		setHasOptionsMenu(true);
    	  return inflater.inflate(R.layout.map_fragment, container, false);
    	  }
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    	getSupportActionBar().setDisplayShowHomeEnabled(true);
		return true;
	}
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.location_selector);

		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
		map.setMyLocationEnabled(true);  
		mLocationClient = new LocationClient(this, this, this);
	    searchTerm = (AutoCompleteTextView) findViewById(R.id.search);
	    searchButton = (Button) findViewById(R.id.searchButton);
	    searchTerm.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
	    searchButton.setOnClickListener(
	    		new OnClickListener() {

					@Override
					public void onClick(View v) {
						if(!searchTerm.getText().toString().isEmpty()) {
						new GetAddressTask().execute();
						}
					}
	    		});
	
		}

	/*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mLocationClient.connect();
    }
    
    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mLocationClient.disconnect();
        super.onStop();
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

	@Override
	public void finish() {
		Log.d(LOG_TAG, "Inside finish");
		Intent resultIntent = new Intent();
		Bundle locBundle = new Bundle();
		resultIntent.putExtra("address", searchTerm.getText().toString());
		locBundle.putParcelable("location", locLatLng);
		resultIntent.putExtra("bundle", locBundle);
		setResult(RESULT_OK, resultIntent);
		super.finish();
	}



	/*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {
        // Display the connection status
    	super.onConnected(dataBundle);
        mCurrentLocation = mLocationClient.getLastLocation();
        if (mCurrentLocation != null) {
        LatLng myLocation = 
				  new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        map.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        map.addMarker(new MarkerOptions()
        .position(myLocation)
        .title("You are here."));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        }
    }

    
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        super.onDisconnected();
    }
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    	super.onConnectionFailed(connectionResult);
    }
	
private class GetAddressTask extends AsyncTask<Void, Void, LatLng> {
    	@Override
        protected LatLng doInBackground(Void... params) {
    		
    		Geocoder coder = new Geocoder(LocationSelector.this);
        	List<Address> address;
        	LatLng p1 = null;

        	try {
        	    address = coder.getFromLocationName(searchTerm.getText().toString(),5);
        	    if (address == null) {
        	    	Log.d(LOG_TAG, "Couldn't search location.");
        	        return null;
        	    }
        	    Address location = address.get(0);
        	    location.getLatitude();
        	    location.getLongitude();

        	    p1 = new LatLng(location.getLatitude(), location.getLongitude());

        	    return p1;
        	} catch (Exception e) {
        		Log.e(LOG_TAG + "GetAddressTask", e.toString());
        		return null;
        	}
    	}
    	 @Override
    	  protected void onPostExecute(LatLng location) {
    	
    		 if(location != null) {
     		 map.addMarker(new MarkerOptions()
             .position(location)
             .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
     		 locLatLng = location;
    		
        		 
    		map.moveCamera(CameraUpdateFactory.newLatLng(location));
    		finish();
    		 } else {
    			 Toast.makeText(LocationSelector.this, "Couldn't search location.", Toast.LENGTH_LONG).show();
    		 }
    	    }
    	 }

private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
	private ArrayList<String> resultList;
	
	public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}
	
	@Override
	public int getCount() {
		return resultList.size();
	}

	@Override
	public String getItem(int index) {
		return resultList.get(index);
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();
				if (constraint != null) {
					// Retrieve the autocomplete results.
					resultList = autocomplete(constraint.toString());
					
					// Assign the data to the FilterResults
					filterResults.values = resultList;
					filterResults.count = resultList.size();
				}
				return filterResults;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				}
				else {
					notifyDataSetInvalidated();
				}
			}};
		return filter;
	}
	
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";

	private static final String API_KEY = "AIzaSyDASf7OX8KCX7D0iAoZoCwmdK_6Nd30oTg";

	private ArrayList<String> autocomplete(String input) {
	    ArrayList<String> resultList = null;

	    System.out.println("Inside FIlter");
	    HttpURLConnection conn = null;
	    StringBuilder jsonResults = new StringBuilder();
	    try {
	        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
	        sb.append("?key=" + API_KEY);
	        //sb.append("&components=country:uk");
	        sb.append("&input=" + URLEncoder.encode(input, "utf8"));

	        URL url = new URL(sb.toString());
	        conn = (HttpURLConnection) url.openConnection();
	        InputStreamReader in = new InputStreamReader(conn.getInputStream());

	        // Load the results into a StringBuilder
	        int read;
	        char[] buff = new char[1024];
	        while ((read = in.read(buff)) != -1) {
	            jsonResults.append(buff, 0, read);
	        }
	    } catch (MalformedURLException e) {
	        Log.e(LOG_TAG, "Error processing Places API URL", e);
	        return resultList;
	    } catch (IOException e) {
	        Log.e(LOG_TAG, "Error connecting to Places API", e);
	        return resultList;
	    } finally {
	        if (conn != null) {
	            conn.disconnect();
	        }
	    }

	    try {
	        // Create a JSON object hierarchy from the results
	        JSONObject jsonObj = new JSONObject(jsonResults.toString());
	        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

	        // Extract the Place descriptions from the results
	        resultList = new ArrayList<String>(predsJsonArray.length());
	        for (int i = 0; i < predsJsonArray.length(); i++) {
	            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
	        }
	    } catch (JSONException e) {
	        Log.e(LOG_TAG, "Cannot process JSON results", e);
	    }

	    return resultList;
	}
}
}