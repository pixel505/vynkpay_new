package com.vynkpay.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class PlugInControlReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;

    @Override
    public void onReceive(final Context context, Intent intent) {

        String action = intent.getAction();
        Log.v("PlugInControlReceiver","action: "+action);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            if(action.equals("android.hardware.usb.action.USB_STATE")) {

                if(intent.getExtras().getBoolean("connected")){
                    if (connectivityReceiverListener != null) {
                        connectivityReceiverListener.onNetworkConnectionChanged(false);
                    }
                    //Toast.makeText(context, "USB Connected", Toast.LENGTH_SHORT).show();
                }else{
                    if (connectivityReceiverListener !=null) {
                        connectivityReceiverListener.onNetworkConnectionChanged(false);
                    }
                    //Toast.makeText(context, "USB Disconnected", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            if(action.equals(Intent.ACTION_POWER_CONNECTED)) {
                if (connectivityReceiverListener!=null) {
                    connectivityReceiverListener.onNetworkConnectionChanged(true);
                }
                //Toast.makeText(context, "USB Connected", Toast.LENGTH_SHORT).show();
            } else if(action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
                if (connectivityReceiverListener!=null) {
                    connectivityReceiverListener.onNetworkConnectionChanged(false);
                }
                //Toast.makeText(context, "USB Disconnected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}
