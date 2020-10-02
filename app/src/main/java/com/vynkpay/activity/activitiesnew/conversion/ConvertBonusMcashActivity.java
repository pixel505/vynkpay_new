package com.vynkpay.activity.activitiesnew.conversion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vynkpay.R;
import com.vynkpay.activity.activitiesnew.RequestWithdrawnActivity;
import com.vynkpay.adapter.ConversionAdapter;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityConvertBonusMcashBinding;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.retrofit.model.ConversionResponse;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConvertBonusMcashActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityConvertBonusMcashBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    Dialog serverDialog;
    Activity activity = ConvertBonusMcashActivity.this;
    String miniAmount = "",walletBalance = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_convert_bonus_mcash);
        activity = ConvertBonusMcashActivity.this;
        serverDialog = M.showDialog(ConvertBonusMcashActivity.this, "", false, false);
        getConversionList();
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getString(R.string.convertmcashtext));
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.submitButton){
            if(binding.amountET.getText().toString().isEmpty()){
                Toast.makeText(activity, "Please Enter Amount", Toast.LENGTH_SHORT).show();
            } else if(Float.parseFloat(binding.amountET.getText().toString())<Float.parseFloat(miniAmount)){
                Toast.makeText(activity, "Minimum Amount is "+Functions.CURRENCY_SYMBOL+miniAmount, Toast.LENGTH_SHORT).show();
            }else if(Float.parseFloat(binding.amountET.getText().toString().trim())>Float.parseFloat(walletBalance.trim())){
                Toast.makeText(activity, "This amount is not available in wallet", Toast.LENGTH_SHORT).show();
            }
            else {
                startActivity(new Intent(ConvertBonusMcashActivity.this,ConvertDetailActivity.class).putExtra("amount",walletBalance).putExtra("convertedAmount",binding.amountET.getText().toString().trim()));
                ConvertBonusMcashActivity.this.finish();
                //submit(binding.conversionHeader.searchUserET.getText().toString());
            }
        }
    }

    private void getConversionList() {
        serverDialog.show();
        MainApplication.getApiService().getConversions(Prefes.getAccessToken(activity)).enqueue(new Callback<ConversionResponse>() {
            @Override
            public void onResponse(Call<ConversionResponse> call, Response<ConversionResponse> response) {
                if(response.isSuccessful() && response.body()!=null){
                    serverDialog.dismiss();
                    if(response.body().getStatus().equals("true")){
                        Log.d("convertcash",new Gson().toJson(response.body()));
                        GridLayoutManager manager = new GridLayoutManager(activity, 1, GridLayoutManager.VERTICAL, false);
                        //ConversionAdapter adapter = new ConversionAdapter(activity, response.body().getData().getListing());
                        /*binding.conversionList.setLayoutManager(manager);
                        binding.conversionList.setAdapter(adapter);

                        if (response.body().getData().getListing().size() > 0) {
                            binding.noLayout.setVisibility(View.GONE);
                        } else {
                            binding.noLayout.setVisibility(View.VISIBLE);
                        }*/
                        walletBalance = response.body().getData().getWalletBalance();
                        binding.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+response.body().getData().getWalletBalance());
                        miniAmount=response.body().getData().getMinimumAmount();
                    }

                }
                else {
                    serverDialog.dismiss();
                    Toast.makeText(activity, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ConversionResponse> call, Throwable t) {
                serverDialog.dismiss();
                Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}