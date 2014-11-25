package com.aks.matchnmove;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aks.matchnmove.DirectionsJSONParser.RouteInfo;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends MapActivity {
	
	private String LOG_TAG  = "MainActivity";
	private String distance, duration, startAddress, endAddress, tel_prefix;
	public static LatLng location;
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private String fromButtonText = "Current Location", toButtonText;
	private GoogleMap map;
	private Marker fromMarker = null, toMarker = null;
	private int location_code;
	private Button from, to, next;
	private LinearLayout imageLayout;
	private Polyline routeMap;
	final private String[] NCR = {"delhi", "gurgaon", "faridabad", "ghaziabad", "noida"};
	
	// Global constants
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
    		FROM_LOCATION_REQUEST = 1, TO_LOCATION_REQUEST = 2;


    // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;
        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }
        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }
        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }
    
    
    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
    	if (requestCode == FROM_LOCATION_REQUEST) {
        	if(resultCode == RESULT_OK){
        		if(!data.getStringExtra("address").isEmpty()) {
        			fromButtonText = data.getStringExtra("address");
        			from.setText(fromButtonText);
        		}
        		Bundle locBundle = data.getParcelableExtra("bundle");
        		LatLng loc = locBundle.getParcelable("location");
        		if(loc != null) {
        			if (fromMarker != null) {
            			fromMarker.remove();
            		}
        		
        		fromMarker = map.addMarker(new MarkerOptions()
                .position(loc)
                .title("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        		map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
        		updateRouteMap();
        		}
        	}
    	}
    	else if (requestCode == TO_LOCATION_REQUEST) {
        	if(resultCode == RESULT_OK){
        		if(!data.getStringExtra("address").isEmpty()) {
        			toButtonText = data.getStringExtra("address");
        			to.setText(toButtonText);
        		}
        		Bundle locBundle = data.getParcelableExtra("bundle");
        		LatLng loc = locBundle.getParcelable("location");
        		if(loc != null) {
        		if (toMarker != null) {
        			toMarker.remove();
        		}
        		toMarker = map.addMarker(new MarkerOptions()
                .position(loc)
                .title("End")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));	    
        		updateRouteMap();
        		
        		int margin = dpToPixel(19);
        		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageLayout.getLayoutParams();
        		params.setMargins(0, margin, 0, 0); 
        		imageLayout.setLayoutParams(params);
        		next.setClickable(true);
        		}
        	} 
        		
    	}
     }
    
    private void updateRouteMap() {
    	if ((fromMarker != null) && (toMarker != null)) {
    		try {
    		LatLngBounds.Builder builder = new LatLngBounds.Builder();	
		    builder.include(fromMarker.getPosition());
		    builder.include(toMarker.getPosition());
		    LatLngBounds bounds = builder.build();
		    map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
    		String url = getDirectionsUrl(fromMarker.getPosition(), toMarker.getPosition()); 
    		DownloadTask downloadTask = new DownloadTask();
    		downloadTask.execute(url);
    		} catch(Exception e) {
    			Toast.makeText(MainActivity.this, "Looking for a flight route?", Toast.LENGTH_LONG).show();
    			Log.e(LOG_TAG, e.toString());
    		}
    	}
    }
    

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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (! locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            showGPSDisabledAlert();
        }
		FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment mapFragment =  (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        map = mapFragment.getMap();
		map.setMyLocationEnabled(true);  
		mLocationClient = new LocationClient(this, this, this);
		from = (Button) findViewById(R.id.from);
		to = (Button) findViewById(R.id.to);
		next = (Button) findViewById(R.id.next);
		imageLayout = (LinearLayout) findViewById(R.id.imageLayout);
		from.setOnClickListener(new AddressButtonClicked(FROM_LOCATION_REQUEST));
		to.setOnClickListener(new AddressButtonClicked(TO_LOCATION_REQUEST));
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((fromMarker != null) && (toMarker != null)) {
					
					String loc = getLocation(startAddress);
					if (loc != null) {
					Intent comparisonIntent = new Intent(MainActivity.this, ShowComparison.class);   
					comparisonIntent.putExtra("distance", distance);
					comparisonIntent.putExtra("time", duration);
					comparisonIntent.putExtra("location", loc);
					comparisonIntent.putExtra("locationCode", Integer.toString(location_code));
					comparisonIntent.putExtra("tel_prefix", tel_prefix);
		            startActivity(comparisonIntent);
					} else {
						Toast.makeText(MainActivity.this, "We are working hard to support this start location.",
								Toast.LENGTH_LONG).show();
					}
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
    
    class AddressButtonClicked implements OnClickListener {

    	int addressType;
    	public AddressButtonClicked(int addressType) {
    		this.addressType = addressType;
    	}
		@Override
		public void onClick(View v) {
			Intent i = new Intent(MainActivity.this, LocationSelector.class);   
            startActivityForResult(i, addressType);
			
		}
    	
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_about_app) {
			Intent aboutUs = new Intent(MainActivity.this, AboutUs.class);   
            startActivity(aboutUs);
			return true;
		} else if (id == R.id.action_share_app) {
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT,
					"Compare and save money on cabs with Match n' Move - http://play.google.com/store/apps/details?id=com.aks.matchnmove");
			sendIntent.setType("text/plain");
			startActivity(sendIntent);
		}
		return super.onOptionsItemSelected(item);
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
        if((fromMarker == null) && (mCurrentLocation != null)) {
          LatLng myLocation = 
   			new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        fromMarker = map.addMarker(new MarkerOptions()
        .position(myLocation)
        .title("You are here."));
        
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        }
    }
    
    @Override
    public void onBackPressed() {
    	Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    	super.onConnectionFailed(connectionResult);
    }
    
    @Override
    public void onDisconnected() {
    	super.onDisconnected();
    }
    
	
   private String getDirectionsUrl(LatLng origin,LatLng dest){
        
        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
        
        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;        
        
                    
        // Sensor enabled
        String sensor = "sensor=false";            
                    
        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
                    
        // Output format
        String output = "json";
        
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        return url;
    }
    
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
                URL url = new URL(strUrl);

                // Creating an http connection to communicate with url 
                urlConnection = (HttpURLConnection) url.openConnection();

                // Connecting to url 
                urlConnection.connect();

                // Reading data from url 
                iStream = urlConnection.getInputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

                StringBuffer sb  = new StringBuffer();

                String line = "";
                while( ( line = br.readLine())  != null){
                        sb.append(line);
                }
                
                data = sb.toString();

                br.close();

        }catch(Exception e){
                Log.d("Exception while downloading url", e.toString());
        }finally{
                iStream.close();
                urlConnection.disconnect();
        }
        return data;
     }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{            
    	ProgressDialog progress;
    	@Override
    	protected void onPreExecute() {
    		progress = new ProgressDialog(MainActivity.this);
    		progress.setMessage("Calculating route..");
    		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    		progress.show();
    	}
        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {
                    
            try{
                // Fetching the data from web service
                return downloadUrl(url[0]);
                
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return null;        
        }
        
        
        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {     
            super.onPostExecute(result);            
            progress.dismiss();
            if (result == null) {
            	Toast.makeText(MainActivity.this, "Failed to calculate route.", Toast.LENGTH_LONG).show();
            }
            ParserTask parserTask = new ParserTask();
            
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
                
        }        
    }
    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
        
        // Parsing the data in non-ui thread        
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            
            JSONObject jObject;    
            List<List<HashMap<String, String>>> routes = null;                       
            
            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();
                
                // Starts parsing data
                RouteInfo routeInfo = parser.parse(jObject);  
                routes = routeInfo.routes;
                distance = routeInfo.distance;
                duration = routeInfo.duration;
                startAddress = routeInfo.startAddress;
                endAddress = routeInfo.endAddress;
                
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
        
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            try {
            setAddressOnButton();
            
            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();
                
                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);
                
                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);                    
                    
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);    
                    
                    points.add(position);                        
                }
                
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(pixelToDp(30));
                lineOptions.color(Color.BLUE);    
                lineOptions.geodesic(true);
            }
            } catch(Exception e) {
    			Toast.makeText(MainActivity.this, "You should take a flight.", Toast.LENGTH_LONG).show();
    			Log.e(LOG_TAG, e.toString());
    		}
            
            // Drawing polyline in the Google Map for the i-th route
            try {
            	if(routeMap != null) {
            		routeMap.remove();
            	}
            	routeMap = map.addPolyline(lineOptions); 
            } catch (Exception e) {
            	Log.e(LOG_TAG + "Polygon Exception: ", e.toString());
            }
        }            
    }   
    
    public void setAddressOnButton() {
 
    	if(! startAddress.isEmpty()) {
    		from.setText(Html.fromHtml("<b>" + fromButtonText + "</b>" +  "<br/>" + 
    				"<small>" + startAddress + "</small>"));
    	}
    	if(! endAddress.isEmpty()) {
        	to.setText(Html.fromHtml("<b>" + toButtonText + "</b>" +  "<br/>" + 
        	        "<small>" + endAddress + "</small>"));
        }

    }
    
    private int dpToPixel(int dp) {
    	float d = this.getResources().getDisplayMetrics().density;
    	return (int) (dp * d);
    }
    private int pixelToDp(float pixel) {
    	float d = this.getResources().getDisplayMetrics().density;
    	return (int) (pixel / d);
    }
    
    private void showGPSDisabledAlert(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Enable GPS for quiker pickup location setting?")
        .setCancelable(false)
        .setPositiveButton("Enable GPS",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                Intent callGPSSettingIntent = new Intent(
                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(callGPSSettingIntent);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int id){
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    
    private String getLocation(String address) {
		for(int i = 0; i < NCR.length; i++) {
			if (startAddress.toLowerCase(Locale.US).contains(NCR[i])) {
				location_code = 1;
				tel_prefix = "+91 11";
				return "NCR";
			}
			
		}
		if (startAddress.toLowerCase(Locale.US).contains("mumbai")) {
			location_code = 2;
			tel_prefix = "+91 22";
			return "Mumbai";
		} else if (startAddress.toLowerCase(Locale.US).contains("hyderabad")) {
			location_code = 3;
			tel_prefix = "+91 40";
			return "Hyderabad";
		} else if ((startAddress.toLowerCase(Locale.US).contains("bengaluru"))) {
			location_code = 4;
			tel_prefix = "+91 80";
			return "Bengaluru";
			
		} else if ((startAddress.toLowerCase(Locale.US).contains("pune"))) {
			location_code = 5;
			tel_prefix = "+91 20";
			return "Pune";
		}
		return null;
    }
}

