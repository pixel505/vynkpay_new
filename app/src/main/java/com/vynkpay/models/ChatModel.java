package com.vynkpay.models;

public class ChatModel {
    String isSender;
    String isReceiver;
    String msg;

    public ChatModel(String isSender, String isReceiver, String msg) {
        this.isSender = isSender;
        this.isReceiver = isReceiver;
        this.msg = msg;
    }

    public String getIsSender() {
        return isSender;
    }

    public void setIsSender(String isSender) {
        this.isSender = isSender;
    }

    public String getIsReceiver() {
        return isReceiver;
    }

    public void setIsReceiver(String isReceiver) {
        this.isReceiver = isReceiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
