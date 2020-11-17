package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import com.bumptech.glide.Glide;
import com.vynkpay.R;
import com.vynkpay.databinding.ActivityWatchFileBinding;

public class WatchFileActivity extends AppCompatActivity {
       ActivityWatchFileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
}
