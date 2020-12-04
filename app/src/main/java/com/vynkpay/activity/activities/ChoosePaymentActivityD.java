package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.databinding.ActivityChoosePaymentDBinding;
import com.vynkpay.prefes.Prefes;

public class ChoosePaymentActivityD extends AppCompatActivity {
    ChoosePaymentActivityD ac;
    ActivityChoosePaymentDBinding binding;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_payment_d);
        ac = ChoosePaymentActivityD.this;
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);

        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.purchase);
        binding.home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(sp.getString("value","").equals("Global") && Prefes.getisIndian(ac).equals("NO")){
                    startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "Global"));
                    finish();
                }

                else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(ac).equals("YES")){
                    startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "India"));
                    finish();
                }


                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(ac).equals("YES")){
                    startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "India"));
                    finish();
                }

                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(ac).equals("NO")){
                    startActivity(new Intent(ac, HomeActivity.class).putExtra("Country", "Global"));
                    finish();
                }

            }
        });
    }
}