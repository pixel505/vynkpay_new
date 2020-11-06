package com.vynkpay.activity.activitiesnew.conversion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.activity.activities.ConversionActivity;
import com.vynkpay.activity.activitiesnew.RequestWithdrawOtpActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityConversionOtpBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.CheckWaletOtp;
import com.vynkpay.retrofit.model.SendWaletOtp;
import com.vynkpay.retrofit.model.SubmitConversionResponse;
import com.vynkpay.utils.M;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConversionOtpActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityConversionOtpBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    Dialog serverDialog;
    Activity activity = ConversionOtpActivity.this;
    String amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_conversion_otp);
        if (getIntent().hasExtra("amount")){
            amount = getIntent().getStringExtra("amount");
        }
        activity = ConversionOtpActivity.this;
        serverDialog = M.showDialog(ConversionOtpActivity.this, "", false, false);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getString(R.string.convertmcashtext));
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.submitButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            if(TextUtils.isEmpty(binding.otpET.getText().toString().trim())){
                Toast.makeText(activity, "Please Enter OTP", Toast.LENGTH_SHORT).show();
            } else {
                checkOTP(binding.otpET.getText().toString());
            }
        }
    }

    void checkOTP(String otp){
        serverDialog.show();
        binding.submitButton.setClickable(false);
        MainApplication.getApiService().checkotp(Prefes.getAccessToken(activity),otp).enqueue(new Callback<CheckWaletOtp>() {
            @Override
            public void onResponse(Call<CheckWaletOtp> call, Response<CheckWaletOtp> response) {
                if(response.isSuccessful() && response.body()!=null){
                    serverDialog.dismiss();
                    binding.submitButton.setClickable(true);
                    if(response.body().getStatus().equals("true")){
                        convertmoney(amount);
                    } else {
                        Toast.makeText(ConversionOtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<CheckWaletOtp> call, Throwable t) {
                serverDialog.dismiss();
                binding.submitButton.setClickable(true);
                Toast.makeText(ConversionOtpActivity.this, t.getMessage()!=null ? t.getMessage() : "Error", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void convertmoney(String amount) {
        serverDialog.show();
        Toast.makeText(activity, "Converting Money", Toast.LENGTH_SHORT).show();
        binding.submitButton.setClickable(false);
        MainApplication.getApiService().submitConversion(Prefes.getAccessToken(activity),"wr",amount).enqueue(new Callback<SubmitConversionResponse>() {
            @Override
            public void onResponse(Call<SubmitConversionResponse> call, Response<SubmitConversionResponse> response) {
                binding.submitButton.setClickable(true);
                serverDialog.dismiss();
                if(response.isSuccessful() && response.body()!=null){

                    if(response.body().getStatus()){
                        Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ConversionOtpActivity.this,ConvertSuccessActivity.class).putExtra("message",response.body().getMessage()!=null ? response.body().getMessage():"Error"));
                        ConversionOtpActivity.this.finish();
                        //getConversionList();
                        //binding.conversionHeader.searchUserET.setText("");
                    }

                }
            }

            @Override
            public void onFailure(Call<SubmitConversionResponse> call, Throwable t) {
                Toast.makeText(activity, t.getMessage()!=null ? t.getMessage() : "Error", Toast.LENGTH_SHORT).show();
                binding.submitButton.setClickable(true);
                serverDialog.dismiss();
            }
        });
    }

}