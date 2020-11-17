package com.vynkpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.vynkpay.newregistration.Register2Activity;
import com.vynkpay.R;
import com.vynkpay.activity.activities.LoginActivity;
import com.vynkpay.custom.NormalButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RechargeSuccess extends AppCompatActivity {

    @BindView(R.id.loginBtnNew)
    NormalButton loginBtn;
    String which = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_success);
        ButterKnife.bind(this);
        try {
            if (getIntent().hasExtra("which")){
                which = getIntent().getStringExtra("which");
            } else {
                which = "";
            }
        }catch (Exception e){
            e.printStackTrace();
            which = "";
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (which.equalsIgnoreCase("customer")){
                    startActivity(new Intent(RechargeSuccess.this, Register2Activity.class).putExtra("forType","login"));
                }else {
                    startActivity(new Intent(RechargeSuccess.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }
}
