package com.vynkpay.activity.activitiesnew.loadmcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityPaymentMethodMcashBinding;
import com.vynkpay.utils.Functions;

public class PaymentMethodMcashActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityPaymentMethodMcashBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    String amount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment_method_mcash);
        amount = getIntent().getStringExtra("amount");
        binding.tvAmontBal.setText(Functions.CURRENCY_SYMBOL+amount);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText("Load MCash");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.submitButton.setOnClickListener(this);
        initListner();
        if (Functions.isIndian){
            binding.linIndian.setVisibility(View.VISIBLE);
            binding.linInternationl.setVisibility(View.GONE);
        }else {
            binding.linIndian.setVisibility(View.GONE);
            binding.linInternationl.setVisibility(View.VISIBLE);
        }
    }

    void initListner(){
        binding.btnCoinbase.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    binding.btnPayeer.setChecked(false);
                }
            }
        });

        binding.btnPayeer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    binding.btnCoinbase.setChecked(false);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            if(Functions.isIndian){
                if (!binding.btnRozarpay.isChecked()){
                    Toast.makeText(PaymentMethodMcashActivity.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                }else {
                    startActivity(new Intent(PaymentMethodMcashActivity.this,LoadMcashSuccessActivity.class));
                }
            }else {
                if (binding.btnCoinbase.isChecked() || binding.btnPayeer.isChecked()){
                    startActivity(new Intent(PaymentMethodMcashActivity.this,LoadMcashSuccessActivity.class));
                }else {
                    Toast.makeText(PaymentMethodMcashActivity.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}