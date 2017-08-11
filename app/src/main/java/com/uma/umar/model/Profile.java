package com.uma.umar.model;

import com.google.gson.annotations.SerializedName;
import com.uma.umar.UmARApplication;

/**
 * Created by danieh on 6/27/17.
 */

public class Profile {

    @SerializedName("name_es")
    private String name_es;
    @SerializedName("name_en")
    private String name_en;
    @SerializedName("image")
    private String image;

    public Profile() {
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

    public String getName() {
        return UmARApplication.isEnglish() ? getName_en() : getName_es();
    }
}
