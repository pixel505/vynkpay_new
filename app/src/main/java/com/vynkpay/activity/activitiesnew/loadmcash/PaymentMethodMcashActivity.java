package com.vynkpay.activity.activitiesnew.loadmcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityPaymentMethodMcashBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.AddMoneyRazorResponse;
import com.vynkpay.retrofit.model.GetProfileResponse;
import com.vynkpay.retrofit.model.GetWalletResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodMcashActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultWithDataListener {

    ActivityPaymentMethodMcashBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    String amount = "";
    SharedPreferences sp;
    String phone ="" ,email ="",countrycode ="";
    String razorpaykey= "";
    Dialog loader;
    PaymentMethodMcashActivity ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment_method_mcash);
        ac = PaymentMethodMcashActivity.this;
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
        loader= M.showDialog(PaymentMethodMcashActivity.this, "", false, false);
        getProfileData();
        amount = getIntent().getStringExtra("amount");
        binding.tvAmontBal.setText(Functions.CURRENCY_SYMBOL+amount);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Load MCash");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.submitButton.setOnClickListener(this);
        initListner();
        if (Functions.isIndian){
            binding.linIndian.setVisibility(View.VISIBLE);
            binding.linInternationl.setVisibility(View.GONE);
        }else {
            binding.linIndian.setVisibility(View.GONE);
            binding.linInternationl.setVisibility(View.VISIBLE);
        }
        fetchWalletData();
    }

    void initListner(){
        binding.btnCoinbase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    binding.btnPayeer.setChecked(false);
                }
            }
        });

        binding.btnPayeer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    binding.btnCoinbase.setChecked(false);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            if(Functions.isIndian){
                if (!binding.btnRozarpay.isChecked()){
                    Toast.makeText(PaymentMethodMcashActivity.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                }else {
                    binding.submitButton.setClickable(false);
                    payUsingRozarPay(amount);
                    //startActivity(new Intent(PaymentMethodMcashActivity.this,LoadMcashSuccessActivity.class));
                }
            }else {
                if (binding.btnCoinbase.isChecked() || binding.btnPayeer.isChecked()){
                    if (binding.btnCoinbase.isChecked()){
                        binding.submitButton.setClickable(false);
                        //url
                        //https://www.mlm.pixelsoftwares.com/vynkpay/account/coinBaseAppWebView/app_choose_payment?package_id=0&plan=1&app_request=request_app&access_token=618f375122156a6df9ea873d57d71e66a7c0c10d577d9d643b8617403629e2471598446930&investValue=125&type=wallet
                        String  url = BuildConfig.BASE_URL+ "account/coinBaseAppWebView/app_choose_payment?package_id=0&plan=1&app_request=request_app&access_token="+Prefes.getAccessToken(ac)+"&investValue="+amount+"&type=wallet";
                        startActivity(new Intent(PaymentMethodMcashActivity.this,CoinbaseActivity.class).putExtra("url",url));
                        binding.submitButton.setClickable(true);
                        PaymentMethodMcashActivity.this.finish();
                    }else if (binding.btnPayeer.isChecked()){
                        Toast.makeText(ac, "Coming soon!!", Toast.LENGTH_SHORT).show();
                    }
                    //startActivity(new Intent(PaymentMethodMcashActivity.this,LoadMcashSuccessActivity.class));
                }else {
                    Toast.makeText(PaymentMethodMcashActivity.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    void payUsingRozarPay(String _amount){
        Activity activity = PaymentMethodMcashActivity.this;
        Checkout co = new Checkout();
        co.setKeyID(razorpaykey);
        int image = R.drawable.logo;
        co.setImage(image);
        try {
            JSONObject options = new JSONObject();
            options.put("name", Prefes.getEmail(PaymentMethodMcashActivity.this));
            options.put("description", "Pay to VynkPay");
            options.put("theme",new JSONObject("{color: '#B10D25'}"));
            //You can omit the image option to fetch the image from dashboard
            //  options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            // options.put("order_id",orderid);
            options.put("currency", "INR");
            String payment = _amount;
            double total = Double.parseDouble(payment);
            total = total * 100;


            options.put("amount", String.format("%.2f", total));
            //  options.put("order_id",iD );

            JSONObject preFill = new JSONObject();
            preFill.put("email", email);
            preFill.put("contact", phone);
            options.put("prefill", preFill);
            co.open(activity, options);
            binding.submitButton.setClickable(true);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        /*try {
            Log.e("razorpay_payment_idmca", "razorpay_payment_id" + paymentData.getData().getString("razorpay_payment_id"));

        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
            Dialog loader=M.showDialog(PaymentMethodMcashActivity.this, "", false, false);
            loader.show();
            Log.e("razorpay_payment_id", "razorpay_payment_id" + paymentData.getData().getString("razorpay_payment_id"));
            MainApplication.getApiService().addMoneyRazorMethod(Prefes.getAccessToken(PaymentMethodMcashActivity.this),
                    paymentData.getData().getString("razorpay_payment_id"),
                    amount.replace(Functions.CURRENCY_SYMBOL, ""))
                    .enqueue(new Callback<AddMoneyRazorResponse>() {
                        @Override
                        public void onResponse(Call<AddMoneyRazorResponse> call, Response<AddMoneyRazorResponse> response) {
                            Log.d("sdasfdasdsdad", response.toString()+"?/");
                            Log.d("sdasfdasdsdad", response.body().getMessage()+"?/");
                            loader.dismiss();
                            if(response.isSuccessful()){
                                Log.d("addmcashtmoney",new Gson().toJson(response.body()));
                                if (response.body().getSuccess()) {
                                    Toast.makeText(PaymentMethodMcashActivity.this, response.body().getMessage() != null ? response.body().getMessage() : "Money added successfully.", Toast.LENGTH_SHORT).show();
                                    PaymentMethodMcashActivity.this.finish();
                                }else {
                                    Toast.makeText(PaymentMethodMcashActivity.this, response.body().getMessage() != null ? response.body().getMessage() : "Error in payment", Toast.LENGTH_SHORT).show();
                                    PaymentMethodMcashActivity.this.finish();
                                }

                                //makeRechargeRequest(payUPaid.getText().toString().replace(Functions.CURRENCY_SYMBOL, ""));
                            }
                        }

                        @Override
                        public void onFailure(Call<AddMoneyRazorResponse> call, Throwable t) {
                            loader.dismiss();
                        }
                    });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(PaymentMethodMcashActivity.this, s, Toast.LENGTH_SHORT).show();
    }

     void fetchWalletData() {
        loader.show();
        MainApplication.getApiService().walletAmountMethod(Prefes.getAccessToken(PaymentMethodMcashActivity.this)).enqueue(new Callback<GetWalletResponse>() {
            @Override
            public void onResponse(Call<GetWalletResponse> call, Response<GetWalletResponse> response) {
                loader.dismiss();
                if(response.body().getSuccess()){
                    //vCashBalance.setText(Functions.CURRENCY_SYMBOL+" "+response.body().getData().getWalletRedeem());
                    razorpaykey=response.body().getData().getRazorpikey();
                    Log.d("razorpaykey",razorpaykey);
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

    void getProfileData(){
        MainApplication.getApiService().getProfileMethod(Prefes.getAccessToken(PaymentMethodMcashActivity.this)).enqueue(new Callback<GetProfileResponse>() {
            @Override
            public void onResponse(Call<GetProfileResponse> call, retrofit2.Response<GetProfileResponse> response) {

                Log.d("getProfileResponse", response.body().getMessage()+"//");
                Log.d("getProfileResponse", response.body().getSuccess()+"//");

                if(response.isSuccessful() ){
                    if(response.body().getSuccess()){
                        phone=response.body().getData().getMobileNumber();
                        email=response.body().getData().getEmail();
                        countrycode=response.body().getData().getZipCode();
                    }else {
                        Toast.makeText(PaymentMethodMcashActivity.this, response.body().getMessage()+"", Toast.LENGTH_SHORT).show();
                        Prefes.clear(PaymentMethodMcashActivity.this);


                        Toast.makeText(PaymentMethodMcashActivity.this, sp.getString("value",""), Toast.LENGTH_SHORT).show();



                        if(sp.getString("value","").equals("Global") && Prefes.getisIndian(getApplicationContext()).equals("NO")){
                            startActivity(new Intent(PaymentMethodMcashActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }

                        else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(getApplicationContext()).equals("YES")){
                            startActivity(new Intent(PaymentMethodMcashActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }


                        else if(sp.getString("value","").equals("India") && Prefes.getisIndian(getApplicationContext()).equals("YES")){
                            startActivity(new Intent(PaymentMethodMcashActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }

                        else if(sp.getString("value","").equals("India") && Prefes.getisIndian(getApplicationContext()).equals("NO")){
                            startActivity(new Intent(PaymentMethodMcashActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }

                    }
                }
            }

            @Override
            public void onFailure(Call<GetProfileResponse> call, Throwable t) {

            }
        });
    }


}