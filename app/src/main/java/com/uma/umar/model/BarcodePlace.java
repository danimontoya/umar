package com.uma.umar.model;

import com.google.gson.annotations.SerializedName;
import com.uma.umar.UmARApplication;

/**
 * Created by danieh on 7/5/17.
 */

public class BarcodePlace {

    @SerializedName("id")
    private String id;

    @SerializedName("name_es")
    private String name_es;

    @SerializedName("name_en")
    private String name_en;

    @SerializedName("description_es")
    private String description_es;

    @SerializedName("description_en")
    private String description_en;

    public BarcodePlace() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getDescription_es() {
        return description_es;
    }

    public void setDescription_es(String description_es) {
        this.description_es = description_es;
    }

    public String getDescription_en() {
        return description_en;
    }

    public void setDescription_en(String description_en) {
        this.description_en = description_en;
    }

    public String getName() {
        return UmARApplication.isEnglish() ? getName_en() : getName_es();
    }

    public String getDescription() {
        return UmARApplication.isEnglish() ? getDescription_en() : getDescription_es();
    }
}
