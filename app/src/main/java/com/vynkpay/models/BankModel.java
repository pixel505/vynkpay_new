package com.vynkpay.models;

public class BankModel {
    private String bankName;
    private String bankAccountNumber;

    private String historyClubTime;
    private String historyTitle;
    private String historyTime;
    private String historyAddOrDeleted;


    private String accountNumber;
    private String accountHolderName;
    private String ifsc;
    private String status;
    private String beneficiary_id;
    private String mobile;

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBeneficiary_id() {
        return beneficiary_id;
    }

    public void setBeneficiary_id(String beneficiary_id) {
        this.beneficiary_id = beneficiary_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getHistoryClubTime() {
        return historyClubTime;
    }

    public void setHistoryClubTime(String historyClubTime) {
        this.historyClubTime = historyClubTime;
    }

    public String getHistoryTitle() {
        return historyTitle;
    }

    public void setHistoryTitle(String historyTitle) {
        this.historyTitle = historyTitle;
    }

    public String getHistoryTime() {
        return historyTime;
    }

    public void setHistoryTime(String historyTime) {
        this.historyTime = historyTime;
    }

    public String getHistoryAddOrDeleted() {
        return historyAddOrDeleted;
    }

    public void setHistoryAddOrDeleted(String historyAddOrDeleted) {
        this.historyAddOrDeleted = historyAddOrDeleted;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }
}
