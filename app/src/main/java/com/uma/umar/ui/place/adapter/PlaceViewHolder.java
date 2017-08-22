package com.uma.umar.ui.place.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uma.umar.R;
import com.uma.umar.model.Place;
import com.uma.umar.ui.place.listener.PlaceClickListener;
import com.uma.umar.ui.schools.listener.SchoolClickListener;

/**
 * Created by danieh on 6/27/17.
 */

public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mImage;
    private final TextView mName;
    private final TextView mLocation;
    private final View mView;

    private PlaceClickListener mListener;

    public PlaceViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mImage = (ImageView) itemView.findViewById(R.id.place_image);
        mName = (TextView) itemView.findViewById(R.id.place_name);
        mLocation = (TextView) itemView.findViewById(R.id.place_location);
        mView.setOnClickListener(this);
    }

    public void bind(Place place) {
        mName.setText(place.getName());
        StringBuilder builder = new StringBuilder("[");
        builder.append(place.getLatitude());
        builder.append(", ");
        builder.append(place.getLongitude());
        builder.append(", ");
        builder.append(place.getAltitude());
        builder.append("]");
        mLocation.setText(builder.toString());
        Picasso.with(mImage.getContext()).load(place.getImage()).into(mImage);
    }

    public void setOnClickListener(PlaceClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null)
            mListener.onItemClick(view, getAdapterPosition());
    }
}
