package com.vynkpay.activity.activities;

public class TokenHistoryModel {
    String id;
    String user_id;
    String token_amount;
    String trx_address;
    String status;
    String txnid;
    String other_data;
    String created_at;
    String updated_at;
    String status_txt;
    String tokenSymbol;

    public TokenHistoryModel(String tokenSymbol, String id, String user_id, String token_amount, String trx_address, String status, String txnid, String other_data, String created_at, String updated_at, String status_txt) {
        this.tokenSymbol = tokenSymbol;
        this.id = id;
        this.user_id = user_id;
        this.token_amount = token_amount;
        this.trx_address = trx_address;
        this.status = status;
        this.txnid = txnid;
        this.other_data = other_data;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status_txt = status_txt;
    }

    public String getTokenSymbol() {
        return tokenSymbol;
    }

    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken_amount() {
        return token_amount;
    }

    public void setToken_amount(String token_amount) {
        this.token_amount = token_amount;
    }

    public String getTrx_address() {
        return trx_address;
    }

    public void setTrx_address(String trx_address) {
        this.trx_address = trx_address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getOther_data() {
        return other_data;
    }

    public void setOther_data(String other_data) {
        this.other_data = other_data;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus_txt() {
        return status_txt;
    }

    public void setStatus_txt(String status_txt) {
        this.status_txt = status_txt;
    }
}
