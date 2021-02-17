package com.vynkpay.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.budiyev.android.imageloader.ImageLoader;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Functions {

    public static final String VERTICAL="1";
    public static final String HORIZONTAL="2";
    public static final String GRID="3";

    public static final int PREPAID=1;
    public static final int POSTPAID=2;
    public static final int DATACARD=3;

    public static final int LANDLINE=5;
    public static final int BROADBAND=6;
    public static final int ELECTRICITY=7;
    public static final int GAS=8;
    public static final int WATER=9;
    public static final int INSURANCE=10;
    public static final int TRAINBOOKING=11;
    public static final int MONEYTRANSFER=12;

    public static String CURRENCY_SYMBOL="₹";
    public static String CURRENCY_SYMBOL_USER="₹";
    public static boolean isIndian=true;

    public static final int DTH=4;

    public static void hideStatusBar(Activity activity) {
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static RecyclerView.LayoutManager layoutManager(Context context, String direction, int gridCount){
        LinearLayoutManager layoutManager;
        if (direction.equals(VERTICAL)){
            layoutManager= new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        }else if (direction.equals(HORIZONTAL)){
            layoutManager= new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
        }else {
            layoutManager= new GridLayoutManager(context, gridCount);
        }
        return layoutManager;
    }

    public static void loadImageCall(Context context, String url, ImageView imageView){
        ImageLoader.with(context)
                .from(url)
                .fade()
                .load(imageView);
    }

    public static byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public static String changeDateFormat(String date, String foundFormat, String requiredFormat){
        //yyyy-MM-dd hh:mm:ss
        SimpleDateFormat spf=new SimpleDateFormat(foundFormat, Locale.getDefault());
        Date newDate= null;
        try {
            newDate = spf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        spf= new SimpleDateFormat(requiredFormat,Locale.getDefault());
        return spf.format(newDate);
    }



}
