package com.vynkpay.models;

public class GenerationBonusDetailModel {
    String id;
    String user_id;
    String profit_type;
    String name;
    String username;
    String p_amount;
    String status;
    String created_date;
    String ref_name;
    String ref_username;
    String ref_email;
    String ref_phone;
    String from_level;

    public GenerationBonusDetailModel(String id, String user_id, String profit_type, String name, String username, String p_amount, String status, String created_date, String ref_name, String ref_username, String ref_email, String ref_phone, String from_level) {
        this.id = id;
        this.user_id = user_id;
        this.profit_type = profit_type;
        this.name = name;
        this.username = username;
        this.p_amount = p_amount;
        this.status = status;
        this.created_date = created_date;
        this.ref_name = ref_name;
        this.ref_username = ref_username;
        this.ref_email = ref_email;
        this.ref_phone = ref_phone;
        this.from_level = from_level;
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

    public String getProfit_type() {
        return profit_type;
    }

    public void setProfit_type(String profit_type) {
        this.profit_type = profit_type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getP_amount() {
        return p_amount;
    }

    public void setP_amount(String p_amount) {
        this.p_amount = p_amount;
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

    public String getRef_name() {
        return ref_name;
    }

    public void setRef_name(String ref_name) {
        this.ref_name = ref_name;
    }

    public String getRef_username() {
        return ref_username;
    }

    public void setRef_username(String ref_username) {
        this.ref_username = ref_username;
    }

    public String getRef_email() {
        return ref_email;
    }

    public void setRef_email(String ref_email) {
        this.ref_email = ref_email;
    }

    public String getRef_phone() {
        return ref_phone;
    }

    public void setRef_phone(String ref_phone) {
        this.ref_phone = ref_phone;
    }

    public String getFrom_level() {
        return from_level;
    }

    public void setFrom_level(String from_level) {
        this.from_level = from_level;
    }
}
