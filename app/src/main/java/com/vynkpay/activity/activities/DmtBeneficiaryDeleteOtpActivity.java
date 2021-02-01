package com.vynkpay.activity.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.vynkpay.events.UpdateDmtFields;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DmtBeneficiaryDeleteOtpActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.verifyButton)
    NormalButton verifyButton;
    @BindView(R.id.resendText)
    NormalTextView resendText;
    @BindView(R.id.timerText)
    NormalTextView timerText;
    @BindView(R.id.etOtp)
    NormalEditText etOtp;
    Dialog dialog;
    String beneficiary_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_otp_rcg);
        ButterKnife.bind(DmtBeneficiaryDeleteOtpActivity.this);
        //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        dialog = M.showDialog(DmtBeneficiaryDeleteOtpActivity.this, "", false, false);
        setListeners();

        beneficiary_id = getIntent().getStringExtra(ApiParams.beneficiary_id);

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
    }

    private void setListeners() {
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etOtp.getText().length() == 0 || etOtp.getText().toString().trim().length() == 0) {
                    Toast.makeText(DmtBeneficiaryDeleteOtpActivity.this, getString(R.string.valid_otp), Toast.LENGTH_SHORT).show();
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


    String mMessage, mSuccess;

    private void verifyRequest() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.deleteWithVerification,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("sdasdasdsgg", response);
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>otpResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                EventBus.getDefault().postSticky(new UpdateDmtFields(true));
                                finish();
                            } else {
                                // {"success":false,"inputs":{"number":"9779573352","type":23,"amount":1,"amount_all":1,"remid":"6769830","lname":"","fname":"","pin":"","otp":"1234","beneficiary_id":"16456998","ben_account":"","ben_ifsc":""},"pay_response":{"BEGIN":"","CERT":"EF494107467CDC28D5CD3EEBC0F154BC63C3CD4A","ERROR":"224","RESULT":"1","DATE":"29.05.2020 05:27:20","OPERATOR_ERROR_MESSAGE":"OTP invalid","SESSION":"97795733521590730038","END":"","BEGIN SIGNATURE":"","hE9rcBhdyN6JnixHwYgI+TV1gvZstYTCJSmOQQkRdRxqhy+8tetlAPvD\/9x2CU6W\ntj3D+SvAtX4NJZ9ir8JnJ0bm3RDxJNdaSqAjq8hJgt0bU\/XVpODA7NodQryoHcDD\nh2e5+qHy5M0zsHLJRVCU73U3Tu6Lsa0JseEXlkAvyN8":"\nEND SIGNATURE","":""},"message":""}
                                JSONObject pay_response=jsonObject.getJSONObject("pay_response");
                                String OPERATOR_ERROR_MESSAGE=pay_response.optString("OPERATOR_ERROR_MESSAGE");
                                makeErrorDialog(OPERATOR_ERROR_MESSAGE, "false");
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
                Map<String, String> map = new HashMap<String, String>();
                map.put(ApiParams.otp, etOtp.getText().toString());
                map.put(ApiParams.user_id, Prefes.getUserID(DmtBeneficiaryDeleteOtpActivity.this));
                map.put(ApiParams.beneficiary_id, beneficiary_id);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(DmtBeneficiaryDeleteOtpActivity.this));
                return params;
            }

        };
        stringRequest.setShouldCache(false);
        MySingleton.getInstance(DmtBeneficiaryDeleteOtpActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void resendOtpRequest() {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, BuildConfig.APP_BASE_URL + BuildConfig.normalDelete,
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
                                //Toast.makeText(DmtBeneficiaryDeleteOtpActivity.this, mMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                M.dialogOk(DmtBeneficiaryDeleteOtpActivity.this, mMessage, "Error");
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
                map.put(ApiParams.beneficiary_id, beneficiary_id);
                map.put(ApiParams.user_id, Prefes.getUserID(DmtBeneficiaryDeleteOtpActivity.this));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(DmtBeneficiaryDeleteOtpActivity.this));
                return params;
            }

        };
        MySingleton.getInstance(DmtBeneficiaryDeleteOtpActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    private void makeErrorDialog(String mMessage, final String errorCode) {
        final Dialog errorDialog = M.inflateDialog(DmtBeneficiaryDeleteOtpActivity.this, R.layout.dialog_error_message_rcg);
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
                    Intent homeIntent = new Intent(DmtBeneficiaryDeleteOtpActivity.this, MoneyTransferActivity.class);
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

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(DmtBeneficiaryDeleteOtpActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(DmtBeneficiaryDeleteOtpActivity.this,DmtBeneficiaryDeleteOtpActivity.this::finishAffinity);
        }
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
