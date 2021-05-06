package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReferalsResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("paid_status")
        @Expose
        private String paidStatus;
        @SerializedName("level")
        @Expose
        private String level;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("leg")
        @Expose
        private String leg;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("created_date")
        @Expose
        private String createdDate;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("designation")
        @Expose
        private String designation;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("purchase_create_date")
        @Expose
        private String purchaseCreateDate;
        @SerializedName("package_price")
        @Expose
        private String packagePrice;
        @SerializedName("country_code")
        @Expose
        private String countryCode;
        @SerializedName("mobile_code")
        @Expose
        private String mobileCode;

        public Datum(String id, String phone, String paidStatus, String level, String username, String leg, String email, String createdDate, String name, String designation, String amount, String purchaseCreateDate, String packagePrice, String countryCode, String mobileCode) {
            this.id = id;
            this.phone = phone;
            this.paidStatus = paidStatus;
            this.level = level;
            this.username = username;
            this.leg = leg;
            this.email = email;
            this.createdDate = createdDate;
            this.name = name;
            this.designation = designation;
            this.amount = amount;
            this.purchaseCreateDate = purchaseCreateDate;
            this.packagePrice = packagePrice;
            this.countryCode = countryCode;
            this.mobileCode = mobileCode;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPaidStatus() {
            return paidStatus;
        }

        public void setPaidStatus(String paidStatus) {
            this.paidStatus = paidStatus;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getLeg() {
            return leg;
        }

        public void setLeg(String leg) {
            this.leg = leg;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPurchaseCreateDate() {
            return purchaseCreateDate;
        }

        public void setPurchaseCreateDate(String purchaseCreateDate) {
            this.purchaseCreateDate = purchaseCreateDate;
        }

        public String getPackagePrice() {
            return packagePrice;
        }

        public void setPackagePrice(String packagePrice) {
            this.packagePrice = packagePrice;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getMobileCode() {
            return mobileCode;
        }

        public void setMobileCode(String mobileCode) {
            this.mobileCode = mobileCode;
        }
    }
    }
