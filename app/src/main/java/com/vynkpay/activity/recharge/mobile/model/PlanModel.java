package com.vynkpay.activity.recharge.mobile.model;

public class PlanModel {
    private String id;
    private String operator_id ;
    private String circle_id;
    private String recharge_amount;
    private String recharge_talktime ;
    private String recharge_validity ;
    private String recharge_long_desc ;
    private String recharge_type;
    private String business_rule;

    public String getBusiness_rule() {
        return business_rule;
    }

    public void setBusiness_rule(String business_rule) {
        this.business_rule = business_rule;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public String getCircle_id() {
        return circle_id;
    }

    public void setCircle_id(String circle_id) {
        this.circle_id = circle_id;
    }

    public String getRecharge_amount() {
        return recharge_amount;
    }

    public void setRecharge_amount(String recharge_amount) {
        this.recharge_amount = recharge_amount;
    }

    public String getRecharge_talktime() {
        return recharge_talktime;
    }

    public void setRecharge_talktime(String recharge_talktime) {
        this.recharge_talktime = recharge_talktime;
    }

    public String getRecharge_validity() {
        return recharge_validity;
    }

    public void setRecharge_validity(String recharge_validity) {
        this.recharge_validity = recharge_validity;
    }

    public String getRecharge_long_desc() {
        return recharge_long_desc;
    }

    public void setRecharge_long_desc(String recharge_long_desc) {
        this.recharge_long_desc = recharge_long_desc;
    }

    public String getRecharge_type() {
        return recharge_type;
    }

    public void setRecharge_type(String recharge_type) {
        this.recharge_type = recharge_type;
    }
}
