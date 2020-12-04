package com.vynkpay.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vynkpay.activity.activitiesnew.ForegroundCheckTask;
import com.vynkpay.prefes.Prefes;

import java.util.concurrent.ExecutionException;

public class AppLifecycleTracker implements Application.ActivityLifecycleCallbacks {
    Prefes prefers;
    private int numStarted = 0;
    Handler handler;
    Runnable runnable;
    public static  int LOCKTIME = 500;

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        prefers = new Prefes(activity);
        handler = new Handler();
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (handler.hasCallbacks(runnable)){
                handler.removeCallbacks(runnable);
            }
        }else {
            handler.removeCallbacks(runnable);
        }
        if (numStarted == 0) {
            Log.d("appstate","foreground");
            // app went to foreground
        }
        numStarted++;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (handler.hasCallbacks(runnable)){
                handler.removeCallbacks(runnable);
            }
        }else {
            handler.removeCallbacks(runnable);
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull final Activity activity) {
        numStarted--;
        if (numStarted == 0) {
            Log.d("appstate","background");
            // app went to background
            runnable = new Runnable() {
                @Override
                public void run() {
                    Log.d("appstate","handercalled");

                    //prefers.setString(Prefers.ASK_PIN, "yes");
                    prefers.saveAskPin("yes");
                }
            };
            handler.postDelayed(runnable, LOCKTIME);

        }

       /* try {
            boolean foreground = new ForegroundCheckTask().execute(activity).get();
            if(!foreground) {
                prefers.saveAskPin("yes");
                Log.d("appstate","handercalledAsyncTask");
                //App is in Background - do what you want
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        Log.d("appstate","killed");
    }

}
