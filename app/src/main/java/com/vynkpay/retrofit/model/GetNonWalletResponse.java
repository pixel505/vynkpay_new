package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetNonWalletResponse {

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

        @SerializedName("vCash_wallet")
        @Expose
        private String vCashWallet;
        @SerializedName("bonus_wallet")
        @Expose
        private String bonusWallet;
        @SerializedName("mcash_wallet")
        @Expose
        private String mcashWallet;

        public String getVCashWallet() {
            return vCashWallet;
        }

        public void setVCashWallet(String vCashWallet) {
            this.vCashWallet = vCashWallet;
        }

        public String getBonusWallet() {
            return bonusWallet;
        }

        public void setBonusWallet(String bonusWallet) {
            this.bonusWallet = bonusWallet;
        }

        public String getMcashWallet() {
            return mcashWallet;
        }

        public void setMcashWallet(String mcashWallet) {
            this.mcashWallet = mcashWallet;
        }
    }
}
