package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetCountryResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("selected_code")
    @Expose
    private Integer selectedCode;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSelectedCode() {
        return selectedCode;
    }

    public void setSelectedCode(Integer selectedCode) {
        this.selectedCode = selectedCode;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("text")
        @Expose
        private String text;
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("std_code")
        @Expose
        private String stdCode;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStdCode() {
            return stdCode;
        }

        public void setStdCode(String stdCode) {
            this.stdCode = stdCode;
        }
    }
}
