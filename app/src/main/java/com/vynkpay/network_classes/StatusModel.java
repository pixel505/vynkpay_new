package com.vynkpay.network_classes;

import org.json.JSONException;
import org.json.JSONObject;

public class StatusModel {
    public String status;
    public String message;

    public StatusModel(JSONObject jsonObject) {
        this.status = jsonObject.optString("success");
        this.message = jsonObject.optString("message");
    }
}
