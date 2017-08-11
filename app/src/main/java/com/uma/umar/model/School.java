package com.uma.umar.model;

import com.google.gson.annotations.SerializedName;
import com.uma.umar.UmARApplication;
import com.uma.umar.ui.dashboard.LanguageFragment;
import com.uma.umar.utils.FirebaseConstants;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danieh on 6/26/17.
 */

public class School implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("address")
    private String address;
    @SerializedName("description_en")
    private String description_en;
    @SerializedName("description_es")
    private String description_es;
    @SerializedName("director")
    private String director;
    @SerializedName("email")
    private String email;
    @SerializedName("phone_concierge")
    private String phone_concierge;
    @SerializedName("phone_secretary")
    private String phone_secretary;
    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;
    @SerializedName("logo")
    private String logo;
    @SerializedName("images")
    private List<ImageUrl> images;

    public School() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getDescription_es() {
        return description_es;
    }

    public void setDescription_es(String description_es) {
        this.description_es = description_es;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_concierge() {
        return phone_concierge;
    }

    public void setPhone_concierge(String phone_concierge) {
        this.phone_concierge = phone_concierge;
    }

    public String getPhone_secretary() {
        return phone_secretary;
    }

    public void setPhone_secretary(String phone_secretary) {
        this.phone_secretary = phone_secretary;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public List<ImageUrl> getImages() {
        return images;
    }

    public void setImages(List<ImageUrl> images) {
        this.images = images;
    }

    public String getDescription() {
        return UmARApplication.isEnglish() ? getDescription_en() : getDescription_es();
    }
}
