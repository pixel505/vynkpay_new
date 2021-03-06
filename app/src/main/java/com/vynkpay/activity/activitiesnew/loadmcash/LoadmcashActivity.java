package com.vynkpay.activity.activitiesnew.loadmcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityLoadmcashBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class LoadmcashActivity extends AppCompatActivity implements View.OnClickListener, PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityLoadmcashBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    NormalButton submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_loadmcash);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        submitButton = findViewById(R.id.submitButton);
        toolbarTitle.setText("Load MCash");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.submitButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(LoadmcashActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            if (TextUtils.isEmpty(binding.amountET.getText().toString().trim())){
                Toast.makeText(LoadmcashActivity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
            }else {
                String amount = binding.amountET.getText().toString();
                startActivity(new Intent(LoadmcashActivity.this, PaymentMethodMcashActivity.class).putExtra("amount",amount));
                LoadmcashActivity.this.finish();
            }
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(LoadmcashActivity.this,LoadmcashActivity.this::finishAffinity);
        }
    }
}