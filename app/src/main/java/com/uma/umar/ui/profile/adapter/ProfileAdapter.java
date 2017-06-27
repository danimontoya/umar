package com.uma.umar.ui.profile.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.uma.umar.model.Profile;
import com.uma.umar.ui.schools.listener.SchoolClickListener;
import com.uma.umar.ui.schools.listener.SchoolsListener;

/**
 * Created by danieh on 6/27/17.
 */

public class ProfileAdapter extends FirebaseRecyclerAdapter<Profile, ProfileViewHolder> implements SchoolClickListener {

    private SchoolsListener mListener;

    public ProfileAdapter(SchoolsListener listener, Class<Profile> modelClass, @LayoutRes int modelLayout, Class<ProfileViewHolder> viewHolderClass, Query query) {
        super(modelClass, modelLayout, viewHolderClass, query);
        mListener = listener;
    }

    @Override
    protected void populateViewHolder(ProfileViewHolder profileViewHolder, Profile profile, int i) {
        profileViewHolder.bind(profile);
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ProfileViewHolder profileViewHolder = super.onCreateViewHolder(parent, viewType);
        profileViewHolder.setOnClickListener(this);
        return profileViewHolder;
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
