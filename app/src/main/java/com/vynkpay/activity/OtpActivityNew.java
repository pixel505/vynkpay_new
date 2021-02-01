package com.vynkpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.OtpVerifyResponse;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtpActivityNew extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

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
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
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
                                    intent.putExtra("which","affiliate");
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
        MySingleton.getInstance(OtpActivityNew.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(OtpActivityNew.this,OtpActivityNew.this::finishAffinity);
        }
    }
}
