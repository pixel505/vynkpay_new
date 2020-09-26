package com.vynkpay.events;

public class UpdateWalletTransferFields {
    public String amount;
    public String mobileNumber;

    public UpdateWalletTransferFields(String amount, String mobileNumber) {
        this.amount = amount;
        this.mobileNumber = mobileNumber;
    }

    public String getAmount() {
        return amount;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }
}
