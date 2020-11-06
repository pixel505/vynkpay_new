package com.vynkpay.models;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

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

    public String toString(){
        JSONObject object = new JSONObject();
        try {
            object.put("description",getDesc());
            object.put("image",getImage());
            object.put("text",getText());
        }catch (Exception e){
            e.printStackTrace();
        }
        return object.toString();
    }

}

