package com.uma.umar.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.uma.umar.ui.dashboard.DashboardActivity;
import com.uma.umar.ui.profile.ProfileActivity;
import com.uma.umar.ui.schools.SchoolsActivity;
import com.uma.umar.utils.UmARSharedPreferences;

/**
 * Created by danieh on 7/20/17.
 * This activity is meant to be the starting point of the app and will take one flow or another
 * depending if its the first time or the user already has selected a school and a profile.
 */

public class UmARInitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (UmARSharedPreferences.getSchoolId() == null) {
            SchoolsActivity.startActivity(this);

        } else if (UmARSharedPreferences.getProfileId() == null) {
            ProfileActivity.startActivity(this, UmARSharedPreferences.getSchoolId());

        } else {
            DashboardActivity.startActivity(this);
        }
        finish();
    }
}
