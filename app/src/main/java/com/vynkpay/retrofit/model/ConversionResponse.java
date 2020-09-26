package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConversionResponse {

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

        @SerializedName("minimum_amount")
        @Expose
        private String minimumAmount;
        @SerializedName("walletBalance")
        @Expose
        private String walletBalance;
        @SerializedName("listing")
        @Expose
        private List<Listing> listing = null;

        public String getMinimumAmount() {
            return minimumAmount;
        }

        public void setMinimumAmount(String minimumAmount) {
            this.minimumAmount = minimumAmount;
        }

        public String getWalletBalance() {
            return walletBalance;
        }

        public void setWalletBalance(String walletBalance) {
            this.walletBalance = walletBalance;
        }

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
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("payment_via")
            @Expose
            private String paymentVia;
            @SerializedName("p_amount")
            @Expose
            private String pAmount;
            @SerializedName("profit_type")
            @Expose
            private String profitType;
            @SerializedName("mode")
            @Expose
            private String mode;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("created_date")
            @Expose
            private String createdDate;
            @SerializedName("username")
            @Expose
            private String username;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("phone")
            @Expose
            private String phone;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("paid_status")
            @Expose
            private String paidStatus;
            @SerializedName("balance")
            @Expose
            private String balance;
            @SerializedName("frontusername")
            @Expose
            private Object frontusername;
            @SerializedName("amount_of")
            @Expose
            private String amountOf;
            @SerializedName("ditribution")
            @Expose
            private String ditribution;
            @SerializedName("tax")
            @Expose
            private String tax;
            @SerializedName("other_detail")
            @Expose
            private String otherDetail;

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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getPaymentVia() {
                return paymentVia;
            }

            public void setPaymentVia(String paymentVia) {
                this.paymentVia = paymentVia;
            }

            public String getPAmount() {
                return pAmount;
            }

            public void setPAmount(String pAmount) {
                this.pAmount = pAmount;
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

            public String getCreatedDate() {
                return createdDate;
            }

            public void setCreatedDate(String createdDate) {
                this.createdDate = createdDate;
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

            public String getPaidStatus() {
                return paidStatus;
            }

            public void setPaidStatus(String paidStatus) {
                this.paidStatus = paidStatus;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public Object getFrontusername() {
                return frontusername;
            }

            public void setFrontusername(Object frontusername) {
                this.frontusername = frontusername;
            }

            public String getAmountOf() {
                return amountOf;
            }

            public void setAmountOf(String amountOf) {
                this.amountOf = amountOf;
            }

            public String getDitribution() {
                return ditribution;
            }

            public void setDitribution(String ditribution) {
                this.ditribution = ditribution;
            }

            public String getTax() {
                return tax;
            }

            public void setTax(String tax) {
                this.tax = tax;
            }

            public String getOtherDetail() {
                return otherDetail;
            }

            public void setOtherDetail(String otherDetail) {
                this.otherDetail = otherDetail;
            }
        }
    }
        }
