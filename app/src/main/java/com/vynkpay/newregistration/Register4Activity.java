package com.vynkpay.newregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.databinding.ActivityRegister4Binding;

public class Register4Activity extends AppCompatActivity implements View.OnClickListener {

    ActivityRegister4Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register4);
        binding.loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.loginButton){
            startActivity(new Intent(Register4Activity.this, LoginActivity.class));
        }
    }
}