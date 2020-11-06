package com.vynkpay.activity.shops.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Categorys {

    //Category

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("total")
    @Expose
    private String total;

    public Categorys(String id, String title, String total) {
        this.id = id;
        this.title = title;
        this.total = total;
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

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

}
