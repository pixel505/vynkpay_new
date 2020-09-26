package com.vynkpay.models;

public class DirectRefModel {

    private String name;
    private String code;
    private String nameRyt;
    private String number;
    private String status;
    private String registerDate;
    private String paidDate;
    private String purchaseAmmount;

    public DirectRefModel(String name, String code, String nameRyt, String number, String status, String registerDate, String paidDate, String purchaseAmmount) {
        this.name = name;
        this.code = code;
        this.nameRyt = nameRyt;
        this.number = number;
        this.status = status;
        this.registerDate = registerDate;
        this.paidDate = paidDate;
        this.purchaseAmmount = purchaseAmmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNameRyt() {
        return nameRyt;
    }

    public void setNameRyt(String nameRyt) {
        this.nameRyt = nameRyt;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public String getPurchaseAmmount() {
        return purchaseAmmount;
    }

    public void setPurchaseAmmount(String purchaseAmmount) {
        this.purchaseAmmount = purchaseAmmount;
    }
}
