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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register3);
        binding.submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            startActivity(new Intent(Register3Activity.this,Register4Activity.class));
        }
    }
}