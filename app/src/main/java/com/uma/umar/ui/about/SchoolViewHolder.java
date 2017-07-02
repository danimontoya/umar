package com.uma.umar.ui.about;

import android.view.View;
import android.widget.TextView;

import com.uma.umar.R;

/**
 * Created by danieh on 7/2/17.
 */

public class SchoolViewHolder {

    View root;
    TextView nameTextView;
    TextView descriptionTextView;
    TextView addressTextView;
    TextView directorTextView;
    TextView emailTextView;
    TextView phoneConciergeTextView;
    TextView phoneSecretaryTextView;

    public SchoolViewHolder(View view) {
        this.root = view;
        nameTextView = (TextView) view.findViewById(R.id.school_name_textview);
        descriptionTextView = (TextView) view.findViewById(R.id.description_textview);
        addressTextView = (TextView) view.findViewById(R.id.address_textview);
        directorTextView = (TextView) view.findViewById(R.id.director_textview);
        emailTextView = (TextView) view.findViewById(R.id.email_textview);
        phoneConciergeTextView = (TextView) view.findViewById(R.id.phone_concierge_textview);
        phoneSecretaryTextView = (TextView) view.findViewById(R.id.phone_secretary_textview);
    }

}
