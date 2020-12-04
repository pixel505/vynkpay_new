package com.vynkpay.newregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.vynkpay.R;
import com.vynkpay.activity.Splash;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.databinding.ActivitySelectionBinding;
import com.vynkpay.prefes.Prefes;

public class SelectionActivity extends AppCompatActivity implements View.OnClickListener {

    ActivitySelectionBinding binding;
    Prefes prefes;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_selection);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
        binding.linGlobal.setOnClickListener(this);
        binding.linIndia.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if (view == binding.linGlobal){
            binding.linGlobal.setBackgroundResource(R.drawable.shadow_button);
            binding.linIndia.setBackgroundResource(R.drawable.register_default_button);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.linIndia.setBackgroundResource(R.drawable.register_default_button);
                    binding.linGlobal.setBackgroundResource(R.drawable.register_default_button);
                    sp.edit().putString("value","Global").apply();
                    callLogin();
                }
            },300);
        }

        if (view == binding.linIndia){
            binding.linGlobal.setBackgroundResource(R.drawable.register_default_button);
            binding.linIndia.setBackgroundResource(R.drawable.shadow_button);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.linGlobal.setBackgroundResource(R.drawable.register_default_button);
                    binding.linIndia.setBackgroundResource(R.drawable.register_default_button);
                    sp.edit().putString("value","India").apply();
                    callLogin();

                }
            },300);
        }

    }


    private void callLogin() {
        startActivity(new Intent(SelectionActivity.this, LoginActivity.class));
        SelectionActivity.this.finish();
    }

}