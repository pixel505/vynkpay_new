package com.vynkpay.activity.activitiesnew;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vynkpay.R;
import com.vynkpay.custom.NormalTextView;
import com.vynkpay.databinding.ActivityTranferWalletBinding;
import com.vynkpay.fragment.BonusWalletFragment;
import com.vynkpay.fragment.VCashWalletFragment;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class TranferWalletActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityTranferWalletBinding binding;
    Toolbar toolbar;
    NormalTextView toolbarTitle;
    Dialog serverDialog;
    public static String bonusBalance = "",vCashBalance="",mCashBalance="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_tranfer_wallet);
        serverDialog = M.showDialog(TranferWalletActivity.this, "", false, false);
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarTitle.setText(getString(R.string.transferWallet));
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getBonusTransaction();
        getMCashTransaction();
        getVCashTransaction();
        binding.crdBouns.setOnClickListener(this);
        binding.crdMCash.setOnClickListener(this);
        binding.crdVCash.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == binding.crdBouns){
            startActivity(new Intent(TranferWalletActivity.this,TransferWalletProcessActivity.class));
        }

        if (view == binding.crdMCash){
            startActivity(new Intent(TranferWalletActivity.this,TransferWalletMCashActivity.class));
        }

        if (view == binding.crdVCash){
            Log.d("vchasssh","click");
        }


    }

    public void getBonusTransaction(){
        serverDialog.show();
        ApiCalls.getBonusTransactions(TranferWalletActivity.this, Prefes.getAccessToken(TranferWalletActivity.this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                serverDialog.dismiss();
                Log.d("transactionZLog", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        bonusBalance=dataObject.getString("walletBalance");
                        binding.tvBonusBalance.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));


                    }else {
                        serverDialog.dismiss();
                        //finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onError(String error) {
                serverDialog.dismiss();
            }
        });
    }
    private void getVCashTransaction(){
        serverDialog.show();
        ApiCalls.getVcashTransactions(TranferWalletActivity.this, Prefes.getAccessToken(TranferWalletActivity.this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("transactionZLog", result + "//");
                serverDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")) {

                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        vCashBalance=dataObject.getString("walletBalance");
                        binding.tvVCashBalance.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));

                        JSONArray listingArray = dataObject.getJSONArray("listing");
                        for (int i = 0; i < listingArray.length(); i++) {
                            JSONObject object = listingArray.getJSONObject(i);
                            String id = object.getString("id");
                            String front_user_id = object.getString("front_user_id");
                            String user_id = object.getString("user_id");
                            String type = object.getString("type");
                            String payment_via = object.getString("payment_via");
                            String p_amount = object.getString("p_amount");
                            String profit_type = object.getString("profit_type");
                            String mode = object.getString("mode");
                            String transactionStatus = object.getString("status");
                            String created_date = object.getString("created_date");
                            String username = object.getString("username");
                            String email = object.getString("email");
                            String phone = object.getString("phone");
                            String name = object.getString("name");
                            String paid_status = object.getString("paid_status");
                            String balance = object.getString("balance");
                            String frontusername = object.getString("frontusername");

                            VCashWalletFragment.walletTransactionsModelArrayList.add(new WalletTransactionsModel(id, front_user_id, user_id, type,
                                    payment_via, p_amount, profit_type, mode, transactionStatus, created_date, username,
                                    email, phone, name, paid_status, balance, frontusername));
                        }

                        Collections.reverse(VCashWalletFragment.walletTransactionsModelArrayList);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(String error) {
                serverDialog.dismiss();
            }

        });
    }
    private void getMCashTransaction(){
        serverDialog.show();
        ApiCalls.getMcashTransactions(TranferWalletActivity.this, Prefes.getAccessToken(TranferWalletActivity.this), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                //  Log.d("tmcashtrrr", result+"//");
                serverDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){

                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        mCashBalance=dataObject.getString("walletBalance");
                        binding.tvMCashBalance.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                serverDialog.dismiss();
            }
        });
    }


}