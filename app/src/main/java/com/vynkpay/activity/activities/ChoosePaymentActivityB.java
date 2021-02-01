package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.databinding.ActivityChoosePaymentBBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetPackageDetailResponse;
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
                    if(response.body().getStatus().equals("true")){
                        binding.mobino.setText("To :"+" "+response.body().getData().getValue());
                        binding.mailid.setText(response.body().getData().getValue2());
                        binding.pnbname.setText(response.body().getData().getB_name());
                        binding.pvtltd.setText("Name :"+" "+response.body().getData().getB_name_2());
                        binding.acountno.setText("A/c No :"+" "+response.body().getData().getAccount());
                        binding.ifsccode.setText("IFSC :"+" "+response.body().getData().getIFSC());
                        binding.branch.setText("Branch :"+" "+response.body().getData().getAddress());


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