package com.vynkpay.activity.giftcards.activity;

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
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.vynkpay.BuildConfig;
import com.vynkpay.activity.PinActivity;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.models.SuccessDialogModel;
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

public class GiftCardPayConfirmation extends AppCompatActivity {
    @BindView(R.id.orderButton)
    NormalButton orderButton;
    @BindView(R.id.giftCardName)
    NormalTextView giftCardName;
    @BindView(R.id.promoCodeName)
    NormalTextView promoCodeName;
    @BindView(R.id.benName)
    NormalTextView benName;
    @BindView(R.id.benEmail)
    NormalTextView benEmail;
    @BindView(R.id.benMobileNumber)
    NormalTextView benMobileNumber;
    @BindView(R.id.preTotalTransfer)
    NormalTextView preTotalTransfer;
    @BindView(R.id.totalTransfer)
    NormalTextView totalTransfer;
    String access_token;
    Dialog dialog;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbar_title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.orendaWalletAmount)
    NormalTextView orendaWalletAmount;
    @BindView(R.id.payUPaid)
    NormalTextView payUPaid;
    @BindView(R.id.payUCheck)
    CheckBox payUCheck;
    @BindView(R.id.orendaWalletLayout)
    LinearLayout orendaWalletLayout;
    @BindView(R.id.orendaCheck)
    CheckBox orendaCheck;
    String product_id;
    String product_name;
    String product_image, giftName, giftCode, firstName, lastNAme, email, mobile, amount, message;
    SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_pay_confirmation_rcg);
         sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);

        ButterKnife.bind(GiftCardPayConfirmation.this);
        access_token = M.fetchUserTrivialInfo(GiftCardPayConfirmation.this, ApiParams.access_token);
        dialog = M.showDialog(GiftCardPayConfirmation.this, "", true, true);
        setListeners();
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar_title.setText("Confirm Payment");
        product_id = getIntent().getStringExtra("id");
        product_name = getIntent().getStringExtra("name");
        product_image = getIntent().getStringExtra("image");
        firstName = getIntent().getStringExtra("firstName");
        lastNAme = getIntent().getStringExtra("lastNAme");
        email = getIntent().getStringExtra("email");
        mobile = getIntent().getStringExtra("mobile");
        giftName = getIntent().getStringExtra("giftName");
        giftCode = getIntent().getStringExtra("giftCode");
        amount = getIntent().getStringExtra("amount");
        message = getIntent().getStringExtra("message");

        giftCardName.setText(product_name);

        promoCodeName.setText(giftCode);
        benName.setText(firstName + " " + lastNAme);
        benEmail.setText(email);
        benMobileNumber.setText(mobile);
        preTotalTransfer.setText(Functions.CURRENCY_SYMBOL + "" + Double.parseDouble(amount) + "");
        totalTransfer.setText(Functions.CURRENCY_SYMBOL + "" + Double.parseDouble(amount) + "");

        dialog.show();
        fetchWalletData();

    }

    float payu, orenda, AMOUNT_FOR_PAYU, AMOUNT_FOR_ORENDA;

    private void setListeners() {
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!payUCheck.isChecked() && orendaCheck.isChecked()) {
                    orenda = Float.parseFloat(orendaWalletAmount.getText().toString().replace(Functions.CURRENCY_SYMBOL + " ", ""));
                    payu = 0;
                    AMOUNT_FOR_ORENDA = payu + orenda;
                    orderGiftCard(AMOUNT_FOR_ORENDA);
                } else {
                    if (payUCheck.isChecked()) {
                        payu = Float.parseFloat(payUPaid.getText().toString().replace(Functions.CURRENCY_SYMBOL + " ", ""));
                    } else {
                        payu = 0;
                    }
                    if (orendaCheck.isChecked()) {
                        orenda = Float.parseFloat(orendaWalletAmount.getText().toString().replace(Functions.CURRENCY_SYMBOL + " ", ""));
                    } else {
                        orenda = 0;
                    }
                    AMOUNT_FOR_PAYU = payu;
                    makePaymentFromPayUFirst(AMOUNT_FOR_PAYU);
                }
            }
        });
    }

    double PLAIN_AMOUNT, WALLET_BALANCE;
    String mSuccess, mMessage;

    private void fetchWalletData() {
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

                                PLAIN_AMOUNT = Double.parseDouble(amount);

                                if (WALLET_BALANCE == 0) {
                                    orendaCheck.setEnabled(false);
                                    orendaWalletLayout.setAlpha(.5f);
                                    orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL + " " + 0 + "");
                                    payUPaid.setText(Functions.CURRENCY_SYMBOL + " " + PLAIN_AMOUNT + "");
                                    payUCheck.setChecked(true);
                                    orendaCheck.setChecked(false);

                                    payUCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL + " " + PLAIN_AMOUNT + ""));

                                            } else {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL + " " + PLAIN_AMOUNT + ""));
                                            }
                                        }
                                    });


                                } else if (WALLET_BALANCE < PLAIN_AMOUNT) {
                                    orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL + " " + WALLET_BALANCE + "");
                                    payUPaid.setText(Functions.CURRENCY_SYMBOL + " " + (PLAIN_AMOUNT - WALLET_BALANCE) + "");
                                    payUCheck.setChecked(true);
                                    orendaCheck.setChecked(true);

                                    payUCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(true);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL + " " + PLAIN_AMOUNT + ""));
                                                payUPaid.setText(Functions.CURRENCY_SYMBOL + " " + (PLAIN_AMOUNT - WALLET_BALANCE) + "");
                                                orderButton.setEnabled(true);
                                                orderButton.setAlpha(1.0f);
                                            } else {
                                                if (orendaCheck.isChecked()) {
                                                    payUCheck.setChecked(false);
                                                    orendaCheck.setChecked(true);
                                                    orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL + " " + PLAIN_AMOUNT + ""));
                                                    payUPaid.setText((Functions.CURRENCY_SYMBOL + " " + 0 + ""));
                                                    orderButton.setEnabled(false);
                                                    orderButton.setAlpha(.5f);
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
                                                orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL + " " + WALLET_BALANCE + "");
                                                payUPaid.setText(Functions.CURRENCY_SYMBOL + " " + (PLAIN_AMOUNT - WALLET_BALANCE) + "");
                                            } else {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL + " " + PLAIN_AMOUNT + ""));
                                            }
                                        }
                                    });


                                } else if (WALLET_BALANCE >= PLAIN_AMOUNT) {
                                    orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL + " " + PLAIN_AMOUNT + "");
                                    payUPaid.setText((Functions.CURRENCY_SYMBOL + " " + 0) + "");
                                    payUCheck.setChecked(false);
                                    orendaCheck.setChecked(true);

                                    payUCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL + " " + PLAIN_AMOUNT + ""));
                                            } else {
                                                payUCheck.setChecked(false);
                                                orendaCheck.setChecked(true);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL + " " + PLAIN_AMOUNT + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL + " " + 0 + ""));
                                            }
                                        }
                                    });
                                    orendaCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            if (isChecked) {
                                                payUCheck.setChecked(false);
                                                orendaCheck.setChecked(true);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL + " " + PLAIN_AMOUNT + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL + " " + 0 + ""));
                                            } else {
                                                payUCheck.setChecked(true);
                                                orendaCheck.setChecked(false);
                                                orendaWalletAmount.setText((Functions.CURRENCY_SYMBOL + " " + 0 + ""));
                                                payUPaid.setText((Functions.CURRENCY_SYMBOL + " " + PLAIN_AMOUNT + ""));
                                            }
                                        }
                                    });
                                }

                            } else {
                                M.dialogOk(GiftCardPayConfirmation.this, mMessage, "Error");
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
                            M.dialogOk(GiftCardPayConfirmation.this, "Please check internet connection", "Error");
                        } else if (error instanceof AuthFailureError) {
                            M.dialogOk(GiftCardPayConfirmation.this, "You are not authorized", "Error");
                        } else if (error instanceof ServerError) {
                            M.dialogOk(GiftCardPayConfirmation.this, "Server issue", "Error");
                        } else if (error instanceof NetworkError) {
                            M.dialogOk(GiftCardPayConfirmation.this, "Please check internet connection", "Error");
                        } else if (error instanceof ParseError) {
                            M.dialogOk(GiftCardPayConfirmation.this, "Internal error", "Error");
                        }
                        dialog.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, access_token);
                return params;
            }
        };
        MySingleton.getInstance(GiftCardPayConfirmation.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


    private void orderGiftCard(final float amount) {
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.giftCardOrder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            dialog.dismiss();
                            JSONObject jsonObject = new JSONObject(response);
                            Log.i(">>changePassResponse", "onResponse: " + jsonObject.toString());
                            mSuccess = jsonObject.optString(ApiParams.success);
                            mMessage = jsonObject.optString(ApiParams.message);
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject transactionObject = jsonObject.getJSONObject(ApiParams.data).getJSONObject("transaction");
                                String txn = transactionObject.getString("txn");
                                String created_at = transactionObject.getString("created_at");
                                String amount = transactionObject.getString("amount");
                                makeDialogSuccess(txn, created_at, amount, mMessage, mSuccess, giftName);
                            } else {
                                makeErrorDialog(mMessage, "000");
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
                map.put(ApiParams.beneficiary_first_name, firstName);
                map.put(ApiParams.beneficiary_last_name, lastNAme);
                map.put(ApiParams.beneficiary_mobile, mobile);
                map.put(ApiParams.product_id, product_id);
                map.put(ApiParams.email, email);
                map.put(ApiParams.price, amount + "");
                map.put(ApiParams.quantity, "1");
                map.put(ApiParams.gift_message, message);
                map.put(ApiParams.theme, "");
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, access_token);
                return params;
            }

        };
        MySingleton.getInstance(GiftCardPayConfirmation.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                M.dialogOk(GiftCardPayConfirmation.this, mMessage, "Error");
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
                params.put(ApiParams.access_token, access_token);
                return params;
            }

        };
        MySingleton.getInstance(GiftCardPayConfirmation.this).addToRequestQueue(stringRequest);
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
                                orderGiftCard(Float.parseFloat(amount));
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
                params.put(ApiParams.access_token, access_token);
                return params;
            }
        };
        MySingleton.getInstance(GiftCardPayConfirmation.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    Dialog errorDialog;

    private void makeErrorDialog(String mMessage, final String errorCode) {
        errorDialog = M.inflateDialog(GiftCardPayConfirmation.this, R.layout.dialog_error_message_rcg);
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


                    if(sp.getString("value","").equals("Global") && Prefes.getisIndian(GiftCardPayConfirmation.this).equals("NO")){
                        startActivity(new Intent(GiftCardPayConfirmation.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                    else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(GiftCardPayConfirmation.this).equals("YES")){
                        startActivity(new Intent(GiftCardPayConfirmation.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }


                    else if(sp.getString("value","").equals("India") && Prefes.getisIndian(GiftCardPayConfirmation.this).equals("YES")){
                        startActivity(new Intent(GiftCardPayConfirmation.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                    else if(sp.getString("value","").equals("India") && Prefes.getisIndian(GiftCardPayConfirmation.this).equals("NO")){
                        startActivity(new Intent(GiftCardPayConfirmation.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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
        if (errorDialog != null) {
            errorDialog.show();
        }
    }

    SuccessDialogModel successDialogModel;

    private void makeDialogSuccess(String txnId, String dateTime, String amount, String title, String mSuccess, String name) {
        successDialogModel = M.makeSuccessDialog(GiftCardPayConfirmation.this);
        successDialogModel.getTvMobileNumber().setText(name);
        double temp_planAmount = Double.parseDouble(amount);
        successDialogModel.getTvAmount().setText(Functions.CURRENCY_SYMBOL + " " + temp_planAmount + "");
        successDialogModel.getForWhatPaid().setText("Gift Card");
        successDialogModel.getTvTransactionId().setText(txnId.equals("") || txnId.equalsIgnoreCase("null") ? "" : txnId);
        successDialogModel.getTvDateTime().setText(M.changeDateTimeFormat(dateTime));
        successDialogModel.getTextResult().setText(title);
        if (mSuccess.equalsIgnoreCase("true")) {
            successDialogModel.getImageResult().setBackgroundDrawable(getResources().getDrawable(R.drawable.success));
        } else {
            successDialogModel.getImageResult().setBackgroundDrawable(getResources().getDrawable(R.drawable.failed));
        }
        successDialogModel.getOkButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                M.makeNavigationToHome(successDialogModel.getSuccessDialog(), GiftCardPayConfirmation.this);
            }
        });
        successDialogModel.getCloseButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.makeNavigationToHome(successDialogModel.getSuccessDialog(), GiftCardPayConfirmation.this);
            }
        });
        successDialogModel.getSuccessDialog().show();
    }
}
