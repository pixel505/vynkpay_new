package com.vynkpay.activity.shops.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("country_name")
    @Expose
    private String countryName;

    public CountryList(String id, String countryName) {
        this.id = id;
        this.countryName = countryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
}
