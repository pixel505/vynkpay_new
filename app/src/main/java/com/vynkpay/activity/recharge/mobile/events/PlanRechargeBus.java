package com.vynkpay.activity.recharge.mobile.events;

public class PlanRechargeBus {
    public String talkTime;
    public String plainAmount;
    public String type;
    public String validity;

    public PlanRechargeBus(String talkTime, String plainAmount, String type, String validity) {
        this.talkTime = talkTime;
        this.plainAmount = plainAmount;
        this.type = type;
        this.validity = validity;
    }

    public String getTalkTime() {
        return talkTime;
    }

    public String getValidity() {
        return validity;
    }


    public String getPlainAmount() {
        return plainAmount;
    }

    public String getType() {
        return type;
    }
}
