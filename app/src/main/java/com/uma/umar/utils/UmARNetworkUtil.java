package com.uma.umar.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

public class UmARNetworkUtil {

    private static ConnectivityManager connectivityManager;
    private static final List<NetworkAvailableListener> listeners = new ArrayList<NetworkAvailableListener>();

    private UmARNetworkUtil() {
    }

    public static void init(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        BroadcastReceiver receiver = new BroadcastReceiver() {
            private boolean lastIsNetworkAvailable = isNetworkAvailable();

            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = isNetworkAvailable();

                if (lastIsNetworkAvailable != isNetworkAvailable) {
                    lastIsNetworkAvailable = isNetworkAvailable;

                    fireListeners(isNetworkAvailable);
                }
            }
        };

        context.registerReceiver(receiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public static void addListener(NetworkAvailableListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public static void fireListener(NetworkAvailableListener listener) {
        if (isNetworkAvailable())
            listener.onNetworkAvailable();
        else
            listener.onNetworkUnavailable();
    }

    public static void removeListener(NetworkAvailableListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    protected static void fireListeners(boolean isNetworkAvailable) {
        NetworkAvailableListener[] listeners;

        synchronized (UmARNetworkUtil.listeners) {
            listeners = UmARNetworkUtil.listeners.toArray(new NetworkAvailableListener[0]);
        }

        for (NetworkAvailableListener listener : listeners) {
            if (isNetworkAvailable)
                listener.onNetworkAvailable();
            else
                listener.onNetworkUnavailable();
        }
    }

    public static Boolean isNetworkAvailable() {
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public interface NetworkAvailableListener {
        void onNetworkAvailable();

        void onNetworkUnavailable();
    }
}
