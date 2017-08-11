package com.uma.umar.ui.profile.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uma.umar.R;
import com.uma.umar.model.Profile;
import com.uma.umar.ui.schools.listener.SchoolClickListener;

/**
 * Created by danieh on 6/27/17.
 */

public class ProfileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mImage;
    private final TextView mName;
    private final View mView;

    private SchoolClickListener mListener;

    public ProfileViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mImage = (ImageView) itemView.findViewById(R.id.profile_imageview);
        mName = (TextView) itemView.findViewById(R.id.profile_textview);
        mView.setOnClickListener(this);
    }

    public void bind(Profile profile) {
        mName.setText(profile.getName());
        Picasso.with(mImage.getContext()).load(profile.getImage()).into(mImage);
    }

    public void setOnClickListener(SchoolClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null)
            mListener.onItemClick(view, getAdapterPosition());
    }
}
