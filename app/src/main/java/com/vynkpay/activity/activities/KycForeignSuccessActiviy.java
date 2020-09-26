package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.vynkpay.BuildConfig;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityKycForeignSuccessActiviyBinding;

public class KycForeignSuccessActiviy extends AppCompatActivity {
   ActivityKycForeignSuccessActiviyBinding binding;
    KycForeignSuccessActiviy ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_kyc_foreign_success_activiy);
        ac = KycForeignSuccessActiviy.this;
        click();
    }


    private void click() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("KYC");





                String nationIdStatus = getIntent().getStringExtra("nationIdStatus");
                String addressProffStatus = getIntent().getStringExtra("addressProffStatus");
                String addressProffPath = getIntent().getStringExtra("addressProffPath");
                String nationProffPath = getIntent().getStringExtra("nationProffPath");

                binding.aadharImage.setVisibility(View.VISIBLE);
                binding.uploadAdharLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + addressProffPath).into(binding.aadharImage);
                binding.panImage.setVisibility(View.VISIBLE);
                binding.uploadPanLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + nationProffPath).into(binding.panImage);
            }





}
