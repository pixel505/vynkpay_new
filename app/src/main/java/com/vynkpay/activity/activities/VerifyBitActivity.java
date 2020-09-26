package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityVerifyBitBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.SendBitResponse;
import com.vynkpay.retrofit.model.VerifyBitResponse;
import com.vynkpay.utils.M;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyBitActivity extends AppCompatActivity {
    ActivityVerifyBitBinding binding;
    VerifyBitActivity ac;
    Dialog dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_verify_bit);
        dialog1 = M.showDialog(VerifyBitActivity.this, "", false, false);

        ac = VerifyBitActivity.this;
        binding.verifyLnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.otpEdt.getText().toString().isEmpty()) {
                    Toast.makeText(ac, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                } else {
                    dialog1.show();
                    MainApplication.getApiService().sendBitverify(Prefes.getAccessToken(VerifyBitActivity.this), binding.otpEdt.getText().toString()).enqueue(new Callback<VerifyBitResponse>() {
                        @Override
                        public void onResponse(Call<VerifyBitResponse> call, Response<VerifyBitResponse> response) {
                            if (response.isSuccessful()) {
                                dialog1.dismiss();
                                if(response.body().getStatus().equals("true")){
                                    Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(VerifyBitActivity.this, BtcActivity.class).putExtra("success", "yes"));
                            }

                            else if(response.body().getStatus().equals("false")){
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
        });

    }
}
