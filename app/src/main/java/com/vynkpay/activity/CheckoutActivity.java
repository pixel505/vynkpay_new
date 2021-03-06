package com.vynkpay.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
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
import com.vynkpay.utils.PlugInControlReceiver;
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
import retrofit2.Response;


public class CheckoutActivity extends AppCompatActivity implements PaymentResultWithDataListener, PlugInControlReceiver.ConnectivityReceiverListener {
    String _AMOUNT = "", _TYPE = "", _OPERATOR_ID = "", _MOBILE_NUMBER = "",percent,points;
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
    Dialog dialog;
    @BindView(R.id.razorLayout)
    LinearLayout razorLayout;
    @BindView(R.id.payAmountText)
    NormalTextView payAmountText;
    String walletBalane;
    @BindView(R.id.payAmountPayText)
    NormalTextView payAmountPayText;
    @BindView(R.id.vCashPercent)
    TextView vCashPercent;
    @BindView(R.id.vCashBalance)
    TextView vCashBalance;
    @BindView(R.id.vCashAmount)
    TextView vCashAmount;
    String razorpaykey;
    @BindView(R.id.walletBalance)
    TextView walletBalance;

    String phone="" ,email="",countrycode="";
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_method_paymemt_rcg);
        ButterKnife.bind(CheckoutActivity.this);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);

        MainApplication.getApiService().getProfileMethod(Prefes.getAccessToken(CheckoutActivity.this)).enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, Response<GetProfileResponse> response) {
                if(response.isSuccessful() ){

                    if (response.body() != null) {
                        if(response.body().getSuccess()){

                            phone=response.body().getData().getMobileNumber();
                            email=response.body().getData().getEmail();
                            countrycode=response.body().getData().getZipCode();
                        } else {
                            Log.d("checkout","success===-false");
                        }
                    }
                } else {
                    Log.d("checkout","there is error");
                }
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                Log.d("checkoutError",t.getMessage()!=null ? t.getMessage():"Error");
            }
        });

        payUCheck.setClickable(false);
        orendaCheck.setClickable(false);


        fetchWalletData();
        dialog = M.showDialog(CheckoutActivity.this, "", true, true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar_title.setText("Payment Method");

        _AMOUNT = getIntent().getStringExtra("amount");
        _TYPE = getIntent().getStringExtra("type");
        _OPERATOR_ID = getIntent().getStringExtra("operator_id");

        Log.i(">>operatorId", "method: " + _OPERATOR_ID);

        _MOBILE_NUMBER = getIntent().getStringExtra("mobile");

        Log.e("operatorDetailid1",""+Prefes.getAccessToken(CheckoutActivity.this));

        txtRechargeAmount.setText(Functions.CURRENCY_SYMBOL+" "+_AMOUNT);

    }


    Dialog successDialog;
    private void makeDialogSuccess(String txnId, String dateTime, String amount, String _MOBILE_NUMBER, String title, String mSuccess) {


        successDialog = M.inflateDialog(CheckoutActivity.this, R.layout.dialog_payment_result_rcg);
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
        forWhatPaid.setText("Mobile Recharge");
        tvTransactionId.setText(txnId.equals("") || txnId.equalsIgnoreCase("null") ? "" : txnId);
        tvDateTime.setText(M.changeDateTimeFormat(dateTime));

        textResult.setText(title);
        if (mSuccess.equalsIgnoreCase("true")) {
            imageResult.setBackgroundResource(R.drawable.success);
        } else {
            imageResult.setBackgroundResource(R.drawable.failed);
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 if(sp.getString("value","").equals("Global") && Prefes.getisIndian(CheckoutActivity.this).equalsIgnoreCase("NO")){
                    startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(CheckoutActivity.this).equalsIgnoreCase("YES")){
                    startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if(sp.getString("value","").equals("India") && Prefes.getisIndian(CheckoutActivity.this).equalsIgnoreCase("YES")){
                    startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if(sp.getString("value","").equals("India") && Prefes.getisIndian(CheckoutActivity.this).equalsIgnoreCase("NO")){
                    startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                // M.makeNavigationToHome(successDialog, PaymentMethodActivity.this);
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(sp.getString("value","").equals("Global") && Prefes.getisIndian(CheckoutActivity.this).equalsIgnoreCase("NO")){
                    startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(CheckoutActivity.this).equalsIgnoreCase("YES")){
                    startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if(sp.getString("value","").equals("India") && Prefes.getisIndian(CheckoutActivity.this).equalsIgnoreCase("YES")){
                    startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                } else if(sp.getString("value","").equals("India") && Prefes.getisIndian(CheckoutActivity.this).equalsIgnoreCase("NO")){
                    startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

        errorDialog = M.inflateDialog(CheckoutActivity.this, R.layout.dialog_error_message_rcg);
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
                    if(sp.getString("value","").equals("Global") && Prefes.getisIndian(CheckoutActivity.this).equals("NO")){
                        startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(CheckoutActivity.this).equals("YES")){
                        startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else if(sp.getString("value","").equals("India") && Prefes.getisIndian(CheckoutActivity.this).equals("YES")){
                        startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    } else if(sp.getString("value","").equals("India") && Prefes.getisIndian(CheckoutActivity.this).equals("NO")){
                        startActivity(new Intent(CheckoutActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

    public void payNow(String toString) {

        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID(razorpaykey);

        int image = R.drawable.logo; // Can be any drawable
        co.setImage(image);

        try {
            JSONObject options = new JSONObject();
            options.put("name", Prefes.getEmail(CheckoutActivity.this));
            options.put("description", "Pay to VynkPay");
            options.put("theme",new JSONObject("{color: '#B10D25'}"));
            //You can omit the image option to fetch the image from dashboard
            //  options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
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
        MainApplication.getApiService().walletAmountMethod(Prefes.getAccessToken(CheckoutActivity.this)).enqueue(new Callback<GetWalletResponse>() {
            @Override
            public void onResponse(Call<GetWalletResponse> call, Response<GetWalletResponse> response) {
                if(response.body().getSuccess()){
                    vCashBalance.setText(Functions.CURRENCY_SYMBOL+" "+response.body().getData().getBalance());
                    razorpaykey=response.body().getData().getRazorpikey();
                    walletBalance.setText("Available Balance "+" "+response.body().getData().getBalance());
                    walletBalane=response.body().getData().getBalance();

                    MainApplication.getApiService().redeemAmountMethod
                            (Prefes.getAccessToken(CheckoutActivity.this),_OPERATOR_ID,_AMOUNT).enqueue(new Callback<ReddemAmountResponse>() {
                        @Override
                        public void onResponse(Call<ReddemAmountResponse> call, Response<ReddemAmountResponse> response) {
                            if(response.isSuccessful()){
                                if(response.body().getSuccess()){
                                    if(response.body().getData().getRedeemShow()==1){
                                        vCashPercent.setText(response.body().getData().getPointsRedeemed());
                                        vCashAmount.setText(Functions.CURRENCY_SYMBOL+" "+ response.body().getData().getPointsAmount());
                                        payAmountPayText.setText(Functions.CURRENCY_SYMBOL+" "+response.body().getData().getTotalAmount());
                                        orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL+" "+response.body().getData().getTotalAmount());
                                        payUPaid.setText(Functions.CURRENCY_SYMBOL+" "+response.body().getData().getTotalAmount());
                                        percent=response.body().getData().getPointsRedeemed();
                                        points=response.body().getData().getPointsAmount();
                                        setListeners(response.body().getData().getTotalAmount());
                                    } else {
                                        setListeners(_AMOUNT);
                                        percent="";
                                        points="";
                                        payAmountPayText.setText("Total Amount "+" "+_AMOUNT);
                                        orendaWalletAmount.setText(Functions.CURRENCY_SYMBOL+" "+_AMOUNT);
                                        payUPaid.setText(Functions.CURRENCY_SYMBOL+" "+_AMOUNT);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ReddemAmountResponse> call, Throwable t) {

                        }
                    });


                }
            }

            @Override
            public void onFailure(Call<GetWalletResponse> call, Throwable t) {

            }
        });
    }

    String mSuccess, mMessage;
    private void setListeners(String totalAmount) {

        Log.e("wall",""+walletBalane);
        Log.e("total",""+totalAmount);

        Log.e("wall",""+Float.parseFloat(walletBalane));
        Log.e("total",""+Float.parseFloat(totalAmount));

        if(Float.parseFloat(walletBalane.replace(",", "")) < Float.parseFloat(totalAmount.replace(",", ""))){

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
        } else {
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
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        makepaymentButton.setEnabled(false);

        try {
            Log.e("razorpay_payment_id", "razorpay_payment_id" + paymentData.getData().getString("razorpay_payment_id"));
            MainApplication.getApiService().addMoneyRazorMethod(Prefes.getAccessToken(CheckoutActivity.this),
                    paymentData.getData().getString("razorpay_payment_id"),
                    payUPaid.getText().toString().replace(Functions.CURRENCY_SYMBOL, ""))
                    .enqueue(new Callback<AddMoneyRazorResponse>() {
                        @Override
                        public void onResponse(Call<AddMoneyRazorResponse> call, Response<AddMoneyRazorResponse> response) {
                            if(response.isSuccessful()){
                                makeRechargeRequest(payUPaid.getText().toString().replace(Functions.CURRENCY_SYMBOL, ""));
                            }
                        }

                        @Override
                        public void onFailure(Call<AddMoneyRazorResponse> call, Throwable t) {

                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }



    private void makeRechargeRequest(final String AMOUNT_FOR_ORENDA) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BuildConfig.APP_BASE_URL + URLS.recharge,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                        makeErrorDialog(error.getMessage(), ApiParams.ErrorCodeArray._ERROR_CODE_400);

                        Log.i(">>error", "onErrorResponse: " + error.getMessage());
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                Log.i(">>params", "getParams: " +
                        _MOBILE_NUMBER + "\n" +
                        AMOUNT_FOR_ORENDA + "\n" +
                        _OPERATOR_ID + "\n" + _TYPE + "\n" + Prefes.getDescription(CheckoutActivity.this) + "\n" +
                        Prefes.getTalktime(CheckoutActivity.this) + "\n" +
                        Prefes.getValidity(CheckoutActivity.this)
                        + "\n" + Prefes.getPlan(CheckoutActivity.this));

                params.put("type", Prefes.getType(CheckoutActivity.this));
                params.put("operator_url_type", Prefes.getOperatorUrl(CheckoutActivity.this));
                params.put("mobile_number", Prefes.getPhoneNumber(CheckoutActivity.this));
                params.put("operator_id",Prefes.getOperatorDID(CheckoutActivity.this));
                params.put("circle", Prefes.getCircle(CheckoutActivity.this));
                params.put("bill_fetch", "0");
                params.put("amount",_AMOUNT);
                params.put("actual_recharge_amount",_AMOUNT);
                params.put("pointsAmount", points);
                params.put("pointsRedeemed",percent);
                params.put("payable_amount",orendaWalletAmount.getText().toString());
                params.put("operator_detail_id",_OPERATOR_ID );

                Log.e("data",""+_AMOUNT);
                Log.e("data",""+orendaWalletAmount.getText().toString());
                Log.e("data",""+points);
                Log.e("data",""+percent);
                Log.e("data",""+_OPERATOR_ID);
                Log.e("data",""+Prefes.getOperatorDID(CheckoutActivity.this));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(CheckoutActivity.this));
                params.put("Accept", "application/json");
                return params;
            }

        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(this);
        rQueue.add(stringRequest);

        // MySingleton.getInstance(PaymentMethodActivity.this).addToRequestQueue(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(CheckoutActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(CheckoutActivity.this,CheckoutActivity.this::finishAffinity);
        }
    }
}

