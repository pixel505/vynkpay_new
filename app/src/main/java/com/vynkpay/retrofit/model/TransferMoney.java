package com.vynkpay.retrofit.model;

public class TransferMoney {


    /**
     * status : false
     * message : Minimum Wallet Transfer is : ₹100
     */

    private boolean status;
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
