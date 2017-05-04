package com.dev.peter.kolo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Handles everything related to our location.
 */

public class LocationHandler implements LocationListener {
    private final String TAG = "LocationHandler";
    private Location mBestLocation;
    private LocationRequest mLocationRequest;

    private GoogleApiClient mGoogleApiClient;
    private Context mActivityContext;

    /**
     * Constructs an location handler.
     * @param context context from the active activity
     * @param googleApiClient provided GoogleApiClient from the activity
     */
    public LocationHandler(Context context, GoogleApiClient googleApiClient) {
        mActivityContext = context;
        mGoogleApiClient = googleApiClient;
    }

    /**
     * Get the best possible Location right now.
     * @return our location
     */
    public Location getCurrent() {
        return mBestLocation;
    }

    /*private void createLocationRequest(int interval, int fastestInterval, int priorityConst) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastestInterval);
        mLocationRequest.setPriority(priorityConst);
    }*/

    private void getLastLocation(final SimpleCallback<Location> callback) {
        if (ActivityCompat.checkSelfPermission(mActivityContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ((PermissionRequester) mActivityContext).requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, new PermissionCallback() {
                @Override
                public void onGranted() {
                    getLastLocation(callback);
                }

                @Override
                public void onRejected() {
                    // TODO ?
                }
            });
        } else {
            Location cache = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (cache != null) {
                Log.v(TAG, "got last location");
                callback.onResult(cache);
            } else {
                Log.v(TAG, "got invalid last location");
                callback.onError();
            }
        }
    }

    private void startLocationUpdate() {
        if (ContextCompat.checkSelfPermission(mActivityContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ((PermissionRequester) mActivityContext).requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, new PermissionCallback() {
                @Override
                public void onGranted() {
                    // try again
                    startLocationUpdate();
                }

                @Override
                public void onRejected() {
                    // TODO what now?
                }
            });
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.v(TAG, "started new location request");
        }
    }

    public void onLocationChanged (Location location){
        mBestLocation = location;
    }

}