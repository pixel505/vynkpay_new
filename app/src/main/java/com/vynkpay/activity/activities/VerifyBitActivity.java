package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vynkpay.activity.PayeerAddressActivity;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityVerifyBitBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.VerifyBitResponse;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyBitActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityVerifyBitBinding binding;
    VerifyBitActivity ac;
    Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_bit);
        dialog1 = M.showDialog(VerifyBitActivity.this, "", false, false);

        ac = VerifyBitActivity.this;
        binding.verifyLnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.otpEdt.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                } else {

                    if (getIntent().getStringExtra("from")!=null){
                        //BTC
                        if (getIntent().getStringExtra("from").equalsIgnoreCase("btc")){
                            Log.d("called","btc");
                            dialog1.show();
                            MainApplication.getApiService().sendBitverify(Prefes.getAccessToken(VerifyBitActivity.this), binding.otpEdt.getText().toString()).enqueue(new Callback<VerifyBitResponse>() {
                                @Override
                                public void onResponse(Call<VerifyBitResponse> call, Response<VerifyBitResponse> response) {
                                    dialog1.dismiss();
                                    if (response.isSuccessful()) {

                                        if(response.body().getStatus().equals("true")){
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(VerifyBitActivity.this, BtcActivity.class).putExtra("success", "yes"));
                                            finish();
                                        } else if(response.body().getStatus().equals("false")){
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<VerifyBitResponse> call, Throwable t) {
                                    dialog1.dismiss();
                                }

                            });

                        }
                        //ETH
                        else if (getIntent().getStringExtra("from").equalsIgnoreCase("eth")){
                            Log.d("called","eth");
                            dialog1.show();
                            MainApplication.getApiService().sendETHverify(Prefes.getAccessToken(VerifyBitActivity.this), binding.otpEdt.getText().toString()).enqueue(new Callback<VerifyBitResponse>() {
                                @Override
                                public void onResponse(Call<VerifyBitResponse> call, Response<VerifyBitResponse> response) {
                                    dialog1.dismiss();
                                    if (response.isSuccessful()) {
                                        Log.d("ethAddress", new Gson().toJson(response.body()));
                                        if(response.body().getStatus().equals("true")){
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(VerifyBitActivity.this, ETHActivity.class).putExtra("success", "yes"));
                                            finish();
                                        } else if(response.body().getStatus().equals("false")){
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<VerifyBitResponse> call, Throwable t) {
                                    dialog1.dismiss();
                                }

                            });

                        }
                        /*Perfect*/
                        else if (getIntent().getStringExtra("from").equalsIgnoreCase("perfect")){
                            Log.d("called","perfect");
                            dialog1.show();
                            MainApplication.getApiService().sendPerfectverify(Prefes.getAccessToken(VerifyBitActivity.this), binding.otpEdt.getText().toString()).enqueue(new Callback<VerifyBitResponse>() {
                                @Override
                                public void onResponse(Call<VerifyBitResponse> call, Response<VerifyBitResponse> response) {
                                    dialog1.dismiss();
                                    if (response.isSuccessful()) {
                                        Log.d("perfectAddress", new Gson().toJson(response.body()));
                                        if(response.body().getStatus().equals("true")){
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(VerifyBitActivity.this, PerfectMoneyActivity.class).putExtra("success", "yes"));
                                            finish();
                                        } else if(response.body().getStatus().equals("false")){
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<VerifyBitResponse> call, Throwable t) {
                                    dialog1.dismiss();
                                }

                            });
                        }
                        /*Payeer*/
                        else if (getIntent().getStringExtra("from").equalsIgnoreCase("payeer")){
                            Log.d("called","payeer");
                            dialog1.show();
                            MainApplication.getApiService().sendPayeerverify(Prefes.getAccessToken(VerifyBitActivity.this), binding.otpEdt.getText().toString()).enqueue(new Callback<VerifyBitResponse>() {
                                @Override
                                public void onResponse(Call<VerifyBitResponse> call, Response<VerifyBitResponse> response) {
                                    dialog1.dismiss();
                                    if (response.isSuccessful()) {
                                        Log.d("payeerAddress", new Gson().toJson(response.body()));
                                        if(response.body().getStatus().equals("true")){
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(VerifyBitActivity.this, PayeerAddressActivity.class).putExtra("success", "yes"));
                                            finish();
                                        } else if(response.body().getStatus().equals("false")){
                                            Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }

                                @Override
                                public void onFailure(Call<VerifyBitResponse> call, Throwable t) {
                                    dialog1.dismiss();
                                }

                            });
                        }
                    }
                }

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(VerifyBitActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(VerifyBitActivity.this,VerifyBitActivity.this::finishAffinity);
        }
    }
}
