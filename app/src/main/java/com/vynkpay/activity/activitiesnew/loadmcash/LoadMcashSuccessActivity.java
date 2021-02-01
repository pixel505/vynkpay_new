package com.vynkpay.activity.activitiesnew.loadmcash;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.vynkpay.R;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityLoadMcashSuccessBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class LoadMcashSuccessActivity extends AppCompatActivity implements View.OnClickListener, PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityLoadMcashSuccessBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    NormalButton submitButton;
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_load_mcash_success);
        if (getIntent().hasExtra("message")){
            message = getIntent().getStringExtra("message");
            if (!TextUtils.isEmpty(message)){
                binding.tvSuccessmessage.setVisibility(View.VISIBLE);
                binding.tvSuccessmessage.setText(message);
            }else {
                binding.tvSuccessmessage.setVisibility(View.GONE);
            }
        }else {
            binding.tvSuccessmessage.setVisibility(View.GONE);
        }
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
        submitButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(LoadMcashSuccessActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == submitButton){

        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(LoadMcashSuccessActivity.this,LoadMcashSuccessActivity.this::finishAffinity);
        }
    }
}