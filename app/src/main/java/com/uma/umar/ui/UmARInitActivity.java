package com.uma.umar.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.uma.umar.R;
import com.uma.umar.ui.dashboard.DashboardActivity;
import com.uma.umar.ui.profile.ProfileActivity;
import com.uma.umar.ui.schools.SchoolsActivity;
import com.uma.umar.utils.UmARNetworkUtil;
import com.uma.umar.utils.UmARSharedPreferences;

/**
 * Created by danieh on 7/20/17.
 * This activity is meant to be the starting point of the app and will take one flow or another
 * depending if its the first time or the user already has selected a school and a profile.
 */

public class UmARInitActivity extends BaseActivity {

    public static void restartApp(Activity activity) {
        UmARSharedPreferences.setSchoolId(null);
        Intent intent = new Intent(activity, UmARInitActivity.class);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!UmARNetworkUtil.isNetworkAvailable()) {
            if (UmARSharedPreferences.getSchoolId() != null && UmARSharedPreferences.getProfileId() != null) {
                DashboardActivity.startActivity(this);
                finish();
            } else {
                showDialogNoInternet();
            }
        } else {
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

    @Override
    public void showDialogNoInternet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_name)
                .setMessage(R.string.need_internet)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();
    }
}
