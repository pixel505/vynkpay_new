package com.vynkpay.activity.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.vynkpay.R;
import com.vynkpay.activity.ClubBonusActivity;
import com.vynkpay.activity.HomeActivity;
import com.vynkpay.activity.activitiesnew.RequestWithdrawnActivity;
import com.vynkpay.activity.activitiesnew.TranferWalletActivity;
import com.vynkpay.activity.activitiesnew.WithdrawTypeActivity;
import com.vynkpay.activity.activitiesnew.conversion.ConvertBonusMcashActivity;
import com.vynkpay.activity.activitiesnew.loadmcash.LoadmcashActivity;
import com.vynkpay.databinding.ActivityWalletNewBinding;
import com.vynkpay.fragment.BonusWalletFragment;
import com.vynkpay.fragment.ECashWalletFragment;
import com.vynkpay.fragment.MCashWalletFragment;
import com.vynkpay.fragment.VCashWalletFragment;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.network_classes.ApiCalls;
import com.vynkpay.network_classes.VolleyResponse;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.utils.Functions;
import com.vynkpay.utils.M;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletNewActivity extends AppCompatActivity {
    ActivityWalletNewBinding binding;
    WalletNewActivity ac;
    String bonusBalance,vCashBalance,mCashBalance;
    Dialog serverDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wallet_new);
        ac = WalletNewActivity.this;
        serverDialog = M.showDialog(ac, "", false, false);
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
        binding.toolbarLayout.toolbarTitlenew.setText(getString(R.string.wallets));
       /* if (Prefes.getUserType(WalletNewActivity.this).equalsIgnoreCase("2")) {
            //getBonusTransaction();
            getVCashTransaction();
            getMCashTransaction();
        }else {
            getBonusTransaction();
            getVCashTransaction();
            getMCashTransaction();
        }*/
        getBonusTransaction();
        getVCashTransaction();
        getMCashTransaction();

       /* if (Prefes.getUserType(WalletNewActivity.this).equalsIgnoreCase("2")){
            binding.tvWTitle.setText("Cashback");
            binding.bonusCard.setVisibility(View.GONE);
        }else {
            binding.bonusCard.setVisibility(View.VISIBLE);
            binding.tvWTitle.setText("MCash Wallet");
        }*/

        binding.bonusCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ac,BonusWalletFragment.class).putExtra("balancWallet",bonusBalance));
            }
        });


        binding.vCashCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(ac,VCashWalletFragment.class).putExtra("balancWalletV",vCashBalance));
            }

        });

        binding.mCashCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ac,MCashWalletFragment.class).putExtra("balancWalletM",mCashBalance));
            }
        });

        binding.reqstCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("dfdfdf","called");
                startActivity(new Intent(ac,ECashWalletFragment.class).putExtra("balancWalletM",mCashBalance));
            }
        });


        binding.transferWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("teransferre","click");
                startActivity(new Intent(WalletNewActivity.this, TranferWalletActivity.class));
            }
        });

        binding.reqstWithdrawalCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Functions.isIndian) {
                    //popupWithdrawalAmount();
                    startActivity(new Intent(WalletNewActivity.this, RequestWithdrawnActivity.class));
                }else {
                    //popupWithdrawalAmountIntern();
                    startActivity(new Intent(WalletNewActivity.this, WithdrawTypeActivity.class));
                }
            }
        });

        binding.loadMcash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WalletNewActivity.this, LoadmcashActivity.class));
            }
        });

        if(Functions.isIndian){
            binding.loadMcash.setVisibility(View.VISIBLE);
            binding.transferCard.setVisibility(View.GONE);
            binding.reqstWithdrawalCard.setVisibility(View.VISIBLE);
            binding.transferWallet.setVisibility(View.VISIBLE);
        }

        else {
            binding.loadMcash.setVisibility(View.VISIBLE);
            binding.transferCard.setVisibility(View.VISIBLE);
            binding.reqstWithdrawalCard.setVisibility(View.VISIBLE);
            binding.transferWallet.setVisibility(View.GONE);

        }

        binding.transferCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ac, ConvertBonusMcashActivity.class));
                //startActivity(new Intent(ac,ConversionActivity.class));
            }
        });


    }
    public void getBonusTransaction(){
        serverDialog.show();
        ApiCalls.getBonusTransactions(ac, Prefes.getAccessToken(ac), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("transactionZLog", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                         serverDialog.dismiss();
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                         bonusBalance=dataObject.getString("walletBalance");
                         binding.reqwithtext.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));
                         binding.bonusAvail.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));
                    }else {
                        serverDialog.dismiss();
                        binding.reqwithtext.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+"0");
                        binding.bonusAvail.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+"0");
                        //finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                Log.d("transaction",error);
                   serverDialog.dismiss();
            }
        });
    }
    private void getVCashTransaction(){
        serverDialog.show();
        ApiCalls.getVcashTransactions(ac, Prefes.getAccessToken(ac), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                Log.d("transactionZLog", result + "//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")) {
                        serverDialog.dismiss();
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        vCashBalance=dataObject.getString("walletBalance");
                        binding.vCashAvail.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));

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
                Log.d("transactionZ",error);
            }

        });
    }

    private void getMCashTransaction(){
        serverDialog.show();
        ApiCalls.getMcashTransactions(ac, Prefes.getAccessToken(ac), new VolleyResponse() {
            @Override
            public void onResult(String result, String status, String message) {
                //  Log.d("tmcashtrrr", result+"//");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("status").equals("true")){
                        serverDialog.dismiss();
                        JSONObject dataObject=jsonObject.getJSONObject("data");
                        mCashBalance=dataObject.getString("walletBalance");
                        binding.mCashAvail.setText("Available Balance"+":"+ Functions.CURRENCY_SYMBOL+dataObject.getString("walletBalance"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                serverDialog.dismiss();
                Log.d("tmacashh",error);
            }
        });
    }

    @Override
    protected void onResume() {
        if(serverDialog!=null){
            serverDialog.dismiss();
        }
        super.onResume();
    }
}