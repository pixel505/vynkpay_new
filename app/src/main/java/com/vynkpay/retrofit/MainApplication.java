package com.vynkpay.retrofit;


import androidx.multidex.MultiDexApplication;


import com.vynkpay.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created on 26 June 2019
 */


public class
MainApplication extends MultiDexApplication
{
    private static ApiCalls apiCalls;
    public static Retrofit retrofit;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static ApiCalls getApiService(){

        //To send network requests to an API, we need to use the Retrofit Builder class and specify the base URL for the service.
        //Here URLForApplication – it is basic URL of our API. We will use this URL for all requests later.
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // add your other interceptors …

        // add logging as last interceptor
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS).addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCalls = retrofit.create(ApiCalls.class);
        System.out.println("MainApplication...   responce  "+apiCalls);
        return apiCalls;
    }


}
