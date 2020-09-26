package com.vynkpay.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vynkpay.BuildConfig;
import com.vynkpay.newregistration.OnboardingActivity;
import com.vynkpay.onboard.SplashActivity;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivitySplashBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.AppVersionResponse;
import com.vynkpay.utils.M;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Splash extends AppCompatActivity {


    boolean GpsStatus ;
    ActivitySplashBinding binding;
    Prefes prefs;
    private boolean sentToSettings = false;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        prefs = new Prefes(Splash.this);
        checkPermissions();
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
                || ActivityCompat.checkSelfPermission(Splash.this, permissionsRequired[3]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[3])) {
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
            CheckGpsStatus();
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
                CheckGpsStatus();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[2])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Splash.this, permissionsRequired[3])) {

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
                                sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
                                if (sp.getString("value", "").isEmpty()) {
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
                                                        startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "India"));
                                                    }else {
                                                        startActivity(new Intent(Splash.this, OnboardingActivity.class).putExtra("Country", "India"));
                                                    }
                                                    finish();
                                                } else {
                                                    sp.edit().putString("value", "Global").commit();
                                                    if (sp.getString("welcome","").equalsIgnoreCase("yes")) {
                                                        startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "Global"));
                                                    }else {
                                                        startActivity(new Intent(Splash.this, OnboardingActivity.class).putExtra("Country", "Global"));
                                                    }
                                                    finish();
                                                }
                                            }
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }



                                    else {
                                       Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        startActivity(intent);


                                    }

                                }
                                else {
                                    Log.e("data", "" + sp.getString("value", ""));
                                    if (sp.getString("welcome","").equalsIgnoreCase("yes")) {
                                        if (sp.getString("value", "").equals("Global")) {
                                            startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "Global"));
                                            finish();
                                        } else if (sp.getString("value", "").equals("India")) {
                                            startActivity(new Intent(Splash.this, HomeActivity.class).putExtra("Country", "India"));
                                            finish();
                                        }
                                    }else {
                                        startActivity(new Intent(Splash.this, OnboardingActivity.class).putExtra("Country", "Global"));
                                        finish();
                                    }
                                }
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

        noThanksButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finishAffinity();
            }
        });

        updateButtonTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        }
    }

    @Override
    protected void onResume() {
        updateVersionApi();
        super.onResume();
    }
}
