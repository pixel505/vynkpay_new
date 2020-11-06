package com.vynkpay.activity.shops.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Deal {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("cashbackPercentage")
    @Expose
    private String cashbackPercentage;
    @SerializedName("shoopingPoints")
    @Expose
    private String shoopingPoints;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("terms")
    @Expose
    private String terms;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("country_id")
    @Expose
    private String countryId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("isActive")
    @Expose
    private String isActive;
    @SerializedName("category_title")
    @Expose
    private String categoryTitle;
    @SerializedName("country_name")
    @Expose
    private String countryName;

    public Deal(String id, String title, String brand, String image, String path, String cashbackPercentage, String shoopingPoints, String description, String terms, String url, String countryId, String categoryId, String createdAt, String updatedAt, String isActive, String categoryTitle, String countryName) {
        this.id = id;
        this.title = title;
        this.brand = brand;
        this.image = image;
        this.path = path;
        this.cashbackPercentage = cashbackPercentage;
        this.shoopingPoints = shoopingPoints;
        this.description = description;
        this.terms = terms;
        this.url = url;
        this.countryId = countryId;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.categoryTitle = categoryTitle;
        this.countryName = countryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCashbackPercentage() {
        return cashbackPercentage;
    }

    public void setCashbackPercentage(String cashbackPercentage) {
        this.cashbackPercentage = cashbackPercentage;
    }

    public String getShoopingPoints() {
        return shoopingPoints;
    }

    public void setShoopingPoints(String shoopingPoints) {
        this.shoopingPoints = shoopingPoints;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

}