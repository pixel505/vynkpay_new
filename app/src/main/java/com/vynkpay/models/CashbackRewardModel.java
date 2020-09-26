package com.vynkpay.models;

public class CashbackRewardModel {
      String id;
      String user_id;
      String type;
      String amount;
      String amount_of;
      String profit_type;
      String status;
      String created_date;
      String username;
      String email;
      String phone;
      String full_name;
      String balance;
      String detail;

    public CashbackRewardModel(String id, String user_id, String type, String amount, String amount_of, String profit_type, String status, String created_date, String username, String email, String phone, String full_name, String balance, String detail) {
        this.id = id;
        this.user_id = user_id;
        this.type = type;
        this.amount = amount;
        this.amount_of = amount_of;
        this.profit_type = profit_type;
        this.status = status;
        this.created_date = created_date;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.full_name = full_name;
        this.balance = balance;
        this.detail = detail;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmount_of() {
        return amount_of;
    }

    public void setAmount_of(String amount_of) {
        this.amount_of = amount_of;
    }

    public String getProfit_type() {
        return profit_type;
    }

    public void setProfit_type(String profit_type) {
        this.profit_type = profit_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
