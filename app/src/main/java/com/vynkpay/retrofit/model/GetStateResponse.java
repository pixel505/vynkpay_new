package com.vynkpay.retrofit.model;

import org.json.JSONObject;

public class GetStateResponse {
    String id;
    String statename;

    public GetStateResponse(String id, String statename) {
        this.id = id;
        this.statename = statename;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatename() {
        return statename;
    }

    public void setStatename(String state_name) {
        this.statename = state_name;
    }

    public String toString(){
        JSONObject object = new JSONObject();
        try {
            object.put("state_id",getId());
            object.put("stateName",getStatename());
        }catch (Exception e){
            e.printStackTrace();
        }
        return object.toString();
    }

}
