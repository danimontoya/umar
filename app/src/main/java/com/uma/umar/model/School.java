package com.uma.umar.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by danieh on 6/26/17.
 */

public class School implements Serializable {

    @SerializedName("name")
    String name;
    @SerializedName("address")
    String address;
    @SerializedName("phone")
    String phone;
    //List<String> images;
    //List<Profile> profiles;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
