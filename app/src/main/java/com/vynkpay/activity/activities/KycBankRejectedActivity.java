package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.vynkpay.BuildConfig;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.databinding.ActivityKycBankRejectedBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class KycBankRejectedActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
    ActivityKycBankRejectedBinding binding;
    KycBankRejectedActivity ac;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_kyc_bank_rejected);
        ac = KycBankRejectedActivity.this;
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);


        if ((getIntent().getStringExtra("status")!=null)) {




            if(getIntent().getStringExtra("status").equals("1")){
                binding.bankStatus.setVisibility(View.VISIBLE);
                binding.bankStatus.setText("Pending");
                binding.bankStatus.setTextColor(Color.parseColor("#E4D760"));
                String AccountType = getIntent().getStringExtra("AccountType");
                Log.d("AccountType",AccountType);
                String NameInBank = getIntent().getStringExtra("NameInBank");
                String AccountNumber = getIntent().getStringExtra("AccountNumber");
                String IfscCode = getIntent().getStringExtra("IfscCode");
                String BankName = getIntent().getStringExtra("BankName");
                String BranchAddress = getIntent().getStringExtra("BranchAddress");
                String ChequeReceipt = getIntent().getStringExtra("ChequeReceipt");

                binding.bankType.setText(AccountType);
                binding.benifiName.setText(NameInBank);
                binding.accNo.setText(AccountNumber);
                binding.ifscName.setText(IfscCode);
                binding.bankName.setText(BankName);
                binding.bnkAdName.setText(BranchAddress);


                binding.cheqImage.setVisibility(View.VISIBLE);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + ChequeReceipt).into(binding.cheqImage);
            }


            else if(getIntent().getStringExtra("status").equals("2")){
                binding.bankStatus.setVisibility(View.VISIBLE);

                binding.bankStatus.setText("Approved");
                binding.bankStatus.setTextColor(Color.parseColor("#5EB161"));
                String AccountType = getIntent().getStringExtra("AccountType");
                String NameInBank = getIntent().getStringExtra("NameInBank");
                String AccountNumber = getIntent().getStringExtra("AccountNumber");
                String IfscCode = getIntent().getStringExtra("IfscCode");
                String BankName = getIntent().getStringExtra("BankName");
                String BranchAddress = getIntent().getStringExtra("BranchAddress");
                String ChequeReceipt = getIntent().getStringExtra("ChequeReceipt");

                binding.bankType.setText(AccountType);
                binding.benifiName.setText(NameInBank);
                binding.accNo.setText(AccountNumber);
                binding.ifscName.setText(IfscCode);
                binding.bankName.setText(BankName);
                binding.bnkAdName.setText(BranchAddress);


                binding.cheqImage.setVisibility(View.VISIBLE);
                Glide.with(ac).load(BuildConfig.BASE_URL + "account/" + ChequeReceipt).into(binding.cheqImage);
            }




        }


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


        binding.approveLn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sp.getString("value","").equals("Global") && Prefes.getisIndian(KycBankRejectedActivity.this).equals("NO")){
                    startActivity(new Intent(KycBankRejectedActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }

                else if(sp.getString("value","").equals("Global") && Prefes.getisIndian(KycBankRejectedActivity.this).equals("YES")){
                    startActivity(new Intent(KycBankRejectedActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }


                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(KycBankRejectedActivity.this).equals("YES")){
                    startActivity(new Intent(KycBankRejectedActivity.this, HomeActivity.class).putExtra("Country", "India").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }

                else if(sp.getString("value","").equals("India") && Prefes.getisIndian(KycBankRejectedActivity.this).equals("NO")){
                    startActivity(new Intent(KycBankRejectedActivity.this, HomeActivity.class).putExtra("Country", "Global").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(KycBankRejectedActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(KycBankRejectedActivity.this,KycBankRejectedActivity.this::finishAffinity);
        }
    }
}
