package com.vynkpay.activity.recharge.mobile.model;

public class ReferEarnSummaryModel {
    String id;
    String front_user_id;
    String user_id;
    String type;
    String payment_via;
    String p_amount;
    String profit_type;
    String mode;
    String status;
    String created_date;
    String username;
    String email;
    String phone;
    String name;
    String paid_status;
    String balance;
    String frontusername;
    String ref_username;
    String ref_name;
    String ref_email;
    String status_txt;

    public ReferEarnSummaryModel(String id, String front_user_id, String user_id, String type, String payment_via, String p_amount, String profit_type, String mode, String status, String created_date, String username, String email, String phone, String name, String paid_status, String balance, String frontusername, String ref_username, String ref_name, String ref_email, String status_txt) {
        this.id = id;
        this.front_user_id = front_user_id;
        this.user_id = user_id;
        this.type = type;
        this.payment_via = payment_via;
        this.p_amount = p_amount;
        this.profit_type = profit_type;
        this.mode = mode;
        this.status = status;
        this.created_date = created_date;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.paid_status = paid_status;
        this.balance = balance;
        this.frontusername = frontusername;
        this.ref_username = ref_username;
        this.ref_name = ref_name;
        this.ref_email = ref_email;
        this.status_txt = status_txt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFront_user_id() {
        return front_user_id;
    }

    public void setFront_user_id(String front_user_id) {
        this.front_user_id = front_user_id;
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

    public String getPayment_via() {
        return payment_via;
    }

    public void setPayment_via(String payment_via) {
        this.payment_via = payment_via;
    }

    public String getP_amount() {
        return p_amount;
    }

    public void setP_amount(String p_amount) {
        this.p_amount = p_amount;
    }

    public String getProfit_type() {
        return profit_type;
    }

    public void setProfit_type(String profit_type) {
        this.profit_type = profit_type;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaid_status() {
        return paid_status;
    }

    public void setPaid_status(String paid_status) {
        this.paid_status = paid_status;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFrontusername() {
        return frontusername;
    }

    public void setFrontusername(String frontusername) {
        this.frontusername = frontusername;
    }

    public String getRef_username() {
        return ref_username;
    }

    public void setRef_username(String ref_username) {
        this.ref_username = ref_username;
    }

    public String getRef_name() {
        return ref_name;
    }

    public void setRef_name(String ref_name) {
        this.ref_name = ref_name;
    }

    public String getRef_email() {
        return ref_email;
    }

    public void setRef_email(String ref_email) {
        this.ref_email = ref_email;
    }

    public String getStatus_txt() {
        return status_txt;
    }

    public void setStatus_txt(String status_txt) {
        this.status_txt = status_txt;
    }
}
