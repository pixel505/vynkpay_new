package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenerationBonusResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Data {

        @SerializedName("listing")
        @Expose
        private List<Listing> listing = null;

        public List<Listing> getListing() {
            return listing;
        }

        public void setListing(List<Listing> listing) {
            this.listing = listing;
        }

        public class Listing {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("front_user_id")
            @Expose
            private String frontUserId;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("profit_type")
            @Expose
            private String profitType;
            @SerializedName("mode")
            @Expose
            private String mode;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("level")
            @Expose
            private String level;
            @SerializedName("currency")
            @Expose
            private String currency;
            @SerializedName("phone")
            @Expose
            private String phone;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("created_date")
            @Expose
            private String createdDate;
            @SerializedName("ref_username")
            @Expose
            private String refUsername;
            @SerializedName("ref_name")
            @Expose
            private String refName;
            @SerializedName("ref_email")
            @Expose
            private String refEmail;
            @SerializedName("week_of_year")
            @Expose
            private String weekOfYear;
            @SerializedName("eamount")
            @Expose
            private Integer eamount;
            @SerializedName("amount_of")
            @Expose
            private String amountOf;
            @SerializedName("p_amount")
            @Expose
            private String pAmount;
            @SerializedName("ref_level")
            @Expose
            private Object refLevel;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFrontUserId() {
                return frontUserId;
            }

            public void setFrontUserId(String frontUserId) {
                this.frontUserId = frontUserId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getProfitType() {
                return profitType;
            }

            public void setProfitType(String profitType) {
                this.profitType = profitType;
            }

            public String getMode() {
                return mode;
            }

            public void setMode(String mode) {
                this.mode = mode;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getLevel() {
                return level;
            }

            public void setLevel(String level) {
                this.level = level;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCreatedDate() {
                return createdDate;
            }

            public void setCreatedDate(String createdDate) {
                this.createdDate = createdDate;
            }

            public String getRefUsername() {
                return refUsername;
            }

            public void setRefUsername(String refUsername) {
                this.refUsername = refUsername;
            }

            public String getRefName() {
                return refName;
            }

            public void setRefName(String refName) {
                this.refName = refName;
            }

            public String getRefEmail() {
                return refEmail;
            }

            public void setRefEmail(String refEmail) {
                this.refEmail = refEmail;
            }

            public String getWeekOfYear() {
                return weekOfYear;
            }

            public void setWeekOfYear(String weekOfYear) {
                this.weekOfYear = weekOfYear;
            }

            public Integer getEamount() {
                return eamount;
            }

            public void setEamount(Integer eamount) {
                this.eamount = eamount;
            }

            public String getAmountOf() {
                return amountOf;
            }

            public void setAmountOf(String amountOf) {
                this.amountOf = amountOf;
            }

            public String getPAmount() {
                return pAmount;
            }

            public void setPAmount(String pAmount) {
                this.pAmount = pAmount;
            }

            public Object getRefLevel() {
                return refLevel;
            }

            public void setRefLevel(Object refLevel) {
                this.refLevel = refLevel;
            }

        }
    }



    }
