package com.uma.umar;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.uma.umar.utils.UMALog;

/**
 * Created by danieh on 6/26/17.
 */

public class UmARApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        UMALog.setLoggingEnabled(BuildConfig.DEBUG);
        FirebaseApp.initializeApp(this);
    }
}
