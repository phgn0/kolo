package com.dev.peter.kolo;

import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Handles the communication with the server api.
 */

class BackendCommunicationHandler {
    private final String TAG = "BackendHandler";

    private RequestQueue requestQueue;
    private String idToken;

    private final String API_URL = "https://hotels-sw3g.c9users.io/api";

    public BackendCommunicationHandler(RequestQueue requestQueue, String idToken) {
        this.requestQueue = requestQueue;
        this.idToken = idToken;
    }

    /**
     * Send our search to the server and get fitting pois as callback.
     * @param search the search we want to execute
     * @param location our current location
     * @param callback interface to handle poi result and error
     */
    public void sendSearch(Search search, Location location, final CallbackInterface<ArrayList<POI>> callback) {
        final String API_SUB_URL_SEARCH = "/top";

        try {
            JSONObject location_json = new JSONObject();
            location_json.put("lat", location.getLatitude());
            location_json.put("lng", location.getLongitude());

            JSONObject request_body = new JSONObject();
            request_body.put("location", location_json);
            request_body.put("token", idToken);

            postRequest(API_URL + API_SUB_URL_SEARCH, request_body, new CallbackInterface<JSONObject>() {
                @Override
                public void onResult(JSONObject data) {
                    try {
                        ArrayList<POI> list = parseReceivedPoiList(data.getJSONArray("data"));
                        if (list == null) {
                            callback.onError();
                        } else {
                            callback.onResult(list);
                        }
                        // TODO implement health attributes
                    } catch (JSONException e) {
                        Log.d(TAG, "search response parsing failed");
                        e.printStackTrace();
                        callback.onError();
                    }
                }

                @Override
                public void onError() {
                    callback.onError();
                }
            });
        } catch (JSONException e) {
            Log.d(TAG, "sendSearch json error");
            e.printStackTrace();
            callback.onError();
        }
    }

    @Nullable
    private ArrayList<POI> parseReceivedPoiList(JSONArray list) {
        if (list == null) {
            Log.d(TAG, "got empty list im parseReceivedPoiList");
            return null;
        }
        ArrayList<POI> poiList = new ArrayList<>(list.length());
        try {
            for (int i = 0; i < list.length(); i++) {
                JSONObject element = list.getJSONObject(i);
                LatLng position = new LatLng(element.getDouble("geo_lat"),
                        element.getDouble("geo_lng"));

                String color;
                if (element.getInt("rot") == 1) {
                    color = "red";
                } else if (element.getInt("blau") == 1) {
                    color = "blue";
                } else {
                    color = "";
                }

                String name = element.getString("name");

               poiList.add(new POI(name, position, color));
            }
        } catch (JSONException e) {
            Log.d(TAG, "got invalid list");
            return null;
        }
        return poiList;
    }

    private void getRequest(String request_url, final CallbackInterface<JSONObject> callback) {
        // Request a string response from the provided URL with volley
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, request_url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onResult(response);
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError();
                        // TODO implement specific error codes
                    }
                });
        requestQueue.add(jsObjRequest);
    }

    private void postRequest(String request_url, JSONObject body,
                             final CallbackInterface<JSONObject> callback) {
        // Request a string response from the provided URL with volley
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, request_url, body, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onResult(response);
                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError();
                        // TODO implement specific volley error codes
                    }
                });
        requestQueue.add(jsObjRequest);
    }
}
