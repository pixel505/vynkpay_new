package com.vynkpay.activity.recharge.electricity.model;

public class EventBusElectric {
     String id;
     String operator_id;
     String state;
     String division;
     String code;
     String status;

    public String getId() {
        return id;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public String getState() {
        return state;
    }

    public String getDivision() {
        return division;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public EventBusElectric(String id, String operator_id, String state, String division, String code, String status) {
        this.id = id;
        this.operator_id = operator_id;
        this.state = state;
        this.division = division;
        this.code = code;
        this.status = status;
    }
}
