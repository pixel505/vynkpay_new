
package com.vynkpay.retrofit.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppVersionResponse {

    @SerializedName("status")
    @Expose
    private Integer status;


    @SerializedName("isUpdate")
    @Expose
    private String isUpdate;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("input")
    @Expose
    private Input input;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(String isUpdate) {
        this.isUpdate = isUpdate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }


    public class Data {

        @SerializedName("version")
        @Expose
        private String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }


    }

    public class Input {

        @SerializedName("version")
        @Expose
        private String version;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    }
