package com.uma.umar.ui.schools.adapter;

import android.support.annotation.LayoutRes;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.uma.umar.model.School;
import com.uma.umar.model.SchoolViewHolder;
import com.uma.umar.ui.schools.listener.SchoolsListener;

/**
 * Created by danieh on 6/27/17.
 */

public class SchoolsAdapter extends FirebaseRecyclerAdapter<School, SchoolViewHolder> {

    private SchoolsListener mListener;

    public SchoolsAdapter(SchoolsListener listener, Class<School> modelClass, @LayoutRes int modelLayout, Class<SchoolViewHolder> viewHolderClass, Query query) {
        super(modelClass, modelLayout, viewHolderClass, query);
        mListener = listener;
    }

    @Override
    protected void populateViewHolder(SchoolViewHolder schoolViewHolder, School school, int i) {
        schoolViewHolder.bind(school);
    }

    @Override
    public void onDataChanged() {
        mListener.onDataChanged();
    }
}
