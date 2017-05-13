package com.dev.peter.kolo;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class DiscoverActivity extends AppCompatActivity
        implements TabLayout.OnTabSelectedListener {
    private static final String TAG = "DiscoverActivity";

    private Search search;
    MapFragment mapFragment;
    DiscoverListFragment listFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);

        // we're not being restored
        if (savedInstanceState == null) {
            // In case this activity was started with special instructions from an
            // Intent, pass the Intent's extras to the fragment as arguments
            //firstFragment.setArguments(getIntent().getExtras());

            // app  bar
            Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
            setSupportActionBar(myToolbar);

            // tabs
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_bar);
            // default tab
            tabLayout.addTab(tabLayout.newTab().setText(R.string.discover_list_tab_text), true);
            tabLayout.addTab(tabLayout.newTab().setText(R.string.discover_map_tab_text));
            tabLayout.addOnTabSelectedListener(this);

            mapFragment = new MapFragment();
            listFragment = new DiscoverListFragment();

            // TODO only a test
            search = new Search("test");
            ArrayList<POI> test = new ArrayList<>();
            test.add(new POI("one", new LatLng(0.0, 0.0), "red"));
            test.add(new POI("two", new LatLng(0.0, 0.0), "red"));
            search.setTestPois(test);
            listFragment.passSearch(search);

            // default tab
            switchToList();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Bundle search_bundle = new Bundle();
        search.onSaveInstance(search_bundle);
        outState.putBundle("Search", search_bundle);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()){
            case 0:
                switchToList();
                break;
            case 1:
                switchToMap();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void switchToMap() {
        Log.v(TAG, "switch to map");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mapFragment).commit();
    }

    private void switchToList() {
        Log.v(TAG, "switch to list");
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, listFragment).commit();
    }
}
