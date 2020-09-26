package com.vynkpay.retrofit.model;

import java.util.List;

public class DeleteNotificationResponse {


    /**
     * success : true
     * data : []
     * message : Notification Deleted Successfully.
     */

    private boolean success;
    private String message;
    private List<?> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
