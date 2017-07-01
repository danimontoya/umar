package com.uma.umar.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ntdat on 1/16/17.
 */

public class ARPoint implements Parcelable {

    private Location location;
    private String name;
    private String url;

    public ARPoint(String name, String url, double lat, double lon, double altitude) {
        this.name = name;
        this.url = url;
        location = new Location("ARPoint");
        location.setLatitude(lat);
        location.setLongitude(lon);
        location.setAltitude(altitude);
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    protected ARPoint(Parcel in) {
        location = (Location) in.readValue(Location.class.getClassLoader());
        name = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(location);
        dest.writeString(name);
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ARPoint> CREATOR = new Parcelable.Creator<ARPoint>() {
        @Override
        public ARPoint createFromParcel(Parcel in) {
            return new ARPoint(in);
        }

        @Override
        public ARPoint[] newArray(int size) {
            return new ARPoint[size];
        }
    };
}
