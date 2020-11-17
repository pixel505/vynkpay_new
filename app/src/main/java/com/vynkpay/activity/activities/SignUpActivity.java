package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.fcm.Config;
import com.vynkpay.fcm.SharedPrefManager;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity {
    @BindView(R.id.continueButton)
    NormalButton continueButton;
    @BindView(R.id.loginText)
    LinearLayout loginText;
    @BindView(R.id.etEmail)
    NormalEditText etEmail;
    @BindView(R.id.etPhone)
    NormalEditText etPhone;
    @BindView(R.id.etPassword)
    NormalEditText etPassword;
    Dialog dialog;
    String mMessage, mSuccess;
    @BindView(R.id.showHideImage)
    ImageView showHideImage;
    private boolean isPasswordShown = false;
    String var = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_rcg);

        //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        ButterKnife.bind(SignUpActivity.this);
        dialog = M.showDialog(SignUpActivity.this, "", false, false);
        setListeners();
        var = getIntent().getStringExtra("teee");
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                M.removeZero(s);
            }
        });

        M.showHidePassword(SignUpActivity.this, isPasswordShown, etPassword, showHideImage);


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
        showHideImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.showHidePassword(SignUpActivity.this, isPasswordShown = !isPasswordShown, etPassword, showHideImage);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhone.getText().length() == 0 || etPhone.getText().toString().trim().length() == 0) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.please_phone), Toast.LENGTH_SHORT).show();
                } else if (etPhone.getText().length() > 10 || etPhone.getText().length() < 10) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.valid_phone), Toast.LENGTH_SHORT).show();
                } else if (etEmail.getText().length() == 0 || etEmail.getText().toString().trim().length() == 0) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.please_email), Toast.LENGTH_SHORT).show();
                } else if (!M.validateEmail(etEmail.getText().toString().trim())) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.valid_email), Toast.LENGTH_SHORT).show();
                } else if (etPassword.getText().length() == 0 || etPassword.getText().toString().trim().length() == 0) {
                    Toast.makeText(SignUpActivity.this, getString(R.string.please_password), Toast.LENGTH_SHORT).show();
                } else if (etPassword.getText().length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password length should be more than 6 char", Toast.LENGTH_SHORT).show();
                } else {
                    makeRegisterRequest();
                }
            }
        });
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.launchActivity(SignUpActivity.this, LoginActivity.class);
            }
        });
    }

    private void makeRegisterRequest() {
        dialog.show();
        Log.i(">>url", "makeRegisterRequest: " + BuildConfig.APP_BASE_URL + URLS.signUP);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.signUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>signUpResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                Intent intent = new Intent(SignUpActivity.this, OtpActivity.class);
                                intent.putExtra("mobile_number", etPhone.getText().toString());
                                intent.putExtra("email", etEmail.getText().toString());
                                intent.putExtra("password", etPassword.getText().toString());
                                intent.putExtra("teee", var);
                                startActivity(intent);
                                finish();
                            } else {
                                if (jsonObject.has(ApiParams.error_code)) {
                                    if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400)) {
                                        Iterator<?> keys = jsonObject.getJSONObject(ApiParams.errors).keys();
                                        while (keys.hasNext()) {
                                            String key = (String) keys.next();
                                            if (jsonObject.getJSONObject(ApiParams.errors).get(key) instanceof String) {
                                                String message = jsonObject.getJSONObject(ApiParams.errors).get(key).toString();
                                                makeErrorDialog(message, jsonObject.getString(ApiParams.error_code));
                                            }
                                        }
                                    } else if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_409)) {
                                        makeErrorDialog(mMessage, jsonObject.getString(ApiParams.error_code));
                                    }
                                } else {
                                    makeErrorDialog(mMessage, "000");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            dialog.dismiss();
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
                Log.i(">>url", "getParams: " + etEmail.getText().toString() +
                        "\n" + etPhone.getText().toString() + "\n" + etPassword.getText().toString() + "1fsdg6sd4ga34g698aw4vwa");
                map.put(ApiParams.email, etEmail.getText().toString());
                map.put(ApiParams.mobile_number, etPhone.getText().toString());
                map.put(ApiParams.password, etPassword.getText().toString());
                map.put(ApiParams.fcm_token, SharedPrefManager.getInstance(SignUpActivity.this).getDeviceToken());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                params.put("access_token", URLS.DEFAULT_ACCESS_TOKEN);
                return params;
            }
        };

        MySingleton.getInstance(SignUpActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void makeErrorDialog(String mMessage, final String code) {
        final Dialog errorDialog = M.inflateDialog(SignUpActivity.this, R.layout.dialog_error_message_rcg);
        NormalTextView errorMessage = errorDialog.findViewById(R.id.errorMessage);
        NormalButton okButton = errorDialog.findViewById(R.id.okButton);
        LinearLayout closeButton = errorDialog.findViewById(R.id.closeButton);
        errorMessage.setText(mMessage);
        if (code.equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_409)) {
            okButton.setText("Login");
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code.equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_409)) {
                    errorDialog.dismiss();
                    Intent homeIntent = new Intent(SignUpActivity.this, LoginActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    homeIntent.putExtra("takeData", etPhone.getText().toString());
                    startActivity(homeIntent);
                    finish();
                } else {
                    errorDialog.dismiss();
                }
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorDialog.dismiss();
            }
        });
        errorDialog.show();
    }
}
