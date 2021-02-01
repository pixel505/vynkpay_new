package com.vynkpay.activity.activitiesnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.custom.NormalButton;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityTransferSuccessBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class TransferSuccessActivity extends AppCompatActivity implements View.OnClickListener, PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityTransferSuccessBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    NormalButton submitButton;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_transfer_success);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        submitButton = findViewById(R.id.submitButton);
        toolbarTitle.setText(getString(R.string.transferWallet));
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        if(getIntent()!=null){
            if(getIntent().getStringExtra("typ").equals("Bonus")){
                binding.messageText.setText("Earning Transferred Successfully");
                if (getIntent().getStringExtra("invoice_number") !=null){
                    String invoice_number = getIntent().getStringExtra("invoice_number")!=null ? getIntent().getStringExtra("invoice_number") : "1234";
                    Log.d("transactionnn","Transaction number:- "+invoice_number);
                    binding.tvTransationNumber.setText("Transaction number:- "+invoice_number);
                }
                //binding.kycImage.setImageResource(R.drawable.bonuswall);
            }

            if(getIntent().getStringExtra("typ").equals("MCash")){
                binding.messageText.setText("MCash Transferred Successfully");
                if (getIntent().getStringExtra("invoice_number") !=null){
                    String invoice_number = getIntent().getStringExtra("invoice_number")!=null ? getIntent().getStringExtra("invoice_number") : "1234";
                    Log.d("transactionnn","Transaction number:- "+invoice_number);
                    binding.tvTransationNumber.setText("Transaction number:- "+invoice_number);
                }
                //binding.kycImage.setImageResource(R.drawable.mcashwall);
            }

            if(getIntent().getStringExtra("typ").equals("eCash")){
                binding.messageText.setText("ECash Request Submitted Successfully");
                //binding.kycImage.setImageResource(R.drawable.requestcash);
            }

            if(getIntent().getStringExtra("typ").equals("vCash")){
                binding.messageText.setText("VCash Request Submitted Successfully");
                //binding.kycImage.setImageResource(R.drawable.requestcash);
                if (getIntent().getStringExtra("invoice_number") !=null){
                    String invoice_number = getIntent().getStringExtra("invoice_number")!=null ? getIntent().getStringExtra("invoice_number") : "1234";
                    Log.d("transactionnn","Transaction number:- "+invoice_number);
                    binding.tvTransationNumber.setText("Transaction number:- "+invoice_number);
                }
            }
        }
        else {
            Log.d("getintentnull","nulll");
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == submitButton){
            if (sp.getString("value","").equalsIgnoreCase("Global")){
                startActivity(new Intent(TransferSuccessActivity.this, HomeActivity.class).putExtra("Country", "Global"));
            }else {
                startActivity(new Intent(TransferSuccessActivity.this, HomeActivity.class).putExtra("Country", "India"));
            }
            finishAffinity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(TransferSuccessActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(TransferSuccessActivity.this,TransferSuccessActivity.this::finishAffinity);
        }
    }
}