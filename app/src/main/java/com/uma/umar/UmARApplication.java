package com.uma.umar;

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.uma.umar.utils.UMALog;

/**
 * Created by danieh on 6/26/17.
 */

public class UmARApplication extends Application {

    private static UmARApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        UMALog.setLoggingEnabled(BuildConfig.DEBUG);
        FirebaseApp.initializeApp(this);
    }

    public static UmARApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }
}
