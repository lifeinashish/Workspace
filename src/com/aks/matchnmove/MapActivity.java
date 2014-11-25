package com.aks.matchnmove;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MapActivity extends ActionBarActivity implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{
	
	private String LOG_TAG = "MapActivity";
	private final static int
    CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	
	public boolean checkPlayServices(Context mContext) {
		  int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(mContext);
		  if (status != ConnectionResult.SUCCESS) {
		    if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
		      showErrorDialog(status, (Activity) mContext);
		    } else {
		      Toast.makeText(mContext, "This device is not supported.", 
		          Toast.LENGTH_LONG).show();
		      finish();
		    }
		    return false;
		  }
		  Log.d("MapActivity", "Google play services available.");
		  return true;
		} 

		void showErrorDialog(int code, Activity mActivity) {
		  GooglePlayServicesUtil.getErrorDialog(code, mActivity, 
				  CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
		}

	
	@Override
    public void onConnected(Bundle dataBundle) {
	
	}
	
    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        // Display the connection status
        Toast.makeText(this, "Disconnected. Please re-connect.",
                Toast.LENGTH_SHORT).show();
    }
    
    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.e(LOG_TAG + "UserError", Integer.toString(connectionResult.getErrorCode()));
        }
    }

}
