package com.vynkpay.activity.recharge.landline.events;

public class EventBusLandline {
    public String operator;
    public String maxLimit;
    public String minLimit;
    public String label;
    public String id;
    public String outer;

    public String getOuter() {
        return outer;
    }



    public EventBusLandline(String outer,String operator, String maxLimit, String minLimit, String label , String id) {
        this.outer = outer;
        this.operator = operator;
        this.maxLimit = maxLimit;
        this.minLimit = minLimit;
        this.label = label;
        this.id = id;
    }

    public String getId() {
        return id;
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
