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
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityBtcBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetBitAddressResponse;
import com.vynkpay.retrofit.model.SendBitResponse;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BtcActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    ActivityBtcBinding binding;
    BtcActivity ac;
    Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_btc);
        ac = BtcActivity.this;
        dialog1 = M.showDialog(BtcActivity.this, "", false, false);

        if (getIntent().getStringExtra("bit") != null) {
            binding.benifiName.setText(getIntent().getStringExtra("bit"));
        }

        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.kyc);

        if (getIntent().getStringExtra("success") != null) {
            if (getIntent().getStringExtra("success").equals("yes")) {
                binding.approveLn.setText("Submit");
                binding.benifiName.setFocusable(true);

                binding.approveLn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.show();
                        MainApplication.getApiService().bitAddress(Prefes.getAccessToken(BtcActivity.this), binding.benifiName.getText().toString()).enqueue(new Callback<GetBitAddressResponse>() {
                            @Override
                            public void onResponse(Call<GetBitAddressResponse> call, Response<GetBitAddressResponse> response) {
                                if (response.isSuccessful()) {
                                    dialog1.dismiss();
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(BtcActivity.this, HomeActivity.class));
                                    //finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<GetBitAddressResponse> call, Throwable t) {
                                Log.d("Error",t.getMessage()!=null?t.getMessage():"Error");
                            }
                        });
                    }
                });
            }
        }


        else {
            binding.benifiName.setFocusable(false);
            binding.approveLn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog1.show();
                    MainApplication.getApiService().sendBit(Prefes.getAccessToken(BtcActivity.this)).enqueue(new Callback<SendBitResponse>() {
                        @Override
                        public void onResponse(Call<SendBitResponse> call, Response<SendBitResponse> response) {
                            if (response.isSuccessful()) {
                                if(response.body().getStatus().equals("true")) {
                                    dialog1.dismiss();
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(BtcActivity.this, VerifyBitActivity.class).putExtra("from","btc"));
                                    finish();
                                }
                                else if(response.body().getStatus().equals("false")){
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<SendBitResponse> call, Throwable t) {
                            Log.d("error",t.getMessage()!=null?t.getMessage():"Error");
                        }
                    });
                }
            });

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(BtcActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(BtcActivity.this,BtcActivity.this::finishAffinity);
        }
    }
}
