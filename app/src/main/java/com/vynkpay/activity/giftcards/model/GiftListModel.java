package com.vynkpay.activity.giftcards.model;

import java.util.ArrayList;

public class GiftListModel {
    private String categoryName;
    private String categoryId;
    private String categoryType;
    private String categoryMin;
    private String categoryMax;
    private String categoryCustomDenominations;
    private String image;
    private String city;
    private String country;
    private String price;
    private ArrayList<String> images;

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getCategoryMin() {
        return categoryMin;
    }

    public void setCategoryMin(String categoryMin) {
        this.categoryMin = categoryMin;
    }

    public String getCategoryMax() {
        return categoryMax;
    }

    public void setCategoryMax(String categoryMax) {
        this.categoryMax = categoryMax;
    }

    public String getCategoryCustomDenominations() {
        return categoryCustomDenominations;
    }

    public void setCategoryCustomDenominations(String categoryCustomDenominations) {
        this.categoryCustomDenominations = categoryCustomDenominations;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
