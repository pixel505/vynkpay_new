package com.vynkpay.models;

public class TransferMoneyModel {
    private String amount;
    private String date;
    private String paymentMethod;
    private String status;
    private String mobile;
    private String id;
    private String tcnId;
    private String sentOrRecieve;
    private String operator;
    private String conn_type;
    private String txn;
    private String provider;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public String getConn_type() {
        return conn_type;
    }

    public void setConn_type(String conn_type) {
        this.conn_type = conn_type;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getSentOrRecieve() {
        return sentOrRecieve;
    }

    public void setSentOrRecieve(String sentOrRecieve) {
        this.sentOrRecieve = sentOrRecieve;
    }

    public String getTcnId() {
        return tcnId;
    }

    public void setTcnId(String tcnId) {
        this.tcnId = tcnId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
