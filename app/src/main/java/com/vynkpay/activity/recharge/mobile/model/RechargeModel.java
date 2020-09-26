package com.vynkpay.activity.recharge.mobile.model;

import java.util.ArrayList;

public class RechargeModel {
    private String operatorName;
    private String opratorImage;
    private String circleName;
    private String operatorId;
    private String circleId;
    private String isCricleActive;
    private String label;
    private String maxLimit;
    private String minLimit;
    private ArrayList<OperatorInnerModel> operatorInnerModelArrayList;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(String maxLimit) {
        this.maxLimit = maxLimit;
    }

    public String getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(String minLimit) {
        this.minLimit = minLimit;
    }

    public ArrayList<OperatorInnerModel> getOperatorInnerModelArrayList() {
        return operatorInnerModelArrayList;
    }

    public void setOperatorInnerModelArrayList(ArrayList<OperatorInnerModel> operatorInnerModelArrayList) {
        this.operatorInnerModelArrayList = operatorInnerModelArrayList;
    }

    public String getIsCricleActive() {
        return isCricleActive;
    }

    public void setIsCricleActive(String isCricleActive) {
        this.isCricleActive = isCricleActive;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getCircleId() {
        return circleId;
    }

    public void setCircleId(String circleId) {
        this.circleId = circleId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getOpratorImage() {
        return opratorImage;
    }

    public void setOpratorImage(String opratorImage) {
        this.opratorImage = opratorImage;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }
}
