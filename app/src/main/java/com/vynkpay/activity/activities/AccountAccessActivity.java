package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.adapter.AccountAdapter;
import com.vynkpay.databinding.ActivityAccountAccessBinding;
import com.vynkpay.models.MyAccount;
import com.vynkpay.utils.CallActivity;

public class AccountAccessActivity extends AppCompatActivity {
    ActivityAccountAccessBinding binding;
    AccountAccessActivity ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        MyAccount[] myAccount = new MyAccount[] {

                new MyAccount("Purchase",R.drawable.packages),
                new MyAccount("Invoice",R.drawable.invoice_icon),
                new MyAccount("Community",R.drawable.mynetwork),
                new MyAccount("Bonuses", R.drawable.bonuses),
                new MyAccount("Wallets", R.drawable.wallets),
                new MyAccount("Withdrawal History", R.drawable.withdrawal),
                //new MyAccount("Community Details", R.drawable.communitydetail),
                new MyAccount("Statement", R.drawable.statement)


        };

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

}
