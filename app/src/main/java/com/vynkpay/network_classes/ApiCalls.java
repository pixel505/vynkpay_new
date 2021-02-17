package com.vynkpay.network_classes;

import android.content.Context;
import android.graphics.Bitmap;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.URLS;
import java.util.HashMap;
import java.util.Map;

public class ApiCalls {

    public static void updateProfile(Context context, Bitmap bitmap, String token, final VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }
        });

        HashMap<String, String> hashMap=new HashMap<>();

        Map<String, VolleyMultipartRequest.DataPart> dataPartMap=new HashMap<>();
        long imageName = System.currentTimeMillis();
        dataPartMap.put("profile_pic", new VolleyMultipartRequest.DataPart(imageName + ".png", Functions.getFileDataFromDrawable(bitmap)));
        volleyNetworkClass.makeRequest(URLS.uploadImage, token, hashMap, dataPartMap);
    }

    public static void checkAppVersion(Context context, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }
        });

        volleyNetworkClass.makeGetRequest(URLS.VERSION_CHECK_URL, "","");
    }

    public static void getBonusTransactions(Context context, String  token, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }
        });

        volleyNetworkClass.makeRequest(URLS.bonus_transaction_URL, token);
    }

    public static void getMcashTransactions(Context context, String  token, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }
        });

        volleyNetworkClass.makeRequest(URLS.mCash_transaction_URL, token);
    }


    public static void getEcashTransactions(Context context, String  token, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }
        });

        volleyNetworkClass.makeRequest(URLS.eCash_transaction_URL, token);
    }

    public static void getVcashTransactions(Context context, String  token, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }
        });

        volleyNetworkClass.makeRequest(URLS.vCash_transaction_URL, token);
    }

    public static void getBanners(Context context,String token, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }
        });

        volleyNetworkClass.makeRequest(URLS.banners_list_URL, token);
    }

    public static void sendWalletOTP(Context context, String token, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }
        });

        volleyNetworkClass.makeRequest(URLS.wallet_otp_URL, token);
    }


    public static void checkWalletOTP(Context context, String token, String wallet_otp, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }
        });

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("wallet_otp", wallet_otp);

        volleyNetworkClass.makeRequest(URLS.otp_check_URL, token, hashMap);
    }

    public static void withdrawalRequest(Context context, String token, String amount, String action, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }
        });

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("blnc", amount);
        hashMap.put("action", "wr");
        hashMap.put("type", action);

        volleyNetworkClass.makeRequest(URLS.withdrawal_request_URL, token, hashMap);
    }

    public static void updatePaymentMode(Context context, String token, String mode, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {

            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }

        });

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mode", mode);

        volleyNetworkClass.makeRequest(URLS.update_pament_mode_URL, token, hashMap);
    }


    public static void withdrawalRequestsList(Context context, String token, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {

            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }

        });

        volleyNetworkClass.makeRequest(URLS.withdraw_list_mode_URL, token);
    }

    public static void getBonusDetail(Context context, String dateForView, String token, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {

            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }

        });


        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("view", dateForView);
        volleyNetworkClass.makeRequest(URLS.bonus_detail_URL, token, hashMap);

        //team_profit_detail_URL
    }

    public static void getTeamProfitDetail(Context context, String front_user_id, String token, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {

            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }

        });

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("view", front_user_id);

        volleyNetworkClass.makeRequest(URLS.team_profit_detail_URL, token, hashMap);
    }


    public static void sendResetPasswordLink(Context context, String userName, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }
        });

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("u_email", userName);
        volleyNetworkClass.makeRequest(URLS.ResetPasswordLink_URL, "", hashMap);
    }


    public static void sendResetPINLink(Context context, String userName, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }

        });

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("u_email", userName);
        volleyNetworkClass.makeRequest(URLS.ResetPINLink_URL, "", hashMap);
    }

    public static void getUserDetails(Context context, String token, VolleyResponse volleyResponse){

        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }

        });

        volleyNetworkClass.makeGetRequest(URLS.USER_URL, "", token);
    }

    public static void getShops(Context context,String id, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {

            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }

        });

        volleyNetworkClass.makeRequest(URLS.SHOP_URL+id, "");
    }

    public static void settings(Context context,String token, VolleyResponse volleyResponse){
        VolleyNetworkClass volleyNetworkClass=new VolleyNetworkClass(context, new VolleyResponse() {

            @Override
            public void onResult(String result, String status, String message) {
                volleyResponse.onResult(result, status, message);
            }

            @Override
            public void onError(String error) {
                volleyResponse.onError(error);
            }

        });

        volleyNetworkClass.makeRequest(URLS.SETTING_URL, token);
    }

}
