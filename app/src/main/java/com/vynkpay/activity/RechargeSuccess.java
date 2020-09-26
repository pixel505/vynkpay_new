package com.vynkpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.custom.NormalButton;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargeSuccess extends AppCompatActivity {

    @BindView(R.id.loginBtnNew)
    NormalButton loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_success);
        ButterKnife.bind(this);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RechargeSuccess.this, LoginActivity.class));
                finish();
            }
        });
    }
}
