package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.vynkpay.R;
import com.vynkpay.databinding.ActivityTransferOptionListBinding;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.M;
import com.vynkpay.utils.PlugInControlReceiver;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TransferOptionListActivity extends AppCompatActivity implements PlugInControlReceiver.ConnectivityReceiverListener, View.OnClickListener {

    ActivityTransferOptionListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transfer_option_list);
        dev();
    }

    private void dev() {
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.vynkToken);

        binding.transactionCard.setOnClickListener(this);
        binding.TransferHistoryCard.setOnClickListener(this);

        binding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeLayout.setRefreshing(false);
                getAllData();
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){
            M.showUSBPopUp(TransferOptionListActivity.this, TransferOptionListActivity.this::finishAffinity);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.transactionCard){
            startActivity(new Intent(this, ChainTransactionActivity.class));
        }else if (v == binding.TransferHistoryCard){
            startActivity(new Intent(this, TokenTrnsfrHistory.class));
        }else if (v == binding.TransferVYNCCard){
            startActivity(new Intent(this, TransferToken.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllData();
    }

    public void getAllData(){
        ApiCalls.getChainHistoryTransactions(this, Prefes.getAccessToken(this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("dssdjkhdLOGD", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        if (data.optBoolean("enable_request")){
                            binding.TransferVYNCCard.setOnClickListener(TransferOptionListActivity.this);
                            binding.TransferVYNCCard.setAlpha(1f);
                        }else {
                            binding.TransferVYNCCard.setOnClickListener(null);
                            binding.TransferVYNCCard.setAlpha(0.5f);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {

            }
        });
    }

}