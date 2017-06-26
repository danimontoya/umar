package com.uma.umar.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.uma.umar.R;

/**
 * Created by danieh on 6/26/17.
 */

public class SchoolViewHolder extends RecyclerView.ViewHolder {

    private final ImageView mImage;
    private final TextView mName;
    private final TextView mAddress;

    public SchoolViewHolder(View itemView) {
        super(itemView);
        mImage = (ImageView) itemView.findViewById(R.id.school_logo);
        mName = (TextView) itemView.findViewById(R.id.school_name);
        mAddress = (TextView) itemView.findViewById(R.id.school_address);
    }

    public void bind(School school){
        mName.setText(school.getName());
        mAddress.setText(school.getAddress());
    }
}
