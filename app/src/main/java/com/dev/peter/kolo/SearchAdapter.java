package com.dev.peter.kolo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * An adapter to display a "Search" in a list.
 */

public class SearchAdapter extends BaseAdapter {
    private Search search;

    private LayoutInflater mInflater;

    public SearchAdapter(Context context, Search search) {
        this.search = search;

        //mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return search.getNumberOfResults();
    }

    @Override
    public Object getItem(int position) {
        return search.getResult(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View elementView = convertView;

        if (elementView == null) {
            elementView = mInflater.inflate(R.layout.discover_list_element, parent, false);
        }

        TextView title = (TextView) elementView.findViewById(R.id.title);
        TextView subtitle = (TextView) elementView.findViewById(R.id.subtitle);

        title.setText("title " + Integer.toString(position));
        subtitle.setText("sub");

        return elementView;
    }
}
