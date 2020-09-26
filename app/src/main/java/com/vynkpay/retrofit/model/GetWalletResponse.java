package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetWalletResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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

        @SerializedName("balance")
        @Expose
        private String balance;
        @SerializedName("walletRedeem")
        @Expose
        private String walletRedeem;

        @SerializedName("earningBalance")
        @Expose
        private String earningBalance;

        public String getEarningBalance() {
            return earningBalance;
        }

        public void setEarningBalance(String earningBalance) {
            this.earningBalance = earningBalance;
        }

        @SerializedName("razorpikey")
        @Expose
        private String razorpikey;

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getWalletRedeem() {
            return walletRedeem;
        }

        public void setWalletRedeem(String walletRedeem) {
            this.walletRedeem = walletRedeem;
        }

        public String getRazorpikey() {
            return razorpikey;
        }

        public void setRazorpikey(String razorpikey) {
            this.razorpikey = razorpikey;
        }
    }


    }
