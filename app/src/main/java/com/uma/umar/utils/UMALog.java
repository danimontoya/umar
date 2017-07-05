package com.uma.umar.utils;

/**
 * Created by danieh on 7/5/17.
 */
public class UMALog {

    private static boolean enableLog = true;
    private static boolean enableErrorLog = true;

    public static void setLoggingEnabled(boolean b) {
        enableLog = b;
    }

    public static void d(String tag, String msg) {
        if (enableLog) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void d(String tag, String msg, Throwable tr) {
        if (enableLog) {
            android.util.Log.d(tag, msg, tr);
        }
    }

    public static void e(String tag, String msg) {
        if (enableErrorLog) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (enableErrorLog) {
            android.util.Log.e(tag, msg, tr);
        }
    }

    public static void i(String tag, String msg) {
        if (enableLog) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (enableLog) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (enableLog) {
            android.util.Log.w(tag, msg);
        }
    }

    public static void wtf(String tag, String msg) {
        if (enableLog) {
            android.util.Log.wtf(tag, msg);
        }
    }

}