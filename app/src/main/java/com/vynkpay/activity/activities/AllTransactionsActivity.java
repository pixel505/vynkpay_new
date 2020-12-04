package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import com.vynkpay.R;
import com.vynkpay.adapter.WalletTransactionAdapter;
import com.vynkpay.databinding.ActivityAllTransactionsBinding;
import com.vynkpay.models.WalletTransactionsModel;

import java.util.ArrayList;

public class AllTransactionsActivity extends AppCompatActivity {

    ActivityAllTransactionsBinding binding;
    String tabType;
    public static ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_all_transactions);
        dev();
    }

    private void dev() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.toolbar.notifyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AllTransactionsActivity.this, NotificationActivity.class));
            }
        });

        Intent intent=getIntent();
        if (intent != null){
            tabType = intent.getStringExtra("tabType");

            if (tabType.equals("bonus")){
                binding.toolbar.toolbarTitle.setText("Earning transactions");
            }else if (tabType.equals("vCash")){
                binding.toolbar.toolbarTitle.setText("Vcash transactions");
            }else if (tabType.equals("mCash")){
                binding.toolbar.toolbarTitle.setText("Mcash transactions");
            }else if (tabType.equals("eCash")){
                binding.toolbar.toolbarTitle.setText("Mcash Requests");
            }
        }

        View view=LayoutInflater.from(AllTransactionsActivity.this).inflate(R.layout.empty_space_layout, null);
        binding.transactionsListView.addHeaderView(view);
        binding.transactionsListView.setAdapter(new WalletTransactionAdapter(AllTransactionsActivity.this, walletTransactionsModelArrayList, true));
    }
}
