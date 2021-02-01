package com.vynkpay.activity.activitiesnew.conversion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.activity.activitiesnew.TransferSuccessActivity;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityConvertSuccessBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class ConvertSuccessActivity extends AppCompatActivity implements View.OnClickListener, PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityConvertSuccessBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    String message = "";
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_convert_success);
        try {
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
        binding.tvMessage.setText("");
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getString(R.string.convertmcashtext));

            if (getIntent().hasExtra("message")){
                message = getIntent().getStringExtra("message");
                binding.tvMessage.setText(message);
            } else {
                binding.tvMessage.setText("");
            }
            toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch (Exception e){
            binding.tvMessage.setText("");
            e.printStackTrace();
        }
        binding.submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            if (sp.getString("value","").equalsIgnoreCase("Global")){
                startActivity(new Intent(ConvertSuccessActivity.this, HomeActivity.class).putExtra("Country", "Global"));
            }else {
                startActivity(new Intent(ConvertSuccessActivity.this, HomeActivity.class).putExtra("Country", "India"));
            }
            finishAffinity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(ConvertSuccessActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(ConvertSuccessActivity.this,ConvertSuccessActivity.this::finishAffinity);
        }
    }
}