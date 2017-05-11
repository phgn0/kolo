package com.dev.peter.kolo;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DiscoverActivity extends AppCompatActivity {
    private Search search;
    MapFragment mapFragment;
    DiscoverListFragment listFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // we're not being restored
        if (savedInstanceState == null) {
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //firstFragment.setArguments(getIntent().getExtras());

            mapFragment = new MapFragment();
            listFragment = new DiscoverListFragment();

            // TODO only a test
            search = new Search("test");
            ArrayList<POI> test = new ArrayList<>();
            test.add(new POI("one", new LatLng(0.0, 0.0), "red"));
            test.add(new POI("two", new LatLng(0.0, 0.0), "red"));
            search.setTestPois(test);

            switchToList();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Bundle search_bundle = new Bundle();
        search.onSaveInstance(search_bundle);
        outState.putBundle("Search", search_bundle);
    }

    private void switchToMap() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, mapFragment).commit();
    }

    private void switchToList() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, listFragment).commit();
    }
}
