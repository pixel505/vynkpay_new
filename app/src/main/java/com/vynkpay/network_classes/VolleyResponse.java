package com.vynkpay.network_classes;

public interface VolleyResponse {
    void onResult(String result, String status, String message);
    void onError(String error);
}
