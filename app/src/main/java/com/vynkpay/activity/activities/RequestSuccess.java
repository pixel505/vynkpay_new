package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.vynkpay.R;
import com.vynkpay.databinding.ActivityRequestSuccessBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class RequestSuccess extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityRequestSuccessBinding binding;
    RequestSuccess ac;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_request_success);
        ac = RequestSuccess.this;
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("Success");


        if(getIntent()!=null){
            if(getIntent().getStringExtra("typ").equals("Bonus")){
                binding.messageText.setText("Bonus Transferred Successfully");
                 binding.kycImage.setImageResource(R.drawable.bonuswall);
            }

            if(getIntent().getStringExtra("typ").equals("MCash")){
                binding.messageText.setText("MCash Transferred Successfully");
                binding.kycImage.setImageResource(R.drawable.mcashwall);
            }

            if(getIntent().getStringExtra("typ").equals("eCash")){
                binding.messageText.setText("ECash Request Submitted Successfully");
                binding.kycImage.setImageResource(R.drawable.requestcash);
            }




        }
        else {

        }

       binding.homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(RequestSuccess.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(RequestSuccess.this,RequestSuccess.this::finishAffinity);
        }
    }
}