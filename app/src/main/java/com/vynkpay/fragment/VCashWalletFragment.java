package com.vynkpay.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.vynkpay.activity.activitiesnew.TranferWalletActivity;
import com.vynkpay.prefes.Prefes;
import com.vynkpay.retrofit.MainApplication;
import com.vynkpay.utils.Functions;
import com.vynkpay.R;
import com.vynkpay.activity.activities.AllTransactionsActivity;
import com.vynkpay.adapter.WalletTransactionAdapter;
import com.vynkpay.databinding.FragmentVcashWalletBinding;
import com.vynkpay.models.WalletTransactionsModel;
import com.vynkpay.utils.M;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class VCashWalletFragment extends AppCompatActivity {
    FragmentVcashWalletBinding binding;
    public static ArrayList<WalletTransactionsModel> walletTransactionsModelArrayList = new ArrayList<>();
    public static String availableBalance;
    VCashWalletFragment activity;
    Dialog serverDialog;
    String m_wallet_transfer_enable = "", v_wallet_transfer_enable = "", earning_wallet_transfer_enable = "",opt_vcash_enable="";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_vcash_wallet);
        activity=VCashWalletFragment.this;
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        hideKeyboard(activity);
        serverDialog = M.showDialog(activity, "", false, false);
        binding.toolbarLayout.toolbarnew.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        binding.toolbarLayout.toolbarnew.setNavigationIcon(R.drawable.ic_back_arrow);
        binding.toolbarLayout.toolbarTitlenew.setText("VCash Wallet");
        dev();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSettings();
    }

    private void dev() {

        Intent intent= getIntent();
        if (intent!=null){
            if (intent.hasExtra("balancWalletV")){
                if (intent.getStringExtra("balancWalletV")==null){
                    binding.bonusHeader.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+"0");
                }else {
                    binding.bonusHeader.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+intent.getStringExtra("balancWalletV"));
                }
            }else {
                binding.bonusHeader.availableBalanceTV.setText(Functions.CURRENCY_SYMBOL+"0");
            }

        }


        binding.bonusHeader.filterLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "Coming soon", Toast.LENGTH_SHORT).show();
            }
        });


        binding.transactionsListView.setAdapter(new WalletTransactionAdapter(activity, walletTransactionsModelArrayList, false));

        if (walletTransactionsModelArrayList.size()>5){
            binding.bonusHeader.viewAll.setVisibility(View.VISIBLE);
        }else {
            binding.bonusHeader.viewAll.setVisibility(View.GONE);
        }

        if (walletTransactionsModelArrayList.size()>0){
            binding.noLayout.setVisibility(View.GONE);
        }else {
            binding.noLayout.setVisibility(View.VISIBLE);
        }

        binding.bonusHeader.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //AllTransactionsActivity.walletTransactionsModelArrayList = walletTransactionsModelArrayList;
                startActivity(new Intent(activity, AllTransactionsActivity.class).putExtra("tabType", "vCash"));
            }
        });

        binding.bonusHeader.optOutActive.setOnClickListener(view -> {

            Dialog dialog = new Dialog(activity,R.style.myDialog);
            dialog.setContentView(R.layout.custom_vcashopt);
            LinearLayout btnNo = dialog.findViewById(R.id.btnNo);
            LinearLayout btnYes = dialog.findViewById(R.id.btnYes);

            btnNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btnYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    callVcashOptOut();
                }
            });
            dialog.show();
        });

    }


    public void getSettings(){
        MainApplication.getApiService().getTransferSettings(Prefes.getAccessToken(activity)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("settingsresponse",response.body());
                //{"status":true,"data":{"m_wallet_transfer_enable":true,"v_wallet_transfer_enable":true,"earning_wallet_transfer_enable":true},"message":"success"}
                try {
                    JSONObject respData = new JSONObject(response.body());
                    if (respData.getString("status").equalsIgnoreCase("true")){
                        JSONObject data = respData.getJSONObject("data");
                        m_wallet_transfer_enable = data.getString("m_wallet_transfer_enable");
                        v_wallet_transfer_enable = data.getString("v_wallet_transfer_enable");
                        earning_wallet_transfer_enable = data.getString("earning_wallet_transfer_enable");
                        opt_vcash_enable = data.getString("opt_vcash_enable");
                        if (opt_vcash_enable.equalsIgnoreCase("true")){
                            binding.bonusHeader.optOutFrame.setVisibility(View.VISIBLE);
                        } else {
                            binding.bonusHeader.optOutFrame.setVisibility(View.GONE);
                        }
                        if (respData.has("message")){
                            Log.d("settingsresponse",respData.getString("message"));
                        }
                    }else {
                        if (respData.has("message")){
                            Log.d("settingsresponse",respData.getString("message"));
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("settingsresponse",t.getMessage()!=null?t.getMessage():"Error");
            }
        });
    }

    public void callVcashOptOut(){
        serverDialog.show();
        MainApplication.getApiService().optVCashAdd(Prefes.getAccessToken(activity)).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                serverDialog.dismiss();
                try {
                    if (response.isSuccessful() && response.body()!=null){
                        //Log.d("vcashresponse",response.body());
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.getString("status").equalsIgnoreCase("true")){
                            String message = jsonObject.getString("message");
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }else {
                            String message = jsonObject.getString("message");
                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                serverDialog.dismiss();
                Log.d("vcashresponse",t.getMessage() != null ? t.getMessage() : "Error");
            }

        });
    }

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
}
