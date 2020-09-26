package com.vynkpay.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReddemAmountResponse {

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

        @SerializedName("paymentRedeemBalance")
        @Expose
        private String paymentRedeemBalance;
        @SerializedName("operator_detail_id")
        @Expose
        private String operatorDetailId;
        @SerializedName("pointsAmount")
        @Expose
        private String pointsAmount;
        @SerializedName("pointsRedeemed")
        @Expose
        private String pointsRedeemed;
        @SerializedName("recharge_amount")
        @Expose
        private String rechargeAmount;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;
        @SerializedName("redeem_show")
        @Expose
        private Integer redeemShow;

        public String getPaymentRedeemBalance() {
            return paymentRedeemBalance;
        }

        public void setPaymentRedeemBalance(String paymentRedeemBalance) {
            this.paymentRedeemBalance = paymentRedeemBalance;
        }

        public String getOperatorDetailId() {
            return operatorDetailId;
        }

        public void setOperatorDetailId(String operatorDetailId) {
            this.operatorDetailId = operatorDetailId;
        }

        public String getPointsAmount() {
            return pointsAmount;
        }

        public void setPointsAmount(String pointsAmount) {
            this.pointsAmount = pointsAmount;
        }

        public String getPointsRedeemed() {
            return pointsRedeemed;
        }

        public void setPointsRedeemed(String pointsRedeemed) {
            this.pointsRedeemed = pointsRedeemed;
        }

        public String getRechargeAmount() {
            return rechargeAmount;
        }

        public void setRechargeAmount(String rechargeAmount) {
            this.rechargeAmount = rechargeAmount;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public Integer getRedeemShow() {
            return redeemShow;
        }

        public void setRedeemShow(Integer redeemShow) {
            this.redeemShow = redeemShow;
        }
    }
}
