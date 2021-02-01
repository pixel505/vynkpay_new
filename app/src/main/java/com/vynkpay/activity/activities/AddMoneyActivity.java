package com.vynkpay.activity.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.activity.recharge.mobile.activity.PaymentMethodActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.AddMoneyRazorResponse;
import com.vynkpay.utils.ApiParams;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;
import com.vynkpay.utils.URLS;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class AddMoneyActivity extends AppCompatActivity implements PaymentResultWithDataListener, PlugInControlReceiver.ConnectivityReceiverListener {
    @BindView(R.id.etAmount)
    NormalEditText etAmount;
    @BindView(R.id.add100)
    LinearLayout add100;
    @BindView(R.id.add500)
    LinearLayout add500;
    @BindView(R.id.add1000)
    LinearLayout add1000;
    @BindView(R.id.addMoneyButton)
    NormalButton addMoneyButton;
    @BindView(R.id.walletBalance)
    NormalTextView walletBalance;
    @BindView(R.id.toolbarTitle)
    NormalTextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.currencyTV)
    TextView currencyTV;

    @BindView(R.id.hundredAmountTV)
    TextView hundredAmountTV;

    @BindView(R.id.fiveHundredAmountTV)
    TextView fiveHundredAmountTV;

    @BindView(R.id.thousandAmountTV)
    TextView thousandAmountTV;

    Dialog dialog;
    JSONObject userPreference;

    String razorpikey;

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
                                double planAmount = Double.parseDouble(balance);
                                walletBalance.setText("Available Balance : " + Functions.CURRENCY_SYMBOL + " " + planAmount);

                                razorpikey =jsonObject1.getString(ApiParams.razorpikey);   // key in ApiParams is :  razorpikey

                            } else {
                                M.dialogOk(AddMoneyActivity.this, mMessage, "Error");
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
                            M.dialogOk(AddMoneyActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof AuthFailureError) {
                            M.dialogOk(AddMoneyActivity.this, "You are not authorized", "Error");
                        } else if (error instanceof ServerError) {
                            M.dialogOk(AddMoneyActivity.this, "Server issue", "Error");
                        } else if (error instanceof NetworkError) {
                            M.dialogOk(AddMoneyActivity.this, "Please check internet connection", "Error");
                        } else if (error instanceof ParseError) {
                            M.dialogOk(AddMoneyActivity.this, "Internal error", "Error");
                        }
                        dialog.dismiss();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(ApiParams.access_token, Prefes.getAccessToken(AddMoneyActivity.this));
                return params;
            }
        };
        MySingleton.getInstance(AddMoneyActivity.this).addToRequestQueue(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    String amountReceived;
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_add_money_rcg);
        ButterKnife.bind(AddMoneyActivity.this);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);

        dialog = M.showDialog(AddMoneyActivity.this, "", false, false);
        //FirebaseMessaging.getInstance().subscribeToTopic(ApiParams.GLOBAL_PARAMS);
        setListeners();

        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (getIntent() != null) {
            amountReceived = getIntent().getStringExtra(ApiParams.amount);
            etAmount.setText(amountReceived);
        }

        currencyTV.setText("("+Functions.CURRENCY_SYMBOL+")");

        hundredAmountTV.setText("+ ("+Functions.CURRENCY_SYMBOL+"100)");

        fiveHundredAmountTV.setText("+ ("+Functions.CURRENCY_SYMBOL+"500)");

        thousandAmountTV.setText("+ ("+Functions.CURRENCY_SYMBOL+"1000)");

        toolbarTitle.setText("Add Money");
        try {
            userPreference = new JSONObject(Prefes.getUserData(AddMoneyActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        etAmount.addTextChangedListener(new TextWatcher() {
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

        fetchWalletData();
    }

    private void setListeners() {
        addMoneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etAmount.getText().length() == 0 || etAmount.getText().length() > 6 || etAmount.getText().toString().trim().length() == 0) {
                    Toast.makeText(AddMoneyActivity.this, getString(R.string.valid_amount), Toast.LENGTH_SHORT).show();
                } else if (Double.valueOf(etAmount.getText().toString()) == 0) {
                    Toast.makeText(AddMoneyActivity.this, getString(R.string.valid_amount), Toast.LENGTH_SHORT).show();
                } else if (Double.valueOf(etAmount.getText().toString()) > 500000 || Double.valueOf(etAmount.getText().toString()) == 500000) {
                    Toast.makeText(AddMoneyActivity.this, "Amount should be less than 500,000", Toast.LENGTH_SHORT).show();
                } else {

                    payNow(etAmount.getText().toString().replace(Functions.CURRENCY_SYMBOL, ""));

                }
            }
        });

        add100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAmount.getText().length() == 0) {
                    etAmount.setText("100");
                } else {
                    etAmount.setText((Integer.valueOf(etAmount.getText().toString()) + 100) + "");
                }
            }
        });

        add500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAmount.getText().length() == 0) {
                    etAmount.setText("500");
                } else {
                    etAmount.setText((Integer.valueOf(etAmount.getText().toString()) + 500) + "");
                }
            }
        });


        add1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAmount.getText().length() == 0) {
                    etAmount.setText("1000");
                } else {
                    etAmount.setText((Integer.valueOf(etAmount.getText().toString()) + 1000) + "");
                }
            }
        });
    }

    // initiateHash(Prefes.getAccessToken(AddMoneyActivity.this));

    public void payNow(String toString) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID(razorpikey);

        int image = R.drawable.logo; // Can be any drawable
        co.setImage(image);

        try {
            JSONObject options = new JSONObject();
            options.put("name", Prefes.getEmail(AddMoneyActivity.this));
            options.put("description", "Add money to wallet");
            options.put("theme",new JSONObject("{color: '#B10D25'}"));
            options.put("currency", "INR");
            String payment = toString;
            double total = Double.parseDouble(payment);
            total = total * 100;

            options.put("amount", String.format("%.2f", total));

            JSONObject preFill = new JSONObject();
            preFill.put("email", Prefes.getEmail(AddMoneyActivity.this));
            preFill.put("contact", Prefes.getPhoneNumber(AddMoneyActivity.this));
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


    String mSuccess, mMessage;
    private void initiateHash(final String id) {
        dialog.show();

        MainApplication.getApiService().addMoneyRazorMethod(Prefes.getAccessToken(AddMoneyActivity.this),
                id,
                etAmount.getText().toString().replace(Functions.CURRENCY_SYMBOL, ""))
                .enqueue(new Callback<AddMoneyRazorResponse>() {
                    @Override
                    public void onResponse(Call<AddMoneyRazorResponse> call, retrofit2.Response<AddMoneyRazorResponse> response) {
                        if(response.isSuccessful() && response.body()!=null){
                            dialog.dismiss();
                            if(response.body().getSuccess()) {

                                Toast.makeText(AddMoneyActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                finish();
                            }

                            else {
                                Toast.makeText(AddMoneyActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<AddMoneyRazorResponse> call, Throwable t) {
                        dialog.dismiss();
                        Toast.makeText(AddMoneyActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result Code is -1 send from Payumoney activity
        Log.d("MainActivityLOGG", "request code " + requestCode + " resultcode " + resultCode);


    }



    private void makeDialogSuccess(String mMessage) {
        final Dialog dialog1 = M.inflateDialog(AddMoneyActivity.this, R.layout.success_popup_layout_rcg);
        NormalTextView message = dialog1.findViewById(R.id.message);
        NormalButton okButton = dialog1.findViewById(R.id.okButton);
        LinearLayout closeButton=dialog1.findViewById(R.id.closeButton);

        message.setText(mMessage);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent homeIntent = new Intent(AddMoneyActivity.this, WalletActivity.class);
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(homeIntent);
                finish();
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                if(sp.getString("value","").equals("Global") && Prefes.getisIndian(AddMoneyActivity.this).equals("NO")){
                    startActivity(new Intent(AddMoneyActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(AddMoneyActivity.this).equals("YES")){
                    startActivity(new Intent(AddMoneyActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }


                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(AddMoneyActivity.this).equals("YES")){
                    startActivity(new Intent(AddMoneyActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }

                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(AddMoneyActivity.this).equals("NO")){
                    startActivity(new Intent(AddMoneyActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        });
        dialog1.show();
    }

    private void makeErrorDialog(String mMessage, final String errorCode) {
        final Dialog errorDialog = M.inflateDialog(AddMoneyActivity.this, R.layout.dialog_error_message_rcg);
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
                    if(sp.getString("value","").equals("Global") && Prefes.getisIndian(AddMoneyActivity.this).equals("NO")){
                        startActivity(new Intent(AddMoneyActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                    else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(AddMoneyActivity.this).equals("YES")){
                        startActivity(new Intent(AddMoneyActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }


                    else if(sp.getString("value","").equals("India") && Prefes.getisIndian(AddMoneyActivity.this).equals("YES")){
                        startActivity(new Intent(AddMoneyActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                    else if(sp.getString("value","").equals("India") && Prefes.getisIndian(AddMoneyActivity.this).equals("NO")){
                        startActivity(new Intent(AddMoneyActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        initiateHash(s);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, s+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(AddMoneyActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(AddMoneyActivity.this,AddMoneyActivity.this::finishAffinity);
        }
    }
}
