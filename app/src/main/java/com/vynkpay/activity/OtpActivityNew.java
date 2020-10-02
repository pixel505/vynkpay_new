package com.vynkpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.activity.activities.Signupnew;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.OtpVerifyResponse;
import com.vynkpay.utils.M;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivityNew extends AppCompatActivity {

    @BindView(R.id.verifyLnNew)
    NormalButton verifyLn;


    @BindView(R.id.otpEdt)
    NormalEditText otpEdt;

    String tempid;
    Dialog dialog;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_new);
        sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
        ButterKnife.bind(this);
        dialog = M.showDialog(OtpActivityNew.this, "", false, false);

        tempid=getIntent().getStringExtra("tempid");

        verifyLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(otpEdt.getText().toString().isEmpty()){
                    Toast.makeText(OtpActivityNew.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }

                else {
                    dialog.show();
                    MainApplication.getApiService().otpVerifyMethod(tempid,otpEdt.getText().toString()).enqueue(new Callback<OtpVerifyResponse>() {
                        @Override
                        public void onResponse(Call<OtpVerifyResponse> call, Response<OtpVerifyResponse> response) {
                            if(response.isSuccessful()) {
                                if (response.body().getSuccess()) {
                                    dialog.dismiss();

                                    Intent intent=new Intent(OtpActivityNew.this, RechargeSuccess.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finishAffinity();

                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(OtpActivityNew.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            else {
                                dialog.dismiss();
                                Toast.makeText(OtpActivityNew.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<OtpVerifyResponse> call, Throwable t) {
                        }
                    });
                }


                /* */
            }
        });

    }

    @Override
    protected void onResume() {
        if(dialog!=null){
            dialog.dismiss();
        }
        super.onResume();
    }
}
