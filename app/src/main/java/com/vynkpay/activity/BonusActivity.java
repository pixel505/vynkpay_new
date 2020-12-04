package com.vynkpay.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.vynkpay.R;
import com.vynkpay.adapter.BonusAdapter;
import com.vynkpay.databinding.ActivityBonusBinding;
import com.vynkpay.models.MyAccount;
import com.vynkpay.utils.Functions;

public class BonusActivity extends AppCompatActivity {

    ActivityBonusBinding binding;
    BonusActivity ac;
    String from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bonus);
        ac = BonusActivity.this;
        if (getIntent().hasExtra("from")){
            from = getIntent().getStringExtra("from");
        }
        Log.d("bonusfrom",from);
        clicks();
    }

    /*On this screen, need to add:
Old Bonuses
It will show to the old users, like web
Some Bonuses from Bonuses section will be migrated here
Leadership Bonus
Ambassador Bonus
Chairman Bonus
Appraisal Bonus
Generation Bonus
Need to add more cards in Bonuses
Change the name of Referral Bonus to First Level Bonus
Performance Bonus
Volume Bonus
Shopping Bonus
Appraisal Bonus
Shopping Bonus
Global Royalty (Before it was Global Pool Bonus, need to change the name only)*/

    private void clicks() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        if (from.equalsIgnoreCase("OldBonuses")){
            binding.toolbarLayout.toolbarTitlenew.setText(R.string.oldbonuses);
        } else if (from.equalsIgnoreCase("VYNCBonuses")){
            binding.toolbarLayout.toolbarTitlenew.setText(R.string.vyncbonuses);
        } else {
            binding.toolbarLayout.toolbarTitlenew.setText(R.string.earning);
        }

        MyAccount[] myAccount;
        if (from.equalsIgnoreCase("Bonuses")){

            if (Functions.isIndian){
                myAccount = new MyAccount[]{
                        new MyAccount("First Level Bonus", R.drawable.referralbonus),
                        new MyAccount("Performance Bonus", R.drawable.performancebonus3x),
                        new MyAccount("Volume Bonus", R.drawable.volumebonus3x),
                        new MyAccount("Appraisal Bonus", R.drawable.appraisal),
                        new MyAccount("Shopping Bonus", R.drawable.shoppingbonus3x)
                        //new MyAccount("Global Royalty", R.drawable.globalpoolbonus)
                };
            }else {
                myAccount = new MyAccount[]{
                        new MyAccount("First Level Bonus", R.drawable.referralbonus),
                        new MyAccount("Performance Bonus", R.drawable.performancebonus3x),
                        new MyAccount("Volume Bonus", R.drawable.volumebonus3x),
                        new MyAccount("Appraisal Bonus", R.drawable.appraisal),
                        new MyAccount("Shopping Bonus", R.drawable.shoppingbonus3x)
                        //new MyAccount("Global Royalty", R.drawable.globalpoolbonus)
                        //,new MyAccount("VYNC Bonuses",R.drawable.vynccbonus3x)
                };
            }



                   /* new MyAccount("Generation Bonus", R.drawable.generationbonus),
                    new MyAccount("Leadership Bonus", R.drawable.leadershipbonu),
                    new MyAccount("Ambassador Bonus", R.drawable.ambassador),
                    new MyAccount("Chairman Bonus", R.drawable.chairman),*/
                    //new MyAccount("Club Bonus", R.drawable.club)
        } else if (from.equalsIgnoreCase("VYNCBonuses")){
            myAccount = new MyAccount[]{
                    new MyAccount("Weekly Bonus", R.drawable.referralbonus),
                    new MyAccount("Referral Bonus", R.drawable.referralbonus),
                    new MyAccount("Performance Bonus", R.drawable.performancebonus3x),
                    new MyAccount("Volume Bonus", R.drawable.volumebonus3x)
            };
        }else {
            myAccount = new MyAccount[]{
                    //new MyAccount("First Level Bonus", R.drawable.referralbonus),
                    //new MyAccount("Global Royalty", R.drawable.globalpoolbonus),
                    new MyAccount("Leadership Bonus", R.drawable.leadershipbonu),
                    new MyAccount("Ambassador Bonus", R.drawable.ambassador),
                    new MyAccount("Chairman Bonus", R.drawable.chairman),
                    new MyAccount("Appraisal Bonus", R.drawable.appraisal),
                    new MyAccount("Generation Bonus", R.drawable.generationbonus)
                    /*new MyAccount("Club Bonus", R.drawable.club)*/
            };
        }

        //Old
        /*MyAccount[] myAccount = new MyAccount[] {
                new MyAccount("First Level Bonus", R.drawable.referralbonus),
                new MyAccount("Generation Bonus", R.drawable.generationbonus),
                new MyAccount("Global Royalty", R.drawable.globalpoolbonus),
                new MyAccount("Leadership Bonus", R.drawable.leadershipbonu),
                new MyAccount("Ambassador Bonus", R.drawable.ambassador),
                new MyAccount("Chairman Bonus", R.drawable.chairman),
                new MyAccount("Appraisal Bonus", R.drawable.appraisal),
                new MyAccount("Club Bonus", R.drawable.club),
        };*/

        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2, GridLayoutManager.VERTICAL, false);
        BonusAdapter adapter = new BonusAdapter(getApplicationContext(), myAccount);
        binding.bonusRecycler.setLayoutManager(manager);
        binding.bonusRecycler.setAdapter(adapter);
    }

}
