package com.uma.umar;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by danieh on 6/26/17.
 */

public class UmARApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
    }
}
