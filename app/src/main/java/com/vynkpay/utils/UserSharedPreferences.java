package com.vynkpay.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.vynkpay.BuildConfig;
import com.vynkpay.retrofit.model.LoginResponse;

public class UserSharedPreferences {

    public static UserSharedPreferences mPreference;
    Context mContext;
    private SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;

    public UserSharedPreferences(Context mContext) {
        this.mContext = mContext;
        this.mPref = this.mContext.getSharedPreferences("MarkLawChambers", Context.MODE_PRIVATE);
    }

    public static UserSharedPreferences getInstance() {
        return mPreference;
    }

    public void save(String key, String value) {
        mEditor = mPref.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }



    public void save(String key, int value) {
        mEditor = mPref.edit();
        mEditor.putInt(key, value);
        mEditor.apply();
    }

    public void save(String key, long value) {
        mEditor = mPref.edit();
        mEditor.putLong(key, value);
        mEditor.apply();
    }

    public boolean save(String key, boolean value) {
        mEditor = mPref.edit();
        mEditor.putBoolean(key, value);
        mEditor.apply();
        return value;
    }


    public String getString(String key) {
        return mPref.getString(key, "");
    }

    public void clear() {
        mEditor = mPref.edit();
        mEditor.clear();
        mEditor.apply();
    }

    public int getInt(String key) {
        return mPref.getInt(key, 1);
    }

    public String getLong(String key) {
        return mPref.getString(key, "");
    }

    public Boolean getBoolean(String key) {
        return mPref.getBoolean(key, false);
    }
}
