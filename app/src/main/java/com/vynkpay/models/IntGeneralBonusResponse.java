package com.vynkpay.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IntGeneralBonusResponse {

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
        private Object refUsername;
        @SerializedName("ref_name")
        @Expose
        private Object refName;
        @SerializedName("ref_email")
        @Expose
        private Object refEmail;
        @SerializedName("week_of_year")
        @Expose
        private String weekOfYear;
        @SerializedName("eamount")
        @Expose
        private Object eamount;
        @SerializedName("amount_of")
        @Expose
        private String amountOf;
        @SerializedName("other_detail")
        @Expose
        private String otherDetail;
        @SerializedName("from_level")
        @Expose
        private String fromLevel;
        @SerializedName("lvl_pkg_id")
        @Expose
        private String lvlPkgId;
        @SerializedName("typeofinvest")
        @Expose
        private String typeofinvest;
        @SerializedName("u_id")
        @Expose
        private String uId;
        @SerializedName("u_username")
        @Expose
        private String uUsername;
        @SerializedName("u_email")
        @Expose
        private String uEmail;
        @SerializedName("u_name")
        @Expose
        private String uName;
        @SerializedName("u_phone")
        @Expose
        private String uPhone;
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

        public Object getRefUsername() {
            return refUsername;
        }

        public void setRefUsername(Object refUsername) {
            this.refUsername = refUsername;
        }

        public Object getRefName() {
            return refName;
        }

        public void setRefName(Object refName) {
            this.refName = refName;
        }

        public Object getRefEmail() {
            return refEmail;
        }

        public void setRefEmail(Object refEmail) {
            this.refEmail = refEmail;
        }

        public String getWeekOfYear() {
            return weekOfYear;
        }

        public void setWeekOfYear(String weekOfYear) {
            this.weekOfYear = weekOfYear;
        }

        public Object getEamount() {
            return eamount;
        }

        public void setEamount(Object eamount) {
            this.eamount = eamount;
        }

        public String getAmountOf() {
            return amountOf;
        }

        public void setAmountOf(String amountOf) {
            this.amountOf = amountOf;
        }

        public String getOtherDetail() {
            return otherDetail;
        }

        public void setOtherDetail(String otherDetail) {
            this.otherDetail = otherDetail;
        }

        public String getFromLevel() {
            return fromLevel;
        }

        public void setFromLevel(String fromLevel) {
            this.fromLevel = fromLevel;
        }

        public String getLvlPkgId() {
            return lvlPkgId;
        }

        public void setLvlPkgId(String lvlPkgId) {
            this.lvlPkgId = lvlPkgId;
        }

        public String getTypeofinvest() {
            return typeofinvest;
        }

        public void setTypeofinvest(String typeofinvest) {
            this.typeofinvest = typeofinvest;
        }

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getUUsername() {
            return uUsername;
        }

        public void setUUsername(String uUsername) {
            this.uUsername = uUsername;
        }

        public String getUEmail() {
            return uEmail;
        }

        public void setUEmail(String uEmail) {
            this.uEmail = uEmail;
        }

        public String getUName() {
            return uName;
        }

        public void setUName(String uName) {
            this.uName = uName;
        }

        public String getUPhone() {
            return uPhone;
        }

        public void setUPhone(String uPhone) {
            this.uPhone = uPhone;
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
