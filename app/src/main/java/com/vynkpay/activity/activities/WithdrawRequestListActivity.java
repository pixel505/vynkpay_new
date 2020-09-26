package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.adapter.WalletRequestAdapter;
import com.vynkpay.databinding.ActivityWithdrawRequestListBinding;
import com.vynkpay.models.WithdrawalRequestModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.Functions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class WithdrawRequestListActivity extends AppCompatActivity {

    ActivityWithdrawRequestListBinding binding;
    ArrayList<WithdrawalRequestModel> withdrawalRequestModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_withdraw_request_list);
        dev();
    }

    private void dev() {
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbar.notifyIcon.setVisibility(View.INVISIBLE);
        binding.toolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.toolbar.toolbarTitle.setText(getString(R.string.withdrawalRequest));

        getRequestsList();

    }

    private void getRequestsList() {
        binding.noLayout.setVisibility(View.GONE);
        binding.progressFrame.setVisibility(View.VISIBLE);
        ApiCalls.withdrawalRequestsList(WithdrawRequestListActivity.this, Prefes.getAccessToken(WithdrawRequestListActivity.this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {

                Log.d("dsdasdaACcoutnt", result);

                binding.progressFrame.setVisibility(View.GONE);
                withdrawalRequestModelArrayList.clear();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray list_history = data.getJSONArray("list_history");
                        if (list_history.length()<=0){
                            binding.noLayout.setVisibility(View.VISIBLE);
                        }else {
                            binding.noLayout.setVisibility(View.GONE);
                        }

                        for (int i=0; i<list_history.length(); i++){
                            JSONObject object = list_history.getJSONObject(i);
                            String id = object.getString("id");
                            String user_id = object.getString("user_id");
                            String type = object.getString("type");
                            String amount_wt  = object.getString("amount_wt");
                            String amount = object.getString("amount");
                            String admin_chrg = object.getString("admin_chrg");
                            String admin_chrg_amount = object.getString("admin_chrg_amount");
                            String tds  = object.getString("tds");
                            String tds_amount  = object.getString("tds_amount");
                            String other_fee_amount  = object.getString("other_fee_amount");
                            String shopping  = object.getString("shopping");
                            String shopping_amount = object.getString("shopping_amount");
                            String invoice_number = object.getString("invoice_number");
                            String description = object.getString("description");
                            String name_in_bank = object.getString("name_in_bank");
                            String bank_name = object.getString("bank_name");
                            String branch_address = object.getString("branch_address");
                            String account_number = object.getString("account_number");
                            String ifsc_code = object.getString("ifsc_code");
                            String mode = object.getString("mode");
                            String bit_address = object.getString("bit_address");
                            String created_date = object.getString("created_date");
                            String isactive = object.getString("isactive");

                            withdrawalRequestModelArrayList.add(new WithdrawalRequestModel( id,  user_id,  type,  amount_wt,  amount,
                                    admin_chrg,  admin_chrg_amount,  tds,  tds_amount,  other_fee_amount,
                                    shopping,  shopping_amount,  invoice_number,  description,
                                    name_in_bank,  bank_name,  branch_address,  account_number,
                                    ifsc_code,  mode,  bit_address,  created_date, isactive, false));
                        }

                        binding.recyclerView.setLayoutManager(Functions.layoutManager(WithdrawRequestListActivity.this, Functions.VERTICAL, 0));
                        binding.recyclerView.setAdapter(new WalletRequestAdapter(WithdrawRequestListActivity.this, withdrawalRequestModelArrayList));

                    }else {
                        binding.noLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(WithdrawRequestListActivity.this, jsonObject.getString("message")+"", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    binding.noLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String error) {
                binding.progressFrame.setVisibility(View.GONE);
                binding.noLayout.setVisibility(View.VISIBLE);
            }
        });
    }
}
