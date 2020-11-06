package com.vynkpay.prefes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.vynkpay.BuildConfig;
import com.vynkpay.activity.activities.LoginActivity;

public class Prefes {
    private static final String APP_KEY = BuildConfig.APP_KEY;
    private static final String SAVE_ID = "_ID";
    private static final String FIRST_TIME = "FIRST_TIME";

    private static final String OPerator = "OPerator";
    private static final String CIrcle = "CIrcle";
    private static final String PLan = "PLan";
    private static final String TYpe = "TYpe";
    private static final String VAlidity = "VAlidity";
    private static final String DEscription = "DEscription";
    private static final String TAlktime = "TAlktime";
    private static final String NAME = "name";
    private static final String PDFFile = "PDFFile";
    private static final String PDFFileREC = "PDFFileREC";

    Context context;
    SharedPreferences prefs;
    public Prefes(Context con) {
        this.context = con;
        prefs = con.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE);
    }


    public void setBoolean(String key, Boolean value){
        SharedPreferences.Editor editor=prefs.edit();
        editor.putBoolean(key, value).apply();
    }

    public Boolean getBoolean(String key, Boolean value){
        return prefs.getBoolean(key, value);
    }


    public static void saveAccessToken(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BuildConfig.ACCESS_TOKEN, value);
        editor.apply();
    }

    public static String getAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(BuildConfig.ACCESS_TOKEN, "");
    }


    public static void savePDFPath(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PDFFile, value);
        editor.apply();
    }

    public static String getPDFFile(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(PDFFile, "");
    }


    public static void savePDFREC(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PDFFileREC, value);
        editor.apply();
    }

    public static String getPDFREC(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(PDFFileREC, "");
    }

    public static void saveUserID(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user_id", value);
        editor.apply();
    }


    public static String getUserID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString("user_id", "");
    }

    public static void saveID(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("id", value);
        editor.apply();
    }


    public static String getID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString("id", "");
    }




    public static void saveRemitterID(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("remitter_id", value);
        editor.apply();
    }


    public static String getRemitterID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString("remitter_id", "");
    }



    public static void saveUserData(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BuildConfig.USER_DATA, value);
        editor.apply();
    }

    public static String getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(BuildConfig.USER_DATA, "");
    }

    public static void savePhoneNumber(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BuildConfig.PHONE, value);
        editor.apply();
    }

    public static String getPhoneNumber(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(BuildConfig.PHONE, "");
    }

    public static void saveEmail(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BuildConfig.EMAIL, value);
        editor.apply();
    }

    public static String getEmail(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(BuildConfig.EMAIL, "");
    }

    public static void setAppFirstTime(int value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(FIRST_TIME, value);
        editor.apply();
    }

    public static int getAppFirstTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getInt(FIRST_TIME, 0);
    }


    public static void saveOperator(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(OPerator, value);
        editor.apply();
    }

    public static String getOperator(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(OPerator, "");
    }

    public static void saveCircle(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(CIrcle, value);
        editor.apply();
    }

    public static String getCircle(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(CIrcle, "");
    }

    public static void savePlan(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PLan, value);
        editor.apply();
    }

    public static String getPlan(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(PLan, "");
    }

    public static void saveType(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TYpe, value);
        editor.apply();
    }

    public static String getType(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(TYpe, "");
    }



    public static void saveValidity(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(VAlidity, value);
        editor.apply();
    }

    public static String getValidity(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(VAlidity, "");
    }

    public static void saveDescription(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEscription, value);
        editor.apply();
    }

    public static String getDescription(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(DEscription, "");
    }

    public static void saveTalktime(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(TAlktime, value);
        editor.apply();
    }

    public static String getTalktime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(TAlktime, "");
    }

    public static void saveName(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(NAME, value);
        editor.apply();
    }

    public static String getName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(NAME, "");
    }


    public static void saveUserName(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", value);
        editor.apply();
    }

    public static String getUserName(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString("username", "");
    }

    public static void clear(Context context) {

        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }



    public static void saveOperatorUrl(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEscription, value);
        editor.apply();
    }

    public static String getOperatorUrl(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(DEscription, "");
    }



    public static void saveOperatorID(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEscription, value);
        editor.apply();
    }

    public static String getOperatorID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(DEscription, "");
    }



    public static void saveOperatorDID(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEscription, value);
        editor.apply();
    }

    public static String getOperatorDID(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(DEscription, "");
    }




    public static void saveIm(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(DEscription, value);
        editor.apply();
    }

    public static String getIma(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(DEscription, "");
    }




    public static void saveisIndian(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("isIndian", value.toUpperCase());
        editor.apply();
    }

    public static String getisIndian(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString("isIndian", "");
    }



    public static void saveImage(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("nonIndianImage", value);
        editor.apply();
    }

    public static String getImage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString("nonIndianImage", "");
    }


    public static void saveCash(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("cash", value);
        editor.apply();
    }

    public static String getCash(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString("cash", "");
    }

    public static void saveUserType(String value, Context context) {
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(BuildConfig.USER_TYPE, value);
        editor.apply();
    }

    public static String getUserType(Context context){
        SharedPreferences preferences = context.getSharedPreferences(APP_KEY, Activity.MODE_PRIVATE);
        return preferences.getString(BuildConfig.USER_TYPE,"");
    }


}
