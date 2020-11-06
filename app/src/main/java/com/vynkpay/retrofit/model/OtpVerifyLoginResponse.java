package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OtpVerifyLoginResponse{

    @SerializedName("status")
    @Expose
    private String  status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("u_id")
        @Expose
        private String uId;
        @SerializedName("access_token")
        @Expose
        private String accessToken;
        @SerializedName("u_refid")
        @Expose
        private String uRefid;
        @SerializedName("u_email")
        @Expose
        private String uEmail;
        @SerializedName("u_bit_address")
        @Expose
        private String uBitAddress;
        @SerializedName("u_pan_number")
        @Expose
        private String uPanNumber;
        @SerializedName("u_aadhar_number")
        @Expose
        private String uAadharNumber;
        @SerializedName("u_fname")
        @Expose
        private String uFname;
        @SerializedName("u_designation")
        @Expose
        private String uDesignation;
        @SerializedName("userpin")
        @Expose
        private String userpin;
        @SerializedName("logged_in")
        @Expose
        private Boolean loggedIn;
        @SerializedName("logged_in_pin")
        @Expose
        private Boolean loggedInPin;
        @SerializedName("isindian")
        @Expose
        private String isindian;

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getURefid() {
            return uRefid;
        }

        public void setURefid(String uRefid) {
            this.uRefid = uRefid;
        }

        public String getUEmail() {
            return uEmail;
        }

        public void setUEmail(String uEmail) {
            this.uEmail = uEmail;
        }

        public String getUBitAddress() {
            return uBitAddress;
        }

        public void setUBitAddress(String uBitAddress) {
            this.uBitAddress = uBitAddress;
        }

        public String getUPanNumber() {
            return uPanNumber;
        }

        public void setUPanNumber(String uPanNumber) {
            this.uPanNumber = uPanNumber;
        }

        public String getUAadharNumber() {
            return uAadharNumber;
        }

        public void setUAadharNumber(String uAadharNumber) {
            this.uAadharNumber = uAadharNumber;
        }

        public String getUFname() {
            return uFname;
        }

        public void setUFname(String uFname) {
            this.uFname = uFname;
        }

        public String getUDesignation() {
            return uDesignation;
        }

        public void setUDesignation(String uDesignation) {
            this.uDesignation = uDesignation;
        }

        public String getUserpin() {
            return userpin;
        }

        public void setUserpin(String userpin) {
            this.userpin = userpin;
        }

        public Boolean getLoggedIn() {
            return loggedIn;
        }

        public void setLoggedIn(Boolean loggedIn) {
            this.loggedIn = loggedIn;
        }

        public Boolean getLoggedInPin() {
            return loggedInPin;
        }

        public void setLoggedInPin(Boolean loggedInPin) {
            this.loggedInPin = loggedInPin;
        }

        public String getIsindian() {
            return isindian;
        }

        public void setIsindian(String isindian) {
            this.isindian = isindian;
        }

    }


}
