package com.uma.umar;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.FirebaseApp;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.uma.umar.utils.FirebaseConstants;
import com.uma.umar.utils.UMALog;
import com.uma.umar.utils.UmARNetworkUtil;
import com.uma.umar.utils.UmARSharedPreferences;

import io.fabric.sdk.android.Fabric;
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

        Fabric.with(this, new Crashlytics());
        UMALog.setLoggingEnabled(BuildConfig.DEBUG);
        UmARNetworkUtil.init(this);
        FirebaseApp.initializeApp(this);
        setupImageManager();

        String language = Locale.getDefault().getLanguage();

        // The first time there is nothing in the shared preferences
        if (UmARSharedPreferences.getLanguage() == null) {
            UmARSharedPreferences.setLanguage(FirebaseConstants.LANGUAGE_ES.equals(language) ?
                    FirebaseConstants.LANGUAGE_ES : FirebaseConstants.LANGUAGE_EN);
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
        UmARApplication instance = UmARApplication.getInstance();
        if (instance == null)
            return true;
        String language = instance.getLocale().getLanguage();
        return FirebaseConstants.LANGUAGE_EN.equals(language);
    }

    private void setupImageManager() {
        Picasso.Builder builder = new Picasso.Builder(this);
        LruCache lruCache = new LruCache(this);
        builder.memoryCache(lruCache);
        builder.build();
    }
}
