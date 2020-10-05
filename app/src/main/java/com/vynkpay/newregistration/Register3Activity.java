package com.vynkpay.newregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vynkpay.R;
import com.vynkpay.databinding.ActivityRegister3Binding;

public class Register3Activity extends AppCompatActivity implements View.OnClickListener {

    ActivityRegister3Binding binding;
    String which = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register3);
        if (getIntent().hasExtra("which")){
            which = getIntent().getStringExtra("which");
        }
        if (which != null) {
            if (which.equalsIgnoreCase("customer")){
                binding.tvTitleText.setText(getString(R.string.verifymobile));
                binding.tvTextVerify.setText(getString(R.string.onetimepasswordphone));
            } else {
                binding.tvTitleText.setText(getString(R.string.verifymmail));
                binding.tvTextVerify.setText(getString(R.string.onetimepasswordemail));
            }
        }
        binding.submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            startActivity(new Intent(Register3Activity.this,Register4Activity.class));
        }
    }
}