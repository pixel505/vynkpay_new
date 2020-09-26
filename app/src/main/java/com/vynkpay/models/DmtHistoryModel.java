package com.vynkpay.models;

public class DmtHistoryModel {
    String amount;
    String charged_amt;
    String created_at;
    String status;

    public DmtHistoryModel(String amount, String charged_amt, String created_at, String status) {
        this.amount = amount;
        this.charged_amt = charged_amt;
        this.created_at = created_at;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCharged_amt() {
        return charged_amt;
    }

    public void setCharged_amt(String charged_amt) {
        this.charged_amt = charged_amt;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
