package com.vynkpay.activity;

public class ChainTransactionModel {
    String id;
    String user_id;
    String status;
    String price;
    String total_price;
    String coin_price;
    String total_coin;
    String invoice_number;
    String created_date;

    public ChainTransactionModel(String id, String user_id, String status, String price, String total_price, String coin_price, String total_coin, String invoice_number, String created_date) {
        this.id = id;
        this.user_id = user_id;
        this.status = status;
        this.price = price;
        this.total_price = total_price;
        this.coin_price = coin_price;
        this.total_coin = total_coin;
        this.invoice_number = invoice_number;
        this.created_date = created_date;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getCoin_price() {
        return coin_price;
    }

    public void setCoin_price(String coin_price) {
        this.coin_price = coin_price;
    }

    public String getTotal_coin() {
        return total_coin;
    }

    public void setTotal_coin(String total_coin) {
        this.total_coin = total_coin;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }
}
