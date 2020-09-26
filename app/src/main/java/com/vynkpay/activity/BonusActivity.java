package com.vynkpay.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.adapter.BonusAdapter;
import com.vynkpay.databinding.ActivityBonusBinding;
import com.vynkpay.models.MyAccount;

public class BonusActivity extends AppCompatActivity {
    ActivityBonusBinding binding;
    BonusActivity ac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bonus);
        ac = BonusActivity.this;
        clicks();
    }

    private void clicks() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.earning);

        MyAccount[] myAccount = new MyAccount[] {
                new MyAccount("Referral Bonus", R.drawable.referralbonus),
                new MyAccount("Generation Bonus", R.drawable.generationbonus),
                new MyAccount("Global Pool Bonus", R.drawable.globalpoolbonus),
                new MyAccount("Leadership Bonus", R.drawable.leadershipbonu),
                new MyAccount("Ambassador Bonus", R.drawable.ambassador),
                new MyAccount("Chairman Bonus", R.drawable.chairman),
                new MyAccount("Appraisal Bonus", R.drawable.appraisal),
                new MyAccount("Club Bonus", R.drawable.club),
        };

        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        BonusAdapter adapter = new BonusAdapter(getApplicationContext(), myAccount);
        binding.bonusRecycler.setLayoutManager(manager);
        binding.bonusRecycler.setAdapter(adapter);
    }
}
