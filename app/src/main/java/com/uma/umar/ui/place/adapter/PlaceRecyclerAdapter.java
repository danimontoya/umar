package com.uma.umar.ui.place.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uma.umar.R;
import com.uma.umar.model.Place;
import com.uma.umar.ui.place.PlacesActivity;
import com.uma.umar.ui.place.listener.PlaceClickListener;
import com.uma.umar.ui.place.listener.PlacesListener;

import java.util.LinkedHashMap;

/**
 * Created by danieh on 8/22/17.
 */
public class PlaceRecyclerAdapter extends RecyclerView.Adapter<PlaceViewHolder> implements PlaceClickListener {

    private static final String TAG = "PlaceRecyclerAdapter";
    private LayoutInflater mInflater;
    private LinkedHashMap<String, Place> mPlacesMap;
    private String[] mKeys;

    private PlacesListener mListener;

    public PlaceRecyclerAdapter(PlacesActivity listener, Context context, LinkedHashMap<String, Place> places) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mPlacesMap = places;
        mKeys = mPlacesMap.keySet().toArray(new String[mPlacesMap.size()]);
        mListener = listener;
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.place_item_layout, parent, false);
        PlaceViewHolder placeViewHolder = new PlaceViewHolder(view);
        placeViewHolder.setOnClickListener(this);
        return placeViewHolder;
    }

    @Override
    public void onBindViewHolder(PlaceViewHolder holder, int position) {
        Place place = mPlacesMap.get(mKeys[position]);
        holder.bind(place);
    }

    @Override
    public int getItemCount() {
        return mPlacesMap.size();
    }

    @Override
    public void onItemClick(View view, int position) {
        mListener.onItemClick(mKeys[position], mPlacesMap.get(mKeys[position]));
    }
}
