package com.uma.umar.model;

import com.google.gson.annotations.SerializedName;
import com.uma.umar.UmARApplication;

import java.util.HashMap;

/**
 * Created by danieh on 6/29/17.
 */

public class Place {

    @SerializedName("name_es")
    private String name_es;
    @SerializedName("name_en")
    private String name_en;
    @SerializedName("image")
    private String image;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("altitude")
    private double altitude;
    @SerializedName("floor")
    private int floor;
    @SerializedName("profiles")
    private HashMap<String, String> profiles;

    public Place() {
    }

    /**
     * [Name=" + placeSplit[1] + " , latitude=" + placeSplit[2] + " , longitude=" + placeSplit[3] + " , altitude=" + placeSplit[4] + " , image=" + placeSplit[5] + "]
     *
     * @param placeSplit
     */
    public Place(String[] placeSplit, int floor) {
        if (Character.isDigit(placeSplit[1].charAt(0))) {
            setName_en("Classroom " + placeSplit[1]);
            setName_es("Aula " + placeSplit[1]);
        } else {
            if (placeSplit[1].contains("Servicio")) {
                setName_es(placeSplit[1]);
                setName_en(placeSplit[1].replace("Servicio", "Toilet"));
            } else if (placeSplit[1].contains("Salon de Actos")) {
                setName_es(placeSplit[1]);
                setName_en(placeSplit[1].replace("Salon de Actos", "Assembly Hall"));
            } else {
                setName_es(placeSplit[1]);
                setName_en(placeSplit[1]);
            }

        }
        setLatitude(Double.parseDouble(placeSplit[2]));
        setLongitude(Double.parseDouble(placeSplit[3]));
        setAltitude(Double.parseDouble(placeSplit[4]));
        setImage(placeSplit[5]);
        setFloor(floor);
    }

    public String getName_es() {
        return name_es;
    }

    public void setName_es(String name_es) {
        this.name_es = name_es;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public String getName() {
        return UmARApplication.isEnglish() ? getName_en() : getName_es();
    }

    public HashMap<String, String> getProfiles() {
        return profiles;
    }

    public void setProfiles(HashMap<String, String> profiles) {
        this.profiles = profiles;
    }

    @Override
    public String toString() {
        return "[Name: " + getName() + ", Latitude: " + getLatitude() + ", Longitude: " + getLongitude() + ", Altitude: " + getAltitude() + ", Floor: " + getFloor() + ", Image: " + getImage() + "]";
    }
}
