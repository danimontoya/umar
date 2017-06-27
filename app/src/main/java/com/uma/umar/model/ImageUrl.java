package com.uma.umar.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by danieh on 6/27/17.
 */

public class ImageUrl {

    @SerializedName("url")
    private String url;

    public ImageUrl(){
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
