package com.uma.umar.ui.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.uma.umar.R;

public class DashboardActivity extends AppCompatActivity {

    public static void startActivity(Activity activity){
        Intent intent = new Intent(activity, DashboardActivity.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_from_right, R.anim.stay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }
}
