package com.uma.umar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danieh on 7/5/17.
 */

public class BarcodePlace {

    @SerializedName("id")
    private String id;

    @SerializedName("place")
    private Place place;

    public BarcodePlace() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public String getName() {
        return place.getName();
    }

    public String getDescription() {
        StringBuilder builder = new StringBuilder("[");
        builder.append(place.getLatitude());
        builder.append(", ");
        builder.append(place.getLongitude());
        builder.append(", ");
        builder.append(place.getAltitude());
        builder.append("]");
        return builder.toString();
    }
}
