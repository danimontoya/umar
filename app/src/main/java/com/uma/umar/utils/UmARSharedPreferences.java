package com.uma.umar.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.uma.umar.UmARApplication;

/**
 * Created by danieh on 7/20/17.
 */
public class UmARSharedPreferences {

    private UmARSharedPreferences() {
    }

    private static SharedPreferences getSharedPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(UmARApplication.getContext());
    }

    public static void setSchoolId(String schoolKey) {
        getSharedPrefs().edit().putString(FirebaseConstants.SCHOOL_ID, schoolKey).apply();
    }

    public static String getSchoolId() {
        return getSharedPrefs().getString(FirebaseConstants.SCHOOL_ID, null);
    }

    public static void setProfileId(String profileKey) {
        getSharedPrefs().edit().putString(FirebaseConstants.PROFILE_ID, profileKey).apply();
    }

    public static String getProfileId() {
        return getSharedPrefs().getString(FirebaseConstants.PROFILE_ID, null);
    }

    public static void setLanguage(String languageCode) {
        getSharedPrefs().edit().putString(FirebaseConstants.LANGUAGE, languageCode).apply();
    }

    public static String getLanguage() {
        return getSharedPrefs().getString(FirebaseConstants.LANGUAGE, null);
    }

    public static void setDistanceRadio(float distanceRadio) {
        getSharedPrefs().edit().putFloat(FirebaseConstants.PLACES_DISTANCE, distanceRadio).apply();
    }

    public static float getDistanceRadio() {
        return getSharedPrefs().getFloat(FirebaseConstants.PLACES_DISTANCE, -1f);
    }
}
