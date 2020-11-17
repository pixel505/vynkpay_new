package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.events.UpDateUIEvent;
import com.vynkpay.fcm.Config;
import com.vynkpay.fcm.SharedPrefManager;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OtpActivity extends AppCompatActivity {
    Dialog dialog;
    @BindView(R.id.verifyButton)
    NormalButton verifyButton;
    @BindView(R.id.resendText)
    NormalTextView resendText;
    @BindView(R.id.timerText)
    NormalTextView timerText;
    @BindView(R.id.etOtp)
    NormalEditText etOtp;
    String mobile_number, email, password;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_rcg);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);

        ButterKnife.bind(OtpActivity.this);
        //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        dialog = M.showDialog(OtpActivity.this, "", false, false);
        setListeners();
        var = getIntent().getStringExtra("teee");
        mobile_number = getIntent().getStringExtra("mobile_number");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        resendText.setVisibility(View.INVISIBLE);
        timerText.setVisibility(View.VISIBLE);

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("Resend Otp in : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timerText.setVisibility(View.INVISIBLE);
                resendText.setVisibility(View.VISIBLE);
            }

        }.start();



        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("fcmTokenLOG", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        if (task.getResult()!=null){
                            String token = task.getResult().getToken();
                            Log.d("fcmTokenLOG", token);
                            SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("regId", token);
                            editor.apply();
                        }
                    }
                });
    }

    private void setListeners() {
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOtp.getText().length() == 0 || etOtp.getText().toString().trim().length() == 0) {
                    etOtp.setError(getString(R.string.valid_otp));
                } else {
                    verifyRequest();
                }
            }
        });

        resendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtpRequest();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    String mMessage, mSuccess;
    String var = "";

    private void verifyRequest() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.login,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>otpResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);

                            if (mSuccess.equalsIgnoreCase("true")) {
                                Prefes.saveUserData(jsonObject.has(ApiParams.data) ? jsonObject.getJSONObject(ApiParams.data).toString() : "", OtpActivity.this);
                                if (var.equals("1")) {
                                    finish();
                                } else {

                                    if(sp.getString("value","").equals("Global") && Prefes.getisIndian(OtpActivity.this).equals("NO")){
                                        startActivity(new Intent(OtpActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }

                                    else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(OtpActivity.this).equals("YES")){
                                        startActivity(new Intent(OtpActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }


                                    else if(sp.getString("value","").equals("India") && Prefes.getisIndian(OtpActivity.this).equals("YES")){
                                        startActivity(new Intent(OtpActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }

                                    else if(sp.getString("value","").equals("India") && Prefes.getisIndian(OtpActivity.this).equals("NO")){
                                        startActivity(new Intent(OtpActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }
                                }
                                EventBus.getDefault().postSticky(new UpDateUIEvent(true));
                                dialog.dismiss();

                            } else {
                                EventBus.getDefault().postSticky(new UpDateUIEvent(false));
                                M.dialogOk(OtpActivity.this, mMessage, "Error");
                            }

                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                            dialog.dismiss();
                            EventBus.getDefault().postSticky(new UpDateUIEvent(false));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                        EventBus.getDefault().postSticky(new UpDateUIEvent(false));
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(ApiParams.mobile_number, mobile_number);
                map.put(ApiParams.otp, etOtp.getText().toString());
                map.put(ApiParams.device_type, "1");
                map.put(ApiParams.device_id, "123");
                map.put(ApiParams.device_info, "1");
                map.put(ApiParams.fcm_token, SharedPrefManager.getInstance(OtpActivity.this).getDeviceToken());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                return params;
            }

        };

        MySingleton.getInstance(OtpActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void resendOtpRequest() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, BuildConfig.APP_BASE_URL + URLS.resendOtp,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>forgotPassword", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.getString(ApiParams.success);
                            mMessage = jsonObject.getString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                Toast.makeText(OtpActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                M.dialogOk(OtpActivity.this, mMessage, "Error");
                            }
                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put(ApiParams.mobile_number, mobile_number);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };
        MySingleton.getInstance(OtpActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

//    private BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equalsIgnoreCase("otp")) {
//                final String message = intent.getStringExtra("message");
//                Log.d(">>SmsReceiver", "otp is " + message);
//                etOtp.setText(message);
//            }
//        }
//    };
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        Log.d(">>on resume unregister", "on resume unregister");
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
//    }
//
//    @Override
//    public void onResume() {
//
//        Log.d(">>on resume register", "on resume register");
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
//        super.onResume();
//    }
}
