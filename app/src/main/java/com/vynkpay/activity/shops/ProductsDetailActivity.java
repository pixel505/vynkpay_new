package com.vynkpay.activity.shops;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.activity.shops.models.Deal;
import com.vynkpay.databinding.ActivityProductsDetailBinding;

public class ProductsDetailActivity extends AppCompatActivity {

    ActivityProductsDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            }

            binding.tvShopOnline.setOnClickListener(view -> {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(dealData.getUrl()));
                    startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            });

        }

    }

}