package com.vynkpay.models;

public class Slider{

    public Integer image;
    public String text;
    public String desc;

    public Slider(Integer image, String text, String desc) {
        this.image = image;
        this.text = text;
        this.desc = desc;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

