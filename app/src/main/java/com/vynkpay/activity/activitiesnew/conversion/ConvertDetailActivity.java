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
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.activity.activities.ConversionActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalEditText;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityConvertDetailBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.CheckWaletOtp;
import com.vynkpay.retrofit.model.SendWaletOtp;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertDetailActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityConvertDetailBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    Dialog serverDialog;
    String amount = "",convertedAmount="";
    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_convert_detail);
        activity = ConvertDetailActivity.this;
        serverDialog = M.showDialog(ConvertDetailActivity.this, "", false, false);
        if (getIntent().hasExtra("amount")) {
            amount = getIntent().getStringExtra("amount");
        }
        binding.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+amount);
        binding.tvBalance.setText(Functions.CURRENCY_SYMBOL+amount);
        if (getIntent().hasExtra("convertedAmount")){
            convertedAmount = getIntent().getStringExtra("convertedAmount");
        }
        binding.tvConvertedAmount.setText(Functions.CURRENCY_SYMBOL+convertedAmount);
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
            submit(convertedAmount);
        }
    }

    private void submit(String amount) {
        serverDialog.show();
        MainApplication.getApiService().getWalletOtp(Prefes.getAccessToken(activity)).enqueue(new Callback<SendWaletOtp>() {
            @Override
            public void onResponse(Call<SendWaletOtp> call, Response<SendWaletOtp> response) {
                if(response.isSuccessful() && response.body()!=null){
                    serverDialog.dismiss();
                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ConvertDetailActivity.this,ConversionOtpActivity.class).putExtra("amount",amount));
                    ConvertDetailActivity.this.finish();
                }
                else{
                    serverDialog.dismiss();
                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SendWaletOtp> call, Throwable t) {
                serverDialog.dismiss();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}