package com.vynkpay.newregistration;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.vynkpay.R;
import com.vynkpay.activity.activities.Signupnew;
import com.vynkpay.databinding.ActivityRegister1Binding;
import com.vynkpay.utils.M;

public class Register1Activity extends AppCompatActivity implements View.OnClickListener {

    ActivityRegister1Binding binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register1);
        binding.linCustomer.setOnClickListener(this);
        binding.linAffiliate.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        if (view == binding.linCustomer){
            binding.linCustomer.setBackgroundResource(R.drawable.shadow_button);
            binding.linAffiliate.setBackgroundResource(R.drawable.register_default_button);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Register1Activity.this,Register2Activity.class).putExtra("which","customer"));
                }
            },400);
        }
        if (view == binding.linAffiliate){
            binding.linCustomer.setBackgroundResource(R.drawable.register_default_button);
            binding.linAffiliate.setBackgroundResource(R.drawable.shadow_button);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Register1Activity.this,Register2Activity.class).putExtra("which","affiliate"));
                }
            },400);
        }

    }

    /* override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }*/

}
