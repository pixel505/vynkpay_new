package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.adapter.AccountAdapter;
import com.vynkpay.databinding.ActivityAccountAccessBinding;
import com.vynkpay.models.MyAccount;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.CallActivity;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class AccountAccessActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityAccountAccessBinding binding;
    AccountAccessActivity ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account_access);
        ac = AccountAccessActivity.this;
        clicks();
    }


    private void clicks() {
        // toolbar
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.myaccount);
        MyAccount[] myAccount;
        if (Prefes.getisIndian(AccountAccessActivity.this).equalsIgnoreCase("YES")){
            myAccount = new MyAccount[] {

                    new MyAccount("Purchase",R.drawable.packages),
                    new MyAccount("Invoice",R.drawable.invoice_icon),
                    new MyAccount("Community",R.drawable.mynetwork),
                   // new MyAccount("Bonuses", R.drawable.bonuses),

                    new MyAccount("VYNC Bonuses", R.drawable.bonuses),

                    //new MyAccount("Old Bonuses", R.drawable.bonuses),
                    new MyAccount("Wallets", R.drawable.wallets),
                    new MyAccount("Withdrawal History", R.drawable.withdrawal),
                    //new MyAccount("Community Details", R.drawable.communitydetail),
                    new MyAccount("Statement", R.drawable.statement)
            };
        }else {
                myAccount = new MyAccount[] {

                        new MyAccount("Purchase",R.drawable.packages),
                        new MyAccount("Invoice",R.drawable.invoice_icon),
                        new MyAccount("Community",R.drawable.mynetwork),
                       // new MyAccount("Bonuses", R.drawable.bonuses),
                        new MyAccount("VYNC Bonuses", R.drawable.bonuses),
                        new MyAccount("Wallets", R.drawable.wallets),
                        new MyAccount("Withdrawal History", R.drawable.withdrawal),
                        //new MyAccount("Community Details", R.drawable.communitydetail),
                        new MyAccount("Statement", R.drawable.statement)
                };
        }


        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        AccountAdapter adapter = new AccountAdapter(getApplicationContext(), myAccount, new CallActivity() {
            @Override
            public void callActivity() {
                startActivity(new Intent(AccountAccessActivity.this, HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });
        binding.accountrecycler.setLayoutManager(manager);
        binding.accountrecycler.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(AccountAccessActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(AccountAccessActivity.this,AccountAccessActivity.this::finishAffinity);
        }
    }
}
