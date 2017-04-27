package com.dev.peter.kolo;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Represents a specific poi search by the user.
 */

class Search {
    private String name;
    private ArrayList<POI> pois;
    private LatLng position;
    private float range;

    public Search(String name) {
        this.name = name;
    }

    public void setPrefs() {

    }

    /**
     * Query the search to the server.
     * @param backendCommunicationHandler a communication handler
     * @param position our position to search from
     */
    public void askServer(BackendCommunicationHandler backendCommunicationHandler, LatLng position) {
        this.position = position;
    }

    /** Asks the server if our local data is up to date. */
    public void refresh(BackendCommunicationHandler backendCommunicationHandler) {
        // TODO refresh search
    }

    public void restoreFromSave() {
        // TODO restore
    }
}
