package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReferLinkResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
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

        @SerializedName("refer_url")
        @Expose
        private String referUrl;
        @SerializedName("refer_page")
        @Expose
        private String referPage;

        public String getReferUrl() {
            return referUrl;
        }

        public void setReferUrl(String referUrl) {
            this.referUrl = referUrl;
        }

        public String getReferPage() {
            return referPage;
        }

        public void setReferPage(String referPage) {
            this.referPage = referPage;
        }

    }
}
