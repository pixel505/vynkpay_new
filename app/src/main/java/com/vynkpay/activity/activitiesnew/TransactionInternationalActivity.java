package com.vynkpay.activity.activitiesnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vynkpay.R;
import com.vynkpay.adapter.WalletTransactionAdapter;
import com.vynkpay.databinding.ActivityTransactionInternationalBinding;
import com.vynkpay.fragment.BonusWalletFragment;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.M;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class TransactionInternationalActivity extends AppCompatActivity {

    ActivityTransactionInternationalBinding binding;
    TransactionInternationalActivity activity;
    public ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList = new ArrayList<>();
    Dialog serverDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_transaction_international);
        activity = TransactionInternationalActivity.this;
        serverDialog = M.showDialog(activity, "", false, false);
        clicks();

    }

    void clicks(){
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText(R.string.transaction);
        getBonusTransaction();
    }

    public void getBonusTransaction(){
        serverDialog.show();
        walletTransactionsModelArrayList.clear();
        ApiCalls.getBonusTransactions(activity, Prefes.getAccessToken(activity), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("inttransactionZLog", result+"//");
                serverDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        JSONArray listingArray = dataObject.getJSONArray("listing");
                        for (int i=0; i<listingArray.length(); i++){
                            JSONObject object=listingArray.getJSONObject(i);
                            String id = object.getString("id");
                            String front_user_id = object.getString("front_user_id");
                            String user_id  = object.getString("user_id");
                            String type  = object.getString("type");
                            String payment_via = object.getString("payment_via");
                            String p_amount  = object.getString("p_amount");
                            String profit_type = object.getString("profit_type");
                            String mode  = object.getString("mode");
                            String transactionStatus  = object.getString("status");
                            String created_date  = object.getString("created_date");
                            String username = object.getString("username");
                            String email  = object.getString("email");
                            String phone  = object.getString("phone");
                            String name  = object.getString("name");
                            String paid_status  = object.getString("paid_status");
                            String balance  = object.getString("balance");
                            String frontusername = object.getString("frontusername");

                            walletTransactionsModelArrayList.add(new WalletTransactionsModel( id,  front_user_id,  user_id,  type,
                                    payment_via, p_amount,  profit_type,  mode,  transactionStatus,  created_date,  username,
                                    email,  phone,  name,  paid_status,  balance, frontusername));
                        }

                        Collections.reverse(walletTransactionsModelArrayList);
                        WalletTransactionAdapter adapter = new WalletTransactionAdapter(activity, walletTransactionsModelArrayList, false);
                        binding.rvTransList.setAdapter(adapter);

                    } else {
                        Log.d("inttransactionZLog","error");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                serverDialog.dismiss();
                Log.d("inttransactionZLog",error+"");
            }

        });
    }


}