package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.databinding.ActivityPerfectMoneyBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetBitAddressResponse;
import com.vynkpay.retrofit.model.SendBitResponse;
import com.vynkpay.utils.M;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfectMoneyActivity extends AppCompatActivity {

    ActivityPerfectMoneyBinding binding;
    PerfectMoneyActivity ac;
    Dialog dialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_perfect_money);
        ac = PerfectMoneyActivity.this;
        dialog1 = M.showDialog(PerfectMoneyActivity.this, "", false, false);

        if (getIntent().getStringExtra("perfect") != null) {
            binding.benifiName.setText(getIntent().getStringExtra("perfect"));
        }

        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.perfectmoney);

        if (getIntent().getStringExtra("success") != null) {
            if (getIntent().getStringExtra("success").equals("yes")) {
                binding.approveLn.setText("Submit");
                binding.benifiName.setFocusable(true);

                binding.approveLn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.show();
                        MainApplication.getApiService().addPerfectAddress(Prefes.getAccessToken(PerfectMoneyActivity.this), binding.benifiName.getText().toString()).enqueue(new Callback<GetBitAddressResponse>() {
                            @Override
                            public void onResponse(Call<GetBitAddressResponse> call, Response<GetBitAddressResponse> response) {
                                dialog1.dismiss();
                                if (response.isSuccessful()) {
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    //finish();
                                    //startActivity(new Intent(PerfectMoneyActivity.this, HomeActivity.class));
                                }
                            }

                            @Override
                            public void onFailure(Call<GetBitAddressResponse> call, Throwable t) {
                                Log.d("Error",t.getMessage()!=null?t.getMessage():"Error");
                                dialog1.dismiss();
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
                    MainApplication.getApiService().sendPerfectOtp(Prefes.getAccessToken(PerfectMoneyActivity.this)).enqueue(new Callback<SendBitResponse>() {
                        @Override
                        public void onResponse(Call<SendBitResponse> call, Response<SendBitResponse> response) {
                            dialog1.dismiss();
                            if (response.isSuccessful()) {
                                if(response.body().getStatus().equals("true")) {
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PerfectMoneyActivity.this, VerifyBitActivity.class).putExtra("from","perfect"));
                                    finish();
                                }
                                else if(response.body().getStatus().equals("false")){
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();

                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<SendBitResponse> call, Throwable t) {
                            dialog1.dismiss();
                            Log.d("error",t.getMessage()!=null?t.getMessage():"Error");
                        }
                    });
                }
            });

        }

    }
}