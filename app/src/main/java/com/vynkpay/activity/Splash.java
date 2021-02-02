package com.vynkpay.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vynkpay.BuildConfig;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.activities.Signupnew;
import com.vynkpay.newregistration.OnboardingActivity;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivitySplashBinding;
import com.vynkpay.newregistration.SelectionActivity;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.AppVersionResponse;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    boolean status = false;
    boolean GpsStatus ;
    ActivitySplashBinding binding;
    Prefes prefs;
    private boolean sentToSettings = false;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    SharedPreferences sp;

    PlugInControlReceiver receiver = new PlugInControlReceiver();
    IntentFilter filter = new IntentFilter("android.hardware.usb.action.USB_STATE");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
        //sp.edit().putString("welcome","").apply();
        prefs = new Prefes(Splash.this);
        //getDynamicLink();

    }

    private void CheckGpsStatus() {
        LocationManager locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(GpsStatus == true) {
            updateVersionApi();
        } else {
            Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent1);
        }
    }

    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(Splash.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Splash.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Splash.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED
                /*|| ActivityCompat.checkSelfPermission(Splash.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED*/) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[2])
                    /*|| ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[3])*/) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Splash.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.show();
            } else if (prefs.getBoolean(permissionsRequired[0], false)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", Splash.this.getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(Splash.this, getString(R.string.gotoPermission), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(Splash.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
            prefs.setBoolean(permissionsRequired[0], true);
        } else {
            //You already have the permission, just go ahead.
            //CheckGpsStatus();
            updateVersionApi();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                //CheckGpsStatus();
                updateVersionApi();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[2])
                    /*|| ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[3])*/) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.needMultiplePermissionMsg));
                builder.setPositiveButton(getString(R.string.grant), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Splash.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(this, getString(R.string.unableToGetPermission), Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
                builder.setCancelable(false);
                builder.setTitle(getString(R.string.needMultiplePermission));
                builder.setMessage(getString(R.string.unableToGetPermission));
                builder.setPositiveButton(getString(R.string.exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });

                builder.show();
            }
        }
    }

    public void getDynamicLink() {
        Log.d("refferla","called");
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                // Get deep link from result (may be null if no link is found)
                Log.d("refferla","called1");
                Uri deepLink = null;
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.getLink();
                    String referLink = deepLink.toString();
                    referLink = referLink.substring(referLink.lastIndexOf("=") + 1);
                    String referalCode = referLink;
                    sp.edit().putString("referalCode",referalCode).apply();

                    Log.e("linkkk", "" + referalCode);
                    Log.e("linkkk", "" + referLink);
                    Log.e("linkkk", "" + pendingDynamicLinkData);
                    Log.e("linkkk", "" + deepLink);
                } else {
                    Log.d("refferla","called2");
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("refferla","called3");
                //Toast.makeText(Splash.this, e.toString(), Toast.LENGTH_SHORT).show();

            }

        });

    }

    private void updateVersionApi() {
        String versionName = BuildConfig.VERSION_NAME;
        MainApplication.getApiService().appVersionMethod(versionName).enqueue(new Callback<AppVersionResponse>() {
            @Override
            public void onResponse(Call<AppVersionResponse> call, Response<AppVersionResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus().toString().equals("200")) {
                            if (response.body().getIsUpdate().equals("0")) {
                                dialog(true, response.body().getMessage());
                            } else {
                                if (status) {
                                    M.showUSBPopUp(Splash.this, Splash.this::finishAffinity);
                                }else {
                                    if (sp.getString("welcome", "").equalsIgnoreCase("yes")) {
                                        if (sp.getString("value", "").equalsIgnoreCase("")) {
                                            startActivity(new Intent(Splash.this, SelectionActivity.class));
                                        } else {
                                            if (Prefes.getAccessToken(Splash.this).equalsIgnoreCase("")) {
                                                startActivity(new Intent(Splash.this, LoginActivity.class));
                                            } else {
                                                if (Prefes.getUserType(Splash.this).equalsIgnoreCase("2")) {
                                                    if (sp.getString("value", "").equalsIgnoreCase("Global")) {
                                                        startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "Global"));
                                                    } else {
                                                        startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "India"));
                                                    }
                                                } else {
                                                    if (new Prefes(Splash.this).getAskPin().equalsIgnoreCase("yes")) {
                                                        Intent intent = new Intent(Splash.this, PinActivity.class);
                                                        intent.putExtra("var", "0000000");
                                                        intent.putExtra("type", "login");
                                                        intent.putExtra("accessToken", Prefes.getAccessToken(Splash.this));
                                                        intent.putExtra("isIndian", Prefes.getisIndian(Splash.this));
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finishAffinity();
                                                    } else {
                                                        if (sp.getString("value", "").equalsIgnoreCase("Global")) {
                                                            startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "Global"));
                                                        } else {
                                                            startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "India"));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        startActivity(new Intent(Splash.this, OnboardingActivity.class).putExtra("Country", sp.getString("value", "")));
                                    }
                                    finish();
                                }


                                //OLD
                               /* if (sp.getString("value", "").isEmpty()) {
                                    GPSTracker gps = new GPSTracker(Splash.this);
                                    // Check if GPS enabled
                                    if (gps.canGetLocation()) {
                                        double latitude = gps.getLatitude();
                                        double longitude = gps.getLongitude();
                                        Geocoder gcd = new Geocoder(Splash.this, Locale.getDefault());
                                        List<Address> addresses = null;
                                        try {
                                            addresses = gcd.getFromLocation(latitude, longitude, 1);
                                            if (addresses.size() > 0) {
                                                String countryName = addresses.get(0).getCountryName();
                                                Log.e("country", "" + countryName);
                                                if (countryName.equals("India")) {
                                                    sp.edit().putString("value", "India").commit();
                                                    if (sp.getString("welcome","").equalsIgnoreCase("yes")) {
                                                        if (Prefes.getAccessToken(Splash.this).equalsIgnoreCase("")){
                                                            startActivity(new Intent(Splash.this, LoginActivity.class));
                                                        }else {
                                                            if (Prefes.getUserType(Splash.this).equalsIgnoreCase("2")) {
                                                                startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "India"));
                                                            } else {
                                                                if (new Prefes(Splash.this).getAskPin().equalsIgnoreCase("yes")) {
                                                                    Intent intent = new Intent(Splash.this, PinActivity.class);
                                                                    intent.putExtra("var", "0000000");
                                                                    intent.putExtra("type", "login");
                                                                    intent.putExtra("accessToken", Prefes.getAccessToken(Splash.this));
                                                                    intent.putExtra("isIndian", Prefes.getisIndian(Splash.this));
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(intent);
                                                                    finishAffinity();
                                                                } else {
                                                                    startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "India"));
                                                                }
                                                            }
                                                        }
                                                    }else {
                                                        startActivity(new Intent(Splash.this, OnboardingActivity.class).putExtra("Country", "India"));
                                                    }
                                                    finish();
                                                } else {
                                                    sp.edit().putString("value", "Global").commit();
                                                    if (sp.getString("welcome","").equalsIgnoreCase("yes")) {
                                                        if (Prefes.getAccessToken(Splash.this).equalsIgnoreCase("")){
                                                            startActivity(new Intent(Splash.this, LoginActivity.class));
                                                        }else {
                                                            if (Prefes.getUserType(Splash.this).equalsIgnoreCase("2")) {
                                                                startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "Global"));
                                                            } else {
                                                                if (new Prefes(Splash.this).getAskPin().equalsIgnoreCase("yes")) {
                                                                    Intent intent = new Intent(Splash.this, PinActivity.class);
                                                                    intent.putExtra("var", "0000000");
                                                                    intent.putExtra("type", "login");
                                                                    intent.putExtra("accessToken", Prefes.getAccessToken(Splash.this));
                                                                    intent.putExtra("isIndian", Prefes.getisIndian(Splash.this));
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(intent);
                                                                    finishAffinity();
                                                                } else {
                                                                    startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "Global"));
                                                                }
                                                            }
                                                        }
                                                    }else {
                                                        startActivity(new Intent(Splash.this, OnboardingActivity.class).putExtra("Country", "Global"));
                                                    }
                                                    finish();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                       Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);
                                    }

                                } else {
                                    Log.e("data", "" + sp.getString("value", ""));
                                    if (sp.getString("welcome","").equalsIgnoreCase("yes")) {
                                        if (Prefes.getAccessToken(Splash.this).equalsIgnoreCase("")){
                                            startActivity(new Intent(Splash.this, LoginActivity.class));
                                            finish();
                                        }else {
                                            if (Prefes.getUserType(Splash.this).equalsIgnoreCase("2")) {
                                                if (sp.getString("value", "").equals("Global")) {
                                                    startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "Global"));
                                                    finish();
                                                } else if (sp.getString("value", "").equals("India")) {
                                                    startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "India"));
                                                    finish();
                                                }
                                            } else {
                                                if (new Prefes(Splash.this).getAskPin().equalsIgnoreCase("yes")) {
                                                    Intent intent = new Intent(Splash.this, PinActivity.class);
                                                    intent.putExtra("var", "0000000");
                                                    intent.putExtra("type", "login");
                                                    intent.putExtra("accessToken", Prefes.getAccessToken(Splash.this));
                                                    intent.putExtra("isIndian", Prefes.getisIndian(Splash.this));
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(intent);
                                                    finishAffinity();
                                                } else {
                                                    if (sp.getString("value", "").equals("Global")) {
                                                        startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "Global"));
                                                        finish();
                                                    } else if (sp.getString("value", "").equals("India")) {
                                                        startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "India"));
                                                        finish();
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        startActivity(new Intent(Splash.this, OnboardingActivity.class).putExtra("Country", sp.getString("value", "")));
                                        finish();
                                    }
                                }*/
                                Log.d("valuevalue",sp.getString("value", ""));
                            }
                        }
                    } else {
                        dialog(false, "");
                    }
                } else {
                    dialog(false, "");
                }
            }


            @Override
            public void onFailure(Call<AppVersionResponse> call, Throwable t) {
                Log.d("eeError",t.getMessage()!=null ? t.getMessage():"Error");
            }
        });

    }

    public void dialog(boolean askingForUpdate, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
        builder.setCancelable(false);

        View view = LayoutInflater.from(Splash.this).inflate(R.layout.app_update_layout_rcg, null);
        builder.setView(view);

        TextView titleTV = view.findViewById(R.id.titleTV);
        TextView messageTV = view.findViewById(R.id.messageTV);
        TextView noThanksButtonTV = view.findViewById(R.id.noThanksButtonTV);
        TextView updateButtonTV = view.findViewById(R.id.updateButtonTV);

        if (askingForUpdate) {
            titleTV.setText("New version available");
            messageTV.setText(message);
            noThanksButtonTV.setText("No, Thanks");
            noThanksButtonTV.setVisibility(View.VISIBLE);
            updateButtonTV.setText("update");
        } else {
            titleTV.setText("Error!");
            messageTV.setText("Please, try after some time");
            noThanksButtonTV.setVisibility(View.GONE);
            updateButtonTV.setText("Ok");
        }

        AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();

        noThanksButtonTV.setOnClickListener(v -> {
            alertDialog.dismiss();
            finishAffinity();
        });

        updateButtonTV.setOnClickListener(v -> {
            alertDialog.dismiss();
            if (askingForUpdate) {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            } else {
                finishAffinity();
            }

        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }

    @Override
    protected void onResume() {
        //updateVersionApi();
        super.onResume();
        checkPermissions();
        try {
            registerReceiver(receiver,filter);
            MySingleton.getInstance(Splash.this).setConnectivityListener(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (receiver!=null){
                unregisterReceiver(receiver);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            status = true;
        }else {
            status = false;
        }
    }
}
