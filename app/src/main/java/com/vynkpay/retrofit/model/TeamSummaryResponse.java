package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TeamSummaryResponse {

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

        @SerializedName("paid_status")
        @Expose
        private String paidStatus;
        @SerializedName("user_phone")
        @Expose
        private String userPhone;
        @SerializedName("refid")
        @Expose
        private String refid;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("ref_by")
        @Expose
        private String refBy;
        @SerializedName("level")
        @Expose
        private String level;
        @SerializedName("register_date")
        @Expose
        private String registerDate;
        @SerializedName("total_purchase")
        @Expose
        private Object totalPurchase;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("designation")
        @Expose
        private Object designation;
        @SerializedName("referdby")
        @Expose
        private String referdby;
        @SerializedName("refer_phone")
        @Expose
        private String referPhone;
        @SerializedName("refusername")
        @Expose
        private String refusername;
        @SerializedName("package_price")
        @Expose
        private String packagePrice;
        @SerializedName("ttlamount")
        @Expose
        private String ttlamount;

        @SerializedName("user_country_code")
        @Expose
        private String user_country_code;



        public String getPaidStatus() {
            return paidStatus;
        }

        public String getUser_country_code() {
            return user_country_code;
        }

        public void setUser_country_code(String user_country_code) {
            this.user_country_code = user_country_code;
        }

        public void setPaidStatus(String paidStatus) {
            this.paidStatus = paidStatus;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getRefid() {
            return refid;
        }

        public void setRefid(String refid) {
            this.refid = refid;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getRefBy() {
            return refBy;
        }

        public void setRefBy(String refBy) {
            this.refBy = refBy;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getRegisterDate() {
            return registerDate;
        }

        public void setRegisterDate(String registerDate) {
            this.registerDate = registerDate;
        }

        public Object getTotalPurchase() {
            return totalPurchase;
        }

        public void setTotalPurchase(Object totalPurchase) {
            this.totalPurchase = totalPurchase;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getDesignation() {
            return designation;
        }

        public void setDesignation(Object designation) {
            this.designation = designation;
        }

        public String getReferdby() {
            return referdby;
        }

        public void setReferdby(String referdby) {
            this.referdby = referdby;
        }

        public String getReferPhone() {
            return referPhone;
        }

        public void setReferPhone(String referPhone) {
            this.referPhone = referPhone;
        }

        public String getRefusername() {
            return refusername;
        }

        public void setRefusername(String refusername) {
            this.refusername = refusername;
        }

        public String getPackagePrice() {
            return packagePrice;
        }

        public void setPackagePrice(String packagePrice) {
            this.packagePrice = packagePrice;
        }

        public String getTtlamount() {
            return ttlamount;
        }

        public void setTtlamount(String ttlamount) {
            this.ttlamount = ttlamount;
        }
    }

}
