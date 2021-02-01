package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityWatchFileBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class WatchFileActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {
       ActivityWatchFileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_watch_file);
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.supportchat);

        if(getIntent().getStringExtra("imageLink")!=null){
            Glide.with(this).load(getIntent().getStringExtra("imageLink")).into(binding.watchImage);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(WatchFileActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(WatchFileActivity.this,WatchFileActivity.this::finishAffinity);
        }
    }
}
