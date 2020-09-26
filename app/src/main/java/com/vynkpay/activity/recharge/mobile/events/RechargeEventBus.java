package com.vynkpay.activity.recharge.mobile.events;

public class RechargeEventBus {
    public  String operator;
    public  String circleName;
    public String circle_id;
    public RechargeEventBus(String operator , String circleName, String circle_id) {
        this.operator = operator;
        this.circleName = circleName;
        this.circle_id = circle_id;
    }

    public String getOperator() {
        return operator;
    }


    public String getCircleName() {
        return circleName;
    }

    public String getCircle_id() {
        return circle_id;
    }
}
