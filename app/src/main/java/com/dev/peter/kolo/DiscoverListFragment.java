package com.dev.peter.kolo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Displays a sortable list with search results.
 */

public class DiscoverListFragment extends Fragment{
    private static final String TAG = "DiscoverListFragment";
    private ListView mListView;

    private Search search;

    public DiscoverListFragment() {

    }

    public void passSearch(Search search) {
        this.search = search;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(TAG, "create view");
        if (search != null) {
            View fragmentView = inflater.inflate(R.layout.fragment_dicover_list, container, false);

            ListView listView = (ListView) fragmentView.findViewById(R.id.discover_list_view);
            SearchAdapter adapter = new SearchAdapter(getContext(), search);
            listView.setAdapter(adapter);

            return fragmentView;
        } else {
            // no search results yet
            Log.d(TAG, "no search results");

            View fragmentView = inflater.inflate(R.layout.fragment_dicover_list, container, false);

            return fragmentView;
        }
    }
}
