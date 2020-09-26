package com.vynkpay.activity.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.vynkpay.R;
import com.vynkpay.adapter.PackageAdapter;
import com.vynkpay.databinding.ActivityPackageABinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.GetPackageResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PackageAActivity extends AppCompatActivity {
    ActivityPackageABinding binding;
    PackageAActivity ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_package_a);
        ac = PackageAActivity.this;
        getPackageByServer();
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
        //binding.toolbarLayout.toolbarTitlenew.setText(R.string.packagetext);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.purchase);
    }


    public void getPackageByServer() {
        MainApplication.getApiService().getPackage(Prefes.getAccessToken(ac)).enqueue(new Callback<GetPackageResponse>() {
            @Override
            public void onResponse(Call<GetPackageResponse> call, Response<GetPackageResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("true")) {
                        binding.viewPager.setAdapter(new PackageAdapter(ac, response.body().getData().getPackages()));
                        Log.e("data", "" + response.body().getData().getPackages());
                        binding.tabLayout.setViewPager(binding.viewPager);
                        binding.viewPager.startAutoScroll();
                        binding.viewPager.setInterval(5000);
                        binding.viewPager.setStopScrollWhenTouch(true);
                        binding.viewPager.setBorderAnimation(true);
                        binding.viewPager.setScrollDurationFactor(8);
                    } else {
                        Toast.makeText(ac, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetPackageResponse> call, Throwable t) {
                Log.d("errorrr",t.getMessage() != null ? t.getMessage() : "Error");
            }

        });

    }

}
