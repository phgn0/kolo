package com.dev.peter.kolo;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

/**
 * Represents a single point of interest (e.g. a hotel).
 */

class POI {
    private String title;
    private String description;
    private LatLng location;

    public String color;

    public POI(String name, LatLng location, String color) {
        this.title = name;
        this.location = location;
        this.color = color;
        // TODO attribute store system
    }

    public String getTitle() {
        return title;
    }

    public LatLng getLocation() {
        return location;
    }
}
