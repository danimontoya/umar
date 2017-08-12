package com.uma.umar;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.google.firebase.FirebaseApp;
import com.uma.umar.utils.FirebaseConstants;
import com.uma.umar.utils.UMALog;
import com.uma.umar.utils.UmARNetworkUtil;
import com.uma.umar.utils.UmARSharedPreferences;

import java.util.Locale;

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
        UmARNetworkUtil.init(this);
        FirebaseApp.initializeApp(this);

        String language = Locale.getDefault().getLanguage();

        // The first time there is nothing in the shared preferences
        if (UmARSharedPreferences.getLanguage() == null) {
            UmARSharedPreferences.setLanguage(FirebaseConstants.LANGUAGE_ES);
        }
        setLocale();

        UMALog.e("language", "Language Phone: " + language);
        UMALog.e("language", "Language App  : " + UmARSharedPreferences.getLanguage());
    }

    public static UmARApplication getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLocale();
    }

    public void setLocale() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources();
        }
        updateResourcesLegacy();
    }

    public Locale getLocale() {
        return new Locale(UmARSharedPreferences.getLanguage());
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void updateResources() {
        final Locale locale = getLocale();
        Locale.setDefault(locale);

        Configuration configuration = getResources().getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);
        createConfigurationContext(configuration);
    }

    @SuppressWarnings("deprecation")
    private void updateResourcesLegacy() {
        final Locale locale = getLocale();
        Locale.setDefault(locale);

        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static boolean isEnglish() {
        return FirebaseConstants.LANGUAGE_EN.equals(UmARApplication.getInstance().getLocale().getLanguage());
    }
}
