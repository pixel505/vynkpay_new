package com.vynkpay.newregistration;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.vynkpay.R;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.activity.Splash;
import com.vynkpay.adapter.WelComeAdapter;
import com.vynkpay.databinding.ActivityOnboardingBinding;
import com.vynkpay.fragment.FragmentHome;
import com.vynkpay.fragment.FragmentHomeGlobal;
import com.vynkpay.models.Slider;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class OnboardingActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityOnboardingBinding binding;

    private int NUM_PAGES = 0;
    private int currentPage = 0;
    Handler handler = new Handler();
    Timer swipeTimer = new Timer();
    ArrayList<Slider> slist = new ArrayList<>();
    String country = "";
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_onboarding);
        sp = getSharedPreferences("PREFS_APP_CHECK", Context.MODE_PRIVATE);
        country = getIntent().getStringExtra("Country");
        if (getIntent().getStringExtra("Country").equals("Global")) {
            Log.d("fgfgfgfg",getIntent().getStringExtra("Country"));
        } else if (getIntent().getStringExtra("Country").equals("India")) {
            Log.d("fgfgfgfg",getIntent().getStringExtra("Country"));
        }
        loadData();
        NUM_PAGES = slist.size();

        binding.btnNext.setOnClickListener(this);
        binding.btnSkip.setOnClickListener(this);
        binding.viewPager.setAdapter(new WelComeAdapter(OnboardingActivity.this, slist));
        binding.tabIndicator.setupWithViewPager(binding.viewPager,true);

        auto_Slide();

        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    currentPage = position;
                    binding.btnNext.setText("Next");
                }else if (position == 1){
                    currentPage = position;
                    binding.btnNext.setText("Next");
                }else {
                    currentPage = position;
                    binding.btnNext.setText("Login");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    void loadData(){
        slist.clear();
        slist.add(new Slider(R.drawable.onboard1,"Vynkpay","Get Amazing Cashback on all Recharge and Bill Payments"));
        slist.add(new Slider(R.drawable.onboard2,"Affiliate Marketing"," Get Amazing deals and offers on your favourite brands"));
        slist.add(new Slider(R.drawable.onboard3,"Ikonique Global","It’s time for you to legitimately earn from the network economy."));
    }

    @Override
    public void onClick(View view) {

        if (view == binding.btnNext){
            if (binding.btnNext.getText().toString().equalsIgnoreCase("Login")){
                sp.edit().putString("welcome","yes").apply();
                //startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                startActivity(new Intent(OnboardingActivity.this, HomeActivity.class).putExtra("Country", country));
                OnboardingActivity.this.finish();

            } else {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                currentPage++;
                binding.viewPager.setCurrentItem(currentPage, true);
            }
        }
        if (view == binding.btnSkip){
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            binding.viewPager.setCurrentItem(currentPage++, true);
        }

    }

    final Runnable Update = new Runnable() {
        public void run() {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            binding.viewPager.setCurrentItem(currentPage++, true);
        }
    };

    public void auto_Slide(){

        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);
    }



}