package com.dev.peter.kolo;

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
    private boolean highAccuracyMode;

    private Location mCurrentLocation;

    private LocationRequest mLocationRequest;
    private boolean mRequestingLocationUpdates;
    private SimpleCallback<Location> locationUpdateCallback;

    private GoogleApiClient mGoogleApiClient;
    private Context mApplicationContext;
    private PermissionRequester permissionRequester;

    /**
     * Constructs an location handler.
     * @param context the application context
     * @param permissionRequester current activity (which implements PermissionRequester)
     * @param googleApiClient provided GoogleApiClient from the activity
     * @param highAccuracyMode do you want high accuracy
     */
    public LocationHandler(Context context, PermissionRequester permissionRequester,
                           GoogleApiClient googleApiClient, boolean highAccuracyMode) {
        mApplicationContext = context.getApplicationContext();
        mGoogleApiClient = googleApiClient;
        this.permissionRequester = permissionRequester;

        this.highAccuracyMode = highAccuracyMode;
        // TODO more granular accuracy / update settings

        mRequestingLocationUpdates = false;
    }

    /**
     * This needs to be called when the used GoogleApiClient is connected.
     */
    public void onGoogleClientConnected() {
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * Get the best possible Location right now, without requesting the the permission.
     * @return our location
     */
    public Location getBestLocation() {
        return mCurrentLocation;
    }

    /**
     * Get one callback with a newly received location.
     * @param callback the callback interface to call
     */
    public void requestNewLocation(final SimpleCallback<Location> callback) {
        getLocationCallbacks(new SimpleCallback<Location>() {
            @Override
            public void onResult(Location data) {
                stopLocationUpdates();
                callback.onResult(data);
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    /**
     * Get a callback every time we get a location update.
     * Works only for one callback.
     * @param callback the callback
     */
    public void getLocationCallbacks(SimpleCallback<Location> callback) {
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            generateLocationRequest();
            locationUpdateCallback = callback;
            startLocationUpdates();
        }
    }

    /**
     * Stop the location update callbacks.
     */
    public void stopLocationCallbacks() {
        stopLocationUpdates();
    }

    private void generateLocationRequest() {
        int interval, fastestInterval, priorityConst;

        if (highAccuracyMode) {
            priorityConst = LocationRequest.PRIORITY_HIGH_ACCURACY;
            interval = 5000;
            fastestInterval = 5000;
        } else {
            priorityConst = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
            interval = 20000;
            fastestInterval = 10000;
        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(interval);
        mLocationRequest.setFastestInterval(fastestInterval);
        mLocationRequest.setPriority(priorityConst);
    }

    private void getLastLocation(final SimpleCallback<Location> callback) {
        if (ActivityCompat.checkSelfPermission(mApplicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionRequester.requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, new PermissionCallback() {
                @Override
                public void onGranted() {
                    getLastLocation(callback);
                }

                @Override
                public void onRejected() {
                    callback.onError();
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

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(mApplicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            permissionRequester.requestPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, new PermissionCallback() {
                @Override
                public void onGranted() {
                    // try again
                    startLocationUpdates();
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

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
        locationUpdateCallback = null;
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        // TODO last update time

        if (mRequestingLocationUpdates) {
            locationUpdateCallback.onResult(location);
        }
    }

    // TODO save state
}
