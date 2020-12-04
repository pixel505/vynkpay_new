package com.vynkpay.activity.activities;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.activity.recharge.mobile.activity.PaymentMethodActivity;
import com.vynkpay.databinding.ActivityChoosePaymentBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.AddMoneyRazorResponse;
import com.vynkpay.retrofit.model.GetProfileResponse;
import com.vynkpay.retrofit.model.GetWalletResponse;
import com.vynkpay.retrofit.model.PayResponse;
import com.vynkpay.retrofit.model.ReddemAmountResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosePaymentActivity extends AppCompatActivity implements PaymentResultWithDataListener {
    ActivityChoosePaymentBinding binding;
    ChoosePaymentActivity ac;
    String mBalance, mAmount;
    Dialog dialog1;
    String phone ,email,countrycode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_payment);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_payment);
        ac = ChoosePaymentActivity.this;
        dialog1 = M.showDialog(ac, "", false, false);
        if (Functions.isIndian) {
        } else {
            binding.payOnline.setText("Bitcoin");
            binding.card1.setVisibility(View.GONE);
            binding.card2.setVisibility(View.GONE);
            binding.paycard.setVisibility(View.GONE);
        }
        binding.mcashBalance.setText("Available Balance"+" :"+Functions.CURRENCY_SYMBOL+Prefes.getCash(ChoosePaymentActivity.this));
        mBalance=Prefes.getCash(ChoosePaymentActivity.this);
        clicks();
        if (getIntent() != null) {
            binding.packagePrice.setText(Functions.CURRENCY_SYMBOL + getIntent().getStringExtra("purchaseprice"));
            mAmount = getIntent().getStringExtra("purchaseprice");
        }
        fetchWalletData();
    }
    private void clicks() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.choosepay);
        if(Functions.isIndian){
            binding.requestMCash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(ChoosePaymentActivity.this, ChoosePaymentActivityB.class).putExtra("am", getIntent().getStringExtra("purchaseprice")));
                    finish();
                }
            });
        }


        else {
            binding.requestMCash.setVisibility(View.GONE);
        }

        binding.choosepaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(binding.rd1.isChecked() || binding.rd2.isChecked())) {
                    Toast.makeText(ac, "Please Select Payment Method", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.rd1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.rd2.setChecked(false);
                    double totalAmount = Double.parseDouble(mAmount);
                    double mbalance = Double.parseDouble(mBalance);
                    if (mbalance < totalAmount) {
                        binding.choosepaymentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ac, "Low Wallet Balance", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        binding.choosepaymentBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog1.show();
                                MainApplication.getApiService().pay(Prefes.getAccessToken(ac), getIntent().getStringExtra("ids")).enqueue(new Callback<PayResponse>() {

                                    @Override
                                    public void onResponse(Call<PayResponse> call, Response<PayResponse> response) {
                                        if (response.isSuccessful() && response.body() != null) {
                                            dialog1.dismiss();
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ac, ChoosePaymentActivityD.class));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<PayResponse> call, Throwable t) {
                                        dialog1.dismiss();
                                    }

                                });
                            }
                        });
                    }
                }
            }
        });
        binding.rd2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    binding.rd1.setChecked(false);
                    double totalAmount = Double.parseDouble(mAmount);
                    double mbalance = Double.parseDouble(mBalance);
                    binding.choosepaymentBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(Functions.isIndian){
                               /* String url = BuildConfig.BASE_URL+"account/coinBaseAppWebView/app_choose_payment?package_id="
                                        + getIntent().getStringExtra("ids") + "&alien=1&app_request=request_app&access_token=" + Prefes.getAccessToken(ac);
                                Log.e("ids", "" + url);
                                startActivity(new Intent(ChoosePaymentActivity.this, WebviewActivityNew.class)
                                        .putExtra("url", url)
                                        .putExtra("packageID", getIntent().getStringExtra("ids")));*/
                                //Toast.makeText(ac, "Clicked"+totalAmount, Toast.LENGTH_SHORT).show();
                                payNow(String.valueOf(totalAmount));
                            } else {
                                dialog1.show();
                                String url = BuildConfig.BASE_URL+"account/coinBaseAppWebView/app_choose_payment?package_id="
                                        + getIntent().getStringExtra("ids") + "&alien=1&app_request=request_app&access_token=" + Prefes.getAccessToken(ac);
                                Log.e("ids", "" + url);
                                startActivity(new Intent(ChoosePaymentActivity.this, WebviewActivityNew.class)
                                        .putExtra("url", url)
                                        .putExtra("packageID", getIntent().getStringExtra("ids")));
                            }
                        }

                    });
                }
            }
        });

        MainApplication.getApiService().getProfileMethod(Prefes.getAccessToken(ChoosePaymentActivity.this)).enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, retrofit2.Response<GetProfileResponse> response) {

                Log.d("getProfileResponse", response.body().getMessage()+"//");
                Log.d("getProfileResponse", response.body().getSuccess()+"//");

                if(response.isSuccessful() ){
                    if(response.body().getSuccess()){
                        phone=response.body().getData().getMobileNumber();
                        email=response.body().getData().getEmail();
                        countrycode=response.body().getData().getZipCode();
                    } else {
                        Log.d("getProfileResponse","Errorro"+response.body().getMessage());
                        //Toast.makeText(ChoosePaymentActivity.this, response.body().getMessage()+"", Toast.LENGTH_SHORT).show();
                        //Prefes.clear(ChoosePaymentActivity.this);
                        //Toast.makeText(PaymentMethodActivity.this, sp.getString("value",""), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {
                Log.d("errorresp",t.getMessage()!=null?t.getMessage():"Error");
            }
        });

    }

    String razorpaykey = "";

    private void fetchWalletData() {
        Dialog loader=M.showDialog(ChoosePaymentActivity.this, "", false, false);
        loader.show();
        MainApplication.getApiService().walletAmountMethod(Prefes.getAccessToken(ChoosePaymentActivity.this)).enqueue(new Callback<GetWalletResponse>() {
            @Override
            public void onResponse(Call<GetWalletResponse> call, Response<GetWalletResponse> response) {
                loader.dismiss();
                if(response.body().getSuccess()){
                    //vCashBalance.setText(Functions.CURRENCY_SYMBOL+" "+response.body().getData().getWalletRedeem());
                    razorpaykey = response.body().getData().getRazorpikey();
                    Log.d("ererre",razorpaykey);
                    //walletBalance.setText("Available Balance "+" "+response.body().getData().getBalance());
                    //walletBalane=response.body().getData().getBalance();
                }
            }

            @Override
            public void onFailure(Call<GetWalletResponse> call, Throwable t) {
                loader.dismiss();
            }

        });
    }

    public void payNow(String toString) {

        final Activity activity = this;
        final Checkout co = new Checkout();
        co.setKeyID(razorpaykey);

        int image = R.drawable.logo; // Can be any drawable
        co.setImage(image);

        try {
            JSONObject options = new JSONObject();
            options.put("name", Prefes.getEmail(ChoosePaymentActivity.this));
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


    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        try {
            //Dialog loader=M.showDialog(ChoosePaymentActivity.this, "", false, false);
            dialog1.show();
            Log.e("razorpay_payment_id", "razorpay_payment_id" + paymentData.getData().getString("razorpay_payment_id"));
            MainApplication.getApiService().addMoneyRazorMethod(Prefes.getAccessToken(ChoosePaymentActivity.this),
                    paymentData.getData().getString("razorpay_payment_id"),
                    mAmount.replace(Functions.CURRENCY_SYMBOL,""))
                    .enqueue(new Callback<AddMoneyRazorResponse>() {
                        @Override
                        public void onResponse(Call<AddMoneyRazorResponse> call, Response<AddMoneyRazorResponse> response) {
                            Log.d("sdasfdasdsdad", response.toString()+"?/");
                            Log.d("sdasfdasdsdad", response.body().getMessage()+"?/");
                            dialog1.dismiss();
                            if(response.isSuccessful()){
                                payForPackage();
                                //makeRechargeRequest(payUPaid.getText().toString().replace(Functions.CURRENCY_SYMBOL, ""));
                            }
                        }

                        @Override
                        public void onFailure(Call<AddMoneyRazorResponse> call, Throwable t) {
                            Log.d("sdasfdasdsdad",t.getMessage()!=null?t.getMessage():"Error");
                            dialog1.dismiss();
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void payForPackage(){
        dialog1.show();
        MainApplication.getApiService().pay(Prefes.getAccessToken(ac), getIntent().getStringExtra("ids")).enqueue(new Callback<PayResponse>() {

            @Override
            public void onResponse(Call<PayResponse> call, Response<PayResponse> response) {
                dialog1.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("payresponse",new Gson().toJson(response.body()));
                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ac, ChoosePaymentActivityD.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PayResponse> call, Throwable t) {
                dialog1.dismiss();
                Log.d("payresponse", t.getMessage() != null ? t.getMessage() : "Error");
            }

        });
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        dialog1.dismiss();
        super.onResume();
    }

}