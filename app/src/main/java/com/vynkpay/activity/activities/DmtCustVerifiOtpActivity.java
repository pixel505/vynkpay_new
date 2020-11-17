package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.vynkpay.events.ListingBeneficiaryEvent;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DmtCustVerifiOtpActivity extends AppCompatActivity {
    Dialog dialog;
    @BindView(R.id.verifyButton)
    NormalButton verifyButton;
    @BindView(R.id.resendText)
    NormalTextView resendText;
    @BindView(R.id.timerText)
    NormalTextView timerText;
    @BindView(R.id.etOtp)
    NormalEditText etOtp;
    String beneficiary_id="", fname="", lname="", ben_account="", ben_ifsc="",  mobile_number="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_rcg);

        ButterKnife.bind(DmtCustVerifiOtpActivity.this);
        //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        dialog = M.showDialog(DmtCustVerifiOtpActivity.this, "", false, false);
        setListeners();

        beneficiary_id = getIntent().getStringExtra(ApiParams.beneficiary_id);

        fname = getIntent().getStringExtra(ApiParams.fname);
        lname = getIntent().getStringExtra(ApiParams.lname);
        ben_ifsc = getIntent().getStringExtra(ApiParams.ben_ifsc);
        ben_account = getIntent().getStringExtra(ApiParams.ben_account);
        mobile_number = getIntent().getStringExtra(ApiParams.mobile_number);

      //Log.i(">>values", "dmtScreen: " + beneficiary_id + "\n" + fname + "\n" + lname + "\n" + ben_account + "\n" + ben_ifsc + "\n" + mobile_number);

       /* resendText.setVisibility(View.INVISIBLE);
        timerText.setVisibility(View.VISIBLE);
        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                timerText.setText("Resend Otp in : " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timerText.setVisibility(View.INVISIBLE);
                resendText.setVisibility(View.VISIBLE);
            }

        }.start();*/
    }

    private void setListeners() {
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOtp.getText().length() == 0 || etOtp.getText().toString().trim().length() == 0) {
                    Toast.makeText(DmtCustVerifiOtpActivity.this, getString(R.string.valid_otp), Toast.LENGTH_SHORT).show();
                } else {
                    verifyRequest();
                }
            }
        });

        resendText.setVisibility(View.GONE);

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

    private void verifyRequest() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.dmtCustVerification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);

                            Log.i(">>otpResponse", "onResponse: " + jsonObject.toString());

                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            String verificationStatus;
                            if (mSuccess.equalsIgnoreCase("true")) {
                                if (jsonObject.has(ApiParams.verify)) {
                                    JSONObject verify = jsonObject.getJSONObject(ApiParams.verify).getJSONObject(ApiParams.data);
                                    verificationStatus = verify.getString(ApiParams.verification_status);
                                } else {
                                    verificationStatus = "";
                                }

                                Intent returnedIntent = new Intent();
                                returnedIntent.putExtra(ApiParams.status, verificationStatus);
                                returnedIntent.putExtra(ApiParams.beneficiary_id, beneficiary_id);
                                returnedIntent.putExtra(ApiParams.ben_account, getIntent().getStringExtra(ApiParams.ben_account));
                                returnedIntent.putExtra(ApiParams.ben_ifsc, getIntent().getStringExtra(ApiParams.ben_ifsc));
                                returnedIntent.putExtra(ApiParams.name, getIntent().getStringExtra(ApiParams.name));
                                returnedIntent.putExtra(ApiParams.mobile_number, getIntent().getStringExtra(ApiParams.mobile_number));
                                EventBus.getDefault().postSticky(new ListingBeneficiaryEvent(verificationStatus, beneficiary_id,
                                        getIntent().getStringExtra(ApiParams.ben_account),
                                        getIntent().getStringExtra(ApiParams.ben_ifsc),
                                        getIntent().getStringExtra(ApiParams.mobile_number),
                                        getIntent().getStringExtra(ApiParams.name)));
                                finish();
                            } else {
                                makeErrorDialog(mMessage, "false");
                            }
                        } catch (Exception e) {
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(">>exception", "onResponse: " + error.getMessage());
                        dialog.dismiss();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put(ApiParams.otp, etOtp.getText().toString());
                map.put(ApiParams.user_id, Prefes.getUserID(DmtCustVerifiOtpActivity.this));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(DmtCustVerifiOtpActivity.this));
                return params;
            }

        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(DmtCustVerifiOtpActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void resendOtpRequest() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, BuildConfig.APP_BASE_URL + BuildConfig.beginResendValidationByOtp,
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
                                Toast.makeText(DmtCustVerifiOtpActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                M.dialogOk(DmtCustVerifiOtpActivity.this, mMessage, "Error");
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
                map.put(ApiParams.fname, fname);
                map.put(ApiParams.lname, lname);
                map.put(ApiParams.ben_account, ben_account);
                map.put(ApiParams.ben_ifsc, ben_ifsc);
                map.put(ApiParams.beneficiary_id, beneficiary_id);
                map.put(ApiParams.mobile_number, mobile_number);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(DmtCustVerifiOtpActivity.this));
                return params;
            }

        };
        MySingleton.getInstance(DmtCustVerifiOtpActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void makeErrorDialog(String mMessage, final String errorCode) {
        final Dialog errorDialog = M.inflateDialog(DmtCustVerifiOtpActivity.this, R.layout.dialog_error_message_rcg);
        NormalTextView errorMessage = errorDialog.findViewById(R.id.errorMessage);
        NormalButton okButton = errorDialog.findViewById(R.id.okButton);
        LinearLayout closeButton = errorDialog.findViewById(R.id.closeButton);
        errorMessage.setText(mMessage);
        if (errorCode.equalsIgnoreCase("false")) {
            okButton.setText("Cancel");
        } else {
            okButton.setText("Try Again");
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errorCode.equalsIgnoreCase("false")) {
                    Intent homeIntent = new Intent(DmtCustVerifiOtpActivity.this, MoneyTransferActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
