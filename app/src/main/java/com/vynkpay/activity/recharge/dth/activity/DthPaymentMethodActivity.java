package com.vynkpay.activity.recharge.dth.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vynkpay.BuildConfig;
import com.vynkpay.activity.recharge.datacard.activity.DataCardPaymentMethodActivity;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.URLS;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DthPaymentMethodActivity extends AppCompatActivity {
    String _AMOUNT = "", _TYPE = "", _OPERATOR_ID = "", _MOBILE_NUMBER = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.txtConnectAmount)
    NormalTextView txtConnectAmount;
    @BindView(R.id.txtRechargeAmount)
    NormalTextView txtRechargeAmount;
    @BindView(R.id.txtBalance)
    NormalTextView txtBalance;
    @BindView(R.id.layProceed)
    LinearLayout makepaymentButton;
    @BindView(R.id.txtMobileNumber)
    NormalTextView txtMobileNumber;
    @BindView(R.id.toolbar_title)
    NormalTextView toolbar_title;
    @BindView(R.id.tvMobileNumber)
    NormalTextView tvMobileNumber;
    Dialog dialog;
    JSONObject userPreference;
    String accessToken, _LABEL = "";
    @BindView(R.id.orendaWalletAmount)
    NormalTextView orendaWalletAmount;
    @BindView(R.id.payUPaid)
    NormalTextView payUPaid;
    @BindView(R.id.payUCheck)
    CheckBox payUCheck;
    @BindView(R.id.orendaCheck)
    CheckBox orendaCheck;

    @BindView(R.id.walletBalance)
    TextView walletBalance;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method__dth_rcg);
        ButterKnife.bind(DthPaymentMethodActivity.this);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);

        dialog = M.showDialog(DthPaymentMethodActivity.this, "", true, true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setListeners();
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar_title.setText("Payment Method");
        try {
            userPreference = new JSONObject(Prefes.getUserData(DthPaymentMethodActivity.this));
            accessToken = Prefes.getAccessToken(DthPaymentMethodActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        _AMOUNT = getIntent().getStringExtra("amount");
        _TYPE = getIntent().getStringExtra("type");
        _OPERATOR_ID = getIntent().getStringExtra("operator_id");

        Log.i(">>operatorId", "method: " + _OPERATOR_ID + "\n" + _TYPE);
        _MOBILE_NUMBER = getIntent().getStringExtra("mobile");
        _LABEL = getIntent().getStringExtra("label");
        tvMobileNumber.setText("DTH Recharge : " + _MOBILE_NUMBER);
        fetchWalletData(accessToken);
    }

    String mSuccess, mMessage;
    double PLAIN_AMOUNT, WALLET_BALANCE;

    @BindView(R.id.orendaWalletLayout)
    LinearLayout orendaWalletLayout;
    private void fetchWalletData(final String accessToken) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BuildConfig.APP_BASE_URL + URLS.wallet,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>walletData", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data);
                                String balance = jsonObject1.getString(ApiParams.balance);

                                WALLET_BALANCE = Double.parseDouble(balance);

                                walletBalance.setText("Available Balance "+" "+WALLET_BALANCE);


                                txtConnectAmount.setText(Functions.CURRENCY_SYMBOL_USER + " " + balance);

                                PLAIN_AMOUNT = Double.parseDouble(_AMOUNT);
                                txtBalance.setText(Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT);
                                txtRechargeAmount.setText(Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT);


                                if (WALLET_BALANCE == 0) {
                                    orendaCheck.setEnabled(false);
                                    orendaWalletLayout.setAlpha(.5f);
                                    orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL_USER + " " + 0 + "");
                                    payUPaid.setText(Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + "");
                                    payUCheck.setChecked(true);
                                    orendaCheck.setChecked(false);

                                    payUCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));

                                            } else {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                            }
                                        }
                                    });


                                } else if (WALLET_BALANCE < PLAIN_AMOUNT) {
                                    orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL_USER + " " + WALLET_BALANCE + "");
                                    payUPaid.setText(Functions.CURRENCY_SYMBOL_USER + " " + (String.format("%.2f", PLAIN_AMOUNT - WALLET_BALANCE)) + "");
                                    payUCheck.setChecked(true);
                                    orendaCheck.setChecked(true);

                                    payUCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(true);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                                payUPaid.setText(Functions.CURRENCY_SYMBOL_USER + " " + (String.format("%.2f", PLAIN_AMOUNT - WALLET_BALANCE)) + "");
                                                makepaymentButton.setEnabled(true);
                                                makepaymentButton.setAlpha(1.0f);
                                            } else {
                                                if (orendaCheck.isChecked()) {
                                                    payUCheck.setChecked(false);
                                                    orendaCheck.setChecked(true);
                                                    orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                                    payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                    makepaymentButton.setEnabled(false);
                                                    makepaymentButton.setAlpha(.5f);
                                                } else {
                                                    payUCheck.setChecked(true);
                                                }

                                            }
                                        }
                                    });
                                    orendaCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(true);
                                                orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL_USER + " " + WALLET_BALANCE + "");
                                                payUPaid.setText(Functions.CURRENCY_SYMBOL_USER + " " + (String.format("%.2f", PLAIN_AMOUNT - WALLET_BALANCE)) + "");
                                            } else {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                            }
                                        }
                                    });


                                } else if (WALLET_BALANCE >= PLAIN_AMOUNT) {
                                    orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + "");
                                    payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0) + "");
                                    payUCheck.setChecked(false);
                                    orendaCheck.setChecked(true);

                                    payUCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                            } else {
                                                payUCheck.setChecked(false);
                                                orendaCheck.setChecked(true);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                            }
                                        }
                                    });
                                    orendaCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(false);
                                                orendaCheck.setChecked(true);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                            } else {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL_USER + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL_USER + " " + PLAIN_AMOUNT + ""));
                                            }
                                        }
                                    });
                                }


                            } else {
                                M.dialogOk(DthPaymentMethodActivity.this, mMessage, "Error");
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
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            M.dialogOk(DthPaymentMethodActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof AuthFailureError) {
                            M.dialogOk(DthPaymentMethodActivity.this, "You are not authorized", "Error");
                        } else if (error instanceof ServerError) {
                            M.dialogOk(DthPaymentMethodActivity.this, "Server issue", "Error");
                        } else if (error instanceof NetworkError) {
                            M.dialogOk(DthPaymentMethodActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof ParseError) {
                            M.dialogOk(DthPaymentMethodActivity.this, "Internal error", "Error");
                        }
                        dialog.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, accessToken);
                return params;
            }
        };
        MySingleton.getInstance(DthPaymentMethodActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    float payu, orenda, AMOUNT_FOR_PAYU, AMOUNT_FOR_ORENDA;
    private void setListeners() {
        makepaymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!payUCheck.isChecked() && orendaCheck.isChecked()) {
                    orenda = Float.parseFloat(orendaWalletAmount.getText().toString().replace(Functions.CURRENCY_SYMBOL_USER + " ", ""));
                    payu = 0;
                    AMOUNT_FOR_ORENDA = payu + orenda;
                    //Log.i(">>hello", "onClick: "+String.format(Locale.US, "%.2f", 1.2975118));
                    makeRechargeRequest(AMOUNT_FOR_ORENDA);
                } else {
                    if (payUCheck.isChecked()) {
                        payu = Float.parseFloat(payUPaid.getText().toString().replace(Functions.CURRENCY_SYMBOL_USER + " ", ""));
                    } else {
                        payu = 0;
                    }
                    if (orendaCheck.isChecked()) {
                        orenda = Float.parseFloat(orendaWalletAmount.getText().toString().replace(Functions.CURRENCY_SYMBOL_USER + " ", ""));
                    } else {
                        orenda = 0;
                    }
                    AMOUNT_FOR_PAYU = payu;
                    makePaymentFromPayUFirst(AMOUNT_FOR_PAYU);
                }
            }
        });
    }
    private void makePaymentFromPayUFirst(final float AMOUNT_FOR_PAYU) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.add_money_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>walletData", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data);
                                String amount = jsonObject1.getString(ApiParams.amount);
                                String productinfo = jsonObject1.getString(ApiParams.productinfo);
                                String surl = jsonObject1.getString(ApiParams.surl);
                                String furl = jsonObject1.getString(ApiParams.furl);
                                String key = jsonObject1.getString(ApiParams.key);
                                String txnid = jsonObject1.getString(ApiParams.txnid);
                                String firstname = jsonObject1.getString(ApiParams.firstname);
                                String email = jsonObject1.getString(ApiParams.email);
                                String phone = jsonObject1.getString(ApiParams.phone);
                                String hash = jsonObject1.getString(ApiParams.hash);
                                String vas_for_mobile_sdk_hash = jsonObject1.getString(ApiParams.vas_for_mobile_sdk_hash);
                                String payment_related_details_for_mobile_sdk_hash = jsonObject1.getString(ApiParams.payment_related_details_for_mobile_sdk_hash);
                                String get_merchant_ibibo_codes_hash = jsonObject1.getString(ApiParams.get_merchant_ibibo_codes_hash);
                                ///
                                String merchantId = jsonObject1.getString("merchantId");
                                boolean mode = jsonObject1.getBoolean("DEBUG");


                            } else {
                                M.dialogOk(DthPaymentMethodActivity.this, mMessage, "Error");
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
                map.put(ApiParams.amount, AMOUNT_FOR_PAYU + "");
                map.put(ApiParams.productinfo, ApiParams.mobile_recharge);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, accessToken);
                return params;
            }

        };
        MySingleton.getInstance(DthPaymentMethodActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,

                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivityLOGG", "request code " + requestCode + " resultcode " + resultCode);

    }


    private void respondToPayResponse(final JSONObject payUResponse) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + BuildConfig.pay_u_sucsess_final,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>ourResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                //from server
                              //  String txn = jsonObject.getJSONObject(ApiParams.data).getString(ApiParams.txn);
                                //from Payu
                             //   String payu = payUResponse.getString("txnid");
                             //   String date = payUResponse.getString("addedon");
                              //  String amount = payUResponse.getString("amount");
                             //   String phone = payUResponse.getString("phone");
                             //   String title = "eSampark India wallet received payment of " + Double.parseDouble(amount);
                              //  String success = mSuccess;
                              //  Log.i(">>logi", "onResponse: " + _AMOUNT + "");
                                makeRechargeRequest(Float.parseFloat(_AMOUNT));
                                //makeDialogSuccess(txn, date, amount, phone, title, success, payu);
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
                                    }else if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase("402")){
                                        makeErrorDialog(mMessage, jsonObject.getString(ApiParams.error_code));
                                    }
                                } else {
                                    makeErrorDialog(mMessage, "000");
                                }
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
                map.put(ApiParams.payu_response, payUResponse.toString());
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(DthPaymentMethodActivity.this));
                return params;
            }
        };
        MySingleton.getInstance(DthPaymentMethodActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
    private void makeRechargeRequest(final float AMOUNT_FOR_ORENDA) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.recharge,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>successFullRecharge", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString("success");
                            mMessage = jsonObject.optString("message");
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data).getJSONObject(ApiParams.transaction);
                                String txnId = jsonObject1.getString(ApiParams.txn);
                                String dateTime = jsonObject1.getString(ApiParams.created_at);
                                String amount = jsonObject1.getString(ApiParams.amount);
                                makeDialogSuccess(txnId, dateTime, amount, _MOBILE_NUMBER, mMessage, mSuccess);
                            } else {
                                // 2 for fail
                                if (jsonObject.has(ApiParams.error_code)) {
                                    if (jsonObject.getString(ApiParams.error_code).equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400)) {
                                        Iterator<?> keys = jsonObject.getJSONObject(ApiParams.errors).keys();
                                        while (keys.hasNext()) {
                                            String key = (String) keys.next();
                                            if (jsonObject.getJSONObject(ApiParams.errors).get(key) instanceof String) {
                                                String message = jsonObject.getJSONObject(ApiParams.errors).get(key).toString();
                                                makeErrorDialog(message, ApiParams.ErrorCodeArray._ERROR_CODE_400);
                                            }
                                        }
                                    }
                                } else {
                                    switch (jsonObject.getString(ApiParams.status)) {
                                        case "2":
                                            JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data).getJSONObject(ApiParams.transaction);
                                            String txnId = jsonObject1.getString(ApiParams.txn);
                                            String dateTime = jsonObject1.getString(ApiParams.created_at);
                                            makeDialogSuccess(txnId, dateTime, _AMOUNT, _MOBILE_NUMBER, mMessage, mSuccess);
                                            break;
                                        case "3":
                                            JSONObject jsonObject12 = jsonObject.getJSONObject(ApiParams.data).getJSONObject(ApiParams.transaction);
                                            String txnId2 = jsonObject12.getString(ApiParams.txn);
                                            String dateTime2 = jsonObject12.getString(ApiParams.created_at);
                                            makeDialogSuccess(txnId2, dateTime2, _AMOUNT, _MOBILE_NUMBER, mMessage, mSuccess);
                                            break;
                                        case "4":
                                            makeErrorDialog(mMessage, ApiParams.ErrorCodeArray._ERROR_CODE_400);
                                            break;
                                            default:
                                                makeErrorDialog(mMessage, ApiParams.ErrorCodeArray._ERROR_CODE_400);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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
                Map<String, String> params = new HashMap<String, String>();
                Log.i(">>params", "getParams: " +
                        _MOBILE_NUMBER +
                        AMOUNT_FOR_ORENDA + "" +
                        _OPERATOR_ID + _TYPE);
                params.put(ApiParams.mobile_number, _MOBILE_NUMBER);
                params.put(ApiParams.amount, AMOUNT_FOR_ORENDA + "");
                params.put("operator_id", _OPERATOR_ID);
                params.put("plan_description", Prefes.getDescription(DthPaymentMethodActivity.this));
                params.put("plan_talktime", Prefes.getTalktime(DthPaymentMethodActivity.this));
                params.put("plan_validity", Prefes.getValidity(DthPaymentMethodActivity.this));
                params.put("plan_name", Prefes.getPlan(DthPaymentMethodActivity.this));
                params.put(ApiParams.type, _TYPE);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Log.i(">>operatorId", "getHeaders: " + accessToken);
                params.put(ApiParams.access_token, Prefes.getAccessToken(DthPaymentMethodActivity.this));
                params.put("Accept", "application/json");
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(stringRequest);

       // MySingleton.getInstance(DthPaymentMethodActivity.this).addToRequestQueue(stringRequest);
    }

    private void makeDialogSuccess(String txnId, String dateTime, String amount, String _MOBILE_NUMBER, String title, String mSuccess) {
        final Dialog dialog1 = M.inflateDialog(DthPaymentMethodActivity.this, R.layout.dialog_payment_result_rcg);
        NormalTextView tvMobileNumber = dialog1.findViewById(R.id.tvMobileNumber);
        NormalTextView tvAmount = dialog1.findViewById(R.id.tvAmount);
        NormalTextView forWhatPaid = dialog1.findViewById(R.id.forWhatPaid);
        NormalTextView tvTransactionId = dialog1.findViewById(R.id.tvTransactionId);
        NormalTextView tvDateTime = dialog1.findViewById(R.id.tvDateTime);
        NormalButton okButton = dialog1.findViewById(R.id.okButton);
        LinearLayout closeButton = dialog1.findViewById(R.id.closeButton);
        ImageView imageResult = dialog1.findViewById(R.id.imageResult);
        NormalTextView textResult = dialog1.findViewById(R.id.textResult);
        tvMobileNumber.setText(_MOBILE_NUMBER);
        double planAmount = Double.parseDouble(amount);
        tvAmount.setText(planAmount + "");
        forWhatPaid.setText(_LABEL);
        tvTransactionId.setText(txnId.equals("") || txnId.equalsIgnoreCase("null") ? "" : txnId);
        tvDateTime.setText(M.changeDateTimeFormat(dateTime));
        textResult.setText(title);
        if (mSuccess.equalsIgnoreCase("true")) {
            imageResult.setBackgroundDrawable(getResources().getDrawable(R.drawable.success));
        } else {
            imageResult.setBackgroundDrawable(getResources().getDrawable(R.drawable.failed));
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                if(sp.getString("value","").equals("Global") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("NO")){
                    startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("YES")){
                    startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }


                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("YES")){
                    startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("NO")){
                    startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                if(sp.getString("value","").equals("Global") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("NO")){
                    startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("YES")){
                    startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }


                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("YES")){
                    startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("NO")){
                    startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        });
        dialog1.show();
    }

    private void makeErrorDialog(String mMessage, final String errorCode) {
        final Dialog errorDialog = M.inflateDialog(DthPaymentMethodActivity.this, R.layout.dialog_error_message_rcg);
        NormalTextView errorMessage = errorDialog.findViewById(R.id.errorMessage);
        NormalButton okButton = errorDialog.findViewById(R.id.okButton);
        LinearLayout closeButton = errorDialog.findViewById(R.id.closeButton);
        errorMessage.setText(mMessage);
        if (errorCode.equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400)) {
            okButton.setText("Home");
        } else {
            okButton.setText("Try Again");
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errorCode.equalsIgnoreCase(ApiParams.ErrorCodeArray._ERROR_CODE_400)) {
                    if(sp.getString("value","").equals("Global") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("NO")){
                        startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                    else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("YES")){
                        startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }


                    else if(sp.getString("value","").equals("India") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("YES")){
                        startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                    else if(sp.getString("value","").equals("India") && Prefes.getisIndian(DthPaymentMethodActivity.this).equals("NO")){
                        startActivity(new Intent(DthPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
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
