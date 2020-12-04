package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetUserPackageResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("points")
        @Expose
        private String points;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;

        @SerializedName("weekly_amount")
        @Expose
        private String weekly_amount;

        private String isClicked;

        /*{"id":"11","type":"1","title":"Beetle Affiliate","price":"100.00","points":"2","weekly_amount":"0.00","total_amount":"100.00"*/

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPoints() {
            return points;
        }

        public void setPoints(String points) {
            this.points = points;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getWeekly_amount() {
            return weekly_amount;
        }

        public void setWeekly_amount(String weekly_amount) {
            this.weekly_amount = weekly_amount;
        }

        public String getIsClicked() {
            return isClicked;
        }

        public void setIsClicked(String isClicked) {
            this.isClicked = isClicked;
        }
    }

}
