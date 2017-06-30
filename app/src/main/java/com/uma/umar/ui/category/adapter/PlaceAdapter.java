package com.uma.umar.ui.category.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.uma.umar.model.Place;
import com.uma.umar.ui.schools.listener.SchoolClickListener;
import com.uma.umar.ui.schools.listener.SchoolsListener;

/**
 * Created by danieh on 6/27/17.
 */

public class PlaceAdapter extends FirebaseRecyclerAdapter<Place, PlaceViewHolder> implements SchoolClickListener {

    private SchoolsListener mListener;

    public PlaceAdapter(SchoolsListener listener, Class<Place> modelClass, @LayoutRes int modelLayout, Class<PlaceViewHolder> viewHolderClass, Query query) {
        super(modelClass, modelLayout, viewHolderClass, query);
        mListener = listener;
    }

    @Override
    protected void populateViewHolder(PlaceViewHolder placeViewHolder, Place place, int i) {
        placeViewHolder.bind(place);
    }

    @Override
    public PlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PlaceViewHolder placeViewHolder = super.onCreateViewHolder(parent, viewType);
        placeViewHolder.setOnClickListener(this);
        return placeViewHolder;
    }

    @Override
    public void onDataChanged() {
        mListener.onDataChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        mListener.onItemClick(view, position);
    }
}
