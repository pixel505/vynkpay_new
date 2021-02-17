package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityChoosePaymentBBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetPackageDetailResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChoosePaymentActivityB extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    ActivityChoosePaymentBBinding binding;
    ChoosePaymentActivityB ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_payment_b);
        ac = ChoosePaymentActivityB.this;

        getDetail();



        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.reqstmcash);
    }

    private void getDetail() {
        MainApplication.getApiService().getPackageDetails(Prefes.getAccessToken(ac)).enqueue(new Callback<GetPackageDetailResponse>() {
            @Override
            public void onResponse(Call<GetPackageDetailResponse> call, Response<GetPackageDetailResponse> response) {
                if(response.isSuccessful() && response.body()!=null){

                    Log.d("walletResposeLOG", new Gson().toJson(response.body().getData())+"//");

                    if(response.body().getStatus().equals("true")){
                        binding.txnAddress.setText(response.body().getData().getTrx_address());
                        binding.payerAccountTV.setText(response.body().getData().getPayer_account());
                        binding.firstTitle.setText(response.body().getData().getName());
                        binding.secondTitle.setText(response.body().getData().getName2());
                        binding.mobino.setText("To :"+" "+response.body().getData().getValue());
                        binding.mailid.setText(response.body().getData().getValue2());
                        binding.pnbname.setText(response.body().getData().getB_name());
                        binding.pvtltd.setText("Name :"+" "+response.body().getData().getB_name_2());
                        binding.acountno.setText("A/c No :"+" "+response.body().getData().getAccount());
                        binding.ifsccode.setText("IFSC :"+" "+response.body().getData().getIFSC());
                        binding.branch.setText("Branch :"+" "+response.body().getData().getAddress());

                        String payUrl= BuildConfig.BASE_URL+"account/"+response.body().getData().getImage();
                        String bankUrl= BuildConfig.BASE_URL+"account/"+response.body().getData().getB_image();
                        String payeerUrl=response.body().getData().getPayer_image();
                        String txnUrl= response.body().getData().getTrx_address_image();

                        Functions.loadImageCall(ChoosePaymentActivityB.this, payUrl, binding.payIV);
                        Functions.loadImageCall(ChoosePaymentActivityB.this, bankUrl, binding.bankIV);
                        Functions.loadImageCall(ChoosePaymentActivityB.this, txnUrl, binding.txnIV);
                        Functions.loadImageCall(ChoosePaymentActivityB.this, payeerUrl, binding.payerIV);

                        if (response.body().getData().getBank_enable().equalsIgnoreCase("true")){
                            binding.bankLinear.setVisibility(View.VISIBLE);
                        }else {
                            binding.bankLinear.setVisibility(View.GONE);
                        }

                        if (response.body().getData().getPayer_enable().equalsIgnoreCase("true")){
                            binding.payeerLinear.setVisibility(View.VISIBLE);
                        }else {
                            binding.payeerLinear.setVisibility(View.GONE);
                        }

                        if (response.body().getData().getTrx_address_enable().equalsIgnoreCase("true")){
                            binding.txnLinear.setVisibility(View.VISIBLE);
                        }else {
                            binding.txnLinear.setVisibility(View.GONE);
                        }

                        if (response.body().getData().getWallet_enable().equalsIgnoreCase("true")){
                            binding.upiLinear.setVisibility(View.VISIBLE);
                        }else {
                            binding.upiLinear.setVisibility(View.GONE);
                        }


                        binding.enterpaymentdetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(ChoosePaymentActivityB.this,ChoosePaymentActivityC.class).putExtra("am1",getIntent().getStringExtra("am")));
                                finish();
                            }
                        });
                    }
                    else {
                        Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPackageDetailResponse> call, Throwable t) {
                Toast.makeText(ac, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(ChoosePaymentActivityB.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(ChoosePaymentActivityB.this,ChoosePaymentActivityB.this::finishAffinity);
        }
    }
}