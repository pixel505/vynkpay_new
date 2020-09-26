package com.vynkpay.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.SystemClock;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.NotificationActivity;
import com.vynkpay.activity.activities.WalletActivity;
import com.vynkpay.activity.activities.history.HistoryDetailActivity;
import com.vynkpay.helper.FileCache;
import com.vynkpay.utils.M;
import com.vynkpay.utils.URLS;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class MyFireBaseMessagingService extends FirebaseMessagingService {
    String image, title="", message, type = "000", amount;
    Notification notification;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        RemoteMessage.Notification m = remoteMessage.getNotification();

        Log.d("notificationDataLOG8787", data.toString()+"//90");

        amount = data.get("amount");
        if (data.containsKey("notification_type")) {
            type = data.get("notification_type");
        }

        if (m!=null){
            Log.d("notificationDataLOG",  m.getImageUrl()+"");
            Log.d("notificationDataLOG",  m.getBody()+"");
            Log.d("notificationDataLOG",  m.getTitle()+"");
            title = m.getTitle();
        }else {
            Log.d( "notificationDataLOG", "nulll");
        }

        switch (type) {
            /**Pay u added money**/
            case "113":
                notificationIntent = new Intent(this, WalletActivity.class);
                message = Functions.CURRENCY_SYMBOL + " " + amount + " added to  your Vynkpay wallet";
                image = "";
                break;

            /**User sends you money**/
            case "112":
                notificationIntent = new Intent(this, WalletActivity.class);
                message = "You have received " + Functions.CURRENCY_SYMBOL + "" + amount + " from " + data.get("provider_mobile");
                image = "";
                break;

            /**Recharge services**/
            case "111":
                notificationIntent = new Intent(this, HistoryDetailActivity.class);
                message = "Successful recharge of " + Functions.CURRENCY_SYMBOL + " " + amount;
                image = "";
                break;

            /**Offer Detail**/
            case "114":
                notificationIntent = new Intent(this, HistoryDetailActivity.class);
                message = "";
                image = "";
                break;
                /**Notification list**/
            default:
                notificationIntent = new Intent(this, NotificationActivity.class);
                message = m.getBody();
                image = m.getIcon();
                Log.i(">>miage", "onMessageReceived: " + image);
                break;
        }

        showNotification(this, title, message, image, type);
    }

    Intent notificationIntent;
    Bitmap remote_picture;

    void showNotification(Context context, String title, String message, String image, String type) {
        int icon = R.drawable.app_icon;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.channel_name);
            int imp = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, title, imp);
            mChannel.setDescription(message);
            mChannel.setLightColor(Color.CYAN);
            mChannel.canShowBadge();
            mChannel.setShowBadge(true);
            notificationManager.createNotificationChannel(mChannel);

            notificationIntent.putExtra(URLS.NOTIFICATION_RECEIVED, true);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //Notification.BigPictureStyle notiStyle = new Notification.BigPictureStyle();
            //notiStyle.setBigContentTitle(title);
            //notiStyle.setSummaryText(message);

            if (image!=null){
                if (!image.isEmpty()) {
                    try {
                        remote_picture = BitmapFactory.decodeStream((InputStream) new URL(image).getContent());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                  //  notiStyle.bigPicture(remote_picture);
                }
            }

            notification = new Notification.Builder(this, "")
                    .setContentTitle(title)
                    .setContentText(message)
                    .setNumber(5)
                    .setSmallIcon(icon)
                    .setStyle(new Notification.BigTextStyle().bigText(message))
                    .setAutoCancel(true)
                    .setChannelId(channelId)
                    .setVisibility(View.VISIBLE)
                    .setContentIntent(intent)
                    .setCategory(Notification.CATEGORY_PROMO)
                    .setPriority(Notification.PRIORITY_HIGH).build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notificationManager.notify((int) SystemClock.currentThreadTimeMillis(), notification);

        } else {
            notificationIntent.putExtra(URLS.NOTIFICATION_RECEIVED, true);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            //NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
            //notiStyle.setBigContentTitle(title);
            //notiStyle.setSummaryText(message);


            if (!image.equals("")) {
                try {
                    remote_picture = BitmapFactory.decodeStream((InputStream) new URL(image).getContent());
                } catch (IOException e) {
                    e.printStackTrace();
                }
               // notiStyle.bigPicture(remote_picture);

            }

            Notification notification = new NotificationCompat.Builder(context)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setSmallIcon(icon)
                    .setContentIntent(intent)
                    .setCategory(Notification.CATEGORY_PROMO)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(View.VISIBLE)
                    .build();

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;
            notification.defaults |= Notification.DEFAULT_LIGHTS;
            notificationManager.notify((int) SystemClock.currentThreadTimeMillis(), notification);
        }
    }

    private Bitmap getBitmap(String url) {
        FileCache fileCache = new FileCache(this);
        File f = fileCache.getFile(url);

        //from SD cache
        Bitmap b = decodeFile(f);
        if (b != null)
            return b;

        //from web
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            M.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }
}







