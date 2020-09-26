package com.vynkpay.activity.recharge.datacard.activity;

import android.app.Activity;
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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.vynkpay.BuildConfig;
import com.vynkpay.activity.PinActivity;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.AddMoneyRazorResponse;
import com.vynkpay.retrofit.model.GetProfileResponse;
import com.vynkpay.retrofit.model.GetWalletResponse;
import com.vynkpay.retrofit.model.ReddemAmountResponse;
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
import retrofit2.Call;
import retrofit2.Callback;

public class DataCardPaymentMethodActivity extends AppCompatActivity implements PaymentResultWithDataListener {
    String _AMOUNT = "", _TYPE = "", _OPERATOR_ID = "", _MOBILE_NUMBER = "",percent,points, _LAND_LINE_NUMBER = "",
            _ACCOUNT_NUMBER = "";
    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
    String accessToken;
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
    double PLAIN_AMOUNT, WALLET_BALANCE;

    String walletBalane;



    @BindView(R.id.vCashPercent)
    TextView vCashPercent;


    @BindView(R.id.vCashBalance)
    TextView vCashBalance;



    @BindView(R.id.vCashAmount)
    TextView vCashAmount;

    String razorpaykey;

    @BindView(R.id.payAmountText)
    NormalTextView payAmountText;

    @BindView(R.id.walletBalance)
    TextView walletBalance;

    @BindView(R.id.razorLayout)
    LinearLayout razorLayout;


    @BindView(R.id.payAmountPayText)
    NormalTextView payAmountPayText;

SharedPreferences sp;

    String phone ,email,countrycode;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_method_datacard_paymemt_rcg);
        ButterKnife.bind(DataCardPaymentMethodActivity.this);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);

        MainApplication.getApiService().getProfileMethod(Prefes.getAccessToken(DataCardPaymentMethodActivity.this)).enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, retrofit2.Response<GetProfileResponse> response) {
                if(response.isSuccessful() ){

                    if(response.body().getSuccess()){

                        phone=response.body().getData().getMobileNumber();
                        email=response.body().getData().getEmail();
                        countrycode=response.body().getData().getZipCode();

                    }


                    else {


                    }
                }

                else {

                }
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {

            }
        });

        fetchWalletData();
        dialog = M.showDialog(DataCardPaymentMethodActivity.this, "", true, true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar_title.setText("Payment Method");
        try {
            userPreference = new JSONObject(Prefes.getUserData(DataCardPaymentMethodActivity.this));
            accessToken = userPreference.getString(ApiParams.access_token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        _AMOUNT = getIntent().getStringExtra("amount");
        txtRechargeAmount.setText(Functions.CURRENCY_SYMBOL+" "+_AMOUNT);

        _TYPE = getIntent().getStringExtra("type");
        _OPERATOR_ID = getIntent().getStringExtra("operator_id");
        Log.i(">>operatorId", "method: " + _OPERATOR_ID + "\n" + _TYPE);
        _MOBILE_NUMBER = getIntent().getStringExtra("mobile");
        tvMobileNumber.setText("Data Card Number : " + _MOBILE_NUMBER);





    }

    Dialog successDialog;
    private void makeDialogSuccess(String txnId, String dateTime, String amount, String _MOBILE_NUMBER, String title, String mSuccess) {

        successDialog = M.inflateDialog(DataCardPaymentMethodActivity.this, R.layout.dialog_payment_result_rcg);
        NormalTextView tvMobileNumber = successDialog.findViewById(R.id.tvMobileNumber);
        NormalTextView tvAmount = successDialog.findViewById(R.id.tvAmount);
        NormalTextView tvTransactionId = successDialog.findViewById(R.id.tvTransactionId);
        NormalTextView tvDateTime = successDialog.findViewById(R.id.tvDateTime);
        NormalButton okButton = successDialog.findViewById(R.id.okButton);
        LinearLayout closeButton = successDialog.findViewById(R.id.closeButton);
        ImageView imageResult = successDialog.findViewById(R.id.imageResult);
        NormalTextView forWhatPaid = successDialog.findViewById(R.id.forWhatPaid);
        NormalTextView textResult = successDialog.findViewById(R.id.textResult);
        tvMobileNumber.setText(_MOBILE_NUMBER);
        double temp_planAmount = Double.parseDouble(amount);
        tvAmount.setText(temp_planAmount + "");

        forWhatPaid.setText("Data Card Recharge");

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


                if(sp.getString("value","").equals("Global") && Prefes.getisIndian(DataCardPaymentMethodActivity.this).equals("NO")){
                    startActivity(new Intent(DataCardPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(DataCardPaymentMethodActivity.this).equals("YES")){
                    startActivity(new Intent(DataCardPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }


                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(DataCardPaymentMethodActivity.this).equals("YES")){
                    startActivity(new Intent(DataCardPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(DataCardPaymentMethodActivity.this).equals("NO")){
                    startActivity(new Intent(DataCardPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                // M.makeNavigationToHome(successDialog, PaymentMethodActivity.this);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sp.getString("value","").equals("Global") && Prefes.getisIndian(DataCardPaymentMethodActivity.this).equals("NO")){
                    startActivity(new Intent(DataCardPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(DataCardPaymentMethodActivity.this).equals("YES")){
                    startActivity(new Intent(DataCardPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }


                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(DataCardPaymentMethodActivity.this).equals("YES")){
                    startActivity(new Intent(DataCardPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(DataCardPaymentMethodActivity.this).equals("NO")){
                    startActivity(new Intent(DataCardPaymentMethodActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
                //  M.makeNavigationToHome(successDialog, PaymentMethodActivity.this);
            }
        });
        if (successDialog != null) {
            successDialog.show();
        }
    }

    Dialog errorDialog;
    private void makeErrorDialog(String mMessage, final String errorCode) {

        errorDialog = M.inflateDialog(DataCardPaymentMethodActivity.this, R.layout.dialog_error_message_rcg);
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
                    Intent homeIntent = new Intent(DataCardPaymentMethodActivity.this, HomeActivity.class);
                    homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
        if (errorDialog != null) {
            errorDialog.show();
        }
    }

    public void payNow(String toString) {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();
        co.setKeyID(razorpaykey);

        int image = R.drawable.logo; // Can be any drawable
        co.setImage(image);

        try {
            JSONObject options = new JSONObject();
            options.put("name", Prefes.getEmail(DataCardPaymentMethodActivity.this));
            options.put("description", "Pay to VynkPay");
            options.put("theme",new JSONObject("{color: '#B10D25'}"));

            //You can omit the image option to fetch the image from dashboard
        //    options.put("image", R.drawable.logo);
            // options.put("order_id",orderid);
            options.put("currency", "INR");
            String payment = toString;
            double total = Double.parseDouble(payment);
            total = total * 100;


            options.put("amount", String.format("%.2f", total));
            //  options.put("order_id",iD );

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phone);
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }



    private void fetchWalletData() {
        Dialog loader = M.showDialog(DataCardPaymentMethodActivity.this, "", false, false);
        loader.show();
        MainApplication.getApiService().walletAmountMethod(Prefes.getAccessToken(DataCardPaymentMethodActivity.this)).enqueue(new Callback<GetWalletResponse>() {
            @Override
            public void onResponse(Call<GetWalletResponse> call, retrofit2.Response<GetWalletResponse> response) {
                if(response.body().getSuccess()){
                    vCashBalance.setText(Functions.CURRENCY_SYMBOL+" "+response.body().getData().getWalletRedeem());
                    razorpaykey=response.body().getData().getRazorpikey();
                    walletBalance.setText("Available Balance "+" "+response.body().getData().getBalance());
                    walletBalane=response.body().getData().getBalance();

                    MainApplication.getApiService().redeemAmountMethod
                            (Prefes.getAccessToken(DataCardPaymentMethodActivity.this),_OPERATOR_ID,_AMOUNT).enqueue(new Callback<ReddemAmountResponse>() {
                        @Override
                        public void onResponse(Call<ReddemAmountResponse> call, retrofit2.Response<ReddemAmountResponse> response) {
                            loader.dismiss();
                            if(response.isSuccessful()){
                                if(response.body().getSuccess()){
                                    if(response.body().getData().getRedeemShow()==1){

                                        vCashPercent.setText("("+response.body().getData().getPointsRedeemed()+")");
                                        vCashAmount.setText(Functions.CURRENCY_SYMBOL+" "+response.body().getData().getPointsAmount());

                                        payAmountPayText.setText(Functions.CURRENCY_SYMBOL+" "+response.body().getData().getTotalAmount());

                                        orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL+" "+response.body().getData().getTotalAmount());
                                        payUPaid.setText(Functions.CURRENCY_SYMBOL+" "+response.body().getData().getTotalAmount());


                                        percent=response.body().getData().getPointsRedeemed();
                                        points=response.body().getData().getPointsAmount();



                                        setListeners(response.body().getData().getTotalAmount());

                                    }

                                    else {
                                        setListeners(_AMOUNT);
                                        percent="";
                                        points="";
                                        payAmountPayText.setText("Total Amount "+" "+Functions.CURRENCY_SYMBOL+" "+_AMOUNT);

                                        orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL+" "+_AMOUNT);
                                        payUPaid.setText(Functions.CURRENCY_SYMBOL+" "+_AMOUNT);

                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ReddemAmountResponse> call, Throwable t) {
                            loader.dismiss();
                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<GetWalletResponse> call, Throwable t) {
                loader.dismiss();
            }
        });
    }

    String mSuccess, mMessage;



    float payu, orenda, AMOUNT_FOR_PAYU, AMOUNT_FOR_ORENDA;
    private void setListeners(String totalAmount) {

        Log.e("wall",""+walletBalane);
        Log.e("total",""+totalAmount);


        Log.e("wall",""+Float.parseFloat(walletBalane));
        Log.e("total",""+Float.parseFloat(totalAmount));

        if(Float.parseFloat(walletBalane) < Float.parseFloat(totalAmount)){
            Toast.makeText(this, "Wallet Balance is less", Toast.LENGTH_SHORT).show();


            orendaWalletLayout.setEnabled(false);

            razorLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click","pau");

                    payUCheck.setChecked(true);
                    orendaCheck.setChecked(false);


                    makepaymentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            payNow(totalAmount);

                        }
                    });
                }
            });
        }

        else {


            payUCheck.setEnabled(false);
            orendaCheck.setEnabled(false);
            orendaWalletLayout.setEnabled(true);

            orendaWalletLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click","orenda");
                    orendaCheck.setEnabled(true);
                    orendaCheck.setChecked(true);
                    payUCheck.setChecked(false);
                    makepaymentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            makeRechargeRequest(totalAmount);
                        }
                    });

                }
            });

            razorLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("click","pau");

                    payUCheck.setChecked(true);
                    orendaCheck.setChecked(false);
                    payUCheck.setEnabled(true);

                    makepaymentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            payNow(totalAmount);

                        }
                    });
                }
            });
        }
        /*
         */

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        makepaymentButton.setEnabled(false);
        Dialog loader = M.showDialog(DataCardPaymentMethodActivity.this, "", false, false);
        loader.show();
        try {
            Log.e("razorpay_payment_id", "razorpay_payment_id" + paymentData.getData().getString("razorpay_payment_id"));
            MainApplication.getApiService().addMoneyRazorMethod(Prefes.getAccessToken(DataCardPaymentMethodActivity.this),
                    paymentData.getData().getString("razorpay_payment_id"),
                    payUPaid.getText().toString().replace(Functions.CURRENCY_SYMBOL, ""))
                    .enqueue(new Callback<AddMoneyRazorResponse>() {
                        @Override
                        public void onResponse(Call<AddMoneyRazorResponse> call, retrofit2.Response<AddMoneyRazorResponse> response) {
                            loader.dismiss();
                            if(response.isSuccessful()){
                                Toast.makeText(DataCardPaymentMethodActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                makeRechargeRequest(payUPaid.getText().toString().replace(Functions.CURRENCY_SYMBOL, ""));


                            }
                        }

                        @Override
                        public void onFailure(Call<AddMoneyRazorResponse> call, Throwable t) {
                            loader.dismiss();
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
            loader.dismiss();
        }



    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    private void makeRechargeRequest(final String AMOUNT_FOR_ORENDA) {
        Dialog loader = M.showDialog(DataCardPaymentMethodActivity.this, "", false, false);
        loader.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.recharge,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loader.dismiss();
                        Log.i(">>successFulchargeSSS", "onResponse: " + response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            mSuccess = jsonObject.optString("success");
                            mMessage = jsonObject.optString("message");
                            if (mSuccess.equalsIgnoreCase("true")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject(ApiParams.data).getJSONObject(ApiParams.transaction);
                                String txnId = jsonObject1.getString(ApiParams.txn);
                                String dateTime = jsonObject1.getString(ApiParams.created_at);
                                String amount = jsonObject1.getString(ApiParams.amount);
                                makeDialogSuccess(txnId, dateTime, amount, _MOBILE_NUMBER, mMessage, mSuccess);
                            } else {
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
                                            JSONObject jsonObject2 = jsonObject.getJSONObject(ApiParams.data).getJSONObject("transaction");
                                            String txnId2 = jsonObject2.getString(ApiParams.txn);
                                            String dateTime2 = jsonObject2.getString(ApiParams.created_at);
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
                            Log.i(">>exception", "onResponse: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loader.dismiss();
                        Log.i(">>error", "onErrorResponse: " + error.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                Log.i(">>params", "getParams: " +
                        _MOBILE_NUMBER + "\n" +
                        AMOUNT_FOR_ORENDA + "\n" +
                        _OPERATOR_ID + "\n" + _TYPE + "\n" + Prefes.getDescription(DataCardPaymentMethodActivity.this) + "\n" +
                        Prefes.getTalktime(DataCardPaymentMethodActivity.this) + "\n" +
                        Prefes.getValidity(DataCardPaymentMethodActivity.this)
                        + "\n" + Prefes.getPlan(DataCardPaymentMethodActivity.this));




                params.put("type", Prefes.getType(DataCardPaymentMethodActivity.this));
                params.put("operator_url_type", Prefes.getOperatorUrl(DataCardPaymentMethodActivity.this));
                params.put("mobile_number", _MOBILE_NUMBER);
                params.put("operator_id",getIntent().getStringExtra("operator_id"));
                params.put("circle", Prefes.getCircle(DataCardPaymentMethodActivity.this));
                params.put("bill_fetch", "0");
                params.put("amount",_AMOUNT);
                params.put("actual_recharge_amount",_AMOUNT);
                params.put("pointsAmount", points);
                params.put("pointsRedeemed",percent);
                params.put("payable_amount",orendaWalletAmount.getText().toString().replace(Functions.CURRENCY_SYMBOL, ""));
                params.put("operator_detail_id",_OPERATOR_ID );

                _LAND_LINE_NUMBER = getIntent().getStringExtra("landlineNumber");
                _ACCOUNT_NUMBER = getIntent().getStringExtra("accountNumber");


                Log.e("data",""+_LAND_LINE_NUMBER);
                Log.e("data",""+_ACCOUNT_NUMBER);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(DataCardPaymentMethodActivity.this));
                params.put("Accept", "application/json");
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(stringRequest);

        // MySingleton.getInstance(DataCardPaymentMethodActivity.this).addToRequestQueue(stringRequest);
    }


}

