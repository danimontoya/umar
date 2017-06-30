package com.uma.umar.ui.schools.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uma.umar.R;
import com.uma.umar.model.School;
import com.uma.umar.ui.schools.listener.SchoolClickListener;

/**
 * Created by danieh on 6/26/17.
 */

public class SchoolViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mImage;
    private final TextView mName;
    private final TextView mAddress;
    private final View mView;

    private SchoolClickListener mListener;

    public SchoolViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mImage = (ImageView) itemView.findViewById(R.id.school_logo);
        mName = (TextView) itemView.findViewById(R.id.school_name);
        mAddress = (TextView) itemView.findViewById(R.id.school_address);
        mView.setOnClickListener(this);
    }

    public void bind(School school) {
        mName.setText(school.getName());
        mAddress.setText(school.getAddress());
        Picasso.with(mImage.getContext()).load(school.getLogo()).into(mImage);
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
