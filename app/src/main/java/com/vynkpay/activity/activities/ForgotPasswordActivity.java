package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity {
    @BindView(R.id.submitButton)
    NormalButton submitButton;
    @BindView(R.id.etPassword)
    NormalEditText etPassword;
    @BindView(R.id.etConfirmPassword)
    NormalEditText etConfirmPassword;
    Dialog dialog;
    String recovery_token = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_rcg);
        ButterKnife.bind(ForgotPasswordActivity.this);
        //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        setListeners();
        dialog = M.showDialog(ForgotPasswordActivity.this, "", false, false);
        if (getIntent() != null) {
            Intent fetchIntent = getIntent();
            fetchIntent.getScheme();
            Log.i(">>data", "onCreate: " + fetchIntent.getScheme() + "--" + fetchIntent.getData().getPath());
            recovery_token = fetchIntent.getData().getQueryParameter(ApiParams.recovery_token);
        }
    }

    private void setListeners() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getText().length() == 0) {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.please_password), Toast.LENGTH_SHORT).show();
                } else if (etConfirmPassword.getText().length() == 0) {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.confirm_not_valid_fill), Toast.LENGTH_SHORT).show();
                } else if (etPassword.getText().toString().trim().length() == 0) {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.please_valid_password), Toast.LENGTH_SHORT).show();
                } else if (etConfirmPassword.getText().toString().trim().length() == 0) {
                    Toast.makeText(ForgotPasswordActivity.this, getString(R.string.valid_new_password), Toast.LENGTH_SHORT).show();
                } else if (etPassword.getText().length() < 6) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password should be at least 6 char long", Toast.LENGTH_SHORT).show();
                } else if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    Toast.makeText(ForgotPasswordActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                } else {
                    makeResetPasswordRequest();
                }
            }
        });
    }

    String mSuccess, mMessage;

    private void makeResetPasswordRequest() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, BuildConfig.APP_BASE_URL + URLS.resetPassword,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>forgotPassword", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                Toast.makeText(ForgotPasswordActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                                M.launchActivityWithFinish(ForgotPasswordActivity.this, LoginActivity.class);
                            } else {
                                M.dialogOk(ForgotPasswordActivity.this, mMessage, "Error");
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
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.password, etPassword.getText().toString());
                params.put(ApiParams.cn_password, etConfirmPassword.getText().toString());
                params.put(ApiParams.recovery_token, recovery_token.equals("") ? "" : recovery_token);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }

        };
        MySingleton.getInstance(ForgotPasswordActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
}
