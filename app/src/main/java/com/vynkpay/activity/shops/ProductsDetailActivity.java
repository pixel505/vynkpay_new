package com.vynkpay.activity.shops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.activity.shops.models.Deal;
import com.vynkpay.databinding.ActivityProductsDetailBinding;
import com.vynkpay.utils.M;
import com.vynkpay.utils.MySingleton;
import com.vynkpay.utils.PlugInControlReceiver;

public class ProductsDetailActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener {

    ActivityProductsDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this,R.layout.activity_products_detail);
        init();
    }
    void init(){
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductsDetailActivity.this.finish();
            }
        });

        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);

        if (getIntent().hasExtra("allData")){
            String allData = getIntent().getStringExtra("allData");
            Deal dealData = new Gson().fromJson(allData,Deal.class);
            if (dealData!=null){
                binding.toolbarLayout.toolbarTitlenew.setText(dealData.getTitle());
                Glide.with(ProductsDetailActivity.this)
                        .load(dealData.getPath()+dealData.getImage())
                        .into(binding.ivBrandImage);
                binding.tvDescription.setText(dealData.getDescription());
                binding.tvTextTC.setText(dealData.getTerms());
            }

            binding.tvShopOnline.setOnClickListener(view -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(dealData.getUrl()));
                    startActivity(intent);
                } catch (Exception e){
                    e.printStackTrace();
                }
            });

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MySingleton.getInstance(ProductsDetailActivity.this).setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(ProductsDetailActivity.this,ProductsDetailActivity.this::finishAffinity);
        }
    }
}