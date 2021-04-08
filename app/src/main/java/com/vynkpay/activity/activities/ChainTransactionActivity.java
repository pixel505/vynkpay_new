package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.vynkpay.R;
import com.vynkpay.activity.ChainTransactionModel;
import com.vynkpay.adapter.TokenTransAdapter;
import com.vynkpay.databinding.ActivityChainTransactionBinding;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChainTransactionActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityChainTransactionBinding binding;
    ArrayList<ChainTransactionModel> chainTransactionModels = new ArrayList<>();
    TokenTransAdapter tokenTransAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chain_transaction);
        dev();
        clicks();
    }

    private void clicks() {
        binding.backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.toolbarTitle.setText("Transactions");
    }


    private void dev() {
        binding.transfertoken.setOnClickListener(this);
        binding.chainRV.setLayoutManager(Functions.layoutManager(this, Functions.VERTICAL, 0));
        tokenTransAdapter = new TokenTransAdapter(chainTransactionModels, this);
        binding.chainRV.setAdapter(tokenTransAdapter);

        binding.progress.setVisibility(View.VISIBLE);
        ApiCalls.getChainTransactions(this, Prefes.getAccessToken(this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                binding.progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray listing = data.getJSONArray("listing");
                        chainTransactionModels.clear();
                        for (int i=0; i<listing.length(); i++){
                            JSONObject object = listing.getJSONObject(i);
                            String id = object.optString("id");
                            String user_id = object.optString("user_id");
                            String statusString = object.optString("status");
                            String price = object.optString("price");
                            String total_price = object.optString("total_price");
                            String coin_price = object.optString("coin_price");
                            String total_coin = object.optString("total_coin");
                            String invoice_number = object.optString("invoice_number");
                            String created_date = object.optString("created_date");

                            JSONObject jsonObject1= new JSONObject(object.getString("other_detail"));
                            chainTransactionModels.add(new ChainTransactionModel( id,  user_id,  statusString,  price,  total_price,  jsonObject1.optString("amount_usd_payable"),  total_coin,  invoice_number, created_date));
                        }

                        tokenTransAdapter.notifyDataSetChanged();

                    }else {
                        Toast.makeText(ChainTransactionActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                binding.progress.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == binding.transfertoken){
            startActivity(new Intent(this, TransferToken.class));
            //startActivity(new Intent(this, TokenTrnsfrHistory.class));
        }
    }
}