package com.dev.peter.kolo;

import  android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Fragment that handles a map.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "MapFragment";

    private GoogleMap mMap;
    private MapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView =  inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) inflatedView.findViewById(R.id.nested_map);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        return inflatedView;
    }

    @Override
    public void onDestroyView (){
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mMapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mMapView.onLowMemory();
        super.onLowMemory();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public void showPOI(POI poi) {
        float color;
        if (poi.color.equals("red")) {
            color = BitmapDescriptorFactory.HUE_RED;
        } else {
            color = BitmapDescriptorFactory.HUE_BLUE;
        }
        showMarker(poi.getLocation(), poi.getTitle(), color);
    }

    private void showMarker(LatLng position, String name, float color) {
        mMap.addMarker(new MarkerOptions()
                .position(position)
                .title(name)
                .icon(BitmapDescriptorFactory.defaultMarker(color)));
    }

    private void zoomTo(LatLng center) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 13));
    }

    private void centerTo(LatLng center) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 13));
    }
}
