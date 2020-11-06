package com.vynkpay.retrofit;


import androidx.multidex.MultiDexApplication;
import com.vynkpay.BuildConfig;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created on 26 June 2019
 */

public class MainApplication extends MultiDexApplication {

    private static ApiCalls apiCalls;
    public static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public static ApiCalls getApiService(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiCalls = retrofit.create(ApiCalls.class);
        System.out.println("MainApplication...   responce  "+apiCalls);
        return apiCalls;
    }


}
