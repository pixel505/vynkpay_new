package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.vynkpay.BuildConfig;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityKycSuccessActiviyBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class KycSuccessActiviy extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
  ActivityKycSuccessActiviyBinding binding;
  KycSuccessActiviy ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kyc_success_activiy);
        ac = KycSuccessActiviy.this;
        click();
    }

    private void click() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("KYC");



        if (getIntent() .getStringExtra("bankStatus")!= null) {
            if(getIntent().getStringExtra("bankStatus").equals("1")){
                String AccountType=getIntent().getStringExtra("AccountType");
                String NameInBank=getIntent().getStringExtra("NameInBank");
                String AccountNumber=getIntent().getStringExtra("AccountNumber");
                String IfscCode=getIntent().getStringExtra("IfscCode");

                String BankName=getIntent().getStringExtra("BankName");
                String BranchAddress=getIntent().getStringExtra("BranchAddress");
                String ChequeReceipt=getIntent().getStringExtra("ChequeReceipt");
                binding.nextLnBank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(KycSuccessActiviy.this, KycBankRejectedActivity.class)
                                .putExtra("AccountType", AccountType)
                                .putExtra("NameInBank",NameInBank )
                                .putExtra("AccountNumber",AccountNumber )
                                .putExtra("IfscCode",IfscCode )
                                .putExtra("BankName", BankName)
                                .putExtra("BranchAddress", BranchAddress)
                                .putExtra("ChequeReceipt", ChequeReceipt)
                                .putExtra("status", getIntent().getStringExtra("bankStatus"))

                        );
                        finish();
                    }
                });

                String nationIdStatus = getIntent().getStringExtra("nationIdStatus");
                String addressProffStatus = getIntent().getStringExtra("addressProffStatus");
                String addressProffPath = getIntent().getStringExtra("addressProffPath");
                String nationProffPath = getIntent().getStringExtra("nationProffPath");

                binding.aadharImage.setVisibility(View.VISIBLE);
                binding.uploadAdharLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + addressProffPath).into(binding.aadharImage);
                binding.panImage.setVisibility(View.VISIBLE);
                binding.uploadPanLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + nationProffPath).into(binding.panImage);
            }


            if(getIntent().getStringExtra("bankStatus").equals("2")){
                String AccountType=getIntent().getStringExtra("AccountType");
                String NameInBank=getIntent().getStringExtra("NameInBank");
                String AccountNumber=getIntent().getStringExtra("AccountNumber");
                String IfscCode=getIntent().getStringExtra("IfscCode");

                String BankName=getIntent().getStringExtra("BankName");
                String BranchAddress=getIntent().getStringExtra("BranchAddress");
                String ChequeReceipt=getIntent().getStringExtra("ChequeReceipt");
                binding.nextLnBank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(KycSuccessActiviy.this, KycBankRejectedActivity.class)
                                .putExtra("AccountType", AccountType)
                                .putExtra("NameInBank",NameInBank )
                                .putExtra("AccountNumber",AccountNumber )
                                .putExtra("IfscCode",IfscCode )
                                .putExtra("BankName", BankName)
                                .putExtra("BranchAddress", BranchAddress)
                                .putExtra("ChequeReceipt", ChequeReceipt)
                                .putExtra("status", getIntent().getStringExtra("bankStatus"))

                        );
                        finish();
                    }
                });

                String nationIdStatus = getIntent().getStringExtra("nationIdStatus");
                String addressProffStatus = getIntent().getStringExtra("addressProffStatus");
                String addressProffPath = getIntent().getStringExtra("addressProffPath");
                String nationProffPath = getIntent().getStringExtra("nationProffPath");

                binding.aadharImage.setVisibility(View.VISIBLE);
                binding.uploadAdharLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + addressProffPath).into(binding.aadharImage);
                binding.panImage.setVisibility(View.VISIBLE);
                binding.uploadPanLn.setEnabled(false);
                Log.d("gsgdjasgdasasd", BuildConfig.BASE_URL+"account/"+nationProffPath);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + nationProffPath).into(binding.panImage);
            }

            else if(getIntent().getStringExtra("bankStatus").equals("4"))  {

                binding.nextLnBank.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(KycSuccessActiviy.this, KycBankActivity.class).putExtra("resubmit","1"));
                        finish();
                    }
                });

                String nationIdStatus = getIntent().getStringExtra("nationIdStatus");
                String addressProffStatus = getIntent().getStringExtra("addressProffStatus");
                String addressProffPath = getIntent().getStringExtra("addressProffPath");
                String nationProffPath = getIntent().getStringExtra("nationProffPath");

                binding.aadharImage.setVisibility(View.VISIBLE);
                binding.uploadAdharLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + addressProffPath).into(binding.aadharImage);
                binding.panImage.setVisibility(View.VISIBLE);
                binding.uploadPanLn.setEnabled(false);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + nationProffPath).into(binding.panImage);
            }



        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(KycSuccessActiviy.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(KycSuccessActiviy.this,KycSuccessActiviy.this::finishAffinity);
        }
    }
}
