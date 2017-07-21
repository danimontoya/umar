package com.uma.umar.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.uma.umar.UmARApplication;

import java.util.UUID;

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
}
