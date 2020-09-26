package com.vynkpay.models;

public class BannerModel {
    String id;
    String banner_image;
    String path;
    String banner_type;
    String created_date;
    String full_banner_image;

    public BannerModel(String id, String banner_image, String path, String banner_type, String created_date, String full_banner_image) {
        this.id = id;
        this.banner_image = banner_image;
        this.path = path;
        this.banner_type = banner_type;
        this.created_date = created_date;
        this.full_banner_image = full_banner_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBanner_type() {
        return banner_type;
    }

    public void setBanner_type(String banner_type) {
        this.banner_type = banner_type;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getFull_banner_image() {
        return full_banner_image;
    }

    public void setFull_banner_image(String full_banner_image) {
        this.full_banner_image = full_banner_image;
    }
}
