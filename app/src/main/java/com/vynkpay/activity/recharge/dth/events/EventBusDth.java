package com.vynkpay.activity.recharge.dth.events;

public class EventBusDth {
    public String operator;
    public String maxLimit;
    public String minLimit;
    public String label;

    public EventBusDth(String operator, String maxLimit, String minLimit, String label) {
        this.operator = operator;
        this.maxLimit = maxLimit;
        this.minLimit = minLimit;
        this.label = label;
    }

    public String getOperator() {
        return operator;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public String getMinLimit() {
        return minLimit;
    }

    public String getLabel() {
        return label;
    }
}
