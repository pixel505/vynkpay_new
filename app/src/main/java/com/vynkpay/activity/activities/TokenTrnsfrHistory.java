package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.vynkpay.R;
import com.vynkpay.activity.ChainTransactionModel;
import com.vynkpay.adapter.TokenTransAdapter;
import com.vynkpay.adapter.TransferHistoryAdapter;
import com.vynkpay.databinding.ActivityTokenTrnsfrHistoryBinding;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TokenTrnsfrHistory extends AppCompatActivity implements CancelButtonIntereface {

    ActivityTokenTrnsfrHistoryBinding binding;
    TransferHistoryAdapter transferHistoryAdapter;
    ArrayList<TokenHistoryModel> chainTransactionModelArrayList = new ArrayList<>();
    String tokenSymbol;
    String days;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (M.isScreenshotDisable){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_token_trnsfr_history);
        dev();
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
        binding.toolbarLayout.toolbarTitlenew.setText("VYNC Transfer History");

    }

    private void dev() {
        binding.chainRV.setLayoutManager(Functions.layoutManager(this, Functions.VERTICAL, 0));
        transferHistoryAdapter = new TransferHistoryAdapter(this, chainTransactionModelArrayList, this);
        binding.chainRV.setAdapter(transferHistoryAdapter);

        binding.progress.setVisibility(View.VISIBLE);

        getAllData();

       /* ApiCalls.getChainHistoryTransactions(this, Prefes.getAccessToken(this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("dssdjkhdLOGD", result+"//");
                binding.progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        tokenSymbol = data.getString("tokenSymbol");
                        days = data.optString("days");
                        JSONArray listing = data.getJSONArray("listing");
                        chainTransactionModelArrayList.clear();
                        for (int i=0; i<listing.length(); i++){
                            JSONObject object = listing.getJSONObject(i);
                            String id = object.optString("id");
                            String user_id = object.optString("user_id");
                            String token_amount = object.optString("token_amount");
                            String trx_address = object.optString("trx_address");
                            String statusString = object.optString("status");
                            String txnid = object.optString("txnid");
                            String other_data = object.optString("other_data");
                            String created_at = object.optString("created_at");
                            String updated_at = object.optString("updated_at");
                            String status_txt = object.optString("status_txt");

                            chainTransactionModelArrayList.add(new TokenHistoryModel(tokenSymbol, id, user_id, token_amount,
                                    trx_address, statusString, txnid, other_data, created_at, updated_at, status_txt));
                        }

                        transferHistoryAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                binding.progress.setVisibility(View.GONE);
            }
        });*/
    }

    public void getAllData(){
        ApiCalls.getChainHistoryTransactions(this, Prefes.getAccessToken(this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("dssdjkhdLOGD", result+"//");
                binding.progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        tokenSymbol = data.getString("tokenSymbol");
                        days = data.optString("days");
                        JSONArray listing = data.getJSONArray("listing");
                        chainTransactionModelArrayList.clear();
                        for (int i=0; i<listing.length(); i++){
                            JSONObject object = listing.getJSONObject(i);
                            String id = object.optString("id");
                            String user_id = object.optString("user_id");
                            String token_amount = object.optString("token_amount");
                            String trx_address = object.optString("trx_address");
                            String statusString = object.optString("status");
                            String txnid = object.optString("txnid");
                            String other_data = object.optString("other_data");
                            String created_at = object.optString("created_at");
                            String updated_at = object.optString("updated_at");
                            String status_txt = object.optString("status_txt");

                            chainTransactionModelArrayList.add(new TokenHistoryModel(tokenSymbol, id, user_id, token_amount,
                                    trx_address, statusString, txnid, other_data, created_at, updated_at, status_txt));
                        }

                        transferHistoryAdapter.notifyDataSetChanged();
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
    public void onClickCancel(String id) {
        // call cancel api
        ApiCalls.cancelTransferToken(this, Prefes.getAccessToken(this), id, new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("sdhjgscheckCal", result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    getAllData();

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